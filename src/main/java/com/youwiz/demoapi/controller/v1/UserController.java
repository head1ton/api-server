package com.youwiz.demoapi.controller.v1;

import com.youwiz.demoapi.advice.exception.CUserNotFoundException;
import com.youwiz.demoapi.entity.User;
import com.youwiz.demoapi.model.response.CommonResult;
import com.youwiz.demoapi.model.response.ListResult;
import com.youwiz.demoapi.model.response.SingleResult;
import com.youwiz.demoapi.repo.UserJpaRepo;
import com.youwiz.demoapi.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
@RequiredArgsConstructor 는 class 상단에 선언하면 class 내부에 final로 선언된 객체에 대해서
Constructor Injection 을 수행합니다. 해당 어노테이션을 사용하지 않고 선언된 객체에 @Autowired를 사용해도 됩니다.
 */
//@PreAuthorize("hasRole('ROLE_USER')") // OR @PreAuthorize("ROLE_USER") // 내부의 모든 리소스에 대하여 일괄로 동일한 권한을 설정할 때
@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RestController// 결과값을 Json으로 출력
@RequestMapping(value = "/v1")  // api resource를 버전별로 관리하기 위해 /v1을 모든 리소스 주소에 적용되도록 처리
public class UserController {
    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService;  // 결과를 처리할 Service

//    @Secured("ROLE_USER")   // 만약 각각의 리소스마다 권한을 설정해야한다면 이렇게 해당 메서드 위에 annotation을 세팅하면 됨.
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다.")
    @GetMapping(value = "/users")
    public ListResult<User> findAllUser() {
        // 결과 데이터가 여러 건인 경우 getListResult를 이용해서 결과를 출력한다.
        return responseService.getListResult(userJpaRepo.findAll());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "회원번호(msrl)로 회원을 조회한다")
    @GetMapping(value = "/user")
    public SingleResult<User> findUserById(@ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang) {
        // SecurityContext에서 인증받은 회원의 정보를 얻어온다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        // 결과데이터가 단일건인경우 getBasicResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(userJpaRepo.findByUid(id).orElseThrow(CUserNotFoundException::new));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다.")
    @PutMapping(value = "/user")
    public SingleResult<User> modify(
            @ApiParam(value = "회원번호", required = true) @RequestParam long msrl,
            @ApiParam(value = "회원이름", required = true) @RequestParam String name
    ) {
        User user = User.builder()
                .msrl(msrl)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 삭제", notes = "userId로 회원정보를 삭제한다.")
    @DeleteMapping(value = "/user/{msrl}")
    public CommonResult delete(
            @ApiParam(value = "회원번호", required = true) @PathVariable long msrl
    ) {
        userJpaRepo.deleteById(msrl);
        // 성공 결과 정보만 필요한 경우 getSuccessResult() 를 이용하여 결과를 출력한다.
        return responseService.getSuccessResult();
    }
}
