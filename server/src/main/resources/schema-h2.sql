-- H2 数据库兼容的 schema
-- 用于替代 MySQL 的 schema.sql

-- 文件表
DROP TABLE IF EXISTS lin_file;
CREATE TABLE lin_file
(
    id          INTEGER          NOT NULL AUTO_INCREMENT,
    path        VARCHAR(500)     NOT NULL,
    type        VARCHAR(10)      NOT NULL DEFAULT 'LOCAL',
    name        VARCHAR(100)     NOT NULL,
    extension   VARCHAR(50)               DEFAULT NULL,
    size        INTEGER                   DEFAULT NULL,
    md5         VARCHAR(40)               DEFAULT NULL,
    create_time TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    delete_time TIMESTAMP                 DEFAULT NULL,
    is_deleted  INTEGER                   DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_md5_del ON lin_file(md5, delete_time);

-- 日志表
DROP TABLE IF EXISTS lin_log;
CREATE TABLE lin_log
(
    id          INTEGER          NOT NULL AUTO_INCREMENT,
    message     VARCHAR(450)              DEFAULT NULL,
    user_id     INTEGER          NOT NULL,
    username    VARCHAR(24)               DEFAULT NULL,
    status_code INTEGER                   DEFAULT NULL,
    method      VARCHAR(20)               DEFAULT NULL,
    path        VARCHAR(50)               DEFAULT NULL,
    permission  VARCHAR(100)              DEFAULT NULL,
    create_time TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    delete_time TIMESTAMP                 DEFAULT NULL,
    is_deleted  INTEGER                   DEFAULT 0,
    PRIMARY KEY (id)
);

-- 权限表
DROP TABLE IF EXISTS lin_permission;
CREATE TABLE lin_permission
(
    id          INTEGER          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(60)      NOT NULL,
    module      VARCHAR(50)      NOT NULL,
    mount       INTEGER          NOT NULL DEFAULT 1,
    create_time TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    delete_time TIMESTAMP                 DEFAULT NULL,
    is_deleted  INTEGER                   DEFAULT 0,
    PRIMARY KEY (id)
);

-- 分组表
DROP TABLE IF EXISTS lin_group;
CREATE TABLE lin_group
(
    id          INTEGER          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(60)      NOT NULL,
    info        VARCHAR(255)              DEFAULT NULL,
    level       INTEGER          NOT NULL DEFAULT 3,
    create_time TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    delete_time TIMESTAMP                 DEFAULT NULL,
    is_deleted  INTEGER                   DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_name_del ON lin_group(name, delete_time);

-- 分组-权限表
DROP TABLE IF EXISTS lin_group_permission;
CREATE TABLE lin_group_permission
(
    id            INTEGER          NOT NULL AUTO_INCREMENT,
    group_id      INTEGER          NOT NULL,
    permission_id INTEGER          NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_group_permission ON lin_group_permission(group_id, permission_id);

-- 用户基本信息表
DROP TABLE IF EXISTS lin_user;
CREATE TABLE lin_user
(
    id          INTEGER          NOT NULL AUTO_INCREMENT,
    username    VARCHAR(24)      NOT NULL,
    nickname    VARCHAR(24)               DEFAULT NULL,
    avatar      VARCHAR(500)              DEFAULT NULL,
    email       VARCHAR(100)              DEFAULT NULL,
    create_time TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    delete_time TIMESTAMP                 DEFAULT NULL,
    is_deleted  INTEGER                   DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_username_del ON lin_user(username, delete_time);
CREATE UNIQUE INDEX idx_email_del ON lin_user(email, delete_time);

-- 用户授权信息表
DROP TABLE IF EXISTS lin_user_identity;
CREATE TABLE lin_user_identity
(
    id            INTEGER          NOT NULL AUTO_INCREMENT,
    user_id       INTEGER          NOT NULL,
    identity_type VARCHAR(100)     NOT NULL,
    identifier    VARCHAR(100),
    credential    VARCHAR(100),
    create_time   TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    delete_time   TIMESTAMP                 DEFAULT NULL,
    is_deleted    INTEGER                   DEFAULT 0,
    PRIMARY KEY (id)
);

-- 用户-分组表
DROP TABLE IF EXISTS lin_user_group;
CREATE TABLE lin_user_group
(
    id       INTEGER          NOT NULL AUTO_INCREMENT,
    user_id  INTEGER          NOT NULL,
    group_id INTEGER          NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_user_group ON lin_user_group(user_id, group_id);

-- 图书表
DROP TABLE IF EXISTS book;
CREATE TABLE book
(
    id          INTEGER          NOT NULL AUTO_INCREMENT,
    title       VARCHAR(50)      NOT NULL,
    author      VARCHAR(30)               DEFAULT NULL,
    summary     VARCHAR(1000)             DEFAULT NULL,
    image       VARCHAR(100)              DEFAULT NULL,
    create_time TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    delete_time TIMESTAMP                 DEFAULT NULL,
    is_deleted  INTEGER                   DEFAULT 0,
    PRIMARY KEY (id)
);

-- 插入超级管理员
INSERT INTO lin_user(id, username, nickname)
VALUES (1, 'root', 'root');

INSERT INTO lin_user_identity (id, user_id, identity_type, identifier, credential)
VALUES (1, 1, 'USERNAME_PASSWORD', 'root',
        '$2a$10$jE0nxSiFEBNAKg2AjW3OfO.b.nmT.wqkcquuFia5GAKdPrWRwlZhW');

INSERT INTO lin_group(id, name, info, level)
VALUES (1, 'root', '超级用户组', 1);

INSERT INTO lin_group(id, name, info, level)
VALUES (2, 'guest', '游客组', 2);

INSERT INTO lin_user_group(id, user_id, group_id)
VALUES (1, 1, 1);

-- 插入图书数据
INSERT INTO book(title, author, summary, image) VALUES ('深入理解计算机系统', 'Randal E.Bryant', '从程序员的视角，看计算机系统！本书适用于那些想要写出更快、更可靠程序的程序员。', 'https://img3.doubanio.com/lpic/s1470003.jpg');
INSERT INTO book(title, author, summary, image) VALUES ('C程序设计语言', '（美）Brian W. Kernighan', '在计算机发展的历史上，没有哪一种程序设计语言像C语言这样应用广泛。', 'https://img3.doubanio.com/lpic/s1106934.jpg');
