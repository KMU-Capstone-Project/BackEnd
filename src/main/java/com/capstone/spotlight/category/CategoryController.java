package com.capstone.spotlight.category;

import com.capstone.spotlight.item.Item;
import com.capstone.spotlight.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    @GetMapping("/category/{id}")
    public String getCategoryItems(@PathVariable("id") Long categoryId, Model model) {
        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (category == null) {
            return "error/404";
        }

        List<Item> items = itemRepository.findByCategoryId(categoryId);


        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        model.addAttribute("category", category);
        model.addAttribute("items", items);

        return "category-items";
    }


}
