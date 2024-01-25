<template>
  <div class="friend__container">
    받은 요청
    <div class="item__container" v-for="(item, index) in requestFromList" :key="index">
      <div class="flex items-center gap-4">
        <img class="w-10 h-10 rounded-full" src="" alt="profile">
        <div class="font-medium dark:text-white">
          <div>{{ item.nickname }}</div>
          <div class="text-sm text-gray-500">content</div>
        </div>
      </div>
      <div class="flex">
        <button>버튼</button>
      </div>
    </div>
    {{ getRequestFromList }}
    보낸 요청
    <div class="item__container" v-for="(item, index) in requestToList" :key="index">
      <div class="flex items-center gap-4">
        <img class="w-10 h-10 rounded-full" src="" alt="profile">
        <div class="font-medium dark:text-white">
          <div>{{ item.nickname }}</div>
          <div class="text-sm text-gray-500">content</div>
        </div>
      </div>
      <div class="flex">
        <button>버튼</button>
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