<template>
  <div>
    <nav class="side-bar">
      <div class="logo">
          <RouterLink :to="getHomeLink()" @click="closeTab">
            <img src="@/assets/images/logo/logo_sm.png" alt="" class="w-10">
          </RouterLink>
      </div>
      <div class="side-bar__item">
        
        <!-- Notifications button -->
        <button @click="toggleNotificationsTab">
          <svg ref="tabRef" :class="[isActivenotificationsTab() ? 'text-violet-800 hover:text-violet-600' : 'text-gray-400 hover:text-gray-500', 'w-6 h-6']" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="14" height="20" fill="none" viewBox="0 0 14 20">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4 7a3 3 0 0 1 3-3M5 19h4m0-3c0-4.1 4-4.9 4-9A6 6 0 1 0 1 7c0 4 4 5 4 9h4Z"/>
          </svg>
        </button>
      </div>

      <!-- Profile image -->
      <img 
        v-if="isLoggedIn"
        type="button" 
        @click="openProfileMenu()"
        class="user-profile" 
        ref="profileRef"
        :src="getProfileImage()" alt="User dropdown"
      >
      
      <!-- Profile menu -->
      <div class="profile-menu" v-show="isOpenProfileMenu" v-click-outside="ClickOustsideProfileMenu">
          <ul class="py-2 text-sm text-gray-700">
            <li class="block px-4 py-2 text-gray-300" v-if="!isLoggedIn || !store.isCompletedSignUp">
              프로필
            </li>
            <li v-if="isLoggedIn && store.isCompletedSignUp">
              <RouterLink :to="getProfileLink()" class="block px-4 py-2 hover:bg-gray-100" @click="closeTab" >프로필</RouterLink>
            </li>
          </ul>
          <div class="py-2">
            <div class="block px-4 py-2 text-sm text-gray-700 cursor-pointer hover:bg-gray-100" @click="logout">로그아웃</div>
          </div>
      </div>
    </nav>

    <!-- Side tab -->
    <transition name="side-tab-transition">
      <div class="side-tab" v-show="isSideTabOpen" v-click-outside="ClickOustsideTab">
        <section v-show="currentSideTab == 'messagesTab'" class="h-full">
            <DM 
              @close-tab="closeTab()"
              :open="isSideTabOpen"  
            />
        </section>

        <section v-show="currentSideTab == 'notificationsTab'" class="h-full">
          <Notifications />
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

const isSideTabOpen = ref(false)
const currentSideTab = ref<string | null>(null)

const userInfo = ref(store.userInfo)

watch(()=> store.userInfo, (UpdateUser)=>{
  userInfo.value = UpdateUser
})

const getHomeLink = () => {
  return isLoggedIn.value ? '/' : '/signin'
}

watch(()=> store.isEntryDmRoom, ()=>{
  if (store.isEntryDmRoom) {
    isSideTabOpen.value = true
    currentSideTab.value = 'messagesTab'
  }
})

const toggleNotificationsTab = () => {
  if (isSideTabOpen.value === true && currentSideTab.value === 'notificationsTab') {
    isSideTabOpen.value = false
    store.isEntryDmRoom = false
  }
  else if (isSideTabOpen.value === false) {
    isSideTabOpen.value = true
    store.isEntryDmRoom = false
  }
  currentSideTab.value = 'notificationsTab'
}

const isActivenotificationsTab = () => {
  if (isSideTabOpen.value === true && currentSideTab.value === 'notificationsTab') {
    return true
  } else {
    return false
  }
}

const getProfileLink = () => {
  return `/profile/${userInfo.value?.nickname}`
}

const getProfileImage = () => {
  const animal = userInfo.value?.animal
  const profile = animal === '강아지' ? 'dog' :
                 animal === '고양이' ? 'cat' :
                 animal === '곰' ? 'bear' :
                 animal === '공룡' ? 'dino' :
                 animal === '펭귄' ? 'penguin' :
                 animal === '토끼' ? 'rabbit' :
                 animal === '사슴' ? 'deer' :
                 'default-profile';
  return `src/assets/images/animal/${profile}.png`
}

const closeTab = () => {
  isSideTabOpen.value = false
  isOpenProfileMenu.value = false
  store.isEntryDmRoom = false
  store.dmInfo = null
}

const logout = () => {
  closeTab()
  store.signOut()
  router.push({ name: 'signin' })
}

const isOpenProfileMenu = ref<boolean>(false)

const openProfileMenu = () => (
  isOpenProfileMenu.value = !isOpenProfileMenu.value
)

const profileRef =  ref<HTMLElement | null>(null)

const ClickOustsideProfileMenu = (isOutSide: boolean, e: Event) => {
  if (isOutSide) {
    if (e.target !== profileRef.value) {
      isOpenProfileMenu.value = false
    }
  }
}

const tabRef =  ref<HTMLElement | null>(null)

const ClickOustsideTab = (isOutSide: boolean, e: Event) => {
  if (isOutSide) {
    if ((e.target !== tabRef.value) && (e.target !== profileRef.value)) {
      closeTab()
    }
  }
}
</script>

<style scoped>
.side-bar {
    @apply fixed flex flex-col items-center flex-shrink-0 w-14 h-screen py-3 bg-white border-r-2 border-gray-300 shadow-sm rounded-tr-3xl rounded-br-3xl z-30;
}
.logo {
    @apply flex-shrink-0 py-4;
}
.side-bar__item {
    @apply flex flex-col items-center flex-1 p-4 space-y-8;
}
.user-profile {
    @apply w-10 h-10 rounded-full cursor-pointer relative;
}
.profile-menu {
  @apply z-10 bg-white divide-y divide-gray-100 rounded-lg shadow w-44 absolute bottom-14 left-8;
}
.side-tab {
    @apply fixed inset-y-0 flex-shrink-0 transition-transform duration-300 transform bg-white border-r-2 border-gray-300 left-14 rounded-tr-3xl rounded-br-3xl z-20;
    width: 450px;
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
</style>