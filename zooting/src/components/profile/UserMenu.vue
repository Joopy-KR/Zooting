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
          </div>
        </div>
      </PopoverPanel>
    </transition>
  </Popover>
</template>

<script setup lang="ts">
import {Popover, PopoverButton, PopoverPanel} from '@headlessui/vue'
import {blockUserApi, disableBlockUserApi} from "@/api/block.js";
import {useAccessTokenStore} from "@/stores/store"
import {computed, onMounted, ref, watch} from "vue";
import type {UserInfo} from "@/types/global";

const store = useAccessTokenStore();
const props = defineProps({
  userInfo: Object as () => UserInfo,
})
const emits = defineEmits([
  "setIsOpenReportDialog"
]);

const friendList = ref(store.friendList);
const blockList = ref(store.blockList);
const isFriend = ref<boolean>(false);
const isBlock = ref<boolean>(false);

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
}
// 유저 차단 해제
const disableBlockUser = () => {
  if (!props.userInfo?.nickname) {
    return;
  }
  disableBlockUserApi(
      {
        nickname: props.userInfo.nickname
      },
      ({data}: any) => console.log(data),
      (error: any) => console.log(error),
  )
}

const items = [
  {name: '친구추가', icon: undefined, status: undefined},
  {name: '친구해제', icon: undefined, status: undefined},
  {name: '차단하기', icon: undefined, onclick: () => blockUser(), status: undefined},
  {name: '차단해제', icon: undefined, status: undefined},
  {name: '신고하기', icon: undefined, onclick: () => setIsOpenReportDialog(), status: true},
]

const activatedItems = computed(() => {
  console.log("ITEM!!!!!!!", items.filter(item => item.status === true));
  return items.filter(item => item.status === true);
})

watch(() => store.friendList,
    (newValue) => {
      isFriend.value = newValue.some(friend => friend.nickname === props.userInfo?.nickname);
    });
watch(() => store.blockList,
    (newValue) => {
      isBlock.value = newValue.some(block => block.nickname === props.userInfo?.nickname);
    });
watch(isFriend,
    (newValue) => {
      if (newValue) {
        items[0].status = false;
        items[1].status = true;
      } else {
        items[0].status = true;
        items[1].status = false;
      }
    }, {deep: true});
watch(isBlock,
    (newValue) => {
      if (newValue) {
        items[2].status = false;
        items[3].status = true;
      } else {
        items[2].status = true;
        items[3].status = false;
      }
    }, {deep: true});

onMounted(() => {
  watch(() => props.userInfo, (newUserInfo, oldUserInfo) => {
    if (!props.userInfo?.nickname) {
      return;
    }
    console.log(props);
    console.log("friendList", friendList.value);
    console.log("blockList", blockList.value);
    console.log("isFriend", isFriend.value);
    console.log("isBlock", isBlock.value);
    console.log("userinfo", props.userInfo?.nickname)
    if (newUserInfo && newUserInfo.nickname) {
      isFriend.value = friendList.value.some(friend => friend.nickname === newUserInfo.nickname);
      isBlock.value = blockList.value.some(block => block.nickname === newUserInfo.nickname);
    }
  }, { immediate: true });
});
</script>