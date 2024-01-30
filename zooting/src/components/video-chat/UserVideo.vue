<template>
	<div v-if="streamManager">
		<ov-video :stream-manager="streamManager"/>
		<div><p>{{ clientData }}</p></div>
	</div>
</template>

<script setup>
import OvVideo from './OvVideo.vue';
import { defineProps, ref, computed } from 'vue';

const props = defineProps(['streamManager']);

const clientData = computed(() => {
  const { clientData } = getConnectionData();
  return clientData;
});

const getConnectionData = () => {
  const { connection } = props.streamManager.stream;
  return JSON.parse(connection.data);
};
</script>