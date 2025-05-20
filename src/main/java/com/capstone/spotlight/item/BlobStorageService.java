package com.capstone.spotlight.item;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class BlobStorageService {

    private final BlobContainerClient containerClient;
    private final String sasToken;

    public BlobStorageService(
            @Value("${azure.storage.endpoint}") String endpoint,
            @Value("${azure.storage.container-name}") String containerName,
            @Value("${azure.storage.sas-token}") String sasToken
    ) {
        this.sasToken = sasToken;

        String connectionUrl = String.format("%s/%s?%s", endpoint, containerName, sasToken);
        this.containerClient = new BlobContainerClientBuilder()
                .endpoint(connectionUrl)
                .sasToken(sasToken)
                .containerName(containerName)
                .buildClient();
    }

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            BlobClient blobClient = containerClient.getBlobClient(fileName);

            try (InputStream inputStream = file.getInputStream()) {
                blobClient.upload(inputStream, file.getSize(), true);
            }

            return blobClient.getBlobUrl();
        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드 실패: " + e.getMessage(), e);
        }
    }

    // AWS S3 처럼 정식 Presigned URL 개념이 없어서 SAS Token을 통해 유사하게 동작함
    // 그래서 임의로 generatePresignedUploadUrl() 함수를 만들어서 사용하는거임
    public String generatePresignedUploadUrl(String blobPath) {
        BlobClient blobClient = containerClient.getBlobClient(blobPath);


        // 생성한 SAS Token에 따라 URL이 다름
        return blobClient.getBlobUrl() + "?" + sasToken;
    }


}
