package com.cookieclicker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cookieclicker.entity.ItemBuyStatusEntity;
import com.cookieclicker.entity.ItemBuyStatusPrimaryKey;

@Repository
public interface ItemBuyStatusRepository extends JpaRepository<ItemBuyStatusEntity, ItemBuyStatusPrimaryKey> {
	public List<ItemBuyStatusEntity> findByAuthId(String authId);

	@Query(value="select COUNT_BUY_ITEM from TBL_ITEM_BUY_STATUS a where a.AUTH_ID = :AUTH and a.ITEM_ID= :ITEM_ID",nativeQuery = true)
    public Integer findCountBuyItem(@Param("AUTH") String auth, @Param("ITEM_ID") String itemId);


}
