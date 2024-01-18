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
    @apply flex flex-col h-screen p-8 bg-slate-100;
}
</style>