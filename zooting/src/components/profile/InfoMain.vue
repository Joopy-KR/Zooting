<script setup lang="ts">
import {defineProps, onMounted, ref, watch} from "vue";
import {useRouter} from "vue-router";
import EditIntroduce from "@/components/profile/EditIntroduce.vue";
import {useAccessTokenStore} from "@/stores/store";

const store = useAccessTokenStore();
const router = useRouter();

const myInfo = ref(store.userInfo);

const props = defineProps({
  userInfo: Object,
  isMyProfile: Boolean,
});

const idealAnimals = ref<string[]>([]);
const isOpenEditIntroduce = ref<boolean>(false);

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
    params: { nickname: props.userInfo.nickname },
  });
};

const openEditIntroduce = () => {
  isOpenEditIntroduce.value = true;
};
const closeEditIntroduce = () => {
  isOpenEditIntroduce.value = false;
};

const getStringInfo = (value: string | null | undefined) => {
  if (value == null) return undefined;
  return value;
}

watch(
  () => props.userInfo!.idealAnimal,
  (newValue, oldValue) => {
    parsingIdealAnimals(newValue);
  }
);

watch(() => store.userInfo, (updateUser) => {
  myInfo.value = updateUser;
})

onMounted(() => {
  parsingIdealAnimals(props.userInfo!.idealAnimal);
});
</script>

<template>
  <EditIntroduce
    :is-open-edit-Introduce="isOpenEditIntroduce"
    @close-edit-introduce="closeEditIntroduce"
  />
  <div class="flex flex-col h-screen">
    <div class="w-full h-1/2">
      <img :src="getStringInfo(myInfo?.backgroundImgUrl)" alt="profile-img" class="w-full h-full" />
    </div>
    <div class="flex flex-row w-full h-1/2">
      <div class="flex w-1/2 justify-center items-center">
        <div class="introduce__container">
          <div class="relative">
            <article
              class="text-3xl font-semibold tracking-tight text-center shadow-md rounded-3xl shadow-pink-200 px-6 py-12 m-4"
            >
              {{ getStringInfo(myInfo?.introduce) ? getStringInfo(myInfo?.introduce) : "안녕하세요!" }}
            </article>
            <div v-if="isMyProfile" class="" @click="openEditIntroduce">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                fill="currentColor"
                class="w-8 h-8 fill-rose-500 hover:fill-amber-400 absolute right-5 bottom-3"
              >
                <path
                  d="M21.731 2.269a2.625 2.625 0 0 0-3.712 0l-1.157 1.157 3.712 3.712 1.157-1.157a2.625 2.625 0 0 0 0-3.712ZM19.513 8.199l-3.712-3.712-12.15 12.15a5.25 5.25 0 0 0-1.32 2.214l-.8 2.685a.75.75 0 0 0 .933.933l2.685-.8a5.25 5.25 0 0 0 2.214-1.32L19.513 8.2Z"
                />
              </svg>
            </div>
          </div>
        </div>
      </div>
      <div class="flex w-1/2 justify-center items-center">
        <div class="relative mx-16 my-4 rounded-3xl shadow-md shadow-pink-200">
          <p class="m-3 truncate lg:text-3xl font-bold tracking-tight text-center">이런 사람이 좋아요</p>
          <div class="flex flex-wrap mx-6 mt-4 mb-8 justify-center">
            <div
              v-for="idealAnimal in idealAnimals"
              :key="idealAnimal"
              class="px-4 py-2 mx-3 my-3 rounded-lg shadow-lg shadow-rose-200"
            >
              <p
                class="truncate hover:text-wrap lg:text-xl font-semibold text-center text-gray-900 font-medium"
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
              class="w-8 h-8 fill-rose-500 hover:fill-amber-400"
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

<!--  <div class="flex flex-col h-screen">-->
<!--    <div class="w-full h-1/2">-->
<!--      <img :src="userInfo!.backgroundImgUrl" alt="profile-img" class="w-full h-full" />-->
<!--    </div>-->
<!--    <div class="flex flex-row w-full h-1/2">-->
<!--      <div class="flex w-1/2 justify-center items-center">-->
<!--        <div class="introduce__container">-->
<!--          <div class="relative">-->
<!--            <article-->
<!--                class="text-3xl font-semibold tracking-tight text-center shadow-md rounded-3xl shadow-pink-200 px-6 py-12 m-4"-->
<!--            >-->
<!--              {{ userInfo!.introduce ? userInfo!.introduce : "안녕하세요!" }}-->
<!--            </article>-->
<!--            <div v-if="isMyProfile" class="" @click="openEditIntroduce">-->
<!--              <svg-->
<!--                  xmlns="http://www.w3.org/2000/svg"-->
<!--                  viewBox="0 0 24 24"-->
<!--                  fill="currentColor"-->
<!--                  class="w-8 h-8 fill-rose-500 hover:fill-amber-400 absolute right-5 bottom-3"-->
<!--              >-->
<!--                <path-->
<!--                    d="M21.731 2.269a2.625 2.625 0 0 0-3.712 0l-1.157 1.157 3.712 3.712 1.157-1.157a2.625 2.625 0 0 0 0-3.712ZM19.513 8.199l-3.712-3.712-12.15 12.15a5.25 5.25 0 0 0-1.32 2.214l-.8 2.685a.75.75 0 0 0 .933.933l2.685-.8a5.25 5.25 0 0 0 2.214-1.32L19.513 8.2Z"-->
<!--                />-->
<!--              </svg>-->
<!--            </div>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--      <div class="flex w-1/2 justify-center items-center">-->
<!--        <div class="relative mx-16 my-4 rounded-3xl shadow-md shadow-pink-200">-->
<!--          <p class="m-3 truncate lg:text-3xl font-bold tracking-tight text-center">이런 사람이 좋아요</p>-->
<!--          <div class="flex flex-wrap mx-6 mt-4 mb-8 justify-center">-->
<!--            <div-->
<!--                v-for="idealAnimal in idealAnimals"-->
<!--                :key="idealAnimal"-->
<!--                class="px-4 py-2 mx-3 my-3 rounded-lg shadow-lg shadow-rose-200"-->
<!--            >-->
<!--              <p-->
<!--                  class="truncate hover:text-wrap lg:text-xl font-semibold text-center text-gray-900 font-medium"-->
<!--              >-->
<!--                # {{ idealAnimal }}-->
<!--              </p>-->
<!--              <div></div>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div v-if="isMyProfile" class="absolute bottom-2 right-2" @click="moveToPersonal">-->
<!--            <svg-->
<!--                xmlns="http://www.w3.org/2000/svg"-->
<!--                viewBox="0 0 24 24"-->
<!--                fill="currentColor"-->
<!--                class="w-8 h-8 fill-rose-500 hover:fill-amber-400"-->
<!--            >-->
<!--              <path-->
<!--                  d="M21.731 2.269a2.625 2.625 0 0 0-3.712 0l-1.157 1.157 3.712 3.712 1.157-1.157a2.625 2.625 0 0 0 0-3.712ZM19.513 8.199l-3.712-3.712-12.15 12.15a5.25 5.25 0 0 0-1.32 2.214l-.8 2.685a.75.75 0 0 0 .933.933l2.685-.8a5.25 5.25 0 0 0 2.214-1.32L19.513 8.2Z"-->
<!--              />-->
<!--            </svg>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
</template>

<style scoped>
.introduce__container {
  @apply m-4 max-h-full overflow-auto;
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
