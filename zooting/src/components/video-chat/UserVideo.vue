<template>
	<div v-if="streamManager">
		<ov-video
    :stream-manager="streamManager"  
    />
	</div>
</template>

<script setup>
import OvVideo from './OvVideo.vue';

defineProps({
  streamManager: Object
})

</script>