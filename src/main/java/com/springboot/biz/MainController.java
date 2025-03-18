package com.springboot.biz;


import com.springboot.biz.free.board.FreeQuestion;
import com.springboot.biz.free.board.FreeQuestionService;
import com.springboot.biz.mj.board.Mjboard;
import com.springboot.biz.mj.board.MjboardService;
import com.springboot.biz.root.rootAdmin.Root;
import com.springboot.biz.root.rootAdmin.RootService;
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
    private final FreeQuestionService freeQuestionService;
    private final RootService rootService;

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

        // 맛집 탑 9
        List<Mjboard> top9MjBoard = mjboardService.getTop9ByView();
        for(int i = 0; i < top9MjBoard.size(); i++){
            System.out.println("----" + top9MjBoard.get(i).getMjTitle());
        }

        model.addAttribute("top9MjBoard", top9MjBoard);

        // 자유게시판 최신 5
        List<FreeQuestion> freeQuestionBoard = freeQuestionService.getFiveQuestions();
        model.addAttribute("freeQuestionBoard", freeQuestionBoard);

        // 루트 최신 5
        List<Root> rootList = rootService.getFiveRoot();
        model.addAttribute("rootList", rootList);

        return "fragments/main";
    }

//    @GetMapping("/main")
//    public String mainPage(Model model) {
//        List<Mjboard> top9MjBoard = mjboardService.getTop9ByView();
//        for(int i = 0; i < top9MjBoard.size(); i++){
//            System.out.println("----" + top9MjBoard.get(i).getMjTitle());
//        }
//
//        model.addAttribute("top9MjBoard", top9MjBoard);
//        return "fragments/main";
//    }

//    @GetMapping("/free")
//    public String free() {
//        return "main/free/main_layout";
//    }

}
