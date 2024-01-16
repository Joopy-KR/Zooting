<template>
  <div class="flex items-center justify-center">
    <img src="@/assets/animal_illustration/dog_illust.png" alt="강아지 사진" style="width: 200px;"
    v-if="resultAnimal === '강아지'">
    <img src="@/assets/animal_illustration/cat_illust.png" alt="고양이 사진" style="width: 200px;"
    v-if="resultAnimal === '고양이'">
    <div>
      <p>회원님은</p>
      <p>{{ resultAnimal }}상이네요</p>
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