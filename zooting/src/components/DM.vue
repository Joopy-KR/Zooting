<template>
  <div class="dm__container">
    <h1>대화 목록</h1>
    {{ DmInfo }}
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useAccessTokenStore } from "../stores/store"

const store = useAccessTokenStore()

const DmInfo = ref<DM | null>(null)

watch(()=> store.DmInfo, (UpdateUser)=>{
  DmInfo.value = UpdateUser
})

interface DM {
dmRoomId: number;
dmList: DmItem[];
cursor: number;
}

interface DmItem {
dmRoomId: number;
sender: string;
message: string;
}
</script>

<style scoped>
.dm__container {
  width: 100%;
  height: 100%;
}
</style>