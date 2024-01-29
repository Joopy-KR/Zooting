<script setup lang="ts">
import { ref, defineProps, defineEmits } from "vue";
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
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" />
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
              <!-- <div>
                <div
                  class="mx-auto flex h-12 w-12 items-center justify-center rounded-full bg-green-100"
                >
                  <CheckIcon class="h-6 w-6 text-green-600" aria-hidden="true" />
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
                  <h3 class="font-semibold pb-3 leading-6 text-lg text-gray-900">
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
                  class="inline-flex w-full justify-center rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 sm:col-start-2"
                  @click="updateIntroduce()"
                >
                  저장
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
