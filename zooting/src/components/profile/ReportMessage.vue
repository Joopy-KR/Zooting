<script setup lang="ts">
import {ref} from "vue";
import {Dialog, DialogPanel, TransitionChild, TransitionRoot,} from "@headlessui/vue";
import type {ReportUserReq} from "@/types/global";
import {reportUserApi} from "@/api/report";

const emits = defineEmits([
  "setIsOpenReportDialog",
]);
const props = defineProps({
  isOpenReportDialog: Boolean,
  nickname: String,
});

const message = ref<string>("");

const closeDialog = () => {
  emits("setIsOpenReportDialog", false);
};
const updateMessage = (event: any) => {
  message.value = event.target.value;
};

// 유저 신고하기
const reportUser = (nickname: string | undefined, reason: string) => {
  if (!nickname) return;
  const reportUserReq: ReportUserReq = {
    nickname: nickname,
    reason: reason,
  };
  reportUserApi(
      reportUserReq,
      ({data}:any) => {},
      (error: any) => console.log(error)
  )
  closeDialog();
}

</script>

<template>
  <TransitionRoot as="template" :show="isOpenReportDialog">
    <Dialog as="div" class="relative z-10" @close="closeDialog">
      <TransitionChild
          as="template"
          enter="ease-out duration-300"
          enter-from="opacity-0"
          enter-to="opacity-100"
          leave="ease-in duration-200"
          leave-from="opacity-100"
          leave-to="opacity-0"
      >
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"/>
      </TransitionChild>

      <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
        <div
            class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0"
        >
          <TransitionChild
              as="template"
              enter="ease-out duration-300"
              enter-from="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
              enter-to="opacity-100 translate-y-0 sm:scale-100"
              leave="ease-in duration-200"
              leave-from="opacity-100 translate-y-0 sm:scale-100"
              leave-to="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
          >
            <DialogPanel
                class="relative transform overflow-hidden rounded-lg bg-white px-4 pb-4 pt-5 text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-lg sm:p-6"
            >
              <div class="bg-white shadow sm:rounded-lg">
                <div class="px-4 py-5 sm:p-6">
                  <h3 class="font-semibold pb-3 leading-6 text-base text-gray-900">
                    `{{ nickname }}` 신고
                  </h3>
                  <div class="w-full">
                    <div>
                      <label for="introduce" class="sr-only">message</label>
                      <textarea
                          name="report"
                          id="report"
                          class="block w-full rounded-md border-0 py-3.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-lg sm:leading-6 resize-none"
                          placeholder="신고사유 입력"
                          style="height: calc(3.5rem + 50px)"
                          v-model="message"
                          @input="updateMessage"
                      ></textarea>
                    </div>
                  </div>
                </div>
              </div>
              <div
                  class="mt-5 sm:mt-6 sm:grid sm:grid-flow-row-dense sm:grid-cols-2 sm:gap-3"
              >
                <button
                    type="button"
                    class="inline-flex w-full justify-center rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 sm:col-start-2"
                    @click="reportUser(nickname, message)"
                >
                  신고하기
                </button>
                <button
                    type="button"
                    class="mt-3 inline-flex w-full justify-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 sm:col-start-1 sm:mt-0"
                    @click="closeDialog"
                    ref="cancelButtonRef"
                >
                  취소
                </button>
              </div>
            </DialogPanel>
          </TransitionChild>
        </div>
      </div>
    </Dialog>
  </TransitionRoot>
</template>
