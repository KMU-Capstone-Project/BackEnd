package com.capstone.spotlight.item;

import com.capstone.spotlight.category.Category;
import com.capstone.spotlight.category.CategoryRepository;
import com.capstone.spotlight.comment.Comment;
import com.capstone.spotlight.comment.CommentRepository;
import com.capstone.spotlight.image.Image;
import com.capstone.spotlight.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BlobStorageService blobStorageService;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping("/")
    String getMain(Model model) {
        List<Item> result = itemRepository.findAll();
        List<Map<String, String>> banners = List.of(
                Map.of("src", "/bannerImages/banner1.jpg", "alt", "첫 번째 배너"),
                Map.of("src", "/bannerImages/banner2.jpg", "alt", "두 번째 배너")
                // Map.of("src", "/images/banner3.jpg", "alt", "세 번째 배너")
        );
        model.addAttribute("banners", banners);

        model.addAttribute("items", result);

        // 카테고리 내역 가져오는 코드
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "mainpage.html";
    }


    @GetMapping("/write")
    String write(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "write.html";
    }

    @PostMapping("/add")
    String addPost(String title, Integer price, String brand, String description, @RequestParam String imageUrl, @RequestParam List<Long> categoryIds) {
        // item 정보 저장
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        item.setBrand(brand);
        item.setDescription(description);

        itemRepository.save(item);

        // item 이미지 url 저장
        Image image = new Image();
        image.setUrl(imageUrl);
        image.setItem(item);

        imageRepository.save(image);

        // item 카테고리 저장
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        item.setCategories(categories);

        itemRepository.save(item);

        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    String detail(@PathVariable Long id, Model model) {

        var commentData = commentRepository.findAllByParentId(id);

        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("itemData", result.get());
            model.addAttribute("commentData", commentData);
            return "detail.html";
        } else {
            return "redirect:/";
        }
    }


    @GetMapping("/{id}/edit")
    String edit(@PathVariable Long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("newData", result.get());

            return "edit.html";
        } else {
            return "redirect:/";
        }
    }

    // 예외상황(가격에 문자들어간 경우) 제어하도록 코드 추가하셈
    @PostMapping("/edit")
    String editItem(Long id, String title, Integer price, String brand) {

        Item item = new Item();
        item.setId(id);
        item.setTitle(title);
        item.setPrice(price);
        item.setBrand(brand);

        itemRepository.save(item);

        return "redirect:/";
    }

    // query string으로 반환(쉬운데 전송 데이터 다 보임)
//    @DeleteMapping("/item")
//    ResponseEntity<String> delete(@RequestParam Long id) {
//
//        itemRepository.deleteById(id);
//
//        return ResponseEntity.status(200).body("삭제완료");
//    }

    // RESTful API JSON형태로 반환(전송 데이터 안보임)
    @DeleteMapping("/item")
    ResponseEntity<String> delete(@RequestBody Map<String, Long> body) {

        itemRepository.deleteById(body.get("id"));

        return ResponseEntity.status(200).body("삭제완료");
    }

    @PostMapping("/search")
    String postSearch(@RequestParam String searchText, Model model) {

        var result = itemRepository.fullTextIndex(searchText);

        model.addAttribute("searchData", result);

        // 카테고리 내역 가져오는 코드
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        return "search.html";
    }


    // 이미지 업로드 요청
    @GetMapping("/presigned-url")
    @ResponseBody
    String getURL(@RequestParam String filename, Model model) {
        var result = blobStorageService.generatePresignedUploadUrl("test/" + filename);


        System.out.println(result);

        return result;
    }

    @GetMapping("/size-guide")
    String getGuide() {

        return "size-guide.html";
    }

}
