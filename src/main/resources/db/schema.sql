USE
`algo_with_me`;

drop table if exists members CASCADE;

drop table if exists problems CASCADE;

drop table if exists submits CASCADE;

drop table if exists testcases CASCADE;

drop table if exists available_languages CASCADE;

drop table if exists programming_languages CASCADE;

drop table if exists problem_tags CASCADE;

drop table if exists tags CASCADE;

drop table if exists solutions CASCADE;

drop table if exists solution_languages CASCADE;

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
    `created_at`  DATETIME     NOT NULL,
    `updated_at`  DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `submits`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `code`       CLOB         NOT NULL,
    `result`     VARCHAR(255) NOT NULL,
    `member_id`  BIGINT       NOT NULL,
    `problem_id` BIGINT       NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL,
);

CREATE TABLE `testcases`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `input_names`  VARCHAR(255) NOT NULL,
    `input_values` VARCHAR(255) NOT NULL,
    `output`       VARCHAR(255) NOT NULL,
    `isGrading`    BOOLEAN      NOT NULL,
    `problem_id`   BIGINT       NOT NULL,
    `created_at`   DATETIME     NOT NULL,
    `updated_at`   DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `available_languages`
(
    `id`                      BIGINT       NOT NULL AUTO_INCREMENT,
    `template_code`           VARCHAR(255) NOT NULL,
    `problem_id`              BIGINT       NOT NULL,
    `programming_language_id` BIGINT       NOT NULL,
    `created_at`              DATETIME     NOT NULL,
    `updated_at`              DATETIME     NOT NULL,
);

CREATE TABLE `programming_languages`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `problem_tags`
(
    `id`         BIGINT   NOT NULL AUTO_INCREMENT,
    `problem_id` BIGINT   NOT NULL,
    `tag_id`     BIGINT   NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `tags`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `solutions`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `title`      VARCHAR(255) NOT NULL,
    `content`    VARCHAR(255) NOT NULL,
    `member_id`  BIGINT       NOT NULL,
    `problem_id` BIGINT       NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `solution_languages`
(
    `id`                      BIGINT   NOT NULL AUTO_INCREMENT,
    `solution_id`             BIGINT   NOT NULL,
    `programming_language_id` BIGINT   NOT NULL,
    `created_at`              DATETIME NOT NULL,
    `updated_at`              DATETIME NOT NULL,
    PRIMARY KEY (`id`)
);

ALTER TABLE `submits`
    ADD FOREIGN KEY (`member_id`) REFERENCES members (`id`)
        ON DELETE CASCADE;

ALTER TABLE `submits`
    ADD FOREIGN KEY (`problem_id`) REFERENCES problems (`id`)
        ON DELETE CASCADE;

ALTER TABLE `testcases`
    ADD FOREIGN KEY (`problem_id`) REFERENCES problems (`id`)
        ON DELETE CASCADE;

ALTER TABLE `available_laguages`
    ADD FOREIGN KEY (`problem_id`) REFERENCES problems (`id`)
        ON DELETE CASCADE;

ALTER TABLE `available_laguages`
    ADD FOREIGN KEY (`programming_language_id`) REFERENCES programming_languages(`id`)
        ON DELETE CASCADE;

ALTER TABLE `problem_tags`
    ADD FOREIGN KEY (`problem_id`) REFERENCES problems (`id`)
        ON DELETE CASCADE;

ALTER TABLE `problem_tags`
    ADD FOREIGN KEY (`tag_id`) REFERENCES tags (`id`)
        ON DELETE CASCADE;

ALTER TABLE `solutions`
    ADD FOREIGN KEY (`member_id`) REFERENCES members (`id`)
        ON DELETE CASCADE;

ALTER TABLE `solutions`
    ADD FOREIGN KEY (`problem_id`) REFERENCES problems (`id`)
        ON DELETE CASCADE;

ALTER TABLE `solution_languages`
    ADD FOREIGN KEY (`solution_id`) REFERENCES solutions (`id`)
        ON DELETE CASCADE;

ALTER TABLE `solution_languages`
    ADD FOREIGN KEY (`programming_language_id`) REFERENCES programming_languages(`id`)