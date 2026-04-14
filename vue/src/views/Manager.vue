<template>
  <div class="manager-container">
    <header class="manager-header">
      <div class="header-content">
        <div class="platform-info">
          <img src="@/assets/imgs/logo.png" alt="平台Logo" class="platform-logo">
          <div class="platform-name">乐享购电商平台</div>
        </div>
        <div class="user-info">
          <img class="user-avatar" :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="用户头像">
          <span class="user-name">{{ data.user.name || '管理员' }}</span>
        </div>
      </div>
    </header>

    <main class="manager-main">
      <aside class="sidebar">
        <el-menu
            router
            class="custom-menu"
            :default-active="router.currentRoute.value.path"
            :default-openeds="['user', 'info']"
        >
          <el-menu-item index="/manager/home">
            <el-icon><HomeFilled /></el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-sub-menu index="info">
            <template #title>
              <el-icon><Memo /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/orders">
              <el-icon><Document /></el-icon>
              <span>订单信息</span>
            </el-menu-item>
            <el-menu-item index="/manager/category">
              <el-icon><Document /></el-icon>
              <span>商品分类</span>
            </el-menu-item>
            <el-menu-item index="/manager/goods">
              <el-icon><Document /></el-icon>
              <span>商品信息</span>
            </el-menu-item>
            <el-menu-item index="/manager/carousel">
              <el-icon><Document /></el-icon>
              <span>轮播图信息</span>
            </el-menu-item>
            <el-menu-item index="/manager/comment">
              <el-icon><Document /></el-icon>
              <span>订单评价</span>
            </el-menu-item>
            <el-menu-item index="/manager/collect">
              <el-icon><Document /></el-icon>
              <span>用户收藏</span>
            </el-menu-item>
            <el-menu-item index="/manager/recharge">
              <el-icon><Document /></el-icon>
              <span>用户充值</span>
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="user">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/user">
              <el-icon><User /></el-icon>
              <span>普通用户</span>
            </el-menu-item>
            <el-menu-item index="/manager/admin">
              <el-icon><User /></el-icon>
              <span>管理员信息</span>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/manager/person">
            <el-icon><User /></el-icon>
            <span>个人信息</span>
          </el-menu-item>
          <el-menu-item index="/manager/password">
            <el-icon><Lock /></el-icon>
            <span>修改密码</span>
          </el-menu-item>
          <el-menu-item class="logout-item" @click="logout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出系统</span>
          </el-menu-item>
        </el-menu>
      </aside>
      <section class="content-area">
        <router-view @updateUser="updateUser" />
      </section>
    </main>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router";
import {ElMessage} from "element-plus";

const data = reactive({
  // 从本地存储中获取用户信息，如果不存在则使用空对象
  user: JSON.parse(localStorage.getItem('system-user') || '{}')  // 从本地存储中获取用户信息并解析为JavaScript对象赋值给user变量
})

if (!data.user?.id) {
  ElMessage.error('请登录！')
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('system-user') || '{}')
}

const logout = () => {
  router.push('/login')
  ElMessage.success('退出成功')
  localStorage.removeItem('system-user')
}
</script>

<style scoped>
.manager-container {
  min-height: 100vh;
  background-color: #f4f4f4;
}

.manager-header {
  height: 60px;
  background: linear-gradient(135deg, #2e3143 0%, #3a3e57 100%);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  border-bottom: 1px solid #e6e6e6;
}

.header-content {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.platform-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.platform-logo {
  width: 40px;
  height: 40px;
  border-radius: 6px;
  transition: transform 0.2s ease;
}

.platform-logo:hover {
  transform: scale(1.05);
}

.platform-name {
  font-size: 20px;
  font-weight: 700;
  color: #fff;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.user-info {
  display: flex;
  align-items: center;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.user-name {
  color: #fff;
  margin-left: 5px;
}

.manager-main {
  display: flex;
  height: calc(100vh - 60px);
}

.sidebar {
  width: 220px;
  background: #fff;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  border-right: 1px solid #e6e6e6;
}

.custom-menu {
  border: none;
  height: 100%;
}

.custom-menu .el-menu-item,
.custom-menu .el-sub-menu__title {
  height: 48px;
  line-height: 48px;
  color: #666;
  font-size: 14px;
  transition: all 0.3s ease;
  border-left: 3px solid transparent;
}

.custom-menu .el-menu-item:hover,
.custom-menu .el-sub-menu__title:hover {
  color: #2e3143;
  background-color: #f8f9fa;
  border-left-color: #2e3143;
}

.custom-menu .el-menu-item.is-active {
  background: linear-gradient(90deg, rgba(46, 49, 67, 0.1) 0%, rgba(255, 255, 255, 0) 100%);
  color: #2e3143;
  border-left-color: #2e3143;
}

.logout-item {
  margin-top: 10px;
  border-top: 1px solid #e6e6e6;
  color: #f56c6c !important;
}

.logout-item:hover {
  color: #f56c6c !important;
  background-color: #fef0f0 !important;
  border-left-color: #f56c6c !important;
}

.content-area {
  flex: 1;
  background-color: #f8f9fa;
  padding: 20px;
  overflow-y: auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    width: 180px;
  }

  .header-content {
    padding: 0 15px;
  }

  .platform-name {
    font-size: 18px;
  }
}
</style>