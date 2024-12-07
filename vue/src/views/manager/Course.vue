<template>
  <div>
    <div class="card">
      <span>请输入关键字来查询课程：</span>
      <el-input
          v-model="data.name_num"
          style="width: 240px"
          placeholder="输入课程名称或课程号"
          :prefix-icon="Search"
      />
      <el-button type="primary" style="margin: 0 10px 0 20px">查询</el-button>
      <el-button type="info" style="margin: 0 10px 0 10px">重置</el-button>
    </div>

    <div class="card">
      <div>
        <el-button type="primary" style="margin: 0 10px 15px 5px">新增课程</el-button>
      </div>
      <div class="table-wrapper">
        <el-table :data="data.tableData" stripe :row-key="row => row.id">
          <el-table-column prop="course_name" label="课程名称"/>
          <el-table-column prop="course_num" label="课程号"/>
          <el-table-column prop="course_type" label="课程类型"/>
          <el-table-column prop="course_credit" label="学分"/>
          <el-table-column prop="teacher_name" label="授课教师"/>
          <el-table-column prop="course_desc" label="课程介绍"/>
          <el-table-column fixed="right" label="操作" min-width="120">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleClick">
                编辑
              </el-button>
              <el-button type="danger" size="small">删除</el-button>
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
import {reactive} from "vue";
import request from "@/utils/request";

const data = reactive({
  // 课程名字或者课程号
  name_num: "",
  tableData: [],
  total: 0,
  pagenum: 1,
  pagesize: 10,
});

const load = () => {
  request.get('/management/course/selectPage', {
    params: {
      pagenum: data.pagenum,
      pagesize: data.pagesize,
    }
  }).then(res => {//存在或者空,加问号，有值就取，没值就不取
    data.tableData = (res.data?.list || []).map(item => ({
      // 从后端直接来的字段
      course_name: item.coursename, // 对应后端的 coursename
      course_num: item.coursenum, // 对应后端的 coursenum
      course_type: item.coursetype, // 对应后端的 coursetype
      course_credit: item.coursecredits, // 对应后端的 coursecredits
      teacher_name: item.teachername || '暂无教师', // 对应后端的 teachername
      course_desc: item.coursedesc || '暂无描述' // 对应后端的 coursedesc

    }));
    data.total = res.data?.total || 0
  })
}
load()

const handleCurrentChange = (pagenum) => {
  //data.pagenum = pagenum;
  load();
  console.log(pagenum)
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

.el-table__row:nth-child(odd) {
  background-color: rgb(240, 210, 208); /* 斑马纹第一种颜色 */
}

.el-table .el-table__row:nth-child(even) {
  background-color: rgb(227, 195, 193); /* 斑马纹第二种颜色 */
}

</style>
