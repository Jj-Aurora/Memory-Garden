package com.memorygarden.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SelfEvaluation 枚举测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class SelfEvaluationTest {

    @Test
    void testEvaluationValues() {
        assertEquals(1, SelfEvaluation.REMEMBERED.getValue());
        assertEquals(2, SelfEvaluation.VAGUE.getValue());
        assertEquals(3, SelfEvaluation.FORGOTTEN.getValue());
    }

    @Test
    void testEvaluationCount() {
        assertEquals(3, SelfEvaluation.values().length);
    }

    @Test
    void testFromValue() {
        assertEquals(SelfEvaluation.REMEMBERED, SelfEvaluation.fromValue(1));
        assertEquals(SelfEvaluation.VAGUE, SelfEvaluation.fromValue(2));
        assertEquals(SelfEvaluation.FORGOTTEN, SelfEvaluation.fromValue(3));
    }

    @Test
    void testFromValueBoundary() {
        assertNull(SelfEvaluation.fromValue(0));
        assertNull(SelfEvaluation.fromValue(4));
    }
}
