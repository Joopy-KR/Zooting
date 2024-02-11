<template>
  <div>
          <ul>
            <li v-for="result in searchResult?.searchResList" :key="result.nickname">
              <div class="flex items-center">
                <RouterLink :to="getProfileLink(result.nickname)" class="flex items-center gap-4">
                  {{ result.nickname }}
                </RouterLink>
                <div>
                  <svg :class="getHeartClass(result.gender)" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                    <path d="m12.7 20.7 6.2-7.1c2.7-3 2.6-6.5.8-8.7A5 5 0 0 0 16 3c-1.3 0-2.7.4-4 1.4A6.3 6.3 0 0 0 8 3a5 5 0 0 0-3.7 1.9c-1.8 2.2-2 5.8.8 8.7l6.2 7a1 1 0 0 0 1.4 0"/>
                  </svg>
                </div>
              </div>
              <!-- <button v-show="!isRequestInList(result) && !isFriendInList(result) && !(result.nickname===store.userInfo?.nickname)" @click="sendFriendRequest(result.nickname)">
                <svg
                  class="text-gray-500 cursor-pointer w-7 h-7 hover:text-gray-700 "
                  aria-hidden="true"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                >
                  <title>친구 신청</title>
                  <path
                    stroke="currentColor"
                    stroke-linecap="round"
                    stroke-width="1.4"
                    d="M16 12h4m-2 2v-4M4 18v-1a3 3 0 0 1 3-3h4a3 3 0 0 1 3 3v1c0 .6-.4 1-1 1H5a1 1 0 0 1-1-1Zm8-10a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"
                  />
                </svg>
              </button>
              <div v-show="result.nickname === store.userInfo?.nickname" class="text-gray-500 me-1">
                me
              </div> -->
            </li>
          </ul>
          <!-- <div v-else class="flex justify-center h-10 mt-1">
            <p>일치하는 닉네임이 없습니다</p>
          </div> -->
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useAccessTokenStore } from '@/stores/store'
import type { Search } from "@/types/global"

const store = useAccessTokenStore()

const searchResult = ref<Search | null>(store.searchResult)

watch(()=> store.searchResult, (UpdateList)=>{
  searchResult.value = UpdateList
})

interface Result {
  email: string
  nickname: string
  gender: string
  animal: string
}

const sendFriendRequest = (nickname: string) => {
  store.friendRequest(nickname)
}

const isRequestInList = (result: Result) => {
  return store.requestToList.some(item => item.nickname === result.nickname) || store.requestFromList.some(item => item.nickname === result.nickname)
}

const isFriendInList = (result: Result) => {
  return store.friendList.some(item => item.nickname === result.nickname)
}

const getProfileLink = (value: string) => {
  return `/profile/${value}`
}

const getHeartClass = (gender: string) => (
  gender === 'man' ? 'w-4 h-4 text-blue-500 ms-1' : 'w-4 h-4 text-pink-500 ms-1'
)
</script>

<style scoped>

.search__result {
  @apply absolute z-10 mt-1 w-full max-h-[50vh] bg-white border border-gray-300 rounded-xl shadow-md;
  overflow-y: auto; 
}
.search__result__img {
  @apply w-10 h-10 rounded-full;
}
.search__result::-webkit-scrollbar {
  @apply hidden;
}
.search__result ul {
  @apply divide-y divide-gray-200 px-1;
}
.search__result li {
  @apply p-2 flex justify-between items-center h-16;
}
</style>