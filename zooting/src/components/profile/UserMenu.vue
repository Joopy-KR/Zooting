<template>
  <Popover class="relative">
    <PopoverButton class="inline-flex items-center gap-x-1 text-sm font-semibold leading-6 text-gray-900">
      <ChevronDownIcon class="h-5 w-5" aria-hidden="true"/>
    </PopoverButton>

    <transition enter-active-class="transition ease-out duration-200" enter-from-class="opacity-0 translate-y-1"
                enter-to-class="opacity-100 translate-y-0" leave-active-class="transition ease-in duration-150"
                leave-from-class="opacity-100 translate-y-0" leave-to-class="opacity-0 translate-y-1">
      <PopoverPanel class="absolute left-1/2 z-10 mt-5 flex w-screen max-w-max -translate-x-1/2 px-4">
        <div
            class="w-screen max-w-44 flex-auto overflow-hidden rounded-3xl bg-white text-sm leading-6 shadow-lg ring-1 ring-gray-900/5">
          <div class="p-4">
            <div v-for="item in items" :key="item.name"
                 class="group relative flex gap-x-2 rounded-lg p-2 hover:bg-gray-50"
                 @click="item.onclick">
              <div
                  class="mt-0.5 flex h-5 w-5 flex-none items-center justify-center rounded-lg bg-gray-50 group-hover:bg-white">
                <component :is="item.icon" class="h-5 w-5 text-gray-600 group-hover:text-indigo-600"
                           aria-hidden="true"/>
              </div>
              <div>
                <div class="font-semibold text-gray-900">
                  {{ item.name }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </PopoverPanel>
    </transition>
  </Popover>
</template>

<script setup lang="ts">
import {Popover, PopoverButton, PopoverPanel} from '@headlessui/vue'
import {ChevronDownIcon, PhoneIcon} from '@heroicons/vue/20/solid'
import {blockUserApi, disableBlockUserApi} from "@/api/block.js";
import {ChartPieIcon, CursorArrowRaysIcon, FingerPrintIcon, SquaresPlusIcon,} from '@heroicons/vue/24/outline'
import {ref} from "vue";

const props = defineProps({
  nickname: string,
})
const emits = defineEmits([
  "setIsOpenReportDialog"
]);

const isFriend = ref<boolean>(false);
const isBlock = ref<boolean>(false);

const setIsOpenReportDialog = () => {
  emits("setIsOpenReportDialog", true);
}

// 유저 차단
const blockUser = () => {
  if (!props.nickname) {
    return;
  }
  blockUserApi(
      {
        nickname: props.nickname
      },
      ({data}: any) => {
      },
      (error: any) => console.log(error)
  )
}
// 유저 차단 해제
const disableBlockUser = () => {
  if (!props.nickname) {
    return;
  }
  disableBlockUserApi(
      {
        nickname: props.nickname
      },
      ({data}: any) => console.log(data),
      (error: any) => console.log(error),
  )
}

const items = [
  {name: '친구추가', icon: ChartPieIcon, status: "not-friend"},
  {name: '친구해제', icon: PhoneIcon, status: "friend"},
  {name: '차단하기', icon: CursorArrowRaysIcon, onclick: () => blockUser(), status: "non-block"},
  {name: '차단해제', icon: FingerPrintIcon, status: "block"},
  {name: '신고하기', icon: SquaresPlusIcon, onclick: () => setIsOpenReportDialog(), status: "common"},
]
</script>