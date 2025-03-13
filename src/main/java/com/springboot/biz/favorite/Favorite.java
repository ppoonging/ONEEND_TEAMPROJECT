package com.springboot.biz.favorite;

import com.springboot.biz.recipe.fm.FmRecipe;
import com.springboot.biz.user.HUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer favoriteId;

    @ManyToOne
    @JoinColumn(name = "user_user_seq")
    private HUser user;

    @ManyToOne
    @JoinColumn(name = "recipe_fmrecipe_seq")
    private FmRecipe recipe;
}
