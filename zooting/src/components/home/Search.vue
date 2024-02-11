<!-- Search.vue -->
<template>
  <div class="search__container">
    <div class="search__input-container">
      
      <div @click="toggleSearchType" class="search__toggle">
        <!-- 전체 유저 중에서 검색 -->
        <svg 
          v-if="isGlobal"
          class="text-gray-500 w-7 h-7 me-1 hover:text-gray-700" 
          aria-hidden="true" 
          xmlns="http://www.w3.org/2000/svg" 
          fill="none" 
          viewBox="0 0 24 24"
        >
          <title>전체 검색</title>
          <path 
            stroke="currentColor" 
            stroke-linecap="round"
            stroke-width="1.5" 
            d="M4.4 7.7c2 .5 2.4 2.8 3.2 3.8 1 1.4 2 1.3 3.2 2.7 1.8 2.3 1.3 5.7 1.3 6.7M20 15h-1a4 4 0 0 0-4 4v1M8.6 4c0 .8.1 1.9 1.5 2.6 1.4.7 3 .3 3 2.3 0 .3 0 2 1.9 2 2 0 2-1.7 2-2 0-.6.5-.9 1.2-.9H20m1 4a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"
          />
        </svg>

        <!-- 내 친구 내에서 검색 -->
        <svg 
          v-if="!isGlobal"
          class="text-gray-500 w-7 h-7 me-1 hover:text-gray-700" 
          aria-hidden="true" 
          xmlns="http://www.w3.org/2000/svg" 
          fill="none" 
          viewBox="0 0 24 24"
        >
          <title>친구 검색</title>
          <path 
            stroke="currentColor" 
            stroke-linecap="round" 
            stroke-width="1.5" 
            d="M4.5 17H4a1 1 0 0 1-1-1 3 3 0 0 1 3-3h1m0-3a2.5 2.5 0 1 1 2-4.5M19.5 17h.5c.6 0 1-.4 1-1a3 3 0 0 0-3-3h-1m0-3a2.5 2.5 0 1 0-2-4.5m.5 13.5h-7a1 1 0 0 1-1-1 3 3 0 0 1 3-3h3a3 3 0 0 1 3 3c0 .6-.4 1-1 1Zm-1-9.5a2.5 2.5 0 1 1-5 0 2.5 2.5 0 0 1 5 0Z"
          />
        </svg>
      </div>

      <!-- Search bar -->
      <div class="relative flex-1">
        <input
          :value="searchQuery"
          placeholder="Search"
          class="search__input"
          @input="handleSearch"
          maxlength="16"
        />

        <!-- Cancel (x) button -->
        <div v-if="searchQuery" @click="cancelSearch" class="search__cancel">
          <svg
            class="w-5 h-5 text-gray-500 cursor-pointer hover:text-gray-700"
            aria-hidden="true"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
          >
            <title>검색 취소</title>
            <path
              stroke="currentColor"
              stroke-linecap="round"
              stroke-width="1.5"
              d="M6 18L18 6M6 6l12 12"
            />
          </svg>
        </div>

        <!-- 검색 결과 표시 -->
        <div v-if="searchQuery" class="search__result">
          <ul v-if="searchResult?.length > 0">
            <li v-for="result in searchResult" :key="result.nickname">
              <div class="flex items-center">
                <RouterLink :to="getProfileLink(result.nickname)" class="flex items-center gap-4">
                  <img class="search__result__img" :src="getProfileImage(result.animal)" alt="profile">
                  {{ result.nickname }}
                </RouterLink>
                <div>
                  <svg :class="getHeartClass(result.gender)" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                    <path d="m12.7 20.7 6.2-7.1c2.7-3 2.6-6.5.8-8.7A5 5 0 0 0 16 3c-1.3 0-2.7.4-4 1.4A6.3 6.3 0 0 0 8 3a5 5 0 0 0-3.7 1.9c-1.8 2.2-2 5.8.8 8.7l6.2 7a1 1 0 0 0 1.4 0"/>
                  </svg>
                </div>
              </div>
              <button v-show="!isRequestInList(result) && !isFriendInList(result) && !(result.nickname===store.userInfo?.nickname)" @click="sendFriendRequest(result.nickname)">
                <!-- 친구 신청 아이콘 -->
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
              </div>
            </li>
          </ul>
          <div v-else class="flex justify-center h-10 mt-1">
            <p>일치하는 닉네임이 없습니다</p>
          </div>
        </div>
        
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useAccessTokenStore } from '@/stores/store'

const store = useAccessTokenStore()

const searchQuery = ref<string>('')
const isGlobal = ref<boolean>(true)

const toggleSearchType = () => {
  isGlobal.value = !isGlobal.value
  if (searchQuery.value) {
    if (isGlobal.value) {
      store.userSearch(searchQuery.value)
    } else {
      store.friendSearch(searchQuery.value)
    }
  }
}

const handleSearch = function (event: any) {
  searchQuery.value = event.target.value
  if (searchQuery.value) {
    if (isGlobal.value) {
      store.userSearch(searchQuery.value)
    } else {
      store.friendSearch(searchQuery.value)
    }
  }
}

const searchResult = ref(store.searchResult)

watch(()=> store.searchResult, (UpdateList)=>{
  searchResult.value = UpdateList
})

const cancelSearch = () => {
  searchQuery.value = ''
  store.searchResult = []
}

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

const getProfileImage = (animal: string) => {
  let imgUrl: URL;
  if (animal === '강아지') {
    imgUrl = new URL('/assets/images/animal/dog.png', import.meta.url);
  } else if (animal === '고양이') {
    imgUrl = new URL('/assets/images/animal/cat.png', import.meta.url);
  } else if (animal === '곰') {
    imgUrl = new URL('/assets/images/animal/bear.png', import.meta.url);
  } else if (animal === '공룡') {
    imgUrl = new URL('/assets/images/animal/dino.png', import.meta.url);
  } else if (animal === '펭귄') {
    imgUrl = new URL('/assets/images/animal/penguin.png', import.meta.url);
  } else if (animal === '토끼') {
    imgUrl = new URL('/assets/images/animal/rabbit.png', import.meta.url);
  } else if (animal === '사슴') {
    imgUrl = new URL('/assets/images/animal/deer.png', import.meta.url);
  } else {
    imgUrl = new URL('/assets/images/animal/animal_group.png', import.meta.url);
  }
  return imgUrl.href;
}

const getHeartClass = (gender: string) => (
  gender === 'man' ? 'w-4 h-4 text-blue-500 ms-1' : 'w-4 h-4 text-pink-500 ms-1'
)
</script>

<style scoped>
.search__input-container {
  @apply mt-3 mb-1 mx-3 flex items-center;
}
.search__input {
  @apply border border-gray-300 shadow-sm rounded-3xl text-sm w-full;  
  padding: 10px 18px 10px 45px;
}
.search__toggle {
  @apply absolute z-10 cursor-pointer ms-3;
}
.search__cancel {
  @apply absolute z-10 right-4 top-1/2 transform -translate-y-1/2;
}
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
