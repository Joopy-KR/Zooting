<template>
  <div class="dm__container">
    <!-- 상대방 정보 -->
    <div class="dm__receiver-info">
      <div class="flex items-center gap-4">
        <!-- 뒤로가기 버튼 -->
        <svg class="exit-button" @click="$emit('closeTab'), isOpenFileInput = !isOpenFileInput" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="m15 19-7-7 7-7"/>
        </svg>
        <!-- 프로필 이미지 -->
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
        <!-- 소켓 통신 -->
        <div>
          <div v-for="(item, index) in sockDmList" :key="index">
            <div :class="[isSender(item.sender) ? 'chat-end': 'chat-start', 'chat']">
              <div class="chat-footer">
                <time class="opacity-50 text-2xs">{{ item.createdAt }}</time>
              </div>
              <div :class="[isSender(item.sender) ? 'bg-violet-200': 'bg-gray-100', 'chat-bubble']">
                <!-- 메시지 -->
                <p v-if="item.message" class="py-2 text-sm text-gray-900 break-all">{{ item.message }}</p>
                <!-- 사진 -->
                <div v-if="item.files && item.files.length > 0" :class="[item.files.length > 1 ? 'grid grid-cols-2 gap-2' : '', 'my-2']">
                  <div v-for="(file, index) in item.files" :key="index" @click="zoomImg(file)">
                    <img :src="file.thumbnailUrl" class="h-32 cursor-pointer" alt="Preview">
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 최근 대화 -->
        <div>
          <div v-for="(item, index) in dmInfo?.dmList" :key="index">
            <div :class="[isSender(item.sender) ? 'chat-end': 'chat-start', 'chat']">
              <div class="chat-footer">
                <time class="opacity-50 text-2xs">{{ item.createdAt }}</time>
              </div>
              <div :class="[isSender(item.sender) ? 'bg-violet-200': 'bg-gray-100', 'chat-bubble']">
                <!-- 메시지 -->
                <p v-if="item.message" class="py-2 text-sm text-gray-900 break-all">{{ item.message }}</p>
                <!-- 사진 -->
                <div v-if="item.files && item.files.length > 0" :class="[item.files.length > 1 ? 'grid grid-cols-2 gap-2' : '', 'my-2']">
                  <div v-for="(file, index) in item.files" :key="index" @click="zoomImg(file)">
                    <img :src="file.thumbnailUrl" class="h-32 cursor-pointer" alt="Preview">
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 이전 대화 (스크롤) -->
        <div v-for="(item, index) in pastDmList" :key="index">
          <div :class="[isSender(item.sender) ? 'chat-end': 'chat-start', 'chat']">
            <div class="chat-footer">
                <time class="opacity-50 text-2xs">{{ item.createdAt }}</time>
              </div>
            <div :class="[isSender(item.sender) ? 'bg-violet-200': 'bg-gray-100', 'chat-bubble']">
              <!-- 메시지 -->
              <p v-if="item.message" class="py-2 text-sm text-gray-900 break-all">{{ item.message }}</p>
              <!-- 사진 -->
              <div v-if="item.files && item.files.length > 0" :class="[item.files.length > 1 ? 'grid grid-cols-2 gap-2' : '', 'my-2']">
                <div v-for="(file, index) in item.files" :key="index" @click="zoomImg(file)">
                  <img :src="file.thumbnailUrl" class="h-32 cursor-pointer" alt="Preview">
                </div>
              </div>
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
        <!-- 파일 등록 -->
        <div class="file-input__div" v-show="isOpenFileInput">
          <label for="dropzone-file" class="flex flex-col items-center justify-center w-full border-2 border-gray-300 border-dashed rounded-lg cursor-pointer h-50 bg-gray-50">
            <!-- 파일 image -->
            <div v-if="fileInput && fileInput.length > 0" :class="[fileInput?.length > 1 ? 'grid grid-cols-2 gap-2' : '', 'my-2']">
              <div v-for="(item, index) in fileInput" :key="index">
                <img :src="getPreviewUrl(item)" class="h-32" alt="Preview">
              </div>
            </div>
            <!-- 파일 input -->
            <div class="file-input__discription" v-show="!fileInput">
              <svg class="w-8 h-8 mb-4 text-gray-500" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 16">
                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 13h3a3 3 0 0 0 0-6h-.025A5.56 5.56 0 0 0 16 6.5 5.5 5.5 0 0 0 5.207 5.021C5.137 5.017 5.071 5 5 5a4 4 0 0 0 0 8h2.167M10 15V6m0 0L8 8m2-2 2 2"/>
              </svg>
              <p class="mb-2 text-sm text-gray-500"><span class="font-semibold">Click to upload up to 4</span></p>
              <p class="text-xs text-gray-500">SVG, PNG, JPG or GIF (MAX. 800x400px)</p>
            </div>
            <input id="dropzone-file" type="file" class="hidden" @change="handleFileChange" multiple>
          </label>
        </div> 
        <!-- 전송 버튼 -->
        <button class="send-button" @click="sendMessage()">
          <svg class="w-6 h-6 text-white transform rotate-90" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="m12 18-7 3 7-18 7 18-7-3Zm0 0v-5"/>
          </svg>
        </button>
    </div>

    <!-- 파일 확대 -->
    <div class="zoom-file" v-if="isZoomImg">
      <div class="zoom-file__box">
        <div class="w-full h-full p-5">
          <img :src="zoomImgUrl" alt="image" class="w-full">
        </div>
        <div class="flex justify-center">
          <button class="download-button" @click="fileDownload()">
            <svg class="w-4 h-4 mr-2 fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M13 8V2H7v6H2l8 8 8-8h-5zM0 18h20v2H0v-2z"/></svg>
            <span>Download</span>
          </button>
          <button class="close-file" @click="closeFile()">
            <span>Close</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useAccessTokenStore } from '@/stores/store'
import type { Friend, DM, DmItem } from "@/types/global"
import axios from 'axios'
const { VITE_SERVER_API_URL } = import.meta.env

const store = useAccessTokenStore()
const emit = defineEmits(['closeTab', 'currentDmRoomId'])
const props = defineProps<{
  dmRes: any
}>()

const dmInfo = ref<DM | null>(store.dmInfo)
const receiverInfo = ref<Friend | null>(store.receiverInfo)
const pastDmList = ref<DmItem[]>(store.pastDmList)
const sockDmList = ref<any>([])

const chatRef = ref<HTMLElement | null>(null)
const isOpenFileInput = ref<boolean>(false)

const messageInput = ref<string>('')
const fileInput = ref<File[] | null>(null)
const sender = ref<string>('')
const receiver = ref<string>('')
const dmRoomId = ref<number>(0)

const isZoomImg = ref<boolean>(false)
const zoomImgUrl = ref<string>('')
const zoomImgId = ref<string>('')
const zoomImgName = ref<string>('')

watch(()=> store.dmInfo, (update) => {
  dmInfo.value = update
})

watch(()=> store.receiverInfo, (update) => {
  receiverInfo.value = update
})

watch(()=> store.pastDmList, (update) => {
  pastDmList.value = update
})

watch(() => store.isEntryDmRoom, () => {
  if (store.isEntryDmRoom) {
    if (store.userInfo?.email) {
      sender.value = store.userInfo.email
    }
    if (receiverInfo.value) {
      receiver.value = receiverInfo.value.email
    }
    if (dmInfo.value) {
      dmRoomId.value = dmInfo.value.dmRoomId
      emit('currentDmRoomId', dmRoomId.value)
    }
    refreshChat()
  } 
  else {
    isOpenFileInput.value = false
    fileInput.value = null
    store.pastDmList = []
    sockDmList.value = []
    receiver.value = ''
    store.exitDmRoom(dmRoomId.value)
    closeFile()
  }
})

watch(()=> props.dmRes, () => {
  sockDmList.value.push({
    sender: props.dmRes.sender,
    message: props.dmRes.message,
    files: props.dmRes.files,
    createdAt: props.dmRes.createdAt,
  })
})

const getProfileImage = () => {
  let imgUrl: URL;
  const animal = receiverInfo.value?.animal
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

const getProfileLink = () => {
  return `/profile/${receiverInfo.value?.nickname}`
}

const getHeartClass = () => {
  return receiverInfo.value?.gender === 'man' ? 'w-4 h-4 text-blue-500 ms-1' : 'w-4 h-4 text-pink-500 ms-1';
}

const isSender = (dmSender: string) => {
  if (dmSender === store.userInfo?.email) {
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

const openFileInput = () => {
  isOpenFileInput.value = !isOpenFileInput.value
  fileInput.value = null
}

const handleFileChange = (event: Event) => {
  const selectedFiles = (event.target as HTMLInputElement).files

  if (selectedFiles && selectedFiles.length > 0) {
    const allowedFileTypes = ['image/svg+xml', 'image/jpeg', 'image/png', 'image/gif']

    // 최대 4개의 파일만 허용
    if (selectedFiles.length > 4) {
      window.alert('최대 4개의 파일만 업로드할 수 있습니다.')
      fileInput.value = null
      return
    }

    // FileList를 배열로 변환
    const filesArray: File[] = Array.from(selectedFiles)

    // 모든 파일이 허용된 형식인지 확인
    for (const file of filesArray) {
      if (!allowedFileTypes.includes(file.type)) {
        window.alert('지원하지 않는 형식의 파일이 포함되어 있습니다.')
        fileInput.value = null
        return
      }
    }
    fileInput.value = filesArray
  }
}

const getPreviewUrl = (file: File) => {
  return URL.createObjectURL(file)
}

const zoomImg = (file: any) => {
  isZoomImg.value = true
  zoomImgUrl.value = file.imgUrl
  zoomImgId.value = file.S3Id
  zoomImgName.value = file.fileName
}

const closeFile = () => {
  isZoomImg.value = false
  zoomImgUrl.value = ''
  zoomImgId.value = ''
}

const fileDownload = () => {
  store.fileDownload(zoomImgId.value, zoomImgName.value)
}

function getCurrentTime(): string {
  const koreanOptions: Intl.DateTimeFormatOptions = {
    timeZone: 'Asia/Seoul',
    hour12: false,
    hour: '2-digit',
    minute: '2-digit',
  }

  return new Date().toLocaleTimeString('en-US', koreanOptions)
}

// Web socket -----------------------------------------------
const socket = new SockJS(`${VITE_SERVER_API_URL}/ws`)
const stompClient = Stomp.over(socket)

// 메시지 전송 함수
async function sendMessage() {
  const fileList = ref([])
  if (messageInput.value || (fileInput.value && fileInput.value.length > 0)) {
    isOpenFileInput.value = false
    // FormData 객체 생성
    const formData = new FormData()
    formData.append('dmRoomId', String(dmRoomId.value))
    formData.append('sender', sender.value)
    formData.append('receiver', receiver.value)
    formData.append('message', messageInput.value)

    // 파일들을 FormData에 추가
    if (fileInput.value && fileInput.value.length > 0) {
      for (let i = 0; i < fileInput.value.length; i++) {
        formData.append('files', fileInput.value[i])
      }
    }

    try {
      if (fileInput.value && fileInput.value.length > 0) {
        // Axios를 사용하여 파일 업로드 요청 보내기
        const response = await axios.post(`${VITE_SERVER_API_URL}/api/file/upload`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${store.getAccessToken()}`,
          }
        })
        // fileResDto
        if (response.data && Array.isArray(response.data.result)) {
          fileList.value = response.data.result.map((file: any) => ({
            S3Id: file.S3Id,
            originFileName: file.originFileName,
            fileName: file.fileName,
            imgUrl: file.imgUrl,
            fileDir: file.fileDir,
            thumbnailUrl: file.thumbnailUrl
          }))
        }
      }

      // 메시지 전송
      stompClient.send('/api/pub/dm/message', {}, JSON.stringify({
        type: 'MESSAGE',
        dmRoomId: dmRoomId.value,
        sender: sender.value,
        receiver: receiver.value,
        message: messageInput.value,
        files: fileList.value,
        createdAt: getCurrentTime()
      }))

      sockDmList.value.push({
        sender: sender.value,
        message: messageInput.value,
        files: fileList.value,
        createdAt: getCurrentTime()
      })

      // 입력 필드 초기화
      messageInput.value = ''
      fileInput.value = null
    }
    catch (error) {
      console.error(error)
    }
  }
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
.file-input__div {
  @apply flex items-center justify-center absolute w-full;
  bottom: 70px;
}
.file-input__discription {
  @apply flex flex-col items-center justify-center pt-5 pb-6;
}
.zoom-file {
  @apply absolute flex items-center justify-center w-full h-full;
}
.zoom-file__box {
  @apply w-3/4 rounded-lg bg-gray-300 shadow-lg;
}
.download-button {
  @apply inline-flex items-center px-4 py-2 mb-5 text-white bg-violet-500 rounded hover:bg-violet-600 mx-1;
}
.close-file {
  @apply inline-flex items-center px-4 py-2 mb-5 text-white bg-gray-400 rounded hover:bg-gray-500 mx-1;
}
</style>