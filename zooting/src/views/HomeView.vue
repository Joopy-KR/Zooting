<template>
    <div>
        <h1>Home</h1>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAccessTokenStore } from "../stores/store"

const store = useAccessTokenStore()

onMounted(async () => {
    try {
        store.checkCompletedSignUp()
    } catch (error) {
        console.error(error)
    }
})
</script>

<style scoped>

</style>