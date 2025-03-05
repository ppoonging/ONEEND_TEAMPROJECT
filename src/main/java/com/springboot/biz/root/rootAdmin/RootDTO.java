package com.springboot.biz.root.rootAdmin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class RootDTO {
    private String title;

    private List<RootListDTO> rootList;
}
