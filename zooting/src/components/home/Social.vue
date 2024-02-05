<template>
  <div class="social__container">
    <Search />
    <SocialTabBar 
      :tabs="tabs"
      @select-tab="handleTabSelected"
    />
    <component 
      :is="currentList"
    />
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
  store.getBlockList()
})

tabs.value[0].count = computed(() => {
  return store.friendList?.length
})

tabs.value[1].count = computed(() => {
  return store.requestFromList?.length
})

tabs.value[2].count = computed(() => {
  return store.blockList?.length
})

const currentList = shallowRef<any>(SocialFriendList)

const handleTabSelected = (currentTab: string) => {
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
  @apply flex flex-col h-screen;
  border-right: 1.5px rgb(205, 205, 205) solid;
  min-width: 430px;
}
</style>