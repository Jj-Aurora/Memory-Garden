package com.memorygarden.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ReviewRecord 实体测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class ReviewRecordTest {

    @Test
    void testFieldAssignment() {
        ReviewRecord record = new ReviewRecord();
        record.setId(1L);
        record.setUserId(100L);
        record.setCardId(10L);
        record.setPlantId(5L);
        record.setSelfEvaluation(1);
        record.setPrevStage(1);
        record.setAfterStage(2);
        record.setPrevRound(0);
        record.setAfterRound(1);
        record.setWasWithered(0);
        record.setScheduledDate(LocalDate.of(2026, 5, 20));
        record.setActualDate(LocalDate.of(2026, 5, 20));
        record.setIsDeleted(0);
        record.setCreateTime(new Date());

        assertEquals(1L, record.getId());
        assertEquals(100L, record.getUserId());
        assertEquals(10L, record.getCardId());
        assertEquals(5L, record.getPlantId());
        assertEquals(1, record.getSelfEvaluation());
        assertEquals(1, record.getPrevStage());
        assertEquals(2, record.getAfterStage());
        assertEquals(0, record.getPrevRound());
        assertEquals(1, record.getAfterRound());
        assertEquals(0, record.getWasWithered());
    }

    @Test
    void testStageProgression() {
        ReviewRecord record = new ReviewRecord();
        record.setPrevStage(1);
        record.setAfterStage(2);
        assertTrue(record.getAfterStage() > record.getPrevStage());
    }

    @Test
    void testRoundProgression() {
        ReviewRecord record = new ReviewRecord();
        record.setPrevRound(2);
        record.setAfterRound(3);
        assertTrue(record.getAfterRound() > record.getPrevRound());
    }
}
