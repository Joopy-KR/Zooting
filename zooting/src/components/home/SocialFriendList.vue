<template>
  <div class="friend__container">
    <ul role="list" class="friend-list">
      <li v-for="(item, index) in friendList" :key="index" class="friend-list__item">
        <RouterLink :to="getProfileLink(item.nickname)" class="friend-list__item__link">
          <div class="flex items-center gap-4">
            <img class="friend-list__img" src="" alt="profile">
            <div class="font-medium">
              <div class="flex items-center">
                {{ item.nickname }}
                <div class="gender-icon">
                  <svg :class="getHeartClass(item.gender)" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                    <path d="m12.7 20.7 6.2-7.1c2.7-3 2.6-6.5.8-8.7A5 5 0 0 0 16 3c-1.3 0-2.7.4-4 1.4A6.3 6.3 0 0 0 8 3a5 5 0 0 0-3.7 1.9c-1.8 2.2-2 5.8.8 8.7l6.2 7a1 1 0 0 0 1.4 0"/>
                  </svg>
                </div>
              </div>
              <div class="friend-list__content">Content</div>
            </div>
          </div>
        </RouterLink>
        <div class="flex items-center">
          <button @click="friendDelete(item)">삭제</button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useAccessTokenStore } from '@/stores/store'
import { RouterLink } from 'vue-router'

const store = useAccessTokenStore()

const friendList = ref(store.friendList)

watch(()=> store.friendList, (UpdateList)=>{
  friendList.value = UpdateList
})

const getProfileLink = (value: string) => {
  return `/profile/${value}`
}

interface Friend {
  email: string;
  nickname: string;
  gender: string;
  animal: string;
};

const friendDelete = (item: Friend) => {
  const payload = {
    nickname: item.nickname,
    email: item.email,
  }
  store.friendDelete(payload)
} 

const getHeartClass = (gender: string) => {
  return gender === 'man' ? 'w-4 h-4 text-blue-500 ms-1' : 'w-4 h-4 text-pink-500 ms-1';
}
</script>

<style scoped>
.friend__container {
  @apply flex-grow;
  overflow-y: auto;
}
.friend__container::-webkit-scrollbar {
  width: 6px;
  background-color: white;
}
.friend__container::-webkit-scrollbar-thumb {
  background-color: #d6d6d6;
  border-radius: 4px;
}
.friend__container::-webkit-scrollbar-track {
  background-color: transparent;
}
.friend-list {
  @apply m-1 divide-y divide-gray-200;
}
.friend-list__item {
  @apply flex justify-between px-6 py-4;
}
.friend-list__item__link {
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
</style>
