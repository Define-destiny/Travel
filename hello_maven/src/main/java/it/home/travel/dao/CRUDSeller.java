package it.home.travel.dao;

import it.home.travel.domain.Seller;

/**
 * 操作tab_seller表
 */
public interface CRUDSeller {
    Seller findBySid(int sid);
}
