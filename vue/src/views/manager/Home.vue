<template>
  <div>

    <div class="card" style="line-height: 30px">
      <div>欢迎您，工号为 <span style="color: #5998b9;font-weight: bold">{{ user.adminId }}</span> 的 <span
          style="color: #5998b9;font-weight: bold">{{ user.username }}</span> 管理员! 祝您今天过得开心！
      </div>
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
</style>