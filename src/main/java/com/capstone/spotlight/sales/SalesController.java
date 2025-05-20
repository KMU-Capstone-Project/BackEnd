package com.capstone.spotlight.sales;

import com.capstone.spotlight.member.CustomUser;
import com.capstone.spotlight.member.Member;
import com.capstone.spotlight.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SalesController {

    private final SalesRepository salesRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/order")
    String postOrder(@RequestParam String title,
                     @RequestParam Integer price,
                     @RequestParam Integer count,
                     Authentication auth) {
        Sales sales = new Sales();
        sales.setCount(count);
        sales.setPrice(price);
        sales.setItemName(title);
        CustomUser user = (CustomUser) auth.getPrincipal();
        var member = new Member();
        member.setId(user.id);
        sales.setMember(member);

        salesRepository.save(sales);

        return "redirect:/";
    }

    // 주문 목록
    @GetMapping("/order/list")
    String getOrderList(Model model) {
        List<Sales> result = salesRepository.customFindAll();

        List<SalesDto> salesDtoList = new ArrayList<>();
        for (Sales sales : result) {
            String itemName = sales.getItemName();
            Integer price = sales.getPrice();
            String username = sales.getMember().getUsername();
            String displayName = sales.getMember().getDisplayName();
            Integer count = sales.getCount();

            SalesDto dto = new SalesDto(itemName, price, username, displayName, count);
            salesDtoList.add(dto);
        }
        model.addAttribute("salesDtoList", salesDtoList);

        return "orderList.html";
    }

    class SalesDto {
        public String itemName;
        public Integer price;
        public String username;
        public String displayName;
        public Integer count;
        SalesDto(String a, Integer b, String c, String d, Integer e) {
            this.itemName = a;
            this.price = b;
            this.username = c;
            this.displayName = d;
            this.count = e;
        }
    }
}
