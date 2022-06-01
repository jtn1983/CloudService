package ru.tenilin.cloudservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class CloudServiceApplication {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/cloud");
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        SpringApplication.run(CloudServiceApplication.class, args);
    }

}
