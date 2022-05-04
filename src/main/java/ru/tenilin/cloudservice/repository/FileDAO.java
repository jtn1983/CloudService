package ru.tenilin.cloudservice.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.tenilin.cloudservice.model.FileEntity;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class FileDAO {
    private static final String CREATE_FILE =
            "INSERT INTO files(file_name, file_size, file_hash, upload_date, user_id) VALUES (?, ?, ?, ?, ?)";

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public FileEntity create(final FileEntity file){
        LocalDate uploadDate = LocalDate.now();
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(x -> {
            PreparedStatement preparedStatement = x.prepareStatement(CREATE_FILE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, file.getFileName());
            preparedStatement.setLong(2, file.getFileSize());
            preparedStatement.setString(3, file.getFileHash());
            preparedStatement.setDate(4, Date.valueOf(uploadDate));
            preparedStatement.setLong(5, file.getUser().getUserId());
            return preparedStatement;
        }, keyHolder);

        return file.toBuilder()
                .fileId(keyHolder.getKey().longValue())
                .uploadDate(uploadDate)
                .build();
    }

}
