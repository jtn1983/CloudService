package ru.tenilin.cloudservice.service;

import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileManager {
    private static final String DIRECTORY_PATH = "/Users/tenilin/IdeaProjects/CloudService/src/main/resources/cloud";

    public void upload(byte[] resource, String hashName) throws IOException {
        Path path = Paths.get(DIRECTORY_PATH, hashName);
        Path file = Files.createFile(path);
        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(file.toString());
            stream.write(resource);
        }finally {
            stream.close();
        }
    }
}
