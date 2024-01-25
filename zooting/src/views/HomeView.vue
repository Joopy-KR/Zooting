<template>
  <div class="home__container">
    <Social />
    <Ready />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useAccessTokenStore } from "../stores/store"
import Social from '../components/home/Social.vue'
import Ready from '../components/home/Ready.vue'

const store = useAccessTokenStore()

const userInfo = ref(store.userInfo)

watch(()=> store.userInfo, (UpdateUser)=>{
  userInfo.value = UpdateUser
})

onMounted(async () => {
  // if (!store.isCompletedSignUp) {
  //     store.checkCompletedSignUp()
  // }
  store.getUserInfo()
})
</script>

<style scoped>
.home__container {
  @apply h-screen flex flex-row w-full;
}
</style>