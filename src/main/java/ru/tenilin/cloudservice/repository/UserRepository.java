package ru.tenilin.cloudservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tenilin.cloudservice.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
}
