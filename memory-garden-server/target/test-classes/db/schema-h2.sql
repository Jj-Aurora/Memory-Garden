-- ============================================================
-- 记忆花园系统 - H2 测试用建表脚本
-- @author jLU
-- @date 2026-05-20
-- ============================================================

DROP TABLE IF EXISTS t_study_pack_import;
DROP TABLE IF EXISTS t_study_pack_item;
DROP TABLE IF EXISTS t_study_pack;
DROP TABLE IF EXISTS t_user_badge;
DROP TABLE IF EXISTS t_badge;
DROP TABLE IF EXISTS t_review_record;
DROP TABLE IF EXISTS t_plant;
DROP TABLE IF EXISTS t_knowledge_card;
DROP TABLE IF EXISTS t_category;
DROP TABLE IF EXISTS t_user;

CREATE TABLE t_user (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    username        VARCHAR(50)     NOT NULL,
    password        VARCHAR(255)    NOT NULL,
    nickname        VARCHAR(50)     DEFAULT NULL,
    avatar_url      VARCHAR(500)    DEFAULT NULL,
    current_streak  INT             NOT NULL DEFAULT 0,
    max_streak      INT             NOT NULL DEFAULT 0,
    last_check_in   DATE            DEFAULT NULL,
    status          TINYINT         NOT NULL DEFAULT 1,
    is_deleted      TINYINT         NOT NULL DEFAULT 0,
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (username)
);

CREATE TABLE t_category (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    name            VARCHAR(100)    NOT NULL,
    icon            VARCHAR(50)     DEFAULT NULL,
    sort_order      INT             NOT NULL DEFAULT 0,
    is_deleted      TINYINT         NOT NULL DEFAULT 0,
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE t_knowledge_card (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    category_id     BIGINT          DEFAULT NULL,
    front_content   TEXT            NOT NULL,
    back_content    TEXT            NOT NULL,
    note            TEXT            DEFAULT NULL,
    source_type     TINYINT         NOT NULL DEFAULT 0,
    source_pack_id  BIGINT          DEFAULT NULL,
    is_deleted      TINYINT         NOT NULL DEFAULT 0,
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE t_plant (
    id                      BIGINT      NOT NULL AUTO_INCREMENT,
    user_id                 BIGINT      NOT NULL,
    card_id                 BIGINT      NOT NULL,
    growth_stage            TINYINT     NOT NULL DEFAULT 1,
    is_withered             TINYINT     NOT NULL DEFAULT 0,
    stage_success_count     INT         NOT NULL DEFAULT 0,
    total_review_count      INT         NOT NULL DEFAULT 0,
    review_round            INT         NOT NULL DEFAULT 0,
    next_review_date        DATE        NOT NULL,
    last_review_date        DATE        DEFAULT NULL,
    is_deleted              TINYINT     NOT NULL DEFAULT 0,
    create_time             DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time             DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (card_id)
);

CREATE TABLE t_review_record (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    card_id         BIGINT          NOT NULL,
    plant_id        BIGINT          NOT NULL,
    self_evaluation TINYINT         NOT NULL,
    prev_stage      TINYINT         DEFAULT NULL,
    after_stage     TINYINT         DEFAULT NULL,
    prev_round      INT             DEFAULT NULL,
    after_round     INT             DEFAULT NULL,
    was_withered    TINYINT         NOT NULL DEFAULT 0,
    scheduled_date  DATE            DEFAULT NULL,
    actual_date     DATE            NOT NULL,
    is_deleted      TINYINT         NOT NULL DEFAULT 0,
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE t_badge (
    id                BIGINT        NOT NULL AUTO_INCREMENT,
    name              VARCHAR(50)   NOT NULL,
    description       VARCHAR(200)  NOT NULL,
    icon              VARCHAR(100)  DEFAULT NULL,
    rarity            TINYINT       NOT NULL DEFAULT 0,
    condition_type    VARCHAR(50)   NOT NULL,
    condition_value   INT           NOT NULL,
    is_deleted        TINYINT       NOT NULL DEFAULT 0,
    create_time       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE t_user_badge (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    badge_id        BIGINT          NOT NULL,
    is_deleted      TINYINT         NOT NULL DEFAULT 0,
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (user_id, badge_id)
);

CREATE TABLE t_study_pack (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    name            VARCHAR(100)    NOT NULL,
    description     TEXT            DEFAULT NULL,
    category_name   VARCHAR(100)    DEFAULT NULL,
    card_count      INT             NOT NULL DEFAULT 0,
    is_deleted      TINYINT         NOT NULL DEFAULT 0,
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE t_study_pack_item (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    pack_id         BIGINT          NOT NULL,
    front_content   TEXT            NOT NULL,
    back_content    TEXT            NOT NULL,
    note            TEXT            DEFAULT NULL,
    sort_order      INT             NOT NULL DEFAULT 0,
    is_deleted      TINYINT         NOT NULL DEFAULT 0,
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE t_study_pack_import (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    pack_id         BIGINT          NOT NULL,
    is_deleted      TINYINT         NOT NULL DEFAULT 0,
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
