package com.springboot.biz.customer.replay;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReplayForm {

    @NotEmpty
    private String replayContent;

}
