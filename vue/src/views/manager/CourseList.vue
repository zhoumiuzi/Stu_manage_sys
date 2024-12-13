<template>
  <div>
    <div class="card">
      <span>请输入关键字来查询课程：</span>
      <el-input v-model="data.coursename" style="width: 150px" placeholder="输入课程名称" :prefix-icon="Search"
                @keydown.enter="load"/>
      <el-input v-model="data.coursenum" style="width: 150px" placeholder="输入课程号" :prefix-icon="Search"
                @keydown.enter="load"/>
      <el-button type="primary" style="margin: 0 10px 0 20px" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px 0 10px" @click="reload">重置</el-button>
    </div>

    <div class="card">
      <div class="table-wrapper">
        <el-table :data="data.tableData" stripe>
          <el-table-column prop="coursename" label="课程名称"/>
          <el-table-column prop="coursenum" label="课程号"/>
          <el-table-column prop="coursetype" label="课程类型"/>
          <el-table-column prop="coursecredits" label="学分"/>
          <el-table-column fixed="right" label="操作" min-width="120">
            <template #default="scope">
              <el-button type="primary" @click="goToCourseStudents(scope.row)">查看学生</el-button>
            </template>
          </el-table-column>
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

const data = reactive({
  coursename: "",
  coursenum: "",
  username: "",
  tableData: [],
  total: 0,
  pagenum: 1,
  pagesize: 18
});

const load = () => {
  request.get('/courseselect/CourseList', {
    params: {
      pagenum: data.pagenum,
      pagesize: data.pagesize,
      coursename: data.coursename,
      coursenum: data.coursenum,
      username: data.username,
    }
  }).then(res => {
    const list = res.data?.list || [];
    data.tableData = list;
    data.total = res.data?.total || 0;
  }).catch(err => {
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
  load();
}

const goToCourseStudents = (row) => {
  router.push({ name: 'AdminCourseStudents', query: { courseid: row.courseid }});
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
