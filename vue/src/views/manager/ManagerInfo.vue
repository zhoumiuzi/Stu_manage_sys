<template>
  <div>
    <div class="card">
      <div class="title">
        <h2>管理员个人信息</h2>
      </div>
      <el-form :model="adminData" label-width="120px" class="form-wrapper">
        <el-form-item label="管理员ID">
          <el-input v-model="adminData.adminid" disabled></el-input>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="adminData.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="adminData.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="adminData.password" show-password type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="身份">
          <el-input v-model="adminData.role" disabled></el-input>
        </el-form-item>
      </el-form>
      <div class="button-wrapper">
        <el-button type="primary" @click="saveChanges"> 保 存 </el-button>
        <el-button type="danger" @click="logout"> 退 登 </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";
import request from "@/utils/request";

//需要使用到本地存储啊啊啊啊啊啊啊啊啊啊
const user = JSON.parse(localStorage.getItem('student-user') || '{}')

const adminData = reactive({
  adminid: "未设置",
  username: "未设置",
  name: "未设置",
  password: "",
  role: "Admin",
});

// 获取管理员信息
const loadAdminInfo = () => {
  request
      .get("/management/admin/showinfo", {
        params: { username: user.username },
      })
      .then((res) => {
        if (res.code === "200") {
          Object.assign(adminData, res.data);
        } else {
          throw new Error(res.msg || "未知错误");
        }
      })
      .catch((err) => {
        ElMessage.error("加载失败：" + err.message || "请求失败");
      });
};



// 保存修改后的信息
const saveChanges = () => {
  request.post("/management/admin/update", adminData).then((res) => {
    if (res.code === "200") {
      ElMessage.success("信息保存成功");
    } else {
      ElMessage.error("信息保存失败：" + res.message);
    }
  });
};

const logout = () => {
  localStorage.removeItem('student-user')
  window.location.href = '/login'
}

onMounted(() => {
  loadAdminInfo(); // 页面加载时获取管理员信息
});
</script>

<style scoped>
.card {
  padding: 30px;
  margin: 20px;
  background-color: rgb(227, 195, 193);
  box-shadow: 5px 5px 10px 0 rgb(105, 61, 83);
  border-radius: 10px;
}

.title h2 {
  text-align: center;
  color: rgb(105, 61, 83);
  margin-bottom: 10px;
}

.form-wrapper {
  margin-bottom: 20px;
}

.button-wrapper {
  display: flex;
  justify-content: center;
}

.el-input {
  background-color: rgb(240, 210, 208);
  border-radius: 5px;
}

.el-button {
  margin-top: 20px;
}
</style>
