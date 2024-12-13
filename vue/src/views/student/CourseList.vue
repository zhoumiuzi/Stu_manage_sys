<template>
  <div>
    <div class="card">
      <span>请输入关键字来查询课程：</span>
      <el-input v-model="data.coursename" style="width: 150px" placeholder="输入课程名称" :prefix-icon="Search"
                @keydown.enter="load"/>
      <el-button type="primary" style="margin: 0 10px 0 20px" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px 0 10px" @click="reload">重置</el-button>
    </div>

    <div class="card">
      <!-- 课程类型选择框 -->
      <el-select v-model="data.coursetype" placeholder="选择课程类型" style="width: 150px" @change="load">
        <el-option label="全部" value="%"></el-option>
        <el-option label="专业必修" value="专业必修"></el-option>
        <el-option label="专业选修" value="专业选修"></el-option>
        <el-option label="通识必修" value="通识必修"></el-option>
        <el-option label="通识选修" value="通识选修"></el-option>
      </el-select>
      <div>
        <p>全部选修课程总学分：{{ data.totalElectiveCredits }}</p>
        <p>已完成选修课程总学分：{{ data.completedElectiveCredits }}</p>
      </div>

      <div class="table-wrapper">
        <el-table :data="data.tableData" stripe>
          <el-table-column prop="coursename" label="课程名称"/>
          <el-table-column prop="coursenum" label="课程号"/>
          <el-table-column prop="coursetype" label="课程类型"/>
          <el-table-column prop="coursecredits" label="学分"/>
          <el-table-column prop="teachername" label="授课教师"/>
          <el-table-column prop="grade" label="成绩"/>
          <el-table-column prop="registertime" label="成绩录入时间" />
        </el-table>
      </div>

      <div class="pagination-wrapper">
        <el-pagination background layout="prev, pager, next" :total="data.total" v-model:page-size="data.pagesize"
                       v-model:current-page="data.pagenum" @current-change="handleCurrentChange">
        </el-pagination>
      </div>
    </div>

  </div>
</template>

<script setup>
import {Search} from "@element-plus/icons-vue";
import {reactive, onMounted} from "vue";
import {useRouter} from 'vue-router'
import request from "@/utils/request";
import {ElMessage} from "element-plus";

const router = useRouter();
const student = JSON.parse(localStorage.getItem("student-user") || "{}");

const data = reactive({
  coursename: "",
  coursenum: "",
  coursetype: "",
  username: "",
  tableData: [],
  total: 0,
  pagenum: 1,
  pagesize: 18,
  totalElectiveCredits: 0,
  completedElectiveCredits: 0
});

const load = () => {
  console.log("开始加载数据...");
  request.get('/courseselect/StudentCourseList', {
    params: {
      pagenum: data.pagenum,
      pagesize: data.pagesize,
      coursename: data.coursename,
      username: data.username,
      coursetype: data.coursetype,
      studentid: student.studentid // 从本地获取的学生ID传给后端
    }
  }).then(res => {
    console.log("完整的API返回数据: ", res);  // 打印整个响应对象
    console.log("完整的API返回数据的data部分: ", res.data);  // 打印res.data

    const responseData = res || {};
    console.log("解析后的返回数据: ", responseData);  // 打印解析后的数据

    // 确保字段映射正确
    data.tableData = responseData.list || [];
    data.total = responseData.total || 0;
    data.totalElectiveCredits = responseData.totalElectiveCredits || 0;
    data.completedElectiveCredits = responseData.completedElectiveCredits || 0;

    console.log("更新后的 data:", data); // 打印更新后的 reactive 对象
  }).catch(err => {
    console.error("请求失败的错误信息: ", err);  // 打印详细错误信息
    ElMessage.error(err.response?.data?.msg || "加载失败");
  })
}


onMounted(() => {
  load();
});

const handleCurrentChange = (pagenum) => {
  data.pagenum = pagenum;
  load();
}

const reload = () => {
  data.coursenum = '';
  data.coursename = '';
  data.username = '';
  data.pagenum = 1;
  load();
}
</script>

<style scoped>
.el-table {
  border-radius: 5px;
  background-color: rgb(227, 195, 193);
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
  justify-content: center;
  padding: 20px 0;
}

.table-wrapper {
  margin-bottom: 20px;
}

.el-table__row:nth-child(odd) {
  background-color: rgb(240, 210, 208);
}

.el-table .el-table__row:nth-child(even) {
  background-color: rgb(227, 195, 193);
}

.el-input {
  display: inline-block;
  margin-right: 10px;
  margin-left: 5px;
}
</style>
