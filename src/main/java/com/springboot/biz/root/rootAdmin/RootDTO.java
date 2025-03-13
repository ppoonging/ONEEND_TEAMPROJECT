package com.springboot.biz.root.rootAdmin;

import com.springboot.biz.root.rootUser.RootAuthList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class RootDTO {
    private String title;

    private String rootList; // json

    private List<RootList> rootLists; // 실제

    private boolean rootState;

}
