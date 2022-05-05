package ru.tenilin.cloudservice.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileManager {
    private String DIRECTORY_PATH = "src/main/resources/cloud/";

    public void upload(byte[] resource, String hashName) throws IOException {
        Path path = Paths.get(DIRECTORY_PATH, hashName);
        Path file = Files.createFile(path);
        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(file.toString());
            stream.write(resource);
        } finally {
            stream.close();
        }
    }

    public Resource download(String hashFile) throws IOException {
        Path path = Paths.get(DIRECTORY_PATH + hashFile);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException();
        }
    }

    public void delete(String fileHash) throws IOException {
        Path path = Paths.get(DIRECTORY_PATH + fileHash);
        Files.delete(path);
    }
}
