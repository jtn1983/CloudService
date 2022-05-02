package ru.tenilin.cloudservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tenilin.cloudservice.model.FileEntity;
import ru.tenilin.cloudservice.model.FileNameSizeProjection;
import ru.tenilin.cloudservice.model.UserEntity;
import ru.tenilin.cloudservice.repository.FileRepository;
import ru.tenilin.cloudservice.repository.UserRepository;

import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

   public List<FileNameSizeProjection> getAllFiles(String userName){
       UserEntity user = userRepository.findByUserName(userName);
       return fileRepository.findFilesByUser(user);
   }




}
