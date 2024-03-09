package com.ecommerce.admin.brand;

import com.ecommerce.common.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    public Brand findByName(String name);
}
