<template>
  <div class="friend__container">
    <SocialFriendListItem />
    <SocialFriendListItem />
    <SocialFriendListItem />
    <SocialFriendListItem />
    <SocialFriendListItem />
    <SocialFriendListItem />
    <SocialFriendListItem />
    <SocialFriendListItem />
  </div>
</template>

<script setup lang="ts">
import SocialFriendListItem from './SocialFriendListItem.vue'
</script>

<style scoped>
.friend__container {
  @apply flex-grow;
  background-color: darkkhaki;
  overflow-y: auto;
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