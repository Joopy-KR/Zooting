<template>
  <div class="video__container">
    <VideoChatTalk class="left-component"/>
    <!-- <VideoChatCatchMind class="left-component"/> -->
    <VideoChatSideBarVue class="right-component"/> 
  </div>
</template>

<script setup lang="ts">
// 자유대화 페이지
import VideoChatTalk from '@/components/video-chat/VideoChatTalk.vue'
// 캐치마인드 페이지
import VideoChatCatchMind from '@/components/video-chat/VideoChatCatchMind.vue'
// 사이드바
import VideoChatSideBarVue from '@/components/video-chat/VideoChatSideBar.vue'


</script>

<style scoped>
.video__container {
  @apply flex w-screen h-screen;
  min-width: 1420px;
}

.left-component {
  @apply flex-grow;
}

.right-component {
  width: 380px;
}
</style>