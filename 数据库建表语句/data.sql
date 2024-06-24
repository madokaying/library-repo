-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: library_web_db
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `book_tag`
--

DROP TABLE IF EXISTS `book_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_tag` (
  `book_id` int NOT NULL COMMENT '书籍的编号',
  `tag_id` int NOT NULL COMMENT '标签的编号',
  PRIMARY KEY (`book_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='书籍和标签的关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_tag`
--

LOCK TABLES `book_tag` WRITE;
/*!40000 ALTER TABLE `book_tag` DISABLE KEYS */;
INSERT INTO `book_tag` VALUES (45,1),(45,3),(45,4),(45,15),(46,9),(46,12),(47,1),(47,3),(48,5),(48,10);
/*!40000 ALTER TABLE `book_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT COMMENT '评论列表',
  `content` varchar(600) NOT NULL COMMENT '评论内容',
  `created_time` datetime DEFAULT NULL COMMENT '评论时间',
  `commenting_target_id` int NOT NULL COMMENT '评论对象的id',
  `user_id` bigint NOT NULL COMMENT '评论用户id',
  `commenting_target` tinyint NOT NULL COMMENT '评论对象:1、对书的评论    2、对帖子的评论     3、对用户的评论（回复）',
  `delete_flag` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0 未删/ 1 删除',
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,'挺好看的','2024-05-23 18:49:19',45,15,1,0),(2,'.','2024-05-23 18:50:04',45,15,1,0),(3,'确实，可惜我看不下去','2024-05-23 18:52:49',1,15,3,0),(4,'真的推荐你去看一下','2024-05-24 23:03:14',3,15,3,0),(5,'测试，三级评论','2024-05-25 15:33:56',3,15,3,0),(6,'测试书籍评论功能',NULL,45,15,1,0),(7,'测试评论时间是否能正确插入','2024-05-29 14:02:44',45,15,1,0),(8,'效果测试','2024-05-29 14:25:33',45,15,1,0),(9,'刷屏刷屏刷屏','2024-05-29 14:25:45',45,15,1,0),(10,'6666666','2024-05-29 14:25:51',45,15,1,0),(11,'刷屏','2024-05-29 14:31:07',45,15,1,0),(12,'刷屏','2024-05-29 14:31:15',45,15,1,0),(13,'继续测试','2024-05-29 15:06:45',45,15,1,0),(14,'能不能别刷屏了？','2024-05-29 15:07:08',12,15,3,0),(15,'测试完毕','2024-05-29 15:07:22',13,15,3,0),(16,'测试评论子评论','2024-05-29 15:07:35',15,15,3,0),(17,'测试回复子评论的子评论','2024-05-29 15:07:52',16,15,3,0),(18,'测试测试测试','2024-05-29 15:24:16',6,15,3,0),(19,'测试','2024-05-29 17:36:56',45,15,1,0),(20,'继续测试','2024-05-29 17:37:53',45,15,1,0),(21,'多用户评论测试','2024-05-29 17:41:28',20,18,3,0),(22,'三级评论测试','2024-05-29 17:43:29',21,17,3,0),(23,'测试drawer','2024-05-30 12:25:46',22,15,3,0),(24,'enter点击效果测试','2024-05-30 12:42:35',23,15,3,0),(25,'长度测试66666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666','2024-05-30 17:10:57',45,15,1,0),(26,'白夜行评论测试','2024-06-05 11:19:45',47,15,1,0),(27,'评论测试2','2024-06-11 16:34:12',45,15,1,0),(28,'多级评论测试','2024-06-11 16:34:28',27,15,3,0),(29,'多级评论测试12','2024-06-11 16:34:43',28,15,3,0),(30,'多级评论测试12','2024-06-11 16:34:44',28,15,3,0);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '菜单名',
  `path` varchar(200) DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int DEFAULT '0' COMMENT '是否删除（0未删除 1已删除）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (0,'管理员权限',NULL,NULL,'0','0','admin','#',NULL,'2024-06-05 18:50:47',NULL,NULL,0,NULL),(1,'删除书籍',NULL,NULL,'0','0','system:book:delete','#',NULL,'2024-06-05 18:50:49',NULL,NULL,0,NULL);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `post_id` int NOT NULL AUTO_INCREMENT COMMENT '帖子id',
  `post_title` varchar(64) NOT NULL COMMENT '帖子标题',
  `post_content` varchar(2000) NOT NULL COMMENT '帖子内容',
  `post_image` varchar(255) DEFAULT NULL COMMENT '帖子图片',
  `created_time` datetime DEFAULT NULL COMMENT '发帖时间',
  `owner_id` int NOT NULL COMMENT '楼主id',
  `delete_flag` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除:0未删，1已删',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='帖子';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `role_key` varchar(100) DEFAULT NULL COMMENT '角色权限字符串',
  `status` char(1) DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
  `del_flag` int DEFAULT '0' COMMENT 'del_flag',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (0,'管理员','admin','0',0,NULL,'2024-04-02 16:37:00',NULL,'2024-04-02 16:37:00','管理员，拥有全部权限'),(1,'未实名用户','unauthenticated_user','0',0,NULL,'2024-04-02 16:37:00',NULL,'2024-04-02 16:37:00','未实名的用户，无法享受借书相关的服务'),(2,'实名用户','user','0',0,NULL,'2024-04-13 21:02:29',NULL,'2024-04-13 21:02:45','实名用户，可以享有正常用户的一切权益');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_menu`
--

DROP TABLE IF EXISTS `role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_menu` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `menu_id` bigint NOT NULL DEFAULT '0' COMMENT '菜单id',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_menu`
--

LOCK TABLES `role_menu` WRITE;
/*!40000 ALTER TABLE `role_menu` DISABLE KEYS */;
INSERT INTO `role_menu` VALUES (0,0);
/*!40000 ALTER TABLE `role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `tag_id` int NOT NULL AUTO_INCREMENT COMMENT '标签的编号',
  `tag_name` varchar(8) NOT NULL COMMENT '标签名',
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='书籍的标签';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (1,'悬疑'),(2,'魔幻'),(3,'推理'),(4,'未来世界'),(5,'古代'),(6,'穿越'),(7,'种田'),(8,'养成'),(9,'谋略'),(10,'古典仙侠'),(11,'西幻'),(12,'西方'),(13,'群像'),(14,'恋爱'),(15,'惊悚'),(16,'魔法'),(17,'校园'),(18,'体育'),(19,'搞笑'),(20,'轻松'),(21,'科幻');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_book`
--

DROP TABLE IF EXISTS `tb_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_book` (
  `book_id` int NOT NULL AUTO_INCREMENT COMMENT '同名图书的编号，作为主键，一种书一个编号，与另一张表（保存每本书的序列号）是一对多的关系，用逻辑外键联系，一种书有多本书，每本书都有一个唯一的序列号\n一种书只有一个id，一本书下的每本书都有不同的序列号',
  `book_name` varchar(12) NOT NULL COMMENT '书名',
  `book_author` varchar(20) NOT NULL COMMENT '作者',
  `book_summary` varchar(255) DEFAULT '暂无简介' COMMENT '简介/内容摘要',
  `publisher` varchar(12) DEFAULT '暂无出版社' COMMENT '出版社',
  `physical_book_price` decimal(5,2) unsigned NOT NULL COMMENT '实体书价格，五位数，其中两位为小数',
  `book_cover` varchar(120) DEFAULT '' COMMENT '图书的封面',
  `borrowed_times` int DEFAULT '0' COMMENT '借阅次数',
  `version` tinyint DEFAULT '1' COMMENT '乐观锁',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除，0.未删 1.已逻辑删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`book_id`),
  UNIQUE KEY `book_book_name_uindex` (`book_name`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='存放图书信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_book`
--

LOCK TABLES `tb_book` WRITE;
/*!40000 ALTER TABLE `tb_book` DISABLE KEYS */;
INSERT INTO `tb_book` VALUES (45,'惊悚乐园','三天两觉','　　一个号称可以让玩家体验到的绝望和恐怖感觉的游戏，能否让他重拾恐惧？\n\n　　这是超越维度的游戏。\n\n　　亦是追寻真理的竞逐。\n\n　　未知的封印，鬼神的赌局……\n\n　　数据的抗争，人类的救赎……\n\n　　在那个连接着现实的虚拟世界——意识决定我们，意识选择我们，意识决定了我们的意识。\n\n　　现在，丢掉你的恐惧。\n\n　　丢掉你的私心杂念、疑问和拒信……解放你的思想。\n\n　　欢迎来到，惊悚乐园。','网文',30.00,'http://localhost:8080/static/cover/45.jpg',3,1,0,'2024-06-04 14:32:06',NULL),(46,'肖申克的救赎','斯蒂芬.金','小说的背景是20世纪30年代，那时，“美国的政治腐败已经到了商人面前”，甚至波及到了监狱。故事中，年轻的银行家安迪因为妻子和她的情人被杀而被判无期徒刑，由于监狱的腐败，他在真相即将大白的情况下仍然得不到昭雪，反而在肖申克监狱饱受了各种精神上和肉体上的摧残。然而，安迪并没有被多舛的命运毁掉，他经过10多年水滴石穿般地不懈挖掘，终于在一个雷雨交加的夜晚，从500码长的污粪管道中爬出，重获自由，在墨西哥海边过上了自由人的生活。','人民文学出版社',30.00,'http://localhost:8080/static/cover/46.jpg',1,1,0,'2024-06-04 15:17:56',NULL),(47,'白夜行','东野圭吾','故事围绕着一对有着不同寻常情愫的小学生展开。1973年，大阪的一栋废弃建筑内发现了一具男尸，此后19年，嫌疑人之女雪穗与被害者之子桐原亮司走上截然不同的人生道路，一个跻身上流社会，一个却在底层游走，而他们身边的人，却接二连三地离奇死去，警察经过19年的艰苦追踪，终于使真相大白。作品以雪穗和亮司的畸形之恋折射社会现实问题，反映家庭教育对孩子身心发展的重要影响。','人民文学出版社',30.00,'http://localhost:8080/static/cover/47.jpg',0,1,0,'2024-06-04 16:16:34',NULL),(48,'笑傲江湖','金庸','《笑傲江湖》是中国现代作家金庸创作的一部长篇武侠小说，1967年开始创作并连载于《明报》，1969年完成。这部小说通过叙述华山派大弟子令狐冲的江湖经历，反映了武林各派争霸夺权的历程。作品没有设置时代背景，“类似的情景可以发生在任何朝代”，折射出中国人独特的政治斗争状态，同时也表露出对这种争斗的哀叹，具有一定的政治寓意。小说情节跌宕起伏，波谲云诡，人物形象个性鲜明，生动可感。','人民文学出版社',30.00,'http://localhost:8080/static/cover/48.jpg',0,1,0,'2024-06-04 16:36:56',NULL),(49,'解忧杂货铺','东野圭吾','暂无简介','无',50.00,'http://localhost:8080/static/cover/49.jpg',0,1,1,'2024-06-08 22:04:18','2024-06-08 22:04:18'),(50,'123','123','meiiyou','12',30.00,'http://localhost:8080/static/cover/50.jpg',0,1,1,'2024-06-11 16:27:59','2024-06-11 16:27:59');
/*!40000 ALTER TABLE `tb_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_book_identifier`
--

DROP TABLE IF EXISTS `tb_book_identifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_book_identifier` (
  `tb_book_id` int NOT NULL COMMENT '书籍编号',
  `tb_identifier` int NOT NULL COMMENT '具体书籍的编号',
  `book_state` tinyint DEFAULT '0' COMMENT '书籍状态:0为在馆，1为借出，2为卖出',
  `delete_flag` tinyint DEFAULT '0' COMMENT '逻辑删除:0为未删，1，为已删',
  PRIMARY KEY (`tb_book_id`,`tb_identifier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='每种书下具体的书籍对应的编号';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_book_identifier`
--

LOCK TABLES `tb_book_identifier` WRITE;
/*!40000 ALTER TABLE `tb_book_identifier` DISABLE KEYS */;
INSERT INTO `tb_book_identifier` VALUES (45,1,1,0),(45,2,0,0),(45,3,0,0),(45,4,0,0),(46,46001,0,0),(46,46002,0,0),(47,47001,0,0);
/*!40000 ALTER TABLE `tb_book_identifier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_borrow`
--

DROP TABLE IF EXISTS `tb_borrow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_borrow` (
  `borrow_id` int NOT NULL AUTO_INCREMENT COMMENT '借书申请id',
  `user_id` int NOT NULL COMMENT '借书用户的id',
  `book_id` int NOT NULL COMMENT '所借书籍的编号',
  `book_identifier` int DEFAULT NULL COMMENT '书籍具体编号',
  `state` tinyint DEFAULT '0' COMMENT '状态：0为默认申请的状态，1为审批通过，书属于借出状态、2为审批不通过 、3为审批通过且书已归还',
  `created_time` datetime DEFAULT NULL COMMENT '申请发起时间',
  `checked_time` datetime DEFAULT NULL COMMENT '管理员审批完成时间',
  `delete_flag` tinyint DEFAULT '0' COMMENT '逻辑删除 0未删 1已删',
  PRIMARY KEY (`borrow_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='借书表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_borrow`
--

LOCK TABLES `tb_borrow` WRITE;
/*!40000 ALTER TABLE `tb_borrow` DISABLE KEYS */;
INSERT INTO `tb_borrow` VALUES (1,15,45,1,3,'2024-06-01 23:49:32','2024-06-09 20:05:23',1),(3,17,45,4,3,'2024-06-09 19:47:03','2024-06-11 16:31:56',1),(4,15,45,NULL,2,'2024-06-09 20:50:57','2024-06-09 22:24:40',1),(6,15,45,1,1,'2024-06-09 22:24:46','2024-06-11 16:29:30',1),(7,15,46,46001,3,'2024-06-09 22:39:06','2024-06-09 22:39:36',1),(8,15,45,NULL,0,'2024-06-11 16:30:25',NULL,0),(9,15,46,NULL,0,'2024-06-11 16:30:30',NULL,0),(10,15,47,NULL,0,'2024-06-11 16:30:34',NULL,0),(11,17,45,NULL,0,'2024-06-11 16:37:24',NULL,0);
/*!40000 ALTER TABLE `tb_borrow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_collection`
--

DROP TABLE IF EXISTS `tb_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_collection` (
  `book_id` int NOT NULL COMMENT '书籍编号',
  `user_id` int NOT NULL COMMENT '用户编号',
  PRIMARY KEY (`book_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户书籍收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_collection`
--

LOCK TABLES `tb_collection` WRITE;
/*!40000 ALTER TABLE `tb_collection` DISABLE KEYS */;
INSERT INTO `tb_collection` VALUES (45,15),(45,17),(47,15);
/*!40000 ALTER TABLE `tb_collection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_search`
--

DROP TABLE IF EXISTS `tb_search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_search` (
  `search_id` int NOT NULL AUTO_INCREMENT COMMENT '搜索id',
  `search_content` varchar(32) NOT NULL COMMENT '搜索内容',
  `search_times` int DEFAULT '1' COMMENT '搜索次数',
  PRIMARY KEY (`search_id`,`search_content`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='搜索表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_search`
--

LOCK TABLES `tb_search` WRITE;
/*!40000 ALTER TABLE `tb_search` DISABLE KEYS */;
INSERT INTO `tb_search` VALUES (1,'诡秘之主',6),(2,'乐园',9),(3,'江湖',1),(4,'惊悚',2),(5,'白夜',2),(6,'笑傲江湖',1);
/*!40000 ALTER TABLE `tb_search` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键/编号',
  `username` varchar(15) DEFAULT NULL COMMENT '用户名/账号',
  `nickname` varchar(12) DEFAULT '书客β' COMMENT '昵称',
  `password` varchar(256) NOT NULL COMMENT '密码',
  `real_name` varchar(6) DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `address` varchar(64) DEFAULT NULL COMMENT '住址',
  `id_card_number` varchar(32) DEFAULT NULL COMMENT '身份证号码',
  `avatar` varchar(128) DEFAULT 'http://localhost:8080/static/img/defaultAvatar.jpg' COMMENT '头像',
  `background` varchar(128) DEFAULT 'http://localhost:8080/static/img/defaultBackground.jpg' COMMENT '个性壁纸',
  `signature` varchar(64) DEFAULT NULL COMMENT '个性签名',
  `phone_number` varchar(11) DEFAULT NULL,
  `max_borrow` tinyint DEFAULT '5' COMMENT '最多可借书数',
  `need_to_pay` int DEFAULT '0' COMMENT '待付金额',
  `state` char(1) DEFAULT '1' COMMENT '状态：1.可用 2.停用/禁用',
  `version` tinyint DEFAULT '1' COMMENT '乐观锁',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：\n0.未删\n1.已删',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (15,'123123123','homura','$2a$10$xgfW.MBN9k9B7y6XAyK7wOFmgjGtGXg6g9DdlSmX.QdFEImEBNsHe','岁远','123456789@outlook.com','昆明市xx区xx路xx号','00000000000','http://localhost:8080/static/img/c4d029a8-c793-4596-9701-2eb895d8c1b3.jpeg','http://localhost:8080/static/img/30cfbb62-ef77-4733-aac5-cc53d7afb6eb.png','焰是圆的','10086',4,5,'1',7,0,'2024-04-18 22:08:19','2024-06-04 12:48:22'),(17,'1234123412','测试人员甲','$2a$10$DDCPZ8SskOeD7GJvUek8X.mQqDWW0qAh6J3scW61NnsR/ldYavHvG','丁真','111@outlook.com','昆明市xx区xx路xx号','00000000001','http://localhost:8080/static/img/1ea72b9b-30d9-42da-b091-7fe69d0bbe8c.jpeg','http://localhost:8080/static/img/defaultBackground.jpg','请多指教','100866',5,0,'1',4,0,'2024-05-29 16:00:06','2024-05-29 16:00:06'),(18,'12341234123','书客β','$2a$10$HO.AuGpgR5vDr4sQLnUFwe8wByv40rCyDnvmqR3q6oZq2RvMsIQgi',NULL,NULL,NULL,NULL,'http://localhost:8080/static/img/defaultAvatar.jpg','http://localhost:8080/static/img/defaultBackground.jpg',NULL,NULL,5,0,'1',1,0,'2024-05-29 16:01:02','2024-05-29 16:01:02'),(19,'12312312312','用户a','$2a$10$MfEHwRJ5jijFrIZgw79Bne9NPOsfP45/8AGjG8LO8CNWoD.Y/qhUy',NULL,'2985077269@q.com',NULL,NULL,'http://localhost:8080/static/img/01d8197b-2018-4f99-b758-ad45006cace0.jpeg','http://localhost:8080/static/img/defaultBackground.jpg','你好世界',NULL,5,0,'1',1,0,'2024-06-11 16:20:58','2024-06-11 16:20:58');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `role_id` tinyint NOT NULL DEFAULT '1' COMMENT '角色id',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (15,0),(17,2),(18,1),(19,1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-24 20:27:38
