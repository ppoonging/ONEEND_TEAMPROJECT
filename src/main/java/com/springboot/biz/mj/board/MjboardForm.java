package com.springboot.biz.mj.board;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class MjboardForm {

    @NotEmpty(message = "제목을 입력해주세요")// 문자가 들어오지 않으면 저장되지 않게 하기위해 만듬
    @Size(max = 200) //제목 사이즈를 설정
    private String mjTitle;

    @NotEmpty(message = "내용을 입력해주세요")
    private String mjContent;

    private MultipartFile file;

    // map 관련
    private String mjMapTitle;  //목적지명

    private String mjMapAddress;  //주소

    private String mjMapRodeAddress; //도로명

    @NotEmpty(message = "위치를 등록해주세요")
    private Double mjMapLatitude;  //위도

    @NotEmpty(message = "위치를 등록해주세요")
    private Double mjMapLongitude;  //경도

    private String mjMapLink; // 장소 정보 url

    private String mjMapCategory; // 장소 분류

}
