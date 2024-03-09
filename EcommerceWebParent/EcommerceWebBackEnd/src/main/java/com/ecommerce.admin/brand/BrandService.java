package com.ecommerce.admin.brand;

import com.ecommerce.common.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    public static final Integer BRANDS_PER_PAGE = 4;
    public List<Brand> listAll(){
        return brandRepository.findAll();
    }

    public Page<Brand> listByPage(Integer pageNum,String sortField , String sortDir, String keyword){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);

        if(isEmptyString(keyword)){
            return brandRepository.findAll(pageable);
        }
        return brandRepository.findByPage(keyword, pageable);
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

    private Boolean isEmptyString(String key) {
        return key == null || key.isEmpty();
    }

}
