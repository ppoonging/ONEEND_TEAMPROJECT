package com.springboot.biz.free.board;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FreeQuestionForm {

    @NotEmpty
    private String frboTitle;

    @NotEmpty
    private String frboContent;




}
