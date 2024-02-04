<template>
  <div class="record__container">
    <div class="record__content">
      <h1 class="title">Record</h1>
    </div>
  </div>
</template>

<script setup lang="ts">

</script>

<style scoped>
.record__container {
  @apply col-span-2 rounded-2xl relative;
  border: 2px #b18597 solid;
  background-color: #f9c4d2;
}
.record__content {
  @apply bg-white absolute rounded-2xl flex flex-col items-center;
  border: 2px #b18597 solid;
  width: 98%;
  height: 98.7%;
  top: -0.5%;
  left: -0.7%;
}
.title {
  @apply flex text-3xl text-center items-center h-20;
}
</style>