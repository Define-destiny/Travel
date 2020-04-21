package it.home.travel.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.home.travel.dao.CRUDCategory;
import it.home.travel.dao.impl.CRUDCategoryImpl;
import it.home.travel.service.CategoryService;
import it.home.travel.utils.JedisUtil;
import redis.clients.jedis.Jedis;

public class CategoryServiceImpl implements CategoryService {
    private CRUDCategory crudCategory = new CRUDCategoryImpl();
    private Jedis jedis = JedisUtil.getJedis();
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public String init() {
        if (jedis.get("category")==null||jedis.get("category").length()==0){
            try {
                jedis.set("category",mapper.writeValueAsString(crudCategory.findAll()));
            } catch (JsonProcessingException e) {
                System.out.println("redis add category error");
            }
        }
        return jedis.get("category");
    }
}
