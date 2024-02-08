<template>
  <div class="home__container">
    <Social/>
    <Ready />
    <MatchingCompleteModal v-if="isMatchingComplete" class="z-40"/>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useAccessTokenStore, useStore } from "@/stores/store"
import Social from '../components/home/Social.vue'
import Ready from '../components/home/Ready.vue'
import MatchingCompleteModal from "@/components/home/MatchingCompleteModal.vue";
const { VITE_SERVER_API_URL } = import.meta.env

const store = useAccessTokenStore()
const dmStore = useStore()
const props = defineProps<{
  dmRoomId: number
}>()
const emit = defineEmits(['receiveMessage'])

const userInfo = ref(store.userInfo)

// 매칭이 된 경우 모달창이 뜨는 조건
const isMatchingComplete = ref(false)
const socket = new SockJS(`${VITE_SERVER_API_URL}/ws/dm`)
const stompClient = Stomp.over(socket)

watch(()=> store.userInfo, (update)=>{
  userInfo.value = update
})

onMounted(async () => {
  const result = await store.checkCompletedSignUp()
  if (result === 'USER') {
    store.getUserInfo()
  }
})

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
  stompClient.subscribe(`/api/sub/dm/${userInfo.value?.email}`,
  (message: any) => {
    const res = JSON.parse(message.body)
    // DM
    if (res.type === 'message') {
      // 현재 open 된 dmRooId인 경우 메시지 전송
      if (props.dmRoomId === res.dmRoomId) {
        emit('receiveMessage', res)
      } else {
        // 새로운 메시지 알림
        dmStore.newMessage.push(res.sender)
      } 
    } 
    // Meeting
    else {
      console.log(res)
    }
  })
}
</script>

<style scoped>
.home__container {
  @apply h-screen flex flex-row w-full;
}
</style>