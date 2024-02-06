<script setup lang="ts">
import {computed, ref} from "vue";

const props = defineProps({
  gender: String,
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
  {name: "강아지", type: "common", style: "mask__selector-dog"},
  {name: "고양이", type: "common", style: "mask__selector-cat"},
  {name: "토끼", type: "common", style: "mask__selector-rabbit"},
  {name: "곰", type: "man", style: "mask__selector-bear"},
  {name: "공룡", type: "man", style: "mask__selector-dinosaur"},
  {name: "펭귄", type: "woman", style: "mask__selector-penguin"},
  {name: "사슴", type: "woman", style: "mask__selector-deer"},
];
const showAnimalType = computed(() => {
  return animalTypes.filter(animalType => animalType.type === "common" || animalType.type === props.gender);
});

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
    <div class="absolute lg:-top-1 lg:left-3 sm:top-20">
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
          <span v-for="badge in showAnimalType" :key="badge.name">
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
  @apply font-sans flex justify-center px-3 py-2 sm:text-sm lg:text-xl font-semibold sm:w-20 lg:w-32 rounded-full;
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

.mask__selector-penguin {
  @apply text-cyan-500 bg-cyan-200 hover:bg-cyan-300;
}

.mask__selector-deer {
  @apply text-amber-500 bg-amber-100 hover:bg-amber-200;
}
</style>
