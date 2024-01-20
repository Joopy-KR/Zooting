<template>
  <div class="flex items-center justify-center">
    <img src="@/assets/animal_illustration/dog_illust.png" alt="강아지 사진" style="width: 200px;"
    v-if="resultAnimal === '강아지'">
    <img src="@/assets/animal_illustration/cat_illust.png" alt="고양이 사진" style="width: 200px;"
    v-if="resultAnimal === '고양이'">
    <img src="@/assets/animal_illustration/bear_illust.png" alt="곰 사진" style="width: 200px;"
    v-if="resultAnimal === '곰'">
    <img src="@/assets/animal_illustration/dino_illust.png" alt="공룡 사진" style="width: 200px;"
    v-if="resultAnimal === '공룡'">
    <img src="@/assets/animal_illustration/rabbit_illust.png" alt="토끼 사진" style="width: 200px;"
    v-if="resultAnimal === '토끼'">
    <img src="@/assets/animal_illustration/turtle_illust.png" alt="꼬부기 사진" style="width: 200px;"
    v-if="resultAnimal === '꼬부기'">
    <img src="@/assets/animal_illustration/deer_illust.png" alt="사슴 사진" style="width: 200px;"
    v-if="resultAnimal === '사슴'">
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