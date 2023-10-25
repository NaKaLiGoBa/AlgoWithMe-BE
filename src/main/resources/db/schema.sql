USE
`web_ide`;

drop table if exists chat_messages CASCADE;

drop table if exists files CASCADE;

drop table if exists member_projects CASCADE;

drop table if exists members CASCADE;

drop table if exists projects CASCADE;

CREATE TABLE `members`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `email`      VARCHAR(255) NOT NULL,
    `password`   VARCHAR(255) NOT NULL,
    `name`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `projects`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NOT NULL,
    `storage_id`  VARCHAR(255) NOT NULL,
    `created_at`  DATETIME     NOT NULL,
    `updated_at`  DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `member_projects`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `project_id` BIGINT       NOT NULL,
    `member_id`  BIGINT       NOT NULL,
    `role`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `chat_messages`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `message`      VARCHAR(255) NOT NULL,
    `message_type` VARCHAR(255) NOT NULL,
    `created_at`   DATETIME     NOT NULL,
    `updated_at`   DATETIME     NOT NULL,
    `project_id`   BIGINT       NOT NULL,
    `member_id`    BIGINT       NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `files`
(
    `id`              BIGINT       NOT NULL AUTO_INCREMENT,
    `storage_file_id` VARCHAR(255) NOT NULL,
    `created_at`      DATETIME     NOT NULL,
    `updated_at`      DATETIME     NOT NULL,
    `project_id`      BIGINT       NOT NULL,
    PRIMARY KEY (`id`)
);

ALTER TABLE `member_projects`
    ADD FOREIGN KEY (`project_id`) REFERENCES projects (`id`)
        ON DELETE CASCADE;

ALTER TABLE `member_projects`
    ADD FOREIGN KEY (`member_id`) REFERENCES members (`id`)
        ON DELETE CASCADE;

ALTER TABLE `chat_messages`
    ADD FOREIGN KEY (`project_id`) REFERENCES projects (`id`)
        ON DELETE CASCADE;

ALTER TABLE `chat_messages`
    ADD FOREIGN KEY (`member_id`) REFERENCES members (`id`)
        ON DELETE CASCADE;

ALTER TABLE `files`
    ADD FOREIGN KEY (`project_id`) REFERENCES projects (`id`)
        ON DELETE CASCADE;
