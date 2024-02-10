<script setup lang="ts">
import type {Background, UserInfo} from "@/types/global";
import Swal from 'sweetalert2'
import {changeDefaultBackgroundImageApi, purchaseBackgroundImageApi} from "@/api/background";

const props = defineProps({
  userInfo: Object as () => UserInfo,
  image: {
    type: Object as () => Background,
    default: {
      backgroundId: 0,
      fileName: undefined,
      imgUrl: '',
      price: 0,
      status: false,
      isSelected: false,
    }
  },
});
const emits = defineEmits([
  "setNotify", "setShowSuccess", "setShowFail", "loadMyInfo",
]);

// 배경 이미지 구매
const purchaseBackgroundImage = async (backgroundId: number) => {
  await purchaseBackgroundImageApi(
      {backgroundId: backgroundId},
      ({data}: any) => {
        if (data.status === 200 || data.status === 201) {
          emits("loadMyInfo");
          emits("setNotify", "배경 이미지 구매 성공", data["result"]);
          emits("setShowSuccess", true);
        } else {
          emits("setNotify", "배경 이미지 구매 실패", data["result"]);
          emits("setShowFail", true);
        }
      }, (error: any) => {
        console.log(error);
        emits("setNotify", "배경 이미지 변경 실패", "서버 에러 발생");
        emits("setShowFail", true);
      }
  );
}
// 배경 이미지 변경
const changeDefaultBackgroundImage = async (backgroundId: number) => {
  await changeDefaultBackgroundImageApi(
      {backgroundId: backgroundId},
      ({data}: any) => {
        if (data.status === 200 || data.status === 201) {
          emits("loadMyInfo");
          emits("setNotify", "배경 이미지 변경 성공", data["result"]);
          emits("setShowSuccess", true);
        } else {
          emits("setNotify", "배경 이미지 변경 실패", data["result"]);
          emits("setShowFail", true);
        }
      }, (error: any) => {
        console.log(error);
        emits("setNotify", "배경 이미지 변경 실패", "서버 에러 발생");
        emits("setShowFail", true);
      }
  );
}

const getTitle = () => {
  // 이미지 구매
  if (!props.image.status) {
    return "배경 구매";
  }
  // 이미지 변경
  if (!props.image.isSelected && props.image.status) {
    return "배경 변경";
  }
  return '';
}

const getText = () => {
  // 이미지 구매
  if (!props.image.status) {
    return `새로운 배경과 함께해요 (${props.image.price}포인트)`;
  }
  // 이미지 변경
  if (!props.image.isSelected && props.image.status) {
    return "기본 배경으로 변경할까요?";
  }
  return '';
}

const getConfirmButtonText = () => {
  // 이미지 구매
  if (!props.image.status) {
    return "구매";
  }
  // 이미지 변경
  if (!props.image.isSelected && props.image.status) {
    return "변경";
  }
  return '';
}

const clickConfirmBtn = async ()=> {
  // 이미지 구매
  if (!props.image.status) {
    await purchaseBackgroundImage(props.image.backgroundId);
  }
  // 이미지 변경
  else if (!props.image.isSelected && props.image.status) {
    await changeDefaultBackgroundImage(props.image.backgroundId);
  }
}

const clickBackgroundImgAlert = () => Swal.fire({
  title: getTitle(),
  text: getText(),
  imageUrl: props.image.imgUrl,
  imageWidth: 750,
  imageHeight: 300,
  imageAlt: "background image",
  showCancelButton: true,
  cancelButtonText: "취소",
  cancelButtonColor: "#ee8585",
  showConfirmButton: true,
  confirmButtonColor: "#ad7ae7",
  confirmButtonText: getConfirmButtonText(),
}).then((result) => {
  if (result.isConfirmed) {
    clickConfirmBtn();
  }
});

</script>

<template>
  <div class="relative rounded-lg ">
    <img
        v-if="image.isSelected"
        class="absolute bottom-0 right-0 z-10 lg:w-5 md:w-4 sm:w-2"
        src="/assets/images/mask/check_icon.png"
        alt="mask_selected_info"/>
    <img
        v-if="!image.isSelected && !image.status"
        class="absolute bottom-0 right-0 z-10 lg:w-5 md:w-4 sm:w-2"
        src="/assets/images/mask/lock_icon.png"
        alt="mask_locked_info"/>
    <div>
      <div
          class="group aspect-h-7 aspect-w-10 block w-full overflow-hidden rounded-lg bg-gray-100 focus-within:ring-2 focus-within:ring-indigo-500 focus-within:ring-offset-2 focus-within:ring-offset-gray-100">
        <img :src="image.imgUrl" alt="" class="pointer-events-none object-cover group-hover:opacity-75"/>
        <button type="button" v-if="!(image.isSelected && image.status)" class="absolute inset-0 focus:outline-none"
                @click="clickBackgroundImgAlert"></button>
      </div>
      <p class="pointer-events-none mt-2 block truncate font-semibold text-gray-900">
        {{ image.fileName }}</p>
    </div>
  </div>
</template>

<style scoped>

</style>