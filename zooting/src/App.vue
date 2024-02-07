<template>
  <div class="flex">
    <TheSideBar
      @currnet-dm-room-id="currentDmRoomId"
      :dm-req = "dmReq"
    />
    <RouterView 
      class="ms-14"
      :dm-room-id="dmRoomId"
      @receive-message="receiveMessage"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { RouterView } from 'vue-router'
import TheSideBar from '@/components/TheSideBar.vue'

const dmRoomId = ref<number>(0)
const dmReq = ref<any>(null)

const currentDmRoomId = (dmRoomid: number) => {
  dmRoomId.value = dmRoomid
}

const receiveMessage = (req: any) => {
  dmReq.value = req
}
</script>

<style scoped>
</style>
