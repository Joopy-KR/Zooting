<template>
  <div class="description__container">
    <transition name="fade">
      <p v-if="isShow">간단한 테스트를 통해 성격 유형을 알아볼게요</p>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

const emit = defineEmits(['testStart'])

const isShow = ref(false)

onMounted(Run)

async function Run() {
    await wait(0.5)
    isShow.value = true
    await wait(1.2)
    isShow.value = false
    await wait(1)
    emit('testStart')
}

const wait = (sec:number) => {
      return new Promise(resolve => setTimeout(resolve, sec * 1000));
}
</script>

<style scoped>
.description__container {
 @apply flex flex-col items-center justify-center w-full h-full text-center bg-white border border-gray-200 p-3; 
}
.base-bg {
  background-image: url("/assets/images/login/background.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}
.description__container p {
  @apply text-4xl font-bold
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 1s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>