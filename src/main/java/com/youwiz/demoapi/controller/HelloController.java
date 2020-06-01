package com.youwiz.demoapi.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // spring에 해당 Class가 Controller임을 알려주기 위해 class 명 상단에 붙임.
public class HelloController {
    /*
    1. 화면에 helloworld 가 출력됩니다.
     */
    @GetMapping(value = "/helloworld/string")
    @ResponseBody   // 지정하지 않을 시 return에 지정된 "helloworld" 이름으로 된 파일을 프로젝트 경로에서 찾아 출력.
    public String helloworldString() {
        return "helloworld";
    }

    /*
    2. 화면에 { message: "helloworld"} 라고 출력.
     */
    @GetMapping("/helloworld/json")
    @ResponseBody
    public Hello helloworldJson() {
        Hello hello = new Hello();
        hello.message = "helloworld";
        return hello;
    }

    /*
    3. 화면에 helloworld.ftl 의 내용이 출력. 되나?ㅡ.ㅡ?
     */
    @GetMapping("/helloworld/page")
    public String helloworld() {
        return "helloworld";
    }

    @Setter
    @Getter
    public static class Hello {
        private String message;
    }
}
