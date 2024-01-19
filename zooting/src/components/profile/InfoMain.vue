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
  <div class="grid h-screen grid-rows-2">
    <div>
      <img :src="profile.profileImg" alt="profile-img" class="w-full h-full" />
    </div>
    <div class="grid items-center w-full grid-cols-2 justify-items-center">
      <div class="p-10">
        <div
          class="max-h-full px-6 py-20 overflow-y-auto text-5xl font-bold tracking-tight text-center shadow-lg rounded-xl"
        >
          {{ profile.introduce }}
        </div>
      </div>
      <div class="p-5">
        <div class="px-8 py-4">
          <p class="text-5xl font-bold tracking-tight text-center">이런 사람이 좋아요</p>
        </div>

        <div class="p-5">
          <div class="grid grid-cols-3 gap-6 px-8 py-2">
            <div
              v-for="interest in profile.interests"
              :key="interest"
              class="px-6 py-3 border-gray-300 rounded-lg shadow-lg"
            >
              <div class="flex-1 min-w-0">
                <span class="absolute inset-0" aria-hidden="true" />
                <p class="text-3xl font-medium text-center text-gray-900"># {{ interest }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
