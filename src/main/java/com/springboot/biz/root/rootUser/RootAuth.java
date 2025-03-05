package com.springboot.biz.root.rootUser;

import com.springboot.biz.root.rootAdmin.Root;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class RootAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rootAuthSeq;

    @NotNull
    private String rootAuthTitle;

    @Column(columnDefinition = "TEXT")
    private String rootAuthContent;

    @OneToOne
    private Root root;

    private LocalDateTime rootAuthDate;

}
