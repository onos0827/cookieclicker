package com.cookieclicker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cookieclicker.entity.PictureDataEntity;

public interface PictureDataRepository extends JpaRepository <PictureDataEntity, String> {
}
