<template>
  <div>
    <div class="card">
      <div class="title">
        <h2>学生个人信息</h2>
      </div>
      <el-form :model="studentData" label-width="120px" class="form-wrapper">
        <el-form-item label="学号">
          <el-input v-model="studentData.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="studentData.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="studentData.gender" placeholder="请选择性别">
            <el-option label="男" value="M"></el-option>
            <el-option label="女" value="F"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="studentData.age" type="number" placeholder="请输入年龄"></el-input>
        </el-form-item>
        <el-form-item label="电话号码">
          <el-input v-model="studentData.phonenum" placeholder="请输入电话号码"></el-input>
        </el-form-item>
      </el-form>
      <div class="button-wrapper">
        <el-button type="primary" @click="saveChanges"> 保 存 </el-button>
        <el-button type="warning" @click="openPasswordDialog"> 修改密码 </el-button>
        <el-button type="danger" @click="logout"> 退 登 </el-button>
      </div>
      <div style="text-align:center;padding-top: 30px;color: #693d53;font-size: 13px;">
        信息不显示请退出重登！
      </div>
    </div>

    <!-- 密码修改对话框 -->
    <el-dialog
        v-model="passwordDialogVisible"
        title="修改密码"
        width="500"
        align-center
    >
      <el-form :model="passwordForm" label-width="120px">
        <el-form-item label="旧密码">
          <el-input
              v-model="passwordForm.oldPassword"
              type="password"
              placeholder="请输入旧密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input
              v-model="passwordForm.newPassword"
              type="password"
              placeholder="请输入新密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="确认新密码">
          <el-input
              v-model="passwordForm.confirmPassword"
              type="password"
              placeholder="请确认新密码"
          ></el-input>
        </el-form-item>
      </el-form>
      <div class="dialog-footer">
        <el-button @click="passwordDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="updatePassword">保 存</el-button>
      </div>
    </el-dialog>

  </div>

</template>


<script setup>
import { reactive, ref, onMounted } from "vue";
import { ElMessage } from "element-plus";
import request from "@/utils/request";

const user = JSON.parse(localStorage.getItem("student-user") || "{}");

// 学生数据
const studentData = reactive({
  studentid: "未设置",
  username: "未设置",
  name: "",
  gender: "F",
  age: 18,
  phonenum: "",
});

// 密码修改弹窗相关数据
const passwordDialogVisible = ref(false); // 控制密码修改弹窗显示状态
const passwordForm = reactive({
  oldPassword: "",
  newPassword: "",
  confirmPassword: "",
});

// 获取学生信息
const loadStudentInfo = () => {
  request
      .get("/student/showStudentInfo", {
        params: { username: user.username },
      })
      .then((res) => {
        if (res.code === "200") {
          Object.assign(studentData, res.data);
        } else {
          throw new Error(res.msg || "未知错误");
        }
      })
      .catch((err) => {
        ElMessage.error("加载失败：" + err.message || "请求失败");
      });
};

// 保存修改后的学生信息
const saveChanges = () => {
  request
      .post("/student/updateStudentInfo", studentData)
      .then((res) => {
        if (res.code === "200") {
          ElMessage.success("信息保存成功");
        } else {
          ElMessage.error("信息保存失败：" + res.message);
        }
      })
      .catch((err) => {
        ElMessage.error("请求失败：" + err.message || "保存失败");
      });
};

// 打开密码修改对话框
const openPasswordDialog = () => {
  passwordDialogVisible.value = true;
};

// 更新密码
const updatePassword = () => {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.error("两次密码输入不一致");
    return;
  }

  request
      .post("/student/updatePassword", {
        username: studentData.username,
        oldPassword: passwordForm.oldPassword,
        newPassword: passwordForm.newPassword,
      })
      .then((res) => {
        if (res.code === "200") {
          ElMessage.success("密码修改成功");
          passwordDialogVisible.value = false; // 关闭弹窗
          passwordForm.oldPassword = "";
          passwordForm.newPassword = "";
          passwordForm.confirmPassword = "";
        } else {
          ElMessage.error("密码修改失败：" + res.message);
        }
      })
      .catch((err) => {
        ElMessage.error("请求失败：" + err.message || "密码修改失败");
      });
};

// 用户登出
const logout = () => {
  localStorage.removeItem("student-user");
  window.location.href = "/login";
};

// 页面加载时获取学生信息
onMounted(() => {
  loadStudentInfo();
});
</script>


<style scoped>
.card {
  padding: 50px 200px 50px 150px;
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

.dialog-footer {
  text-align: right;
}

.el-dialog {
  z-index: 2000 !important;
}
</style>
