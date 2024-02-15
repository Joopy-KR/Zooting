<script setup lang="ts">
import {computed, onMounted, ref, watch} from "vue";
import {useRouter} from "vue-router";
import EditMaskItem from "@/components/profile/EditMaskItem.vue";
import IconMaskDropDown from "./IconMaskDropDown.vue";
import {changeDefaultMaskApi, getMaskListApi, getMyMaskListApi, purchaseMaskApi} from "@/api/mask";
import type {Mask, UserInfo, Notify} from "@/types/global";
import SuccessNotify from "@/components/util/SuccessNotification.vue";
import FailNotify from "@/components/util/FailNotification.vue";
import Swal from 'sweetalert2';

const router = useRouter();
const props = defineProps({
  userInfo: Object as () => UserInfo,
  isMyProfile: Boolean
});
const emits = defineEmits([
  "loadMyInfo"
]);
const selectedMaskId = ref<Number>();
const maskList = ref<Mask[]>([]);
const myMaskList = ref<Mask[]>([]);
const animalType = ref<string>("강아지"); // 현재 선택된 동물
const totalPage = ref<number>(0);
const currentPage = ref<number>(0);
const showSuccess = ref<boolean>(false);
const showFail = ref<boolean>(false);
const notify = ref<Notify>({
  title: '',
  message: '',
})

const setShowSuccess = (status: boolean) => {
  showSuccess.value = status;
}
const setShowFail = (status: boolean) => {
  showFail.value = status;
}
const setNotify = (title: string, message: any) => {
  notify.value.title = title;
  notify.value.message = message;
}

const loadMyInfo = () => {
  emits("loadMyInfo");
}

const clickMaskAlert = (title: string, text: string, type: string, mask: Mask) => Swal.fire({
  title: title,
  text: text,
  imageUrl: mask.imgUrl,
  imageWidth: 400,
  imageHeight: 400,
  imageAlt: "mask image",
  showCancelButton: true,
  cancelButtonText: "취소",
  cancelButtonColor: "#ee8585",
  showConfirmButton: true,
  confirmButtonColor: "#ad7ae7",
  confirmButtonText: type === 'purchase' ? "구매" : "변경",
}).then((result) => {
  if (result.isConfirmed) {
    if (type === "purchase") {
      purchaseMask(mask.maskId);
    } else {
      changeDefaultMask(mask.maskId);
    }
  }
});
const clickMask = (mask: Mask) => {
  if (!props.userInfo?.animal) {
    return;
  }
  if (props.userInfo.animal !== mask.animal) {
    setNotify("동물상 불일치", "내 동물상과 달라 사용하실 수 없습니다.");
    setShowFail(true);
    return;
  }
  // 내가 소유한 mask가 아니라면 구매
  if (!mask.status) {
    clickMaskAlert(
        "마스크 구매",
        `새로운 마스크와 함께해요. (${mask.price}포인트)`,
        "purchase",
        mask
    );
  } // 내가 소유한 마스크이면 default 마스크로 변경
  else {
    // 현재 마스크와 동일한 경우
    if (mask.isSelected) {
      setNotify("동물상 가면 변경 실패", "현재 마스크와 일치합니다.");
      setShowFail(true);
    } else {
      clickMaskAlert(
          "마스크 변경",
          "이 마스크와 함께 하시나요?",
          "change",
          mask
      );
    }
  }
}
// 마스크 변경하기
const changeDefaultMask = (maskId: number) => {
  changeDefaultMaskApi({maskId: maskId},
      ({data}: any) => {
        if (data.status === 200 || data.status === 201) {
          setNotify("마스크 변경", data.result);
          setShowSuccess(true);
          selectedMaskId.value = maskId;
          emits("loadMyInfo");
          getMyMaskList(props.userInfo?.animal);
        } else {
          setNotify("마스크 변경", data.result);
          setShowFail(true);
        }
      },
      (error: any) => {
        console.log(error);
        setNotify("마스크 변경", "마스크 변경에 실패 했습니다.");
        setShowFail(true);
      }
  )
}

// 마스크 구매하기
const purchaseMask = (maskId: number) => {
  purchaseMaskApi({maskId: maskId},
      ({data}: any) => {
        if (data.status === 200 || data.status === 201) {
          setNotify("마스크 구매", data.result);
          setShowSuccess(true);
          emits("loadMyInfo");
          getMyMaskList(props.userInfo?.animal);
        } else {
          setNotify("마스크 구매", data.result);
          setShowFail(true);
        }
      }, (error: any) => {
        console.log(error);
        setNotify("마스크 구매", "마스크 구매를 실패했습니다.");
        setShowFail(true);
      })
}

const filterMaskByAnimal = computed(() => {
  return maskList.value.filter((mask) => mask.animal === animalType.value);
});

const getMaskList = async (animal: string | undefined, page: number) => {
  await getMaskListApi(
      {
        animal: animal,
        page: page,
        size: 6,
      },
      ({data}: any) => {
        const maskData = data["result"]["maskResList"];
        currentPage.value = data["result"]["currentPage"];
        totalPage.value = data["result"]["totalPage"];

        const masks: Mask[] = [];
        if (!maskData) {
          maskList.value = [];
          return;
        }
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
      },
      (error: any) => console.error(error)
  );
};

const getMyMaskList = async (animal: string | undefined) => {
  if (!animal) return;
  await getMyMaskListApi(
      ({data}: any) => {
        const maskData = data["result"];
        const masks: Mask[] = [];
        for (const mask of maskData) {
          const tmp = {
            maskId: mask.maskId,
            animal: mask.animal,
            description: mask.description,
            price: mask.price,
            fileName: mask.fileName,
            imgUrl: mask.imgUrl,
            status: true,
            isSelected: false,
          };
          masks.push(tmp);
        }

        myMaskList.value = masks;
      },
      (error: any) => console.error(error)
  );
};

const redoAnimalTest = () => {
  router.push({name: "animal-test"});
}

const setAnimalType = (animal: string) => {
  animalType.value = animal;
};

const moveToMyPage = () => {
  router.push({
    name: "profile-check",
    params: {nickname: props.userInfo!.nickname},
  });
};

const moveToLeft = async () => {
  if (currentPage.value > 0) {
    currentPage.value = currentPage.value - 1;
    await getMaskList(animalType.value, currentPage.value);
    await getMyMaskList(animalType.value);
  }
}

const moveToRight = async () => {
  if (currentPage.value < totalPage.value - 1) {
    currentPage.value = currentPage.value + 1;
    await getMaskList(animalType.value, currentPage.value);
    await getMyMaskList(animalType.value);
  }
}

watch(myMaskList, (newMyMaskList) => {
  if (!newMyMaskList) return;

  newMyMaskList.forEach((myMask) => {
    const correspondingMask = maskList.value?.find(
        (mask: Mask) => mask.maskId === myMask.maskId
    );

    if (correspondingMask) {
      // myMaskList의 id와 maskList의 id가 같은 경우에만 처리
      correspondingMask.status = myMask.status;
      if (correspondingMask.maskId === selectedMaskId.value) {
        correspondingMask.isSelected = true;
      } else {
        correspondingMask.isSelected = false;
      }
    }
  });
});
watch(() => props.userInfo, (newValue) => {
  if (newValue?.animal) {
    animalType.value = newValue?.animal;
  }
  if (newValue?.maskId) {
    selectedMaskId.value = newValue?.maskId;
  }
})
watch(() => animalType.value, async (newValue) => {
  await getMaskList(newValue, 0);
  await getMyMaskList(newValue);
})

onMounted(async () => {
  if (props.userInfo) {
    if (props.userInfo?.animal) {
      animalType.value = props.userInfo?.animal;
      await getMaskList(animalType.value, 0);
      await getMyMaskList(animalType.value);
    }
    if (props.userInfo?.maskId) {
      selectedMaskId.value = props.userInfo?.maskId;
    }
  }
});
</script>

<template>
  <SuccessNotify
      :title="notify.title"
      :message="notify.message"
      :show-from-parent="showSuccess"
      @set-parent-show="setShowSuccess"
  />
  <FailNotify
      :title="notify.title"
      :message="notify.message"
      :show-from-parent="showFail"
      @set-parent-show="setShowFail"
  />
  <div class="relative flex flex-col">
    <div @click="moveToMyPage()" class="absolute flex flex-col items-center ml-4 top-5 left-5">
      <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="1.5"
          stroke="currentColor"
          class="w-10 h-10 mx-auto stroke-orange-500 fill-rose-100 hover:fill-rose-300"
      >
        <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M8.25 9V5.25A2.25 2.25 0 0 1 10.5 3h6a2.25 2.25 0 0 1 2.25 2.25v13.5A2.25 2.25 0 0 1 16.5 21h-6a2.25 2.25 0 0 1-2.25-2.25V15m-3 0-3-3m0 0 3-3m-3 3H15"
        />
      </svg>
      <p class="font-sans text-xs font-semibold tracking-tight text-center">마이페이지</p>
    </div>
    <p class="font-bold tracking-tighter text-center sm:pt-16 lg:px-12 lg:pt-16 lg:pb-10 lg:text-3xl sm:text-xl">
      아바타 동물상 선택
    </p>
    <div>
      <div class="flex flex-row justify-between px-12 mr-4">
        <IconMaskDropDown :animal-type="animalType" :gender="userInfo?.gender" @set-animal-type="setAnimalType"
                          class="z-30"/>
        <span
            class="inline-flex items-center gap-x-1.5 rounded-full bg-green-200/60 px-2 py-1 text-xs font-medium text-gray-600 w-auto h-11"
        >
          <div class="flex items-center justify-center tracking-tighter">
            <p class="py-2 pl-4 pr-2 font-sans font-semibold text-center text-gray-900 lg:text-xl">
              {{ userInfo?.point ? userInfo.point : 0 }}
            </p>
            <p class="py-2 pr-4 font-sans text-xl font-medium text-center">Point</p>
          </div>
        </span>
      </div>
    </div>
    <div class="flex flex-row items-center content-center justify-center w-2/3 mx-auto">
      <div v-if="currentPage > 0" @click="moveToLeft">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-10 h-10 stroke-violet-400 hover:stroke-violet-600/80">
          <path stroke-linecap="round" stroke-linejoin="round" d="m18.75 4.5-7.5 7.5 7.5 7.5m-6-15L5.25 12l7.5 7.5" />
        </svg>
      </div>
      <div class="grid grid-cols-3 gap-3 px-12 py-8 m-4">
        <EditMaskItem v-for="mask in filterMaskByAnimal" :key="mask.maskId" :mask="mask"
                      :selected-mask-id="selectedMaskId" @load-my-info="loadMyInfo"
                      @click="clickMask(mask)"/>
      </div>
      <div v-if="currentPage < totalPage - 1" @click="moveToRight">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-10 h-10 stroke-violet-400 hover:stroke-violet-600/80">
          <path stroke-linecap="round" stroke-linejoin="round" d="m5.25 4.5 7.5 7.5-7.5 7.5m6-15 7.5 7.5-7.5 7.5" />
        </svg>
      </div>
    </div>
    <div class="flex justify-center">
      <button
          type="button"
          class="rounded-full bg-indigo-600 px-4 py-2.5 w-1/2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
          @click="redoAnimalTest"
      >
        동물상 다시 검사하기
      </button>
    </div>
  </div>
</template>

<style scoped></style>
