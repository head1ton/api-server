package com.youwiz.demoapi.repo;

import com.youwiz.demoapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepo extends JpaRepository<User, Long> {
}
