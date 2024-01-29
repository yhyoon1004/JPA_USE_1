package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j // 어노테이션이 자동으로 logger 생성
public class HomeController {

//    Logger logger = LoggerFactory.getLogger(getClass()) 기본 slf4j logger 생성법

    @RequestMapping("/home")
    public String home() {
        log.info("home controller");
        return "home";
    }
}


