package com.ecommerce.admin.brand;

import com.ecommerce.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNullApi;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    public Brand findByName(String name);

    @Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
    public Page<Brand> findByPage(String keyword, Pageable pageable);
}
