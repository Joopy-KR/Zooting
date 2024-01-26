<!-- Search.vue -->
<template>
  <div class="search__container">
    <div class="search__input-container">
      
      <div @click="toggleSearchType" class="absolute z-10 cursor-pointer ms-3">
        <!-- 전체 유저 중에서 검색 (친구일 경우 친구 신청 버튼 X) -->
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
          v-model="searchQuery"
          placeholder="Search"
          class="w-full search__input"
          @keyup.enter="handleSearch"
          maxlength="16"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useAccessTokenStore } from '@/stores/store'

const store = useAccessTokenStore()

const searchQuery = ref<string>('')
const isGlobal = ref<boolean>(true)

const toggleSearchType = () => {
  isGlobal.value = !isGlobal.value
}

// const searchResult = ref<
const handleSearch = () => {
  if (isGlobal) {
    store.userSearch()
  } else {
    store.friendSearch()
  }
}

</script>

<style scoped>
.search__container {
  /* min-height: 50px; */
}

.search__input-container {
  @apply mt-3 mb-1 mx-3;
  display: flex;
  align-items: center;
}

.search__dropdown {
  padding: 5px;
  border-radius: 4px;
  font-size: 14px;
}

.search__input {
  padding: 10px 18px 10px 45px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 30px;
  font-size: 14px;
}
</style>
