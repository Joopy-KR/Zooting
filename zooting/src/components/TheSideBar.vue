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

      <!-- Profile image -->
      <img 
        v-if="isLoggedIn"
        id="avatarButton" 
        type="button" 
        data-dropdown-toggle="userDropdown" 
        data-dropdown-placement="bottom-start" 
        class="user-profile" 
        src="" alt="User dropdown"
      >
      
      <!-- 유저 정보가 없을 경우에 사용할 빈 프로필 -->
      <!-- <div class="relative w-10 h-10 overflow-hidden bg-gray-100 rounded-full dark:bg-gray-600">
        <svg class="absolute w-12 h-12 text-gray-400 -left-1" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd"></path></svg>
      </div> -->

      <!-- Dropdown menu -->
      <div id="userDropdown" class="z-10 hidden bg-white divide-y divide-gray-100 rounded-lg shadow w-44" v-if="isLoggedIn">
          <ul class="py-2 text-sm text-gray-700" aria-labelledby="avatarButton">
            <li>
              <RouterLink :to="getProfileLink()" class="block px-4 py-2 hover:bg-gray-100" @click="closeTab">프로필</RouterLink>
            </li>
          </ul>
          <div class="py-1">
            <div class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100" @click="logout">로그아웃</div>
          </div>
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
    @apply w-10 h-10 rounded-full cursor-pointer bg-slate-500;
}
.side-tab {
    @apply fixed inset-y-0 flex-shrink-0 transition-transform duration-300 transform bg-white border-r-2 border-gray-300 left-14 rounded-tr-3xl rounded-br-3xl z-10;
    width: 450px;
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