package com.youwiz.demoapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.youwiz.demoapi.entity.common.CommonDateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends CommonDateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable =  false, length = 50)
    private String author;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "msrl")
    private User user;

    // Join 테이블이 Json 결과에 표시되지 않도록 처리
    @JsonIgnore
    public Board getBoard() {
        return board;
    }

    public Post(User user, Board board, String author, String title, String content) {
        this.user = user;
        this.board = board;
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public Post setUpdate(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        return this;
    }
}
