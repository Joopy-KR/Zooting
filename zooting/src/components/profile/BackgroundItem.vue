<script setup lang="ts">
import type {Background, UserInfo} from "@/types/global";

const props = defineProps({
  userInfo: Object as () => UserInfo,
  image: {
    type: Object as () => Background,
    default: {
      backgroundId: 0,
      fileName: undefined,
      imgUrl: '',
      price: 0,
      status: false,
      isSelected: false,
    }
  },
})
</script>

<template>
  <div class="relative rounded-lg ">
    <img
        v-if="image.isSelected"
        class="absolute top-0 left-0 z-10 lg:w-5 md:w-4 sm:w-2"
        src="/assets/images/mask/check_icon.png"
        alt="mask_selected_info"/>
    <img
        v-if="!image.status"
        class="absolute bottom-0 right-0 z-10 lg:w-5 md:w-4 sm:w-2"
        src="/assets/images/mask/lock_icon.png"
        alt="mask_locked_info"/>
    <div>
      <div
          class="group aspect-h-7 aspect-w-10 block w-full overflow-hidden rounded-lg bg-gray-100 focus-within:ring-2 focus-within:ring-indigo-500 focus-within:ring-offset-2 focus-within:ring-offset-gray-100">
        <img :src="image.imgUrl" alt="" class="pointer-events-none object-cover group-hover:opacity-75"/>
        <button type="button" class="absolute inset-0 focus:outline-none"/>
      </div>
      <p class="pointer-events-none mt-2 block truncate font-semibold text-gray-900">
        {{ image.fileName }}</p>
    </div>
  </div>
</template>

<style scoped>

</style>