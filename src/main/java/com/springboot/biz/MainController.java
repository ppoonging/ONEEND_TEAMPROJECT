package com.springboot.biz;


import com.springboot.biz.mj.board.Mjboard;
import com.springboot.biz.mj.board.MjboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MjboardService mjboardService;

    @GetMapping("/")
    public String root(Model model){

        String defaultImageUrl = "/images/total/default.png";
        List<Mjboard> mjboard = mjboardService.getList();

        Map<Integer, String> imageUrlMap = new HashMap<>();
        for (Mjboard board : mjboard) {
            String content = board.getMjContent();
            String imageUrl = null;
            if (content != null && content.contains("<img")) {
                int srcIndex = content.indexOf("src=\"");
                if (srcIndex != -1) {
                    int start = srcIndex + 5; // "src=\""의 길이
                    int end = content.indexOf("\"", start);
                    if (end != -1) {
                        imageUrl = content.substring(start, end);
                    }
                }
            }

            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                imageUrl = defaultImageUrl;
            }

            imageUrlMap.put(board.getMjSeq(), imageUrl);
        }

        model.addAttribute("mjboard", mjboard);
        model.addAttribute("imageUrlMap", imageUrlMap);

        return "fragments/main";
    }

    @GetMapping("/free")
    public String free() {
        return "main/free/main_layout";
    }

}
