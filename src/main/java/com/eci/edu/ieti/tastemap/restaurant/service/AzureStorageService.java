package com.eci.edu.ieti.tastemap.restaurant.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class AzureStorageService {
    private final BlobServiceClient blobServiceClient;
    private final String defaultContainerName;
    private final String menusContainerName;

    public AzureStorageService(
            @Value("${azure.storage.connection-string}") String connectionString,
            @Value("${azure.storage.container-name}") String containerName,
            @Value("${azure.storage.menus-container-name:menus}") String menusContainerName
    ) {
        this.blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        this.defaultContainerName = containerName;
        this.menusContainerName = menusContainerName;
        BlobContainerClient defaultContainer = blobServiceClient.getBlobContainerClient(containerName);
        defaultContainer.createIfNotExists();
    }

    public String uploadImage(MultipartFile file) {
        return uploadFile(file, defaultContainerName);
    }

    public String uploadFile(MultipartFile file, String containerName) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        containerClient.createIfNotExists();

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());
        String blobName = UUID.randomUUID() + "-" + originalFilename;
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        try (InputStream inputStream = file.getInputStream()) {
            blobClient.upload(inputStream, file.getSize(), true);
            return blobClient.getBlobUrl();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to upload file to Azure Blob Storage", e);
        }
    }

    public String uploadMenu(MultipartFile file) {
        return uploadFile(file, menusContainerName);
    }

    public boolean deleteFileByUrl(String fileUrl) {
        if (!StringUtils.hasText(fileUrl)) {
            return false;
        }

        URI uri;
        try {
            uri = URI.create(fileUrl);
        } catch (IllegalArgumentException e) {
            return false;
        }

        String rawPath = uri.getRawPath();
        if (!StringUtils.hasText(rawPath) || !rawPath.startsWith("/")) {
            return false;
        }

        String path = rawPath.substring(1);
        int firstSlash = path.indexOf('/');
        if (firstSlash <= 0 || firstSlash == path.length() - 1) {
            return false;
        }

        String containerName = path.substring(0, firstSlash);
        String blobName = URLDecoder.decode(path.substring(firstSlash + 1), StandardCharsets.UTF_8);

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        return blobClient.deleteIfExists();
    }
}