<template>
  <div>
    <div class="card" style="line-height: 30px" v-if="user.role === 'Admin'">
      <div>欢迎您，工号为 <span style="color: #5998b9;font-weight: bold">{{ user.adminid }}</span> 的 <span
          style="color: #5998b9;font-weight: bold">{{ user.username }}</span> 管理员! 祝您今天过得开心！
      </div>
    </div>

    <div class="card" style="line-height: 30px" v-if="user.role === 'Student'">
      <div>欢迎您，学号为 <span style="color: #5998b9;font-weight: bold">{{ user.username }}</span> 的 <span
          style="color: #5998b9;font-weight: bold">{{ user.name }}</span> ! 祝您今天过得开心！
      </div>
    </div>

    <div class="image-container">
      <img src="@/assets/imgs/flower.gif" alt="Image" />
    </div>
  </div>
</template>

<script setup>
const user = JSON.parse(localStorage.getItem('student-user') || '{}')
</script>

<style>
body {
  background-color: #efd3cd;
}

.card {
  background-color: rgb(227, 195, 193);
  padding: 20px;
  margin: 10px 0;
  box-shadow: 5px 5px 10px 0 rgb(105, 61, 83);
}

.image-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 50px;
}

.image-container img {
  width: 100%;
  max-width: 800px; /* 控制图片的最大宽度 */
  height: auto;
  object-fit: cover; /* 保持图片比例，填充容器 */
}
</style>
