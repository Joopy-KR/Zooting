<script setup lang="ts">
import { ref, defineProps, watch, onMounted } from "vue";

const props = defineProps({
  userInfo: Object,
});

const idealAnimals = ref<string[]>([]);

const parsingIdealAnimals = (idealAnimalStr: string) => {
  if (idealAnimalStr) {
    const idealAnimalArray = idealAnimalStr.replace("[", "").replace("]", "").split(", ");
    idealAnimals.value = idealAnimalArray.map((interest: string) => interest.trim());
  } else {
    idealAnimals.value = [];
  }
};

watch(
  () => props.userInfo!.idealAnimal,
  (newValue, oldValue) => {
    parsingIdealAnimals(newValue);
  }
);

onMounted(() => {
  parsingIdealAnimals(props.userInfo!.idealAnimal);
});
</script>

<template>
  <div class="grid h-screen grid-rows-2">
    <div>
      <img :src="userInfo!.backgroundImgUrl" alt="profile-img" class="w-full h-full" />
    </div>
    <div class="grid items-center w-full grid-cols-2 justify-items-center">
      <div class="p-10">
        <div
          class="max-h-full px-6 py-20 overflow-y-auto text-5xl font-bold tracking-tight text-center shadow-lg rounded-xl"
        >
          {{ userInfo!.introduce ? userInfo!.introduce : "안녕하세요" }}
        </div>
      </div>
      <div class="p-5">
        <div class="px-8 py-4">
          <p class="text-4xl font-bold tracking-tight text-center">이런 사람이 좋아요</p>
        </div>

        <div class="grid grid-cols-3 gap-4 px-4 py-2">
          <div
            v-for="idealAnimal in idealAnimals"
            :key="idealAnimal"
            class="px-4 py-2 border-gray-300 rounded-lg shadow-lg"
          >
            <p class="text-2xl text-center text-gray-900 font-medium"># {{ idealAnimal }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
