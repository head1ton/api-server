package com.youwiz.demoapi.model.response;

import lombok.Getter;
import lombok.Setter;

/*
Generic Interface에 <T>를 지정하여 어떤 형태의 값도 넣을 수 있도록 구현.
또한 CommonResult를 상속받으므로 api 요청 결과도 같이 출력됩니다.
 */
@Setter
@Getter
public class SingleResult<T> extends CommonResult {
    private T data;
}
