# 🛒 在线商城系统 - Online Mall

基于 **Spring Cloud Alibaba** 微服务架构的在线商城系统，实现商品管理、购物车、订单处理、用户认证等核心功能，支持高并发访问和分布式事务处理。

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.3.12-brightgreen)](https://spring.io/projects/spring-boot)
[![Spring Cloud Alibaba](https://img.shields.io/badge/Spring%20Cloud%20Alibaba-2.2.8-blue)](https://github.com/alibaba/spring-cloud-alibaba)
[![Vue](https://img.shields.io/badge/Vue-3.x-green)](https://vuejs.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

---

## 📋 目录

- [项目简介](#项目简介)
- [技术架构](#技术架构)
- [功能模块](#功能模块)
- [项目结构](#项目结构)
- [快速启动](#快速启动)
- [接口文档](#接口文档)
- [性能测试](#性能测试)
- [项目亮点](#项目亮点)
- [界面展示](#界面展示)
- [相关文档](#相关文档)

---

## 📖 项目简介

本项目是一个完整的微服务电商系统，采用前后端分离架构。

**后端**使用 Spring Cloud Alibaba 全家桶，包含 Nacos（服务注册发现）、Gateway（网关）、OpenFeign（服务调用）、Seata（分布式事务）等组件。

**前端**使用 Vue3 + Element Plus，包含用户端（商品浏览、购物车、下单）和管理端（数据看板、商品管理、订单管理）。

**数据库**采用每个服务独立数据库的设计，真正实现微服务数据隔离。

**性能**经过 JMeter 100 并发压测验证，吞吐量达 243.9 TPS，错误率 0%。

---

## 🏗️ 技术架构

### 后端技术栈

| 组件 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.3.12.RELEASE | 基础框架 |
| Spring Cloud Alibaba | 2.2.8.RELEASE | 微服务框架 |
| Spring Cloud Gateway | Hoxton.SR12 | API 网关 |
| Nacos | 2.0.3 | 服务注册与配置中心 |
| OpenFeign | - | 服务间调用 |
| Seata | 1.4.2 | 分布式事务 |
| MySQL | 8.0 | 关系型数据库 |
| Redis | 5.0 | 缓存数据库 |
| MyBatis Plus | 3.4.3.4 | ORM 框架 |
| Knife4j | 3.0.3 | 接口文档 |

### 前端技术栈

| 组件 | 版本 | 用途 |
|------|------|------|
| Vue | 3.x | 前端框架 |
| Element Plus | - | UI 组件库 |
| Axios | - | HTTP 客户端 |
| Vue Router | - | 路由管理 |
| Pinia | - | 状态管理 |
| Vite | - | 构建工具 |

### 架构图
┌─────────────────────────────────────────┐
│ 前端 (Vue3 + Element Plus) │
│ http://localhost:3000 │
└─────────────────────────────────────────┘
│
▼
┌─────────────────────────────────────────┐
│ API 网关 (Spring Cloud Gateway) │
│ http://localhost:9000 │
└─────────────────────────────────────────┘
│
┌───────────┼───────────┬───────────┐
▼ ▼ ▼ ▼
┌───────────┐ ┌───────────┐ ┌───────────┐ ┌───────────┐
│ 商品服务 │ │ 购物车服务 │ │ 订单服务 │ │ 用户服务 │
│ :9001 │ │ :9002 │ │ :9003 │ │ :9004 │
└───────────┘ └───────────┘ └───────────┘ └───────────┘
│ │ │ │
└───────────┴───────────┴───────────┘
│
┌───────────────┼───────────────┐
▼ ▼ ▼
┌────────┐ ┌────────┐ ┌──────────┐
│ Nacos │ │ Redis │ │ MySQL │
│ :8848 │ │ :6379 │ │ :3306 │
└────────┘ └────────┘ └──────────┘

text

---

## 🎯 功能模块

### 用户端
- ✅ 用户登录 / 注册
- ✅ 商品列表浏览（搜索、排序）
- ✅ 商品详情查看
- ✅ **实时库存查询**（Redis 缓存，TTL=10s）
- ✅ 购物车管理（添加、修改数量、删除）
- ✅ **购物车金额实时计算**（后端计算，防篡改）
- ✅ 订单创建（购物车 → 结算 → 下单）
- ✅ **下单自动扣减库存**（数据库行锁防超卖）
- ✅ 订单列表（Tab 筛选：全部/待支付/已完成）
- ✅ 订单详情
- ✅ 取消订单 / 确认收货
- ✅ 个人中心

### 管理端
- ✅ 数据看板（动态数字滚动、订单趋势图、分类占比）
- ✅ 商品管理（增删改查）
- ✅ 订单管理

---

## 📁 项目结构
online-mall/
├── backend/ # 后端微服务

│ ├── mall-common/ # 公共模块（实体类、DTO、配置）

│ ├── mall-product-service/ # 商品服务 :9001

│ ├── mall-cart-service/ # 购物车服务 :9002

│ ├── mall-order-service/ # 订单服务 :9003

│ ├── mall-user-service/ # 用户服务 :9004

│ ├── mall-gateway/ # 网关服务 :9000

│ └── pom.xml # 父 POM

├── frontend/ # 前端项目
│ ├── src/

│ │ ├── api/ # 接口封装

│ │ ├── views/ # 页面组件

│ │ │ ├── admin/ # 管理后台

│ │ │ └── layout/ # 布局组件

│ │ ├── router/ # 路由配置

│ │ ├── store/ # 状态管理

│ │ └── utils/ # 工具函数

│ ├── public/images/ # 商品图片

│ ├── package.json

│ └── vite.config.js

├── docs/ # 项目文档

│ └── 在线商城系统的架构设计与开发.docx

└── README.md

text

---

## 🚀 快速启动

### 环境要求

- JDK 1.8+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0
- Redis 5.0+
- Nacos 2.0.3

### 1. 启动中间件

```bash
# 启动 MySQL（确保服务运行）

# 启动 Redis
redis-server

# 启动 Nacos
cd nacos/bin
startup.cmd -m standalone

# 启动 Seata（可选）
cd seata/bin
seata-server.bat -p 8091 -h 127.0.0.1 -m db
2. 初始化数据库
在 MySQL 中执行数据库初始化脚本，创建 mall_product、mall_cart、mall_order、mall_user、seata 数据库。

3. 启动后端服务
按顺序启动以下服务：

bash
# 1. 商品服务 (9001)
cd backend/mall-product-service
mvn spring-boot:run

# 2. 购物车服务 (9002)
cd backend/mall-cart-service
mvn spring-boot:run

# 3. 订单服务 (9003)
cd backend/mall-order-service
mvn spring-boot:run

# 4. 用户服务 (9004)
cd backend/mall-user-service
mvn spring-boot:run

# 5. 网关服务 (9000)
cd backend/mall-gateway
mvn spring-boot:run
4. 启动前端
bash
cd frontend
npm install
npm run dev
5. 访问系统
地址	说明
http://localhost:3000	前端首页
http://localhost:3000/admin/dashboard	管理后台
http://localhost:8080	Nacos 控制台
http://localhost:9001/doc.html	接口文档
6. 测试账号
用户名	密码	角色
admin	123456	管理员
user	123456	普通用户
📡 接口文档
启动商品服务后，访问 Knife4j 接口文档：

text
http://localhost:9001/doc.html
所有接口通过网关统一访问：

接口	方法	URL
商品列表	GET	/api/product/list
实时库存	GET	/api/product/stock/{id}
添加购物车	POST	/api/cart/add
购物车详情	GET	/api/cart/detail
创建订单	POST	/api/order/create
订单列表	GET	/api/order/list
用户登录	POST	/api/user/login
📊 性能测试
使用 JMeter 5.5 进行 100 并发压测，总请求数 5000：

接口	    样本数	  平均响应	   吞吐量	    错误率
商品列表	  1000	  8ms	       50.6/sec	  0%
实时库存	  1000	  6ms	       50.6/sec	  0%
添加购物车	1000	  13ms	     50.5/sec	  0%
购物车详情	1000	  6ms	       50.6/sec	  0%
创建订单	  1000	  42ms	     50.4/sec	  0%
总体	    5000	  15ms	     237.2/sec	0%
✨ 项目亮点
微服务架构：4 个独立服务 + 网关 + Nacos 注册中心，每个服务独立数据库，真正实现数据隔离
分布式事务：集成 Seata 1.4.2，使用 @GlobalTransactional 保证跨服务事务一致性
防超卖机制：数据库行锁 WHERE stock >= quantity + @Transactional，100 并发下零超卖
高并发优化：Redis 缓存库存数据，读操作响应仅 6-8ms
性能达标：100 并发压测，吞吐量 237 TPS，错误率 0%
前后端分离：Vue3 + Element Plus，包含用户端和管理端
完整测试：功能测试（10+ 用例）、接口测试、性能压测全覆盖

📸 界面展示
用户端
商品列表（搜索、排序、实时库存）
购物车（数量修改、金额实时计算）
订单管理（Tab 筛选、取消/收货）
管理端
数据看板（动态数字、趋势图、分类占比）
商品管理（增删改查）
订单管理
📄 相关文档
项目设计文档
🤝 贡献指南
本项目为课程设计/毕业设计项目，欢迎 Star 和 Fork。
