<template>
  <div class="interest__container">
    <ul role="list" class="friend-list">
      <!-- 친구 리스트 -->
      <li v-for="(item, index) in friendList" :key="index" class="friend-list__item">
        <!-- 리스트 누르면 채팅 열림 -->
        <div class="w-full cursor-pointer" @click="entryChat(item)">
          <div class="friend-list__item__chat">
            <div class="flex items-center gap-4">
              <!-- 프로필 이미지 -->
              <img class="friend-list__img" :src="getProfileImage(item.animal)" alt="profile">
              <div class="font-medium">
                <div class="flex items-center">
                  {{ item.nickname }}
                  <!-- 성별 아이콘 -->
                  <div class="gender-icon">
                    <svg :class="getHeartClass(item.gender)" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                      <path d="m12.7 20.7 6.2-7.1c2.7-3 2.6-6.5.8-8.7A5 5 0 0 0 16 3c-1.3 0-2.7.4-4 1.4A6.3 6.3 0 0 0 8 3a5 5 0 0 0-3.7 1.9c-1.8 2.2-2 5.8.8 8.7l6.2 7a1 1 0 0 0 1.4 0"/>
                    </svg>
                  </div>
                </div>
                <!-- 현재 접속 상태 -->
                <div class="friend-list__content">Content</div>
              </div>
            </div>
          </div>
        </div>
        <div class="flex items-center">

          <!-- 프로필 이동/ 친구 삭제 메뉴 -->
          <svg class="menu-button" @click="openMenu(item.nickname)" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <path stroke="currentColor" stroke-linecap="round" stroke-width="2" d="M12 6h0m0 6h0m0 6h0"/>
          </svg>
        </div>

        <div class="menu" v-show="isOpenMenu && openedMenuNickname === item.nickname">
          <div class="py-2 text-sm text-gray-700">
            <RouterLink :to="getProfileLink(item.nickname)" class="block px-4 py-2 hover:bg-gray-100">프로필 보기</RouterLink>
          </div>
          <div class="py-2">
            <div class="block px-4 py-2 text-sm text-red-700 cursor-pointer hover:bg-gray-100" @click="friendDelete(item.nickname)">친구 삭제</div>
          </div>
      </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useAccessTokenStore } from '@/stores/store'
import { RouterLink } from 'vue-router'
import type { Friend } from "@/types/global"

const store = useAccessTokenStore()

const friendList = ref(store.friendList)
const isOpenMenu = ref<boolean>(false)
const openedMenuNickname = ref<string | null>(null)

watch(() => store.friendList, (updatedList) => {
  friendList.value = updatedList
})

const getProfileLink = (value: string) => `/profile/${value}`

const friendDelete = (nickname: string) => {
  store.friendDelete(nickname)
} 

const getHeartClass = (gender: string) => (
  gender === 'man' ? 'w-4 h-4 text-blue-500 ms-1' : 'w-4 h-4 text-pink-500 ms-1'
)

const getProfileImage = (animal: string) => {
  const profile = animal === '강아지' ? 'dog' :
                 animal === '고양이' ? 'cat' :
                 animal === '곰' ? 'bear' :
                 animal === '공룡' ? 'dino' :
                 animal === '펭귄' ? 'penguin' :
                 animal === '토끼' ? 'rabbit' :
                 animal === '사슴' ? 'deer' :
                 'default-profile';
  return `src/assets/images/animal/${profile}.png`
}

const entryChat = (item: Friend) => {
  store.entryDmRoom(item)
  isOpenMenu.value = false
  openedMenuNickname.value = null
} 

const openMenu = (nickname: string) => {
  if (isOpenMenu.value && openedMenuNickname.value == nickname) {
    isOpenMenu.value = false
  } else {
    isOpenMenu.value = true
  }
  openedMenuNickname.value = isOpenMenu.value ? nickname : null
}
</script>

<style scoped>
.interest__container {
  @apply flex-grow;
  overflow-y: auto;
}
.interest__container::-webkit-scrollbar {
  width: 6px;
  background-color: white;
}
.interest__container::-webkit-scrollbar-thumb {
  background-color: #d6d6d6;
  border-radius: 4px;
}
.interest__container::-webkit-scrollbar-track {
  background-color: transparent;
}
.friend-list {
  @apply m-1 divide-y divide-gray-200;
}
.friend-list__item {
  @apply flex justify-between px-6 py-4;
}
.friend-list__item__chat {
  @apply flex items-center justify-between gap-4;
}
.friend-list__img {
  @apply w-10 h-10 rounded-full;
}
.friend-list__content {
  @apply text-sm text-gray-500;
}

button {
  @apply rounded bg-white px-4 text-xs font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 h-8;
}
.gender-icon {
  @apply text-xl;
}
.menu-button {
  @apply relative w-6 h-6 text-gray-800;
}
.menu {
  @apply absolute z-10 bg-white divide-y divide-gray-100 rounded-lg shadow w-44 left-[470px];
}
</style>
