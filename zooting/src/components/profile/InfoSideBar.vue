<script setup lang="ts">
import {ref, watch} from "vue";
import {useRouter} from "vue-router";
import type {UserInfo} from "@/types/global";
import ReportMessage from "@/components/profile/ReportMessage.vue";
import UserMenu from "@/components/profile/UserMenu.vue";
import {useStore} from "@/stores/store";
import InfoPersonality from "@/components/profile/InfoPersonality.vue";

const router = useRouter();

const props = defineProps({
  userInfo: Object as () => UserInfo,
  isMyProfile: Boolean,
  nickname: String,
});

const emits = defineEmits(["loadUserInfo"]);

const interests = ref<string[]>([]);
const ageGroup = ref<string>();
const isOpenReportDialog = ref<boolean>(false);
const isOpenPersonalityDialog = ref<boolean>(false);

const loadUserInfo = () => {
  emits("loadUserInfo", props.nickname);
}
const moveToSetting = () => {
  if (props.isMyProfile) {
    router.push({
      name: "profile-info",
      params: {nickname: props.userInfo!.nickname},
    });
  }
};

const moveToMaskList = () => {
  if (props.isMyProfile) {
    router.push({
      name: "profile-mask-list",
      params: {nickname: props.userInfo!.nickname},
    });
  }
};
const moveToPersonal = () => {
  if (!props.userInfo) return;
  router.push({
    name: "profile-personal-info",
    params: {nickname: props.userInfo.nickname},
  });
};

const setIsOpenReportDialog = (status: boolean) => {
  isOpenReportDialog.value = status;
}

const setIsOpenPersonalityDialog = (status: boolean) => {
  isOpenPersonalityDialog.value = status;
}
const getPersonalityMsg = (animal: string | undefined, personality: string | undefined) => {
  if (!animal && !personality) return undefined;
  if (!animal) return personality;
  if (!personality) return animal;

  const store = useStore();
  const mbti = store.personality[personality.toUpperCase()];

  return mbti.title + " " + animal;
}
const getHeartClass = (gender: string | undefined) => {
  return gender === 'man' ? 'w-5 h-5 text-blue-500 ms-1' : 'w-5 h-5 text-pink-500 ms-1';
}
const getAgeGroup = (birth: string) => {
  const birthDate: Date = new Date(birth);
  const currentDate: Date = new Date();

  const ageInMillis: number = currentDate.getTime() - birthDate.getTime();
  const ageInYears: number = Math.floor(ageInMillis / (365 * 24 * 60 * 60 * 1000));

  // 연령대 계산
  const ageGroup: number = Math.floor(ageInYears / 10) * 10;

  const remainder: number = ageInYears % 10;
  if (remainder < 3) {
    return `${ageGroup}대 초반`;
  } else if (remainder < 7) {
    return `${ageGroup}대 중반`;
  } else {
    return `${ageGroup}대 후반`;
  }
};
watch(() => props.userInfo?.birth,
    (newValue) => {
      if (newValue) {
        ageGroup.value = getAgeGroup(newValue);
      }
    }
);
watch(() => props.userInfo?.interest,
    (newValue) => {
      if (newValue) {
        const interestsArray = newValue.replace("[", "").replace("]", "").split(", ");
        // Populate the interests array with trimmed values
        interests.value = interestsArray.map((interest: string) => interest.trim());
      } else {
        interests.value = [];
      }
    }
);
</script>

<template>
  <ReportMessage
      :is-open-report-dialog="isOpenReportDialog"
      :nickname="userInfo?.nickname"
      @set-is-open-report-dialog="setIsOpenReportDialog"
  />
  <InfoPersonality
      :user-info="userInfo"
      :is-my-profile="isMyProfile"
      :is-open-personality-dialog="isOpenPersonalityDialog"
      @set-is-open-personality-dialog="setIsOpenPersonalityDialog"
  />
  <div class="flex flex-col h-screen">
    <div class="w-full h-1/3">
      <div class="flex justify-end px-8 pt-4">
        <!-- 설정 버튼 -->
        <div v-if="isMyProfile" @click="moveToSetting">
          <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="2.0"
              stroke="currentColor"
              class="w-8 h-8 hover:fill-slate-400"
          >
            <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M9.594 3.94c.09-.542.56-.94 1.11-.94h2.593c.55 0 1.02.398 1.11.94l.213 1.281c.063.374.313.686.645.87.074.04.147.083.22.127.325.196.72.257 1.075.124l1.217-.456a1.125 1.125 0 0 1 1.37.49l1.296 2.247a1.125 1.125 0 0 1-.26 1.431l-1.003.827c-.293.241-.438.613-.43.992a7.723 7.723 0 0 1 0 .255c-.008.378.137.75.43.991l1.004.827c.424.35.534.955.26 1.43l-1.298 2.247a1.125 1.125 0 0 1-1.369.491l-1.217-.456c-.355-.133-.75-.072-1.076.124a6.47 6.47 0 0 1-.22.128c-.331.183-.581.495-.644.869l-.213 1.281c-.09.543-.56.94-1.11.94h-2.594c-.55 0-1.019-.398-1.11-.94l-.213-1.281c-.062-.374-.312-.686-.644-.87a6.52 6.52 0 0 1-.22-.127c-.325-.196-.72-.257-1.076-.124l-1.217.456a1.125 1.125 0 0 1-1.369-.49l-1.297-2.247a1.125 1.125 0 0 1 .26-1.431l1.004-.827c.292-.24.437-.613.43-.991a6.932 6.932 0 0 1 0-.255c.007-.38-.138-.751-.43-.992l-1.004-.827a1.125 1.125 0 0 1-.26-1.43l1.297-2.247a1.125 1.125 0 0 1 1.37-.491l1.216.456c.356.133.751.072 1.076-.124.072-.044.146-.086.22-.128.332-.183.582-.495.644-.869l.214-1.28Z"
            />
            <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"
            />
          </svg>
        </div>
      </div>
      <!-- 나의 동물상 마스크 -->
      <div class="flex justify-center p-4">
        <div class="relative w-2/5 p-2 bg-gray-100 rounded-full min-w-48">
          <img :src="userInfo!.maskImgUrl" class="w-full p-2" :alt="userInfo!.maskImgUrl"/>
          <div v-if="isMyProfile" class="absolute bottom-1 right-1" @click="moveToMaskList">
            <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                fill="currentColor"
                class="w-6 h-6 fill-rose-500 hover:fill-amber-400"
            >
              <path
                  d="M21.731 2.269a2.625 2.625 0 0 0-3.712 0l-1.157 1.157 3.712 3.712 1.157-1.157a2.625 2.625 0 0 0 0-3.712ZM19.513 8.199l-3.712-3.712-12.15 12.15a5.25 5.25 0 0 0-1.32 2.214l-.8 2.685a.75.75 0 0 0 .933.933l2.685-.8a5.25 5.25 0 0 0 2.214-1.32L19.513 8.2Z"
              />
            </svg>
          </div>
        </div>
      </div>
    </div>
    <div class="mx-8 my-4">
      <div class="flex items-center justify-center p-2">
        <div
            class="relative font-bold underline lg:text-2xl text-stone-800 decoration-pink-300 decoration-wavy"
        >
          {{ userInfo?.nickname }}
          <div class="absolute bottom-1 -right-6">
            <svg :class="getHeartClass(userInfo?.gender)" aria-hidden="true" xmlns="http://www.w3.org/2000/svg"
                 fill="currentColor" viewBox="0 0 24 24">
              <path
                  d="m12.7 20.7 6.2-7.1c2.7-3 2.6-6.5.8-8.7A5 5 0 0 0 16 3c-1.3 0-2.7.4-4 1.4A6.3 6.3 0 0 0 8 3a5 5 0 0 0-3.7 1.9c-1.8 2.2-2 5.8.8 8.7l6.2 7a1 1 0 0 0 1.4 0"/>
            </svg>
          </div>
          <div class="absolute text-xs bottom-0.5 -right-14 rounded-full" v-if="!isMyProfile">
            <UserMenu
                v-if="userInfo"
                :user-info="userInfo"
                @set-is-open-report-dialog="setIsOpenReportDialog"
                @load-user-info="loadUserInfo"
            />
          </div>
        </div>
      </div>
      <div class="flex flex-row px-2 py-3">
        <div class="flex items-center justify-center w-1/3 font-semibold lg:text-xl">
          {{ userInfo!.gender === "man" ? "남자" : "여자" }}
        </div>
        <div class="flex items-center justify-center w-1/3 font-semibold lg:text-xl">
          {{ ageGroup }}
        </div>
        <div class="flex items-center justify-center w-1/3 font-semibold lg:text-xl">
          {{ userInfo?.address }}
        </div>
      </div>
      <div class="flex justify-center px-4 py-2 font-bold tracking-tight lg:text-3xl text-rose-600"
           @click="setIsOpenPersonalityDialog(true)">
        {{ getPersonalityMsg(userInfo?.animal, userInfo?.personality) }}
      </div>
    </div>
    <div class="interest__container">
      <div class="flex items-end justify-center h-max-full">
        <div class="relative w-2/3 m-4 shadow-inner rounded-3xl shadow-pink-200">
          <div class="grid grid-cols-1 gap-4 px-8 py-6 sm:grid-cols-2">
            <div
                v-for="interest in interests"
                :key="interest"
                class="flex items-center justify-center px-4 py-2 space-x-6 bg-white rounded-lg shadow-lg border-rose-300 shadow-rose-200"
            >
              <p class="font-semibold text-center text-gray-900 truncate lg:text-lg hover:text-wrap">
                # {{ interest }}
              </p>
            </div>
          </div>
          <div v-if="isMyProfile" class="absolute bottom-1 right-1" @click="moveToPersonal">
            <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                fill="currentColor"
                class="w-6 h-6 fill-rose-500 hover:fill-amber-400"
            >
              <path
                  d="M21.731 2.269a2.625 2.625 0 0 0-3.712 0l-1.157 1.157 3.712 3.712 1.157-1.157a2.625 2.625 0 0 0 0-3.712ZM19.513 8.199l-3.712-3.712-12.15 12.15a5.25 5.25 0 0 0-1.32 2.214l-.8 2.685a.75.75 0 0 0 .933.933l2.685-.8a5.25 5.25 0 0 0 2.214-1.32L19.513 8.2Z"
              />
            </svg>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.interest__container {
  @apply w-full overflow-y-auto;
}

.interest__container::-webkit-scrollbar {
  width: 6px;
  background-color: white;
}

.interest__container::-webkit-scrollbar-thumb {
  background-color: #d6d6d6;
  border-radius: 4px;
}

.interest__container::-webkit-scrollbar-track {
  background-color: transparent;
}
</style>
