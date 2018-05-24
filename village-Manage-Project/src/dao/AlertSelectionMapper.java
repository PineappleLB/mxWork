package dao;

import model.AlertSelection;

public interface AlertSelectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AlertSelection record);

    int insertSelective(AlertSelection record);

    AlertSelection selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AlertSelection record);

    int updateByPrimaryKey(AlertSelection record);
}