<template>
    <div>
        <h1>Home</h1>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAccessTokenStore } from "../stores/store"
import { useRouter } from "vue-router"

const store = useAccessTokenStore()
const router = useRouter()

onMounted(async () => {
    try {
        store.checkCompletedSignUp()
        if (!store.isNickname) {
            router.push({ name: "signup" })
        }
    } catch (error) {
        console.error(error)
    }
})
</script>

<style scoped>

</style>