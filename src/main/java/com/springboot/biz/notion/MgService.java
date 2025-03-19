package com.springboot.biz.notion;

import com.springboot.biz.DataNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MgService {

    private final MgRepository mgRepository;

    public Page<MgNotion> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("notionRegDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.mgRepository.findAll(pageable);
    }

    public MgNotion getMgNotion(Integer notionSeq) {
        Optional<MgNotion> mgNotion = this.mgRepository.findById(notionSeq);

        if (mgNotion.isPresent()) {
            return mgNotion.get();
        } else {
            throw new DataNotFoundException("검색한 데이터가 없습니다");
        }
    }

    public void create(String notionTitle, String notionContent, MultipartFile file) throws Exception {
       String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
        // Generate the file name with UUID
        UUID uuid = UUID.randomUUID();
        String frboFileName = uuid + "_" + file.getOriginalFilename();

        // Save the file to disk
        File saveFile = new File(projectPath, frboFileName);
        file.transferTo(saveFile);

        // Create MgNotion object
        MgNotion q = new MgNotion();
        q.setNotionTitle(notionTitle);
        q.setNotionContent(notionContent);
        q.setNotionRegDate(LocalDateTime.now());
        q.setFrboFilePath("/files/" + frboFileName);  // Use the correct file name
        q.setFrboFileName(frboFileName);

        // Save the MgNotion entity to the repository
        this.mgRepository.save(q);
    }

    public void modify(MgNotion mgNotion, String notionTitle, String notionContent, MultipartFile file)throws Exception  {
        mgNotion.setNotionTitle(notionTitle);
        mgNotion.setNotionContent(notionContent);
        mgNotion.setModifyDate(LocalDateTime.now());


        if (!file.isEmpty()) {
            String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
            UUID uuid = UUID.randomUUID();
            String frboFileName = uuid + "_" + file.getOriginalFilename();

            // 파일 저장
            File saveFile = new File(projectPath, frboFileName);
            file.transferTo(saveFile);

            // 파일 정보 저장 (기존 파일 대체)
            mgNotion.setFrboFilePath("/files/" + frboFileName);
            mgNotion.setFrboFileName(frboFileName);
        }

        this.mgRepository.save(mgNotion);
    }

    public void delete(MgNotion mgNotion) {
        this.mgRepository.delete(mgNotion);
    }


    public Page<MgNotion> searchNotions(String keyword, Pageable pageable) {
        return mgRepository.findByNotionTitleContainingIgnoreCaseOrNotionContentContainingIgnoreCase(keyword, keyword, pageable);
    }

    public Page<MgNotion> findAll(Pageable pageable) {
        return mgRepository.findAll(pageable);
    }


}