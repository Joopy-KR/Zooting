<template>
  <div class="flex items-center h-full mx-2 mt-1 mb-3">
    <RouterLink :to="getProfileLink(nickname)" class="record__profile">
      <img :src="getProfileImage(animal)" alt="profile">
    </RouterLink>
    <div>
      <div class="flex flex-col gap-4 item-center">
        <div class="text-xl underline decoration-pink-300 decoration-wavy">
          {{ nickname }}
        </div>
        <button v-if="!isFriendInList(nickname)" class="flex justify-center w-20 py-1 bg-pink-300 rounded-md shadow-md hover:bg-pink-400" @click="sendFriendRequest(nickname)">친구 신청</button>
        <div v-else class="text-sm text-gray-400">이미 친구인 유저입니다</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { watch, ref } from "vue"
import { useAccessTokenStore, useStore } from '@/stores/store'

const props = defineProps(["recordItem"])
const store = useAccessTokenStore()
const animal = ref("")
const nickname = ref("")

watch(()=> props.recordItem, (update) => {
   animal.value = props.recordItem.animal
   nickname.value = props.recordItem.nickName

})

const getProfileLink = (value: string) => `/profile/${value}`

const getProfileImage = (animal: string) => {
  let imgUrl: URL;
  if (animal === '강아지') {
    imgUrl = new URL('/assets/images/animal/dog.png', import.meta.url);
  } else if (animal === '고양이') {
    imgUrl = new URL('/assets/images/animal/cat.png', import.meta.url);
  } else if (animal === '곰') {
    imgUrl = new URL('/assets/images/animal/bear.png', import.meta.url);
  } else if (animal === '공룡') {
    imgUrl = new URL('/assets/images/animal/dino.png', import.meta.url);
  } else if (animal === '펭귄') {
    imgUrl = new URL('/assets/images/animal/penguin.png', import.meta.url);
  } else if (animal === '토끼') {
    imgUrl = new URL('/assets/images/animal/rabbit.png', import.meta.url);
  } else if (animal === '사슴') {
    imgUrl = new URL('/assets/images/animal/deer.png', import.meta.url);
  } else {
    imgUrl = new URL('/assets/images/animal/animal_group.png', import.meta.url);
  }
  return imgUrl.href;
}

const sendFriendRequest = (nickname: string) => {
  store.friendRequest(nickname)
}

const isFriendInList = (nickname: string) => {
  return store.friendList.some(item => item.nickname === nickname)
}
</script>

<style scoped>
.record__profile {
  @apply rounded-full bg-white border-gray-200 shadow-lg p-1 shadow-pink-200 me-3;
  width: 90px;
  height: 90px;
}
</style>