package it.home.travel.dao;

import it.home.travel.domain.Category;

import java.util.List;

/**
 * 操作tab_category表的接口
 */
public interface CRUDCategory {
    List<Category> findAll();
}
