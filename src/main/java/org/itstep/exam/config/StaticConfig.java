package org.itstep.exam.config;


import org.itstep.exam.entity.Role;

public class StaticConfig {

    public static final Role ROLE_ADMIN = new Role(1L, "ROLE_ADMIN", "Admin Role");
    public static final Role ROLE_USER = new Role(2L, "ROLE_USER", "User Role");
    public static final Role ROLE_MODERATOR = new Role(3L, "ROLE_MODERATOR", "Moderator Role");

    public static final Integer PAGE_SIZE=5;
    private StaticConfig() {
    }
}
