<template>
  <div class="recent__container">
    <SocialRecentListItem />
    <SocialRecentListItem />
    <SocialRecentListItem />
    <SocialRecentListItem />
    <SocialRecentListItem />
    <SocialRecentListItem />
    <SocialRecentListItem />
    <SocialRecentListItem />
  </div>
</template>

<script setup lang="ts">
import SocialRecentListItem from './SocialRecentListItem.vue'
</script>

<style scoped>
.recent__container {
  @apply flex-grow;
  background-color: darkorange;
  overflow-y: auto;
}
.recent__container::-webkit-scrollbar {
  width: 7px;
  background-color: white;
}
.recent__container::-webkit-scrollbar-thumb {
  background-color: #d6d6d6;
  border-radius: 4px;
}
.recent__container::-webkit-scrollbar-track {
  background-color: transparent;
}
</style>