<template>
  <div class="record__container">
    <div class="record__top">
      <button @click="toggleContent">x</button>
    </div>
    <div v-if="showContent" class="record__content">
      <!-- Your content goes here -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const showContent = ref(true);

const toggleContent = () => {
  showContent.value = !showContent.value;
};
</script>

<style scoped>
.record__container {
  @apply flex flex-col col-span-2;
}

.record__top {
  @apply flex justify-end h-8 bg-slate-500;
}

.record__content {
  @apply flex-grow border border-violet-300;
  transition: transform 0.3s ease-out;
  transform-origin: top;
  transform: translateY(0);
}

.record__content.slide-up {
  transform: translateY(-100%);
}
</style>
