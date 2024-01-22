<script setup lang="ts">
import type { List } from "postcss/lib/list";
import IconMaskDropDown from "./IconMaskDropDown.vue";
const point: Number = 122;

interface Mask {
  id: number;
  imageUrl: string;
  price: Number;
  title: string;
}

const masks: Mask[] = [
  {
    id: 1,
    title: "개구쟁이 시바",
    imageUrl: "@/assets/images/mask/dog1.png",
    price: 30,
  },
  {
    id: 2,
    title: "멍뭉이",
    imageUrl: "@/assets/images/mask/dog2.png",
    price: 30,
  },
  {
    id: 3,
    title: "퇴근 시바",
    imageUrl: "@/assets/images/mask/dog3.png",
    price: 30,
  },
  {
    id: 4,
    title: "순댕이",
    imageUrl: "@/assets/images/mask/dog4.png",
    price: 30,
  },
  {
    id: 5,
    title: "금쪽이",
    imageUrl: "@/assets/images/mask/dog5.png",
    price: 30,
  },
];
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
    <div>
      <div class="grid grid-cols-3 gap-3 px-12 py-8 m-4">
        <div v-for="mask in masks" :key="mask.id">
          <div class="rounded-lg">
            <img :src="mask.imageUrl" :alt="mask.title" class="object-cover w-full h-full" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
