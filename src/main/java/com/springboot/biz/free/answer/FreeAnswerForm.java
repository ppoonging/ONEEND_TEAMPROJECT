package com.springboot.biz.free.answer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreeAnswerForm {

    @NotEmpty(message = "내용은 반드시 입력하셔야 합니다")
    public String frboAnsContent;




}
