<template>
  <div class="dm__container">
    <!-- 상대방 정보 -->
    <div class="dm__receiver-info">
      <div class="flex items-center gap-4">
        <!-- 뒤로가기 버튼 -->
        <svg class="exit-button" @click="$emit('closeTab')" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="m15 19-7-7 7-7"/>
        </svg>
        <!-- 프로필 이미지 (프로필로 라우팅) -->
        <RouterLink :to="getProfileLink()" @click="$emit('closeTab')">
          <img class="w-12" :src="getProfileImage()" alt="profile">
        </RouterLink>
          <!-- 상대방 닉네임/성별 -->
          <div class="flex items-center">
            {{ receiverInfo?.nickname }}
            <div class="gender-icon">
              <svg :class="getHeartClass()" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                <path d="m12.7 20.7 6.2-7.1c2.7-3 2.6-6.5.8-8.7A5 5 0 0 0 16 3c-1.3 0-2.7.4-4 1.4A6.3 6.3 0 0 0 8 3a5 5 0 0 0-3.7 1.9c-1.8 2.2-2 5.8.8 8.7l6.2 7a1 1 0 0 0 1.4 0"/>
              </svg>
            </div>
          </div>
      </div>
    </div>

    <!-- 대화 내용 -->
    <div class="dm__chat" ref="chatRef" @scroll="handleChatScroll">
      <div v-for="(item, index) in DmInfo?.dmList" :key="index">
        <div :class="[isSender(item.sender) ? 'justify-end': '', 'flex mb-4']">
          <div :class="[isSender(item.sender) ? 'bg-violet-200 rounded-s-xl rounded-b-xl': 'bg-gray-100 rounded-e-xl rounded-es-xl', 'dm__chat-item']">
              <p class="py-2 text-sm text-gray-900" style="word-break: break-all;">{{ item.message }}</p>
          </div>
        </div>
      </div>
    </div>


    <div class="dm__input">
        <!-- 파일 첨부 버튼 -->

        <!-- 텍스트 입력창 -->
        <input 
          class="text-input"
          v-model="messageInput" 
          type="text" 
          placeholder="Type your message..."
          @keyup.enter="sendMessage"
        >

        <!-- 전송 버튼 -->
        <button class="send-button" @click="sendMessage">
          <svg class="w-6 h-6 text-white transform rotate-90" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="m12 18-7 3 7-18 7 18-7-3Zm0 0v-5"/>
          </svg>
        </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import { useAccessTokenStore } from "../stores/store"

const store = useAccessTokenStore()
const emit = defineEmits(['closeTab'])

const DmInfo = ref<DM | null>(store.DmInfo)
const receiverInfo = ref<Friend | null>(store.receiverInfo)

const chatRef = ref<any>(null)

const messageInput = ref<string>('')
const fileInput = ref<string>('')
const sender = ref<string | undefined>(receiverInfo.value?.email)
const receiver = ref<string | null | undefined>(store.userInfo?.email)
const dmRoomId = ref<number | undefined>(DmInfo.value?.dmRoomId)

watch(()=> store.DmInfo, (UpdateUser)=>{
  DmInfo.value = UpdateUser
})

watch(()=> store.receiverInfo, (UpdateUser)=>{
  receiverInfo.value = UpdateUser
})

const getProfileImage = () => {
  const imgUrl = ref<string>('')
  const animal = receiverInfo.value?.animal
  if (animal === '강아지') {
    imgUrl.value = 'src/assets/images/animal/dog.png'
  }
  if (animal === '고양이') {
    imgUrl.value = 'src/assets/images/animal/cat.png'
  }
  if (animal === '곰') {
    imgUrl.value = 'src/assets/images/animal/bear.png'
  }
  if (animal === '공룡') {
    imgUrl.value = 'src/assets/images/animal/dino.png'
  }
  if (animal === '펭귄') {
    imgUrl.value = 'src/assets/images/animal/penguin.png'
  }
  if (animal === '토끼') {
    imgUrl.value = 'src/assets/images/animal/rabbit.png'
  }
  if (animal === '사슴') {
    imgUrl.value = 'src/assets/images/animal/deer.png'
  }
  return imgUrl.value
}

const getProfileLink = () => {
  return `/profile/${receiverInfo.value?.nickname}`
}

const getHeartClass = () => {
  return receiverInfo.value?.gender === 'man' ? 'w-4 h-4 text-blue-500 ms-1' : 'w-4 h-4 text-pink-500 ms-1';
}

const isSender = (sender: string) => {
  if (sender === store.userInfo?.email) {
    return true
  }
  return false
}


const handleChatScroll = () => {
  if (chatRef.value && !store.isRefreshing) {
    const threshold = 0.6
    const isAtBottom = Math.abs(chatRef.value.clientHeight - chatRef.value.scrollHeight - chatRef.value.scrollTop) < threshold
    if (isAtBottom) {
      store.isRefreshing = true
      refreshChat()
    }
  }
}

const refreshChat = () => {
  if (DmInfo.value) {
    const params = {
      dmRoomId: DmInfo.value.dmRoomId,
      cursor: DmInfo.value.cursor
    }
    store.cursorDmRoom(params)
  }
}

const sendMessage = () => {
  if (messageInput.value) {

    messageInput.value = ''
  }
}

interface DM {
  dmRoomId: number;
  dmList: DmItem[];
  cursor: number;
}

interface DmItem {
  dmRoomId: number;
  sender: string;
  message: string;
}

interface Friend {
  email: string;
  nickname: string;
  animal: string;
  gender: string;
}
</script>

<style scoped>
.dm__container {
  @apply h-full flex flex-col;
}
.dm__receiver-info {
  @apply h-14 border-b border-gray-200 flex px-4 py-10;
}
.dm__chat {
@apply flex-grow mx-10 flex flex-col-reverse relative;
overflow-y: auto;
}
.dm__chat::-webkit-scrollbar {
  @apply hidden;
}
.dm__input {
  @apply flex items-center h-16 border border-gray-300 rounded-lg py-7 px-3 mx-6 mb-7 shadow-md;
}
.dm__chat-item {
  @apply flex max-w-[300px] py-2 px-6 border-gray-200;
}
.file-input {
}
.text-input {
  @apply flex-grow border border-none rounded-md;
}
.send-button {
  @apply cursor-pointer bg-violet-500 p-2 ms-2 rounded-3xl hover:bg-violet-600 shadow-lg;
}
.exit-button {
  @apply text-gray-500 cursor-pointer w-7 h-7 hover:text-gray-700;
}
.refresh-button {
  @apply  absolute top-5 left-1/2 flex text-gray-400 text-lg hover:text-gray-500 cursor-pointer;
}
</style>