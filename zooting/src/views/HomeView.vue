<template>
  <div class="home__container">
    <Social />
    <Ready />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useAccessTokenStore } from "../stores/store"
import Social from '../components/home/Social.vue'
import Ready from '../components/home/Ready.vue'

const store = useAccessTokenStore()

const userInfo = ref(store.userInfo)

watch(()=> store.userInfo, (UpdateUser)=>{
  userInfo.value = UpdateUser
})

onMounted(async () => {
  if (!store.isCompletedSignUp) {
    store.checkCompletedSignUp()
  } 
})

// Web socket -----------------------------------------------
const { VITE_SERVER_API_URL } = import.meta.env

const socket = new SockJS(`${VITE_SERVER_API_URL}/ws/dm`)
const stompClient = Stomp.over(socket)

const isConnected = ref<boolean>(false)

// 소켓 통신 연결 요청
stompClient.connect(
  {}, 
  () => {
    console.log('Connected to WebSocket')
    onConnected()
    isConnected.value = true
  },
  () => {
    console.log("Could not WebSocket server")
  }
)

// 연결 해제 시 호출
socket.onclose = () => {
    console.log('Disconnected from WebSocket')
    isConnected.value = false
}

// 소켓 클라이언트 Subscribe 요청
const onConnected = () => {
  stompClient.subscribe(`${VITE_SERVER_API_URL}/api/sub/dm/${userInfo.value.email}`,
  (message: any) => {
    const dmReq = JSON.parse(message.body)
    console.log('Received DM:', dmReq)
  })
}
</script>

<style scoped>
.home__container {
  @apply h-screen flex flex-row w-full;
}
</style>