<template>
  <div>
    <div class="card">
      <span>当前课程ID: {{ courseid }}</span>
      <el-button type="primary" style="margin-left:20px" @click="reflesh">刷新</el-button>
    </div>
    <div class="card">
      <div class="table-wrapper">
        <el-table :data="data.tableData" stripe>
          <el-table-column prop="studentid" label="学生ID" />
          <el-table-column prop="studentname" label="学生学号" />
          <el-table-column prop="name" label="学生姓名" />
          <el-table-column prop="grade" label="成绩" />
          <el-table-column prop="registertime" label="录入时间" width="180">
            <template #default="scope">
              <span v-if="scope.row.registertime">{{ formatDate(scope.row.registertime) }}</span>
              <span v-else>未登记</span>
            </template>
          </el-table-column>

          <el-table-column fixed="right" label="操作" min-width="120">
            <template #default="scope">
              <el-button
                  :type="scope.row.grade ? 'success' : 'primary'"
                  @click="openGradeDialog(scope.row)"
              >
                {{ scope.row.grade ? '修改成绩' : '成绩录入' }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <el-dialog title="成绩录入" v-model="gradeDialog.visible" width="300px" align-center>
      <div>
        <el-form :model="gradeDialog.form">
          <el-form-item label="成绩">
            <el-input v-model="gradeDialog.form.grade" type="number" />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <el-button @click="gradeDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="submitGrade">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>


<script setup>
import { onMounted, reactive } from "vue";
import { useRoute } from "vue-router"; // 确保引入 useRoute
import request from "@/utils/request";
import { ElMessage } from "element-plus";

// 从路由中获取参数
const route = useRoute();
const courseid = parseInt(route.query.courseid, 10);

const manager = JSON.parse(localStorage.getItem("student-user") || "{}");

const data = reactive({
  tableData: [],
});

const gradeDialog = reactive({
  visible: false,
  form: {
    enrollmentid: null,
    grade: null,
  },
});

const load = () => {
  request
      .get("/courseselect/courseStudents", { params: { courseid } })
      .then((res) => {
        data.tableData = res.data || [];
      })
      .catch((err) => {
        ElMessage.error(err.response?.data?.msg || "加载失败");
      });
};

onMounted(() => {
  load();
});

const reflesh = () => {
  load();
  ElMessage.success("刷新成功");
};

const openGradeDialog = (row) => {
  console.log("Opening grade dialog with row:", row); // 调试信息
  gradeDialog.form.enrollmentid = row.enrollmentid;
  gradeDialog.form.grade = row.grade || null;
  console.log("Before setting visible:", gradeDialog.visible);
  gradeDialog.visible = true;
  console.log("After setting visible:", gradeDialog.visible);
};

const submitGrade = () => {
  if (!gradeDialog.form.grade && gradeDialog.form.grade !== 0) {
    ElMessage.error("请输入成绩");
    return;
  }
  console.log("scoreregisterid:", manager.adminid);

  request
      .post("/courseselect/updateGrade", null, {
        params: {
          enrollmentid: gradeDialog.form.enrollmentid,
          grade: gradeDialog.form.grade,
          scoreregisterid: manager.adminid,
        },
      })
      .then((res) => {
        ElMessage.success("登记成功");
        gradeDialog.visible = false;

        // 更新表格中的成绩信息
        const updatedRow = data.tableData.find(
            (row) => row.enrollmentid === gradeDialog.form.enrollmentid
        );
        if (updatedRow) {
          updatedRow.grade = gradeDialog.form.grade;
          updatedRow.registertime = new Date().toISOString(); // 设置当前时间为注册时间
        }

        load();
      })
      .catch((err) => {
        ElMessage.error(err.response?.data?.msg || "登记失败");
      });
};

const formatDate = (dt) => {
  if (!dt) return "";
  const date = new Date(dt);
  const y = date.getFullYear();
  const m = (date.getMonth() + 1).toString().padStart(2, "0");
  const d = date.getDate().toString().padStart(2, "0");
  const hh = date.getHours().toString().padStart(2, "0");
  const mm = date.getMinutes().toString().padStart(2, "0");
  const ss = date.getSeconds().toString().padStart(2, "0");
  return `${y}-${m}-${d} ${hh}:${mm}:${ss}`;
};



</script>


<style scoped>
.card {
  padding: 30px;
  margin: 20px;
  background-color: rgb(227, 195, 193);
  box-shadow: 5px 5px 10px 0 rgb(105, 61, 83);
}

.table-wrapper {
  margin-bottom: 20px;
}

.el-table {
  border-radius: 5px;
  background-color: rgb(227, 195, 193);
}

.el-table-column {
  background-color: #eaaac5;
}

.el-table__row:nth-child(odd) {
  background-color: rgb(240, 210, 208);
}

.el-table .el-table__row:nth-child(even) {
  background-color: rgb(227, 195, 193);
}

</style>
