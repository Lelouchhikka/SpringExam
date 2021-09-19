package org.itstep.exam.model;

import lombok.Data;

@Data
public class UserModel {

    private String email;

    private String fullName;

    private String password;

    private String confirmPassword;
}
