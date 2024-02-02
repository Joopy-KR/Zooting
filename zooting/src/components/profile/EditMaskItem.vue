<script setup lang="ts">
import { ListBulletIcon } from "@heroicons/vue/16/solid";

interface Mask {
  maskId: number;
  animal: string;
  description: string;
  price: number;
  fileName: string;
  imgUrl: string;
  status: boolean;
  isSelected: boolean;
}

const props = defineProps({
  mask: {
    type: Object as () => Mask,
    required: true,
  },
});
</script>
<template>
  <div
    class="flex items-center justify-center overflow-hidden border-2 border-spacing-2 rounded-3xl border-red-500/20"
    :class="{ 'bg-gray-400': !mask.status }"
  >
    <div class="relative">
      <img
        v-if="mask.isSelected"
        class="absolute top-0 left-0 z-10 w-16"
        src="/src/assets/images/mask/check_icon.png"
      />
      <img
        v-if="!mask.status"
        class="absolute bottom-0 right-0 z-10 w-16"
        src="/src/assets/images/mask/lock_icon.png"
      />
      <img
        :src="mask.imgUrl"
        :alt="mask.description"
        class="z-0 object-contain w-full h-full p-2"
        :style="{ filter: mask.status ? 'none' : 'brightness(60%)' }"
      />
    </div>
  </div>
</template>

<style scoped></style>
