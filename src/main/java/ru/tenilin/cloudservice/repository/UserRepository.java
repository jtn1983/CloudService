package ru.tenilin.cloudservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tenilin.cloudservice.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserName(String userName);
    UserEntity findUserById(Long id);
}
