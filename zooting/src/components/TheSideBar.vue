<template>
  <div>
    <nav class="side-bar">
      <div class="logo">
          <RouterLink :to="getHomeLink()" @click="closeTab">
            <img src="/images/logo_sm.png" alt="" class="w-10">
          </RouterLink>
      </div>
      <div class="side-bar__item">
        <!-- Messages button -->
        <button @click="toggleMessagesTab" v-if="isLoggedIn && isCompletedTest">
          <svg :class="[isActiveMessageTab() ? 'text-violet-800' : 'text-gray-400', 'w-5 h-6']" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 18 20" transform="rotate(45)">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="m9 17 8 2L9 1 1 19l8-2Zm0 0V9"/>
          </svg>
        </button>
        <!-- Notifications button -->
        <button @click="toggleNotificationsTab">
          <svg :class="[isActivenotificationsTab() ? 'text-violet-800' : 'text-gray-400', 'w-6 h-6']" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="14" height="20" fill="none" viewBox="0 0 14 20">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4 7a3 3 0 0 1 3-3M5 19h4m0-3c0-4.1 4-4.9 4-9A6 6 0 1 0 1 7c0 4 4 5 4 9h4Z"/>
          </svg>
        </button>
      </div>
        
      <!-- signout -->
      <div class="signout">
        <button @click="logout" v-if="isLoggedIn">
          <svg class="w-5 h-6 m-3 text-gray-400" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 16 16" transform="rotate(180)">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4 8h11m0 0-4-4m4 4-4 4m-5 3H3a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h3"/>
          </svg>
        </button>
      </div>

      <!-- User profile -->
      <div class="user-profile">
        <RouterLink :to="getProfileLink()" v-if="isLoggedIn && isCompletedTest" @click="closeTab">
          <img class="user-profile__img" src="" alt="user-profile"/>
        </RouterLink>
      </div>
    </nav>

    <!-- Side tab -->
    <transition name="side-tab-transition">
      <div class="side-tab" v-show="isSideTabOpen">
        <section v-show="currentSideTab == 'messagesTab'">
          <div class="side-tab__content">
            <DM />
          </div>
        </section>

        <section v-show="currentSideTab == 'notificationsTab'">
          <div class="side-tab__content">
            <Notifications />
          </div>
        </section>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { useAccessTokenStore } from "../stores/store"
import DM from './DM.vue'
import Notifications from './Notifications.vue'

const store = useAccessTokenStore()
const router = useRouter()

const isLoggedIn = computed(() => store.isLogin)
const isCompletedTest = computed(() => store.isCompletedTest)

const isSideTabOpen = ref(false)
const currentSideTab = ref<string | null>(null)

const getHomeLink = () => {
  return isLoggedIn.value ? '/' : '/signin'
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
    isSideTabOpen.value = false
  }
  else if (isSideTabOpen.value === false) {
    isSideTabOpen.value = true
  }
  currentSideTab.value = 'notificationsTab'
}

const isActiveMessageTab = () => {
  if (isSideTabOpen.value === true && currentSideTab.value === 'messagesTab') {
    return true
  } else {
    return false
  }
} 

const isActivenotificationsTab = () => {
  if (isSideTabOpen.value === true && currentSideTab.value === 'notificationsTab') {
    return true
  } else {
    return false
  }
}

const userInfo = ref(store.userInfo)
const nickname = ref<string | null | undefined>(null)

watch(()=> store.userInfo, (UpdateUser)=>{
  userInfo.value = UpdateUser
  nickname.value = userInfo.value?.nickname
})

const getProfileLink = () => {
  return `/profile/${nickname.value}`
}

const closeTab = () => {
  isSideTabOpen.value = false
}

const logout = () => {
  isSideTabOpen.value = false
  store.signOut()
  router.push({ name: 'signin' })
}
</script>

<style scoped>
.side-bar {
    @apply fixed flex flex-col items-center flex-shrink-0 w-14 h-screen py-3 bg-white border-r-2 border-gray-300 shadow-sm rounded-tr-3xl rounded-br-3xl z-20;
}
.logo {
    @apply flex-shrink-0 py-4;
}
.side-bar__item {
    @apply flex flex-col items-center flex-1 p-4 space-y-8;
}
.user-profile {
    @apply relative flex items-center flex-shrink-0 my-3;
}
.user-profile__img {
    @apply w-10 h-10 rounded-full shadow-md;
}
.side-tab {
    @apply fixed inset-y-0 flex-shrink-0 transition-transform duration-300 transform bg-white border-r-2 border-gray-300 left-14 rounded-tr-3xl rounded-br-3xl z-10;
    width: 450px;
}
.side-tab__content {
}

section {
    @apply px-4 py-6;
}
.side-tab-transition-enter-active,
.side-tab-transition-leave-active {
  transition: transform 0.5s;
}

.side-tab-transition-enter-from,
.side-tab-transition-leave-to {
    @apply -translate-x-full;
}

.side-tab-transition-enter-to,
.side-tab-transition-leave-from {
    @apply translate-x-0;
}
svg {
    @apply hover:text-gray-500;
}
</style>