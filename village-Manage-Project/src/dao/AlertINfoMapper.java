package dao;

import model.AlertINfo;

public interface AlertINfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AlertINfo record);

    int insertSelective(AlertINfo record);

    AlertINfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AlertINfo record);

    int updateByPrimaryKey(AlertINfo record);
}