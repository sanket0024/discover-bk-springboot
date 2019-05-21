package com.example.discover.repository;

import org.springframework.transaction.annotation.Transactional;

import com.example.discover.POJO.user.User;

/**
 * @Date : Apr 14, 2019
 *
 * @Author: Divyavijay Sahay  
 */
@Transactional
public interface UserRepository extends UserBaseRepository<User> {
	
}
