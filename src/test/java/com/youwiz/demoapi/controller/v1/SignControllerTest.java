package com.youwiz.demoapi.controller.v1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
MockMvc Method

perform
* 주어진 url을 수행할 수 있는 환경을 구성.
* GET, POST, PUT, DELETE 등 다양한 method 처리가 가능.
* header 에 값을 세팅하거나 AcceptType 설정을 지원
* mockMvc.perform(post("/v1/signin").params(params)

andDo
* perform 요청을 처리. andDo(print())를 하면 처리 결과를 console 화면에서 볼 수 있음

andExpect
* 검증 내용을 체크.
* 결과가 200 OK 인지 체크 - andExpect(status().isOK())
* 결과 데이터가 Json인 경우 다음과 같이 체크가능
 .andExpect(jsonPath("$.success").value(true))

andReturn
* 테스트 완료 후 결과 객체를 받을 수 있음. 후속 작업이 필요할 때 용이.
* MvcResult result = mockMvc.perform(post("/v1/signin").params(params)).andDo(print());

 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void signin() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "head1ton@gmail.com");
        params.add("password", "1234");
        mockMvc.perform(post("/v1/signin").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    public void signup() throws Exception {
        long epochTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "head1ton_" + epochTime + "@gmail.com");
        params.add("password", "1234");
        params.add("name", "head1ton_" + epochTime);
        mockMvc.perform(post("/v1/signup").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists());
    }
}