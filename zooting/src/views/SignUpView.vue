<template>
    <div class="signup-container">
        <InfoInput/>
    </div>
</template>

<script setup lang="ts">
import InfoInput from '../components/signup/InfoInput.vue'
</script>

<style scoped>
.signup-container {
    @apply flex flex-col md:h-screen w-full p-6 bg-slate-100;
    overflow-y: auto;
}
</style>