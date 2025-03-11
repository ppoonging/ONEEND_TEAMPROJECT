package com.springboot.biz.root.rootAdmin;

import com.springboot.biz.user.HUser;
import com.springboot.biz.user.HUserSerevice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RootRestController {

    private final RootService rootService;
    private final HUserSerevice hUserSerevice;

//    // 그냥 Controller 로 옮김 -> script에서 fetch 로 처리하는걸 form submit 으로 처리
//    @PostMapping("/root/register/form/save")
//    public ResponseEntity<String> saveRoot(@RequestBody RootDTO rootDTO, Principal principal) {
//        System.out.println(rootDTO.getTitle());
//        List<RootListDTO> list = rootDTO.getRootList();
//
//        HUser user = this.hUserSerevice.getUser(principal.getName());
//
//        rootService.save(rootDTO.getTitle(), rootDTO.getRootList(),user);
//
//        return ResponseEntity.ok("성공~");
//    }

    @GetMapping("/root/form/{rootSeq}")
    public ResponseEntity<List<RootList>> getRootList(@PathVariable("rootSeq") Integer rootSeq) {
       List<RootList> rootLists = rootService.getRootList(rootSeq);

        for (RootList list : rootLists) {
            System.out.println(list.getRootListAddress());
            System.out.println(list.getRootListTitle());
            System.out.println(list.getRootListLatitude());
            System.out.println(list.getRootListRodeAddress());
        }
        System.out.println(rootLists);
      return ResponseEntity.ok(rootLists);
    }



}
