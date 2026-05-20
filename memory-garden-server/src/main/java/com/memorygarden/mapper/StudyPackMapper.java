package com.memorygarden.mapper;

import com.memorygarden.model.entity.StudyPack;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 预设知识包 Mapper 接口
 *
 * @author jLU
 * @date 2026-05-20
 */
@Mapper
public interface StudyPackMapper {

    /**
     * 查询所有知识包
     *
     * @return 知识包列表
     */
    List<StudyPack> selectAll();

    /**
     * 根据 ID 查询知识包
     *
     * @param id 知识包 ID
     * @return 知识包实体
     */
    StudyPack selectById(Long id);
}
