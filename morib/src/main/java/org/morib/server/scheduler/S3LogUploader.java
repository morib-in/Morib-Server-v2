package org.morib.server.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3LogUploader {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.log-path-prefix}")
    private String logPathPrefix;

    private static final Pattern LOG_FILE_PATTERN = Pattern.compile("error\\.(\\d{4}-\\d{2}-\\d{2})\\.log");
    private static final DateTimeFormatter DATE_FORMATTER_YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter S3_PATH_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    // 임시 설정: 백로그 처리를 위해 5분마다 실행
    // 원래 설정: @Scheduled(cron = "0 0 5 * * *", zone = "Asia/Seoul")
    @Scheduled(cron = "0 */5 * * * *", zone = "Asia/Seoul")
    public void uploadLogFilesToS3() { // 메서드 이름도 임시로 변경
        Path logsDirectory = Paths.get("./logs");

        if (!Files.isDirectory(logsDirectory)) {
            log.warn("Logs directory not found: {}", logsDirectory.toAbsolutePath());
            return;
        }

        log.info("Scanning log directory: {}", logsDirectory.toAbsolutePath());

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDirectory, "error.*.log")) {
            for (Path localLogFilePath : stream) {
                if (Files.isRegularFile(localLogFilePath)) {
                    String logFileName = localLogFilePath.getFileName().toString();
                    Matcher matcher = LOG_FILE_PATTERN.matcher(logFileName);

                    if (matcher.matches()) {
                        String dateString = matcher.group(1);
                        try {
                            LocalDate logDate = LocalDate.parse(dateString, DATE_FORMATTER_YYYY_MM_DD);
                            // --- S3 업로드 로직 시작 ---
                            String s3ObjectKey = buildS3ObjectKey(logDate, logFileName);
                            log.info("Found log file {}. Uploading to S3 bucket '{}' with key '{}'", logFileName, bucketName, s3ObjectKey);
                            try {
                                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                        .bucket(bucketName)
                                        .key(s3ObjectKey)
                                        .build();

                                s3Client.putObject(putObjectRequest, RequestBody.fromFile(localLogFilePath));

                                log.info("Successfully uploaded log file {} to S3.", logFileName);

                                // 선택 사항: 업로드 성공 후 로컬 파일 삭제 (백로그 처리 시에는 주의해서 사용)
                                // deleteLocalFile(localLogFilePath);

                            } catch (S3Exception e) {
                                log.error("Error uploading log file {} to S3: {}", logFileName, e.awsErrorDetails().errorMessage(), e);
                            } catch (Exception e) {
                                log.error("An unexpected error occurred during S3 upload of {}: {}", logFileName, e.getMessage(), e);
                            }
                            // --- S3 업로드 로직 끝 ---
                        } catch (DateTimeParseException e) {
                            log.warn("Could not parse date from log file name: {}", logFileName);
                        }
                    } else {
                        log.warn("Filename {} does not match expected pattern.", logFileName);
                    }
                }
            }
            log.info("Finished scanning log directory.");
        } catch (IOException e) {
            log.error("Error reading log directory: {}", logsDirectory.toAbsolutePath(), e);
        }
    }

    private String buildS3ObjectKey(LocalDate date, String logFileName) {
        // 예: logs/2024/07/29/error.2024-07-29.log
        return logPathPrefix +
                date.format(S3_PATH_DATE_FORMATTER) +
                "/" +
                logFileName;
    }

    private void deleteLocalFile(Path filePath) {
        try {
            Files.delete(filePath);
            log.info("Successfully deleted local log file: {}", filePath);
        } catch (IOException e) {
            log.error("Failed to delete local log file {}: {}", filePath, e.getMessage(), e);
        }
    }
}