package com.youwiz.demoapi.repo;

import com.youwiz.demoapi.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/*
assertNotNull(obj), assertNotNull(obj) -> 객체(obj)의 Null여부를 테스트
assertTrue(condition), assertFalse(condition) -> 조건(condition)의 참/거짓 테스트
assertEquals(obj1, obj2), assertNotEquals(obj1, obj2) -> obj1와 obj2의 값이 같은지 테스트
assertSame(obj1, obj2) -> obj1과 obj2의 객체가 가튼지 테스트
assertArrayEquals(arr1, arr2) -> 배열 arr1과 arr2가 같은지 테스트
assertThat(T actual, Matcher matcher) -> 첫 번째 인자에 비교대상 값을 넣고, 두 번째 로직에는 비교로직을 넣어 조건 테스트
ex) assertThat(a, is(100)) : a의 값이 100인가?
ex) assertThat(obj, is(nullValue())); 객체가 null인가?
 */
@RunWith(SpringRunner.class)
@DataJdbcTest
public class UserJpaRepoTest {

    @Autowired
    private UserJpaRepo userJpaRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void whenFindByUid_thenReturnUser() {
        String uid = "head1ton@gmail.com";
        String name = "head1ton";
        // given
        userJpaRepo.save(User.builder()
                .uid(uid)
                .password(passwordEncoder.encode("1234"))
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        // when
        Optional<User> user = userJpaRepo.findByUid(uid);
        // then
        assertNotNull(user);    // user 객체가 null 이 아닌지 체크
        assertTrue(user.isPresent());   // user 객체가 존재여부 true / false 체크
        assertEquals(user.get().getName(), name);   // user 객체의 name과 name 변수값이 같은지 체크
        assertThat(user.get().getName(), is(name)); // user 객체의 name과 name 변수값이 같은지 체크
    }
}