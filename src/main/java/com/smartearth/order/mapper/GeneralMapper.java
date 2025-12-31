package com.smartearth.order.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface GeneralMapper {

    List<Map<String, Object>> query(String column, String table, String condition);

    void deleteByPrimaryKey(String table, String id);

    void insert(String table, Map<String, Object> data);

    void update(String table, String id, Map<String, Object> data);

    default void update2(String table, String id, Map<String, Object> data){
        update(table, id, data);
    }
}
