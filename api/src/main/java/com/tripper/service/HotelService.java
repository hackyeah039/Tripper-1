package com.tripper.service;

import com.tripper.domain.HotelForm;
import com.tripper.domain.HotelInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author HanJiyoung
 * '호텔 찾기' 기능 컨트롤러 클래스
 */
@Service
public class HotelService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private WebDriver driver;
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "C:\\Temp\\driver\\chromedriver.exe";
    private String baseUrl = "https://hotel.naver.com/hotels/main";

    /**
     * '네이버 호텔'에서 지역, 체크인, 체크아웃, 성인 인원수, 어린이 인원수를 가지고 검색한 결과를 크롤링하는 함수
     * @param hotelForm 검색 폼에 입력된 데이터들을 담고있는 객체
     * @return 크롤링한 데이터를 담은 객체들을 담고있는 리스트
     */
    public List<HotelInfo> crawlingHotels(HotelForm hotelForm) {
        List<HotelInfo> hotelList = new ArrayList<>();

        /* 크롬 드라이버 로딩 후 baseUrl페이지를 불러온다 */
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        driver = new ChromeDriver();
        driver.get(baseUrl);
        WebDriverWait wait = new WebDriverWait(driver, 50);

        try {
            /* 네이버 호텔 메인 페이지의 지역 input 태그에 파라미터로 넘어온 location 값을 넣고 엔터 */
            WebElement locationForm = driver.findElement(By.id("hotel_search"));
            locationForm.sendKeys(hotelForm.getLocation());
            locationForm.sendKeys(Keys.ENTER);

            /* '호텔 검색' 버튼 클릭*/
            WebElement searchButton = driver.findElement(By.cssSelector("body > div > div > div.container.main.ng-scope > " +
                    "div.aside > div:nth-child(1) > div > div.search_select_box > a"));
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("arguments[0].click()", searchButton);

            /*
             * 여기까지 하면 location='제주'일 경우,
             * https://hotel.naver.com/hotels/list?destination=place:Jeju&rooms=2
             * -> 이렇게목적지 파라미터 뒤에 rooms파라미터(인원수)가 자동으로 붙음.
             * 그래서 Jeju까지만 url을 split 해주고, 그 뒤에 Tripper 사용자가 입력했던 정보들을 넣어줘서 최종 url(rsltUrl)을 완성함.
             */
            String curUrl = driver.getCurrentUrl();
            String splitUrl = curUrl.split("&")[0];
            String rsltUrl = splitUrl + hotelForm.makeRsltUrl();
            driver.get(rsltUrl);
            logger.info("rsltUrl=" + rsltUrl);
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

            /* 페이지 번호 개수 세어서 아래 for문 반복 횟수(repeatNum)를 구함 */
            WebElement pageList = driver.findElement(By.cssSelector("body > div > div > div.container.ng-scope > " +
                    "div.content > div.hotel_list_area > div.paginate.ng-scope"));
            List<WebElement> pages = pageList.findElements(By.className("ng-scope"));
            int repeatNum = pages.size();

            for(int i = 0; i < repeatNum; i++) {
                /* 한 페이지의 호텔들이 모두 담겨있는 ul */
                WebElement ul = driver.findElement(By.cssSelector("body > div > div > div.container.ng-scope > " +
                        "div.content > div.hotel_list_area > ul"));

                /* ul 하위 li들 */
                List<WebElement> li = ul.findElements(By.cssSelector("body > div > div > div.container.ng-scope > " +
                        "div.content > div.hotel_list_area > ul > li"));

                /* li개수만큼 반복하면서 각 호텔의 이름, 이미지, 최저가 가져옴 */
                for(WebElement e : li) {
                    WebElement name = e.findElement(By.tagName("strong"));
                    List<WebElement> imgs = e.findElements(By.tagName("img"));
                    WebElement img = imgs.get(1);
                    String imgSrc = img.getAttribute("src");
                    WebElement lowestPrice = e.findElement(By.tagName("em"));
//                    logger.info("name:" + name.getText() + " imgSrc:" + imgSrc + " lowestPrice:" + lowestPrice.getText());

                    /* 크롤링 한 호텔 정보들을 넣을 객체 */
                    HotelInfo hotel = new HotelInfo();
                    hotel.setName(name.getText());
                    hotel.setImgsrc(imgSrc);
                    hotel.setLowestprice(lowestPrice.getText());

                    hotelList.add(hotel);
                }
                WebElement nextButton = driver.findElement(By.xpath("/html/body/div/div/div[1]/div[2]/div[6]/div[2]/a[2]/span[1]"));
                nextButton.click();

                /* '다음' 버튼 클릭 후 페이지 로딩 기다림 */
                Thread.sleep(1000);
            }
            return hotelList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }

        return hotelList;
    }
}
