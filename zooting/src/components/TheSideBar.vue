<template>
    <div>
        <nav class="side-bar">
            <div class="logo">
                <p>ZT</p>
            </div>
            <div class="side-bar__item">
                <!-- Home button (로그인 상태 => HomeView, 비로그인 상태 => logInView)-->
                <RouterLink :to="getHomeLink()">
                    <svg class="w-6 h-6 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
                        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M3 8v10a1 1 0 0 0 1 1h4v-5a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v5h4a1 1 0 0 0 1-1V8M1 10l9-9 9 9"/>
                    </svg>
                </RouterLink>

                <!-- Messages button -->
                <button v-if="isLoggedIn" @click="toggleMessagePanel">
                    <svg class="w-6 h-6 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 18">
                        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M16 5h2a1 1 0 0 1 1 1v7a1 1 0 0 1-1 1h-2v3l-4-3H8m4-13H2a1 1 0 0 0-1 1v7a1 1 0 0 0 1 1h2v3l4-3h4a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1Z"/>
                    </svg>
                </button>

                <!-- Notifications button -->
                <button>
                    <svg class="w-6 h-6 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 16 21">
                        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M8 3.464V1.1m0 2.365a5.338 5.338 0 0 1 5.133 5.368v1.8c0 2.386 1.867 2.982 1.867 4.175C15 15.4 15 16 14.462 16H1.538C1 16 1 15.4 1 14.807c0-1.193 1.867-1.789 1.867-4.175v-1.8A5.338 5.338 0 0 1 8 3.464ZM4.54 16a3.48 3.48 0 0 0 6.92 0H4.54Z"/>
                    </svg>
                </button>
            </div>
            
            <!-- signout -->
            <div>
            </div>

            <!-- User profile -->
            <div class="user-profile">
                <button v-if="isLoggedIn">
                <!-- <RouterLink :to="getProfileLink()"> -->
                    <img class="user-profile__img" src="" alt="user-profile"/>
                <!-- </RouterLink> -->
                </button>
            </div>
        </nav>
    </div>
</template>
  
<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink } from 'vue-router'

const isLoggedIn = true     /* store에서 가져오기 */
const open = ref(false)

// const getProfileLink = () => {
//     if (isLoggedIn) {
//         return `profile/${ /* nickname */ }`
//     }
// }

const getHomeLink = () => {
    return isLoggedIn ? '/' : '/signin'
}

const toggleMessagePanel = () => {
    open.value = !open.value
}
</script>

<style scoped>
.side-bar {
    @apply flex flex-col items-center flex-shrink-0 w-14 h-screen py-3 bg-white border-r-2 border-indigo-100 shadow-emerald-600 rounded-tr-3xl rounded-br-3xl
}

.logo {
    @apply flex-shrink-0 py-4
}

.side-bar__item {
    @apply flex flex-col items-center flex-1 p-2 space-y-6
}

.user-profile {
    @apply relative flex items-center flex-shrink-0 p-2
}

.user-profile__img {
    @apply w-10 h-10 rounded-full shadow-md
}
</style>