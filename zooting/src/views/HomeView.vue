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
  // store.getUserInfo()
  connect()
})

// Web socket -----------------------------------------------
import SockJS from "sockjs-client"
import Stomp from "stompjs"
const { VITE_SERVER_API_URL } = import.meta.env

const socket = ref<any>(null)
const stompClient = ref<any>(null)

// 소켓 통신 연결 요청
const connect = () => {
  console.log("Tring to open connection")

  if (!store.getAccessToken()) {
    console.log("not found access token")
    return
  }
  socket.value = new SockJS(`${VITE_SERVER_API_URL}/ws/dm`)
  stompClient.value = Stomp.over(socket.value)
  console.log(stompClient.value.connect())

  stompClient.value.connect(
      () => {
        console.log("Connected from WebSocket")
        // onConnected()
      },
      () => {
        console.log("Could not WebSocket server")
      }
  )
}

// 소켓 클라이언트 Subscribe 요청
// const onConnected = () => {
//   stompClient.value.subscribe(`${VITE_SERVER_API_URL}/api/sub/dm/${userInfo.value.email}`,
//   (message: any) => {
//     const dmReq = JSON.parse(message.body)
//     console.log('Received DM:', dmReq)
//   })
// }

</script>

<style scoped>
.home__container {
  @apply h-screen flex flex-row w-full;
}
</style>