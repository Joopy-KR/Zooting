<template>
	<div v-if="streamManager">
		<ov-video
    :stream-manager="streamManager"  
    :cameraHeight="cameraHeight" 
    :cameraWidth="cameraWidth"/>
		<!-- <div><p>{{ clientData }}</p></div> -->
	</div>
</template>

<script setup lang="ts">
import OvVideo from './OvVideo.vue';
import { computed } from 'vue';

const props = defineProps<{
  streamManager: any
  cameraHeight: Number
  cameraWidth: Number
}>()

const clientData = computed(() => {
  const { clientData } = getConnectionData();
  return clientData;
});

const getConnectionData = () => {
  const { connection } = props.streamManager.stream;
  return JSON.parse(connection.data);
};
</script>