<template>
  <SuccessNotification
      :title="notification.title"
      :message="notification.message"
      :show-from-parent="showSuccess"
      @set-parent-show="setShowSuccess"
  />
  <FailNotification
      :title="notification.title"
      :message="notification.message"
      :show-from-parent="showFail"
      @set-parent-show="setShowFail"
  />
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
          </div>
        </div>
      </PopoverPanel>
    </transition>
  </Popover>
</template>

<script setup lang="ts">
import {Popover, PopoverButton, PopoverPanel} from '@headlessui/vue'
import {blockUserApi, disableBlockUserApi} from "@/api/block.js";
import {EyeIcon, EyeSlashIcon, MegaphoneIcon, UserPlusIcon, UsersIcon} from '@heroicons/vue/20/solid'
import {useAccessTokenStore} from "@/stores/store"
import {computed, ref, watch} from "vue";
import type {Friend, Notify, UserInfo} from "@/types/global";
import {addFriendApi, disableFriendApi, loadFriendRequestListApi} from "@/api/friend";
import {UserMinusIcon} from "@heroicons/vue/16/solid";
import SuccessNotification from "@/components/util/SuccessNotification.vue";
import FailNotification from "@/components/util/FailNotification.vue";

const store = useAccessTokenStore();
const props = defineProps({
  userInfo: Object as () => UserInfo,
  isMyProfile: Boolean,
})
const emits = defineEmits([
  "setIsOpenReportDialog",
  "loadUserInfo",
]);

const friendList = ref<Friend[]>();
const friendRequestList = ref<Friend[]>();
const showSuccess = ref<boolean>(false);
const showFail = ref<boolean>(false);
const setShowSuccess = (status: boolean) => {
  showSuccess.value = status;
}
const setShowFail = (status: boolean) => {
  showFail.value = status;
}
const notification = ref<Notify>({
  title: '',
  message: ''
});
const setNotification = (title: string, message: any) => {
  notification.value.title = title;
  notification.value.message = message;
}
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
        if (data.status === 200 || data.status === 201) {
          setNotification("차단 성공", data.result);
          setShowSuccess(true);
        } else {
          setNotification("차단 실패", data.result);
          setShowFail(true);
        }
      },
      (error: any) => console.log(error)
  )
  items.value[3].status = false;
  items.value[4].status = true;
}
// 유저 차단 해제
const disableBlockUser = () => {
  if (!props.userInfo?.nickname) {
    return;
  }
  disableBlockUserApi(props.userInfo.nickname,
      ({data}: any) => {
        if (data.status === 200 || data.status === 201) {
          setNotification("차단해제 성공", data.result);
          setShowSuccess(true);
        } else {
          setNotification("차단해제 실패", data.result);
          setShowFail(true);
        }
      },
      (error: any) => console.log(error),
  )
  items.value[3].status = true;
  items.value[4].status = false;
}

const addFriend = () => {
  if (!props.userInfo?.nickname) {
    return;
  }

  addFriendApi({nickname: props.userInfo.nickname},
      ({data}: any) => {
        if (data.status === 200 || data.status === 201) {
          setNotification("친구 요청 성공", data.result);
          setShowSuccess(true);
        } else {
          setNotification("친구 요청 실패", data.result);
          setShowFail(true);
        }
      },
      (error: any) => console.log(error)
  );
  items.value[0].status = false;
  items.value[1].status = true;
  items.value[2].status = false;
}

const disableFriend = () => {
  if (!props.userInfo?.nickname) {
    return;
  }

  disableFriendApi(props.userInfo.nickname,
      ({data}: any) => {
        if (data.status === 200 || data.status === 201) {
          setNotification("친구 삭제 성공", data.result);
          setShowSuccess(true);
        } else {
          setNotification("친구 삭제 실패", data.result);
          setShowFail(true);
        }
      },
      (error: any) => console.log(error));
  items.value[0].status = true;
  items.value[1].status = false;
  items.value[2].status = false;
}
const items = ref([
  {name: '친구추가', icon: UserPlusIcon, onclick: () => addFriend(), status: false},
  {name: '친구요청중', icon: UsersIcon, status: false},
  {name: '친구해제', icon: UserMinusIcon, onclick: () => disableFriend(), status: false},
  {name: '차단하기', icon: EyeSlashIcon, onclick: () => blockUser(), status: false},
  {name: '차단해제', icon: EyeIcon, onclick: () => disableBlockUser(), status: false},
  {name: '신고하기', icon: MegaphoneIcon, onclick: () => setIsOpenReportDialog(), status: true},
]);

const activatedItems = computed(() => {
  return items.value.filter(item => item.status === true);
})

const loadFriendRequestList = async () => {
  if (!props.userInfo?.nickname) {
    return;
  }

  await loadFriendRequestListApi(
      ({data}: any) => {
        const resultArray = data.result || [];
        const result: Friend[] = [];

        resultArray.forEach((item: any) => {
          result.push({
            email: item.email,
            nickname: item.nickname,
            animal: item.animal,
            gender: item.gender,
          });
        })

        friendList.value = result;
        console.log("FriendRequestList", friendRequestList.value);
        // friend update
        updateFriendStatus();
      },
      (error: any) => console.log(error)
  )
}
const updateFriendStatus = () => {
  const find = friendList.value?.find(friend => friend.nickname === props.userInfo?.nickname);

  // 친구 요청 중인 경우
  if (!!find) {
    items.value[0].status = false;
    items.value[2].status = false;
    items.value[1].status = true;
  } else {
    items.value[1].status = false;
  }
}

watch((props.userInfo as UserInfo),
    async (newUserInfo) => {
      if (!newUserInfo?.memberStatus) {
        items.value[0].status = false;
        items.value[1].status = false;
        items.value[2].status = false;
        items.value[3].status = false;
        items.value[4].status = true;
        return;
      }
      // 현재 친구 상태
      if (newUserInfo.memberStatus.isFriend) {
        items.value[0].status = false;
        items.value[2].status = true;
      } else {
        items.value[0].status = true;
        items.value[2].status = false;
      }
      // 현재 차단 상태
      if (newUserInfo.memberStatus.isBlock) {
        items.value[3].status = false;
        items.value[4].status = true;
      } else {
        items.value[3].status = true;
        items.value[4].status = false;
      }
      // 현재 신고된 상태
      if (newUserInfo.memberStatus.isReport) {
        items.value[5].status = true;
      }

      await loadFriendRequestList();
    }, {deep: true});
</script>