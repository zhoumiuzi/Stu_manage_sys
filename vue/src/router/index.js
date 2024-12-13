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
        { path: 'management/students', name: 'StudentManagement', component: () => import('@/views/manager/Student.vue') },
        { path: 'management/teachers', name: 'TeacherManagement', component: () => import('@/views/manager/Teacher.vue') },
        { path: 'management/courses', name: 'CourseManagement', component: () => import('@/views/manager/Course.vue') },
        { path: 'management/students/courseselect', name: 'CourseSelect', component: () => import('@/views/student/CourseSelect.vue') },
        { path: 'management/students/courselist', name: 'StudentCourse', component: () => import('@/views/student/CourseList.vue') },

        // 成绩录入
        { path: 'management/courselist', name: 'AdminCourseList', component: () => import('@/views/manager/CourseList.vue') },
        { path: 'management/courselogin', name: 'AdminCourseStudents', component: () => import('@/views/manager/CourseLogin.vue') },

        // 数据统计
        { path: 'statistics/teachers', name: 'TeacherStatistics', component: () => import('@/views/manager/TeacherStatistics.vue') },
        { path: 'statistics/students', name: 'StudentStatistics', component: () => import('@/views/manager/StudentStatistics.vue') },

        // 个人信息
        { path: 'management/adminprofile', name: 'AdminprofileProfile', component: () => import('@/views/manager/ManagerInfo.vue') },
        { path: 'management/studentprofile', name: 'Studentprofile', component: () => import('@/views/student/StudentInfo.vue') },
      ]
    },
    {
      // 登录页面
      path: '/login',
      name: 'Login',
      component: () => import('@/views/login.vue'),
    },
    {
      //注册页面
      path: '/register',
      name: 'Register',
      component:()=>import('@/views/register.vue')
    },
    {
      // 捕获所有未定义路由，跳转到首页或显示404
      path: '/:pathMatch(.*)*',
      redirect: '/home',
    }
  ]
})

export default router
