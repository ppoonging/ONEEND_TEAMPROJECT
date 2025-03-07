package com.springboot.biz.customer.replay;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReplayForm {

    @NotEmpty(message = "내용은 반드시 입력하셔야 합니당")
    private String replayContent;

}
