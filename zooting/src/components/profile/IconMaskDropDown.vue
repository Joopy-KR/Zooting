<script setup lang="ts">
import { ref, defineProps, defineEmits } from "vue";

const props = defineProps({
  animalType: String,
});

const emits = defineEmits(["setAnimalType"]);

const showAllBadges = ref<boolean>(false);
const allowDirection = ref<string>("arrow-down");

interface Animal {
  name: string;
  type: string;
  style: string;
}
const animalTypes: Animal[] = [
  { name: "강아지", type: "common", style: "mask__selector-dog" },
  { name: "고양이", type: "common", style: "mask__selector-cat" },
  { name: "토끼", type: "common", style: "mask__selector-rabbit" },
  { name: "곰", type: "common", style: "mask__selector-bear" },
  { name: "공룡", type: "common", style: "mask__selector-dinosaur" },
];

const toggleBadge = (badge: string) => {
  showAllBadges.value = false;
  allowDirection.value = "arrow-down";
  emits("setAnimalType", badge);
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
    <div class="absolute -top-1 left-3">
      <div class="flex flex-row">
        <div class="flex justify-center items-start px-2 py-1">
          <font-awesome-icon
            :icon="['fas', allowDirection]"
            size="2x"
            @click="toggleShowAllBadge"
            class="font-awesome-icon"
          />
        </div>
        <div :class="{ div__mask_showAll: showAllBadges, div__mask: !showAllBadges }">
          <span v-for="badge in animalTypes" :key="badge.name">
            <p
              v-if="showAllBadges || animalType === badge.name"
              @click="() => toggleBadge(badge.name)"
              :class="['mask__selector', badge.style]"
            >
              {{ badge.name }}상
            </p>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.font-awesome-icon {
  display: flex;
  justify-content: center;
  align-items: center;
}
.div__mask {
  @apply flex flex-col;
}
.div__mask_showAll {
  @apply flex flex-col gap-2;
}
.mask__selector {
  @apply font-sans flex justify-center px-3 py-2 lg:text-xl font-semibold w-32 rounded-full;
}

.mask__selector-dog {
  @apply text-red-700 bg-red-100 hover:bg-red-200;
}
.mask__selector-cat {
  @apply text-blue-700 bg-blue-100 hover:bg-blue-200;
}
.mask__selector-rabbit {
  @apply text-pink-700 bg-pink-100 hover:bg-pink-200;
}
.mask__selector-bear {
  @apply text-purple-700 bg-purple-100 hover:bg-purple-200;
}
.mask__selector-dinosaur {
  @apply text-green-600 bg-green-100 hover:bg-green-200;
}
</style>
