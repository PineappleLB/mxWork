package dao;

import model.WarningConfig;

public interface WarningConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WarningConfig record);

    int insertSelective(WarningConfig record);

    WarningConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WarningConfig record);

    int updateByPrimaryKey(WarningConfig record);
}