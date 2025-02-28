package com.springboot.biz.free.board;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FreeQuestionForm {

    @NotEmpty(message = "제목은 반드시 입력하셔야합니당")
    private String frboTitle;

    @NotEmpty(message = "내용은 반드시 입력하셔야 합니당")
    private String frboContent;




}
