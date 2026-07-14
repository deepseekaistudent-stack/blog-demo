---
AIGC:
    Label: "1"
    ContentProducer: 001191440300708461136T1XGW3
    ProduceID: 6d7179dfc1c076de82009c8a0508c7eb_c5a7ce397f4911f18ac35254006c9bbf
    ReservedCode1: HRRRVgvyV2lbVbB+GyZ7ldQBy4fEBJaQ8Yo5Ggel0P+QYGDh8b0AMC6MWi32BY4foU/wtxfbifSW2iTRYAcqxMlw2iK/zQRxQQ6j8jZPmtRwG6Hn9ys1K25XB+wcFdmNQVViL3GMrtRI1zfp8E8q+j28NtrSVk4cwNx+kFfod2PUhtTUKxvQPpck/0Y=
    ContentPropagator: 001191440300708461136T1XGW3
    PropagateID: 6d7179dfc1c076de82009c8a0508c7eb_c5a7ce397f4911f18ac35254006c9bbf
    ReservedCode2: HRRRVgvyV2lbVbB+GyZ7ldQBy4fEBJaQ8Yo5Ggel0P+QYGDh8b0AMC6MWi32BY4foU/wtxfbifSW2iTRYAcqxMlw2iK/zQRxQQ6j8jZPmtRwG6Hn9ys1K25XB+wcFdmNQVViL3GMrtRI1zfp8E8q+j28NtrSVk4cwNx+kFfod2PUhtTUKxvQPpck/0Y=
---

# lianxi

整合博客功能和用户系统的 Spring Boot Web 应用，提供前台博客浏览、评论互动，以及后台用户管理与评论管理。

## 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 框架 | Spring Boot | 2.7.18 |
| JDK | Java | 1.8 |
| 模板引擎 | Thymeleaf | (Starter) |
| ORM | MyBatis-Plus | 3.5.3.1 |
| 数据库 | MySQL | 8.0.33 |
| 缓存 | Redis | (Starter) |
| 安全 | Spring Security Crypto (BCrypt) | (Starter) |
| 工具 | Lombok | (Latest) |
| XSS 过滤 | Jsoup | 1.17.2 |

## 功能特性

### 前台博客
- 文章列表分页展示，支持按分类筛选
- 文章详情页，展示正文与评论
- 匿名/署名评论提交，内置 Jsoup XSS 过滤
- 验证码校验 (Redis 存储，2 分钟有效)

### 用户系统
- 用户注册（BCrypt 密码加密）
- 用户登录 / 登出
- 忘记密码 → 重置密码
- Session 会话管理

### 后台管理
- Dashboard 用户列表分页查看
- 评论删除 (REST API)

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 数据库初始化

创建数据库并执行初始化：

```sql
CREATE DATABASE IF NOT EXISTS blog_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
```

项目启动后 MyBatis-Plus 会自动根据 Entity 创建表结构（若已配置自动 DDL），或手动导入 SQL 脚本。

### 配置环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `DB_PASSWORD` | MySQL 密码 | 空 |
| `REDIS_PASSWORD` | Redis 密码 | 空 |
| `LOG_LEVEL` | 日志级别 | INFO |

### 运行

```bash
# 克隆项目
git clone <repo-url>
cd lianxi

# 编译运行
mvn spring-boot:run

# 或打包后运行
mvn clean package -DskipTests
java -jar target/lianxi-1.0.0.jar
```

启动后访问：`http://localhost:8080`

## 项目结构

```
src/main/java/com/example/lianxi/
├── BlogApplication.java              # 应用入口
├── common/
│   └── Result.java                   # 统一 REST 响应体
├── config/
│   ├── CacheCleanupConfig.java       # 缓存清理配置
│   ├── CsrfFilter.java               # CSRF 过滤器
│   ├── MybatisPlusConfig.java        # MyBatis-Plus 配置
│   ├── RedisConfig.java              # Redis 配置
│   └── SecurityConfig.java           # 安全配置 (BCrypt)
├── controller/
│   ├── BlogController.java           # 前台博客 (列表/详情/评论)
│   ├── LoginController.java          # 登录 & Dashboard
│   ├── RegisterController.java       # 用户注册
│   ├── CaptchaController.java        # 验证码生成
│   ├── ForgotController.java         # 忘记密码
│   ├── UserController.java           # 用户管理 REST API
│   └── AdminCommentController.java   # 评论管理 REST API
├── entity/
│   ├── Article.java                  # 文章实体
│   ├── Category.java                 # 分类实体
│   ├── Comment.java                  # 评论实体
│   └── User.java                     # 用户实体
├── exception/
│   ├── BusinessException.java        # 业务异常
│   └── GlobalExceptionHandler.java   # 全局异常处理
├── mapper/
│   ├── ArticleMapper.java
│   ├── CategoryMapper.java
│   ├── CommentMapper.java
│   └── UserMapper.java
├── service/
│   ├── ArticleService.java / impl/
│   ├── CategoryService.java / impl/
│   ├── CommentService.java / impl/
│   └── UserService.java / impl/
└── util/
    ├── CaptchaUtil.java              # 验证码图片生成
    └── HtmlSanitizer.java            # Jsoup XSS 过滤

src/main/resources/
├── application.yml
├── templates/
│   ├── list.html                     # 博客列表页
│   ├── detail.html                   # 文章详情页
│   ├── login.html                    # 登录页
│   ├── register.html                 # 注册页
│   ├── forgot-password.html          # 忘记密码页
│   ├── error.html                    # 错误页
│   └── admin/
│       └── dashboard.html            # 后台仪表盘
```

## API 接口说明

### 前台页面

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/` | 博客列表（支持 `?page=&categoryId=`） |
| GET | `/article/{id}` | 文章详情 + 评论列表 |
| POST | `/comment` | 提交评论 (`articleId, author, content`) |

### 用户认证

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/login` | 登录页面 |
| POST | `/login` | 登录 (`username, password, captcha, uuid`) |
| GET | `/logout` | 退出登录 |
| GET | `/register` | 注册页面 |
| POST | `/register` | 用户注册 (`username, password, confirmPassword, gender, addr`) |
| GET | `/forgot-password` | 忘记密码页面 |
| POST | `/forgot-password` | 重置密码 (`username, newPassword`) |
| GET | `/captcha?uuid=` | 获取验证码图片 |

### 后台管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/admin/dashboard` | 后台仪表盘（需登录） |

### REST API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET/POST/PUT/DELETE | `/api/users` | 用户 CRUD |
| DELETE | `/api/comments/{id}` | 删除评论 |

> 所有 REST 接口返回统一 `Result<T>` 响应体：`{"code":200,"data":...,"message":"success"}`

## 安全机制

- 密码使用 BCrypt 加密存储（`spring-security-crypto`）
- 登录验证码存入 Redis，有效期 2 分钟
- CSRF Token 防护（`CsrfFilter`）
- 评论内容 Jsoup XSS 过滤（`HtmlSanitizer`）
- Session 校验：Dashboard 等后台页面需登录态
