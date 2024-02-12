<template>
  <div class="search__container">
    <div class="search__input-container">
      <!-- Search bar -->
      <div class="relative flex-1">
        <input
          :value="searchQuery"
          placeholder="Search"
          class="search__input"
          @input="handleSearch"
          @click="startSearch"
          maxlength="16"
        />
        
        <!-- Search icon -->
        <div v-show="!isSearching" class="search__cancel" @click="startSearch">
          <svg class="w-5 h-5 text-[#A297F6] cursor-pointer hover:text-[#8072EF]" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <path stroke="currentColor" stroke-linecap="round" stroke-width="2.5" d="m21 21-3.5-3.5M17 10a7 7 0 1 1-14 0 7 7 0 0 1 14 0Z"/>
          </svg>
        </div>

        <!-- Cancel (x) button -->
        <div v-show="isSearching" class="search__cancel" @click="cancelSearch">
          <svg class="w-5 h-5 text-[#A297F6] cursor-pointer hover:text-[#8072EF]"  aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <title>검색 취소</title>
            <path stroke="currentColor" stroke-linecap="round" stroke-width="2.5" d="M6 18L18 6M6 6l12 12"/>
          </svg>
        </div>
      </div>
    </div>
    
    <div v-show="isSearching && !searchQuery" class="flex justify-center pt-5 pb-2 text-xl font-semibold text-[#A297F6]">
      <h1>당신과의 인연을 기다리고 있어요</h1>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useAccessTokenStore } from '@/stores/store'

const store = useAccessTokenStore()
const emit = defineEmits(['searchState', 'searchQuery'])

const isSearching = ref<boolean>(false)
const searchQuery = ref<string>('')

const startSearch = () => {
  if (!isSearching.value) {
    isSearching.value = true
    emit('searchState', isSearching.value)
    store.userSearch({page:0, size:12, sort:[], nickname:searchQuery.value})
  }
}

const cancelSearch = () => {
  isSearching.value = false
  searchQuery.value = ''
  store.searchResult = null
  emit('searchState', isSearching.value)
}

const handleSearch = function (event: any) {
  searchQuery.value = event.target.value
  if (searchQuery.value) {
    store.userSearch({page:0, size:12, sort:[], nickname:searchQuery.value})
  } else {
    store.userSearch({page:0, size:12, sort:[]})
  }
  emit('searchQuery', searchQuery.value)
}
</script>

<style scoped>
.search__input-container {
  @apply mt-3 mb-1 mx-3 flex items-center;
}
.search__input {
  @apply border-2 border-[#A297F6] shadow-sm rounded-3xl w-full py-2 px-5;  
}
.search__cancel {
  @apply absolute z-10 right-4 top-1/2 transform -translate-y-1/2;
}
</style>
