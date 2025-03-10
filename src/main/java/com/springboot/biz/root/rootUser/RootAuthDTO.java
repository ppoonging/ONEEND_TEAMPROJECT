package com.springboot.biz.root.rootUser;

import com.springboot.biz.root.rootAdmin.Root;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class RootAuthDTO {

//    private Long rootAuthSeq;
//
//    @NotBlank(message = "루트를 선택하세요.")
//    private String rootSeq;

    @NotBlank(message = "제목을 입력하세요.")
    @Size(min = 2, max = 100, message = "제목은 최소 2자 ~ 최대 100자까지 입력해야 합니다.")
    private String title;

    @NotBlank(message = "후기를 입력하세요.")
    @Size(min = 5, max = 1000, message = "후기는 최소 5자 ~ 최대 1000자까지 입력해야 합니다.")
    private String content;

//    private List<MultipartFile> files; // 파일 업로드
//
//    @NotBlank(message = "후기를 입력하세요.")
//    @Size(min = 1, message = "최소 하나 이상은 선택해야 합니다.")
//    private String rootList; // JSON 문자열로 전달받는 루트 리스트
}
