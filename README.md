Supermarket 2026 电商平台

这是一个基于 Spring Boot 3 和 Vue 3 开发的现代化电商平台项目。系统采用前后端分离架构，具备完整的商品管理、用户购物、订单处理及后台数据统计功能。

🚀 项目特性

🛒 前台购物端

•
商品浏览：支持分类查看商品、搜索商品及查看商品详情。

•
购物车系统：灵活添加、修改商品数量，一键结算。

•
订单管理：用户可查看个人订单状态（待付款、待发货、已收货等）。

•
个人中心：支持个人信息修改、余额充值、我的收藏及评价管理。

•
交互功能：商品评价、收藏功能，以及精美的首页轮播图展示。

⚙️ 后台管理端

•
数据可视化：首页仪表盘展示总销售额、今日销售额、商品总数及用户总数，包含销售趋势折线图和分类占比饼图。

•
商品管理：完整的商品分类、商品信息（价格、库存、描述等）的增删改查。

•
订单处理：管理员可管理全站订单，处理发货及状态变更。

•
用户管理：管理平台注册用户信息及权限。

•
系统配置：轮播图管理、公告管理及管理员账号维护。

🛠️ 技术栈

后端 (Spring Boot)

•
核心框架：Spring Boot 3.3.1

•
持久层：MyBatis + MySQL 8.0

•
工具类：Hutool (全能工具包)

•
分页插件：PageHelper

•
API 通信：OkHttp3 (用于集成外部 API)

•
Java 版本：JDK 21

前端 (Vue.js)

•
核心框架：Vue 3.3.4

•
构建工具：Vite 4.4.11

•
UI 组件库：Element Plus

•
路由管理：Vue Router

•
网络请求：Axios

•
图表展示：ECharts

•
富文本编辑：WangEditor

📂 项目结构

Plain Text


supermarket2026/
├── springboot/           # 后端项目源码
│   ├── src/main/java/    # Java 业务代码
│   ├── src/main/resources/ # 配置文件及 MyBatis Mapper XML
│   └── pom.xml           # Maven 依赖配置
├── vue/                  # 前端项目源码
│   ├── src/views/        # 页面组件 (front/manager)
│   ├── src/components/   # 公共组件
│   └── package.json      # 前端依赖配置
├── code2026.sql          # 数据库初始化脚本
└── files/                # 项目相关资源文件



⚙️ 快速开始

1. 环境准备

•
JDK 21+

•
Node.js 18+

•
MySQL 8.0+

•
Maven 3.8+

2. 数据库配置

1.
创建名为 supermarket 的数据库。

2.
导入根目录下的 code2026.sql 脚本。

3.
修改 springboot/src/main/resources/application.yml 中的数据库连接信息：

YAML


spring:
  datasource:
    username: root
    password: 您的密码
    url: jdbc:mysql://localhost:3306/supermarket?...





3. 启动后端

Bash


cd springboot
mvn clean install
mvn spring-boot:run



后端服务将运行在 http://localhost:9090 。

4. 启动前端

Bash


cd vue
npm install
npm run dev



前端服务将运行在 http://localhost:5173 。

🔐 默认账号

•
管理员：admin / admin

•
普通用户：可在前台自行注册




本项目仅供学习与参考使用。

