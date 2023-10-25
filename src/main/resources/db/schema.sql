USE
`algo_with_me`;

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
