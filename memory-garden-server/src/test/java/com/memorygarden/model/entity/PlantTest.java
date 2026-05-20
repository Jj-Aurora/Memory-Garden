package com.memorygarden.model.entity;

import com.memorygarden.model.enums.GrowthStage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Plant 实体测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class PlantTest {

    @Test
    void testFieldAssignment() {
        Plant plant = new Plant();
        plant.setId(1L);
        plant.setUserId(100L);
        plant.setCardId(10L);
        plant.setGrowthStage(GrowthStage.SEED.getValue());
        plant.setIsWithered(0);
        plant.setStageSuccessCount(0);
        plant.setTotalReviewCount(0);
        plant.setReviewRound(0);
        plant.setNextReviewDate(LocalDate.of(2026, 5, 21));
        plant.setLastReviewDate(null);
        plant.setIsDeleted(0);
        plant.setCreateTime(new Date());
        plant.setUpdateTime(new Date());

        assertEquals(1L, plant.getId());
        assertEquals(100L, plant.getUserId());
        assertEquals(10L, plant.getCardId());
        assertEquals(1, plant.getGrowthStage());
        assertEquals(0, plant.getIsWithered());
        assertEquals(0, plant.getStageSuccessCount());
        assertEquals(0, plant.getTotalReviewCount());
        assertEquals(0, plant.getReviewRound());
    }

    @Test
    void testGrowthStageDefaultValue() {
        Plant plant = new Plant();
        assertEquals(1, plant.getGrowthStage());
    }

    @Test
    void testWitheredToggle() {
        Plant plant = new Plant();
        plant.setIsWithered(0);
        assertFalse(plant.isWithered());

        plant.setIsWithered(1);
        assertTrue(plant.isWithered());
    }
}
