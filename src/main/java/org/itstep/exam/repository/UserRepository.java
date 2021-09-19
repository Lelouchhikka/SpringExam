package org.itstep.exam.repository;
import org.itstep.exam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    User findUserByEmail(String email);
}
