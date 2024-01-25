<template>
  <div class="friend__container">
    <div>
      <p>친구</p>
      <p>검색창</p>
    </div>
    <div class="item__container" v-for="(item, index) in friendList" :key="index">
      <RouterLink :to="getProfileLink(item.nickname)" class="flex items-center gap-4">
        <img class="w-10 h-10 rounded-full" src="" alt="profile">
        <div class="font-medium">
          <div>{{ item.nickname }}</div>
          <div class="text-sm text-gray-500">Content</div>
        </div>
      </RouterLink>
      <div class="flex">
        <button class="px-3 border border-gray-500">삭제</button>
      </div>
    </div>
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
</script>

<style scoped>
.friend__container {
  @apply flex-grow;
  overflow-y: auto;
}
.item__container {
  @apply flex bg-white justify-between py-3 px-5 m-1 border border-gray-600;
  height: 70px;
}
.friend__container::-webkit-scrollbar {
  width: 7px;
  background-color: white;
}
.friend__container::-webkit-scrollbar-thumb {
  background-color: #d6d6d6;
  border-radius: 4px;
}
.friend__container::-webkit-scrollbar-track {
  background-color: transparent;
}
</style>