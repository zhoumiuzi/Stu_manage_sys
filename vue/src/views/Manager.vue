<template>
  <div>
    <div style="height: 60px; background-color: #fff; display: flex; align-items: center; border-bottom: 1px solid #ddd">
      <div style="flex: 1">
        <div style="padding-left: 20px; display: flex; align-items: center">
          <img src="@/assets/imgs/logo.png" alt="" style="width: 40px">
          <div style="font-weight: bold; font-size: 24px; margin-left: 5px">学生成绩管理系统</div>
        </div>
      </div>
      <div style="width: fit-content; padding-right: 10px; display: flex; align-items: center;">
        <img src="@/assets/imgs/admin.png" alt="" style="width: 40px; height: 40px">
        <span style="margin-left: 5px">{{ user.username }}</span>
      </div>
    </div>

    <div style="display: flex">
      <div style="width: 220px; border-right: 1px solid #f5d2cc; min-height: calc(100vh - 60px)">
        <el-menu
            router
            style="border: none"
            :default-active="$route.path"
            :default-openeds="['/management', '/statistics']"
        >
          <!-- 管理首页 -->
          <el-menu-item index="/home">
            <el-icon><HomeFilled /></el-icon>
            <span>管理首页</span>
          </el-menu-item>

          <!-- 信息管理板块 -->
          <el-sub-menu index="/management">
            <template #title>
              <el-icon><Management /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/management/students" v-if="user.role ==='Admin'">
              <el-icon><UserFilled /></el-icon>
              <span>学生管理</span>
            </el-menu-item>
            <el-menu-item index="/management/teachers" v-if="user.role ==='Admin'">
              <el-icon><Avatar /></el-icon>
              <span>教师管理</span>
            </el-menu-item>
            <el-menu-item index="/management/courses" v-if="user.role ==='Admin'">
              <el-icon><Menu /></el-icon>
              <span>课程管理</span>
            </el-menu-item>
            <el-menu-item index="/management/students/courseselect" v-if="user.role ==='Student'">
              <el-icon><Grid /></el-icon>
              <span>学生选课</span>
            </el-menu-item>
            <el-menu-item index="/management/students/courselist" v-if="user.role ==='Student'">
              <el-icon><InfoFilled /></el-icon>
              <span>课程情况</span>
            </el-menu-item>
          </el-sub-menu>

          <!-- 成绩录入板块 -->
          <el-menu-item index="/management/courselist" v-if="user.role ==='Admin'">
            <el-icon><Checked /></el-icon>
            <span>成绩录入</span>
          </el-menu-item>

          <!-- 数据统计板块 -->
          <el-sub-menu index="/statistics">
            <template #title>
              <el-icon><PieChart /></el-icon>
              <span>统计分析</span>
            </template>
            <el-menu-item index="/statistics/teachers" v-if="user.role ==='Admin'">
              <el-icon><TrendCharts /></el-icon>
              <span>教师统计</span>
            </el-menu-item>
            <el-menu-item index="/statistics/students" v-if="user.role ==='Admin'">
              <el-icon><Histogram /></el-icon>
              <span>学生统计</span>
            </el-menu-item>
            <!-- 数据统计板块
            <el-menu-item index="/statistics/Detailes" >
              <el-icon><HelpFilled /></el-icon>
              <span>学分统计</span>
            </el-menu-item>-->
          </el-sub-menu>

          <!-- 个人信息板块 -->
          <el-menu-item index="management/adminprofile" v-if="user.role ==='Admin'">
            <el-icon><User /></el-icon>
            <span>个人信息</span>
          </el-menu-item>
          <el-menu-item index="management/studentprofile" v-if="user.role ==='Student'">
            <el-icon><User /></el-icon>
            <span>个人信息</span>
          </el-menu-item>

          <!-- 退出系统 -->
          <el-menu-item index="logout" @click="logout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出系统</span>
          </el-menu-item>
        </el-menu>
      </div>

      <div style="flex: 1; width: 0; background-color: #f5d2cc; padding: 10px">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
const $route = useRoute()

const logout = () => {
  localStorage.removeItem('student-user')
  window.location.href = '/login'
}

const user = JSON.parse(localStorage.getItem('student-user') || '{}')

</script>

<style scoped>
.el-menu-item.is-active {
  background-color: rgba(194, 146, 164, 0.63) !important;
  box-shadow: 0 1px 5px rgba(241, 151, 188, 0.63);
}
.el-menu-item:hover {
  color: #5dbae5;
}
</style>
