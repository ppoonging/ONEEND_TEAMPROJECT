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

    // 목록
    @GetMapping("/list")
    public String list(Model model,
                       @PageableDefault(size = 6, sort = "mjRegDate", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {

        // 각 게시글의 mjSeq를 키로, 추출한 이미지 URL을 값으로 저장할 Map 생성
        Map<String, Object> result = mjboardService.getList(pageable, kw);
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

        model.addAttribute("paging", paging);
        model.addAttribute("starCountMap", result.get("starCountMap"));
        model.addAttribute("kw", kw);
        model.addAttribute("imageUrlMap", imageUrlMap);
        return "mj/mjboard_list";
    }

    // 작성 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(MjboardForm mjboardForm) {
        return "mj/mjboard_form";
    }

    // 저장
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
                mjboardForm.getMjMapTitle(), mjboardForm.getMjMapAddress(), mjboardForm.getMjMapRodeAddress(), mjboardForm.getMjMapLatitude(),
                mjboardForm.getMjMapLongitude(), mjboardForm.getMjMapLink(), mjboardForm.getMjMapCategory());
        return "redirect:/mjboard/list";
    }

    // 상세
    @GetMapping("/detail/{mjSeq}")
    public String detail(Model model, @PathVariable("mjSeq") Integer mjSeq) {
        Mjboard mjboard = mjboardService.getMjboard(mjSeq);
        String imageUrl = null;

        // 이미지 URL 파싱 및 유효성 체크
        if (mjboard.getMjContent() != null && mjboard.getMjContent().contains("<img")) {
            int srcIndex = mjboard.getMjContent().indexOf("src=");
            if (srcIndex != -1) {
                srcIndex = mjboard.getMjContent().indexOf("\"", srcIndex) + 1;
                int endIndex = mjboard.getMjContent().indexOf("\"", srcIndex);
                if (endIndex != -1) {
                    imageUrl = mjboard.getMjContent().substring(srcIndex, endIndex);
                    File imageFile = new File("파일이_저장되는_경로" + imageUrl); // 실제 경로로 교체 필요
                    if (!imageFile.exists()) {
                        imageUrl = "/default-image.jpg"; // 대체 이미지
                    }
                }
            }
        }
        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("mjanswerForm", new MjAnswerForm());
        model.addAttribute("mjreplyForm", new MjAnswerForm());
        model.addAttribute("mjboard", mjboard);
        return "mj/mjboard_detail";
    }



    // 썸머노트 이미지 업로드
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/uploadImage")
    @ResponseBody
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        String imageUrl = mjboardService.saveSummernoteImage(file);
        return ResponseEntity.ok(Collections.singletonMap("url", imageUrl));
    }

    // 외부 이미지 업로드
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/uploadExternalImage")
    @ResponseBody
    public Map<String, String> uploadExternalImage(@RequestBody Map<String, String> req) throws Exception {
        String imageUrl = req.get("url");
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/mj";

        URL url = new URL(imageUrl);
        String fileName = UUID.randomUUID() + "_" + Paths.get(url.getPath()).getFileName().toString();
        File file = new File(projectPath, fileName);

        try (InputStream in = url.openStream(); OutputStream out = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return Collections.singletonMap("url", "/files/mj/" + fileName);
    }

    // 추천
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mjRecommend/{mjSeq}")
    public String recommend(@PathVariable("mjSeq") Integer mjSeq, Principal principal) {
        Mjboard mjboard = mjboardService.getMjboard(mjSeq);
        HUser user = hUserSerevice.getUser(principal.getName());
        mjboardService.mjRecommend(mjboard, user);
        return "redirect:/mjboard/detail/" + mjSeq;
    }

    // 수정 폼
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

    // 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{mjSeq}")
    public String modify(@Valid MjboardForm form, BindingResult bindingResult, @PathVariable Integer mjSeq) {
        if (bindingResult.hasErrors()) return "mj/mjboardModify_form";
        Mjboard board = mjboardService.getMjboard(mjSeq);
        mjboardService.modify(board, form.getMjTitle(), form.getMjContent(), form.getMjMapTitle(), form.getMjMapAddress(), form.getMjMapRodeAddress(),
                form.getMjMapLatitude(), form.getMjMapLongitude(), form.getMjMapLink(), form.getMjMapCategory());
        return "redirect:/mjboard/list";
    }

    // 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{mjSeq}")
    public String delete(@PathVariable Integer mjSeq) {
        Mjboard board = mjboardService.getMjboard(mjSeq);
        mjboardService.delete(board);
        return "redirect:/mjboard/list";
    }

    // 검색
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/form/search")
    @ResponseBody
    public List<Map<String, String>> search(@RequestParam String query) {
        return mapService.search(query); // JSON 형태로 반환
    }

}
