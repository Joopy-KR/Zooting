<template>
  <div class="record__container" :class="{ 'shadow-md': isShowContent }">
    <div class="record__top" :class="{ 'shadow-md': !isShowContent }">
      <div class="record__dots">
        <div class="record__dot dot1"></div>
        <div class="record__dot dot2"></div>
        <div class="record__dot dot3"></div>
      </div>
      <button @click="toggleContent" class="record__toggle">
        <font-awesome-icon :icon="['fas', 'window-minimize']" v-show="isShowContent"/>
        <font-awesome-icon :icon="['fas', 'window-maximize']" v-show="!isShowContent"/>
      </button>
    </div>
    <div v-if="isShowContent" class="record__content">
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const isShowContent = ref<boolean>(false)

const toggleContent = () => {
  isShowContent.value = !isShowContent.value
}
</script>

<style scoped>
.record__container {
  @apply flex flex-col col-span-2;
}

.record__top {
  @apply flex justify-between h-8 px-2;
  background-color: #7360DF;
}

.record__content {
  @apply flex-grow border border-gray-300 bg-white;

}
.record__dots {
  @apply flex items-center;
}
.record__dot {
  @apply rounded-3xl w-4 h-4 mx-1;
  border: 2px white solid;
}
.dot1 {
  background-color: rgb(239, 99, 160);
}
.dot2 {
  background-color: rgb(249, 244, 108);
}
.dot3 {
  background-color: rgba(0, 255, 89, 0.558);
}
.record__toggle {
  @apply text-white;
}
</style>
