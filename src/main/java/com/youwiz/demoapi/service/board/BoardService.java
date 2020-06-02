package com.youwiz.demoapi.service.board;

import com.youwiz.demoapi.advice.exception.CNotOwnerException;
import com.youwiz.demoapi.advice.exception.CResourceNotExistException;
import com.youwiz.demoapi.advice.exception.CUserExistException;
import com.youwiz.demoapi.advice.exception.CUserNotFoundException;
import com.youwiz.demoapi.entity.Board;
import com.youwiz.demoapi.entity.Post;
import com.youwiz.demoapi.entity.User;
import com.youwiz.demoapi.model.board.ParamsPost;
import com.youwiz.demoapi.repo.UserJpaRepo;
import com.youwiz.demoapi.repo.board.BoardJpaRepo;
import com.youwiz.demoapi.repo.board.PostJpaRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardJpaRepo boardJpaRepo;
    private final PostJpaRepo postJpaRepo;
    private final UserJpaRepo userJpaRepo;

    public Board insertBoard(String boardName) {
        return boardJpaRepo.save(Board.builder().name(boardName).build());
    }

    // 게시판 이름으로 게시판을 조회. 없을 경우 CResourceNotExistException 처리
    public Board findBoard(String boardName) {
        return Optional.ofNullable(boardJpaRepo.findByName(boardName)).orElseThrow(CResourceNotExistException::new);
    }

    // 게시판 이름으로 게시물 리스트 조회
    public List<Post> findPosts(String boardName) {
        return postJpaRepo.findByBoard(findBoard(boardName));
    }

    // 게시물 ID로 게시물 단건 조회. 없을 경우 CResourceNotExistException 처리
    public Post getPost(long postId) {
        return postJpaRepo.findById(postId).orElseThrow(CResourceNotExistException::new);
    }

    // 게시물을 등록. 게시물의 회원 ID가 조회되지 않으면 CUserNotFoundException 처리
    public Post writePost(String uid, String boardName, ParamsPost paramsPost) {
        Board board = findBoard(boardName);
        Post post = new Post(userJpaRepo.findByUid(uid).orElseThrow(CUserNotFoundException::new), board, paramsPost.getAuthor(), paramsPost.getTitle(), paramsPost.getContent());
        return postJpaRepo.save(post);
    }

    // 게시물을 수정합니다. 게시물 등록자와 로그인 회원정보가 틀리면 CNotOwnerException 처리
    public Post updatePost(long postId, String uid, ParamsPost paramsPost) {
        Post post = getPost(postId);
        User user = post.getUser();
        if (!uid.equals(user.getUid()))
            throw new CNotOwnerException();
         // 영속성 컨텍스트의 변경 감지(dirty checking) 기능에 의해 조회한 Post 내용을 변경만 해도 Update가 실행
        post.setUpdate(paramsPost.getAuthor(), paramsPost.getTitle(), paramsPost.getContent());
        return post;
    }

    // 게시물을 삭제. 게시물 등록자와 로그인 회원정보가 다르면 CNotOwnerException 처리
    public boolean deletePost(long postId, String uid) {
        Post post = getPost(postId);
        User user = post.getUser();
        if (!uid.equals(user.getUid()))
            throw new CNotOwnerException();
        postJpaRepo.delete(post);
        return true;
    }
}
