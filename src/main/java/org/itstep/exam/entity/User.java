package org.itstep.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Table(name = "userdb")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(name = "email", unique = true)
    private String email;

    private String password;

    private String fullName;

    private String ava;

    private String avaHash;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    public User(String email, String password, List<Role> roles) {
    }
}