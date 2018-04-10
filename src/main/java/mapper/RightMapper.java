package mapper;

import entity.Right;
import entity.RightExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RightMapper {
    int countByExample(RightExample example);

    int deleteByExample(RightExample example);

    int deleteByPrimaryKey(String rightId);

    int insert(Right record);

    int insertSelective(Right record);

    List<Right> selectByExample(RightExample example);

    Right selectByPrimaryKey(String rightId);

    int updateByExampleSelective(@Param("record") Right record, @Param("example") RightExample example);

    int updateByExample(@Param("record") Right record, @Param("example") RightExample example);

    int updateByPrimaryKeySelective(Right record);

    int updateByPrimaryKey(Right record);
}