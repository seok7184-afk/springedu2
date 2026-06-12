package com.example.springedu2.controller;

import com.example.springedu2.entity.Visitor;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class VisitorController {

    @GetMapping("/vlist")
    public ModelAndView vlist(){
        return null;
    }

    @GetMapping("/vsearch")
    public ModelAndView vsearch() {
        return null;
    }

    // @Valid : form 태그에서 넘어온 자료를 @Entity 에 있는 설정
    // 설정(@Id, @NotBlank, @Column(nullable=false)
    // 과 비교해서 입력 data 를 검증한다
    @PostMapping("/vinsert")
    public String vinsert(@Valid Visitor visitor
            , Model model) {
        return "redirect:/vlist";
    }

}
