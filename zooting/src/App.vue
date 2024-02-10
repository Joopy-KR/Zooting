<template>
  <div class="flex">
    <TheSideBar
      v-if="store.isLogin"
      @current-dm-room-id="currentDmRoomId"
      :dm-res = "dmRes"
    />
    <RouterView
      class="ms-14"
      :dm-room-id="dmRoomId"
      @receive-message="receiveMessage"
    />
    <MatchingCompleteModal
      class="z-40"
      />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { RouterView } from 'vue-router'
import TheSideBar from '@/components/TheSideBar.vue'
import MatchingCompleteModal from "@/components/MatchingCompleteModal.vue"
import { useAccessTokenStore } from './stores/store'

const store = useAccessTokenStore()
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
