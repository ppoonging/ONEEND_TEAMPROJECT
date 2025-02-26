package com.springboot.biz.tour.mjtour;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class TourForm {

    @Size(min = 3, max = 60)
    @NotEmpty(message = "제목을 입력하세요")
    private String title;

    @NotEmpty(message = "루트를 하나라도 선택하세요.")
    private List<String> rootList;
}
