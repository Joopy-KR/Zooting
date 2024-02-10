<template>
  <div class="home__container">
    <Social/>
    <Ready/>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useAccessTokenStore, useStore } from "@/stores/store"
import Social from '@/components/home/Social.vue'
import Ready from '@/components/home/Ready.vue'
const { VITE_SERVER_API_URL } = import.meta.env

const store = useAccessTokenStore()
const dmStore = useStore()
const props = defineProps<{
  dmRoomId: number
}>()
const emit = defineEmits(['receiveMessage', 'matchingComplete'])

const userInfo = ref(store.userInfo)

const socket = new SockJS(`${VITE_SERVER_API_URL}/ws/dm`)
const stompClient = Stomp.over(socket)

watch(()=> store.userInfo, (update)=>{
  userInfo.value = update
})

onMounted(async () => {
  const result = await store.checkCompletedSignUp()
  if (result === 'USER') {
    await store.getUserInfo()
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
    // MESSAGE
    if (res.type === 'MESSAGE') {
      // 현재 open 된 dmRooId인 경우 메시지 전송
      if (props.dmRoomId === res.dmRoomId) {
        emit('receiveMessage', res)
      } else {
        // 새로운 메시지 알림
        dmStore.newMessage.push(res.sender)
      } 
    } 
    // 매칭 완료
    else if (res.type === 'match') {
      emit('matchingComplete', res.roomId)
      store.isMatchingComplete = true
      store.matchingEnd()
    }
    // 매칭 수락
    else if (res.type === 'openviduToken') {
      store.pushMeetingRoom(res.token)
    }
  })
}
</script>

<style scoped>
.home__container {
  @apply h-screen flex flex-row w-full;
}
</style>