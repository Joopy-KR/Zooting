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
  if (newSender.value.includes(sender)) {
    const index = newSender.value.indexOf(sender)
    newSender.value.splice(index, 1)
  }
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
    // 현재 open 된 dmRooId인 경우 메시지 전송
    if (props.dmRoomId === dmReq.dmRoomId) {
      emit('receiveMessage', dmReq)
    } else {
      // 새로운 메시지 알림
      newSender.value.push(dmReq.sender)
    }
    
  })
}
</script>

<style scoped>
.home__container {
  @apply h-screen flex flex-row w-full;
}
</style>