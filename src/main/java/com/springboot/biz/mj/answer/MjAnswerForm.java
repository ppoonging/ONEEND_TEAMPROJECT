package com.springboot.biz.mj.answer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MjAnswerForm {

    @NotEmpty(message ="답변을 입력하쇼")
    public String content;

}
