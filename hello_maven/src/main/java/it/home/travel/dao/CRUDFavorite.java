package it.home.travel.dao;

import it.home.travel.domain.Favorite;

/**
 * 操作tab_favorite表的接口
 */
public interface CRUDFavorite {
    Favorite findByRidAUid(int rid, int uid);
    boolean insertOne(Favorite favorite);
    int findTotalCount(int uid);
}
