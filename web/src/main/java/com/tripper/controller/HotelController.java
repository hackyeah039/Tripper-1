package com.tripper.controller;

import com.tripper.domain.HotelForm;
import com.tripper.domain.HotelInfo;
import com.tripper.service.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author HanJiyoung
 * '호텔 찾기' 기능 컨트롤러 클래스
 */
@Controller
public class HotelController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    /**
     * 호텔 검색 폼 페이지로 이동하는 함수
     * @return 호텔 검색 폼 페이지
     */
    @RequestMapping("/hotel")
    public String goHotelForm() {
        return "hotel_form";
    }

    /**
     * 호텔 크롤링 후 가져온 데이터를 model에 담아서 뷰페이지로 넘겨주는 함수
     * @param hotelForm 검색 폼에 입력된 데이터들을 담고있는 객체
     * @param model 뷰로 넘겨줄 모델 객체
     * @return 호텔 검색 결과 페이지
     */
    @PostMapping("/hotel/crawling")
    public String crawlHotel(@ModelAttribute HotelForm hotelForm, Model model){
        List<HotelInfo> hotels = hotelService.crawlingHotels(hotelForm);
//        logger.info(hotels.toString());
        model.addAttribute("hotelList", hotels);
        return "hotel_search_list";
    }
}