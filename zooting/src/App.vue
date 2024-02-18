<template>
  <div class="text-black bg-white">
    <RouterView/>
    <!-- 다대다 매칭 완료 -->
    <MatchingCompleteModal
v-if="store.isLogin"
      />
      <!-- 일대일 매칭 요청 수신 -->
    <MeetingAcceptModal
      v-if="store.isLogin"
      />
    <!-- 매칭 종료 후 선택 결과 -->
    <SelecteResultModal
      v-if="store.isLogin"
    />
    <!-- 다대다 매칭 대기 -->
    <MatchingLoadModal
      v-if="store.isLogin"
    />
  </div>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import MatchingCompleteModal from "@/components/MatchingCompleteModal.vue"
import MeetingAcceptModal from "@/components/MeetingAcceptModal.vue"
import SelecteResultModal from '@/components/SelecteResultModal.vue'
import MatchingLoadModal from '@/components/MatchingLoadModal.vue'
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
