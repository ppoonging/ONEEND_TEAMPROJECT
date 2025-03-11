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

    private final RestTemplate restTemplate;
    private final RootRepository rootRepository;
    private final RootListRepository rootListRepository;

    @Value("${naver.client-id}")
    private String id;

    @Value("${naver.client-secret}")
    private String secret;

    @Value("${naver.url.search.local}")
    private String localUrl;

    public List<Map<String, String>> search (String query) {

        List<Map<String, String>> restaurants = new ArrayList<>();

        try {
            ByteBuffer buffer = StandardCharsets.UTF_8.encode(query);
            String encode = StandardCharsets.UTF_8.decode(buffer).toString();

            URI uri = UriComponentsBuilder.fromUriString(localUrl)
                    .queryParam("query", encode)
                    .queryParam("display", 5)
                    .queryParam("start", 1)
                    .queryParam("sort", "random")
                    .encode()
                    .build()
                    .toUri();

            RequestEntity<Void> rel = RequestEntity.get(uri)
                    .header("X-Naver-Client-Id", id)
                    .header("X-Naver-Client-Secret", secret)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(rel, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            System.out.println(response.getBody());

            JsonNode itemsNode = rootNode.path("items");
            for (JsonNode itemNode : itemsNode) {
                Map<String, String> restaurant = new HashMap<>();
                restaurant.put("title", itemNode.path("title").asText().replaceAll("<b>|</b>", "").replaceAll("&amp;", "&")); // 장소 이름
                restaurant.put("address", itemNode.path("address").asText());
                restaurant.put("roadAddress", itemNode.path("roadAddress").asText());// 장소 주소
                restaurant.put("link", itemNode.path("link").asText());// 장소 상세 정보 url
                restaurant.put("category", itemNode.path("category").asText());// 장소 분류 정보

                double latitude = Double.parseDouble(itemNode.path("mapy").asText()) / 1e7;
                double longitude = Double.parseDouble(itemNode.path("mapx").asText()) / 1e7;
                restaurant.put("latitude", String.valueOf(latitude));
                restaurant.put("longitude", String.valueOf(longitude));

                restaurants.add(restaurant); // 리스트에 추가
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

            return  restaurants;

    }

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

    public void save(String title, List<RootListDTO> rootListDTO, HUser user) {
        Root root = new Root();
        root.setRootTitle(title);
        root.setUserId(user);


        // System.out.println("service"+rootListDTO.get(1).getRoadaddress());

        List<RootList> rootList = new ArrayList<>();

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
//            index++;
        }

    }

    @Transactional
    public void modify(Integer rootSeq, String title, List<RootListDTO> rootListDTO, HUser user) {
        Root root = rootRepository.findById(rootSeq)
                .orElseThrow(() -> new IllegalArgumentException("수정할 데이터가 없습니다."));

        root.setRootTitle(title);
        root.setRootModifyDate(LocalDateTime.now());

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
