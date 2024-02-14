<template>
  <div class="flex">
    <RouterView/>
    <MatchingCompleteModal
      v-if="store.isLogin"
      class="z-50"
      />
    <MeetingAcceptModal
      v-if="store.isLogin"
      class="z-50"
      />
    <SelecteResultModal
      v-if="store.isLogin"
      class="z-50"
    />
  </div>
</template>

<script setup lang="ts">

import { RouterView } from 'vue-router'
import MatchingCompleteModal from "@/components/MatchingCompleteModal.vue"
import MeetingAcceptModal from "@/components/MeetingAcceptModal.vue"
import SelecteResultModal from '@/components/SelecteResultModal.vue'
import { useAccessTokenStore } from '@/stores/store'

const store = useAccessTokenStore()
</script>
<style scoped>

@import url("https://hangeul.pstatic.net/hangeul_static/css/nanum-square.css");
* {
  font-family: 'NanumSquareAcb';
  -ms-user-select: none;
  -moz-user-select: -moz-none;
  -webkit-user-select: none;
  -khtml-user-select: none;
  user-select: none;
}
</style>
