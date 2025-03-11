package com.springboot.biz.sns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SnsDTO {
    private String title;

    private String link;

    private String tag;

    private String imageName;

    private String imagePath;
}
