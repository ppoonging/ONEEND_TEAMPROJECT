package com.springboot.biz.sns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SnsDTO {

    private String link;

    private String tag;

    private String imageName;

    private String imagePath;

    private MultipartFile file;

    private String title;

    private String content;

}
