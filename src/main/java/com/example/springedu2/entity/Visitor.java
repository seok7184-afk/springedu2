package com.example.springedu2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Visitor {

    @Id // 기본키 : primary key
    @GeneratedValue(strategy = GenerationType.AUTO) //  번호 자동 증가
    private Integer id; // 방명록 번호 id : 기본키

    // 작성자: name
    // NotBlank: null, ""(빈문자열), "  " 공백 포함된 문자열 전부 X
    // Size(max=50): 문자열(50문자), 배열(50개), 숫자 (50 이하)
    @NotBlank(message = "이름은 필수입니다.")
    @Size(max=50)
    private String  name;

    // 작성일: writeDate
    // CreationTimestamp: 데이터 등록일 자동 입력 일일이 LocalDateTime.now() 넣지 않아도 됨
    // Column
    @CreationTimestamp
    @Column(name="writedate", nullable = false, updatable = false)
    private String writeDate;

    // 방명록 내용: memo
    @NotBlank(message = "입력해 주세요.")
    @Size(max=100)
    private String memo;
}
