<template>
  <div class="home__container">
    <Social/>
    <Ready />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useAccessTokenStore } from "@/stores/store"
import Social from '../components/home/Social.vue'
import Ready from '../components/home/Ready.vue'
const { VITE_SERVER_API_URL } = import.meta.env

const store = useAccessTokenStore()
const props = defineProps<{
  dmRoomId: number
}>()
const emit = defineEmits(['receiveMessage'])

const userInfo = ref(store.userInfo)

// 매칭이 된 경우
const isMatchingComplete = ref(true)

const socket = new SockJS(`${VITE_SERVER_API_URL}/ws/dm`)
const stompClient = Stomp.over(socket)

watch(()=> store.userInfo, (update)=>{
  userInfo.value = update
})

onMounted(async () => {
  if (!store.isCompletedSignUp) {
    store.checkCompletedSignUp()
  }
})

// 소켓 통신 연결 요청
stompClient.connect(
  {},
  () => {
    console.log('Connected to WebSocket')
    localStorage.setItem('newSender', JSON.stringify([]))
    // if (store.isCompletedSignUp) {
    //   onConnected()
    // }
  },
  () => {
    console.log("Could not WebSocket server")
  }
)

// 연결 해제 시 호출
socket.onclose = () => {
    console.log('Disconnected from WebSocket')
}

const onConnected = () => {
  stompClient.subscribe(`/api/sub/dm/${userInfo.value.email}`,
  (message: any) => {
    const dmReq = JSON.parse(message.body)
    // 현재 open 된 dmRooId인 경우 메시지 전송
    if (props.dmRoomId === dmReq.dmRoomId) {
      emit('receiveMessage', dmReq)
    } else {
      // 새로운 메시지 알림
      const existingNewSenders: string[] = JSON.parse(localStorage.getItem('newSender') || '[]')
      const newSender = dmReq.sender
      existingNewSenders.push(newSender)
      localStorage.setItem('newSender', JSON.stringify(existingNewSenders))
    }
    
  })
}
</script>

<style scoped>
.home__container {
  @apply h-screen flex flex-row w-full;
}
</style>