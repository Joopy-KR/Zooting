<template>
  <div class="match__container">
    <div class="match__page">
      <button>매칭 시작</button>
    </div>
  </div>
</template>

<script setup lang="ts">
</script>


<style scoped>
.match__container {
  @apply flex flex-col items-center col-span-3 col-start-3 py-5;
}
.match__page {
  @apply flex flex-grow justify-center items-center;
}

.match__ready {
  @apply h-32 flex items-center;
}


</style>
