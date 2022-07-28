
DROP DATABASE IF EXISTS `db_divideclass`;

CREATE DATABASE `db_divideclass`  DEFAULT CHARACTER SET utf8 ;

USE `db_divideclass`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `adminType` int(11) NULL DEFAULT NULL COMMENT '管理员类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '12345', 1);
INSERT INTO `admin` VALUES (2, 'cj5174', '12345', 0);
INSERT INTO `admin` VALUES (3, 'hch5175', '12345', 0);
INSERT INTO `admin` VALUES (4, 'lyr5176', '12345', 0);

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `classCode` int(11) UNIQUE NOT NULL COMMENT '班级编号',
  `className` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '班级名称',
  `sum` int(11) NULL DEFAULT 0 COMMENT '实时人数',
  PRIMARY KEY (`classCode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '班级信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES ('1001', '一班', '5');
INSERT INTO `class` VALUES ('1002', '二班', '4');
INSERT INTO `class` VALUES ('1003', '三班', '5');
INSERT INTO `class` VALUES ('1004', '四班', '4');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `stuNum` int(11) UNIQUE NOT NULL COMMENT '学号',
  `stuName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学生名',
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `sex` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `classCode` int(11) NULL DEFAULT NULL COMMENT '班级编号',
  PRIMARY KEY (`stuNum`) USING BTREE,
  FOREIGN KEY (`classCode`) REFERENCES class (`classCode`)
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '学生信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('311971', '李华', '12345', '男', '1001');
INSERT INTO `student` VALUES ('311972', '梁朝伟', '12345', '男', '1003');
INSERT INTO `student` VALUES ('311973', '道明寺', '12345', '男', '1002');
INSERT INTO `student` VALUES ('311974', '李寻欢', '12345', '男', '1002');
INSERT INTO `student` VALUES ('311975', '陈琛', '12345', '男', '1003');
INSERT INTO `student` VALUES ('311976', '陈琛', '12345', '男', '1002');
INSERT INTO `student` VALUES ('311977', '陈世美', '12345', '男', '1004');
INSERT INTO `student` VALUES ('311978', '陈晨', '12345', '男', '1001');
INSERT INTO `student` VALUES ('321974', '何花', '12345', '女', '1002');
INSERT INTO `student` VALUES ('321975', '何恋', '12345', '女', '1002');
INSERT INTO `student` VALUES ('321976', '刘笛', '12345', '女', '1003');
INSERT INTO `student` VALUES ('321977', '赵敏', '12345', '女', '1003');
INSERT INTO `student` VALUES ('321978', '蔡蔡', '12345', '男', '1001');
INSERT INTO `student` VALUES ('321979', '胡慧', '12345', '女', '1004');
INSERT INTO `student` VALUES ('321980', '陈红', '12345', '女', '1004');
INSERT INTO `student` VALUES ('321982', '何莲', '12345', '女', '1001');
INSERT INTO `student` VALUES ('321983', '王晶', '12345', '女', '1004');
INSERT INTO `student` VALUES ('321985', '蔡凌', '12345', '女', '1003');


SET FOREIGN_KEY_CHECKS = 1;
