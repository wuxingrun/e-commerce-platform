<template>
  <div class="login-container">
    <div class="login-background">
      <!-- 背景装饰元素 -->
      <div class="bg-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
        <div class="shape shape-4"></div>
      </div>

      <!-- 平台优势展示 -->
      <div class="features">
        <div class="feature-item">
          <div class="feature-icon">🚚</div>
          <div class="feature-text">快速配送</div>
        </div>
        <div class="feature-item">
          <div class="feature-icon">💰</div>
          <div class="feature-text">品质保证</div>
        </div>
        <div class="feature-item">
          <div class="feature-icon">🛡️</div>
          <div class="feature-text">售后无忧</div>
        </div>
      </div>

      <div class="platform-info">
        <div class="logo-title">
          <img src="@/assets/imgs/logo.png" alt="乐享购" class="logo">
          <div class="platform-name">乐享购电商购物平台</div>
        </div>
        <div class="platform-description">让购物更简单，让生活更精彩</div>
        <div class="platform-stats">
          <div class="stat-item">
            <div class="stat-number">1000+</div>
            <div class="stat-label">优质商品</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">99%</div>
            <div class="stat-label">用户满意度</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">24h</div>
            <div class="stat-label">极速送达</div>
          </div>
        </div>
      </div>
    </div>
    <div class="login-box">
      <div class="welcome-title">欢迎登录</div>
      <div class="subtitle">开启您的购物之旅</div>
      <el-form :model="data.form" ref="formRef" :rules="data.rules" class="login-form">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item prop="role">
          <el-select size="large" style="width: 100%" v-model="data.form.role">
            <el-option value="普通用户" label="普通用户"></el-option>
            <el-option value="管理员" label="管理员"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" class="login-btn" @click="login">立即登录</el-button>
        </el-form-item>
      </el-form>
      <div class="register-link">
        还没有账号？<a href="/register">立即注册</a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock } from "@element-plus/icons-vue";
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";

const data = reactive({
  dialogVisible: true,
  form: { role: '管理员' },
  rules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' },
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
    ],
  }
})

const formRef = ref()

// 点击登录按钮的时候会触发这个方法
const login = () => {
  formRef.value.validate((valid => {
    if (valid) {
      // 调用后台的接口
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success("登录成功")
          localStorage.setItem('system-user', JSON.stringify(res.data))
          if (res.data.role === '管理员') {
            router.push('/manager/home')
          } else {
            router.push('/front/home')
          }
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })).catch(error => {
    console.error(error)
  })
}

</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}

.login-background {
  flex: 1;
  background: linear-gradient(135deg, #FF6B6B 0%, #8B5CF6 100%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 40px;
  position: relative;
  overflow: hidden;
}

.login-background::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="50" cy="50" r="1" fill="rgba(255,255,255,0.1)"/></pattern></defs><rect width="100" height="100" fill="url(%23grain)"/></svg>');
  opacity: 0.3;
}

.platform-info {
  text-align: center;
  color: white;
  z-index: 1;
}

.logo-title {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.logo {
  width: 60px;
  height: 60px;
  margin-right: 15px;
}

.platform-name {
  font-size: 32px;
  font-weight: bold;
  letter-spacing: 2px;
}

.platform-description {
  font-size: 18px;
  opacity: 0.9;
  margin-top: 10px;
  margin-bottom: 30px;
}

.bg-shapes {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
}

.shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
  animation: float 6s ease-in-out infinite;
}

.shape-1 {
  width: 100px;
  height: 100px;
  background: rgba(255, 255, 255, 0.3);
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.shape-2 {
  width: 150px;
  height: 150px;
  background: rgba(255, 255, 255, 0.2);
  top: 60%;
  right: 15%;
  animation-delay: 2s;
}

.shape-3 {
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.4);
  bottom: 20%;
  left: 20%;
  animation-delay: 4s;
}

.shape-4 {
  width: 120px;
  height: 120px;
  background: rgba(255, 255, 255, 0.25);
  top: 30%;
  right: 25%;
  animation-delay: 1s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
}

.features {
  display: flex;
  justify-content: center;
  gap: 40px;
  margin-bottom: 40px;
  z-index: 1;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: white;
  opacity: 0.8;
  transition: all 0.3s ease;
}

.feature-item:hover {
  opacity: 1;
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 24px;
  margin-bottom: 8px;
}

.feature-text {
  font-size: 14px;
  font-weight: bold;
}

.platform-stats {
  display: flex;
  justify-content: center;
  gap: 30px;
  z-index: 1;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: white;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 12px;
  opacity: 0.8;
}

.login-box {
  width: 400px;
  background: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 50px 40px;
  box-shadow: -5px 0 30px rgba(0, 0, 0, 0.1);
}

.welcome-title {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  text-align: center;
  margin-bottom: 10px;
}

.subtitle {
  font-size: 16px;
  color: #666;
  text-align: center;
  margin-bottom: 40px;
}

.login-form {
  margin-bottom: 30px;
}

.el-form-item {
  margin-bottom: 25px;
}

.el-input, .el-select {
  border-radius: 8px;
}

.el-input:hover, .el-select:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.login-btn {
  width: 100%;
  height: 50px;
  font-size: 16px;
  font-weight: bold;
  border-radius: 8px;
  background: linear-gradient(135deg, #FF6B6B 0%, #8B5CF6 100%);
  border: none;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(255, 107, 107, 0.3);
}

.register-link {
  text-align: center;
  color: #666;
}

.register-link a {
  color: #FF6B6B;
  text-decoration: none;
  font-weight: bold;
  transition: all 0.3s ease;
}

.register-link a:hover {
  color: #8B5CF6;
  text-decoration: underline;
}

@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }

  .login-background {
    padding: 20px;
  }

  .login-box {
    width: 100%;
    box-shadow: none;
  }
}
</style>