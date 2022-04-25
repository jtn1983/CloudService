package ru.tenilin.cloudservice.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String userName;

    @Column
    private String password;

//    @ManyToOne
//    @JoinColumn(name = "role_id")
//    private RoleEntity roleEntity;
}
