package com.capstone.spotlight.member;

import com.capstone.spotlight.sales.Sales;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    private String email;

    private String phone;
    private String displayName;

////    Member 행 출력시 그 Member가 기록된 Sales 행도 전부 출력한다는 소리
//    근데 그냥 salesRepository.findByMemberId() 이런식으로 쓰는게 더 편함
//    @ToString.Exclude // stack overflow 오류나면 이 코드 넣으셈
//    @OneToMany(mappedBy = "member")
//    List<Sales> sales = new ArrayList<>();

}
