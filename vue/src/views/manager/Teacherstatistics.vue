<template>
  <div>

    <!-- 新增：不同职称的教师统计 -->
    <div class="card">
      <h3>不同职称的教师统计</h3>
      <el-table :data="data.titleStats" stripe>
        <el-table-column prop="title" label="职称" />
        <el-table-column prop="count" label="教师数量" />
        <el-table-column prop="avgSalary" label="平均工资" :formatter="formatCurrency" />
      </el-table>
    </div>

    <!-- 新增：每个教师所教每门课程的成绩统计 -->
    <div class="card">
      <h3>每个教师所教课程的成绩统计</h3>
      <el-table :data="data.courseStats" stripe>
        <el-table-column prop="teacherid" label="教师ID" />
        <el-table-column prop="username" label="教师用户名" />
        <el-table-column prop="courseid" label="课程ID" />
        <el-table-column prop="coursename" label="课程名称" />
        <el-table-column prop="avgGrade" label="平均成绩" />
        <el-table-column prop="maxGrade" label="最高分" />
        <el-table-column prop="minGrade" label="最低分" />
      </el-table>
    </div>
  </div>
</template>


<script setup>
import { Search } from "@element-plus/icons-vue";
import { reactive, onMounted } from "vue";
import request from "@/utils/request";
import { ElMessage } from "element-plus";

const data = reactive({
  username: "", // 查询学生学号
  tableData: [], // 存储学生数据
  coursetype: "",
  total: 0, // 总记录数
  pagenum: 1, // 当前页码
  pagesize: 18, // 每页显示数量

  // 新增：统计数据
  titleStats: [], // 不同职称的教师统计
  courseStats: [] // 每个教师每门课程的成绩统计
});

const load = () => {
  request.get('/courseselect/studentCredits', {
    params: {
      pagenum: data.pagenum,
      pagesize: data.pagesize,
      coursetype: data.coursetype,
      username: data.username,
    }
  }).then(res => {
    const list = res.data?.list || [];
    data.tableData = list.map(item => ({
      studentname: item.username,  // 学号
      name: item.name,             // 学生姓名
      totalCredits: item.totalCredits,  // 总学分
      completedCredits: item.completedCredits // 已完成学分
    }));
    data.total = res.data?.total || 0;

    console.log("Table Data", data.tableData); // 调试用，查看数据是否正确
  }).catch(err => {
    ElMessage.error('加载数据失败');
  });
}

const reloadd = () => {
  ElMessage.success("更新成功")
  load();
}

onMounted(() => {
  load();
  loadTitleStats();
  loadCourseStats();
});

const handleCurrentChange = (pagenum) => {
  data.pagenum = pagenum;
  load();
}

const reload = () => {
  data.username = ''; // 重置查询条件
  load();
}

// 新增：加载不同职称的教师统计
const loadTitleStats = () => {
  request.get('/teacher/statistics/title')
      .then(res => {
        data.titleStats = res.data;
        console.log("Title Stats", data.titleStats);
      })
      .catch(err => {
        ElMessage.error('加载职称统计数据失败');
      });
}

// 新增：加载每个教师所教每门课程的成绩统计
const loadCourseStats = () => {
  request.get('/teacher/statistics/course')
      .then(res => {
        data.courseStats = res.data;
        console.log("Course Stats", data.courseStats);
      })
      .catch(err => {
        ElMessage.error('加载课程成绩统计数据失败');
      });
}

//转rmb
const formatCurrency = (row, column, cellValue) => {
  return `¥${parseFloat(cellValue).toFixed(2)}`;
}
</script>


<style scoped>
.el-table {
  border-radius: 5px;
  background-color: rgb(227, 195, 193); /* 设置背景颜色与 card 一致 */
}

.card {
  padding: 30px;
  margin: 20px;
  background-color: rgb(227, 195, 193);
  box-shadow: 5px 5px 10px 0 rgb(105, 61, 83);
}

.el-table-column {
  background-color: #eaaac5;
}

.pagination-wrapper {
  display: flex;
  justify-content: center; /* 居中对齐 */
  padding: 20px 0;
}

.table-wrapper {
  margin-bottom: 20px;
}

.el-input {
  display: inline-block;
  margin-right: 10px;
  margin-left: 5px;
}

.el-select {
  margin-right: 10px;
  margin-left: 5px;
}

/* 新增：统计卡片的标题样式 */
.card h3 {
  margin-bottom: 20px;
  color: #333;
}
</style>

