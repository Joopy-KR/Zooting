<template>
    <div class="main">
      <div class="main__info">
        <p class="main__room-time">3:12</p>
        <div class="flex m-2">
          <img class="w-8 h-8 rounded-full" src="/images/강아지.png" alt="프로필 사진">
          <img class="w-8 h-8 rounded-full" src="/images/고양이.png" alt="프로필 사진">
          <img class="w-8 h-8 rounded-full" src="/images/사슴.png" alt="프로필 사진">
          <img class="w-8 h-8 rounded-full" src="/images/토끼.png" alt="프로필 사진">
        </div>
      </div>
      <div class="main__title">
        <p>자유 대화 시간</p>
      </div>
      <div class="main__body">
        <!-- 개별 채팅 -->
        <div class="main__body--single-chat" v-for="msg in messages" :key="msg">
          <img class="w-8 h-8 rounded-full" src="/images/토끼.png" alt="프로필 사진">
          <!-- 성별 아이콘 -->
          <div class="absolute gender-icon left-3.5 top-5">
            <svg :class="getHeartClass()" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
              <path d="m12.7 20.7 6.2-7.1c2.7-3 2.6-6.5.8-8.7A5 5 0 0 0 16 3c-1.3 0-2.7.4-4 1.4A6.3 6.3 0 0 0 8 3a5 5 0 0 0-3.7 1.9c-1.8 2.2-2 5.8.8 8.7l6.2 7a1 1 0 0 0 1.4 0"/>
            </svg>
          </div>
          <div class="main__body--chat-context">
            <!-- 채팅 이름 -->
            <span class="text-sm font-semibold text-gray-900 dark:text-white">나는세진: </span>
            <!-- 채팅 내용 -->
            <span class="text-sm font-normal py-2.5 text-gray-900 dark:text-white">{{ msg }}</span> 
          </div>
        </div>
      </div>
      <div class="main__feature">
        <!-- 음소거 버튼 -->
        <div class="w-6 h-6">
          <font-awesome-icon :icon="['fas', 'microphone']" v-show="!is_muted" @click="is_muted = !is_muted" style="cursor: pointer; margin-left: 4px;"/>
          <font-awesome-icon :icon="['fas', 'microphone-slash']" v-show="is_muted" @click="is_muted = !is_muted" style="cursor: pointer;"/>
        </div>

        <!-- 텍스트 입력창 -->
        <input type="text" v-model="inputChat" placeholder="Say something" class="main__feature--input">

        <!-- 전송 버튼 -->
        <button class="main__feature--send-button" @click.prevent="send()">
          <svg class="w-6 h-6 text-white transform rotate-90" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="m12 18-7 3 7-18 7 18-7-3Zm0 0v-5"/>
          </svg>
        </button>
    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, watch, defineProps } from 'vue'
import { useAccessTokenStore } from "@/stores/store"

const store = useAccessTokenStore()
const receiverInfo = ref<any>(store.receiverInfo)

const props = defineProps(['session'])
const session = props.session

const inputChat = ref('')
const messages = ref([])

console.log(props)


const send = function() {
  props.session?.signal({
    data: inputChat.value,  // Any string (optional)
    to: [],                     // Array of Connection objects (optional. Broadcast to everyone if empty)
    type: 'my-chat'             // The type of message (optional)
  })
  .then(() => {
    console.log('Message successfully sent')
    messages.value.push(inputChat.value)
    inputChat.value = ''
  })
  .catch(error => {
    console.error(error);
  });
}


watch(()=> store.receiverInfo, (UpdateUser)=>{
  receiverInfo.value = UpdateUser
})

const is_muted = ref(true)

const getHeartClass = () => {
  return receiverInfo.value?.gender === 'man' ? 'w-3 h-3 text-blue-500 ms-1' : 'w-3 h-3 text-pink-500 ms-1';
}

</script>

<style scoped>
.main {
  @apply border-gray-300 border-solid border-s-2 h-screen flex flex-col;
}

.main__info {
  @apply flex items-center justify-between border-b-2 border-gray-300 bg-violet-500 shadow-sm;
  height: 60px;
  max-height: 60px
}

.main__room-num {
  @apply ml-5 font-bold text-white
}

.main__room-time {
  @apply text-white m-4 font-semibold 
}

.main__title {
  @apply flex items-center self-center justify-center h-14 bg-white z-10 font-extrabold border-2 rounded-md shadow-md border-violet-400 px-10 w-full mt-1;
}

.main__body {
  @apply flex flex-col flex-grow items-start justify-end;
  overflow-y: auto;
}

.main__body::-webkit-scrollbar {
  @apply hidden;
}

.main__body--single-chat {
  @apply flex justify-center ml-4 mt-2 mb-2 gap-1 relative;
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

</style>  