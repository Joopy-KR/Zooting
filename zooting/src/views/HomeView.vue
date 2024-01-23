<template>
    <div>
        {{ store.userInfo }}
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAccessTokenStore } from "../stores/store"

const store = useAccessTokenStore()
const user = ref(store.userInfo)

// onMounted(async () => {
//     try {
//         // const userInfo = await store.getUserInfo()
//         store.getUserInfo()
//         console.log('yes')
//         console.log(store.userInfo)
//     } catch (error) {
//         console.error('Noooooooo')
//     }
// })
</script>

<style scoped>

</style>