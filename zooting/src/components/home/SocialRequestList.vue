<template>
  <div class="grid friend__container">
    <!-- 받은 요청 -->
    <div>
      <!-- Title -->
      <div class="relative mt-3">
        <div class="absolute inset-0 flex items-center" aria-hidden="true">
          <div class="w-full border-t border-gray-300"></div>
        </div>
        <div class="relative flex justify-center">
          <span class="px-2 text-sm text-gray-500 bg-white">받은 요청</span>
        </div>
      </div>

      <!-- Content -->
      <div v-if="!requestFromList" class="mt-3 text-center text-gray-500">
        받은 요청이 없습니다
      </div>
      <ul role="list" class="friend-list">
        <li v-for="(item, index) in requestFromList" :key="index" class="friend-list__item">
          <RouterLink :to="getProfileLink(item.nickname)" class="friend-list__item__link">
            <img class="friend-list__img" :src="getProfileImage(item.animal)" alt="profile">
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
          </RouterLink>
          <div class="flex items-center">
            <button class="me-2" @click="friendAccept(item.nickname)">수락</button>
            <button @click="friendReject(item.nickname)">거절</button>
          </div>
        </li>
      </ul>
    </div>
    
    <!-- 보낸 요청 -->
    <div>
      <!-- Title -->
      <div class="relative">
        <div class="absolute inset-0 flex items-center" aria-hidden="true">
          <div class="w-full border-t border-gray-300"></div>
        </div>
        <div class="relative flex justify-center">
          <span class="px-2 text-sm text-gray-500 bg-white">보낸 요청</span>
        </div>
      </div>
      
      <!-- Content -->
      <div v-if="!requestToList" class="mt-3 text-center text-gray-500">
        보낸 요청이 없습니다
      </div>
      <ul role="list" class="friend-list">
        <li v-for="(item, index) in requestToList" :key="index" class="friend-list__item">
          <RouterLink :to="getProfileLink(item.nickname)" class="friend-list__item__link">
            <img class="friend-list__img" :src="getProfileImage(item.animal)" alt="profile">
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
          </RouterLink>
          <div class="flex items-center">
            <button @click="friendRequestCancel(item.nickname)">취소</button>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { useAccessTokenStore } from '@/stores/store';

const store = useAccessTokenStore();

const requestFromList = ref(store.requestFromList);
const requestToList = ref(store.requestToList);

watch(()=> store.requestFromList, (UpdateList)=>{
  requestFromList.value = UpdateList;
});

watch(()=> store.requestToList, (UpdateList)=>{
  requestToList.value = UpdateList;
});

const getProfileLink = (value: string) => {
  return `/profile/${value}`;
};

const friendAccept = (nickname: string) => {
  store.friendAccept(nickname);
};

const friendReject = (nickname: string) => {
  store.friendReject(nickname);
};

const friendRequestCancel = (nickname: string) => {
  store.friendRequestCancel(nickname);
}

const getHeartClass = (gender: string) => {
  return gender === 'man' ? 'w-4 h-4 text-blue-500 ms-1' : 'w-4 h-4 text-pink-500 ms-1';
}

const getProfileImage = (animal: string) => {
  return `/images/${animal}.png`
}
</script>

<style scoped>
.friend__container {
  @apply h-screen overflow-y-auto;
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
  @apply divide-y divide-gray-200 flex-shrink-0;
  flex-grow: 1;
}
.friend-list__item {
  @apply flex justify-between px-6 py-4;
}
.friend-list__item__link {
  @apply flex items-center gap-4;
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
</style>