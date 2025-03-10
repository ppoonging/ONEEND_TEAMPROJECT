package com.springboot.biz.mj.board;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MjboardForm {

    @NotEmpty(message = "제목을 입력해주세요")// 문자가 들어오지 않으면 저장되지 않게 하기위해 만듬
    @Size(max = 200) //제목 사이즈를 설정
    private String mjTitle;

    @NotEmpty(message = "내용을 입력해주세요")
    private String mjContent;
}
