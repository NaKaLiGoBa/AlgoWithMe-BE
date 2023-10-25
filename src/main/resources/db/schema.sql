USE
`algo_with_me`;

drop table if exists members CASCADE;

drop table if exists problems CASCADE;

drop table if exists submissions CASCADE;

drop table if exists submits CASCADE;

drop table if exists answer_testcases CASCADE;

drop table if exists answer_testcase_inputs CASCADE;

drop table if exists testcases CASCADE;

drop table if exists testcase_inputs CASCADE;

drop table if exists available_language CASCADE;

drop table if exists programming_languages CASCADE;

drop table if exists problem_tags CASCADE;

drop table if exists tags CASCADE;

CREATE TABLE `members`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `email`      VARCHAR(255) NOT NULL,
    `password`   VARCHAR(255) NOT NULL,
    `nickname`   VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `problems`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `number`      BIGINT       NOT NULL,
    `description` VARCHAR(255) NOT NULL,
    `title`       VARCHAR(255) NOT NULL,
    `difficulty`  VARCHAR(255) NOT NULL,
    `acceptance`  DECIMAL      NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `submissions`
(
  `id`     BIGINT       NOT NULL AUTO_INCREMENT,
  `code`   CLOB         NOT NULL,
  `result` VARCHAR(255) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `submits`
(
    `member_id`     BIGINT NOT NULL,
    `problem_id`    BIGINT NOT NULL,
    `submission_id` BIGINT NOT NULL,
);

CREATE TABLE `answer_testcases`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `output`     VARCHAR(255) NOT NULL,
    `problem_id` BIGINT       NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `answer_testcase_inputs`
(
    `id`                 BIGINT       NOT NULL AUTO_INCREMENT,
    `name`               VARCHAR(255) NOT NULL,
    `value`              VARCHAR(255) NOT NULL,
    `answer_testcase_id` BIGINT       NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `testcases`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `expected`   VARCHAR(255) NOT NULL,
    `problem_id` BIGINT       NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `testcase_inputs`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(255) NOT NULL,
    `value`       VARCHAR(255) NOT NULL,
    `testcase_id` BIGINT       NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `available_languages`
(
    `problem_id`              BIGINT NOT NULL,
    `programming_language_id` BIGINT NOT NULL,
);

CREATE TABLE `programming_languages`
(
    `id`   BIGINT       NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `problem_tags`
(
    `problem_id` BIGINT NOT NULL,
    `tag_id`     BIGINT NOT NULL,
);

CREATE TABLE `tags`
(
    `id`   BIGINT       NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

ALTER TABLE `submits`
    ADD FOREIGN KEY (`member_id`) REFERENCES members (`id`)
        ON DELETE CASCADE;

ALTER TABLE `submits`
    ADD FOREIGN KEY (`problem_id`) REFERENCES problems (`id`)
        ON DELETE CASCADE;

ALTER TABLE `submits`
    ADD FOREIGN KEY (`submission_id`) REFERENCES submissions (`id`)
        ON DELETE CASCADE;

ALTER TABLE `answer_testcases`
    ADD FOREIGN KEY (`problem_id`) REFERENCES problems (`id`)
        ON DELETE CASCADE;

ALTER TABLE `answer_testcase_inputs`
    ADD FOREIGN KEY (`answer_testcase_id`) REFERENCES answer_testcases (`id`)
        ON DELETE CASCADE;

ALTER TABLE `testcases`
    ADD FOREIGN KEY (`problem_id`) REFERENCES problems (`id`)
        ON DELETE CASCADE;

ALTER TABLE `testcase_inputs`
    ADD FOREIGN KEY (`testcase_id`) REFERENCES testcases (`id`)
        ON DELETE CASCADE;

ALTER TABLE `available_laguages`
    ADD FOREIGN KEY (`problem_id`) REFERENCES problems (`id`)
        ON DELETE CASCADE;

ALTER TABLE `available_laguages`
    ADD FOREIGN KEY (`programming_languages`) REFERENCES programming_languages(`id`)
        ON DELETE CASCADE;

ALTER TABLE `problem_tags`
    ADD FOREIGN KEY (`problem_id`) REFERENCES problems (`id`)
        ON DELETE CASCADE;

ALTER TABLE `problem_tags`
    ADD FOREIGN KEY (`tag_id`) REFERENCES tags (`id`)
        ON DELETE CASCADE;