<script setup lang="ts">
import {onMounted, ref, watch} from "vue";
import {useRouter} from "vue-router";
import EditIntroduce from "@/components/profile/EditIntroduce.vue";
import type {UserInfo} from "@/types/global";
import EditBackground from "@/components/profile/EditBackground.vue";

const router = useRouter();
const emits = defineEmits([
  "loadMyInfo"
]);

const props = defineProps({
  userInfo: Object as () => UserInfo,
  isMyProfile: Boolean,
});

const idealAnimals = ref<string[]>([]);
const isOpenEditIntroduce = ref<boolean>(false);
const showEditBackground = ref<boolean>(false);

const loadMyInfo = () => {
  emits("loadMyInfo");
}

const parsingIdealAnimals = (idealAnimalStr: string) => {
  if (idealAnimalStr) {
    const idealAnimalArray = idealAnimalStr.replace("[", "").replace("]", "").split(", ");
    idealAnimals.value = idealAnimalArray.map((interest: string) => interest.trim());
  } else {
    idealAnimals.value = [];
  }
};

const moveToPersonal = () => {
  if (!props.userInfo) return;
  router.push({
    name: "profile-personal-info",
    params: {nickname: props.userInfo.nickname},
  });
};

const setShowEditBackground = (status: boolean) => {
  showEditBackground.value = status;
}

const openEditIntroduce = () => {
  isOpenEditIntroduce.value = true;
};
const closeEditIntroduce = () => {
  isOpenEditIntroduce.value = false;
};

watch(
    () => props.userInfo!.idealAnimal,
    (newValue) => {
      parsingIdealAnimals(newValue);
    }
);

onMounted(() => {
  parsingIdealAnimals(props.userInfo!.idealAnimal);
});
</script>

<template>
  <div>
    <!--  자기소개 수정 다이얼로그-->
    <EditIntroduce
        :is-open-edit-Introduce="isOpenEditIntroduce"
        :introduce="userInfo?.introduce"
        @close-edit-introduce="closeEditIntroduce"
        @load-my-info="loadMyInfo"
    />
    <!--  배경이미지 수정-->
    <EditBackground
        :user-info="userInfo"
        :show-from-parent="showEditBackground"
        @set-parent-show="setShowEditBackground"
        @load-my-info="loadMyInfo"
    />
    <div class="flex flex-col h-screen">
      <!--    배경 이미지-->
      <div class="relative w-full h-1/2">
        <img :src="userInfo?.backgroundImgUrl" alt="profile-img" class="w-full h-full"/>
        <div v-if="isMyProfile" class="" @click="setShowEditBackground(true)">
          <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              fill="currentColor"
              class="absolute w-6 h-6 cursor-pointer right-3 fill-rose-500 hover:fill-amber-400 bottom-3"
          >
            <path
                d="M21.731 2.269a2.625 2.625 0 0 0-3.712 0l-1.157 1.157 3.712 3.712 1.157-1.157a2.625 2.625 0 0 0 0-3.712ZM19.513 8.199l-3.712-3.712-12.15 12.15a5.25 5.25 0 0 0-1.32 2.214l-.8 2.685a.75.75 0 0 0 .933.933l2.685-.8a5.25 5.25 0 0 0 2.214-1.32L19.513 8.2Z"
            />
          </svg>
        </div>
      </div>
      <div class="grid justify-center grid-cols-2 gap-5 px-8 py-10 h-1/2">
        <div class="flex w-full h-5/6">
          <div class="introduce__container">
            <div class="relative w-full">
              <p class="my-5 font-bold text-center lg:text-2xl">저는 이런 사람이에요</p>
              <article
                  class="px-6 py-12 m-4 font-semibold tracking-tight text-center shadow-md grow lg:text-2xl rounded-3xl shadow-pink-200"
              >
                {{ userInfo?.introduce ? userInfo?.introduce : "안녕하세요!" }}
              </article>
              <div v-if="isMyProfile" class="" @click="openEditIntroduce">
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    fill="currentColor"
                    class="absolute w-6 h-*:6 cursor-pointer fill-rose-500 hover:fill-amber-400 right-5 bottom-3"
                >
                  <path
                      d="M21.731 2.269a2.625 2.625 0 0 0-3.712 0l-1.157 1.157 3.712 3.712 1.157-1.157a2.625 2.625 0 0 0 0-3.712ZM19.513 8.199l-3.712-3.712-12.15 12.15a5.25 5.25 0 0 0-1.32 2.214l-.8 2.685a.75.75 0 0 0 .933.933l2.685-.8a5.25 5.25 0 0 0 2.214-1.32L19.513 8.2Z"
                  />
                </svg>
              </div>
            </div>
          </div>
        </div>
        <div class="items-center justify-center">
          <p class="my-5 font-bold text-center lg:text-2xl">이런 사람이 좋아요</p>
          <div class="relative w-full shadow-md rounded-3xl shadow-pink-200">
            <div class="flex flex-wrap justify-center lg:grid-cols-3">
              <div
                  v-for="idealAnimal in idealAnimals"
                  :key="idealAnimal"
                  class="py-3 mx-2 my-3 rounded-lg shadow-lg px-7 shadow-rose-200"
              >
                <p
                    class="font-semibold text-center text-gray-900 truncate hover:text-wrap lg:text-lg"
                >
                  # {{ idealAnimal }}
                </p>
                <div></div>
              </div>
            </div>
            <div v-if="isMyProfile" class="absolute bottom-2 right-2" @click="moveToPersonal">
              <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill="currentColor"
                  class="w-6 h-6 cursor-pointer fill-rose-500 hover:fill-amber-400"
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
  </div>
</template>

<style scoped>
.introduce__container {
  @apply w-full;
}

.introduce__container::-webkit-scrollbar {
  width: 6px;
  background-color: white;
}

.introduce__container::-webkit-scrollbar-thumb {
  background-color: #d6d6d6;
  border-radius: 4px;
}

.introduce__container::-webkit-scrollbar-track {
  background-color: transparent;
}
</style>
