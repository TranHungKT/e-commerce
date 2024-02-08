package com.ecommerce.admin.category;

import com.ecommerce.common.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> listAll(){
        return categoryRepository.findAll();
    }
}
