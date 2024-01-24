<template>
  <div class="social__container">
    <Search />
    <SocialTabBar />
    friend list
    <SocialFriendList />
    recent list
    <SocialRecentList />
  </div>
</template>

<script setup lang="ts">
import Search from '../Search.vue'
import SocialTabBar from './SocialTabBar.vue'
import SocialFriendList from './SocialFriendList.vue'
import SocialRecentList from './SocialRecentList.vue'
</script>

<style scoped>
.social__container {
  @apply flex flex-col;
  min-width: 430px;
  background-color: aqua;
  border: 1px solid black;
}
</style>