package com.youwiz.demoapi.repo;

import com.youwiz.demoapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<User, Long> {
    // 회원 가입 시 가입한 이메일 조회를 위해 메서드 선언
    Optional<User> findByUid(String email);
}
