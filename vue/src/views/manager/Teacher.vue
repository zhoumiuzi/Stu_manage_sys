<template>
  <div>
    <!-- 查询框 -->
    <div class="card">
      <span>请输入关键字来查询教师：</span>
      <el-input v-model="data.query.username" placeholder="输入用户名" style="width: 150px" />
      <el-button type="primary" @click="load" style="margin: 0 10px 0 20px">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <!-- 教师表格 -->
    <div class="card">
      <div>
        <el-button type="primary" style="margin: 0 10px 15px 5px" @click="showAddForm">新增教师</el-button>
      </div>
      <div class="table-wrapper">
        <el-table :data="data.teachers" stripe>
          <el-table-column prop="username" label="用户名" />
          <el-table-column prop="title" label="职称" />
          <el-table-column prop="salary" label="薪资" />
          <el-table-column fixed="right" label="操作" width="300">
            <template #default="scope">
              <el-button type="primary" style="margin-right: 10px" @click="showEditForm(scope.row)">编辑</el-button>
              <el-button type="danger" @click="confirmDelete(scope.row.teacherid)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-wrapper">
          <el-pagination
              background
              layout="prev, pager, next"
              :total="data.total"
              :page-size="data.pagesize"
              :current-page="data.pagenum"
              @current-change="handleCurrentChange"
          ></el-pagination>
        </div>
      </div>
    </div>

    <!-- 教师信息表单 -->
    <el-dialog v-model="data.formVisible" title="教师信息" width="500px" align-center>
      <el-form :model="data.form" style="margin-right: 30px;margin-left: 30px">
        <el-form-item label="用户名">
          <el-input v-model="data.form.username" />
        </el-form-item>
        <el-form-item label="职称">
          <el-select v-model="data.form.title" placeholder="请选择职称" style="margin-left: 30px">
            <el-option label="助教" value="助教"></el-option>
            <el-option label="讲师" value="讲师"></el-option>
            <el-option label="副教授" value="副教授"></el-option>
            <el-option label="教授" value="教授"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="薪资">
          <el-input type="number" v-model="data.form.salary" style="margin-left: 30px" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ElMessageBox, ElMessage } from "element-plus";
import request from "@/utils/request";

export default {
  data() {
    return {
      data: {
        teachers: [],
        form: { teacherid: null, username: "", title: "", salary: null },
        formVisible: false,
        query: { username: "" },
        total: 0, // 总记录数
        pagenum: 1, // 当前页码
        pagesize: 18, // 每页显示记录数
      },
    };
  },
  methods: {
    load() {
      // 加载数据
      request
          .get("/teacher/selectPage", {
            params: {
              username: this.data.query.username,
              pagenum: this.data.pagenum,
              pagesize: this.data.pagesize,
            },
          })
          .then((res) => {
            const response = res.data || {};
            this.data.teachers = response.list || [];
            this.data.total = response.total || 0;
          });
    },
    reset() {
      this.data.query.username = "";
      this.load();
    },
    showAddForm() {
      this.data.form = { teacherid: null, username: "", title: "", salary: null };
      this.data.formVisible = true;
    },
    showEditForm(teacher) {
      this.data.form = { ...teacher };
      this.data.formVisible = true;
    },
    save() {
      // 区分新增和更新操作
      const url = this.data.form.teacherid ? `/teacher/update` : `/teacher/add`;
      const method = this.data.form.teacherid ? "put" : "post";

      request({
        url,
        method,
        data: this.data.form,
      })
          .then(() => {
            const operation = this.data.form.teacherid ? "更新" : "新增";
            ElMessage.success(`${operation}成功`);
            this.data.formVisible = false;
            this.load();
          })
          .catch(() => {
            const operation = this.data.form.teacherid ? "更新" : "新增";
            ElMessage.error(`${operation}失败`);
          });
    },
    confirmDelete(teacherId) {
      ElMessageBox.confirm("此操作将永久删除该教师信息，是否继续？", "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.deleteTeacher(teacherId);
          })
          .catch(() => {
            ElMessage.info("已取消删除");
          });
    },
    deleteTeacher(teacherId) {
      request.delete(`/teacher/delete/${teacherId}`).then(() => {
        ElMessage.success("删除成功");
        this.load();
      });
    },
    handleCurrentChange(page) {
      this.data.pagenum = page;
      this.load();
    },
  },
  mounted() {
    this.load();
  },
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

.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}
</style>
