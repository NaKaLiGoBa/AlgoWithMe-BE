CREATE TABLE `members`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `email`      VARCHAR(255) NOT NULL,
    `password`   VARCHAR(255) NOT NULL,
    `nickname`   VARCHAR(255) NOT NULL,
    `role`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `problems`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `number`      BIGINT       NOT NULL,
    `description` TEXT         NOT NULL,
    `title`       VARCHAR(255) NOT NULL,
    `difficulty`  VARCHAR(255) NOT NULL,
    `acceptance`  DECIMAL      NOT NULL,
    `created_at`  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `testcases`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `problem_id`   BIGINT       NOT NULL,
    `number`       BIGINT       NOT NULL,
    `input_names`  VARCHAR(255) NOT NULL,
    `input_values` VARCHAR(255) NOT NULL,
    `output`       VARCHAR(255) NOT NULL,
    `isGrading`    BOOLEAN      NOT NULL,
    `created_at`   DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`   DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `programming_languages`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `tags`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `problem_tags`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `problem_id` BIGINT      NOT NULL,
    `tag_id`     BIGINT      NOT NULL,
    `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `available_languages`
(
    `id`                       BIGINT       NOT NULL AUTO_INCREMENT,
    `template_code`            VARCHAR(255) NOT NULL,
    `programming_languages_id` BIGINT       NOT NULL,
    `problem_id`               BIGINT       NOT NULL,
    `created_at`               DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`               DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `submits`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `problem_id` BIGINT       NOT NULL,
    `member_id`  BIGINT       NOT NULL,
    `code`       TEXT         NOT NULL,
    `result`     VARCHAR(255) NOT NULL,
    `created_at` DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `solutions`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id`  BIGINT       NOT NULL,
    `problem_id` BIGINT       NOT NULL,
    `title`      VARCHAR(255) NOT NULL,
    `content`    TEXT         NOT NULL,
    `view_count` BIGINT       NOT NULL,
    `created_at` DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `solution_languages`
(
    `id`                       BIGINT      NOT NULL AUTO_INCREMENT,
    `solution_id`              BIGINT      NOT NULL,
    `programming_languages_id` BIGINT      NOT NULL,
    `created_at`               DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`               DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `comments`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id`   BIGINT       NOT NULL,
    `solution_id` BIGINT       NOT NULL,
    `content`     VARCHAR(255) NOT NULL,
    `created_at`  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `replies`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `member_id`  BIGINT      NOT NULL,
    `comment_id` BIGINT      NOT NULL,
    `content`    BIGINT      NOT NULL,
    `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `solution_likes`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT,
    `member_id`   BIGINT      NOT NULL,
    `solution_id` BIGINT      NOT NULL,
    `created_at`  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `comment_likes`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `member_id`  BIGINT      NOT NULL,
    `comment_id` BIGINT      NOT NULL,
    `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `reply_likes`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `reply_id`   BIGINT      NOT NULL,
    `member_id`  BIGINT      NOT NULL,
    `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `mini_quizzes`
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT,
    `description`      VARCHAR(255) NOT NULL,
    `explain`          VARCHAR(255) NOT NULL,
    `answer`           TEXT         NOT NULL,
    `type`             VARCHAR(255) NOT NULL,
    `difficulty`       VARCHAR(255) NOT NULL,
    `choiceOrInitials` VARCHAR(255) NOT NULL,
    `created_at`       DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`       DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE `mini_quiz_tags`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT,
    `tag_id`       BIGINT      NOT NULL,
    `mini_quiz_id` BIGINT      NOT NULL,
    `created_at`   DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`   DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
);

ALTER TABLE `testcases`
    ADD CONSTRAINT `FK_problems_TO_testcases_1` FOREIGN KEY (
                                                             `problem_id`
        )
        REFERENCES `problems` (
                               `id`
            );

ALTER TABLE `problem_tags`
    ADD CONSTRAINT `FK_problems_TO_problem_tags_1` FOREIGN KEY (
                                                                `problem_id`
        )
        REFERENCES `problems` (
                               `id`
            );

ALTER TABLE `problem_tags`
    ADD CONSTRAINT `FK_tags_TO_problem_tags_1` FOREIGN KEY (
                                                            `tag_id`
        )
        REFERENCES `tags` (
                           `id`
            );

ALTER TABLE `available_languages`
    ADD CONSTRAINT `FK_programming_languages_TO_available_languages_1` FOREIGN KEY (
                                                                                    `programming_languages_id`
        )
        REFERENCES `programming_languages` (
                                            `id`
            );

ALTER TABLE `available_languages`
    ADD CONSTRAINT `FK_problems_TO_available_languages_1` FOREIGN KEY (
                                                                       `problem_id`
        )
        REFERENCES `problems` (
                               `id`
            );

ALTER TABLE `submits`
    ADD CONSTRAINT `FK_problems_TO_submits_1` FOREIGN KEY (
                                                           `problem_id`
        )
        REFERENCES `problems` (
                               `id`
            );

ALTER TABLE `submits`
    ADD CONSTRAINT `FK_members_TO_submits_1` FOREIGN KEY (
                                                          `member_id`
        )
        REFERENCES `members` (
                              `id`
            );

ALTER TABLE `solutions`
    ADD CONSTRAINT `FK_members_TO_solutions_1` FOREIGN KEY (
                                                            `member_id`
        )
        REFERENCES `members` (
                              `id`
            );

ALTER TABLE `solutions`
    ADD CONSTRAINT `FK_problems_TO_solutions_1` FOREIGN KEY (
                                                             `problem_id`
        )
        REFERENCES `problems` (
                               `id`
            );

ALTER TABLE `solution_languages`
    ADD CONSTRAINT `FK_solutions_TO_solution_languages_1` FOREIGN KEY (
                                                                       `solution_id`
        )
        REFERENCES `solutions` (
                                `id`
            );

ALTER TABLE `solution_languages`
    ADD CONSTRAINT `FK_available_languages_TO_solution_languages_1` FOREIGN KEY (
                                                                                 `programming_languages_id`
        )
        REFERENCES `available_languages` (
                                          `id`
            );

ALTER TABLE `comments`
    ADD CONSTRAINT `FK_members_TO_comments_1` FOREIGN KEY (
                                                           `member_id`
        )
        REFERENCES `members` (
                              `id`
            );

ALTER TABLE `comments`
    ADD CONSTRAINT `FK_solutions_TO_comments_1` FOREIGN KEY (
                                                             `solution_id`
        )
        REFERENCES `solutions` (
                                `id`
            );

ALTER TABLE `replies`
    ADD CONSTRAINT `FK_members_TO_replies_1` FOREIGN KEY (
                                                          `member_id`
        )
        REFERENCES `members` (
                              `id`
            );

ALTER TABLE `replies`
    ADD CONSTRAINT `FK_comments_TO_replies_1` FOREIGN KEY (
                                                           `comment_id`
        )
        REFERENCES `comments` (
                               `id`
            );

ALTER TABLE `solution_likes`
    ADD CONSTRAINT `FK_members_TO_solution_likes_1` FOREIGN KEY (
                                                                 `member_id`
        )
        REFERENCES `members` (
                              `id`
            );

ALTER TABLE `solution_likes`
    ADD CONSTRAINT `FK_solutions_TO_solution_likes_1` FOREIGN KEY (
                                                                   `solution_id`
        )
        REFERENCES `solutions` (
                                `id`
            );

ALTER TABLE `comment_likes`
    ADD CONSTRAINT `FK_members_TO_comment_likes_1` FOREIGN KEY (
                                                                `member_id`
        )
        REFERENCES `members` (
                              `id`
            );

ALTER TABLE `comment_likes`
    ADD CONSTRAINT `FK_comments_TO_comment_likes_1` FOREIGN KEY (
                                                                 `comment_id`
        )
        REFERENCES `comments` (
                               `id`
            );

ALTER TABLE `reply_likes`
    ADD CONSTRAINT `FK_replies_TO_reply_likes_1` FOREIGN KEY (
                                                              `reply_id`
        )
        REFERENCES `replies` (
                              `id`
            );

ALTER TABLE `reply_likes`
    ADD CONSTRAINT `FK_members_TO_reply_likes_1` FOREIGN KEY (
                                                              `member_id`
        )
        REFERENCES `members` (
                              `id`
            );

ALTER TABLE `mini_quiz_tags`
    ADD CONSTRAINT `FK_tags_TO_mini_quiz_tags_1` FOREIGN KEY (
                                                              `tag_id`
        )
        REFERENCES `tags` (
                           `id`
            );

ALTER TABLE `mini_quiz_tags`
    ADD CONSTRAINT `FK_mini_quizzes_TO_mini_quiz_tags_1` FOREIGN KEY (
                                                                      `mini_quiz_id`
        )
        REFERENCES `mini_quizzes` (
                                   `id`
            );

# 테스트 데이터 삽입
START TRANSACTION;

# admin, 테스트 계정
INSERT INTO `members`
    (email, password, nickname, role)
VALUES ('admin@admin.com', 'admin', 'admin', 'ROLE_ADMIN'),
       ('user1@user1.com', 'user1', 'user1', 'ROLE_USER'),
       ('user2@user2.com', 'user2', 'user2', 'ROLE_USER');

# 문제 태그
INSERT INTO `tags`
    (id, name)
VALUES (1, 'DFS'),
       (2, 'BFS'),
       (3, 'BackTracking'),
       (4, 'DP'),
       (5, 'Simulation'),
       (6, 'Graph'),
       (7, 'Sort'),
       (8, 'Tree'),
       (9, 'Greedy'),
       (10, 'Dijkstra');

# 프로그래밍 언어
INSERT INTO `programming_languages` (id, name)
VALUES (1, 'JAVA');

# 문제
INSERT INTO `problems`
    (id, number, description, title, difficulty, acceptance)
VALUES (1, 1, '## 문제

n개의 음이 아닌 정수들이 있습니다.
이 정수들을 순서를 바꾸지 않고 적절히 더하거나 빼서 타겟 넘버를 만들려고 합니다.
예를 들어 [1, 1, 1, 1, 1]로 숫자 3을 만들려면 다음 다섯 방법을 쓸 수 있습니다.

`-1+1+1+1+1 = 3
+1-1+1+1+1 = 3
+1+1-1+1+1 = 3
+1+1+1-1+1 = 3
+1+1+1+1-1 = 3`

사용할 수 있는 숫자가 담긴 배열 numbers, 타겟 넘버 target이 매개변수로 주어질 때
숫자를 적절히 더하고 빼서 타겟 넘버를 만드는 방법의 수를 return 하도록 solution 함수를 작성해주세요.

## 입력

첫째 줄에 정점의 개수 N(1 ≤ N ≤ 1,000), 간선의 개수 M(1 ≤ M ≤ 10,000),
탐색을 시작할 정점의 번호 V가 주어진다. 다음 M개의 줄에는 간선이 연결하는 두 정점의 번호가 주어진다.
어떤 두 정점 사이에 여러 개의 간선이 있을 수 있다. 입력으로 주어지는 간선은 양방향이다.

## 출력

첫째 줄에 DFS를 수행한 결과를, 그 다음 줄에는 BFS를 수행한 결과를 출력한다.
V부터 방문된 점을 순서대로 출력하면 된다.

## 제한 사항

- 주어지는 숫자의 개수는 2개 이상 20개 이하입니다.
- 각 숫자는 1 이상 50 이하인 자연수입니다.
- 타겟 넘버는 1 이상 1000 이하인 자연수입니다.

## 예제1:
> 입력:
[1,1,1,1,1]
3

> 출력:
5

## 예제2:
> 입력:
[4,1,2,1]
4

> 출력:
2', '타겟 넘버', 'MEDIUM', 0),
       (2, 2, '## 문제
자연수 n이 주어지면, n과 구성이 같으면서 n보다 큰 수 중 가장 작은 수를 출력하는 프로그
램을 만들고 싶습니다.
구성이 같다는 말은 각 자릿수가 같은 숫자들로 이루어졌다는 의미입니다. 예를 들어 123과
231은 서로 구성이 같습니다. 하지만 123과 215는 구성이 다릅니다.
매개변수 n에 자연수가 주어지면 n과 구성이 같은 숫자 중 n보다 큰 수 중 가장 작은수를 반
환하는 프로그램을 작성하세요.
n과 구성이 같으면서 n보다 큰 수가 없으면 -1를 반환합니다.\n

## 입력
> 자연수의 N(1<=N<=9999999)\n

## 예제 1
> 입력 : 123
> 출력 : 132', '가장 가까운 큰수', 'EASY', 0),
       (3, 3, '## 문제

N×M크기의 배열로 표현되는 미로가 있다.
`1	0	1	1	1	1
 1	0	1	0	1	0
 1	0	1	0	1	1
 1	1	1	0	1	1`

미로에서 1은 이동할 수 있는 칸을 나타내고, 0은 이동할 수 없는 칸을 나타낸다.
이러한 미로가 주어졌을 때, (1, 1)에서 출발하여 (N, M)의 위치로 이동할 때 지나야 하는 최소의 칸 수를 구하는 프로그램을 작성하시오.
한 칸에서 다른 칸으로 이동할 때, 서로 인접한 칸으로만 이동할 수 있다.

위의 예에서는 15칸을 지나야 (N, M)의 위치로 이동할 수 있다. 칸을 셀 때에는 시작 위치와 도착 위치도 포함한다.

## 입력

첫째 줄에 두 정수 N, M(2 ≤ N, M ≤ 100)이 주어진다.
다음 N개의 줄에는 M개의 정수로 미로가 주어진다. 각각의 수들은 붙어서 입력으로 주어진다.

## 출력

첫째 줄에 지나야 하는 최소의 칸 수를 출력한다. 항상 도착위치로 이동할 수 있는 경우만 입력으로 주어진다.

## 예제1:
> 입력:
4 6
101111
101010
101011
111011

> 출력: 15

## 예제2:
> 입력:
4 6
110110
110110
111111
111101

> 출력: 9', '미로 탐색', 'HARD', 0),
       (4, 4, '## 문제
현수의 집에서 상점까지는 자연수가 적혀있는 타일이 깔려있습니다. i번째 탈일에 적힌 수를
Ai라고 한다면 현수는 i번째 타일 위에서 오른쪽으로 Ai이하만큼 점프할 수 있습니다.
예를 들어 현수가 2번째 탈일에 있고, 적힌 수가 3이라면 현수는 3, 4, 5번째 타일로 점프할
수 있다.
현수가 집에서 상점까지 최소 몇 번의 점프로 갈 수 있는지 구하는 프로그램을 작성하세요.
매개변수 nums에 현수에 집에서부터 상점까지 차례대로 타일에 적힌 수가 주어집니다. 왼쪽
첫 번째 타일이 현수의 집이고, 오른쪽 끝 타일이 상점이라고 간주합니다.
현수가 집에서 상점까지 가는데 최소 점프 횟수를 반환하는 프로그램을 작성하세요. 만약
현수가 상점까지 갈 수 없다면 -1를 반환하세요.

## 예제 1
> 입력 : 2 2 1 2 1 1
> 출력 : 3', '타일 점프', 'MEDIUM', 0),
       (5, 5, '## 문제

N개의 정수로 이루어진 수열이 있을 때,
크기가 양수인 부분수열 중에서 그 수열의 원소를 다 더한 값이 S가 되는 경우의 수를 구하는 프로그램을 작성하시오.

## 입력

첫째 줄에 정수의 개수를 나타내는 N과 정수 S가 주어진다.
(1 ≤ N ≤ 20, |S| ≤ 1,000,000) 둘째 줄에 N개의 정수가 빈 칸을 사이에 두고 주어진다.
주어지는 정수의 절댓값은 100,000을 넘지 않는다.

## 출력

첫째 줄에 합이 S가 되는 부분수열의 개수를 출력한다.

## 예제1:
> 입력: 5 0 [-7, -3, -2, 5, 8]
> 출력: 1', '부분수열의 합', 'HARD', 0),
       (6, 6, '## 문제
N-Queen 문제는 크기가 N × N인 체스판 위에 퀸 N개를 서로 공격할 수 없게 놓는 문제이다.
N이 주어졌을 때, 퀸을 놓는 방법의 수를 구하는 프로그램을 작성하시오.

## 입력
첫째 줄에 N이 주어진다. (1 ≤ N < 15)

## 출력
첫째 줄에 퀸 N개를 서로 공격할 수 없게 놓는 경우의 수를 출력한다.

## 예제 1
> 입력 : 7
> 출력 : 40', 'N-Queen', 'MEDIUM', 0),
       (7, 7, '## 문제

수열 A가 주어졌을 때, 가장 긴 증가하는 부분 수열을 구하는 프로그램을 작성하시오.

예를 들어, 수열 A = {10, 20, 10, 30, 20, 50} 인 경우에 가장 긴 증가하는 부분 수열은 A = {10, 20, 10, 30, 20, 50} 이고, 길이는 4이다.

## 입력

첫째 줄에 수열 A의 크기 N (1 ≤ N ≤ 1,000)이 주어진다.

둘째 줄에는 수열 A를 이루고 있는 Ai가 주어진다. (1 ≤ Ai ≤ 1,000)

## 출력

첫째 줄에 수열 A의 가장 긴 증가하는 부분 수열의 길이를 출력한다.

## 예제1:
> 입력: 6, [10,20,10,30,20,50]
> 출력: 4
', '가장 긴 증가하는 부분 수열', 'MEDIUM', 0),
       (8, 8, '## 문제
철수는 계단을 오를 때 한 번에 한 계단 또는 두 계단씩 올라간다. 만약 총 4계단을 오른다면 그 방법의 수는
1+1+1+1, 1+1+2, 1+2+1, 2+1+1, 2+2 로 5가지이다.
그렇다면 총 N계단일 때 철수가 올라갈 수 있는 방법의 수는 몇 가지인가?

## 입력
첫째 줄은 계단의 개수인 자연수 N(3≤N≤35)이 주어집니다.

## 출력
첫 번째 줄에 올라가는 방법의 수를 출력합니다.

## 예제 1
> 입력 : 3
> 출력 : 3', '계단 오르기', 'EASY', 0),
       (9, 9, '## 문제

알파벳 대소문자로 된 단어가 주어지면,
이 단어에서 가장 많이 사용된 알파벳이 무엇인지 알아내는 프로그램을 작성하시오.
단, 대문자와 소문자를 구분하지 않는다.

## 입력

첫째 줄에 알파벳 대소문자로 이루어진 단어가 주어진다.
주어지는 단어의 길이는 1,000,000을 넘지 않는다.

## 출력

첫째 줄에 이 단어에서 가장 많이 사용된 알파벳을 대문자로 출력한다.
단, 가장 많이 사용된 알파벳이 여러 개 존재하는 경우에는 ?를 출력한다.

## 예제1:
> 입력: Mississipi
> 출력: ?

## 예제2:
> 입력: zZa
> 출력: Z', '단어 공부', 'MEDIUM', 0),
       (10, 10, '## 문제
바이토닉 수열이란 수열이 증가했다가 감소하는 수열을 의미합니다.
예를 들어 1, 2, 3, 2, 1과 같이 증가했다가 감소하면 바이토닉 수열이라고 합니다. 하지만
1, 2, 3, 4, 5와 같이 증가만 하거나, 5, 4, 3, 2, 1처럼 감소만 하면 바이토닉 수열이라 하지
않습니다. 또 1, 2, 2, 3, 2, 1처럼 같은 값이 이웃해도 바이토닉 수열이라 하지 않습니다.
매개변수 nums에 길이가 n인 수열이 주어지면 이 수열의 연속부분수열 중 가장 긴 바이토닉
수열을 찾아 그 길이를 반환하는 프로그램을 작성하세요.
만약 [1, 3, 2, 5, 7, 4, 2, 5, 1]수열이 주어지면 이 수열의 연속부분수열 중 가장 긴 바이토
닉 수열은 [2, 5, 7, 4, 2]이고, 답은 5입니다.

## 입력
• nums의 길이 3 <= n <= 10,000
• 배열 nums의 원소는 자연수입니다.

## 예제 1
> 입력 : 1 3 2 5 7 4 2 5 1
> 출력 : 5', '최대길이 바이토닉 수열', 'HARD', 0),
       (11, 11, '## 문제

정수 A를 B로 바꾸려고 한다. 가능한 연산은 다음과 같은 두 가지이다.

2를 곱한다.
1을 수의 가장 오른쪽에 추가한다.
A를 B로 바꾸는데 필요한 연산의 최솟값을 구해보자.

## 입력

첫째 줄에 A, B (1 ≤ A < B ≤ 109)가 주어진다.

## 출력

A를 B로 바꾸는데 필요한 연산의 최솟값에 1을 더한 값을 출력한다.
만들 수 없는 경우에는 -1을 출력한다.

## 예제1:
> 입력: 2 162
> 출력: 5

## 예제2:
> 입력: 4 42
> 출력: -1', 'A -> B', 'HARD', 0),
       (12, 12, '## 문제
강호는 코딩 교육을 하는 스타트업 스타트링크에 지원했다. 오늘은 강호의 면접날이다. 하지만, 늦잠을 잔 강호는 스타트링크가 있는 건물에 늦게 도착하고 말았다.
스타트링크는 총 F층으로 이루어진 고층 건물에 사무실이 있고, 스타트링크가 있는 곳의 위치는 G층이다. 강호가 지금 있는 곳은 S층이고, 이제 엘리베이터를 타고 G층으로 이동하려고 한다.
보통 엘리베이터에는 어떤 층으로 이동할 수 있는 버튼이 있지만, 강호가 탄 엘리베이터는 버튼이 2개밖에 없다. U버튼은 위로 U층을 가는 버튼, D버튼은 아래로 D층을 가는 버튼이다. (만약, U층 위, 또는 D층 아래에 해당하는 층이 없을 때는, 엘리베이터는 움직이지 않는다)
강호가 G층에 도착하려면, 버튼을 적어도 몇 번 눌러야 하는지 구하는 프로그램을 작성하시오. 만약, 엘리베이터를 이용해서 G층에 갈 수 없다면, "use the stairs"를 출력한다.

## 입력
첫째 줄에 F, S, G, U, D가 주어진다. (1 ≤ S, G ≤ F ≤ 1000000, 0 ≤ U, D ≤ 1000000) 건물은 1층부터 시작하고, 가장 높은 층은 F층이다.

## 출력
첫째 줄에 강호가 S층에서 G층으로 가기 위해 눌러야 하는 버튼의 수의 최솟값을 출력한다. 만약, 엘리베이터로 이동할 수 없을 때는 "use the stairs"를 출력한다.

## 예제 1
> 입력 : 10 1 10 2 1
> 출력 : 6', '스타트링크', 'EASY', 0),
       (13, 13, '## 문제

배열을 정렬하는 것은 쉽다. 수가 주어지면, 그 수의 각 자리수를 내림차순으로 정렬해보자.

## 입력

첫째 줄에 정렬하려고 하는 수 N이 주어진다. N은 1,000,000,000보다 작거나 같은 자연수이다.

## 출력

첫째 줄에 자리수를 내림차순으로 정렬한 수를 출력한다.

## 예제1:
> 입력: 2143
> 출력: 4321

## 예제2:
> 입력: 999998999
> 출력: 999999998', '소트인사이드', 'EASY', 0),
       (14, 14, '## 문제
동규는 세수를 하다가 정렬이 하고 싶어졌다.
정수 세 개를 생각한 뒤에, 이를 오름차순으로 정렬하고 싶어졌다.
정수 세 개가 주어졌을 때, 가장 작은 수, 그 다음 수, 가장 큰 수를 출력하는 프로그램을 작성하시오.

## 입력
정수 세 개가 주어진다. 이 수는 1보다 크거나 같고, 1,000,000보다 작거나 같다. 이 수는 모두 다르다.

## 출력
제일 작은 수, 그 다음 수, 제일 큰 수를 차례대로 출력한다.

## 예제 1
> 입력 : 4 8 7
> 출력 : 4', '세수정렬', 'MEDIUM', 0),
       (15, 15, '## 문제

n개의 정점을 갖는 이진 트리의 정점에 1부터 n까지의 번호가 중복 없이 매겨져 있다.
이와 같은 이진 트리의 인오더와 포스트오더가 주어졌을 때, 프리오더를 구하는 프로그램을 작성하시오.

## 출력

첫째 줄에 n(1 ≤ n ≤ 100,000)이 주어진다.
다음 줄에는 인오더를 나타내는 n개의 자연수가 주어지고,
그 다음 줄에는 같은 식으로 포스트오더가 주어진다.

## 예제1:
> 입력:
3
1 2 3
1 3 2
> 출력: 2 1 3', '트리의 순회', 'HARD', 0),
       (16, 16, '## 문제
현수는 송아지를 잃어버렸다. 다행히 송아지에는 위치추적기가 달려 있다.
현수의 위치와 송아지의 위치가 수직선상의 좌표 점으로 주어지면 현수는 현재 위치에서 송아지의 위치까지 다음과 같은 방법으로 이동한다.
송아지는 움직이지 않고 제자리에 있다.
현수는 스카이 콩콩을 타고 가는데 한 번의 점프로 앞으로 1, 뒤로 1, 앞으로 5를 이동할 수 있다.
최소 몇 번의 점프로 현수가 송아지의 위치까지 갈 수 있는지 구하는 프로그램을 작성하세요.

## 입력
첫 번째 줄에 현수의 위치 S와 송아지의 위치 E가 주어진다. 직선의 좌표 점은 1부터 10,000까지이다.

## 출력
점프의 최소횟수를 구한다. 답은 1이상이며 반드시 존재합니다.

## 예제 1
> 입력 : 5 14
> 출력 : 3', '송아지 찾기1', 'MEDIUM', 0),
       (17, 17, '## 문제

인하은행에는 ATM이 1대밖에 없다. 지금 이 ATM앞에 N명의 사람들이 줄을 서있다. 사람은 1번부터 N번까지 번호가 매겨져 있으며, i번 사람이 돈을 인출하는데 걸리는 시간은 Pi분이다.

사람들이 줄을 서는 순서에 따라서, 돈을 인출하는데 필요한 시간의 합이 달라지게 된다. 예를 들어, 총 5명이 있고, P1 = 3, P2 = 1, P3 = 4, P4 = 3, P5 = 2 인 경우를 생각해보자. [1, 2, 3, 4, 5] 순서로 줄을 선다면, 1번 사람은 3분만에 돈을 뽑을 수 있다. 2번 사람은 1번 사람이 돈을 뽑을 때 까지 기다려야 하기 때문에, 3+1 = 4분이 걸리게 된다. 3번 사람은 1번, 2번 사람이 돈을 뽑을 때까지 기다려야 하기 때문에, 총 3+1+4 = 8분이 필요하게 된다. 4번 사람은 3+1+4+3 = 11분, 5번 사람은 3+1+4+3+2 = 13분이 걸리게 된다. 이 경우에 각 사람이 돈을 인출하는데 필요한 시간의 합은 3+4+8+11+13 = 39분이 된다.

줄을 [2, 5, 1, 4, 3] 순서로 줄을 서면, 2번 사람은 1분만에, 5번 사람은 1+2 = 3분, 1번 사람은 1+2+3 = 6분, 4번 사람은 1+2+3+3 = 9분, 3번 사람은 1+2+3+3+4 = 13분이 걸리게 된다. 각 사람이 돈을 인출하는데 필요한 시간의 합은 1+3+6+9+13 = 32분이다. 이 방법보다 더 필요한 시간의 합을 최소로 만들 수는 없다.

줄을 서 있는 사람의 수 N과 각 사람이 돈을 인출하는데 걸리는 시간 Pi가 주어졌을 때, 각 사람이 돈을 인출하는데 필요한 시간의 합의 최솟값을 구하는 프로그램을 작성하시오.

## 입력

첫째 줄에 사람의 수 N(1 ≤ N ≤ 1,000)이 주어진다. 둘째 줄에는 각 사람이 돈을 인출하는데 걸리는 시간 Pi가 주어진다. (1 ≤ Pi ≤ 1,000)

## 출력

첫째 줄에 각 사람이 돈을 인출하는데 필요한 시간의 합의 최솟값을 출력한다.

## 예제1:
> 입력:
5
3 1 4 3 2
> 출력:
32', 'ATM', 'EASY', 0),
       (18, 18, '## 문제

물류창고에서 근무하는 현수는 n개의 물건을 A창고에서 B창고로 옮기려고 합니다.
현수는 한 번에 5kg이하로만 들고 이동할 수 있습니다. 한 번에 여러개를 옮길 수 있습니다.
A창고에서 B창고로의 왕복을 한 번의 이동이라고 할 때 최소 이동 횟수로 n개의 물건을 모두 옮
기려고 합니다.
매개변수 nums에 n개의 물건의 무게정보가 주어지면 모든 물건을 A창고에서 B창고로 옮기는데
필요한 최소 이동 횟수를 반환하는 프로그램을 작성하세요.

## 제한사항
• nums의 길이는 300,000을 넘지 않습니다.
• 모든 물건의 무게는 2kg이상 5kg이하입니다.

## 예제 1
> 입력 : 2 5 3 4 2 3
> 출력 : 4', '물류창고', 'MEDIUM', 0),
       (19, 19, '## 문제

외판원 순회 문제는 영어로 Traveling Salesman problem (TSP) 라고 불리는 문제로 computer science 분야에서 가장 중요하게 취급되는 문제 중 하나이다. 여러 가지 변종 문제가 있으나, 여기서는 가장 일반적인 형태의 문제를 살펴보자.

1번부터 N번까지 번호가 매겨져 있는 도시들이 있고, 도시들 사이에는 길이 있다. (길이 없을 수도 있다) 이제 한 외판원이 어느 한 도시에서 출발해 N개의 도시를 모두 거쳐 다시 원래의 도시로 돌아오는 순회 여행 경로를 계획하려고 한다. 단, 한 번 갔던 도시로는 다시 갈 수 없다. (맨 마지막에 여행을 출발했던 도시로 돌아오는 것은 예외) 이런 여행 경로는 여러 가지가 있을 수 있는데, 가장 적은 비용을 들이는 여행 계획을 세우고자 한다.

각 도시간에 이동하는데 드는 비용은 행렬 W[i][j]형태로 주어진다. W[i][j]는 도시 i에서 도시 j로 가기 위한 비용을 나타낸다. 비용은 대칭적이지 않다. 즉, W[i][j] 는 W[j][i]와 다를 수 있다. 모든 도시간의 비용은 양의 정수이다. W[i][i]는 항상 0이다. 경우에 따라서 도시 i에서 도시 j로 갈 수 없는 경우도 있으며 이럴 경우 W[i][j]=0이라고 하자.

N과 비용 행렬이 주어졌을 때, 가장 적은 비용을 들이는 외판원의 순회 여행 경로를 구하는 프로그램을 작성하시오.

## 입력

첫째 줄에 도시의 수 N이 주어진다. (2 ≤ N ≤ 16) 다음 N개의 줄에는 비용 행렬이 주어진다. 각 행렬의 성분은 1,000,000 이하의 양의 정수이며, 갈 수 없는 경우는 0이 주어진다. W[i][j]는 도시 i에서 j로 가기 위한 비용을 나타낸다.

항상 순회할 수 있는 경우만 입력으로 주어진다.

## 출력

첫째 줄에 외판원의 순회에 필요한 최소 비용을 출력한다.

## 예제1:
> 입력:
4
0 10 15 20
5 0 9 10
6 13 0 12
8 8 9 0', '외판원 순회', 'HARD', 0),
       (20, 20, '## 문제
수빈이는 동생과 숨바꼭질을 하고 있다. 수빈이는 현재 점 N(0 ≤ N ≤ 100,000)에 있고, 동생은 점 K(0 ≤ K ≤ 100,000)에 있다. 수빈이는 걷거나 순간이동을 할 수 있다. 만약, 수빈이의 위치가 X일 때 걷는다면 1초 후에 X-1 또는 X+1로 이동하게 된다. 순간이동을 하는 경우에는 0초 후에 2*X의 위치로 이동하게 된다.
수빈이와 동생의 위치가 주어졌을 때, 수빈이가 동생을 찾을 수 있는 가장 빠른 시간이 몇 초 후인지 구하는 프로그램을 작성하시오.

## 입력
첫 번째 줄에 수빈이가 있는 위치 N과 동생이 있는 위치 K가 주어진다. N과 K는 정수이다.

## 출력
수빈이가 동생을 찾는 가장 빠른 시간을 출력한다.

## 예제 1
> 입력 : 3 17
> 출력 : 2

## 예제 2
> 입력 : 2 20
> 출력 : 1', '숨바꼭질 3', 'MEDIUM', 0);

# 문제 태그
INSERT INTO `problem_tags`
    (problem_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (3, 1),
       (3, 2),
       (4, 2),
       (5, 3),
       (6, 3),
       (7, 4),
       (8, 4),
       (9, 5),
       (10, 5),
       (11, 6),
       (12, 6),
       (13, 7),
       (14, 7),
       (15, 8),
       (16, 8),
       (17, 9),
       (18, 9),
       (19, 10),
       (20, 10);

# 풀이 가능한 언어
INSERT INTO `available_languages`
    (template_code, programming_languages_id, problem_id)
VALUES ('', 1, 1),
       ('', 1, 2),
       ('', 1, 3),
       ('', 1, 4),
       ('', 1, 5),
       ('', 1, 6),
       ('', 1, 7),
       ('', 1, 8),
       ('', 1, 9),
       ('', 1, 10),
       ('', 1, 11),
       ('', 1, 12),
       ('', 1, 13),
       ('', 1, 14),
       ('', 1, 15),
       ('', 1, 16),
       ('', 1, 17),
       ('', 1, 18),
       ('', 1, 19),
       ('', 1, 20);

# 테스트케이스
INSERT INTO `testcases`
    (problem_id, number, input_names, input_values, output, isGrading)
VALUES (1, 1, 'nums tar', '[1,1,1,1,1] 3', '5', false),
       (1, 2, 'nums tar', '[4,1,2,1] 4', '2', false),
       (1, 1, 'nums tar', '[1,2,1,2] 2', '3', true),
       (2, 1, 'n', '123', '132', false),
       (2, 1, 'n', '321', '-1', true),
       (3, 1, 'n m miro',
        '4 6 [[1,0,1,1,1,1][1,0,1,0,1,0][1,0,1,0,1,1][1,1,1,0,1,1]]',
        '9', false),
       (3, 1, 'n m miro',
        '2 25 [[1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1][1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,1,0,1]]',
        '13', true),
       (4, 1, 'nums', '[2,2,1,2,1,1]', '3', false),
       (4, 1, 'nums', '[1,2,1,2,1,2,1,1,3,1,2,1]', '6', true),
       (5, 1, 'n, s, nums', '5 0 [-7,-3,-2,5,8]', '1', false),
       (5, 1, 'n, s, nums', '15 17 [9,-2,2,-2,1,-3,5,-3,-4,1,0,-9,0,7,1]', '328', true),
       (6, 1, 'n', '7', '40', false),
       (6, 2, 'n', '10', '724', false),
       (6, 1, 'n', '8', '92', true),
       (7, 1, 'array', '[10,20,10,30,20,50]', '4', false),
       (7, 1, 'array', '[1,2,1,2]', '2', true),
       (8, 1, 'n', '3', '3', false),
       (8, 1, 'n', '7', '21', true),
       (9, 1, 'word', 'Mississipi', '?', false),
       (9, 1, 'word', 'zZa', 'z', true),
       (10, 1, 'array', '[1,3,2,5,7,4,2,5,1]', '5', false),
       (10, 1, 'array', '[1,2,1,2,1,2,1,1,3,1,2,1]', '8', true),
       (11, 1, 'a b ', '2 162', '5', false),
       (11, 1, 'a b', '4 42', '-1', true),
       (12, 1, 'buttons', '[10,1,10,2,1]', '6', false),
       (12, 1, 'buttons', '[9,1,6,4,1]', '5', true),
       (12, 1, 'f g s u d', '10 1 10 2 1', '6', false),
       (12, 1, 'f g s u d', '9 1 6 4 1', '5', true),
       (13, 1, 'n', '2143', 'n4321', false),
       (13, 1, 'n', '999998999', '999999998', true),
       (14, 1, 'nums', '[4,8,7]', '[4,7,8]', false),
       (14, 1, 'nums', '[3,1,2]', '[1,2,3]', true),
       (15, 1, 'n inOrder postOrder', '3 [1,2,3] [1,3,2]', '[2,1,3]', false),
       (15, 1, 'n inOrder postOrder', '4 [1,2,3,4] [4,3,2,1]', '[1,2,3,4]', true),
       (16, 1, 's e', '5 14', '3', false),
       (16, 1, 's e', '3 15', '4', true),
       (17, 1, 'p', '[3,1,4,3,2]', '32', false),
       (17, 1, 'p', '[1,2,3,4,1000]', '1030', true),
       (18, 1, 'nums', '[2,5,3,4,2,3]', '4', false),
       (18, 1, 'nums', '[3,3,3,3,3]', '5', true),
       (19, 1, 'costs', '[[0,10,15,20][5,0,9,10][6,13,0,12][8,8,9,0]]', '35', false),
       (19, 1, 'costs', '[[0,7,3,3][7,0,9,2][1,9,0,12][7,7,12,0]]', '20', true),
       (20, 1, 'n k', '3 17', '2', false),
       (20, 1, 'n k', '7 17', '2', true);

# 미니퀴즈
INSERT INTO `mini_quizzes`
(id, description, `explain`, choiceOrInitials, type, difficulty, answer)
VALUES (1, 'BFS의 결과는 트리이다.', '비순환 구조를 갖는다.', '', 'OX', 'EASY', 'O'),
       (2, '초성에 맞는 단어를 채워주세요.', '', 'ㄱ,ㅇ,ㅇ,ㅅ,ㅌ,ㅅ', 'INITIAL', 'MEDIUM', '깊이우선탐색'),
       (3, '깊이우선탐색은?', 'BandWith First Search', 'BFS,DFS,MST,BitMask', 'CHOICE', 'HARD', '2'),
       (4, 'BFS는 큐(Queue) 자료구조를 사용하여 구현된다.', '', '', 'OX', 'EASY', 'O'),
       (5, '초성에 맞는 단어를 채워주세요.', 'BFS는 큐를 사용하여 구현되며, 가장 가까운 노드부터 탐색을 시작하여 너비 우선으로 탐색합니다.', '', 'INITIAL', 'MEDIUM', 'O'),
       (6, '너비우선탐색은?', 'BandWith First Search', 'BFS,DFS,MST,BitMask', 'CHOICE', 'HARD', '1'),
       (7, '백트래킹은 항상 최적해를 찾을 수 있다.', '항상 최적해를 찾을 수 있는 것은 아니며 이 때 다른 방식을 사용해 최적해를 찾을 수 있습니다.', '', 'OX', 'EASY', 'X'),
       (8, '초성에 맞는 단어를 채워주세요.', '', '', 'INITIAL', 'MEDIUM', ''),
       (9, '백트래킹 알고리즘의 주요 특징은 무엇입니까?',
        '백트래킹 알고리즘의 핵심 특징은 해결책을 찾는 과정에서 해가 될 수 없다고 판단되는 지점에서 이전 단계로 ''되돌아가는(Backtrack)'' 것입니다. 이 방법은 브루트 포스 접근법을 개선한 것으로, 불필요한 계산을 줄여 효율성을 높입니다.',
        '모든 가능성을 시도,최적의 해 보장,경로 되돌아가기,데이터 정렬 필요', 'CHOICE', 'HARD', '3'),
       (10, '동적 프로그래밍은 작은 부분 문제의 최적해를 이용하여 전체 문제의 최적해를 구하는 알고리즘 패러다임이다.',
        '동적 프로그래밍은 큰 문제를 작은 부분 문제로 분할하고, 각 부분 문제의 최적해를 저장하여 중복 계산을 피하며 해결하는 방법입니다.', '', 'OX', 'EASY', 'X'),
       (11, '초성에 맞는 단어를 채워주세요.', '', '', 'INITIAL', 'MEDIUM', ''),
       (12, '다이나믹 프로그래밍의 기본 원리는 무엇입니까?', '',
        '모든 가능한 해를 순차적으로 탐색,큰 문제를 작은 문제로 나누고 이를 재사용,빠른 실행을 위해 병렬 처리 사용,모든 데이터를 정렬 후 순차적으로 처리', 'CHOICE', 'HARD', '2'),
       (13, '시뮬레이션은 실제 상황을 모방하여 문제를 해결하는 기법이다.', '', '', 'OX', 'EASY', ''),
       (14, '초성에 맞는 단어를 채워주세요.', '시뮬레이션은 실제 상황을 모방하여 시스템 또는 알고리즘을 테스트하거나 문제를 해결하는 데 사용되는 기법입니다.', '', 'INITIAL',
        'MEDIUM', 'O'),
       (15, '시뮬레이션 알고리즘에서 가장 중요한 요소는 무엇입니까?', '', '데이터 정렬,실제 상황 모델링,최단 경로 찾기,대규모 데이터 처리', 'CHOICE', 'HARD', '2'),
       (16, '그래프(Graph)는 항상 사이클을 포함한다.', '그래프는 항상 사이클을 포함하지는 않으며, 비순환 그래프도 존재합니다.', '', 'OX', 'EASY', 'X'),
       (17, '초성에 맞는 단어를 채워주세요.', '', '', 'INITIAL', 'MEDIUM', ''),
       (18, '그래프 알고리즘에서 ''노드''는 주로 무엇을 나타냅니까?', '', '데이터정렬방식,실행시간,그래프의 정점,메모리 사용량', 'CHOICE', 'HARD', '3'),
       (19, '병합 정렬(Merge Sort)은 항상 O(n^2)의 시간 복잡도를 가진다.', '병합 정렬의 시간 복잡도는 항상 O(n log n)입니다. O(n^2)가 아닙니다.', '', 'OX',
        'EASY', 'X'),
       (20, '초성에 맞는 단어를 채워주세요.', '', '', 'INITIAL', 'MEDIUM', ''),
       (21, '정렬 알고리즘의 주요 목적은 무엇입니까?', '데이터를 빠르게 검색,데이터를 특정 순서대로 배열,메모리 최적화,병렬 처리', '', 'CHOICE', 'HARD', '2'),
       (22, '이진 탐색 트리(Binary Search Tree)에서 중복된 값을 가진 노드를 허용하지 않는다.',
        '이진 탐색 트리에서 중복된 값을 가진 노드를 허용하지 않고, 동일한 값은 하나의 노드에 저장됩니다.', '', 'OX', 'EASY', 'O'),
       (23, '초성에 맞는 단어를 채워주세요.', '그리디 알고리즘은 현재 상황에서 가장 좋은 선택을 하는 방식으로 동작하며, 항상 최적해를 보장하지는 않을 수 있습니다.', '', 'INITIAL',
        'MEDIUM', 'X'),
       (24, '트리 구조에서 ''루트 노드''는 무엇을 의미합니까?', '', '트리의 가장 깊은 노드,트리의 가장 상단에 위치한 노드,트리의 가장 낮은 레벨의 노드,트리의 가장 많은 자식을 가진 노드',
        'CHOICE', 'HARD', ''),
       (25, '그리디 알고리즘은 항상 최적해를 보장한다.', '', '', 'OX', 'EASY', ''),
       (26, '초성에 맞는 단어를 채워주세요.', '', '', 'INITIAL', 'MEDIUM', ''),
       (27, '탐욕 알고리즘의 주요 특징은 무엇입니까?', '', '모든 가능한 해를 탐색,현재 상황에서 최선의 선택만을 취함,실행시간이 매우 길다', 'CHOICE', 'HARD', '2'),
       (28, '다익스트라 알고리즘(Dijkstra''s Algorithm)은 음의 가중치를 가진 그래프에서도 동작한다.',
        '다익스트라 알고리즘은 음의 가중치를 가진 그래프에서는 정확한 결과를 제공하지 않습니다. 가중치는 음이 아닌 값이어야 합니다.', '', 'OX', 'EASY', 'X'),
       (29, '초성에 맞는 단어를 채워주세요.', '', '', 'INITIAL', 'MEDIUM', ''),
       (30, '다익스트라 알고리즘은 주로 어떤 문제를 해결하기 위해 사용됩니까?', '', '데이터 정렬,빠른 검색,최단 경로 찾기,대규모 데이터 처리', 'CHOICE', 'HARD', '3');

# 미니퀴즈 태그
INSERT INTO `mini_quiz_tags`
    (mini_quiz_id, tag_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 2),
       (5, 2),
       (6, 2),
       (7, 3),
       (8, 3),
       (9, 3),
       (10, 4),
       (11, 4),
       (12, 4),
       (13, 5),
       (14, 5),
       (15, 5),
       (16, 6),
       (17, 6),
       (18, 6),
       (19, 7),
       (20, 7),
       (21, 7),
       (22, 8),
       (23, 8),
       (24, 8),
       (25, 9),
       (26, 9),
       (27, 9),
       (28, 10),
       (29, 10),
       (30, 10);
