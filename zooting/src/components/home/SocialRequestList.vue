<template>
  <div class="friend__container">
    받은 요청
    <div class="item__container" v-for="(item, index) in requestFromList" :key="index">
      <RouterLink :to="getProfileLink(item.nickname)" class="flex items-center gap-4">
        <img class="w-10 h-10 rounded-full" src="" alt="profile">
        <div class="font-medium">
          <div>{{ item.nickname }}</div>
          <div class="text-sm text-gray-500">Content</div>
        </div>
      </RouterLink>
      <div class="flex">
        <button class="px-3 border border-gray-500 me-2" @click="friendAccept(item)">수락</button>
        <button class="px-3 border border-gray-500" @click="friendReject(item)">거절</button>
      </div>
    </div>
    보낸 요청
    <div class="item__container" v-for="(item, index) in requestToList" :key="index">
      <RouterLink :to="getProfileLink(item.nickname)" class="flex items-center gap-4">
        <img class="w-10 h-10 rounded-full" src="" alt="profile">
        <div class="font-medium">
          <div>{{ item.nickname }}</div>
          <div class="text-sm text-gray-500">content</div>
        </div>
      </RouterLink>
      <div class="flex">
        <button class="px-3 border border-gray-500" @click="friendRequestCancel(item)">취소</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useAccessTokenStore } from '@/stores/store'

const store = useAccessTokenStore()

const requestFromList = ref(store.requestFromList)
const requestToList = ref(store.requestToList)

watch(()=> store.requestFromList, (UpdateList)=>{
  requestFromList.value = UpdateList
})

watch(()=> store.requestToList, (UpdateList)=>{
  requestToList.value = UpdateList
})

const getProfileLink = (value: string) => {
  return `/profile/${value}`
}

interface Friend {
  email: string;
  nickname: string;
}

const friendAccept = (item: Friend) => {
  const params = {
    email: item.email,
    nickname: item.nickname,
  }
  store.friendAccept(params)
}

const friendReject = (item: Friend) => {
  const params = {
    email: item.email,
    nickname: item.nickname,
  }
  store.friendReject(params)
}

const friendRequestCancel = (item: Friend) => {
  const params = {
    email: item.email,
    nickname: item.nickname,
  }
  store.friendRequestCancel(params)
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