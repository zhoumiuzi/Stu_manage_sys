<script setup>
import {reactive, ref} from 'vue';
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";

// 定义表单数据
const data = reactive({
  form: {
    username: '',
    password: '',
    name: '',
    role: 'Student',

  }
});

// 定义表单验证规则
const rules = reactive({
  username: [
    {required: true, message: '请输入账号！', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码！', trigger: 'blur'},
  ],
  name: [
    {required: true, message: '请输入姓名！', trigger: 'blur'},
  ],
});

const ruleFormRef = ref();

// validate 方法用于验证整个表单的数据是否合法
const register = () => {
  ruleFormRef.value.validate((valid) => {
    if (valid) {
      request.post('/register', data.form).then(res => {
        console.log('Response:', res); // 调试返回值
        if (res.code === '200') {
          localStorage.setItem('student-user', JSON.stringify(res.data));
          ElMessage.success("注册成功");
          router.push('/login');
        } else {
          ElMessage.error("注册失败");
        }
      }).catch(error => {
        // 处理请求异常的情况
        console.error('Error:', error);
        const message = error.response?.data?.message || "注册失败";
        ElMessage.error(message);
      });
    }
  });
};
</script>

<template>
  <div>
    <div class="login_design">
      <div style="width: 350px" class="login_box">
        <div style="font-weight: bold;font-size: 26px;text-align: center;padding: 10px;margin-bottom: 10px">Register</div>
        <el-form ref="ruleFormRef" :rules="rules" :model="data.form" label-width="auto">
          <!-- 账号输入框 -->
          <el-form-item prop="username">
            <el-input prefix-icon="Avatar" v-model="data.form.username" placeholder="输入学号"/>
          </el-form-item>
          <!-- 密码输入框 -->
          <el-form-item prop="password">
            <el-input show-password prefix-icon="Unlock" v-model="data.form.password" placeholder="输入密码"
                      @keydown.enter="login"/>
          </el-form-item>
          <!-- 个人姓名-->
          <el-form-item prop="username">
            <el-input prefix-icon="Avatar" v-model="data.form.name" placeholder="输入姓名"/>
          </el-form-item>
          <!-- 登录按钮 -->
          <el-button type="info" style="width: 100%;margin-top: 15px" @click="register">注册</el-button>
        </el-form>
        <div style="margin-top: 20px;font-size: 14px;text-align: center">
          If Have a Account, Please <a href="/login" style="color: #f17660">登录</a>
        </div>
        <div style="margin-top: 20px;font-size: 13px;text-align: center;color: #f5d2cc">
          注意！只能注册学生账号!
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 可在这里添加样式 */
.login_design {
  min-height: 100vh;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5d2cc;
}

.login_box {
  background-color: #87536a;
  box-shadow: 0 0 50px rgb(105, 61, 83);
  padding: 40px;
  border-radius: 20px;
}

.el-form-item__label {
  color: #f5d2cc;
}


/* 修改验证错误消息的颜色 */
::v-deep(.el-form-item__error) {
  color: #d07264;
  font-size: 12px;
}
</style>