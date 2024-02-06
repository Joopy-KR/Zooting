<script setup lang="ts">
import {Dialog, DialogPanel, TransitionChild, TransitionRoot} from '@headlessui/vue'
import type {Personality, UserInfo} from "@/types/global";
import {ref, watch} from "vue";
import {useStore} from "@/stores/store";
import {useRouter} from "vue-router";

const store = useStore();
const router = useRouter();

const props = defineProps({
  userInfo: Object as () => UserInfo,
  isMyProfile: Boolean,
  isOpenPersonalityDialog: Boolean,
});
const emits = defineEmits([
  "setIsOpenPersonalityDialog"
]);

const personality = ref<Personality | undefined>();

const setIsOpenPersonalityDialog = (status: boolean) => {
  emits("setIsOpenPersonalityDialog", status);
}

const getColorClass = (value: string | undefined) => {
  if (!value) return undefined;
  if (value.includes('여름')) {
    return 'summer-text'
  } else if (value.includes('겨울')) {
    return 'winter-text'
  } else if (value.includes('봄')) {
    return 'spring-text'
  } else if (value.includes('가을')) {
    return 'autumn-text'
  }
}

const moveToPersonalityTest = () => {
  router.push({name: "personality_test"})
}

watch(props.userInfo, (newValue) => {
  if (newValue?.personality) {
    personality.value = store.personality[newValue.personality.toUpperCase()];
  }
});
</script>

<template>
  <TransitionRoot as="template" :show="isOpenPersonalityDialog">
    <Dialog as="div" class="relative z-30" @close="setIsOpenPersonalityDialog(false)">
      <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0" enter-to="opacity-100"
                       leave="ease-in duration-200" leave-from="opacity-100" leave-to="opacity-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"/>
      </TransitionChild>

      <div class="fixed inset-0 z-30 w-screen overflow-y-auto">
        <div class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
          <TransitionChild as="template" enter="ease-out duration-300"
                           enter-from="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
                           enter-to="opacity-100 translate-y-0 sm:scale-100" leave="ease-in duration-200"
                           leave-from="opacity-100 translate-y-0 sm:scale-100"
                           leave-to="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95">
            <DialogPanel
                class="relative transform overflow-hidden rounded-lg bg-white px-4 pb-4 pt-5 text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-screen-md sm:p-6">
              <div class="flex justify-end">
                <font-awesome-icon :icon="['fas', 'times']" size="2x" color="red"
                                   @click="setIsOpenPersonalityDialog(false)"/>
              </div>
              <div class="result__container">
                <div>
                  <div class="result__title">
                    <h1>{{ userInfo?.nickname }} 님의 성격 유형은</h1>
                    <h1>
                      <span :class="getColorClass(personality?.title)">
                      {{ personality?.title }}
                        </span>이에요
                    </h1>
                  </div>

                  <div class="result__content">
                    <li v-for="(content, index) in personality?.content ? personality.content : []" :key="index">
                      {{ content }}
                    </li>
                    <li><span :class="getColorClass(personality?.match)">
            {{ personality?.title }}
          </span> 유형과 궁합이 맞아요.
                    </li>
                  </div>
                </div>
                <div v-if="isMyProfile" class="mt-5 sm:mt-6">
                  <button type="button"
                          class="inline-flex w-full justify-center rounded-md bg-indigo-600 px-12 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                          @click="moveToPersonalityTest">
                    다시 검사히기
                  </button>
                </div>
              </div>
            </DialogPanel>
          </TransitionChild>
        </div>
      </div>
    </Dialog>
  </TransitionRoot>
</template>

<style scoped>
.result__container {
  @apply h-full w-full flex flex-col justify-center bg-white border border-gray-200 shadow py-8 items-center;
}

.result__title {
  @apply text-2xl font-black my-5;
}

.summer-text {
  color: #4CA975;
}

.winter-text {
  color: #6A5ACD;
}

.spring-text {
  color: #FF7493;
}

.autumn-text {
  color: #D27D32;
}

.result__content {
  @apply bg-white border border-gray-200 rounded-lg shadow py-5 px-5 text-left;
}

.result__content span {
  @apply font-black;
}

.test-completed {
  @apply text-gray-900 bg-gradient-to-r from-red-200 via-red-300 to-yellow-200 hover:bg-gradient-to-bl focus:outline-none focus:ring-red-100 font-medium rounded-lg text-lg px-5 py-2.5 text-center mx-3 my-7 w-80;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 1s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>