<template>
  <div class="participation__container">
    <h1>Matching</h1>
    <img src="/images/heart-matching.png" class="heart-matching animate__bounceIn" alt="">
  </div>
</template>

<script setup lang="ts">
import 'animate.css'
</script>


<style scoped>
.participation__container {
  @apply col-span-3 border border-black;
}
.heart-matching {
  @apply w-2/3 xl:w-1/3 cursor-pointer;
}

</style>