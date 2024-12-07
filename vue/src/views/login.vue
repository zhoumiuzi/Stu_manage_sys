<script setup>
import {reactive, ref} from 'vue';
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";
// 定义表单数据
const data = reactive({
  form: {}
});

// 定义表单验证规则
const rules = reactive({
  username: [
    {required: true, message: '请输入账号！', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码！', trigger: 'blur'},
  ]
});

const ruleFormRef = ref()

//validate 方法用于验证整个表单的数据是否合法
const login = () => {
  ruleFormRef.value.validate((valid) => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        console.log('Response:', res); // 调试返回值
        if (res.code === '200') {
          localStorage.setItem('student-user', JSON.stringify(res.data))
          ElMessage.success("登陆成功")
          //这样通过浏览器去刷新的，应该直接用roter比较好
          // location.href = '/home' //登录到主页
          router.push('/home')
        } else {
          ElMessage.error("登录失败")
        }
        // }).catch(err => {
        //   console.error('Request failed:', err); // 捕获异常
        //   ElMessage.error("请求失败，请稍后重试！");
      });
    }
  })
}

// 获取表单的信息


</script>


<template>
  <div>
    <div class="login_design">
      <div style="width: 350px" class="login_box">
        <div style="font-weight: bold;font-size: 26px;text-align: center;padding: 10px">Login</div>
        <el-form ref="ruleFormRef" :rules="rules" :model="data.form" label-width="auto">
          <el-form-item prop="username">
            <el-input prefix-icon="Avatar" v-model="data.form.username" placeholder="输入账号"/>
          </el-form-item>
          <el-form-item prop="password">
            <el-input show-password prefix-icon="Unlock" v-model="data.form.password" placeholder="输入密码" @keydown.enter="login"/>
          </el-form-item>
          <el-button type="info" style="width: 100%" @click="login" @keydown.enter="login">登录</el-button>
        </el-form>
        <div style="margin-top: 10px;font-size: 14px;text-align: center">
          IF No Account,Please <a href="/register" style="color: #f17660">注册</a>
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
  border-radius: 15px;
}

.login_box {
  //border: 1px solid;
  background-color: #87536a;
  box-shadow: 0 0 50px rgb(105, 61, 83);
  padding: 35px;
  border-radius: 20px;
}

/* 修改验证错误消息的颜色 */
/deep/ .el-form-item__error {
  color: #d07264;
  font-size: 12px;
}
</style>
