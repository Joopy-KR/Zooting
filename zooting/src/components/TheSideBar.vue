<template>
    <div>
        <nav class="side-bar">
            <div class="logo">
                <RouterLink :to="getHomeLink()">
                    ZT
                </RouterLink>
            </div>
            <div class="side-bar__item">
                <!-- Messages button -->
                <button @click="toggleMessagesTab" v-if="isLoggedIn">
                    <svg class="w-6 h-6 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 18">
                    <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M16 5h2a1 1 0 0 1 1 1v7a1 1 0 0 1-1 1h-2v3l-4-3H8m4-13H2a1 1 0 0 0-1 1v7a1 1 0 0 0 1 1h2v3l4-3h4a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1Z"/>
                    </svg>
                </button>

                <!-- Notifications button -->
                <button @click="toggleNotificationsTab">
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

        <!-- Side tab -->
        <div class="side-tab" v-show="isSideTabOpen">
            <section v-show="currentSideTab == 'messagesTab'">
            <h1>Messages</h1>
            <div class="side-tab__content">
                <DM />
            </div>
            </section>

            <section v-show="currentSideTab == 'notificationsTab'">
              <h1>Notifications</h1>
              <div class="side-tab__content">
                <h1>공지사항</h1>
              </div>
            </section>
        </div>
    </div>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink } from 'vue-router'
import DM from './DM.vue'

const isLoggedIn = ref(true)
// const isLoggedIn = computed(() => store.isLogin)
const isSideTabOpen = ref(false)
const currentSideTab = ref<string | null>(null)

const getHomeLink = () => {
    return isLoggedIn ? '/' : '/signin'
}

const toggleMessagesTab = () => {
    if (isSideTabOpen.value === true && currentSideTab.value === 'messagesTab') {
        isSideTabOpen.value = false
    }
    else if (isSideTabOpen.value === false) {
        isSideTabOpen.value = true
    }
    currentSideTab.value = 'messagesTab'
}

const toggleNotificationsTab = () => {
    if (isSideTabOpen.value === true && currentSideTab.value === 'notificationsTab') {
        isSideTabOpen.value = !isSideTabOpen.value
    }
    else if (isSideTabOpen.value === false) {
        isSideTabOpen.value = true
    }
    currentSideTab.value = 'notificationsTab'
}

// const getProfileLink = () => {
//     return `/profile/${/*request user nickname*/}`
// }
</script>

<style scoped>
.side-bar {
    @apply flex flex-col items-center flex-shrink-0 w-14 h-screen py-3 bg-white border-r-2 border-indigo-100 shadow-emerald-600 rounded-tr-3xl rounded-br-3xl;
}

.logo {
    @apply flex-shrink-0 py-4;
}

.side-bar__item {
    @apply flex flex-col items-center flex-1 p-2 space-y-6;
}

.user-profile {
    @apply relative flex items-center flex-shrink-0 p-2;
}

.user-profile__img {
    @apply w-10 h-10 rounded-full shadow-md;
}

.side-tab {
    @apply fixed inset-y-0 z-10 flex-shrink-0 w-64 transition-transform duration-300 transform bg-white border-r-2 border-indigo-100 shadow-lg left-14 rounded-tr-3xl rounded-br-3xl;
}

.side-tab__content {
    @apply h-screen;
}

section {
    @apply px-4 py-6;
}
</style>
