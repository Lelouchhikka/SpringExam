package org.itstep.exam.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;


@Data
@Entity
@NoArgsConstructor
public class Genre extends BaseEntity {

    private String role;

    public Genre(Long id, String role_user, String user_role) {
        this.setId(id);
        this.role = role_user;
    }


}