package com.example.discover.repository;

import org.springframework.transaction.annotation.Transactional;

import com.example.discover.POJO.user.User;


@Transactional
public interface UserRepository extends UserBaseRepository<User> {
	
}
