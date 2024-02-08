package com.ecommerce.admin.category;

import com.ecommerce.common.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public String listAll(Model model){
        List<Category> listCategories = categoryService.listAll();

        model.addAttribute("listCategories", listCategories);

        return "categories/categories";
    }
}
