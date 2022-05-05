package ru.tenilin.cloudservice.service;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.tenilin.cloudservice.model.FileEntity;
import ru.tenilin.cloudservice.model.FileNameSizeProjection;
import ru.tenilin.cloudservice.model.UserEntity;
import ru.tenilin.cloudservice.repository.FileRepository;
import ru.tenilin.cloudservice.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FileManager fileManager;

    public FileService(FileRepository fileRepository, UserRepository userRepository, FileManager fileManager) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.fileManager = fileManager;
    }

    public List<FileNameSizeProjection> getAllFiles(String userName, Integer limit) {
        UserEntity user = userRepository.findByUserName(userName);
        List<FileNameSizeProjection> files = fileRepository.findFilesByUser(user);
        PagedListHolder<FileNameSizeProjection> pages = new PagedListHolder<>(files);
        pages.setPage(0);
        pages.setPageSize(limit);
        return pages.getPageList();
    }

    public FileEntity getFileByName(String fileName) {
        return fileRepository.findByFileName(fileName);
    }

    @Transactional(rollbackOn = {IOException.class})
    public void upload(MultipartFile resource, String userName) throws IOException {
        checkFileName(resource.getOriginalFilename());
        UserEntity user = userRepository.findByUserName(userName);
        if (user == null) throw new IOException();
        String hashFile = generateKey(resource.getName());
        FileEntity createdFile = FileEntity.builder()
                .fileName(resource.getOriginalFilename())
                .fileHash(hashFile)
                .fileSize(resource.getSize())
                .uploadDate(LocalDate.now())
                .user(user)
                .build();
        fileRepository.save(createdFile);
        fileManager.upload(resource.getBytes(), hashFile);
    }

    public Resource download(String hashFile) throws IOException {
        return fileManager.download(hashFile);
    }

    public void delete(String fileName) throws IOException {
        FileEntity file = fileRepository.findByFileName(fileName);
        fileRepository.delete(file);
        fileManager.delete(file.getFileHash());
    }

    public void update(String oldFileName, String newFileName) throws IOException {
        checkFileName(newFileName);
        FileEntity file = fileRepository.findByFileName(oldFileName);
        if (file != null) {
            fileRepository.updateFileName(newFileName, oldFileName);
        } else {
            throw new IOException();
        }
    }

    private String generateKey(String name) {
        return DigestUtils.md5Hex(name + LocalDateTime.now().toString());
    }

    private void checkFileName(String fileName) throws IOException {
        if (fileRepository.findByFileName(fileName) != null) {
            throw new IOException();
        }
    }
}
