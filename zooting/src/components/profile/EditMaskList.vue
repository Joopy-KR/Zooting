<script setup lang="ts">
import EditMaskItem from "@/components/profile/EditMaskItem.vue";
import IconMaskDropDown from "./IconMaskDropDown.vue";
const point: Number = 122;

interface Mask {
  id: number;
  imageUrl: string;
  price: Number;
  title: string;
  status: boolean;
  isSelected: boolean;
}

const masks: Mask[] = [
  {
    id: 1,
    title: "개구쟁이 시바",
    imageUrl: "/src/assets/images/mask/dog1.png",
    price: 30,
    status: true,
    isSelected: false,
  },
  {
    id: 2,
    title: "멍뭉이",
    imageUrl: "/src/assets/images/mask/dog2.png",
    price: 30,
    status: true,
    isSelected: false,
  },
  {
    id: 3,
    title: "퇴근 시바",
    imageUrl: "/src/assets/images/mask/dog3.png",
    price: 30,
    status: false,
    isSelected: false,
  },
  {
    id: 4,
    title: "순댕이",
    imageUrl: "/src/assets/images/mask/dog4.png",
    price: 30,
    status: false,
    isSelected: false,
  },
  {
    id: 5,
    title: "금쪽이",
    imageUrl: "/src/assets/images/mask/dog5.png",
    price: 30,
    status: true,
    isSelected: true,
  },
];
</script>

<template>
  <div class="flex flex-col">
    <p class="p-12 m-4 text-5xl font-bold tracking-tighter text-center">아바타 동물상 선택</p>
    <div>
      <div class="flex flex-row justify-between px-12 mr-4">
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
    <div class="flex items-center content-center justify-center w-2/3 mx-auto">
      <div class="grid grid-cols-3 gap-3 px-12 py-8 m-4">
        <EditMaskItem v-for="mask in masks" :key="mask.id" :mask="mask" />
      </div>
    </div>
    <div class="flex justify-center">
      <button
        type="button"
        class="rounded-full bg-indigo-600 px-4 py-2.5 w-1/2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
      >
        동물상 다시 검사하기
      </button>
    </div>
  </div>
</template>

<style scoped></style>
