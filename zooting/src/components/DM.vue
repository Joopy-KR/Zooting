<template>
  <div class="dm__container">
    <!-- 상대방 정보 -->
    <div class="dm__receiver-info">
      <div class="flex items-center gap-4">
        <!-- 뒤로가기 버튼 -->
        <svg class="exit-button" @click="$emit('closeTab'), isOpenFileInput = !isOpenFileInput" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
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
      <div v-for="(item, index) in dmInfo?.dmList" :key="index">
        <div :class="[isSender(item.sender) ? 'justify-end': '', 'flex mb-4']">
          <div :class="[isSender(item.sender) ? 'bg-violet-200 rounded-s-xl rounded-b-xl': 'bg-gray-100 rounded-e-xl rounded-es-xl', 'dm__chat-item']">
              <p class="py-2 text-sm text-gray-900" style="word-break: break-all;">{{ item.message }}</p>
          </div>
        </div>
      </div>
    </div>


    <div class="dm__input">
        <!-- 파일 첨부 버튼 -->
        <button class="file-button" @click="openFileInput">
          <font-awesome-icon :icon="['fas', 'paperclip']" />
        </button>
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
        <!-- 파일 등록 -->
        <div class="file-input" v-show="isOpenFileInput">
          <label for="dropzone-file" class="flex flex-col items-center justify-center w-full border-2 border-gray-300 border-dashed rounded-lg cursor-pointer h-50 bg-gray-50">
            <!-- 파일 imamge -->
            <img v-if="fileInput" :src="getPreviewUrl(fileInput)" class="my-3 h-28" alt="Preview">
            <!-- 파일 input -->
            <div class="file-input__discription" v-show="!fileInput">
              <svg class="w-8 h-8 mb-4 text-gray-500" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 16">
                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 13h3a3 3 0 0 0 0-6h-.025A5.56 5.56 0 0 0 16 6.5 5.5 5.5 0 0 0 5.207 5.021C5.137 5.017 5.071 5 5 5a4 4 0 0 0 0 8h2.167M10 15V6m0 0L8 8m2-2 2 2"/>
              </svg>
              <p class="mb-2 text-sm text-gray-500"><span class="font-semibold">Click to upload</span> and drop</p>
              <p class="text-xs text-gray-500">SVG, PNG, JPG or GIF (MAX. 800x400px)</p>
            </div>
            <input id="dropzone-file" type="file" class="hidden" @change="handleFileChange"/>
          </label>
      </div> 
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useAccessTokenStore } from "../stores/store"
import SockJS from "sockjs-client"
import Stomp from "stompjs"
import type { Friend, DM } from "@/types/global"
const { VITE_SERVER_API_URL } = import.meta.env

const store = useAccessTokenStore()
const emit = defineEmits(['closeTab'])
const props = defineProps<{
  open: boolean
}>()

const dmInfo = ref<DM | null>(store.dmInfo)
const receiverInfo = ref<Friend | null>(store.receiverInfo)

const chatRef = ref<HTMLElement | null>(null)
const isOpenFileInput = ref<boolean>(false)

const messageInput = ref<string>('')
const fileInput = ref<File | null>(null)
const sender = ref<string | undefined>(receiverInfo.value?.email)
const receiver = ref<string | null | undefined>(store.userInfo?.email)
const dmRoomId = ref<number | undefined>(dmInfo.value?.dmRoomId)

watch(()=> store.dmInfo, (UpdateUser)=>{
  dmInfo.value = UpdateUser
})

watch(()=> store.receiverInfo, (UpdateUser)=>{
  receiverInfo.value = UpdateUser
})

watch(() => props.open, () => {
  if (!props.open) {
    isOpenFileInput.value = false
    fileInput.value = null
  } else {
    connect()
  }
})
const getProfileImage = () => {
  return `/images/${receiverInfo.value?.animal}.png`
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
  if (chatRef.value) {
    const threshold = 0.8
    const isAtBottom = Math.abs(chatRef.value.clientHeight - chatRef.value.scrollHeight - chatRef.value.scrollTop) < threshold
    if (isAtBottom) {
      refreshChat()
    }
  }
}

const refreshChat = () => {
  if (dmInfo.value) {
    const params = {
      dmRoomId: dmInfo.value.dmRoomId,
      cursor: dmInfo.value.cursor
    }
    if (!store.isRefreshing) {
      store.isRefreshing = true
      store.cursorDmRoom(params)
    }
  }
}

const sendMessage = () => {
  if (messageInput.value) {

    messageInput.value = ''
  }
}

const openFileInput = () => {
  isOpenFileInput.value = !isOpenFileInput.value
  fileInput.value = null
}

const handleFileChange = (event: any) => {
  const selectedFiles = (event.target as HTMLInputElement).files

  if (selectedFiles && selectedFiles.length > 0) {
    const allowedFileTypes = ['image/svg+xml', 'image/jpeg', 'image/png', 'image/gif']
    const selectedFileType = selectedFiles[0].type
    
    if (allowedFileTypes.includes(selectedFileType)) {
      fileInput.value = selectedFiles[0]
      console.log(fileInput.value)
    } else {
      window.alert('지원하지 않는 파일 형식입니다.')
      fileInput.value = null
    }
  }
}

const getPreviewUrl = (file: File) => {
  return URL.createObjectURL(file)
}

// Web socket -----------------------------------------------
const socket = ref<any>(null)
const stompClient = ref<any>(null)
const connection = ref<boolean>(false)

// 소켓 통신 연결 요청
const connect = () => {
  if (!store.getAccessToken()) {
    console.log("not found access token")
    return
  }
  socket.value = new SockJS(`${VITE_SERVER_API_URL}/ws/dm`)
  stompClient.value = Stomp.over(socket.value)

  var headers = {
    "Authorization": `Bearer ${store.getAccessToken()}`,
  };
  stompClient.value.connect(
      headers,
      () => {
        console.log("OKOKOKO")
        // onConnected(dmRoomId.value)
      },
      () => {
        console.log("Could not WebSocket server. Retry!")
      }
  )
}

// 소켓 클라이언트 Subscribe 요청
const onConnected = () => {
  stompClient.value.subscribe("/api/sub", "zyo0720@kakao.com")
  console.log("111111111111111")
  // connection.value = true;
  // stompClient.value.send(
  //     "/pub/chat/enter",
  //     {},
  //     JSON.stringify({
  //       roomId: roomId,
  //       sender: me.value.nickname,
  //       senderId: me.value.id,
  //     })
  // );
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
min-height: 300px;
}
.dm__chat::-webkit-scrollbar {
  @apply hidden;
}
.dm__input {
  @apply relative flex justify-center items-center h-16 border border-gray-300 rounded-lg py-7 px-3 mx-6 mb-7 shadow-md;
}
.dm__chat-item {
  @apply flex max-w-[300px] py-2 px-6 border-gray-200;
}
.text-input {
  @apply flex-grow border border-none rounded-md;
}
.send-button {
  @apply cursor-pointer bg-violet-500 p-2 ms-2 rounded-3xl hover:bg-violet-600 shadow-lg;
}
.exit-button {
  @apply text-gray-500 cursor-pointer w-7 h-7 hover:text-gray-700 border-none;
}
.file-button {
  @apply w-5 me-1 text-gray-500 cursor-pointer hover:text-gray-700 border-none;
}
.file-input {
  @apply flex items-center justify-center absolute w-full;
  bottom: 70px;
}
.file-input__discription {
  @apply flex flex-col items-center justify-center pt-5 pb-6;
}
</style>