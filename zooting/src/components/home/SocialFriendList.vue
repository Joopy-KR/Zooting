<template>
  <div class="interest__container">
    <ul role="list" class="friend-list">
      <!-- 친구 리스트 -->
      <li v-for="(item, index) in friendList" :key="index" class="friend-list__item">
        <!-- 리스트 누르면 채팅 열림 -->
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
          
        <div class="buttons">
          <!-- 채팅 버튼 -->
          <svg class="dm-button" @click="entryChat(item)" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="m12 18-7 3 7-18 7 18-7-3Zm0 0v-5"/>
          </svg>
          <!-- 새로운 채팅 알림 -->
          <div class="absolute bottom-[10px] left-[10px] cursor-pointer" v-show="isNewSender(item.email)" @click="entryChat(item)">
            <span class="relative flex w-2 h-2">
              <span class="absolute inline-flex w-full h-full rounded-full opacity-75 animate-ping bg-[#DF75DB]"></span>
              <span class="relative inline-flex w-2 h-2 rounded-full bg-[#DF75DB]"></span>
            </span>
          </div>
          <!-- 프로필 이동/ 친구 삭제 메뉴 버튼 -->
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

const emit = defineEmits(['readMessage'])

const friendList = ref(store.friendList)
const isOpenMenu = ref<boolean>(false)
const openedMenuNickname = ref<string | null>(null)

watch(() => store.friendList, (update) => {
  friendList.value = update
})

const getProfileLink = (value: string) => `/profile/${value}`

const friendDelete = (nickname: string) => {
  store.friendDelete(nickname)
} 

const getHeartClass = (gender: string) => (
  gender === 'man' ? 'w-4 h-4 text-blue-500 ms-1' : 'w-4 h-4 text-pink-500 ms-1'
)

const getProfileImage = (animal: string) => {
  let imgUrl: URL;
  if (animal === '강아지') {
    imgUrl = new URL('@/assets/images/animal/dog.png', import.meta.url);
  } else if (animal === '고양이') {
    imgUrl = new URL('@/assets/images/animal/cat.png', import.meta.url);
  } else if (animal === '곰') {
    imgUrl = new URL('@/assets/images/animal/bear.png', import.meta.url);
  } else if (animal === '공룡') {
    imgUrl = new URL('@/assets/images/animal/dino.png', import.meta.url);
  } else if (animal === '펭귄') {
    imgUrl = new URL('@/assets/images/animal/penguin.png', import.meta.url);
  } else if (animal === '토끼') {
    imgUrl = new URL('@/assets/images/animal/rabbit.png', import.meta.url);
  } else if (animal === '사슴') {
    imgUrl = new URL('@/assets/images/animal/deer.png', import.meta.url);
  } else {
    imgUrl = new URL('@/assets/images/animal/animal_group.png', import.meta.url);
  }
  return imgUrl.href;
}

const entryChat = (item: Friend) => {
  store.entryDmRoom(item)
  isOpenMenu.value = false
  openedMenuNickname.value = null

  const emailToRemove: string = item.email
  const existingNewSenders: string[] = JSON.parse(localStorage.getItem('newSender') || '[]')
  const updatedNewSenders: string[] = existingNewSenders.filter(email => email !== emailToRemove)

  localStorage.setItem('newSender', JSON.stringify(updatedNewSenders))
}

const openMenu = (nickname: string) => {
  if (isOpenMenu.value && openedMenuNickname.value == nickname) {
    isOpenMenu.value = false
  } else {
    isOpenMenu.value = true
  }
  openedMenuNickname.value = isOpenMenu.value ? nickname : null
}

const isNewSender = (email: string) => {
  const newSenders = JSON.parse(localStorage.getItem('newSender') || '[]')
  return newSenders.includes(email)
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

.buttons {
  @apply flex items-center relative;
}
.gender-icon {
  @apply text-xl;
}
.dm-button {
  @apply w-6 h-6 mb-1 text-gray-500 transform rotate-45 cursor-pointer me-2 hover:text-gray-600;
}
.menu-button {
  @apply relative w-6 h-6 text-gray-800 cursor-pointer;
}
.menu {
  @apply absolute z-10 bg-white divide-y divide-gray-100 rounded-lg shadow w-44 left-[470px];
}
</style>
