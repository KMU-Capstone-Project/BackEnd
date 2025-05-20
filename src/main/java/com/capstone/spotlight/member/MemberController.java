package com.capstone.spotlight.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signup() {

        return "signup.html";
    }

    @PostMapping("/register")
    public String register(String username, String password, String email, String phone, String displayName) {

        Member member = new Member();

        member.setUsername(username);

        var hash = passwordEncoder.encode(password);
        member.setPassword(hash);

        member.setEmail(email);
        member.setPhone(phone);
        member.setDisplayName(displayName);

        memberRepository.save(member);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        var result = memberRepository.findByUsername("wkdwlsgh9622");
        System.out.println(result.get().getDisplayName());

        return "login.html";
    }

    @GetMapping("/my-page")
    public String myPage(Authentication auth, Model model) {
        var a = memberRepository.findById(1l);
        var result = a.get();
        var data = new MemberDto(result.getUsername(), result.getDisplayName());

        model.addAttribute("userData", data);


        return "mypage.html";
    }

    @GetMapping("/test")
    public String test(Authentication auth) {

        System.out.println(auth.getPrincipal());

        return "redirect:/";
    }

    @GetMapping("/find-id")
    public String getFindForm() {
        return "find-id.html";
    }

    @PostMapping("/post-find-id")
    public String postFindId(@RequestParam String email, Model model) {

        var result = memberRepository.findAllByEmail(email);
//        System.out.println(result.get().getUsername());

        if (result != null) {
            model.addAttribute("foundId", result.get().getUsername());
        } else {
            model.addAttribute("error", "일치하는 회원 정보가 없습니다.");
        }

        return "find-id-result.html";
    }

}

class MemberDto {
    public String username;
    public String displayName;
    MemberDto(String a, String b) {
        this.username = a;
        this.displayName = b;
    }
}
