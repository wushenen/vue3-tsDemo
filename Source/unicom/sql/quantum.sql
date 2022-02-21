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
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'api资源表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_api_resource
-- ----------------------------
INSERT INTO `t_api_resource` VALUES (1, '量子密钥管理', NULL, 0, '量子密钥相关', '2022-01-18 16:52:32', '2022-01-18 16:52:32');
INSERT INTO `t_api_resource` VALUES (2, '获取量子随机数', 'GenerateRandom', 1, '生成指定大小的量子随机数', '2022-01-18 16:52:32', '2022-01-18 16:52:32');
INSERT INTO `t_api_resource` VALUES (3, '获取量子密钥', 'GenerateTempKey', 1, '获取指定大小的量子密钥', '2022-01-18 16:52:32', '2022-01-18 16:52:32');

-- ----------------------------
-- Table structure for t_app
-- ----------------------------
DROP TABLE IF EXISTS `t_app`;
CREATE TABLE `t_app`  (
  `app_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `app_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `app_type` int(1) NOT NULL DEFAULT 1 COMMENT '应用类型：1-专用应用（密钥云终端）；2-通用应用（标准协议应用）',
  `app_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用key',
  `app_secret` varchar(96) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用secret',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后一次修改时间',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`app_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '应用用户表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_app_config
-- ----------------------------
DROP TABLE IF EXISTS `t_app_config`;
CREATE TABLE `t_app_config`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `app_id` int(20) NOT NULL COMMENT '应用id',
  `enc_port` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '加密端口',
  `enc_type` int(1) NULL DEFAULT NULL COMMENT '加密类型（0-不加密，1-AES，2-AES强制加密，6-SM4，7-SM4强制加密）',
  `enc_freq` int(10) NULL DEFAULT NULL COMMENT '密钥更新频率',
  `version` int(10) NULL DEFAULT 1 COMMENT '配置版本信息',
  `start_index` bigint(20) NULL DEFAULT NULL COMMENT '离线充注开始值',
  `end_index` bigint(20) NULL DEFAULT NULL COMMENT '离线充注结束值',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '应用配置表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_app_device
-- ----------------------------
DROP TABLE IF EXISTS `t_app_device`;
CREATE TABLE `t_app_device`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `app_id` int(20) NOT NULL COMMENT '应用id',
  `device_id` int(20) NOT NULL COMMENT '终端用户id',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '应用设备绑定表' ROW_FORMAT = Compact;

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
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '终端权限表' ROW_FORMAT = Compact;

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
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '终端状态信息表' ROW_FORMAT = Compact;

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
  PRIMARY KEY (`device_id`) USING BTREE,
  UNIQUE INDEX `index_device_name`(`device_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '终端用户表' ROW_FORMAT = Compact;

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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分组表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_group_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_group_auth`;
CREATE TABLE `t_group_auth`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `group_id` int(20) NOT NULL COMMENT '分组id',
  `api_id` int(20) NOT NULL COMMENT '资源id',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分组权限表' ROW_FORMAT = Compact;

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
-- Table structure for t_ip
-- ----------------------------
DROP TABLE IF EXISTS `t_ip`;
CREATE TABLE `t_ip`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `ip_info` varchar(31) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '白名单ip',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'ip 白名单表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_key_info
-- ----------------------------
DROP TABLE IF EXISTS `t_key_info`;
CREATE TABLE `t_key_info`  (
  `key_id` binary(16) NOT NULL DEFAULT 0                COMMENT '主键',
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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '量子密钥额度表' ROW_FORMAT = Compact;

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
  `key_source` int(1) NOT NULL COMMENT '密钥源（1-QRNG，2-902, 3-QKD）',
  `source_ip` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥源ip',
  `source_ip2` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用密钥源ip',
  `config_info` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QKD配置信息',
  `priority` int(1) NOT NULL COMMENT '优先级（1-4，1优先级最高,4为不显示配置标识）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '量子密钥源配置表' ROW_FORMAT = Compact;

INSERT INTO `t_key_source_config`(`id`, `key_source`, `source_ip`, `source_ip2`, `config_info`, `priority`) VALUES (1, 2, '0.0.0.0', '', NULL, 2);
INSERT INTO `t_key_source_config`(`id`, `key_source`, `source_ip`, `source_ip2`, `config_info`, `priority`) VALUES (2, 1, NULL, NULL, NULL, 1);
INSERT INTO `t_key_source_config`(`id`, `key_source`, `source_ip`, `source_ip2`, `config_info`, `priority`) VALUES (3, 3, '0.0.0.0', '', '{\"config\":{\"cryptKey\":\"cryptKey\",\"devKey\":\"devKey\",\"localName\":\"localName\",\"peerName\":\"peerName\"},\"config2\":{\"cryptKey\":\"cryptKey\",\"devKey\":\"devKey\",\"localName\":\"localName\",\"peerName\":\"peerName\"}}', 3);


-- ----------------------------
-- Table structure for t_key_source_info
-- ----------------------------
DROP TABLE IF EXISTS `t_key_source_info`;
CREATE TABLE `t_key_source_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_source` int(1) NOT NULL COMMENT '密钥源（1-QRNG，2-902, 3-QKD）',
  `key_generate_rate` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥生成速率',
  `key_generate_num` bigint(20) NULL DEFAULT 0 COMMENT '密钥获取数量',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '量子随机源数据记录表' ROW_FORMAT = Compact;

INSERT INTO `t_key_source_info`(`id`, `key_source`, `key_generate_rate`, `key_generate_num`, `create_time`, `update_time`) VALUES (1, 1, '0.0 Kbps', 0, now(), now());
INSERT INTO `t_key_source_info`(`id`, `key_source`, `key_generate_rate`, `key_generate_num`, `create_time`, `update_time`) VALUES (2, 2, '0.0 Kbps', 0, now(), now());
INSERT INTO `t_key_source_info`(`id`, `key_source`, `key_generate_rate`, `key_generate_num`, `create_time`, `update_time`) VALUES (3, 3, '0.0 Kbps', 0, now(), now());


-- ----------------------------
-- Table structure for t_mail_config_info
-- ----------------------------
DROP TABLE IF EXISTS `t_mail_config_info`;
CREATE TABLE `t_mail_config_info`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `email_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱服务器',
  `email_username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱用户名',
  `email_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱密码（授权码）',
  `email_port` int(255) NULL DEFAULT NULL COMMENT '邮箱端口',
  `email_protocol` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'smtp' COMMENT '邮件安全协议',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

INSERT INTO `unicom2`.`t_mail_config_info`(`id`, `email_host`, `email_username`, `email_password`, `email_port`, `email_protocol`) VALUES (1, 'smtp.exmail.qq.com', 'emaill@qtec.cn', 'password', 465, 'smtp');

-- ----------------------------
-- Table structure for t_mail_log
-- ----------------------------
DROP TABLE IF EXISTS `t_mail_log`;
CREATE TABLE `t_mail_log`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `destination` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮件接收者',
  `detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮件详情',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '操作时间（默认当前）',
  `mail_status` int(1) NULL DEFAULT NULL COMMENT '发送结果，0-发送成功；1-发送失败',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

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
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_update_time`(`update_time`) USING BTREE,
  INDEX `index_operator`(`operator`) USING BTREE,
  INDEX `index_operate_model`(`operate_model`) USING BTREE,
  INDEX `index_detail`(`detail`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3501532 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日志信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_qkm_version
-- ----------------------------
DROP TABLE IF EXISTS `t_qkm_version`;
CREATE TABLE `t_qkm_version`  (
  `mac_addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `state` int(1) NULL DEFAULT 0 COMMENT '0-初始；1-就绪',
  `param` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '初始' COMMENT '什么原因自检失败',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

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
  `account_type` int(1) NULL DEFAULT NULL COMMENT '账户类型:9-超级管理员;1-CA应用管理员;2-CA安全审计员',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户表' ROW_FORMAT = Compact;

INSERT INTO `t_user`(`id`, `user_name`, `password`, `email`, `create_time`, `update_time`, `comments`, `account_type`) VALUES (1, 'admin', 'AD1B04033BE0AAAE4E65EDA11C6F4F31', 'a@qtec.cn', '2022-01-18 16:52:32', '2022-01-18 16:52:32', '系统管理员', 9);


-- ----------------------------
-- Table structure for t_user_app
-- ----------------------------
DROP TABLE IF EXISTS `t_user_app`;
CREATE TABLE `t_user_app`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `app_id` int(20) NOT NULL COMMENT '应用id',
  `user_id` int(20) NOT NULL COMMENT '终端用户id',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '应用管理员与应用绑定表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
