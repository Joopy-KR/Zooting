<template>
  <div class="flex">
    <RouterView
      @get-openvidu-token="isMatchingLoad = false"
    />
    <!-- 다대다 매칭 완료 -->
    <MatchingCompleteModal
      v-if="store.isLogin"
      class="z-50"
      @matching-accept="isMatchingLoad = true"
      />
      <!-- 일대일 매칭 요청 수신 -->
    <MeetingAcceptModal
      v-if="store.isLogin"
      class="z-50"
      />
    <!-- 매칭 종료 후 결과 선택 -->
    <SelecteResultModal
      v-if="store.isLogin"
      class="z-50"
    />
    <!-- 다대다 매칭 대기 -->
    <MatchingLoadModal
      v-if="store.isLogin"
      class="z-50"
      :is-matching-load="isMatchingLoad"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { RouterView } from 'vue-router'
import MatchingCompleteModal from "@/components/MatchingCompleteModal.vue"
import MeetingAcceptModal from "@/components/MeetingAcceptModal.vue"
import SelecteResultModal from '@/components/SelecteResultModal.vue'
import MatchingLoadModal from '@/components/MatchingLoadModal.vue'
import { useAccessTokenStore } from '@/stores/store'

const store = useAccessTokenStore()
const isMatchingLoad = ref<boolean>(false)  // 미팅방 이동 대기
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
