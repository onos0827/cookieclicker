package com.cookieclicker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cookieclicker.entity.UserEntity;

public interface UserRepository extends JpaRepository <UserEntity, String> {
	public List<UserEntity> findByAuthId(String auth);
}
