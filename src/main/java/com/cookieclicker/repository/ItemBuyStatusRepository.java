package com.cookieclicker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cookieclicker.entity.ItemBuyStatusEntity;

public interface ItemBuyStatusRepository extends JpaRepository <ItemBuyStatusEntity, String> {
	public List<ItemBuyStatusEntity> findByAuthId(String authId);
}
