<template>
  <div class="flex flex-col items-center justify-center gap-2">
    <div class="record__profile">
      <img :src="getProfileImage(recordItem.animal)" alt="profile">
    </div>
    <div class="record__nickname">
      {{ recordItem.nickname }}
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  recordItem: any
}>()

const getProfileImage = (animal: string) => {
  let imgUrl: URL;
  if (animal === '강아지') {
    imgUrl = new URL('/assets/images/animal/dog.png', import.meta.url);
  } else if (animal === '고양이') {
    imgUrl = new URL('/assets/images/animal/cat.png', import.meta.url);
  } else if (animal === '곰') {
    imgUrl = new URL('/assets/images/animal/bear.png', import.meta.url);
  } else if (animal === '공룡') {
    imgUrl = new URL('/assets/images/animal/dino.png', import.meta.url);
  } else if (animal === '펭귄') {
    imgUrl = new URL('/assets/images/animal/penguin.png', import.meta.url);
  } else if (animal === '토끼') {
    imgUrl = new URL('/assets/images/animal/rabbit.png', import.meta.url);
  } else if (animal === '사슴') {
    imgUrl = new URL('/assets/images/animal/deer.png', import.meta.url);
  } else {
    imgUrl = new URL('/assets/images/animal/animal_group.png', import.meta.url);
  }
  return imgUrl.href;
}
</script>

<style scoped>
.record__profile {
  @apply rounded-full bg-white border-gray-200 shadow-md p-1;
  width: 90px;
  height: 90px;
}
.record__nickname {
  @apply text-center text-2xl;
}
</style>