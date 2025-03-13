package com.springboot.biz.mj.answer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MjAnswerForm {

    @NotEmpty(message = "답변을 작성을 해주세요")
    private String mjAnsContent;

}
