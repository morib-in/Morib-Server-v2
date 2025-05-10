package org.morib.server.global.common; // 패키지 경로는 실제 프로젝트에 맞게 확인해주세요.

import lombok.RequiredArgsConstructor;
import org.morib.server.global.exception.InvalidStateException;
import org.morib.server.global.message.ErrorMessage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/s3")
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // @Value("${cloud.aws.s3.uri}")
    // private String baseURI;

    private final S3Presigner s3Presigner;

    @GetMapping("/presigned-url") // 메소드명 및 엔드포인트 변경 제안
    public List<S3Info> getInstallerDownloadPresignedUrls() { // 메소드명 변경 제안
        String keyPrefix = "morib-installer/";

        // Presigned URL을 발급받을 파일 목록 (실제 파일 이름으로 변경 필요)
        List<String> fileNames = List.of(
                "Morib-1.0.0-installer-arm64.dmg",
                "Morib-1.0.0-installer-universal.dmg",
                "Morib-1.0.0-installer-x64.dmg"
        );

        List<S3Info> presignedUrls = new ArrayList<>();
        Duration expirationDuration = Duration.ofHours(1); // 1시간 유효기간

        for (String fileName : fileNames) {
            String objectKey = keyPrefix + fileName;
            try {
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(objectKey)
                        .build();

                GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                        .signatureDuration(expirationDuration)
                        .getObjectRequest(getObjectRequest)
                        .build();

                PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
                String preSignedURL = presignedGetObjectRequest.url().toString();

                presignedUrls.add(S3Info.of(fileName.split("-")[3], preSignedURL));

            } catch (S3Exception e) { // S3 관련 예외 우선 처리
                LoggerFactory.getLogger(S3Service.class).error("S3 error generating presigned URL for {}: {}", objectKey, e.getMessage(), e);
                // 개별 파일 실패 시 어떻게 처리할지 결정 (예: null 추가, 빈 URL 추가, 또는 전체 실패)
                // 여기서는 일단 다음 파일로 넘어가도록 함. 필요시 전체 예외 발생 가능.
            } catch (SdkException e) { // 포괄적인 AWS SDK 예외
                LoggerFactory.getLogger(S3Service.class).error("AWS SDK error generating presigned URL for {}: {}", objectKey, e.getMessage(), e);
            } catch (Exception e) { // 기타 예외 (URISyntaxException 등 포함 가능)
                LoggerFactory.getLogger(S3Service.class).error("Unexpected error generating presigned URL for {}: {}", objectKey, e.getMessage(), e);
            }
        }

        // 만약 일부 URL 생성에 실패했더라도, 성공한 것들만 반환할지, 아니면 전체 요청을 실패로 처리할지 결정 필요
        // 모든 URL 생성이 필수라면, for문 밖에서 presignedUrls.size() 와 fileNames.size()를 비교하여
        // 개수가 다르면 예외를 던질 수 있습니다.
        if (presignedUrls.isEmpty() && !fileNames.isEmpty()) {
            // BusinessLogicException은 예시이며, 실제 프로젝트의 예외 처리 방식에 맞게 수정
            throw new InvalidStateException(ErrorMessage.BAD_REQUEST);
        }

        return presignedUrls;
    }
}