<template>
  <div class="home__container">
    <Social/>
    <Ready/>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref, watch} from 'vue'
import {useAccessTokenStore, useStore} from "@/stores/store"
import Social from '@/components/home/Social.vue'
import Ready from '@/components/home/Ready.vue'
import DmSound from '/assets/sounds/dm.mp3'
const { VITE_SERVER_API_URL } = import.meta.env

const store = useAccessTokenStore()
const dmStore = useStore()
const props = defineProps<{
  dmRoomId: number
}>()
const emit = defineEmits(['receiveMessage'])

const userInfo = ref(store.userInfo)

// @ts-ignore
const socket = new SockJS(`${VITE_SERVER_API_URL}/ws`)
// @ts-ignore
const stompClient = Stomp.over(socket)
let intervalId: any;
const START_HEART_CHECK = 5 * 1000;
const HEART_CHECK_INTERVAL = 30 * 1000;
const intervalTime = ref<number>(START_HEART_CHECK);

const dmSound = new Audio(DmSound)

function playSound(sound:any) {
    sound.currentTime = 0
    sound.play()
}

watch(() => store.userInfo, (update) => {
  userInfo.value = update
})

onMounted(async () => {
  const result = await store.checkCompletedSignUp()
  if (result === 'USER') {
    await store.getUserInfo()
  }
  startHeartbeat();
})

const startHeartbeat = () => {
  intervalId = setInterval(() => {
    stompClient.send('/api/pub/member/heartbeat', {}, JSON.stringify({
      memberId: userInfo.value?.email,
      nickname: userInfo.value?.nickname,
    }));
  }, intervalTime.value);
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
  stompClient.subscribe(`/api/sub/dm/${userInfo.value?.email}`,
  (message: any) => {
    const type = JSON.parse(message.body).type;
    const res = JSON.parse(message.body).result;
    // MESSAGE
    if (type === 'MESSAGE') {
      // 현재 open 된 dmRooId인 경우 메시지 전송
      if (props.dmRoomId === res.dmRoomId) {
        emit('receiveMessage', res)
      } else {
        // 새로운 메시지 알림
        dmStore.newMessage.push(res.sender)
        playSound(dmSound)
      } 
    } 
    // 매칭 완료
    else if (type === 'MATCH') {
          store.MatchingComplete()
    }
    // 매칭 수락
    else if (type === 'OPENVIDU') {
          store.pushMeetingRoom(res.token)
    }
    // 유저 상태 정보
    else if (type == 'HEARTBEAT') {
      if (intervalTime.value === START_HEART_CHECK) {
        clearInterval(intervalId);
        intervalTime.value = HEART_CHECK_INTERVAL;
        startHeartbeat();
      }
      const onlineFriends: String[] = res.onlineFriends;
      for (const friend of store.friendList) {
        friend.isOnline = onlineFriends.includes(friend.nickname);
      }
    }
    // 일대일 미팅 요청 수신
    else if (type === 'MEETING') {
      store.meetingSender = res.nickname
      store.isRecieveMeeting = true
    }
    // 미팅 무응답
    else if (type === 'REJECT') {
      
    }
  })
}
</script>

<style scoped>
.home__container {
  @apply h-screen flex flex-row w-full;
}
</style>