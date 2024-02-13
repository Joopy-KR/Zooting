<template>
  <div class="search-result" @scroll="handleSearchScroll" ref="resultRef">
    <div v-show="searchResult?.searchResList.length === 0" class="flex justify-center pt-3">
      <h1>검색 결과가 없습니다</h1>
    </div>
    <ul>
      <li v-for="result in searchResult?.searchResList" :key="result.nickname">
        <RouterLink :to="getProfileLink(result.nickname)" class="search-result__item">
          {{ result.nickname }}
          <svg :class="getHeartClass(result.gender)" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
            <path d="m12.7 20.7 6.2-7.1c2.7-3 2.6-6.5.8-8.7A5 5 0 0 0 16 3c-1.3 0-2.7.4-4 1.4A6.3 6.3 0 0 0 8 3a5 5 0 0 0-3.7 1.9c-1.8 2.2-2 5.8.8 8.7l6.2 7a1 1 0 0 0 1.4 0"/>
          </svg>
        </RouterLink>
          <button v-show="!isRequestInList(result.nickname) && !isFriendInList(result.nickname) && !(result.nickname === store.userInfo?.nickname)" @click="sendFriendRequest(result.nickname)">
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
              stroke-width="1.3"
              d="M16 12h4m-2 2v-4M4 18v-1a3 3 0 0 1 3-3h4a3 3 0 0 1 3 3v1c0 .6-.4 1-1 1H5a1 1 0 0 1-1-1Zm8-10a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"
            />
          </svg>
        </button>
        <div v-show="result.nickname === store.userInfo?.nickname" class="text-gray-500 me-1">
          me
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useAccessTokenStore } from '@/stores/store'
import type { Search } from "@/types/global"

const store = useAccessTokenStore()
const searchResult = ref<Search | null>(store.searchResult)
const resultRef = ref<HTMLElement | null>(null)

const props = defineProps<{
  searchQuery: string
}>()

watch(()=> store.searchResult, (UpdateList)=>{
  searchResult.value = UpdateList
})

const sendFriendRequest = (nickname: string) => {
  store.friendRequest(nickname)
}

const isRequestInList = (nickname: string) => {
  return store.requestToList.some(item => item.nickname === nickname) || store.requestFromList.some(item => item.nickname === nickname)
}

const isFriendInList = (nickname: string) => {
  return store.friendList.some(item => item.nickname === nickname)
}

const getProfileLink = (value: string) => `/profile/${value}`

const getHeartClass = (gender: string) => (
  gender === 'man' ? 'w-4 h-4 text-blue-500 ms-1' : 'w-4 h-4 text-pink-500 ms-1'
)

const handleSearchScroll = () => {
  if (resultRef.value && searchResult.value) {
    const threshold = 0.8
    const isAtBottom = Math.abs(resultRef.value.scrollHeight - resultRef.value.clientHeight - resultRef.value.scrollTop) < threshold
    if (isAtBottom && searchResult.value.currentPage + 1 < searchResult.value.totalPage) {
      refreshSearch()
    }
  }
}

const refreshSearch = () => {
  if (searchResult.value) {
    if (props.searchQuery) {
      store.userSearch({page:searchResult.value.currentPage + 1, size:12, sort:[], nickname:props.searchQuery})
    } else {
      store.userSearch({page:searchResult.value.currentPage + 1, size:12, sort:[]})
    }
  }
}
</script>

<style scoped>
.search-result {
  @apply px-5 py-3;
  overflow-y: auto; 
}
.search-result::-webkit-scrollbar {
  width: 6px;
  background-color: white;
}
.search-result::-webkit-scrollbar-thumb {
  background-color: #d6d6d6;
  border-radius: 4px;
}
.search-result::-webkit-scrollbar-track {
  background-color: transparent;
}
.search-result ul {
  @apply divide-y divide-gray-200 px-1;
}
.search-result li {
  @apply flex justify-between items-center h-20 px-3 hover:bg-gray-50 cursor-pointer;
}
.search-result__item {
  @apply flex items-center h-full flex-grow;
}
</style>