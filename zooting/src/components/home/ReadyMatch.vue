<template>
  <div class="match__container">
    <div class="mt-20 match__page">
      <ReadyState/>
      <div class="button drag-prevent" @click="meetingRegister"><p>MATCHING</p></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import axios from 'axios'
const { VITE_SERVER_API_URL } = import.meta.env
import ReadyState from '@/components/home/ReadyState.vue'
import { useAccessTokenStore } from '@/stores/store'

const store = useAccessTokenStore()

const meetingRegister = function () {
	axios({
		method: "post",
		url: `${VITE_SERVER_API_URL}/api/meeting/register`,
		headers: {
			Authorization: `Bearer ${store.getAccessToken()}`,
		},
	})
	.then((res) => {
		console.log(res);
	})
	.catch((err) => {
		console.log(err);
	});
};
</script>

<style lang="scss" scoped>
.match__container {
  @apply flex flex-col items-center col-span-3 col-start-3 py-5;
}
.match__page {
  @apply flex flex-grow justify-center items-center;
}

.match__ready {
  @apply h-32 flex items-center;
}
.drag-prevent{
  -ms-user-select: none;
  -moz-user-select: -moz-none;
  -webkit-user-select: none;
  -khtml-user-select: none;
  user-select: none;
}
.button {
	text-align: center;
	font-size: 42px;
  font-weight: 700;
	color: #5647ab;
	width: 350px;
	height: 100px;

	border-radius: 30px;
	text-shadow: 
		0px 5px hsl(55, 100%, 74%), 
		0px 10px 10px #0003;
	box-shadow: 
		0px 0px 0px 15px #7360DF,
		0px 10px 0px 15px #5647ab,
		0px 20px 20px 15px #0003;
	cursor: pointer;
	border-radius: 100px 30px 100px 30px;

}

.button p {
	margin-top: 20px;
	transform: rotate(-3deg);
}

.button:hover {
	animation: .8s morph ease infinite;
}

.button:hover p {
	animation: .8s rot ease infinite;
}

@keyframes morph {
	0% {
		border-radius: 100px 30px 100px 30px;
	} 50% {
		border-radius: 30px 100px 30px 100px;
	} 100% {
		border-radius: 100px 30px 100px 30px;
	}
}

@keyframes rot {
	0% {
		transform: rotate(-3deg);
	} 50% {
		transform: rotate(3deg);
	} 100% {
		transform: rotate(-3deg);
	}
}
</style>