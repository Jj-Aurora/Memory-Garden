package com.memorygarden.service.impl;

import com.memorygarden.common.exception.BusinessException;
import com.memorygarden.common.result.ResultCode;
import com.memorygarden.mapper.KnowledgeCardMapper;
import com.memorygarden.mapper.PlantMapper;
import com.memorygarden.mapper.StudyPackImportMapper;
import com.memorygarden.mapper.StudyPackItemMapper;
import com.memorygarden.mapper.StudyPackMapper;
import com.memorygarden.model.entity.KnowledgeCard;
import com.memorygarden.model.entity.Plant;
import com.memorygarden.model.entity.StudyPack;
import com.memorygarden.model.entity.StudyPackImport;
import com.memorygarden.model.entity.StudyPackItem;
import com.memorygarden.service.StudyPackService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * 预设知识包服务实现类
 *
 * @author jLU
 * @date 2026-05-20
 */
@Service
public class StudyPackServiceImpl implements StudyPackService {

    private final StudyPackMapper studyPackMapper;
    private final StudyPackItemMapper studyPackItemMapper;
    private final KnowledgeCardMapper cardMapper;
    private final PlantMapper plantMapper;
    private final StudyPackImportMapper studyPackImportMapper;

    public StudyPackServiceImpl(StudyPackMapper studyPackMapper, StudyPackItemMapper studyPackItemMapper,
                                KnowledgeCardMapper cardMapper, PlantMapper plantMapper,
                                StudyPackImportMapper studyPackImportMapper) {
        this.studyPackMapper = studyPackMapper;
        this.studyPackItemMapper = studyPackItemMapper;
        this.cardMapper = cardMapper;
        this.plantMapper = plantMapper;
        this.studyPackImportMapper = studyPackImportMapper;
    }

    /**
     * 获取可用知识包列表
     *
     * @return 知识包列表
     */
    @Override
    public List<StudyPack> list() {
        return studyPackMapper.selectAll();
    }

    /**
     * 获取知识包详情（含条目预览）
     *
     * @param packId 知识包 ID
     * @return 知识包实体
     */
    @Override
    public StudyPack getDetail(Long packId) {
        return studyPackMapper.selectById(packId);
    }

    /**
     * 获取知识包条目列表
     *
     * @param packId 知识包 ID
     * @return 条目列表
     */
    @Override
    public List<StudyPackItem> getPackItems(Long packId) {
        return studyPackItemMapper.selectByPackId(packId);
    }

    /**
     * 导入知识包到个人花园
     *
     * @param userId 用户 ID
     * @param packId 知识包 ID
     * @return 导入的卡片数量
     */
    @Override
    public int importPack(Long userId, Long packId) {
        StudyPack pack = studyPackMapper.selectById(packId);
        if (pack == null) {
            throw new BusinessException(ResultCode.NOT_FOUND_ERROR, "知识包不存在");
        }

        if (studyPackImportMapper.existsByUserIdAndPackId(userId, packId)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "该知识包已导入，请勿重复操作");
        }

        List<StudyPackItem> items = studyPackItemMapper.selectByPackId(packId);
        if (items == null || items.isEmpty()) {
            return 0;
        }

        for (StudyPackItem item : items) {
            KnowledgeCard card = new KnowledgeCard();
            card.setUserId(userId);
            card.setFrontContent(item.getFrontContent());
            card.setBackContent(item.getBackContent());
            card.setSourceType(1);
            card.setSourcePackId(packId);
            card.setIsDeleted(0);
            cardMapper.insert(card);

            Plant plant = new Plant();
            plant.setCardId(card.getId());
            plant.setUserId(userId);
            plant.setGrowthStage(1);
            plant.setReviewRound(1);
            plant.setNextReviewDate(LocalDate.now());
            plant.setIsWithered(0);
            plant.setIsDeleted(0);
            plantMapper.insert(plant);
        }

        StudyPackImport importRecord = new StudyPackImport();
        importRecord.setUserId(userId);
        importRecord.setPackId(packId);
        importRecord.setIsDeleted(0);
        studyPackImportMapper.insert(importRecord);

        return items.size();
    }
}
