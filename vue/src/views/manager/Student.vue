<template>
  <div>
    <div class="card">
      <span>请输入关键字来查询学生：</span>
      <el-input v-model="data.username" style="width: 150px" placeholder="输入学生姓名" :prefix-icon="Search"
                @keydown.enter="load"/>
      <el-input v-model="data.name" style="width: 150px" placeholder="输入学号" :prefix-icon="Search"
                @keydown.enter="load"/>
      <el-button type="primary" style="margin: 0 10px 0 20px" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px 0 10px" @click="reload">重置</el-button>
    </div>

    <div class="card">
      <div>
        <el-button type="primary" style="margin: 0 10px 15px 5px" @click="addFormShow">新增学生</el-button>
      </div>
      <div class="table-wrapper">
        <el-table :data="data.tableData" stripe :row-key="row => row.studentid">
          <el-table-column prop="username" label="学号" sortable/>
          <el-table-column prop="name" label="学生姓名"/>
          <el-table-column prop="gender" label="性别"/>
          <el-table-column prop="age" label="年龄"/>
          <el-table-column prop="course" label="选修课程"/>
          <el-table-column prop="email" label="邮箱"/>
          <el-table-column prop="phonenum" label="电话"/>

          <el-table-column fixed="right" label="操作" min-width="120">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleClick(scope.row)">
                编辑
              </el-button>
              <el-button type="danger" size="small" @click="deleteManage(scope.row.student_id)">删除</el-button>
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

    <el-dialog
        v-model="data.formvisible"
        title="请填写学生信息"
        width="500px"
        align-center
    >
      <el-form :model="data.form" label-position="left" label-width="120px">
        <el-form-item label="学生姓名">
          <el-input v-model="data.form.name"></el-input>
        </el-form-item>
        <el-form-item label="学号">
          <el-input v-model="data.form.username"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="data.form.stu_class"></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="data.form.stu_major"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="data.form.stu_email"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formvisible = false">取消</el-button>
          <el-button type="primary" @click="dataAddSave">保存</el-button>
        </div>
      </template>

    </el-dialog>
  </div>
</template>

<script setup>
import {Search} from "@element-plus/icons-vue";
import {reactive} from "vue";
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  name: "",
  username: "",
  tableData: [],
  total: 0,
  pagenum: 1,
  pagesize: 18,
  formvisible: false,
  form: {
    username: "",
    name: "",
    gender: "",
    age: "",
    password: "",
    phonenum: "",
  }
});

const load = () => {
  request.get('/management/admin/studentPage', {
    params: {
      pagenum: data.pagenum,
      pagesize: data.pagesize,
      name: data.name,
      username: data.username,
    }
  }).then((res) => {
    data.tableData = (res.data?.list || []).map(item => ({
      username: item.username,
      name: item.name,
      gender: item.gender,
      age: item.age,
      phonenum: item.phonenum,
      password: item.password,
    }));
    data.total = res.data?.total || 0;
  })

};
load();

const dataAddSave = () => {

};

const resetPassword=(row)=>{

}

const handleClick = (row) => {
  data.form = {...row};
  data.formvisible = true;
};

const addFormShow = () => {
  data.form = {
    stu_id: null,
    stu_name: "",
    stu_class: "",
    stu_major: "",
    stu_email: ""
  };
  data.formvisible = true;
};

const deleteManage = (student_id) => {
  ElMessageBox.confirm('删除后无法恢复，确认删除吗？', '删除确认', {type: 'warning'}).then(() => {
    request.delete('/management/student/delete/' + student_id).then(res => {
      if (res.code === "200") {
        load();
        ElMessage.success("删除成功");
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(() => {
  });
};

const handleCurrentChange = (pagenum) => {
  data.pagenum = pagenum;
  load();
};

const reload = () => {
  data.name = "";
  data.username = "";
  data.stu_class = "";
  load();
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

.el-input {
  margin-left: 15px;
}

</style>
