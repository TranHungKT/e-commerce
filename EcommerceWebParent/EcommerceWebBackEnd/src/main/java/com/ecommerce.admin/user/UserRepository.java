package com.ecommerce.admin.user;

import com.ecommerce.common.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
