package com.capstone.spotlight.item;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByTitleContains(String title);

    @Query(value = "select * from item where match(title) against(?1)", nativeQuery = true)
    List<Item> fullTextIndex(String text);

    @Query("SELECT i FROM Item i JOIN i.categories c WHERE c.id = :categoryId")
    List<Item> findByCategoryId(Long categoryId);
}
