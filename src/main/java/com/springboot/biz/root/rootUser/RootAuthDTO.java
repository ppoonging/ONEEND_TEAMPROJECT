package com.springboot.biz.root.rootUser;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class RootAuthDTO {

    private Long rootAuthSeq;

    private Integer rootSeq;

    private String title;

    private String content;

    private List<MultipartFile> files; // 파일 업로드

    private String rootAuthList; // json

    private List<RootAuthList> rootAuthLists; // 실제
}
