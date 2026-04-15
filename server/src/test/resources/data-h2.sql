MERGE INTO lin_user(id, username, nickname, email) KEY(id) VALUES (2, 'testuser', 'Test User', 'test@example.com');
MERGE INTO lin_user(id, username, nickname, email) KEY(id) VALUES (3, 'admin', 'Admin User', 'admin@example.com');

MERGE INTO lin_user_identity (id, user_id, identity_type, identifier, credential) KEY(id) VALUES (2, 2, 'USERNAME_PASSWORD', 'testuser', '$2a$10$jE0nxSiFEBNAKg2AjW3OfO.b.nmT.wqkcquuFia5GAKdPrWRwlZhW');
MERGE INTO lin_user_identity (id, user_id, identity_type, identifier, credential) KEY(id) VALUES (3, 3, 'USERNAME_PASSWORD', 'admin', '$2a$10$jE0nxSiFEBNAKg2AjW3OfO.b.nmT.wqkcquuFia5GAKdPrWRwlZhW');

MERGE INTO lin_group(id, name, info, level) KEY(id) VALUES (3, '普通用户组', '普通用户组', 3);

MERGE INTO lin_user_group(id, user_id, group_id) KEY(id) VALUES (2, 2, 2);
MERGE INTO lin_user_group(id, user_id, group_id) KEY(id) VALUES (3, 3, 3);

MERGE INTO lin_permission(id, name, module, mount) KEY(id) VALUES (1, '查看图书', '图书', 1);
MERGE INTO lin_permission(id, name, module, mount) KEY(id) VALUES (2, '添加图书', '图书', 1);
MERGE INTO lin_permission(id, name, module, mount) KEY(id) VALUES (3, '编辑图书', '图书', 1);
MERGE INTO lin_permission(id, name, module, mount) KEY(id) VALUES (4, '删除图书', '图书', 1);
MERGE INTO lin_permission(id, name, module, mount) KEY(id) VALUES (5, '查看用户', '用户', 1);

MERGE INTO lin_group_permission(id, group_id, permission_id) KEY(id) VALUES (1, 1, 1);
MERGE INTO lin_group_permission(id, group_id, permission_id) KEY(id) VALUES (2, 1, 2);
MERGE INTO lin_group_permission(id, group_id, permission_id) KEY(id) VALUES (3, 1, 3);
MERGE INTO lin_group_permission(id, group_id, permission_id) KEY(id) VALUES (4, 1, 4);
MERGE INTO lin_group_permission(id, group_id, permission_id) KEY(id) VALUES (5, 2, 1);
MERGE INTO lin_group_permission(id, group_id, permission_id) KEY(id) VALUES (6, 3, 1);

MERGE INTO lin_file(id, path, type, name, extension, size, md5) KEY(id) VALUES (1, '/upload/test.jpg', 'LOCAL', 'test.jpg', 'jpg', 1024, 'abc123');
MERGE INTO lin_file(id, path, type, name, extension, size, md5) KEY(id) VALUES (2, 'http://qiniu.com/test.png', 'QINIU', 'test.png', 'png', 2048, 'def456');

MERGE INTO lin_log(id, message, user_id, username, status_code, method, path, permission) KEY(id) VALUES (1, '测试日志1', 1, 'root', 200, 'GET', '/v1/book', '查看图书');
MERGE INTO lin_log(id, message, user_id, username, status_code, method, path, permission) KEY(id) VALUES (2, '测试日志2', 2, 'testuser', 200, 'POST', '/v1/book', '添加图书');

MERGE INTO book(id, title, author, summary, image) KEY(id) VALUES (1, '深入理解计算机系统', 'Randal E.Bryant', '从程序员的视角，看计算机系统！', 'https://img3.doubanio.com/lpic/s1470003.jpg');
MERGE INTO book(id, title, author, summary, image) KEY(id) VALUES (2, 'C程序设计语言', 'Brian W. Kernighan', 'C语言经典教材', 'https://img3.doubanio.com/lpic/s1106934.jpg');
MERGE INTO book(id, title, author, summary, image) KEY(id) VALUES (3, 'Java编程思想', 'Bruce Eckel', 'Java经典著作', 'https://img3.doubanio.com/lpic/s1234567.jpg');
