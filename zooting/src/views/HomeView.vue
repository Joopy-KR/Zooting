<template>
  <div class="home__container">
    <Social />
    <Ready />
    <MatchingCompleteModal v-if="isMatchingComplete" class="z-40"/>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useAccessTokenStore } from "../stores/store"
import Social from '../components/home/Social.vue'
import Ready from '../components/home/Ready.vue'
import MatchingCompleteModal from "@/components/home/MatchingCompleteModal.vue";

const store = useAccessTokenStore()

const userInfo = ref(store.userInfo)

// 매칭이 된 경우
const isMatchingComplete = ref(true)

watch(()=> store.userInfo, (UpdateUser)=>{
  userInfo.value = UpdateUser
})

onMounted(async () => {
  if (!store.isCompletedSignUp) {
      store.checkCompletedSignUp()
  }
  store.getUserInfo()
})
</script>

<style scoped>
.home__container {
  @apply h-screen flex flex-row w-full;
}
</style>