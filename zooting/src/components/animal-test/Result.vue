<template>
  <div class="flex items-center justify-center">
    <img src="@/assets/animal_illustration/dog_illust.png" alt="강아지 사진" style="width: 200px;"
    v-if="resultAnimal === '강아지'">
    <img src="@/assets/animal_illustration/cat_illust.png" alt="고양이 사진" style="width: 200px;"
    v-if="resultAnimal === '고양이'">
    <div class="flex flex-col items-center justify-center">
      <p class="text-xl">회원님은</p>
      <p class="text-xl"><span class="font-bold">{{ resultAnimal }}상</span>이시네요</p>
    </div>
  </div>

</template>

<script setup>

const props = defineProps({
  resultAnimal: String
})

</script>

<style scoped>

</style>