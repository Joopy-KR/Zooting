<template>
  <div class="home__container">
    <Social 
      :new-sender="newSender"
      @read-message = "readMessage"
    />
    <Ready />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useAccessTokenStore } from "../stores/store"
import Social from '../components/home/Social.vue'
import Ready from '../components/home/Ready.vue'
const { VITE_SERVER_API_URL } = import.meta.env

const store = useAccessTokenStore()

const userInfo = ref(store.userInfo)

const socket = new SockJS(`${VITE_SERVER_API_URL}/ws/dm`)
const stompClient = Stomp.over(socket)

const newSender = ref<string[]>([])

watch(()=> store.userInfo, (UpdateUser)=>{
  userInfo.value = UpdateUser
})

onMounted(async () => {
  if (!store.isCompletedSignUp) {
    store.checkCompletedSignUp()
  } 
})

const readMessage = (sender: string) => {
  const index = newSender.value.indexOf(sender)
  newSender.value.splice(index, 1)
}

// 소켓 통신 연결 요청
stompClient.connect(
  {}, 
  () => {
    console.log('Connected to WebSocket')
    onConnected()
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
    console.log('Received DM:', dmReq.value)

    newSender.value.push(dmReq.sender)
    // 만약 탭이 열려 있으면 메시지 보여줌 emit
    // 닫혀 있는 애들한테는 알림 표시 - props V
  })
}
</script>

<style scoped>
.home__container {
  @apply h-screen flex flex-row w-full;
}
</style>