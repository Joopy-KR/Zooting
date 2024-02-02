<script setup lang="ts">
import { ref } from "vue";
import {
  Dialog,
  DialogPanel,
  DialogTitle,
  TransitionChild,
  TransitionRoot,
} from "@headlessui/vue";
import { CheckIcon } from "@heroicons/vue/24/outline";
import { updateIntroduceApi } from "@/api/profile";

const props = defineProps({
  isOpenEditIntroduce: Boolean,
  introduce: String,
});

const introduceValue = ref<string | null>();
const emits = defineEmits(["closeEditIntroduce"]);

const closeDialog = () => {
  emits("closeEditIntroduce");
};
const updateIntroduceValue = (event: any) => {
  introduceValue.value = event.target.value;
};

const updateIntroduce = () => {
  console.log(introduceValue);
  if (!introduceValue.value) {
    return;
  }

  updateIntroduceApi(
    {
      introduce: introduceValue.value,
    },
    ({ data }: any) => {
      introduceValue.value = null;
    },
    (error: any) => console.error(error)
  );
  emits("closeEditIntroduce");
};
</script>

<template>
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
        <div class="fixed inset-0 transition-opacity bg-gray-500 bg-opacity-75" />
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
              <!-- <div>
                <div
                  class="flex items-center justify-center w-12 h-12 mx-auto bg-green-100 rounded-full"
                >
                  <CheckIcon class="w-6 h-6 text-green-600" aria-hidden="true" />
                </div>
                <div class="mt-3 text-center sm:mt-5">
                  <DialogTitle
                    as="h3"
                    class="text-base font-semibold leading-6 text-gray-900"
                    >Payment successful</DialogTitle
                  >
                  <div class="mt-2">
                    <p class="text-sm text-gray-500">
                      Lorem ipsum, dolor sit amet consectetur adipisicing elit. Eius
                      aliquam laudantium explicabo pariatur iste dolorem animi vitae error
                      totam. At sapiente aliquam accusamus facere veritatis.
                    </p>
                  </div>
                </div>
              </div> -->
              <div class="bg-white shadow sm:rounded-lg">
                <div class="px-4 py-5 sm:p-6">
                  <h3 class="pb-3 text-lg font-semibold leading-6 text-gray-900">
                    자기소개 수정
                  </h3>
                  <div class="w-full">
                    <div>
                      <label for="introduce" class="sr-only">introduce</label>
                      <textarea
                        name="introduce"
                        id="introduce"
                        class="block w-full rounded-md border-0 py-3.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-xl sm:leading-6 resize-none"
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
