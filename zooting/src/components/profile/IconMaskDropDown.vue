<script setup lang="ts">
import { ref } from "vue";

const activeBadge = ref<string>("강아지상");
const showAllBadges = ref<boolean>(false);
const allowDirection = ref<string>("arrow-down");

const toggleBadge = (badge: string) => {
  showAllBadges.value = false;
  allowDirection.value = "arrow-down";
  activeBadge.value = badge;
};

const toggleShowAllBadge = () => {
  showAllBadges.value = !showAllBadges.value;
  if (showAllBadges.value) {
    allowDirection.value = "arrow-up";
  } else {
    allowDirection.value = "arrow-down";
  }
};
</script>

<template>
  <div class="relative">
    <div class="absolute top-0 left-3">
      <div class="flex flex-row">
        <div class="flex justify-center px-2 mr-3">
          <font-awesome-icon
            :icon="['fas', allowDirection]"
            size="3x"
            @click="toggleShowAllBadge"
            class="font-awesome-icon"
          />
        </div>
        <div class="flex flex-col gap-2">
          <span
            v-if="showAllBadges || activeBadge === '강아지상'"
            @click="() => toggleBadge('강아지상')"
            class="inline-flex items-center px-2 py-1 text-xs font-medium text-red-700 bg-red-100 rounded-full"
          >
            <p class="px-4 py-2 text-3xl font-medium w-44">강아지상</p></span
          >
          <span
            v-if="showAllBadges || activeBadge === '고양이상'"
            @click="() => toggleBadge('고양이상')"
            class="inline-flex items-center px-2 py-1 text-xs font-medium text-blue-700 bg-blue-100 rounded-full"
            ><p class="px-4 py-2 text-3xl font-medium w-44">고양이상</p></span
          >
          <span
            v-if="showAllBadges || activeBadge === '토끼상'"
            @click="() => toggleBadge('토끼상')"
            class="inline-flex items-center px-2 py-1 text-xs font-medium text-pink-700 bg-pink-100 rounded-full"
            ><p class="px-4 py-2 text-3xl font-medium w-44">토끼상</p></span
          >
          <span
            v-if="showAllBadges || activeBadge === '곰상'"
            @click="() => toggleBadge('곰상')"
            class="inline-flex items-center px-2 py-1 text-xs font-medium text-purple-700 bg-purple-100 rounded-full"
            ><p class="px-4 py-2 text-3xl font-medium w-44">곰상</p></span
          >
          <span
            v-if="showAllBadges || activeBadge === '공룡상'"
            @click="() => toggleBadge('공룡상')"
            class="inline-flex items-center px-2 py-1 text-xs font-medium text-green-700 bg-green-100 rounded-full"
            ><p class="px-4 py-2 text-3xl font-medium w-44">공룡상</p></span
          >
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.font-awesome-icon {
  justify-content: center;
  align-items: center;
}
</style>
