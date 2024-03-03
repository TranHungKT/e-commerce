package com.ecommerce.admin.brand;

import com.ecommerce.common.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    @GetMapping("/brands")
    public String listBrands(Model model){
        List<Brand> listBrands = brandService.listAll();
        model.addAttribute("listBrands", listBrands);

        return "brands/brands";
    }
}
