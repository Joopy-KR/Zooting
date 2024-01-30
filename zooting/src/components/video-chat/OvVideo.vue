<template>
	<video autoplay/>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';

const props = defineProps(['streamManager']);
const videoRef = ref();

onMounted(() => {
  props.streamManager.addVideoElement(videoRef.value);
})

</script>
