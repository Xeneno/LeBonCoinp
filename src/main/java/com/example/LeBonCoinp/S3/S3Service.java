package com.example.LeBonCoinp.S3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import java.net.URL;
import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    /**
     * Generate presigned URL for uploading an image to S3.
     * @param fileKey e.g., "items/123/image.jpg" (path in bucket)
     * @param expiryMinutes e.g., 15
     * @return Presigned URL + metadata
     */
    public Map<String, Object> generatePresignedUploadUrl(String fileKey, int expiryMinutes, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .contentType(contentType)  // Adjust based on file type
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(r -> r
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(expiryMinutes)));

        URL uploadUrl = presignedRequest.url();

        return Map.of(
                "uploadUrl", uploadUrl.toString(),
                "fileKey", fileKey,
                "expiresIn", expiryMinutes * 60 
        );
    }

}