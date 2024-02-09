<script setup lang="ts">
import {onMounted, ref} from "vue";
import {Dialog, DialogPanel, TransitionChild, TransitionRoot,} from "@headlessui/vue";
import {updateIntroduceApi} from "@/api/profile";
import SuccessNotification from "@/components/util/SuccessNotification.vue";
import FailNotification from "@/components/util/FailNotification.vue";

const emits = defineEmits([
  "closeEditIntroduce",
  "loadMyInfo",
]);
const props = defineProps({
  isOpenEditIntroduce: Boolean,
  introduce: String,
});

const introduceValue = ref<string>("");

const closeDialog = () => {
  emits("closeEditIntroduce");
};
const updateIntroduceValue = (event: any) => {
  introduceValue.value = event.target.value;
};

const updateIntroduce = () => {
  if (!introduceValue.value) {
    return;
  }

  updateIntroduceApi(
      {
        introduce: introduceValue.value,
      },
      ({data}: any) => {
        if (data.status === 201 || data.status === 200) {
          emits("loadMyInfo")
          introduceValue.value = "";
          showSuccess.value = true;
        } else {
          showFail.value = true;
        }
      },
      (error: any) => console.error(error)
  );
  emits("closeEditIntroduce");
};

const showSuccess = ref(false);
const setShowSuccess = (status: boolean) => showSuccess.value = status;
const showFail = ref(false);
const setShowFail = (status: boolean) => showFail.value = status;
onMounted(() => {
  if (props.introduce) {
    introduceValue.value = props.introduce;
  }
})
</script>

<template>
  <SuccessNotification title="저장 성공" message="자기 소개 수정 완료" :show-from-parent="showSuccess"
                       @set-parent-show="setShowSuccess"/>
  <FailNotification title="저장 실패" message="자기 소개 수정 실패" :show-from-parent="showFail" @set-parent-show="setShowFail"/>
  <TransitionRoot as="template" :show="isOpenEditIntroduce">
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
        <div class="fixed inset-0 transition-opacity bg-gray-500 bg-opacity-75"/>
      </TransitionChild>

      <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
        <div
            class="flex items-end justify-center min-h-full p-4 text-center sm:items-center sm:p-0"
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
                class="relative px-4 pt-5 pb-4 overflow-hidden text-left transition-all transform bg-white rounded-lg shadow-xl sm:my-8 sm:w-full sm:max-w-lg sm:p-6"
            >
              <div class="bg-white shadow sm:rounded-lg">
                <div class="px-4 py-5 sm:p-6">
                  <h3 class="pb-3 font-semibold leading-6 text-gray-900 lg:text-lg">
                    자기소개 수정
                  </h3>
                  <div class="w-full">
                    <div>
                      <label for="introduce" class="sr-only">introduce</label>
                      <textarea
                          name="introduce"
                          id="introduce"
                          class="block w-full rounded-md border-0 py-3.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 lg:text-lg sm:leading-6 resize-none"
                          :placeholder="introduce"
                          style="height: calc(3.5rem + 50px)"
                          v-model="introduceValue"
                          @input="updateIntroduceValue"
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
                    class="inline-flex justify-center w-full px-3 py-2 text-sm font-semibold text-white bg-indigo-600 rounded-md shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 sm:col-start-2"
                    @click="updateIntroduce()"
                >
                  저장
                </button>
                <button
                    type="button"
                    class="inline-flex justify-center w-full px-3 py-2 mt-3 text-sm font-semibold text-gray-900 bg-white rounded-md shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 sm:col-start-1 sm:mt-0"
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
