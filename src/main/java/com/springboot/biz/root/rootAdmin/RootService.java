package com.springboot.biz.root.rootAdmin;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.biz.DataNotFoundException;
import com.springboot.biz.root.rootUser.RootAuth;
import com.springboot.biz.root.rootUser.RootAuthList;
import com.springboot.biz.user.HUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RootService {

    private final RootRepository rootRepository;
    private final RootListRepository rootListRepository;

    public List<Root> getList() {
        return this.rootRepository.findAll();
    }

    public void delete(Integer rootSeq) {
        List<RootList> rootList = this.rootListRepository.findByRootId(rootSeq);
        for(RootList rel : rootList) {
            Integer seq = rel.getRoot().getRootSeq();
            if(Objects.equals(seq, rootSeq)){
                this.rootListRepository.deleteById(seq);
            }
        }
        this.rootRepository.deleteById(rootSeq);
    }

    public void save(String title, boolean rootState, List<RootListDTO> rootListDTO, HUser user) {
        Root root = new Root();
        root.setRootTitle(title);
        root.setUserId(user);

        // System.out.println("service"+rootListDTO.get(1).getRoadaddress());

        List<RootList> rootList = new ArrayList<>();

        root.setRootState(rootState);
        root.setRootList(rootList);
        root.setRootDate(LocalDateTime.now());

        this.rootRepository.save(root);

        Integer index = 1;

        for(RootListDTO rel : rootListDTO) {
            RootList list = RootList.builder()
                    .root(root)
                    .rootListTitle(rel.getTitle())
                    .rootListAddress(rel.getAddress())
                    .rootListRodeAddress(rel.getRoadaddress())
                    .rootListLatitude(rel.getLatitude())
                    .rootListLongitude(rel.getLongitude())
                    .rootListLink(rel.getLink())
                    .rootListCategory(rel.getCategory())
                    .rootListIndex(index++)
                    .userId(user)
                    .build();

            this.rootListRepository.save(list);

        }

    }

    @Transactional
    public void modify(Integer rootSeq, String title, boolean rootState, List<RootListDTO> rootListDTO, HUser user) {
        Root root = rootRepository.findById(rootSeq)
                .orElseThrow(() -> new IllegalArgumentException("수정할 데이터가 없습니다."));

        root.setRootTitle(title);
        root.setRootModifyDate(LocalDateTime.now());
        root.setRootState(rootState);

        // 기존 RootList 모두 삭제
        rootListRepository.deleteByRoot(root);

        int index = 1;
        for (RootListDTO rel : rootListDTO) {
            RootList list = RootList.builder()
                    .root(root)
                    .rootListTitle(rel.getTitle())
                    .rootListAddress(rel.getAddress())
                    .rootListRodeAddress(rel.getRoadaddress())
                    .rootListLatitude(rel.getLatitude())
                    .rootListLongitude(rel.getLongitude())
                    .rootListLink(rel.getLink())
                    .rootListCategory(rel.getCategory())
                    .rootListIndex(index++)
                    .userId(user)
                    .build();

            this.rootListRepository.save(list);
        }

        rootRepository.save(root);
    }

    public List<RootList> getRootList(Integer rootSeq) {
        return rootListRepository.findByRootId(rootSeq);
    }

    public Root get(Integer rootSeq) {
        Optional<Root> root = this.rootRepository.findById(rootSeq);
        if(root.isPresent()) {
            return root.get();
        }

       throw new DataNotFoundException("루트 데이터가 없습니다.");
    }



}
