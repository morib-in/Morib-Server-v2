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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3LogUploader {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.log-path-prefix}")
    private String logPathPrefix;

    @Scheduled(cron = "0 0 5 * * *", zone = "Asia/Seoul")
    public void uploadYesterdayLogErrorLogToS3() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String datePattern = "yyyy-MM-dd";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
        String formattedYesterday = yesterday.format(dateFormatter);

        String logFileName = "error." + formattedYesterday + ".log";
        Path localLogFilePath = Paths.get("./logs/", logFileName);

        log.info("Checking for yesterday's log file: {}", localLogFilePath);

        if (Files.exists(localLogFilePath)) {
            String s3ObjectKey = buildS3ObjectKey(yesterday, logFileName);
            log.info("Found log file {}. Uploading to S3 bucket '{}' with key '{}'", logFileName, bucketName, s3ObjectKey);
            try {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(s3ObjectKey)
                        .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromFile(localLogFilePath));

                log.info("Successfully uploaded log file {} to S3.", logFileName);

                // Optional: Uncomment to delete local file after successful upload
                // deleteLocalFile(localLogFilePath);

            } catch (S3Exception e) {
                log.error("Error uploading log file {} to S3: {}", logFileName, e.awsErrorDetails().errorMessage(), e);
            } catch (Exception e) {
                log.error("An unexpected error occurred during S3 upload of {}: {}", logFileName, e.getMessage(), e);
            }
        } else {
            log.info("Log file {} not found locally, skipping upload.", logFileName);
        }
    }

    private String buildS3ObjectKey(LocalDate date, String logFileName) {
        DateTimeFormatter s3PathDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return logPathPrefix +
               date.format(s3PathDateFormatter) +
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