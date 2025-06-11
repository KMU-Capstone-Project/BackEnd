package com.capstone.spotlight.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 이름으로 카테고리 찾기
    // Category findByName(String name);
}
