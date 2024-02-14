<template>
  <div class="record__container">
    <div class="button">
      <button v-show="index !== 0" @click="index--">
        <svg class="text-[#FC9AED] cursor-pointer w-10 h-10" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
          <path fill-rule="evenodd" d="M13.7 5.6A2 2 0 0 1 17 7v10a2 2 0 0 1-3.3 1.5l-5.9-4.9a2 2 0 0 1 0-3l6-5Z" clip-rule="evenodd"/>
        </svg>
      </button>
    </div>
    <ReadyRecordItem
    :record-item=recordList[index]
    />
    <div class="button">
      <button v-show="index !== recordList.length - 1" @click="index++">
        <svg class="text-[#FC9AED] cursor-pointer w-10 h-10" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
          <path fill-rule="evenodd" d="M10.3 5.6A2 2 0 0 0 7 7v10a2 2 0 0 0 3.3 1.5l5.9-4.9a2 2 0 0 0 0-3l-6-5Z" clip-rule="evenodd"/>
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref, watch} from 'vue'
import ReadyRecordItem from '@/components/home/ReadyRecordItem.vue'
const index = ref<number>(0)
import {useAccessTokenStore} from "@/stores/store";
import type {MeetingLog} from "@/types/global";
const store = useAccessTokenStore();
let recordList =  ref<MeetingLog[]>([]);



watch(()=> store.recordList, (update) => {
  recordList.value = update;
})

</script>

<style scoped>
.record__container {
  @apply flex justify-between items-center p-3;
  @apply border-[#E1BAFF] border-4 rounded-lg shadow-lg m-5 end-0;
  width: 300px;
  height: 200px;
  background-color: #FEDAF0;
}
.button {
  @apply w-7 h-7;
}
</style>
