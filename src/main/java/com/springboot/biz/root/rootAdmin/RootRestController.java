package com.springboot.biz.root.rootAdmin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RootRestController {

    private final RootService rootService;

    @PostMapping("/root/register/form/save")
    public ResponseEntity<String> saveRoot(@RequestBody RootDTO rootDTO) {
        System.out.println(rootDTO.getTitle());
        List<RootListDTO> list = rootDTO.getRootList();
        rootService.save(rootDTO.getTitle(), rootDTO.getRootList());

        if (list == null) {
            System.out.println("❌ rootList가 null입니다!");
        } else {
            System.out.println("✅ rootList 개수: " + list.size());
            System.out.println("controller"+list.get(1).getRoadaddress());
        }
        return ResponseEntity.ok("성공~");
    }

}
