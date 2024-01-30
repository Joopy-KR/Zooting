<template>
  <div class="dm__container">
    <h1>대화 목록</h1>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAccessTokenStore } from "../stores/store"

const store = useAccessTokenStore()

const DmInfo = ref()

onMounted(async () => {
  DmInfo.value = store.entryDmRoom()
})
</script>

<style scoped>
.dm__container {
  width: 100%;
  height: 100%;
}
</style>