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
      :is-matching-complete="isMatchingComplete"
      @receive-message="receiveMessage"
      @match-complete="matchingComplete"
    />
    <MatchingCompleteModal
      class="z-40"
      :room-id="videoRoomId"
      :is-matching-complete="isMatchingComplete"
      @close-modal="closeModal"
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
const videoRoomId = ref<string>('')
const isMatchingComplete = ref<boolean>(false)

const currentDmRoomId = (id: number) => {
  dmRoomId.value = id
}

const receiveMessage = (req: any) => {
  dmRes.value = req
}

const matchingComplete = (id: string) => {
  videoRoomId.value = id
  isMatchingComplete.value = true
}

const closeModal = () => {
  isMatchingComplete.value = false
}
</script>

<style scoped>
</style>
