<script setup lang="ts">
import { ref } from "vue";

const profile = ref({
  nickname: "",
  interests: ["등산", "수영", "여행", "캠핑", "게임", "노래"],
});
</script>
<template>
  <div class="flex flex-row h-screen divide-x-2 divide-gray-100">
    <div class="flex flex-col w-1/3 p-4">
      <div class="flex justify-end h-1/12">
        <p>icon</p>
      </div>
      <div class="flex justify-center p-4">
        <div class="p-2 bg-gray-100 rounded-full">
          <img src="@/assets/images/mypage/default_animal.png" class="w-full p-2" />
        </div>
      </div>
      <div class="mx-8">
        <div class="flex justify-center p-2">
          <p class="text-3xl font-semibold">Joopy_KR</p>
        </div>
        <div class="flex flex-row p-2">
          <div class="flex items-center justify-center w-1/3 text-2xl">여성</div>
          <div class="flex items-center justify-center w-1/3 text-2xl">20대 중반</div>
          <div class="flex items-center justify-center w-1/3 text-2xl">서울</div>
        </div>
        <div class="flex justify-center my-4 text-4xl font-bold tracking-tight">
          <p>겨울의 눈송이 개</p>
        </div>
        <!-- <div class="flex p-4 justify-items-center">
          <div class="flex flex-wrap p-4 rounded-lg">
            <div class="px-6 py-4 mx-4 my-2 border-2 rounded-lg">
              <p class="text-xl text-center"># 등산</p>
            </div>
            <div class="px-6 py-4 mx-4 my-2 border-2 rounded-lg">
              <p class="text-xl text-center"># 수영</p>
            </div>
            <div class="px-6 py-4 mx-4 my-2 border-2 rounded-lg">
              <p class="text-xl text-center"># 여행</p>
            </div>
            <div class="px-6 py-4 mx-4 my-2 border-2 rounded-lg">
              <p class="text-xl text-center"># 캠핑</p>
            </div>
            <div class="px-6 py-4 mx-4 my-2 border-2 rounded-lg">
              <p class="text-xl text-center"># 게임</p>
            </div>
            <div class="px-6 py-4 mx-4 my-2 border-2 rounded-lg">
              <p class="text-xl text-center"># 노래</p>
            </div>
          </div>
        </div> -->
        <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
          <div
            v-for="interest in profile.value.interests"
            :key="person.email"
            class="relative flex items-center px-6 py-5 space-x-3 bg-white border border-gray-300 rounded-lg shadow-sm focus-within:ring-2 focus-within:ring-indigo-500 focus-within:ring-offset-2 hover:border-gray-400"
          >
            <div class="flex-shrink-0">
              <img class="w-10 h-10 rounded-full" :src="person.imageUrl" alt="" />
            </div>
            <div class="flex-1 min-w-0">
              <a href="#" class="focus:outline-none">
                <span class="absolute inset-0" aria-hidden="true" />
                <p class="text-sm font-medium text-gray-900">{{ person.name }}</p>
                <p class="text-sm text-gray-500 truncate">{{ person.role }}</p>
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="flex flex-col w-2/3 px-8">spofnsonfs</div>
  </div>
</template>

<style scoped></style>
