<template>
  <div v-show="existRecordList" class="record__container">
    <div class="flex justify-between w-full">
      <div class="button">
        <button v-if="index !== 0" @click="index--">
          <svg class="text-[#FC9AED] cursor-pointer w-10 h-10" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
            <path fill-rule="evenodd" d="M13.7 5.6A2 2 0 0 1 17 7v10a2 2 0 0 1-3.3 1.5l-5.9-4.9a2 2 0 0 1 0-3l6-5Z" clip-rule="evenodd"/>
          </svg>
        </button>
      </div>
      <h1 class="flex justify-center pt-1.5 text-xl">최근 매칭 기록</h1>
      <div class="button">
        <button v-if="checkListLength()" @click="index++">
          <svg class="text-[#FC9AED] cursor-pointer w-10 h-10" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
            <path fill-rule="evenodd" d="M10.3 5.6A2 2 0 0 0 7 7v10a2 2 0 0 0 3.3 1.5l5.9-4.9a2 2 0 0 0 0-3l-6-5Z" clip-rule="evenodd"/>
          </svg>
        </button>
      </div>
    </div>
    <div v-show="existRecordList">
      <ReadyRecordItem  :record-item="recordList[index]"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, watch} from 'vue'
import ReadyRecordItem from '@/components/home/ReadyRecordItem.vue'
import {useAccessTokenStore} from "@/stores/store";
import type {MeetingLog} from "@/types/global";

const index = ref<number>(0)
const existRecordList = ref<boolean>(false);
const store = useAccessTokenStore();
const recordList = ref<MeetingLog[]>([]);

watch(()=> store.recordList, (update) => {
  if (store.recordList != null) {
    recordList.value = update;
    existRecordList.value = true;
  }
})

const checkListLength = function () {
  if (store.recordList != null) {
    return index.value != recordList.value.length - 1;
  }
  return false;
}

</script>

<style scoped>
.record__container {
  @apply flex flex-col p-3;
  @apply border-[#E1BAFF] border-4 rounded-lg shadow-lg m-5 end-0 bg-white;
  width: 300px;
  height: 200px;
}
.button {
  @apply w-7 h-7;
}
</style>
