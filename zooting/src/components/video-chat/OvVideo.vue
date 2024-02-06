<template>
  <video ref="videoElement" class="videoElement" autoplay />
</template>

<script setup>
import { ref, onMounted } from 'vue';

const props = defineProps({
  streamManager: Object,
	cameraHeight: Number,
  cameraWidth: Number
});

const videoElement = ref(null);

onMounted(() => {
	const videoHeight = props.cameraHeight + 'px'
	const videoWidth = props.cameraWidth + 'px'
	videoElement.value.style.height = videoHeight
	videoElement.value.style.width = videoWidth
  props.streamManager.addVideoElement(videoElement.value);
});

</script>

<style scoped>

</style>