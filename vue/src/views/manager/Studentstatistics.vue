<template>
  <div>
    <div class="card">
      <span>请输入关键字来查询学生：</span>
      <el-input v-model="data.username" style="width: 150px" placeholder="输入学生学号" :prefix-icon="Search" @keydown.enter="load"/>
      <el-button type="primary" style="margin: 0 10px 0 20px" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px 0 10px" @click="reload">重置</el-button>
    </div>

    <div class="card">
      <div>
        <el-select v-model="data.coursetype" placeholder="选择课程类型" style="width: 150px;margin-bottom: 30px" @change="reloadd">
          <el-option label="全部" value="%"></el-option>
          <el-option label="专业必修" value="专业必修"></el-option>
          <el-option label="专业选修" value="专业选修"></el-option>
          <el-option label="通识必修" value="通识必修"></el-option>
          <el-option label="通识选修" value="通识选修"></el-option>
        </el-select>
      </div>
      <div class="table-wrapper">
        <el-table :data="data.tableData" stripe :row-key="row => row.studentid">
          <el-table-column prop="studentname" label="学号"/>
          <el-table-column prop="name" label="学生姓名"/>
          <el-table-column prop="totalCredits" label="总学分"/>
          <el-table-column prop="completedCredits" label="已完成学分"/>
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
});

const handleCurrentChange = (pagenum) => {
  data.pagenum = pagenum;
  load();
}

const reload = () => {
  data.username = ''; // 重置查询条件
  load();
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
</style>
