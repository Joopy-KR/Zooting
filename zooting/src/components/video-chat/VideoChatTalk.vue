<template>
  <div class="talk__container">
    <div id="video-container" class="flex flex-col justify-center flex-grow" v-if="session">
      <div class="grid grid-cols-2 gap-5 my-4">
        <user-video :stream-manager="publisher"/>
        <user-video v-for="sub in subscribers" :key="sub.stream.connection.connectionId" :stream-manager="sub"/>
      </div>
    </div>
    <div class="bottom-bar">
      <button class="mic-video-button" @click="toggleCamera()">
        <font-awesome-icon :icon="['fas', isVideo? 'video' : 'video-slash']"/>
      </button>
      <button class="mic-video-button" @click="toggleMute()">
        <font-awesome-icon :icon="['fas', isMuted? 'microphone-slash': 'microphone']"/>
      </button>
      <button class="leave-button" @click="leaveChat()">
        <font-awesome-icon :icon="['fas', 'phone']" />
      </button>
    </div>
  </div>

</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import UserVideo from '@/components/video-chat/UserVideo.vue' 

const props = defineProps({
  session: Object,
  publisher: Object,
  subscribers: Object,
})

const isMuted = ref<boolean>(false)
const isVideo = ref<boolean>(true)

const router = useRouter()

const toggleMute = () => {
  if (isMuted.value) {
    isMuted.value = false
    props.publisher?.publishAudio(false)
  } else {
    isMuted.value = true
    props.publisher?.publishAudio(true)
  }
}

const toggleCamera = () => {
  if (isVideo.value) {
    isVideo.value = false
    props.publisher?.publishVideo(false)
  } else {
    isVideo.value = true
    props.publisher?.publishVideo(true)
  }
}

const leaveChat = () => {
  router.push({name: 'home'})
}
</script>


<style scoped>
.talk__container {
  @apply flex flex-col items-center justify-center;
}
.bottom-bar {
  @apply flex gap-3 justify-center w-full h-14;
}
.mic-video-button {
  @apply w-12 h-12 bg-slate-800 text-white rounded-full;
}
.leave-button {
  @apply w-12 h-12 bg-red-600 text-white rounded-full;
}
</style>  