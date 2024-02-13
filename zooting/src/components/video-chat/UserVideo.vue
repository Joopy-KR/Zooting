<template>
	<div v-if="streamManager" class="relative">
		<ov-video
    :stream-manager="streamManager"  
    />
    <div class="absolute bottom-0 bg-red-400">
      {{ nickname }}
      {{ gender }}
    </div>
	</div>
</template>

<script setup>
import { ref, watchEffect } from 'vue'
import OvVideo from './OvVideo.vue'

const props = defineProps({
  streamManager: Object
})

const nickname = ref('')
const gender = ref('')

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

</script>