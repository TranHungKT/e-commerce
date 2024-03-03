package com.ecommerce.admin.brand;

import com.ecommerce.common.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public List<Brand> listAll(){
        return brandRepository.findAll();
    }
}
