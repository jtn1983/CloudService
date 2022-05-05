package ru.tenilin.cloudservice.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "files")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Column
    private String fileName;

    @Column
    private Long fileSize;

    @Column
    private String fileHash;

    @Column
    private LocalDate uploadDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
