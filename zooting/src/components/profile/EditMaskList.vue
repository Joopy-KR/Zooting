<script setup lang="ts">
import { ref, defineProps, onMounted, watch, computed } from "vue";
import { useRouter } from "vue-router";
import EditMaskItem from "@/components/profile/EditMaskItem.vue";
import IconMaskDropDown from "./IconMaskDropDown.vue";
import { getMyMaskListApi, getMaskListApi } from "@/api/mask";

const router = useRouter();
const props = defineProps({
  userInfo: Object,
});

const maskList = ref<Mask[]>([]);
const myMaskList = ref<Mask[]>([]);
const animalType = ref<string>("강아지");

interface Mask {
  maskId: number;
  animal: string;
  description: string;
  price: number;
  fileName: string;
  imgUrl: string;
  status: boolean;
  isSelected: boolean;
}

const filterMaskByAnimal = computed(() => {
  return maskList.value.filter((mask) => mask.animal === animalType.value);
});

const getMaskList = (type: string, page: number) => {
  getMaskListApi(
    {
      type: type,
      page: page,
      size: 6,
    },
    ({ data }: any) => {
      const maskData = data["result"];

      const masks: Mask[] = [];
      for (const mask of maskData) {
        masks.push({
          maskId: mask.maskId,
          animal: mask.animal,
          description: mask.description,
          price: mask.price,
          fileName: mask.fileName,
          imgUrl: mask.imgUrl,
          status: false,
          isSelected: false,
        });
      }
      maskList.value = masks;
      console.log("mask-list", maskList.value);
    },
    (error: any) => console.error(error)
  );
};

const getMyMaskList = (type: string) => {
  getMyMaskListApi(
    type,
    ({ data }: any) => {
      const maskData = data["result"];

      const masks: Mask[] = [];
      for (const mask of maskData) {
        masks.push({
          maskId: mask.maskId,
          animal: mask.animal,
          description: mask.description,
          price: mask.price,
          fileName: mask.fileName,
          imgUrl: mask.imgUrl,
          status: true,
          isSelected: false,
        });
      }

      myMaskList.value = masks;
      console.log("my-mask-list", myMaskList.value);
    },
    (error: any) => console.error(error)
  );
};

const setAnimalType = (animal: string) => {
  animalType.value = animal;
};

const moveToMyPage = () => {
  router.push({
    name: "profile-check",
    params: { nickname: props.userInfo!.nickname },
  });
};

watch(myMaskList, (newMyMaskList, oldMyMaskList) => {
  if (!maskList.value) return;
  if (!newMyMaskList || !oldMyMaskList) {
    return;
  }

  // myMaskList가 변경될 때 실행되는 콜백 함수

  // 예전 목록과 새로운 목록을 비교하여 변경된 항목을 찾음
  const changedItems = newMyMaskList.filter((newItem) => {
    const oldItem = oldMyMaskList.find((item) => item.maskId === newItem.maskId);
    return oldItem && oldItem.status !== newItem.status;
  });

  // 변경된 항목에 대해 처리
  changedItems.forEach((changedItem) => {
    const correspondingMask = maskList.value?.find(
      (mask: Mask) => mask.maskId === changedItem.maskId
    );

    if (correspondingMask) {
      // myMaskList의 id와 maskList의 id가 같은 경우에만 처리
      correspondingMask.status = changedItem.status;
    }
  });
  console.log("update----", maskList.value);
});

onMounted(async () => {
  await getMaskList("강아지", 0);
  await getMyMaskList("");
});
</script>

<template>
  <div class="flex flex-col relative">
    <div @click="moveToMyPage()" class="flex flex-col items-center ml-4 absolute top-5 left-5">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        stroke-width="1.5"
        stroke="currentColor"
        class="w-10 h-10 stroke-orange-500 fill-rose-100 mx-auto hover:fill-rose-300"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M8.25 9V5.25A2.25 2.25 0 0 1 10.5 3h6a2.25 2.25 0 0 1 2.25 2.25v13.5A2.25 2.25 0 0 1 16.5 21h-6a2.25 2.25 0 0 1-2.25-2.25V15m-3 0-3-3m0 0 3-3m-3 3H15"
        />
      </svg>
      <p class="font-sans font-semibold text-xs tracking-tight text-center">마이페이지</p>
    </div>
    <p class="lg:px-12 lg:pt-16 lg:pb-10 lg:text-4xl font-bold tracking-tighter text-center">
      아바타 동물상 선택
    </p>
    <div>
      <div class="flex flex-row justify-between px-12 mr-4">
        <IconMaskDropDown :animal-type="animalType" @set-animal-type="setAnimalType" class="z-30" />
        <span
          class="inline-flex items-center gap-x-1.5 rounded-full bg-gray-100 px-2 py-1 text-xs font-medium text-gray-600 w-auto h-11"
        >
          <div class="flex items-center justify-center tracking-tighter">
            <p class="pl-4 pr-2 py-2 lg:text-xl font-semibold text-center font-sans text-gray-900">
              {{ userInfo?.point ? userInfo.point : 0 }}
            </p>
            <p class="text-xl pr-4 py-2 font-medium text-center font-sans">Point</p>
          </div>
        </span>
      </div>
    </div>
    <div class="flex items-center content-center justify-center w-2/3 mx-auto">
      <div class="grid grid-cols-3 gap-3 px-12 py-8 m-4">
        <EditMaskItem v-for="mask in filterMaskByAnimal" :key="mask.maskId" :mask="mask" />
      </div>
    </div>
    <div class="flex justify-center">
      <button
        type="button"
        class="rounded-full bg-indigo-600 px-4 py-2.5 w-1/2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
      >
        동물상 다시 검사하기
      </button>
    </div>
  </div>
</template>

<style scoped></style>
