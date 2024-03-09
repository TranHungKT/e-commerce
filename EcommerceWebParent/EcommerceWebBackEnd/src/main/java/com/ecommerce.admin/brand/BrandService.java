package com.ecommerce.admin.brand;

import com.ecommerce.common.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public List<Brand> listAll(){
        return brandRepository.findAll();
    }

    public Brand saveBrand(Brand brand){
        return  brandRepository.save(brand);
    }

    public Brand getBrandById(Integer id) throws BrandNotFoundException {
        return brandRepository.findById(id).orElseThrow(() -> new BrandNotFoundException("Can not find this brand"));
    }

    public void deleteBrandById(Integer id) throws BrandNotFoundException {
        brandRepository.findById(id).orElseThrow(() -> new BrandNotFoundException("Can not find this brand"));

        brandRepository.deleteById(id);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);
        Brand brandByName = brandRepository.findByName(name);

        if(brandByName == null){
            return "OK";
        }

        if(isCreatingNew || !Objects.equals(brandByName.getId(), id)){
            return "Duplicate";
        }

        return "OK";
    }
}
