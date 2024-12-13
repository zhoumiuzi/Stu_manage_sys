<template>
  <div>
    <div class="card">
      <span>请输入关键字来查询课程：</span>
      <el-input v-model="data.coursename" style="width: 150px" placeholder="输入课程名称" :prefix-icon="Search"
                @keydown.enter="load"/>
      <el-input v-model="data.coursenum" style="width: 150px" placeholder="输入课程号" :prefix-icon="Search"
                @keydown.enter="load"/>
      <el-input v-model="data.username" style="width: 150px" placeholder="输入教师名称" :prefix-icon="Search"
                @keydown.enter="load"/>
      <el-button type="primary" style="margin: 0 10px 0 20px" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px 0 10px" @click="reload">重置</el-button>
    </div>

    <div class="card">
      <div class="table-wrapper">
        <el-table :data="data.tableData" stripe :row-key="row => row.courseid">
          <el-table-column prop="coursename" label="课程名称"/>
          <el-table-column prop="coursenum" label="课程号"/>
          <el-table-column prop="coursetype" label="课程类型"/>
          <el-table-column prop="coursecredits" label="学分"/>
          <el-table-column prop="teacher_name" label="授课教师"/>
          <el-table-column prop="coursedesc" label="课程介绍"/>
          <el-table-column fixed="right" label="操作" min-width="120">
            <template #default="scope">
              <el-button
                  :type="scope.row.selected ? 'success' : 'primary'"
                  @click="handleSelect(scope.row)"
              >
                {{ scope.row.selected ? '已选' : '选课' }}
              </el-button>
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
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";

const user = JSON.parse(localStorage.getItem('student-user') || '{}');

const data = reactive({
  coursename: "",
  coursenum: "",
  username: "",
  tableData: [],
  total: 0,
  pagenum: 1,
  pagesize: 18,
  formvisible: false,
  form: {
    courseid: null,
    coursename: "",
    coursenum: "",
    username: "",
    coursetype: "",
    coursecredits: 0,
    coursedesc: ""
  }
});

const load = () => {
  request.get('/management/course/selectPage', {
    params: {
      pagenum: data.pagenum,
      pagesize: data.pagesize,
      coursename: data.coursename,
      coursenum: data.coursenum,
      username: data.username,
    }
  }).then(res => {
    const list = res.data?.list || [];
    data.tableData = list.map(item => ({
      courseid: item.courseid,
      coursename: item.coursename,
      coursenum: item.coursenum,
      coursetype: item.coursetype,
      coursecredits: item.coursecredits,
      teacher_name: item.username || '暂无教师',
      coursedesc: item.coursedesc || '暂无描述',
      selected: false // 初始先设为false，稍后根据已选课程更新
    }));
    data.total = res.data?.total || 0;

    // 加载当前学生已选课程列表，以更新selected状态
    loadStudentSelectedCourses();
  })
}

const loadStudentSelectedCourses = () => {
  if (!user.studentid) return;
  request.get('/courseselect/listByStudent', { params: { studentid: user.studentid }})
      .then(res => {
        const enrolledCourses = res.data || [];
        // enrolledCourses 返回的应是当前学生已选的courseid列表
        const enrolledSet = new Set(enrolledCourses.map(item => item.courseid));
        data.tableData.forEach(row => {
          if (enrolledSet.has(row.courseid)) {
            row.selected = true;
          } else {
            row.selected = false;
          }
        })
      })
}

const handleSelect = (row) => {
  if (!user.studentid) {
    ElMessage.error("请先登录学生账号");
    return;
  }

  if (row.selected) {
    // 已选 -> 退选
    ElMessageBox.confirm('确定要退选该课程吗？', '退选确认', { type: 'warning' })
        .then(() => {
          request.delete('/courseselect/deletecourse', {
            data: {
              studentid: user.studentid,
              courseid: row.courseid
            }
          }).then(res => {
            if (res.code === '200') {
              row.selected = false;
              ElMessage.success("退选成功");
            } else {
              ElMessage.error(res.msg || "退选失败");
            }
          }).catch(err => {
            ElMessage.error(err.response?.data?.msg || "退选失败");
          })
        })
        .catch(() => {});
  } else {
    // 未选 -> 选课
    ElMessageBox.confirm('确定要选该课程吗？', '选课确认', { type: 'warning' })
        .then(() => {
          request.post('/courseselect/addcourse', {
            studentid: user.studentid,
            courseid: row.courseid,
          }).then(res => {
            if (res.code === '200') {
              row.selected = true;
              ElMessage.success("选课成功");
            } else {
              ElMessage.error(res.msg || "选课失败");
            }
          }).catch(err => {
            ElMessage.error(err.response?.data?.msg || "选课失败");
          })
        })
        .catch(() => {});
  }
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
