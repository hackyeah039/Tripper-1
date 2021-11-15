package com.tripper.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author HanJiyoung
 * 크롤링한 호텔 데이터를 갖고있는 클래스
 */
@Getter
@Setter
@ToString
public class HotelInfo {
    private String name;
    private String imgsrc;
    private String lowestprice;

    public HotelInfo() {
    }
}
