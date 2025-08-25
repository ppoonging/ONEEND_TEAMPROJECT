package com.springboot.biz.map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MapService {

    private final RestTemplate restTemplate;

//    @Value("${naver.client-id}")
//    private String id;
//
//    @Value("${naver.client-secret}")
//    private String secret;
//
//    @Value("${naver.url.search.local}")
//    private String localUrl;

    @Value("${kakao.rest-api-key}")
    private String directionSecret;

//    public List<Map<String, String>> search (String query) {
//
//        List<Map<String, String>> restaurants = new ArrayList<>();
//
//        try {
//            ByteBuffer buffer = StandardCharsets.UTF_8.encode(query);
//            String encode = StandardCharsets.UTF_8.decode(buffer).toString();
//
//            URI uri = UriComponentsBuilder.fromUriString(localUrl)
//                    .queryParam("query", encode)
//                    .queryParam("display", 5)
//                    .queryParam("start", 1)
//                    .queryParam("sort", "random")
//                    .encode()
//                    .build()
//                    .toUri();
//
//            RequestEntity<Void> rel = RequestEntity.get(uri)
//                    .header("X-Naver-Client-Id", id)
//                    .header("X-Naver-Client-Secret", secret)
//                    .build();
//
//            ResponseEntity<String> response = restTemplate.exchange(rel, String.class);
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode rootNode = objectMapper.readTree(response.getBody());
//
//            System.out.println(response.getBody());
//
//            JsonNode itemsNode = rootNode.path("items");
//            for (JsonNode itemNode : itemsNode) {
//                Map<String, String> restaurant = new HashMap<>();
//                restaurant.put("title", itemNode.path("title").asText().replaceAll("<b>|</b>", "").replaceAll("&amp;", "&")); // 장소 이름
//                restaurant.put("address", itemNode.path("address").asText());
//                restaurant.put("roadAddress", itemNode.path("roadAddress").asText());// 장소 주소
//                restaurant.put("link", itemNode.path("link").asText());// 장소 상세 정보 url
//                restaurant.put("category", itemNode.path("category").asText());// 장소 분류 정보
//
//                double latitude = Double.parseDouble(itemNode.path("mapy").asText()) / 1e7;
//                double longitude = Double.parseDouble(itemNode.path("mapx").asText()) / 1e7;
//                restaurant.put("latitude", String.valueOf(latitude));
//                restaurant.put("longitude", String.valueOf(longitude));
//
//                restaurants.add(restaurant); // 리스트에 추가
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return  restaurants;
//
//    }

    public Map<String, Object> directions(String start, String goal) {
        String url = "https://apis-navi.kakaomobility.com/v1/directions";
        String priority = "RECOMMEND";

        List<List<Double>> pathList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("origin", start)
                .queryParam("destination", goal)
                .queryParam("priority", priority)
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> request = RequestEntity.get(uri)
                .header("Authorization", "KakaoAK " + directionSecret)
                .build();

        try {
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            JsonNode routes = rootNode.path("routes");
            if (routes.isMissingNode() || routes.isEmpty()) {
                throw new IllegalStateException("카카오 API 응답에 route 데이터가 없습니다.");
            }

            JsonNode section = routes.get(0).path("sections").get(0);
            JsonNode roads = section.path("roads");

            for (JsonNode road : roads) {
                JsonNode vertexes = road.path("vertexes");
                for (int i = 0; i < vertexes.size(); i += 2) {
                    double x = vertexes.get(i).asDouble();     // 경도
                    double y = vertexes.get(i + 1).asDouble(); // 위도
                    pathList.add(List.of(y, x)); // 카카오 → 위도, 경도 순서
                }

            }

            JsonNode summary = routes.get(0).path("summary");
            int distance = summary.path("distance").asInt();
            int duration = summary.path("duration").asInt();

            result.put("path", pathList);
            result.put("summary", Map.of("distance", distance, "duration", duration));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("카카오 길찾기 처리 실패: " + e.getMessage());
            result.clear();  // 안전하게 초기화
            result.put("error", "카카오 경로 처리 중 오류 발생");

        }

        return result;
    }

}
