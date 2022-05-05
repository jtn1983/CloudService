package ru.tenilin.cloudservice;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.tenilin.cloudservice.model.FileEntity;
import ru.tenilin.cloudservice.service.FileManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class FileManagerTest {

    private static MultipartFile multipartFile;
    private static FileManager fileManager;
    private static FileEntity file;

    @BeforeClass
    public static void prepareTestData() throws IOException {
        file = FileEntity.builder()
                .fileId(5L)
                .fileName("test.txt")
                .fileHash("test.txt")
                .fileSize(54786L)
                .uploadDate(LocalDate.now())
                .build();
        multipartFile = new MockMultipartFile(
                "test",
                "test.txt",
                "txt",
                new FileInputStream("src/test/resources/test.txt")
        );
        fileManager = new FileManager();
    }

    @Test
    public void uploadTest() throws IOException {
        ReflectionTestUtils.setField(
                fileManager,
                "DIRECTORY_PATH",
                "src/test/resources/testFileStorage/"
        );

        fileManager.upload(multipartFile.getBytes(), "test.txt");

        Path checkFile = Paths.get("src/test/resources/testFileStorage/test.txt");
        assertThat(Files.exists(checkFile)).isTrue();
        assertThat(Files.isRegularFile(checkFile)).isTrue();
        assertThat(Files.size(checkFile)).isEqualTo(multipartFile.getSize());
        Files.delete(checkFile);
    }

    @Test
    public void downloadTest() throws IOException {
        ReflectionTestUtils.setField(fileManager, "DIRECTORY_PATH", "src/test/resources/");

        Resource resource = fileManager.download(file.getFileHash());

        assertThat(resource.isFile()).isTrue();
        assertThat(resource.getFilename()).isEqualTo(file.getFileName());
        assertThat(resource.exists()).isTrue();
    }

    @Test
    public void deleteTest() throws IOException {
        Path checkFile = Paths.get("src/test/resources/testFileStorage/test.txt");
        Files.createFile(checkFile);

        assertThat(Files.exists(checkFile)).isTrue();
        assertThat(Files.isRegularFile(checkFile)).isTrue();
        ReflectionTestUtils.setField(
                fileManager,
                "DIRECTORY_PATH",
                "src/test/resources/testFileStorage/"
        );

        fileManager.delete(file.getFileHash());

        assertThat(Files.notExists(checkFile)).isTrue();
    }
}
