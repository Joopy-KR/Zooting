<template>
  <div class="interest__container">
    <ul role="list" class="friend-list">
      <li v-for="(item, index) in blockList" :key="index" class="friend-list__item">
        <RouterLink :to="getProfileLink(item.nickname)" class="friend-list__item__link">
          <img class="friend-list__img" src="" alt="profile">
          <div class="font-medium">
            {{ item.nickname }}
          </div>
        </RouterLink>
        <div class="flex items-center">
          <button @click="blockCancel(item.nickname)">해제</button>
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

const blockList = ref(store.blockList)

watch(()=> store.blockList, (UpdateList)=>{
  blockList.value = UpdateList
})

const getProfileLink = (value: string) => {
  return `/profile/${value}`
}


const blockCancel = (nickname: string) => {
  const payload = nickname
  store.blockCancel(payload);
}
</script>

<style scoped>
.interest__container {
  @apply flex-grow-0;
  overflow-y: auto;
}
.interest__container::-webkit-scrollbar {
  width: 7px;
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