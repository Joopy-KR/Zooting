<template>
  <div class="flex">
    <TheSideBar
      @current-dm-room-id="currentDmRoomId"
      :dm-req = "dmReq"
    />
    <RouterView
      class="ms-14"
      :dm-room-id="dmRoomId"
      @receive-message="receiveMessage"
    />
    <!-- <VideoChatView class="ms-14"/> -->
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { RouterView } from 'vue-router'
import TheSideBar from '@/components/TheSideBar.vue'
import VideoChatView from './views/VideoChatView.vue'

const dmRoomId = ref<number>(0)
const dmReq = ref<any>(null)

const currentDmRoomId = (id: number) => {
  dmRoomId.value = id
}

const receiveMessage = (req: any) => {
  dmReq.value = req
}
</script>

<style scoped>
</style>
