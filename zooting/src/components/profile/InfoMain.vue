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
    <div class="banner">
      <!-- <img :src="profile.profileImg" alt="profile-img" class="w-full" /> -->
    </div>
    <div class="flex flex-row h-3/5">
      <div class="flex items-center justify-center w-7/12 p-10">
        <div
          class="max-h-full px-6 py-20 overflow-y-auto text-5xl font-bold tracking-tight text-center shadow-lg rounded-xl"
        >
          {{ profile.introduce }}
        </div>
      </div>

      <div class="flex items-center justify-start w-5/12 p-4">
        <div class="flex flex-col">
          <div class="px-8 py-4">
            <p class="text-5xl font-bold tracking-tight text-center">이런 사람이 좋아요</p>
          </div>
          <div class="grid grid-cols-1 gap-6 p-8 sm:grid-cols-3">
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

<style scoped>
.banner {
  @apply h-1/3;
  background-color: aquamarine;
  height: 40vh;
}

.banner img {
  height: inherit;
}
</style>
