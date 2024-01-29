<script setup lang="ts">
import { ref, defineProps, watch, onMounted } from "vue";
import EditIntroduce from "@/components/profile/EditIntroduce.vue";

const props = defineProps({
  userInfo: Object,
  isMyProfile: Boolean,
});

const idealAnimals = ref<string[]>([]);
const isOpenEditIntroduce = ref<boolean>(false);

const parsingIdealAnimals = (idealAnimalStr: string) => {
  if (idealAnimalStr) {
    const idealAnimalArray = idealAnimalStr.replace("[", "").replace("]", "").split(", ");
    idealAnimals.value = idealAnimalArray.map((interest: string) => interest.trim());
  } else {
    idealAnimals.value = [];
  }
};

const openEditIntroduce = () => {
  isOpenEditIntroduce.value = true;
};
const closeEditIntroduce = () => {
  isOpenEditIntroduce.value = false;
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
  <EditIntroduce
    :is-open-edit-Introduce="isOpenEditIntroduce"
    @close-edit-introduce="closeEditIntroduce"
  />
  <div class="grid h-screen grid-rows-2">
    <div>
      <img :src="userInfo!.backgroundImgUrl" alt="profile-img" class="w-full h-full" />
    </div>
    <div class="grid items-center w-full grid-cols-2 justify-items-center">
      <div class="p-10 relative">
        <div
          class="max-h-full px-6 py-20 overflow-y-auto text-5xl font-bold tracking-tight text-center shadow-lg rounded-xl"
        >
          {{ userInfo!.introduce ? userInfo!.introduce : "안녕하세요" }}
        </div>
        <div
          v-if="isMyProfile"
          class="absolute bottom-14 right-14"
          @click="openEditIntroduce"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="currentColor"
            class="w-8 h-8 fill-rose-500 hover:fill-amber-400"
          >
            <path
              d="M21.731 2.269a2.625 2.625 0 0 0-3.712 0l-1.157 1.157 3.712 3.712 1.157-1.157a2.625 2.625 0 0 0 0-3.712ZM19.513 8.199l-3.712-3.712-12.15 12.15a5.25 5.25 0 0 0-1.32 2.214l-.8 2.685a.75.75 0 0 0 .933.933l2.685-.8a5.25 5.25 0 0 0 2.214-1.32L19.513 8.2Z"
            />
          </svg>
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
            <p class="text-2xl text-center text-gray-900 font-medium">
              # {{ idealAnimal }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
