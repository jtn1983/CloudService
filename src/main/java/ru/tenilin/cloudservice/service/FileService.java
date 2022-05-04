package ru.tenilin.cloudservice.service;


import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.tenilin.cloudservice.model.FileEntity;
import ru.tenilin.cloudservice.model.FileNameSizeProjection;
import ru.tenilin.cloudservice.model.UserEntity;
import ru.tenilin.cloudservice.repository.FileDAO;
import ru.tenilin.cloudservice.repository.FileRepository;
import ru.tenilin.cloudservice.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileDAO fileDAO;

    @Autowired
    private FileManager fileManager;

   public List<FileNameSizeProjection> getAllFiles(String userName){
       UserEntity user = userRepository.findByUserName(userName);
       return fileRepository.findFilesByUser(user);
   }


    public FileEntity upload(MultipartFile resource, String userName) throws IOException {
       UserEntity user = userRepository.findByUserName(userName);
       String hashFile = generateKey(resource.getName());
       FileEntity createdFile = FileEntity.builder()
               .fileName(resource.getOriginalFilename())
               .fileHash(hashFile)
               .fileSize(resource.getSize())
               .user(user)
               .build();
       createdFile = fileDAO.create(createdFile);
       fileManager.upload(resource.getBytes(), hashFile);
       return createdFile;
    }

    private String generateKey(String name){
       return DigestUtils.md5Hex(name + LocalDateTime.now().toString());
    }
}
