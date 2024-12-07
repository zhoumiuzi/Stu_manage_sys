import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Manager',
      component: () => import('@/views/Manager.vue'),
      redirect: '/home',
      children: [
        // 管理首页
        { path: 'home', name: 'Home', component: () => import('@/views/manager/Home.vue') },

        // 信息管理
        { path: 'management/students', name: 'StudentManagement', component: () => import('@/views/manager/management/StudentManagement.vue') },
        { path: 'management/teachers', name: 'TeacherManagement', component: () => import('@/views/manager/management/TeacherManagement.vue') },
        { path: 'management/courses', name: 'CourseManagement', component: () => import('@/views/manager/Course.vue') },

        // 成绩录入
        { path: 'grades', name: 'GradeEntry', component: () => import('@/views/manager/GradeEntry.vue') },

        // 数据统计
        { path: 'statistics/teachers', name: 'TeacherStatistics', component: () => import('@/views/manager/statistics/TeacherStatistics.vue') },
        { path: 'statistics/students', name: 'StudentStatistics', component: () => import('@/views/manager/statistics/StudentStatistics.vue') },

        // 个人信息
        { path: 'profile', name: 'Profile', component: () => import('@/views/manager/Profile.vue') },
      ]
    },
    {
      // 登录页面
      path: '/login',
      name: 'Login',
      component: () => import('@/views/login.vue'),
    },
    {
      // 捕获所有未定义路由，跳转到首页或显示404
      path: '/:pathMatch(.*)*',
      redirect: '/home',
    }
  ]
})

export default router
