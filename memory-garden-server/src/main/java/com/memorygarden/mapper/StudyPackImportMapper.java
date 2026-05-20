package com.memorygarden.mapper;

import com.memorygarden.model.entity.StudyPackImport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户导入记录 Mapper 接口
 *
 * @author jLU
 * @date 2026-05-20
 */
@Mapper
public interface StudyPackImportMapper {

    /**
     * 插入导入记录
     *
     * @param record 导入记录
     * @return 影响行数
     */
    int insert(StudyPackImport record);

    /**
     * 判断用户是否已导入指定知识包
     *
     * @param userId 用户 ID
     * @param packId 知识包 ID
     * @return true=已导入
     */
    boolean existsByUserIdAndPackId(@Param("userId") Long userId, @Param("packId") Long packId);
}
