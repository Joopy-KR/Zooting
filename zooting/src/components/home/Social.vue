<template>
  <div class="social__container">
    <Search />
    <SocialTabBar 
      :tabs="tabs"
      @select-tab="handleTabSelected"
    />
    <component :is="currentList"/>
    recent list
    <SocialRecentList />
  </div>
</template>

<script setup lang="ts">
import { ref, shallowRef, onMounted, computed } from 'vue'
import { useAccessTokenStore } from '@/stores/store'
import Search from '../Search.vue'
import SocialTabBar from './SocialTabBar.vue'
import SocialFriendList from './SocialFriendList.vue'
import SocialRequestList from './SocialRequestList.vue'
import SocialBlockList from './SocialBlockList.vue'
import SocialRecentList from './SocialRecentList.vue'

const store = useAccessTokenStore()

const tabs = ref<{name: string, count: any}[]>([
  { name: '친구', count: 0 },
  { name: '친구 요청', count: 0 },
  { name: '차단', count: 0 },
])

onMounted(async () => {
  store.getFriendList()
  store.getRequestFromList()
  store.getRequestToList()
})

tabs.value[0].count = computed(() => {
  return store.friendList?.length
})

tabs.value[1].count = computed(() => {
  return store.requestFromList?.length
})

// tabs.value[2].count = computed(() => {
//   return store.BlockList?.length
// })

const currentList = shallowRef(SocialFriendList)

const handleTabSelected = (currentTab: string) => {
  console.log(currentTab)
  if (currentTab === '친구') {
    currentList.value = SocialFriendList
  } else if (currentTab === '친구 요청') {
    currentList.value = SocialRequestList
  } else {
    currentList.value = SocialBlockList
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