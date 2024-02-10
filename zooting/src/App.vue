<template>
  <div class="flex">
    <TheSideBar
      @current-dm-room-id="currentDmRoomId"
      :dm-res = "dmRes"
    />
    <RouterView
      class="ms-14"
      :dm-room-id="dmRoomId"
      @receive-message="receiveMessage"
    />
    <!-- <testovvideo class="ms-14" /> -->
    <!-- <VideoChatView class="ms-14"/> -->
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { RouterView } from 'vue-router'
import TheSideBar from '@/components/TheSideBar.vue'
import testovvideo from '@/components/video-chat/testovvideo.vue'
import VideoChatView from './views/VideoChatView.vue'

const dmRoomId = ref<number>(0)
const dmRes = ref<any>(null)

const currentDmRoomId = (id: number) => {
  dmRoomId.value = id
}

const receiveMessage = (req: any) => {
  dmRes.value = req
}
</script>

<style scoped>
</style>
