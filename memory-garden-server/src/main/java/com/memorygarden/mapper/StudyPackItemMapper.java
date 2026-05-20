package com.memorygarden.mapper;

import com.memorygarden.model.entity.StudyPackItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 知识包条目 Mapper 接口
 *
 * @author jLU
 * @date 2026-05-20
 */
@Mapper
public interface StudyPackItemMapper {

    /**
     * 根据知识包 ID 查询条目列表
     *
     * @param packId 知识包 ID
     * @return 条目列表
     */
    List<StudyPackItem> selectByPackId(Long packId);
}
