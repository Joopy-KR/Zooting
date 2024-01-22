<script setup lang="ts">
import { ref } from "vue";

const profile = ref({
  nickname: "",
  interests: ["등산", "수영", "여행", "캠핑", "게임", "노래"],
  introduce: "캐치마인드 개고수 입니다",
  profileImg:
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQCgmII3Iks54cyWu3LiHejR1mcmJb_PVT_unHodbxd5dk_Tg4oN38yn4JN_km5a9xoT4Q&usqp=CAU",
});
</script>

<template>
  <div class="flex flex-col">
    <div class="flex justify-end px-8 pt-4 h-1/12">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        stroke-width="2.0"
        stroke="currentColor"
        class="w-12 h-12"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M9.594 3.94c.09-.542.56-.94 1.11-.94h2.593c.55 0 1.02.398 1.11.94l.213 1.281c.063.374.313.686.645.87.074.04.147.083.22.127.325.196.72.257 1.075.124l1.217-.456a1.125 1.125 0 0 1 1.37.49l1.296 2.247a1.125 1.125 0 0 1-.26 1.431l-1.003.827c-.293.241-.438.613-.43.992a7.723 7.723 0 0 1 0 .255c-.008.378.137.75.43.991l1.004.827c.424.35.534.955.26 1.43l-1.298 2.247a1.125 1.125 0 0 1-1.369.491l-1.217-.456c-.355-.133-.75-.072-1.076.124a6.47 6.47 0 0 1-.22.128c-.331.183-.581.495-.644.869l-.213 1.281c-.09.543-.56.94-1.11.94h-2.594c-.55 0-1.019-.398-1.11-.94l-.213-1.281c-.062-.374-.312-.686-.644-.87a6.52 6.52 0 0 1-.22-.127c-.325-.196-.72-.257-1.076-.124l-1.217.456a1.125 1.125 0 0 1-1.369-.49l-1.297-2.247a1.125 1.125 0 0 1 .26-1.431l1.004-.827c.292-.24.437-.613.43-.991a6.932 6.932 0 0 1 0-.255c.007-.38-.138-.751-.43-.992l-1.004-.827a1.125 1.125 0 0 1-.26-1.43l1.297-2.247a1.125 1.125 0 0 1 1.37-.491l1.216.456c.356.133.751.072 1.076-.124.072-.044.146-.086.22-.128.332-.183.582-.495.644-.869l.214-1.28Z"
        />
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"
        />
      </svg>
    </div>
    <div class="flex justify-center p-4">
      <div class="p-2 bg-gray-100 rounded-full">
        <img src="@/assets/images/mypage/default_animal.png" class="w-full p-2" />
      </div>
    </div>
    <div class="mx-8">
      <div class="flex justify-center p-2">
        <p class="text-4xl font-medium">Joopy_KR</p>
      </div>
      <div class="flex flex-row p-2 my-4">
        <div class="flex items-center justify-center w-1/3 text-3xl font-medium">여성</div>
        <div class="flex items-center justify-center w-1/3 text-3xl font-medium">20대 중반</div>
        <div class="flex items-center justify-center w-1/3 text-3xl font-medium">서울</div>
      </div>
      <div class="flex justify-center mx-4 my-8 text-5xl font-bold tracking-tight">
        <p class="">겨울의 눈송이 개</p>
      </div>
      <div class="flex items-end justify-center min-w-max">
        <div class="w-2/3 m-4 border-t-4 shadow-inner rounded-3xl">
          <div class="grid grid-cols-1 gap-6 p-8 sm:grid-cols-2">
            <div
              v-for="interest in profile.interests"
              :key="interest"
              class="relative flex items-center px-6 py-3 space-x-3 bg-white border-gray-300 rounded-lg shadow-lg focus-within:ring-2 focus-within:ring-indigo-500 focus-within:ring-offset-2 hover:border-gray-400"
            >
              <!-- <div class="flex-shrink-0">
      <img class="w-10 h-10 rounded-full" :src="person.imageUrl" alt="" />
    </div> -->
              <div class="flex-1 min-w-0">
                <span class="absolute inset-0" aria-hidden="true" />
                <p class="text-3xl font-medium text-center text-gray-900"># {{ interest }}</p>
                <!-- <p class="text-sm text-gray-500 truncate">{{ interest }}</p> -->
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
