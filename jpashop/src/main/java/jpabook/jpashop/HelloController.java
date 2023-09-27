package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello") // hello라는 url이 들어오면 메소드 호출
    public String hello(Model model) // model이라는 곳에 데이터를 심어서 View로 데이터를 넘길 수 있음
    {
        model.addAttribute("data", "안녕!!!");
        return "hello"; // return 값은 화면 이름, 렌더링 시 자동 이동


    }
}
