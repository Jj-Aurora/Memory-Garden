package com.memorygarden.service;

import com.memorygarden.model.entity.StudyPack;
import com.memorygarden.model.entity.StudyPackItem;

import java.util.List;

/**
 * 预设知识包服务接口
 *
 * @author jLU
 * @date 2026-05-20
 */
public interface StudyPackService {

    /**
     * 获取可用知识包列表
     *
     * @return 知识包列表
     */
    List<StudyPack> list();

    /**
     * 获取知识包详情（含条目预览）
     *
     * @param packId 知识包 ID
     * @return 知识包实体
     */
    StudyPack getDetail(Long packId);

    /**
     * 获取知识包条目列表
     *
     * @param packId 知识包 ID
     * @return 条目列表
     */
    List<StudyPackItem> getPackItems(Long packId);

    /**
     * 导入知识包到个人花园
     *
     * @param userId 用户 ID
     * @param packId 知识包 ID
     * @return 导入的卡片数量
     */
    int importPack(Long userId, Long packId);
}
