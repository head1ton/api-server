package com.youwiz.demoapi.repo.board;

import com.youwiz.demoapi.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardJpaRepo extends JpaRepository<Board, Long> {
    Board findByName(String name);
}
