package com.springboot.biz.mj.board;

import com.springboot.biz.map.MapService;
import com.springboot.biz.mj.answer.MjAnswerForm;
import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mjboard")
public class MjboardController {

    private final HUserSerevice hUserSerevice;
    private final MjboardService mjboardService;
    private final MapService mapService;

    // 게시판 목록 조회
    @GetMapping("/list")
    public String list(Model model,
                       @PageableDefault(size = 6, sort = "mjRegDate", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(value = "kw", defaultValue = "") String kw,
                       @RequestParam(value = "searchType", defaultValue = "both") String searchType) {

        // 검색 타입에 맞게 서비스에서 검색 결과 가져오기
        Map<String, Object> result = mjboardService.getList(pageable, kw, searchType);
        Page<Mjboard> paging = (Page<Mjboard>) result.get("paging");

        String defaultImageUrl = "/images/total/default.png";

        // 각 게시글에서 이미지 URL 추출 후, mjSeq를 키로 하는 Map에 저장
        Map<Integer, String> imageUrlMap = new HashMap<>();
        for (Mjboard board : paging) {
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

        // 모델에 필요한 데이터 추가
        model.addAttribute("paging", paging);
        model.addAttribute("starCountMap", result.get("starCountMap"));
        model.addAttribute("kw", kw);
        model.addAttribute("searchType", searchType); // 검색 타입 추가
        model.addAttribute("imageUrlMap", imageUrlMap);

        return "mj/mjboard_list"; // 해당 뷰로 이동
    }

    //게시글 작성 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(MjboardForm mjboardForm) {
        return "mj/mjboard_form";
    }

    //게시글 저장
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createPost(@Valid MjboardForm mjboardForm,
                             BindingResult bindingResult,
                             @RequestParam("file") MultipartFile file,
                             Principal principal) throws Exception {
        if (bindingResult.hasErrors()) {
            return "mj/mjboard_form";
        }
        HUser hUser = hUserSerevice.getUser(principal.getName());
        mjboardService.create(mjboardForm.getMjTitle(), mjboardForm.getMjContent(), file, hUser, 0,
                mjboardForm.getMjMapTitle(), mjboardForm.getMjMapAddress(), mjboardForm.getMjMapRodeAddress(),
                mjboardForm.getMjMapLatitude(), mjboardForm.getMjMapLongitude(), mjboardForm.getMjMapLink(),
                mjboardForm.getMjMapCategory());
        return "redirect:/mjboard/list";
    }

    //게시글 상세 조회
    @GetMapping("/detail/{mjSeq}")
    public String detail(Model model, @PathVariable("mjSeq") Integer mjSeq) {
        Mjboard mjboard = mjboardService.getMjboard(mjSeq);
        model.addAttribute("mjboard", mjboard);
        return "mj/mjboard_detail";
    }

    //썸머노트 이미지 업로드
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/uploadImage")
    @ResponseBody
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        String imageUrl = mjboardService.saveSummernoteImage(file);
        return ResponseEntity.ok(Collections.singletonMap("url", imageUrl));
    }

    //추천 기능
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mjRecommend/{mjSeq}")
    public String recommend(@PathVariable("mjSeq") Integer mjSeq, Principal principal) {
        Mjboard mjboard = mjboardService.getMjboard(mjSeq);
        HUser user = hUserSerevice.getUser(principal.getName());
        mjboardService.mjRecommend(mjboard, user);
        return "redirect:/mjboard/detail/" + mjSeq;
    }

    //게시글 수정 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{mjSeq}")
    public String modifyForm(Model model, MjboardForm form, @PathVariable Integer mjSeq) {
        Mjboard board = mjboardService.getMjboard(mjSeq);
        form.setMjTitle(board.getMjTitle());
        form.setMjContent(board.getMjContent());
        form.setMjMapTitle(board.getMjMapTitle());
        form.setMjMapAddress(board.getMjMapAddress());
        form.setMjMapRodeAddress(board.getMjMapRodeAddress());
        form.setMjMapLatitude(board.getMjMapLatitude());
        form.setMjMapLongitude(board.getMjMapLongitude());
        form.setMjMapLink(board.getMjMapLink());
        form.setMjMapCategory(board.getMjMapCategory());
        model.addAttribute("mjboard", board);
        return "mj/mjboardModify_form";
    }

    //게시글 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{mjSeq}")
    public String delete(@PathVariable Integer mjSeq) {
        Mjboard board = mjboardService.getMjboard(mjSeq);
        mjboardService.delete(board);
        return "redirect:/mjboard/list";
    }

    //검색 기능
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/form/search")
    @ResponseBody
    public List<Map<String, String>> search(@RequestParam String query) {
        return mapService.search(query);
    }
}
