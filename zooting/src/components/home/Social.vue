<template>
  <div class="social__container">
    <Search />
    <SocialTabBar 
      @select-tab="handleTabSelected"
    />
    <!-- <component :is="currnetList"/> -->
    recent list
    <SocialRecentList />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useAccessTokenStore } from '@/stores/store'
import Search from '../Search.vue'
import SocialTabBar from './SocialTabBar.vue'
import SocialFriendList from './SocialFriendList.vue'
import SocialRequestList from './SocialRequestList.vue'
import SocialBlockList from './SocialBlockList.vue'
import SocialRecentList from './SocialRecentList.vue'
import type { JSX } from 'vue/jsx-runtime'

const store = useAccessTokenStore()

// const friendList = ref(store.friendList)
// const requestFromList = ref(store.friendList)
// const requestToList = ref(store.friendList)

// watch(()=> store.friendList, (UpdateList)=>{
//   friendList.value = UpdateList
// })

// watch(()=> store.requestFromList, (UpdateList)=>{
//   requestFromList.value = UpdateList
// })

// watch(()=> store.requestToList, (UpdateList)=>{
//   requestToList.value = UpdateList
// })

onMounted(async () => {
  store.getFriendList()
  store.getRequestFromList()
  store.getRequestToList()
})

const tabs = ref<{name: string, current: boolean}[]>([
  { name: '친구', current: true },
  { name: '친구 요청', current: false },
  { name: '차단', current: false },
])

const handleTabSelected = (currentTab: string) => {
  console.log(currentTab)
  if (currentTab === '친구') {
    
  } else if (currentTab === '친구 요청') {

  } else {

  }
}
</script>

<style scoped>
.social__container {
  @apply flex flex-col;
  min-width: 430px;
  border: 1px solid black;
}
</style>