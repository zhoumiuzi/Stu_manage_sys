<template>
  <div>
    <div class="card">
      <span>请输入关键字来查询课程：</span>
      <el-input v-model="data.cor_name" style="width: 150px" placeholder="输入课程名称" :prefix-icon="Search"
                @keydown.enter="load"/>
      <el-input v-model="data.cor_num" style="width: 150px" placeholder="输入课程号" :prefix-icon="Search"
                @keydown.enter="load"/>
      <el-input v-model="data.tea_name" style="width: 150px" placeholder="输入教师名称" :prefix-icon="Search"
                @keydown.enter="load"/>
      <el-button type="primary" style="margin: 0 10px 0 20px" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px 0 10px" @click="reload">重置</el-button>
    </div>

    <div class="card">
      <div>
        <el-button type="primary" style="margin: 0 10px 15px 5px" @click="addFormShow">新增课程</el-button>
      </div>
      <div class="table-wrapper">
        <el-table :data="data.tableData" stripe :row-key="row => row.course_id">
          <el-table-column prop="course_name" label="课程名称"/>
          <el-table-column prop="course_num" label="课程号"/>
          <el-table-column prop="course_type" label="课程类型"/>
          <el-table-column prop="course_credit" label="学分"/>
          <el-table-column prop="teacher_name" label="授课教师"/>
          <el-table-column prop="course_desc" label="课程介绍"/>
          <el-table-column fixed="right" label="操作" min-width="120">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleClick(scope.row)">
                编辑
              </el-button>
              <el-button type="danger" size="small" @click="deleteManage(scope.row.course_id)">删除</el-button>
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
        title="请填入想要处理的课程信息"
        width="500px"
        align-center
    >
      <el-form :model="data.form" label-position="left" label-width="120px">
        <el-form-item label="课程名称">
          <el-input v-model="data.form.cor_name"></el-input>
        </el-form-item>
        <el-form-item label="课程编号">
          <el-input v-model="data.form.cor_num"></el-input>
        </el-form-item>
        <el-form-item label="课程类型">
          <el-select v-model="data.form.cor_type" placeholder="请选择课程类型">
            <el-option label="专业必修" value="专业必修"></el-option>
            <el-option label="专业选修" value="专业选修"></el-option>
            <el-option label="通识必修" value="通识必修"></el-option>
            <el-option label="通识选修" value="通识选修"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课程学分">
          <el-input v-model="data.form.cor_credit"></el-input>
        </el-form-item>
        <el-form-item label="授课教师">
          <el-input v-model="data.form.tea_name"></el-input>
        </el-form-item>
        <el-form-item label="课程描述">
          <el-input v-model="data.form.cor_desc"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formvisible = false">取 消</el-button>
          <el-button type="primary" @click="dataAddSave">保 存</el-button>
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
  // 课程名字或者课程号
  cor_name: "",
  cor_num: "",
  tea_name: "",
  tableData: [],
  total: 0,
  pagenum: 1,
  pagesize: 18,
  formvisible: false,
  deletvisible: false,
  form: {
    cor_id: null,
    cor_name: "",
    cor_num: "",
    tea_name: "",
    cor_type: "",
    cor_credit: 0,
    cor_desc: ""
  }
});

const load = () => {
  request.get('/management/course/selectPage', {
    params: {
      //左边后端，右边前端，老是搞混！！！！！烦！！！
      pagenum: data.pagenum,
      pagesize: data.pagesize,
      coursename: data.cor_name,
      coursenum: data.cor_num,
      username: data.tea_name,
    }
  }).then(res => {//存在或者空,加问号，有值就取，没值就不取
    data.tableData = (res.data?.list || []).map(item => ({
      // 从后端直接来的字段
      course_id: item.courseid,
      course_name: item.coursename, // 对应后端的 coursename
      course_num: item.coursenum, // 对应后端的 coursenum
      course_type: item.coursetype, // 对应后端的 coursetype
      course_credit: item.coursecredits, // 对应后端的 coursecredits
      teacher_name: item.username || '暂无教师', // 对应后端的 teachername
      course_desc: item.coursedesc || '暂无描述', // 对应后端的 coursedesc
    }));
    data.total = res.data?.total || 0
  })
}
load()

const dataAddSave = () => {
  const requestData = {
    courseid: data.form.cor_id,
    coursename: data.form.cor_name,
    coursenum: data.form.cor_num,
    coursetype: data.form.cor_type,
    coursecredits: data.form.cor_credit,
    coursedesc: data.form.cor_desc === '暂无描述' ? '' : data.form.cor_desc,
    username: data.form.tea_name === '暂无教师' ? '' : data.form.tea_name,
  };

  request
      .request({
        url: data.form.cor_id ? '/management/course/update' : '/management/course/add',
        method: data.form.cor_id ? 'PUT' : 'POST',
        data: requestData,
      })
      .then((res) => {
        if (res.code === "200") {
          load();
          data.formvisible = false;
          ElMessage.success("课程处理成功");
        } else {
          ElMessage.error(res.msg || "课程处理失败");
        }
      })
      .catch((error) => {
        // 捕获后端返回的错误信息
        const errorMsg = error.response?.data?.msg || "课程处理失败";
        ElMessage.error(errorMsg);
      });
};


//这个click是编辑添加的click
const handleClick = (row) => {
  data.form = {
    cor_id: row.course_id,
    cor_name: row.course_name, // 表单绑定字段
    cor_num: row.course_num,
    cor_type: row.course_type,
    cor_credit: row.course_credit,
    tea_name: row.teacher_name === '暂无教师' ? '' : row.teacher_name, // 如果为暂无教师则设为空字符串
    cor_desc: row.course_desc === '暂无描述' ? '' : row.course_desc
  };
  data.formvisible = true;
}

//打开新增表单
const addFormShow = () => {
  data.form = {
    cor_id: null, // 确保新增时 cor_id 为 null
    cor_name: "",
    cor_num: "",
    cor_type: "",
    cor_credit: 0,
    cor_desc: "",
    tea_name: ""
  };
  data.formvisible = true;
}

const deleteManage = (course_id) => {
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗？', '删除确认', {type: 'warning'}).then(res => {
    request.delete('/management/course/delete/' + course_id).then(res => {
      if (res.code === '200') {
        load()    // 重新获取数据
        ElMessage.success("操作成功")
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(res => {
  })
}

//处理页面跳转
const handleCurrentChange = (pagenum) => {
  //data.pagenum = pagenum;
  load();
  //console.log(pagenum)
}

//重新加载表格信息
const reload = () => {
  data.form.cor_id = null; // 重置 form 中的 cor_id
  data.cor_num = '';
  data.cor_name = '';
  data.tea_name = '';
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
