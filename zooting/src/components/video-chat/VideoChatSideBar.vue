<template>
    <div class="main">
      <div class="main__info">
        <div class="main__room-time">	
        <div>
          <span>{{ min }}</span> :
          <span>{{ sec }}</span>
        </div>
        </div>
        <div class="flex m-2">
          <img
           v-for="(animal, index) in $props.currentAnimals" :key="index"
           class="w-8 h-8 rounded-full" 
           :src="getProfileImage(animal)"
           alt="프로필 사진">
        </div>
      </div>
      <div class="main__title">
        <p>{{ props.statusInfo }}</p>
      </div>
      <div class="unzo">
        <div class="main__body">
          <!-- 개별 채팅 -->
          <div class="main__body--single-chat" v-for="msg in $props.currentChat" :key="msg">
            <img class="w-8 h-8 rounded-full" :src="getProfileImage(msg.animal)" alt="프로필 사진">
            <!-- 성별 아이콘 -->
            <div class="absolute gender-icon left-3.5 top-5">
              <svg :class="msg.gender === 'man' ? 'w-3 h-3 text-blue-500 ms-1' : 'w-3 h-3 text-pink-500 ms-1'" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                <path d="m12.7 20.7 6.2-7.1c2.7-3 2.6-6.5.8-8.7A5 5 0 0 0 16 3c-1.3 0-2.7.4-4 1.4A6.3 6.3 0 0 0 8 3a5 5 0 0 0-3.7 1.9c-1.8 2.2-2 5.8.8 8.7l6.2 7a1 1 0 0 0 1.4 0"/>
              </svg>
            </div>
            <div class="main__body--chat-context">
              <!-- 채팅 이름 -->
              <span class="text-sm font-semibold text-gray-900">{{ msg.nickname }}: </span>
              <!-- 채팅 내용 -->
              <span class="text-sm font-normal py-2.5 text-gray-900">{{ msg.message }}</span> 
            </div>
          </div>
        </div>
      </div>
      <div class="main__feature">
        <!-- 음소거 버튼 -->
        <div class="w-6 h-6">
          <font-awesome-icon :icon="['fas', 'microphone']" v-show="!is_muted" @click="doMute()" style="cursor: pointer; margin-left: 4px;"/>
          <font-awesome-icon :icon="['fas', 'microphone-slash']" v-show="is_muted" @click="doUnmute()" style="cursor: pointer;"/>
        </div>
        
        <form @submit.prevent="send()" class="flex justify-between flex-grow">
          <!-- 텍스트 입력창 -->
          <input type="text" :value="inputChat" @input="onInput" placeholder="Say something" class="main__feature--input">
        
          <!-- 전송 버튼 -->
          <button class="main__feature--send-button">
            <svg class="w-6 h-6 text-white transform rotate-90" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="m12 18-7 3 7-18 7 18-7-3Zm0 0v-5"/>
            </svg>
          </button>
        </form>
    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'


// 타이머 변수 및 함수
let stTime = ref(0)
let timerStart = ref(null)
let min = ref('00')
let sec = ref('00')

const addZero = (num) => (num < 10 ? '0' + num : '' + num)

const StartWatch = () => {
  if (!stTime.value) {
    stTime.value = Date.now()
  } 
  
  if (!timerStart.value) {
    timerStart.value = setInterval(() => {
      const nowTime = new Date(Date.now() - stTime.value)
      min.value = addZero(nowTime.getMinutes())
      sec.value = addZero(nowTime.getSeconds())
    }, 1)
  }
};

onMounted(() => {
  StartWatch()
})

onUnmounted(() => {
  if (timerStart.value) {
    clearInterval(timerStart.value);
  }
})

// 채팅 기능
const props = defineProps({
  session: Object,
  currentChat: Object,
  currentAnimals: Object,
  publisher: Object,
  subscribers: Object,
  statusInfo: String,
  })

const inputChat = ref('')
const onInput = function(event) {
  inputChat.value = event.currentTarget.value
}

const send = function() {
  props.session?.signal({
    data: inputChat.value,  // 현재 입력한 내용
    to: [],                 // [] - Broadcast
  })
  .then(() => {
    inputChat.value = ''  // 현재 입력한 내용 지우기
  })
  .catch(error => {
    console.error("메시지 전송에 실패했어요..." + error)
  })
}

// 음소거 기능
const is_muted = ref(true)

const doMute = function() {
  console.log(props.publisher.publishAudio)
  props.publisher.publishAudio(false)
  is_muted.value = true
}

const doUnmute = function() {
  props.publisher.publishAudio(true)
  is_muted.value = false
}

// 프로필 이미지 경로구하는 함수
const getProfileImage = (animal: String) => {
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
</script>

<style scoped>
.main {
  @apply border-gray-300 border-solid border-s-2 h-screen flex flex-col;
}

.main__info {
  @apply flex items-center justify-between border-b-2 border-gray-300 bg-violet-500 shadow-sm;
  height: 60px;
  min-height: 60px;
}

.main__room-num {
  @apply ml-5 font-bold text-white
}

.main__room-time {
  @apply text-white m-4 font-semibold 
}

.main__title {
  @apply flex items-center self-center justify-center h-14 bg-white z-10 font-extrabold border-2 rounded-md shadow-md border-violet-400 px-10 w-full mt-1;
  height: 60px;
  min-height: 60px;
}

.main__body {
  @apply flex flex-col items-start justify-end;
  /* overflow-y: auto; */
}

.unzo::-webkit-scrollbar {
  @apply hidden;
}

.main__body--single-chat {
  @apply flex justify-center items-center ml-4 mt-2 mb-2 gap-1 relative;
}

.main__body--chat-context {
  max-width: 270px
}

.main__feature {
  @apply flex items-center h-14 border border-gray-300 rounded-lg py-7 px-3 mx-3 mb-5 mt-1 shadow-md;
}

.main__feature--input {
  @apply flex-grow border border-none rounded-md;
}

.main__feature--send-button {
  @apply cursor-pointer bg-violet-500 p-2 ms-2 rounded-3xl hover:bg-violet-600 shadow-lg;
}

.unzo {
  @apply flex-grow flex flex-col-reverse;
  overflow-y: auto;
  /* overflow: hidden; */
}
</style>  