<template>
  <div class="interest__container">
    <!-- 일대일 화상 채팅 신청 상태 알림 -->
    <transition name="fade">
      <div class="video-chat-alert" v-if="isOpenAlert">
        <div role="alert" class="alert" v-if="isSuccessRequest && !isOpenRejectAlert">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-green-500 stroke-current shrink-0" fill="none" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <div class="flex flex-col">
            <span class="font-semibold">화상 채팅 신청 성공</span>
            <span class="text-gray-500">상대방이 수락할 때까지 기다려 주세요</span>
          </div>
        </div>
        <div role="alert" class="alert" v-else>
          <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-red-500 stroke-current shrink-0" fill="none" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
          </svg>
          <div class="flex flex-col">
            <span class="font-semibold">미팅 신청 실패</span>
            <span v-if="isOpenRejectAlert" class="text-gray-500">상대방이 미팅에 응답하지 않았어요</span>
            <span v-else class="text-gray-500">현재 다른 미팅 요청 중이에요</span>
          </div>
        </div>
      </div>
    </transition>

    <div v-if="friendList.length === 0" class="mt-5 text-center text-gray-500">
      추가한 사용자가 없습니다
    </div>
    <ul role="list" class="friend-list">
      <!-- 친구 리스트 -->
      <li v-for="(item, index) in friendList" :key="index" :class="[item.nickname === openedMenuNickname ? 'bg-gray-50': '', 'friend-list__item']">
        <div class="friend-list__item__chat">
          <div class="flex items-center gap-4">
            <!-- 프로필 이미지 -->
            <RouterLink :to="getProfileLink(item.nickname)">
              <img :class="getAvatarPretty(item)" :src="getProfileImage(item.animal)" alt="profile">
            </RouterLink>
            <div class="font-medium">
              <div class="flex items-center">
                {{ item.nickname }}
                <!-- 성별 아이콘 -->
                <div class="gender-icon">
                  <svg :class="getHeartClass(item.gender)" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                    <path d="m12.7 20.7 6.2-7.1c2.7-3 2.6-6.5.8-8.7A5 5 0 0 0 16 3c-1.3 0-2.7.4-4 1.4A6.3 6.3 0 0 0 8 3a5 5 0 0 0-3.7 1.9c-1.8 2.2-2 5.8.8 8.7l6.2 7a1 1 0 0 0 1.4 0"/>
                  </svg>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="buttons">
          <!-- 일대일 신청 중 표시 -->
          <div role="status" class="me-5" v-if="isSuccessRequest && item.nickname === meetingReciver">
              <svg aria-hidden="true" class="inline w-6 h-6 text-gray-200 animate-spin fill-[#8072EF]" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"/>
                  <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"/>
              </svg>
          </div>
          <!-- 채팅 버튼 -->
          <svg class="dm-button" @click="entryChat(item)" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="m12 18-7 3 7-18 7 18-7-3Zm0 0v-5"/>
          </svg>
          <!-- 새로운 채팅 알림 -->
          <div class="absolute bottom-[12px] left-[11px] cursor-pointer" v-show="isNewSender(item.email)" @click="entryChat(item)">
            <span class="relative flex w-2 h-2">
              <span class="absolute inline-flex w-full h-full rounded-full opacity-75 animate-ping bg-[#DF75DB]"></span>
              <span class="relative inline-flex w-2 h-2 rounded-full bg-[#DF75DB]"></span>
            </span>
          </div>
          <!-- 프로필 이동/ 친구 삭제 메뉴 버튼 -->
          <svg class="menu-button" @click="openMenu(item.nickname)" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <path stroke="currentColor" stroke-linecap="round" stroke-width="2" d="M12 6h0m0 6h0m0 6h0"/>
          </svg>
        </div>
        <div class="menu" v-show="isOpenMenu && openedMenuNickname === item.nickname">
          <div class="py-2">
            <div v-if="item.isOnline" class="block px-4 py-2 text-sm text-gray-700 cursor-pointer hover:bg-gray-100" @click="meetingRequestFriend(item.nickname)">미팅 신청</div>
            <p v-else class="block px-4 py-2 text-sm text-gray-300">미팅 신청</p>
          </div>
          <div class="py-2 text-sm text-gray-700">
            <RouterLink :to="getProfileLink(item.nickname)" class="block px-4 py-2 hover:bg-gray-100">프로필 보기</RouterLink>
          </div>
          <div class="py-2">
            <div class="block px-4 py-2 text-sm text-red-700 cursor-pointer hover:bg-gray-100" @click="friendDelete(item.nickname)">친구 삭제</div>
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>
<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import { useAccessTokenStore, useStore } from '@/stores/store'
import { RouterLink } from 'vue-router'
import type { Friend } from "@/types/global"

const store = useAccessTokenStore()
const dmStore = useStore()
const friendList = ref(store.friendList)
const isOpenMenu = ref<boolean>(false)
const openedMenuNickname = ref<string | null>(null)
const isOpenAlert = ref<boolean>(false)
const isOpenRejectAlert = ref<boolean>(false)
const isSuccessRequest = ref<boolean>(true)
const meetingReciver = ref<string>('')

const emit = defineEmits(['readMessage'])

watch(() => store.friendList, (update) => {
  friendList.value = update
})

// 일대일 미팅 거절
watch(() => store.isMeetingReject, (update) => {
  if (update) {
    isOpenAlert.value = true
    isOpenRejectAlert.value = true
    isSuccessRequest.value = false
  }
  setTimeout(() => {
    isOpenAlert.value = false
    isOpenRejectAlert.value = false
    store.isMeetingReject = false
  }, 2000)
})

const getProfileLink = (value: string) => `/profile/${value}`

const friendDelete = (nickname: string) => {
  store.friendDelete(nickname)
} 

const getHeartClass = (gender: string) => (
  gender === 'man' ? 'w-4 h-4 text-blue-500 ms-1' : 'w-4 h-4 text-pink-500 ms-1'
)

const getAvatarPretty = (item: Friend) => {
  if (!item.isOnline) {
    return "friend-list__img__offline"
  }
  if (item.gender === 'man') {
    return "friend-list__img__man__online"
  } else {
    return "friend-list__img__woman__online"
  }
}

const getProfileImage = (animal: string) => {
  let imgUrl: URL;
  if (animal === '강아지') {
    imgUrl = new URL('/assets/images/animal/dog.png', import.meta.url);
  } else if (animal === '고양이') {
    imgUrl = new URL('/assets/images/animal/cat.png', import.meta.url);
  } else if (animal === '곰') {
    imgUrl = new URL('/assets/images/animal/bear.png', import.meta.url);
  } else if (animal === '공룡') {
    imgUrl = new URL('/assets/images/animal/dino.png', import.meta.url);
  } else if (animal === '펭귄') {
    imgUrl = new URL('/assets/images/animal/penguin.png', import.meta.url);
  } else if (animal === '토끼') {
    imgUrl = new URL('/assets/images/animal/rabbit.png', import.meta.url);
  } else if (animal === '사슴') {
    imgUrl = new URL('/assets/images/animal/deer.png', import.meta.url);
  } else {
    imgUrl = new URL('/assets/images/animal/animal_group.png', import.meta.url);
  }
  return imgUrl.href;
}

// DM 입장
const entryChat = (item: Friend) => {
  store.entryDmRoom(item)
  isOpenMenu.value = false
  openedMenuNickname.value = null
  dmStore.newMessage = dmStore.newMessage.filter(email => email !== item.email)
}

const openMenu = (nickname: string) => {
  if (isOpenMenu.value && openedMenuNickname.value == nickname) {
    isOpenMenu.value = false
  } else {
    isOpenMenu.value = true
  }
  openedMenuNickname.value = isOpenMenu.value ? nickname : null
}

// 새로운 메시지를 보낸 친구일 경우 알림 표시
const isNewSender = computed(() => (email: string) => dmStore.newMessage.includes(email))

// 일대일 미팅 요청
const meetingRequestFriend = (reciever: string) => {
  isOpenAlert.value = true
  isOpenMenu.value = false
  openedMenuNickname.value = null
  // 요청 중이 아닌 상태에서 요청
  if (!store.isRequesting &&  !store.isMatching) {
    meetingReciver.value = reciever
    store.meetingRequestFriend(reciever)
    isSuccessRequest.value = true
  } else {
    isSuccessRequest.value = false
  }
  setTimeout(() => {
    isOpenAlert.value = false
  }, 2000)
}
</script>

<style scoped>
.interest__container {
  @apply flex-grow;
  overflow-y: auto;
}
.interest__container::-webkit-scrollbar {
  width: 6px;
  background-color: white;
}
.interest__container::-webkit-scrollbar-thumb {
  background-color: #d6d6d6;
  border-radius: 4px;
}
.interest__container::-webkit-scrollbar-track {
  background-color: transparent;
}
.friend-list {
  @apply m-1 divide-y divide-gray-200;
}
.friend-list__item {
  @apply flex justify-between px-6 py-4;
}
.friend-list__item__chat {
  @apply flex items-center justify-between gap-4;
}
.friend-list__img__woman__online {
  @apply w-12 h-12 rounded-full;
  box-shadow: 0 0 5px 2px rgb(253, 108, 108);
}
.friend-list__img__man__online {
  @apply w-12 h-12 rounded-full;
  box-shadow: 0 0 5px 2px rgb(93, 161, 249);
}
.friend-list__img__offline {
  @apply w-12 h-12 rounded-full border border-gray-200 shadow-sm;
}
.buttons {
  @apply flex items-center relative;
}
.gender-icon {
  @apply text-xl;
}
.dm-button {
  @apply w-6 h-6 mb-1 text-gray-500 transform rotate-45 cursor-pointer me-2 hover:text-gray-600;
}
.menu-button {
  @apply relative w-6 h-6 text-gray-800 cursor-pointer;
}
.menu {
  @apply absolute z-10 bg-white divide-y divide-gray-100 rounded-lg shadow w-44 left-[470px] p-0;
}
.video-chat-alert {
  @apply absolute z-20;
  width: 428px;
}
.alert {
  @apply rounded-none shadow-md;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.7s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
