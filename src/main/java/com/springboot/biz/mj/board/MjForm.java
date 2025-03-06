package com.springboot.biz.mj.board;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MjForm {
    @NotEmpty(message = "내용을 입력 해주시기 바랍니다.")
    private String mjContent;

    @NotEmpty(message = "제목을 입력 해주시기 바랍니다.")
    @Size(max = 200)
    private String mjTitle;
}
