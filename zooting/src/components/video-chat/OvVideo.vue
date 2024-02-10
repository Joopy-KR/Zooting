<template>
  <video ref="videoElement" class="videoElement" autoplay />
</template>

<script setup>
import { ref, onMounted } from 'vue';

const props = defineProps({
  streamManager: Object
});

const videoElement = ref(null);

onMounted(() => {
  props.streamManager.addVideoElement(videoElement.value);
});

</script>

<style scoped>

</style>