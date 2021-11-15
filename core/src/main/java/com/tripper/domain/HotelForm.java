package com.tripper.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author HanJiyoung
 * 사용자가 호텔 검색시 입력한 값들을 가지고 있는 클래스
 */
@Getter
@Setter
public class HotelForm {
    private String location;
    private String checkin;
    private String checkout;
    private String adult;
    private String child;

    public HotelForm(String location, String checkin, String checkout, String adult, String child) {
        this.location = location;
        this.checkin = checkin;
        this.checkout = checkout;
        this.adult = adult;
        this.child = child;
    }

    public String makeRsltUrl() {
        return "&checkin=" + checkin + "&checkout=" + checkout + "&rooms=" + adult + ":" + child;
    }
}
