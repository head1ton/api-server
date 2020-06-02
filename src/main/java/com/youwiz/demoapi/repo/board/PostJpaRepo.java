package com.youwiz.demoapi.repo.board;

import com.youwiz.demoapi.entity.Board;
import com.youwiz.demoapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJpaRepo extends JpaRepository<Post, Long> {
    List<Post> findByBoard(Board board);
}
