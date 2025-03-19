package com.springboot.biz.free.answer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreeAnswerForm {

    @NotEmpty
    public String frboAnsContent;




}
