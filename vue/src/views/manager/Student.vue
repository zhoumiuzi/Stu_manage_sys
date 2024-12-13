<template>
  <div>
    <div class="card">
      <span>请输入关键字来查询学生：</span>
      <el-input v-model="data.username" style="width: 150px" placeholder="输入学生学号" :prefix-icon="Search"
                @keydown.enter="load"/>
      <el-input v-model="data.name" style="width: 150px" placeholder="输入姓名" :prefix-icon="Search"
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
          <el-table-column prop="phonenum" label="电话"/>

          <el-table-column fixed="right" label="操作" min-width="120">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleClick(scope.row)">
                编辑
              </el-button>
              <el-button type="danger" size="small" @click="deleteManage(scope.row.studentid)">删除</el-button>
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
        align-center>
      <el-form :model="data.form" label-position="left" label-width="120px">
        <el-form-item label="学生姓名">
          <el-input v-model="data.form.name"></el-input>
        </el-form-item>
        <el-form-item label="学号"
                      :rules="[{ required: true, message: '请输入学号' }, { validator: validateUsername, trigger: 'blur' }]">
          <el-input v-model="data.form.username" :disabled="!data.isNew"></el-input>
        </el-form-item>

        <el-form-item label="性别">
          <el-select v-model="data.form.gender" placeholder="请选择性别" style="width: 100%;margin-left: 15px;">
            <el-option label="男" value="M"></el-option>
            <el-option label="女" value="F"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="data.form.age"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="data.form.phonenum"></el-input>
        </el-form-item>

        <el-form-item label="密码">
          <template v-if="data.isNew">
            <!-- 新增模式，直接输入密码 -->
            <el-input v-model="data.form.password" show-password placeholder="请输入密码" style="width: 100%;" />
          </template>
          <template v-else>
            <!-- 编辑模式，禁止直接修改密码，提供查看密码和修改密码按钮 -->
            <el-input
                v-model="data.form.password"
                :type="data.showPassword ? 'text' : 'password'"
                placeholder="点击修改密码"
                style="width: 100%;"
                disabled>
              <template #append>
                <!-- 查看密码按钮 -->
                <el-button
                    @click="togglePasswordVisibility" type="primary" style="color: #596986;">看密码
                </el-button>
                <!-- 修改密码按钮 -->
                <el-button
                    type="primary" style="color: #693d53;" @click="openChangePasswordDialog">
                  修改密码
                </el-button>
              </template>
            </el-input>
          </template>
        </el-form-item>



      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formvisible = false">取消</el-button>
          <el-button type="primary" @click="dataAddSave">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
        v-model="data.passwordDialogVisible"
        title="修改密码"
        width="400px"
        align-center>
      <el-form :model="data.passwordForm" label-position="left" label-width="120px">
        <el-form-item label="新密码">
          <el-input v-model="data.passwordForm.newPassword" show-password type="password" placeholder="请输入新密码"/>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="data.passwordForm.confirmPassword" show-password type="password" placeholder="请再次输入新密码"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.passwordDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitPasswordChange">确认修改</el-button>
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
  showPassword: false, // 控制密码显示状态
  passwordDialogVisible: false,
  isNew: true, // 新增模式标识
  passwordForm: {
    newPassword: "",
    confirmPassword: "",
  },
  form: {
    studentid: "",
    username: "",
    name: "",
    gender: "",
    age: "",
    password: "",
    phonenum: "",
  },
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
      studentid: item.studentid,
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

// 切换密码显示状态
const togglePasswordVisibility = () => {
  data.showPassword = !data.showPassword;
};

const dataAddSave = () => {
  const requestData = { ...data.form }; // 获取表单数据
  const url = data.isNew ? '/management/admin/studentAdd' : '/management/admin/studentUpdate';
  const method = data.isNew ? 'POST' : 'PUT';

  request
      .request({ url, method, data: requestData })
      .then((res) => {
        if (res.code === '200') {
          load();
          data.formvisible = false;
          ElMessage.success(data.isNew ? "学生新增成功" : "学生信息修改成功");
        } else {
          ElMessage.error(res.msg || "操作失败");
        }
      })
      .catch((error) => {
        ElMessage.error(error.response?.data?.msg || "操作失败");
      });
};


const validateUsername = (rule, value, callback) => {
  if (!value) {
    return callback(new Error("请输入学号"));
  }

  // 调用后端接口检查学号唯一性
  request
      .get("/management/admin/checkUsername", { params: { username: value } })
      .then((res) => {
        if (res.code === 200 && res.data.exists) {
          callback(new Error("学号已存在，请重新输入"));
        } else {
          callback();
        }
      })
      .catch(() => callback(new Error("验证失败，请稍后重试")));
};


const openChangePasswordDialog = () => {
  data.passwordDialogVisible = true;
  data.passwordForm.newPassword = "";
  data.passwordForm.confirmPassword = "";
};

const submitPasswordChange = () => {
  if (!data.form.studentid) {
    ElMessage.error("学生ID不存在，无法修改密码");
    return;
  }
  if (data.passwordForm.newPassword !== data.passwordForm.confirmPassword) {
    ElMessage.error("两次密码输入不一致");
    return;
  }
  request.post(`/management/admin/changePassword/${data.form.studentid}`, {
    newPassword: data.passwordForm.newPassword,
  }).then((res) => {
    if (res.code === '200') {
      ElMessage.success("密码修改成功");
      data.passwordDialogVisible = false;
      load()
    } else {
      ElMessage.error(res.msg || "密码修改失败");
    }
  });
};


const handleClick = (row) => {
  data.isNew = false; // 设置为编辑模式
  data.form = {...row}; // 深拷贝行数据
  console.log(data.form);
  data.formvisible = true;
};


const addFormShow = () => {
  data.isNew = true; // 设置为新增模式
  data.form = {      // 清空表单数据
    studentid: null,
    username: "",
    name: "",
    gender: "",
    age: "",
    password: "",
    phonenum: "",
  };
  data.formvisible = true;
};


const deleteManage = (studentid) => {
  ElMessageBox.confirm('删除后无法恢复，确认删除吗？', '删除确认', {type: 'warning'}).then(() => {
    request.delete('/student/delete/'+studentid).then(res => {
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
