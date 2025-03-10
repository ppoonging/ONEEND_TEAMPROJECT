package com.springboot.biz.mj.board;


import com.springboot.biz.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MjboardService {
    private final MjboardRepository mjboardRepository;
    // 파일 업로드 기본 디렉토리 설정(절대 경로로 설정 동일한 위치에서 사용)
    private final String uploadDir = "/Users/your-name/project-root/src/main/resources/static/mjimage/";




    public Page<Mjboard> getList(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("mjRegDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.mjboardRepository.findAll(pageable);
 /*   }public void create(String mjTitle, String mjContent, HUser user) {
        Mjboard mj = new Mjboard();
        mj.setMjTitle(mjTitle);
        mj.setMjContent(mjContent); //  유효성 검사하고 아래꺼는 죽이고 위에는 살리기
        mj.setMjRegDate(LocalDateTime.now());
        mj.setUserId(user);
        this.mjboardRepository.save(mj);
    }*/
}public void create(String mjTitle, String mjContent, MultipartFile file)throws IOException { //IOException = 파일저장 예외 하는 것
    Mjboard mj = new Mjboard();
    mj.setMjTitle(mjTitle);
    mj.setMjContent(mjContent);
    mj.setMjRegDate(LocalDateTime.now());
// 파일 업로드 처리
        if (file != null && !file.isEmpty()) {
            String filePath = saveFile(file);
            mj.setMjFilePath(filePath);
            mj.setMjFileName(file.getOriginalFilename());
        }

    this.mjboardRepository.save(mj);
}
//파일 저장
    public String saveFile(MultipartFile file) throws IOException {
        // 업로드 폴더 설정
        String uploadDir =System.getProperty("user.dir")+ "uploads/mjimage/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            System.out.println("폴더 생성 여부: " + result);
            if (!result) {
                throw new IOException("업로드 폴더 생성 실패: " + dir.getAbsolutePath());
            }
        }
        //원본 파일명 가져오기
        String originalFilename = file.getOriginalFilename();
        String filePath = Paths.get(uploadDir, originalFilename).toString();
        //파일 저장
        File dest = new File(filePath);
        file.transferTo(dest);

        //저장된 파일의 경로 반환
        return "/uploads/mjimage/" + originalFilename;
    }

//데이터베이스에서 특정 ID에 해당하는 MJBOARD 객체를 조회하는 역할을 한다.
    //게시글 1개를 가져오고 없으면 예외(객체가 없네요)를 발생시키는 기능.
public Mjboard getMjboard(Integer mjSeq) {
        Optional<Mjboard> mjboard = this.mjboardRepository.findById(mjSeq);
        if (mjboard.isPresent()) {
            return mjboard.get();
        }else{
            throw  new DataNotFoundException("객체가 없네요..");
        }
}
public  void  modify(Mjboard mjboard, String mjTitle, String mjContent) {
        mjboard.setMjTitle(mjTitle);
        mjboard.setMjContent(mjContent);
        this.mjboardRepository.save(mjboard);
}
public void delete(Mjboard mjboard) {
        this.mjboardRepository.delete(mjboard); //mjboard에서 객체를 삭제하기 위함
}
@GetMapping("/mjRecommend/{mjSeq}")
public void mjRecommend(Mjboard mjboard) {
        mjboard.getMjRecommend();
        this.mjboardRepository.save(mjboard);

}




}
