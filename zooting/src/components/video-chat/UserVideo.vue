<template>
	<div v-if="streamManager">
		<ov-video
    :stream-manager="streamManager"  
    :cameraHeight="cameraHeight" 
    :cameraWidth="cameraWidth"
    @send-canvas="sendCanvas"
    />
	</div>
</template>

<script setup lang="ts">
import OvVideo from './OvVideo.vue';

defineProps<{
  streamManager: any
  cameraHeight: Number
  cameraWidth: Number
}>()

const emit = defineEmits(['sendCanvas'])

const sendCanvas = function(canvas) {
  emit('sendCanvas', canvas)
}
</script>