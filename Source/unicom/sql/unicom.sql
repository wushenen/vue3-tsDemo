SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_api_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_api_resource`;
CREATE TABLE `t_api_resource`  (
  `api_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键,接口标识',
  `api_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口名称',
  `api_URL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口请求URL',
  `parent_id` int(20) NOT NULL DEFAULT 0 COMMENT '上级api_id,若是父级默认为0',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`api_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'api资源表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_api_resource
-- ----------------------------
INSERT INTO `t_api_resource` VALUES (1, '主密钥管理', '', 0, '用户主密钥相关', '2021-09-24 13:51:55', '2021-09-24 13:51:55');
INSERT INTO `t_api_resource` VALUES (7, '密码运算', NULL, 0, 'SM2、SM3、SM4等密码运算相关', '2021-10-13 17:00:09', '2021-10-13 17:00:09');
INSERT INTO `t_api_resource` VALUES (8, '量子密钥管理', NULL, 0, '量子密钥相关', '2021-10-13 17:00:20', '2021-10-13 17:00:20');
INSERT INTO `t_api_resource` VALUES (9, '创建主密钥', 'CreateKey', 1, '创建一个主密钥', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (10, '启用主密钥', 'EnableKey', 1, '将一个指定的 CMK 标记为启用状态', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (11, '禁用主密钥', 'DisableKey', 1, '将一个指定的主密钥（CMK）标记为禁用状态', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (12, '申请删除主密钥', 'ScheduleKeyDeletion', 1, '申请删除一个指定的主密钥', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (13, '撤销删除密钥', 'CancelKeyDeletion', 1, '撤销密钥删除', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (14, '查询主密钥信息', 'DescribeKey', 1, '查询指定主密钥（CMK）的相关信息', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (15, '查询调用者主密钥ID', 'ListKeys1', 1, '查询调用者在调用区域的所有主密钥ID', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (16, '替换主密钥描述信息', 'UpdateKeyDescription', 1, '替换主密钥描述信息', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (17, '查询指定密钥版本信息', 'DescribeKeyVersion', 1, '查询指定密钥版本信息', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (18, '查询主密钥所有版本', 'ListKeyVersions', 1, '列出主密钥的所有密钥版本', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (19, '更新密钥轮转策略', 'UpdateRotationPolicy', 1, '更新密钥轮转策略', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (20, '创建非对称主密钥新版本', 'CreateKeyVersion', 1, '为非对称主密钥创建一个新的密钥版本', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (51, '在线算法验证SM2', 'arithmeticsm2', 7, '在线算法验证SM2', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (52, '在线算法验证SM3', 'arithmeticsm3', 7, '在线算法验证SM3', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (53, '在线算法验证SM4', 'arithmeticsm4', 7, '在线算法验证SM4', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (60, '获取随机数', 'GenerateRandom', 8, '生成随机数', '2021-10-13 17:25:22', '2021-10-13 17:25:22');
INSERT INTO `t_api_resource` VALUES (61, '获取临时密钥', 'GenerateTempKey', 8, '产生临时密钥', '2021-10-13 17:25:22', '2021-10-13 17:25:22');

-- ----------------------------
-- Table structure for t_app_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_app_auth`;
CREATE TABLE `t_app_auth`  (
  `auth_id` int(20) NOT NULL AUTO_INCREMENT,
  `app_user_id` int(20) NOT NULL COMMENT '终端用户id',
  `api_id` int(20) NOT NULL COMMENT '资源id',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后一次修改时间',
  PRIMARY KEY (`auth_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '应用用户权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_app_secret
-- ----------------------------
DROP TABLE IF EXISTS `t_app_secret`;
CREATE TABLE `t_app_secret`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用用户标识',
  `app_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用key',
  `app_secret` varchar(96) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用secret',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `app_key`(`app_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '应用用户应用表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_app_user
-- ----------------------------
DROP TABLE IF EXISTS `t_app_user`;
CREATE TABLE `t_app_user`  (
  `user_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户标识',
  `login_pass` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `status` int(1) NULL DEFAULT NULL COMMENT '用户状态：0：不可用；1：可用',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后一次修改时间',
  `commit_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '应用用户表' ROW_FORMAT = Compact;


-- ----------------------------
-- Table structure for t_card_data
-- ----------------------------
DROP TABLE IF EXISTS `t_card_data`;
CREATE TABLE `t_card_data`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `card_version` int(4) NULL DEFAULT NULL,
  `card_data` blob NULL,
  `sql_data` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `mac_addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '密码卡信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_device_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_device_auth`;
CREATE TABLE `t_device_auth`  (
  `auth_id` int(20) NOT NULL AUTO_INCREMENT,
  `device_id` int(20) NOT NULL COMMENT '终端用户id',
  `api_id` int(20) NOT NULL COMMENT '资源id',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后一次修改时间',
  PRIMARY KEY (`auth_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '终端权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_device_operation
-- ----------------------------
DROP TABLE IF EXISTS `t_device_operation`;
CREATE TABLE `t_device_operation`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键，终端用户id',
  `device_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `operation` int(1) NOT NULL DEFAULT 0 COMMENT '设备操作（0-无操作，1-重启，2-置零，默认0',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备操作表（记录置零或重启）' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_device_status
-- ----------------------------
DROP TABLE IF EXISTS `t_device_status`;
CREATE TABLE `t_device_status`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键,id',
  `device_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '终端名称，即设备id',
  `device_ip` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端ip',
  `work_status` tinyint(1) NULL DEFAULT 0 COMMENT '终端状态',
  `online_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '上线时间',
  `key_num` bigint(20) NULL DEFAULT 0 COMMENT '密钥使用量',
  `enc_data` bigint(20) NULL DEFAULT 0 COMMENT '加密数据',
  `dec_data` bigint(20) NULL DEFAULT 0 COMMENT '解密数据',
  `enc_rate` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '加密速率',
  `dec_rate` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '解密速率',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '终端状态信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_device_user
-- ----------------------------
DROP TABLE IF EXISTS `t_device_user`;
CREATE TABLE `t_device_user`  (
  `device_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键，终端用户id',
  `device_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `user_type` int(1) NOT NULL DEFAULT 0 COMMENT '用户类型(0-软件用户，1-硬件用户，默认0)',
  `enc_key` binary(48) NULL DEFAULT NULL COMMENT '密钥加密密钥',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`device_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '终端用户表' ROW_FORMAT = Compact;


-- ----------------------------
-- Table structure for t_encryption_context
-- ----------------------------
DROP TABLE IF EXISTS `t_encryption_context`;
CREATE TABLE `t_encryption_context`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `key_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `key_hash` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密文+EncryptionContext  做的sm3',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `key_version_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_encrytion_context_context_hash`(`key_hash`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '密文表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_group
-- ----------------------------
DROP TABLE IF EXISTS `t_group`;
CREATE TABLE `t_group`  (
  `group_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键，分组id',
  `group_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组名称',
  `group_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组唯一标志',
  `group_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组描述',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分组表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_group_app_user
-- ----------------------------
DROP TABLE IF EXISTS `t_group_app_user`;
CREATE TABLE `t_group_app_user`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键，唯一标识',
  `group_id` int(20) NULL DEFAULT NULL COMMENT '分组表主键id',
  `app_user_id` int(20) NULL DEFAULT NULL COMMENT '应用用户id',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分组应用用户表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_group_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_group_auth`;
CREATE TABLE `t_group_auth`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `group_id` int(20) NOT NULL COMMENT '角色id',
  `api_id` int(20) NOT NULL COMMENT '资源id',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分组权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_group_device_user
-- ----------------------------
DROP TABLE IF EXISTS `t_group_device_user`;
CREATE TABLE `t_group_device_user`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键，唯一标识',
  `group_id` int(20) NULL DEFAULT NULL COMMENT '分组表主键id',
  `device_id` int(20) NULL DEFAULT NULL COMMENT '用户表主键id',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分组终端用户表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_group_user
-- ----------------------------
DROP TABLE IF EXISTS `t_group_user`;
CREATE TABLE `t_group_user`  (
  `group_user_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键，唯一标识',
  `group_id` int(20) NULL DEFAULT NULL COMMENT '分组表主键id',
  `device_id` int(20) NULL DEFAULT NULL COMMENT '用户表主键id',
  `app_user_id` int(20) NULL DEFAULT NULL COMMENT '应用用户id',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`group_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分组成员表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_ip
-- ----------------------------
DROP TABLE IF EXISTS `t_ip`;
CREATE TABLE `t_ip`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `ip_info` varchar(31) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '白名单ip',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'ip 白名单表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_ip
-- ----------------------------
INSERT INTO `t_ip` VALUES (1, '127.0.0.1', '2021-11-01 17:08:58');

-- ----------------------------
-- Table structure for t_key_info
-- ----------------------------
DROP TABLE IF EXISTS `t_key_info`;
CREATE TABLE `t_key_info`  (
  `key_id` binary(16) NOT NULL DEFAULT 0 COMMENT '主键',
  `key_value` binary(48) NOT NULL COMMENT '密钥',
  `key_status` int(1) NOT NULL DEFAULT 0 COMMENT '密钥状态（0-可用，1-不可用，2-已使用，默认0）',
  `applicant` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密钥申请者',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`key_id`) USING BTREE,
  UNIQUE INDEX `key_id`(`key_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '量子密钥生成表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_key_limit
-- ----------------------------
DROP TABLE IF EXISTS `t_key_limit`;
CREATE TABLE `t_key_limit`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `user_type` int(1) NULL DEFAULT NULL COMMENT '用户类型（1-终端用户，2-应用用户）',
  `limit_num` int(11) NULL DEFAULT NULL COMMENT '量子密钥额度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '量子密钥额度表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_key_offline
-- ----------------------------
DROP TABLE IF EXISTS `t_key_offline`;
CREATE TABLE `t_key_offline`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `key_id` binary(16) NOT NULL COMMENT 'keyId',
  `key_value` binary(48) NOT NULL COMMENT '密钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `key_id`(`key_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '离线量子密钥充注表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_key_source_config
-- ----------------------------
DROP TABLE IF EXISTS `t_key_source_config`;
CREATE TABLE `t_key_source_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `key_source` int(1) NOT NULL COMMENT '密钥源（1-QRNG，2-密码卡，3-902, 4-QKD）',
  `source_ip` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥源ip',
  `source_ip2` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用密钥源ip',
  `config_info` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QKD配置信息',
  `priority` int(1) NOT NULL COMMENT '优先级（1-5，1优先级最高,5为不显示配置标识）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '量子密钥源配置表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_key_source_config
-- ----------------------------
INSERT INTO `t_key_source_config` VALUES (1, 3, '0.0.0.0', '', NULL, 2);
INSERT INTO `t_key_source_config` VALUES (2, 2, NULL, NULL, NULL, 1);
INSERT INTO `t_key_source_config` VALUES (3, 1, NULL, NULL, NULL, 4);
INSERT INTO `t_key_source_config` VALUES (4, 4, '0.0.0.0', '', '{\"config\":{\"cryptKey\":\"32409943A28D6B6F52070F3C7847D1C492DBF72FE0CC0E8BD8BA168F55A907D2\",\"devKey\":\"A4F4A168C72D4D60E8A911F2788598D9F99388432E25AEE6F8101AC3B5150FDC\",\"localName\":\"ROUTER_1\",\"peerName\":\"ROUTER_2\"},\"config2\":{\"cryptKey\":\"A4F4A168C72D4D60E8A911F2788598D9F99388432E25AEE6F8101AC3B5150FDC\",\"devKey\":\"A4F4A168C72D4D60E8A911F2788598D9F99388432E25AEE6F8101AC3B5150FDC\",\"localName\":\"ROUTER_2\",\"peerName\":\"ROUTER_1\"}}', 3);

-- ----------------------------
-- Table structure for t_key_version
-- ----------------------------
DROP TABLE IF EXISTS `t_key_version`;
CREATE TABLE `t_key_version`  (
  `key_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密钥标识',
  `key_version_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密钥版本的标志符',
  `creation_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建密钥版本的时间',
  `key_data` blob NULL COMMENT '初始密钥加密的密钥',
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥创建者',
  `pub_key_data` blob NULL,
  `pri_key_data` blob NULL,
  `card_index` int(2) NULL DEFAULT NULL,
  PRIMARY KEY (`key_id`, `key_version_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '密钥版本表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_mail_config_info
-- ----------------------------
DROP TABLE IF EXISTS `t_mail_config_info`;
CREATE TABLE `t_mail_config_info`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `email_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱服务器',
  `email_username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱用户名',
  `email_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱密码（授权码）',
  `email_port` int(255) NULL DEFAULT NULL COMMENT '邮箱密码（授权码）',
  `email_protocol` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'smtp' COMMENT '邮件安全协议',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_mail_config_info
-- ----------------------------
INSERT INTO `t_mail_config_info` VALUES (1, 'smtp.exmail.qq.com', 'emaill@qtec.cn', 'password', 465, 'smtp');

-- ----------------------------
-- Table structure for t_mail_log
-- ----------------------------
DROP TABLE IF EXISTS `t_mail_log`;
CREATE TABLE `t_mail_log`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `destination` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮件接收者',
  `detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮件主题',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '操作时间（默认当前）',
  `mail_status` int(1) NULL DEFAULT NULL COMMENT '操作后结果状态0-发送成功；1-发送失败',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_material
-- ----------------------------
DROP TABLE IF EXISTS `t_material`;
CREATE TABLE `t_material`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `key_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密钥标识',
  `key_material_expire_unix` bigint(20) NULL DEFAULT NULL COMMENT '密钥材料的过期时间',
  `wrapping_algorithm` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用于加密密钥材料的算法',
  `wrapping_key_spec` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用于加密密钥材料的公钥类型',
  `public_key` blob NULL COMMENT '用于加密密钥材料的公钥',
  `encrypted_key_material` blob NULL COMMENT '加密后的密钥材料',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '密钥材料表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `t_operate_log`;
CREATE TABLE `t_operate_log`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `operator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员名称',
  `operate_model` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作模块',
  `detail` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作功能',
  `operate_ip` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作者IP',
  `operate_status` int(1) NULL DEFAULT NULL COMMENT '操作后结果状态0-成功；1-失败',
  `exec_time` int(11) NULL DEFAULT NULL COMMENT '操作执行时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '操作时间（默认当前）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日志信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission`  (
  `permission_id` int(20) NOT NULL AUTO_INCREMENT,
  `permission` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `desc` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `avaliable` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`permission_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_primary_key
-- ----------------------------
DROP TABLE IF EXISTS `t_primary_key`;
CREATE TABLE `t_primary_key`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `key_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥标识',
  `key_spec` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥的类型',
  `key_usage` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥的用途',
  `origin` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'QTEC_KMS' COMMENT '密钥材料来源',
  `protection_level` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'SOFTWARE' COMMENT '密钥的保护级别',
  `automatic_rotation` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'Disabled' COMMENT 'Disabled;Enabled;Suspended',
  `rotation_interval` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自动轮转的时间周期',
  `creation_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建主密钥的日期',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥的描述',
  `key_state` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主密钥的状态',
  `primary_key_version` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对称类型主密钥的当前主版本标志符',
  `delete_date` datetime(0) NULL DEFAULT NULL COMMENT '主密钥的预计删除时间',
  `last_rotation_date` datetime(0) NULL DEFAULT NULL COMMENT '最近一次轮转的时间',
  `arn` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '云资源名称',
  `owner` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_key_id`(`key_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '主密钥信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_qems_config
-- ----------------------------
DROP TABLE IF EXISTS `t_qems_config`;
CREATE TABLE `t_qems_config`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `config_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置名称',
  `enc_port` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '加密端口',
  `enc_type` int(1) NULL DEFAULT NULL COMMENT '加密类型（0-不加密，1-AES，2-AES强制加密，6-SM4，7-SM4强制加密）',
  `enc_freq` int(10) NULL DEFAULT NULL COMMENT '密钥更新频率',
  `version` int(10) NULL DEFAULT 1 COMMENT '配置版本信息',
  `start_index` bigint(20) NULL DEFAULT NULL COMMENT '离线充注开始值',
  `end_index` bigint(20) NULL DEFAULT NULL COMMENT '离线充注结束值',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'qems配置信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_qems_config
-- ----------------------------
INSERT INTO `t_qems_config` VALUES (1, 'config1', '0', 1, 60, 10, 1, 1000, '2021-12-01 11:19:42', '2021-12-06 16:12:36');

-- ----------------------------
-- Table structure for t_qkm_version
-- ----------------------------
DROP TABLE IF EXISTS `t_qkm_version`;
CREATE TABLE `t_qkm_version`  (
  `mac_addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '{\"MYSQL版本\":\"5.7.36\",\"管理工具版本\":\"V1.0.1\",\"密码卡版本\":\"V3.7.1\",\"USB-KEY版本\":\"V1.2 \"}',
  `state` int(1) NULL DEFAULT 0 COMMENT '0-初始；1-就绪',
  `param` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '初始' COMMENT '什么原因自检失败',
  `key_control_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1234567812345678',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '量子密钥云平台',
  `verify_war` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '20080229-001-00123',
  `public_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sign_war` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_qkm_version
-- ----------------------------
INSERT INTO `t_qkm_version` VALUES ('3c:ec:ef:87:9b:82', 1, '{\"MYSQL版本\":\"5.7.36\",\"管理工具版本\":\"V1.0.1\",\"密码卡版本\":\"V3.7.1\",\"USB-KEY版本\":\"V1.2 \"}', 1, '就绪', '1234567812345678', '量子密钥云平台', '5FE0A2ADF96CD6B07F8CB85FF2D2124B3D1672576C585E5833789ED03F0ADEC8', '20080229-001-00123', '2f285052d5d491ee8610b122cb08c312c89c58e4eddd200a5dec0cd67b9e55047343093af6a48bea4d3a73ef8360819da1f18fba343806d8d3ed1962571e3ebb', '30440220157A8557AE63B4E5BCD2330F67E3A5F31E2E49576A15DB3A6D963F6E3012812602207E6014440D4748449BB33CE692F4C12953181108675C339978CB0193034F9597');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `role_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键，随机生成',
  `role_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色code，唯一',
  `role_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `inner_user` int(1) NULL DEFAULT 1 COMMENT '内置角色，0-内置，1-不是内置，默认1',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间，默认当前时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新操作时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_role_app_user
-- ----------------------------
DROP TABLE IF EXISTS `t_role_app_user`;
CREATE TABLE `t_role_app_user`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键，唯一标识',
  `role_id` int(20) NULL DEFAULT NULL COMMENT '角色表主键id',
  `app_user_id` int(20) NULL DEFAULT NULL COMMENT '应用用户id',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色应用用户表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_role_auth`;
CREATE TABLE `t_role_auth`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `role_id` int(20) NOT NULL COMMENT '角色id',
  `api_id` int(20) NOT NULL COMMENT '资源id',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_role_device_user
-- ----------------------------
DROP TABLE IF EXISTS `t_role_device_user`;
CREATE TABLE `t_role_device_user`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键，唯一标识',
  `role_id` int(20) NULL DEFAULT NULL COMMENT '角色表主键id',
  `device_id` int(20) NULL DEFAULT NULL COMMENT '用户表主键id',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色终端用户表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_role_user
-- ----------------------------
DROP TABLE IF EXISTS `t_role_user`;
CREATE TABLE `t_role_user`  (
  `role_user_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键，唯一标识',
  `role_id` int(20) NULL DEFAULT NULL COMMENT '角色表主键id',
  `device_id` int(20) NULL DEFAULT NULL COMMENT '用户表主键id',
  `app_user_id` int(20) NULL DEFAULT NULL COMMENT '应用用户id',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`role_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色成员表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_status_show
-- ----------------------------
DROP TABLE IF EXISTS `t_status_show`;
CREATE TABLE `t_status_show`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键,id',
  `key_num` bigint(20) NULL DEFAULT 0 COMMENT '密钥使用量',
  `enc_data` bigint(20) NULL DEFAULT 0 COMMENT '加密数据',
  `dec_data` bigint(20) NULL DEFAULT 0 COMMENT '解密数据',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '数据时间（创建时间）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '每日状态信息数据统计表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_strategy
-- ----------------------------
DROP TABLE IF EXISTS `t_strategy`;
CREATE TABLE `t_strategy`  (
  `strategy_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键，策略id',
  `strategy_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '策略类型：1-API，2-数据（默认1，当前版本只支持1）',
  `strategy_type` int(1) NULL DEFAULT NULL COMMENT '策略类型',
  `strategy_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '策略描述',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间，默认当前时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间，默认当前时间',
  PRIMARY KEY (`strategy_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '策略信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_strategy_action
-- ----------------------------
DROP TABLE IF EXISTS `t_strategy_action`;
CREATE TABLE `t_strategy_action`  (
  `strategy_action_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键，策略操作id',
  `strategy_id` int(20) NULL DEFAULT NULL COMMENT '策略id',
  `api_id` int(20) NULL DEFAULT NULL COMMENT 'api_id',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间，默认当前时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间，默认当前时间',
  PRIMARY KEY (`strategy_action_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '策略操作表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_strategy_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_strategy_auth`;
CREATE TABLE `t_strategy_auth`  (
  `strategy_auth_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `device_id` int(20) NULL DEFAULT NULL COMMENT '被授权终端id',
  `role_id` int(20) NULL DEFAULT NULL COMMENT '被授权角色id',
  `group_id` int(20) NULL DEFAULT NULL COMMENT '被授权分组id',
  `app_user_id` int(20) NULL DEFAULT NULL COMMENT '应用用户id',
  `strategy_id` int(20) NULL DEFAULT NULL COMMENT '策略id',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`strategy_auth_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '策略授权表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_temp_key
-- ----------------------------
DROP TABLE IF EXISTS `t_temp_key`;
CREATE TABLE `t_temp_key`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `key_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `temp_key` blob NOT NULL COMMENT 'Base64编码密钥',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '临时密钥表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户标识',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理员邮箱',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最近一次修改时间',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `account_type` int(1) NULL DEFAULT NULL COMMENT '账户类型:\r\n9-CA超级管理员\r\n1-CA业务操作员\r\n2-CA审计员',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'admin', 'AD1B04033BE0AAAE4E65EDA11C6F4F31', 'a@qtec.cn', '2021-11-30 14:36:47', '2021-12-10 13:15:37', '系统管理员', 9);

SET FOREIGN_KEY_CHECKS = 1;
