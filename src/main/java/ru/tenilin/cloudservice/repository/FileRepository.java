package ru.tenilin.cloudservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.tenilin.cloudservice.model.FileEntity;
import ru.tenilin.cloudservice.model.FileNameSizeProjection;
import ru.tenilin.cloudservice.model.UserEntity;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    @Query(value = "select f.file_name as filename, f.file_size as size from files f where f.user_id = :user", nativeQuery = true)
    List<FileNameSizeProjection> findFilesByUser(UserEntity user);

}
