<template>
	<div v-if="streamManager" class="relative">
		<ov-video
      :stream-manager="streamManager"  
      @mouseover="showInfo = true"
      @mouseleave="showInfo = false"
    />
    <div 
      class="info" 
      v-show="showInfo"
      @mouseover="showInfo = true"
      @mouseleave="showInfo = false"
      >
      <span>{{ nickname }}</span>
      <span>({{ getGender }})</span>
    </div>
	</div>
</template>

<script setup lang="ts">
import { ref, watchEffect, computed } from 'vue'
import OvVideo from '@/components/video-chat/OvVideo.vue'

const props = defineProps({
  streamManager: Object
})

const nickname = ref<string>('')
const gender = ref<string>('')
const showInfo = ref<boolean>(false)

watchEffect(() => {
  if (props.streamManager && props.streamManager.stream && props.streamManager.stream.connection && props.streamManager.stream.connection.data) {
    try {
      const data = JSON.parse(props.streamManager.stream.connection.data)
      nickname.value = data.nickname
      gender.value = data.gender
    } catch (error) {
      console.error(error)
    }
  }
});

const getGender = computed(() => gender.value === 'man' ? '남자' : '여자')
</script>

<style scoped>
.info {
  @apply absolute bottom-0 bg-slate-600 text-white flex gap-1 px-3 opacity-10;
}
</style>