package com.example.springedu2.controller;

import com.example.springedu2.entity.Visitor;
import com.example.springedu2.repository.VisitorRepository;
import jakarta.servlet.http.PushBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.RedirectViewControllerRegistration;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class VisitorController {

    // 1번) @Autowired 대신 생성자 주임
    // @Autowired
    // private VisitorRepository visitorRepository;

    // 2번) 생성자 주임 : 요즘 방식
    //    private VisitorRepository visitorRepository;
    //    public VisitorController(VisitorRepository visitorRepository) {
    //        this.visitorRepository = visitorRepository;
    //    }

    // 3) 생성자 주임 다른 방법
    // @RequiredArgsConstrunctor 필수 : Lombok 필수
    private final VisitorRepository visitorRepository;


    @GetMapping("/vlist")
    public ModelAndView vlist(){
        List<Visitor> visitors = visitorRepository.findAll(); // 목록 조회
        return visitorView(visitors, null);
    }

    private ModelAndView visitorView(List<Visitor> visitors, String buttonText) {
        ModelAndView mv = new ModelAndView("visitorView");
        // mv.setViewName("visitorView"); // visitorView.html(Model 사용) - thymeleaf
        if (visitors.isEmpty()) {
            mv.addObject("msg", "조회된 결과가 없습니다");
        } else {
            mv.addObject("vList", visitors);
        }
        if(buttonText != null) {
            mv.addObject("buttonText", buttonText);
        }
        return mv;
    }

    @GetMapping("/vsearch")
    public ModelAndView vsearch() {
        return null;
    }

    // @Valid : form 태그에서 넘어온 자료를 @Entity 에 있는 설정
    // 설정(@Id, @NotBlank, @Column(nullable=false)
    // 과 비교해서 입력 data 를 검증한다
    @PostMapping("/vinsert")
    @Transactional
    public String vinsert(@Valid Visitor visitor
            , BindingResult bindingResult
            , Model model) {

        System.out.println("visitor:" + visitor);
        System.out.println("bindingResult:" + bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("msg", "이름과 내용을 모두 입력하세요");
            return "visitorView";
        }
        visitorRepository.save(visitor); // entity type

        return "redirect:/vlist";
    }

    // /one 방명록 id 조회 : Rest 호출 결과 : json
    // return 값이 Visitor 객체인데 이것은 json으로 변경되어 다운로드됨
    // return 값이 ResponseEntity<Visitor> 일 때는 data는 json으로 상태코드 리턴가능
    @GetMapping(value = "/one", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<Visitor> one(@RequestParam Integer id) {
        return visitorRepository.findById(Long.valueOf(id))
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
                // 못찾으면 nill 대신해 404 코드를 객체로 바꿔서(.build()) 리턴
    }

    // /vupdate 수정
//    @PostMapping("/vupdate")
//    public String vupdate(@Valid Visitor visitor,
//                          BindingResult bindingResult,
//                          Model model, RedirectAttributes redirectAttributes, PushBuilder pushBuilder) {
//        if(bindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("msg", "수정할 이름과 내용을 모두 입력하세요");
//            return "redirect:/vlist";
//        }
//        // 수정
//        visitorRepository.save(visitor);
//        return "redirect:/vlist";
//    }

    @PostMapping("/vupdate")
    @Transactional
    public String vupdate(@Valid Visitor visitor,
                          BindingResult bindingResult,
                          Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("msg",
                    "수정할 이름과 내용을 모두 입력하세요");
            return "redirect:/vlist";
        }
        Visitor entity = visitorRepository.findById(Long.valueOf(visitor.getId()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방명록입니다"));
        entity.setName(visitor.getName());
        entity.setMemo(visitor.getMemo());
        return "redirect:/vlist";
    }

}
