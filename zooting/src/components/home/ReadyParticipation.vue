<template>
  <div class="participation__container">
    <img src="@/assets/images/logo/logo_lg.png" alt="" style="width: 500px; margin-bottom: 2rem;" class="">
    <img src="@/assets/images/home/heart-matching.png" class="heart-matching animate__bounceIn" alt="">
  </div>
</template>

<script setup lang="ts">
import 'animate.css'
</script>


<style scoped>
.participation__container {
  @apply h-screen w-full flex items-center justify-center flex-col;
}
.heart-matching {
  @apply w-2/3 xl:w-1/3 cursor-pointer;
}

</style>