<template>
  <SuccessNotification
      :title="notify.title"
      :message="notify.message"
      :show-from-parent="showSuccess"
      @set-parent-show="setShowSuccess"
  />
  <FailNotification
      :title="notify.title"
      :message="notify.message"
      :show-from-parent="showFail"
      @set-parent-show="setShowFail"
  />
  <TransitionRoot as="template" :show="$props.showFromParent">
    <Dialog as="div" class="relative z-10" @close="closeDialog">
      <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0" enter-to="opacity-100"
                       leave="ease-in duration-200" leave-from="opacity-100" leave-to="opacity-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"/>
      </TransitionChild>

      <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
        <div class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
          <TransitionChild as="template" enter="ease-out duration-300"
                           enter-from="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
                           enter-to="opacity-100 translate-y-0 sm:scale-100" leave="ease-in duration-200"
                           leave-from="opacity-100 translate-y-0 sm:scale-100"
                           leave-to="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95">
            <DialogPanel
                class="relative transform overflow-hidden rounded-lg bg-white px-10 pb-8 pt-5 text-left shadow-xl transition-all max-h-fit">
              <div class="pb-5">
                <p class="text-center font-bold text-gray-900 lg:text-xl">배경 이미지</p>
              </div>
              <div>
                <!-- Content -->
                <ul role="list"
                    class="grid grid-cols-1 gap-x-10 gap-y-8 sm:gap-x-6 sm:grid-cols-1 lg:grid-cols-2 md:grid-cols-2">
                  <li v-for="image in images" :key="image.source" class="relative lg:min-w-96 md:min-w-60 sm:min-w-44">
                    <div
                        class="group aspect-h-7 aspect-w-10 block w-full overflow-hidden rounded-lg bg-gray-100 focus-within:ring-2 focus-within:ring-indigo-500 focus-within:ring-offset-2 focus-within:ring-offset-gray-100">
                      <img :src="image.source" alt="" class="pointer-events-none object-cover group-hover:opacity-75"/>
                      <button type="button" class="absolute inset-0 focus:outline-none"/>
                    </div>
                    <p class="pointer-events-none mt-2 block truncate font-semibold text-gray-900">
                      {{ image.title }}</p>
                  </li>
                </ul>
              </div>
              <!-- 확인 버튼 -->
              <div class="flex justify-center mt-5 sm:mt-6 pt-6">
                <button type="button"
                        class="w-1/2 rounded-md bg-violet-500 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-violet-400 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-violet-500"
                        @click="closeDialog">돌아가기
                </button>
              </div>
            </DialogPanel>
          </TransitionChild>
        </div>
      </div>
    </Dialog>
  </TransitionRoot>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {Dialog, DialogPanel, TransitionChild, TransitionRoot} from '@headlessui/vue'
import SuccessNotification from "@/components/util/SuccessNotification.vue";
import FailNotification from "@/components/util/FailNotification.vue";
import type {Background, Notify} from "@/types/global";
import {
  changeDefaultBackgroundImageApi,
  loadBackgroundImageApi,
  loadMyBackgroundImageApi,
  purchaseBackgroundImageApi
} from "@/api/background";

const props = defineProps({
  showFromParent: {
    type: Boolean,
    default: false,
  },
})
const emits = defineEmits(["setParentShow"])

const closeDialog = () => {
  emits("setParentShow", false);
}

const showSuccess = ref(false);
const showFail = ref(false);
const notify = ref<Notify>({
  title: '',
  message: undefined,
});
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

interface BackgroundImage

const myImages = ref<Background[]>([]);
const images = ref<Background[]>([]);
const currentPage = ref<number>(0);
const totalPage = ref<number>(0);
const DEFAULT_SIZE = 4;

onMounted(async () => {

});
// 배경 이미지 조회 (한번에 4개씩)
const loadBackgroundImage = async (page: number) => {
  await loadBackgroundImageApi({page: page, size: DEFAULT_SIZE},
      ({data}: any) => {
        if (data.status === 200 || data.status === 201) {
          const result = data["result"];

          // TODO 이미지 저장, 저장시 소유 여부 및 기본 이미지 인지 확인 필요
          for (const r of result) {
          }
        } else {

        }
      },
      (error: any) => console.log(error)
  );
}
// 내 배경 이미지 조회
const loadMyBackgroundImage = async () => {
  await loadMyBackgroundImageApi(
      ({data}: any) => {
        if (data.status === 200 || data.status === 201) {
          const result = data["result"];

          myImages.value = [];
          for (const r of result) {
            myImages.value.push({
              backgroundId: r.backgroundId,
              fileName: r.fileName,
              imgUrl: r.imgUrl,
              price: r.price
            })
          }
        } else {
          setNotify("내 배경 불러오기 실패", "내 배경 목록 조회에 실패하였습니다.");
          setShowFail(true);
        }
      },
      (error: any) => console.log(error)
  );
}
// 배경 이미지 구매
const purchaseBackgroundImage = async (backgroundId: number) => {
  await purchaseBackgroundImageApi(
      {backgroundId: backgroundId},
      ({data}: any) => {
        if (data.status === 200 || data.status === 201) {
          setNotify("배경 이미지 구매 성공", data["result"]);
          setShowSuccess(true);
        } else {
          setNotify("배경 이미지 구매 실패", data["result"]);
          setShowFail(true);
        }
      }, (error: any) => {
        console.log(error);
        setNotify("배경 이미지 변경 실패", "서버 에러 발생");
        setShowFail(true);
      }
  );
}
// 배경 이미지 변경
const changeDefaultBackgroundImage = async (backgroundId: number) => {
  await changeDefaultBackgroundImageApi(
      {backgroundId: backgroundId},
      ({data}: any) => {
        if (data.status === 200 || data.status === 201) {
          setNotify("배경 이미지 변경 성공", data["result"]);
          setShowSuccess(true);
        } else {
          setNotify("배경 이미지 변경 실패", data["result"]);
          setShowFail(true);
        }
      }, (error: any) => {
        console.log(error);
        setNotify("배경 이미지 변경 실패", "서버 에러 발생");
        setShowFail(true);
      }
  );
}

const jimages = [
  {
    title: 'IMG_4985.HEIC',
    size: '3.9 MB',
    source:
        'https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/oilpaintart.jpg',
  },
  {
    title: 'IMG_4985.HEIC',
    size: '3.9 MB',
    source:
        'https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/oilpaintart.jpg',
  },
  {
    title: 'IMG_4985.HEIC',
    size: '3.9 MB',
    source:
        'https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/oilpaintart.jpg',
  },
  {
    title: 'IMG_4985.HEIC',
    size: '3.9 MB',
    source:
        'https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/oilpaintart.jpg',
  },
]
</script>

<style scoped>

.group {
  position: relative;
  width: 100%;
  height: 0;
  padding-top: calc(100% / (20 / 8));
  overflow: hidden;
}

/* 이미지를 절대 위치로 설정하여 상위 div에 꽉 차게 함 */
.group img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover; /* 이미지 비율 유지 */
}
</style>