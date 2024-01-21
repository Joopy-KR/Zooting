<script setup lang="ts">
import IconMaskDropDown from "./IconMaskDropDown.vue";
const point: Number = 122;
</script>

<template>
  <div class="flex flex-col">
    <div>
      <p class="p-12 m-4 text-5xl font-bold tracking-tighter text-center">아바타 동물상 선택</p>
    </div>
    <div>
      <div class="flex flex-row justify-between px-12 m-4">
        <IconMaskDropDown />
        <span
          class="inline-flex items-center gap-x-1.5 rounded-full bg-gray-100 px-2 py-1 text-xs font-medium text-gray-600 w-auto h-11"
        >
          <div class="flex items-center justify-center">
            <p class="px-4 py-2 text-3xl font-medium text-center">{{ point }} Point</p>
          </div>
        </span>
      </div>
    </div>
    <div>sofnsonfosnfosnf</div>
    <div>sofnsonfosnfosnf</div>
    <div>sofnsonfosnfosnf</div>
  </div>
</template>

<style scoped></style>
