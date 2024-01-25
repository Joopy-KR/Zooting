<template>
    <div>
        <h1>Home</h1>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useAccessTokenStore } from "../stores/store"

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

</style>