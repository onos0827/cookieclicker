package com.cookieclicker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cookieclicker.entity.ItemDataEntity;

public interface ItemDataRepository extends JpaRepository <ItemDataEntity, String> {
	public List<ItemDataEntity> findAllByOrderByDisplayOrder();
	public List<ItemDataEntity> findByItemId(String itemId);
}
