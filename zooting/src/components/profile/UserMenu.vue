<template>
  <Popover class="relative">
    <PopoverButton class="inline-flex items-center gap-x-1 text-sm font-semibold leading-6 text-gray-900">
      <font-awesome-icon :icon="['fas', 'chevron-down']" style="font-size: 20px;"/>
    </PopoverButton>

    <transition enter-active-class="transition ease-out duration-200" enter-from-class="opacity-0 translate-y-1"
                enter-to-class="opacity-100 translate-y-0" leave-active-class="transition ease-in duration-150"
                leave-from-class="opacity-100 translate-y-0" leave-to-class="opacity-0 translate-y-1">
      <PopoverPanel class="absolute left-1/2 z-10 mt-5 flex w-screen max-w-max -translate-x-1/2 px-4">
        <div
            class="w-screen max-w-44 flex-auto overflow-hidden rounded-3xl bg-white text-sm leading-6 shadow-lg ring-1 ring-gray-900/5">
          <div class="p-4">
            <div v-for="item in activatedItems" :key="item.name"
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
            <div v-if="isMyProfile" class="mt-5 sm:mt-6">
              <button type="button"
                      class="inline-flex w-full justify-center rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                      @click="open = false">Go back to dashboard
              </button>
            </div>
          </div>
        </div>
      </PopoverPanel>
    </transition>
  </Popover>
</template>

<script setup lang="ts">
import {Popover, PopoverButton, PopoverPanel} from '@headlessui/vue'
import {blockUserApi, disableBlockUserApi} from "@/api/block.js";
import {EyeIcon, EyeSlashIcon, FaceFrownIcon, FaceSmileIcon, MegaphoneIcon} from '@heroicons/vue/20/solid'
import {useAccessTokenStore} from "@/stores/store"
import {computed, onMounted, ref, watch} from "vue";
import type {UserInfo} from "@/types/global";
import {addFriendApi, disableFriendApi} from "@/api/friend";

const store = useAccessTokenStore();
const props = defineProps({
  userInfo: Object as () => UserInfo,
  isMyProfile: Boolean,
})
const emits = defineEmits([
  "setIsOpenReportDialog",
  "loadUserInfo",
]);

const setIsOpenReportDialog = () => {
  emits("setIsOpenReportDialog", true);
}
// 유저 차단
const blockUser = () => {
  if (!props.userInfo?.nickname) {
    return;
  }
  blockUserApi(
      {
        nickname: props.userInfo.nickname
      },
      ({data}: any) => {
      },
      (error: any) => console.log(error)
  )
  items.value[2].status = false;
  items.value[3].status = true;
}
// 유저 차단 해제
const disableBlockUser = () => {
  if (!props.userInfo?.nickname) {
    return;
  }
  disableBlockUserApi(props.userInfo.nickname,
      ({data}: any) => {},
      (error: any) => console.log(error),
  )
  items.value[2].status = true;
  items.value[3].status = false;
}

const addFriend = () => {
  if (!props.userInfo?.nickname) {
    return;
  }

  addFriendApi({nickname: props.userInfo.nickname},
      ({data}: any) => {},
      (error: any) => console.log(error)
  );
  items.value[0].status = false;
  items.value[1].status = true;
}

const disableFriend = () => {
  if (!props.userInfo?.nickname) {
    return;
  }

  disableFriendApi(props.userInfo.nickname,
      ({data}: any) => {},
      (error: any) => console.log(error));
  items.value[0].status = true;
  items.value[1].status = false;
}
const items = ref([
  {name: '친구추가', icon: FaceSmileIcon, onclick: () => addFriend(), status: false},
  {name: '친구해제', icon: FaceFrownIcon, onclick: () => disableFriend(), status: false},
  {name: '차단하기', icon: EyeSlashIcon, onclick: () => blockUser(), status: false},
  {name: '차단해제', icon: EyeIcon, onclick: () => disableBlockUser(), status: false},
  {name: '신고하기', icon: MegaphoneIcon, onclick: () => setIsOpenReportDialog(), status: true},
]);

const activatedItems = computed(() => {
  return items.value.filter(item => item.status === true);
})

watch(props.userInfo,
    (newUserInfo) => {
      if (!newUserInfo?.memberStatus) {
        items.value[0].status = false;
        items.value[1].status = false;
        items.value[2].status = false;
        items.value[3].status = false;
        return;
      }
      // 현재 친구 상태
      if (newUserInfo.memberStatus.isFriend) {
        items.value[0].status = false;
        items.value[1].status = true;
      } else {
        items.value[0].status = true;
        items.value[1].status = false;
      }
      // 현재 차단 상태
      if (newUserInfo.memberStatus.isBlock) {
        items.value[2].status = false;
        items.value[3].status = true;
      } else {
        items.value[2].status = true;
        items.value[3].status = false;
      }
      // 현재 신고된 상태
      if (newUserInfo.memberStatus.isReport) {
        items.value[4].status = true;
      }
    }, {deep: true});
</script>