<template>
	<div v-if="streamManager">
		<ov-video :stream-manager="streamManager"/>
		<div><p>{{ clientData }}</p></div>
	</div>
</template>

<script setup lang="ts">
import OvVideo from './OvVideo.vue';
import { defineProps, computed } from 'vue';

const props = defineProps<{
  streamManager: any
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