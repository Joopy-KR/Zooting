<template>
  <div>
    <div class="grid items-center justify-center grid-cols-1 md:flex" v-show="is_started">
      <div id="webcam-container">
        <video id="webcam" ref="videoRef" autoplay playsinline></video>
      </div>

      <div class="flex flex-col items-center justify-between ml-10">
        <div>
          <p class="mb-5 text-2xl font-bold">사진은 저장되지 않아요</p>
          <Popover class="relative flex items-center justify-center">
            <span class="mr-2">촬영이 되지 않나요?</span>
            <PopoverButton
              class="inline-flex items-center text-sm font-semibold leading-6 text-gray-900 gap-x-1"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="w-6 h-6"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="m11.25 11.25.041-.02a.75.75 0 0 1 1.063.852l-.708 2.836a.75.75 0 0 0 1.063.853l.041-.021M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Zm-9-3.75h.008v.008H12V8.25Z"
                />
              </svg>
            </PopoverButton>
            <transition
              enter-active-class="transition duration-200 ease-out"
              enter-from-class="translate-y-1 opacity-0"
              enter-to-class="translate-y-0 opacity-100"
              leave-active-class="transition duration-150 ease-in"
              leave-from-class="translate-y-0 opacity-100"
              leave-to-class="translate-y-1 opacity-0"
            >
              <PopoverPanel
                class="absolute z-10 flex w-screen px-4 mt-5 -translate-x-1/2 left-1/2 max-w-max"
              >
                <div
                  class="flex-auto w-full max-w-md overflow-hidden text-sm leading-6 bg-white shadow-lg rounded-3xl ring-1 ring-gray-900/5"
                >
                  <div class="flex flex-col items-center justify-center p-4">
                    <div class="mt-10 hide-button">
                      <div href="#" class="rounded" style="position: static; margin-left: 80px">
                        <svg
                          version="1.1"
                          x="0px"
                          y="0px"
                          width="512px"
                          height="512px"
                          viewBox="0 0 512 512"
                          enable-background="new 0 0 512 512"
                          xml:space="preserve"
                        >
                          <path
                            id="instagram-icon"
                            d="M341.205,197.143H460.93v183.795c0,44.77-36.292,81.062-81.062,81.062H132.133c-44.769,0-81.062-36.293-81.062-81.062V197.143H170.26c-12.2,17.141-19.399,38.087-19.399,60.729c0,57.919,46.953,104.872,104.873,104.872c57.919,0,104.872-46.953,104.872-104.872C360.605,235.229,353.405,214.283,341.205,197.143z M460.93,131.062v53.239H330.447c-19.022-19.315-45.465-31.302-74.714-31.302c-29.251,0-55.693,11.986-74.714,31.302H51.07v-53.239c0-27.322,13.532-51.469,34.245-66.154v87.289h16.62v-96.36c3.77-1.516,7.679-2.752,11.704-3.691v100.052h16.621V50.047c0.625-0.015,1.245-0.047,1.873-0.047h9.598v102.196h16.62V50h221.517C424.638,50,460.93,86.292,460.93,131.062z M423.879,96.897c0-7.181-5.822-13.002-13.003-13.002h-43.821c-7.183,0-13.003,5.821-13.003,13.002v44.785c0,7.181,5.82,13.002,13.003,13.002h43.821c7.181,0,13.003-5.821,13.003-13.002V96.897z M174.938,257.872c0-24.188,10.698-45.909,27.593-60.729c5.926-5.197,12.613-9.539,19.875-12.842c10.169-4.625,21.447-7.224,33.327-7.224c11.881,0,23.157,2.599,33.326,7.224c7.263,3.303,13.95,7.645,19.876,12.842c16.895,14.82,27.592,36.542,27.592,60.729c0,44.55-36.243,80.794-80.794,80.794C211.183,338.666,174.938,302.422,174.938,257.872z M196.286,257.872c0,32.979,26.735,59.712,59.714,59.712c32.979,0,59.713-26.733,59.713-59.712c0-32.98-26.733-59.713-59.713-59.713C223.021,198.159,196.286,224.892,196.286,257.872z"
                          />
                        </svg>
                      </div>
                    </div>
                    <div class="flex flex-col">
                      <p>카메라는 얼굴을 인식해요</p>
                      <p>위처럼 촬영 버튼이 회색이면 촬영되지 않아요</p>
                      <br />
                      <p>
                        1. 카메라에 얼굴을 <span class="text-violet-500">가까이</span> 가져와 주세요
                      </p>
                      <p>
                        2. 카메라에 두 명 이상 있으면 촬영되지
                        <span class="text-violet-500">않아요</span>
                      </p>
                    </div>
                  </div>
                </div>
              </PopoverPanel>
            </transition>
          </Popover>
        </div>

        <div class="mt-14 md:mt-48 show-button" v-show="showButton">
          <a href="javascript:void(0)" class="rounded" @click="stop">
            <svg
              version="1.1"
              x="0px"
              y="0px"
              width="512px"
              height="512px"
              viewBox="0 0 512 512"
              enable-background="new 0 0 512 512"
              xml:space="preserve"
            >
              <path
                id="instagram-icon"
                d="M341.205,197.143H460.93v183.795c0,44.77-36.292,81.062-81.062,81.062H132.133c-44.769,0-81.062-36.293-81.062-81.062V197.143H170.26c-12.2,17.141-19.399,38.087-19.399,60.729c0,57.919,46.953,104.872,104.873,104.872c57.919,0,104.872-46.953,104.872-104.872C360.605,235.229,353.405,214.283,341.205,197.143z M460.93,131.062v53.239H330.447c-19.022-19.315-45.465-31.302-74.714-31.302c-29.251,0-55.693,11.986-74.714,31.302H51.07v-53.239c0-27.322,13.532-51.469,34.245-66.154v87.289h16.62v-96.36c3.77-1.516,7.679-2.752,11.704-3.691v100.052h16.621V50.047c0.625-0.015,1.245-0.047,1.873-0.047h9.598v102.196h16.62V50h221.517C424.638,50,460.93,86.292,460.93,131.062z M423.879,96.897c0-7.181-5.822-13.002-13.003-13.002h-43.821c-7.183,0-13.003,5.821-13.003,13.002v44.785c0,7.181,5.82,13.002,13.003,13.002h43.821c7.181,0,13.003-5.821,13.003-13.002V96.897z M174.938,257.872c0-24.188,10.698-45.909,27.593-60.729c5.926-5.197,12.613-9.539,19.875-12.842c10.169-4.625,21.447-7.224,33.327-7.224c11.881,0,23.157,2.599,33.326,7.224c7.263,3.303,13.95,7.645,19.876,12.842c16.895,14.82,27.592,36.542,27.592,60.729c0,44.55-36.243,80.794-80.794,80.794C211.183,338.666,174.938,302.422,174.938,257.872z M196.286,257.872c0,32.979,26.735,59.712,59.714,59.712c32.979,0,59.713-26.733,59.713-59.712c0-32.98-26.733-59.713-59.713-59.713C223.021,198.159,196.286,224.892,196.286,257.872z"
              />
            </svg>
          </a>
        </div>

        <div class="mt-14 md:mt-48 hide-button" v-show="!showButton">
          <div href="#" class="rounded">
            <svg
              version="1.1"
              x="0px"
              y="0px"
              width="512px"
              height="512px"
              viewBox="0 0 512 512"
              enable-background="new 0 0 512 512"
              xml:space="preserve"
            >
              <path
                id="instagram-icon"
                d="M341.205,197.143H460.93v183.795c0,44.77-36.292,81.062-81.062,81.062H132.133c-44.769,0-81.062-36.293-81.062-81.062V197.143H170.26c-12.2,17.141-19.399,38.087-19.399,60.729c0,57.919,46.953,104.872,104.873,104.872c57.919,0,104.872-46.953,104.872-104.872C360.605,235.229,353.405,214.283,341.205,197.143z M460.93,131.062v53.239H330.447c-19.022-19.315-45.465-31.302-74.714-31.302c-29.251,0-55.693,11.986-74.714,31.302H51.07v-53.239c0-27.322,13.532-51.469,34.245-66.154v87.289h16.62v-96.36c3.77-1.516,7.679-2.752,11.704-3.691v100.052h16.621V50.047c0.625-0.015,1.245-0.047,1.873-0.047h9.598v102.196h16.62V50h221.517C424.638,50,460.93,86.292,460.93,131.062z M423.879,96.897c0-7.181-5.822-13.002-13.003-13.002h-43.821c-7.183,0-13.003,5.821-13.003,13.002v44.785c0,7.181,5.82,13.002,13.003,13.002h43.821c7.181,0,13.003-5.821,13.003-13.002V96.897z M174.938,257.872c0-24.188,10.698-45.909,27.593-60.729c5.926-5.197,12.613-9.539,19.875-12.842c10.169-4.625,21.447-7.224,33.327-7.224c11.881,0,23.157,2.599,33.326,7.224c7.263,3.303,13.95,7.645,19.876,12.842c16.895,14.82,27.592,36.542,27.592,60.729c0,44.55-36.243,80.794-80.794,80.794C211.183,338.666,174.938,302.422,174.938,257.872z M196.286,257.872c0,32.979,26.735,59.712,59.714,59.712c32.979,0,59.713-26.733,59.713-59.712c0-32.98-26.733-59.713-59.713-59.713C223.021,198.159,196.286,224.892,196.286,257.872z"
              />
            </svg>
          </div>
        </div>
      </div>
    </div>

    <div class="flex flex-col items-center justify-center" v-show="!is_started">
      <p class="mb-8 text-5xl font-bold">카메라 준비 중</p>
      <h3 class="mb-5">잠시만 기다려 주세요</h3>
      <div class="flex items-center justify-center" role="status">
        <svg
          aria-hidden="true"
          class="inline w-3/12 text-gray-200 animate-spin dark:text-gray-600 fill-pink-400"
          viewBox="0 0 100 101"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
            fill="currentColor"
          />
          <path
            d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
            fill="currentFill"
          />
        </svg>
      </div>
    </div>
  </div>
</template>

<script setup type="text/javascript" lang="ts">
import { Popover, PopoverButton, PopoverPanel } from "@headlessui/vue";
import { ref, onMounted, nextTick, computed } from "vue";
import { FaceDetector, FilesetResolver } from "@mediapipe/tasks-vision";
import type {Detection} from "@mediapipe/tasks-vision";
import { useAccessTokenStore } from "@/stores/store";

const emit = defineEmits(["workFinished"]);
const store = useAccessTokenStore()

// 얼굴인식 변수
let faceDetector: FaceDetector;
let runningMode: any = "IMAGE";
const videoRef = ref <HTMLVideoElement | null> (null);

const is_started = ref(false); // 카메라 로딩을 판단하는 변수 (false시 로딩중을 출력)
let is_working = true; // 촬영버튼 시 측정 동작을 멈추는 변수

const showButton = ref(true); // 유사도가 90 이상일때 촬영버튼 출력

onMounted(() => {
  initializeFaceDetector();
  init();
  store.getUserInfo()
});

// 성별 정보
const gender = computed(() => {
  if (store.userInfo?.gender === 'man') {
    return 'male';
  } else {
    return 'female';
  }
});

// 얼굴 인식 모델
// 얼굴 인식기 초기화
const initializeFaceDetector = async () => {
  await nextTick();

  const vision = await FilesetResolver.forVisionTasks(
    "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.0/wasm"
  );

  faceDetector = await FaceDetector.createFromOptions(vision, {
    baseOptions: {
      modelAssetPath:
        "https://storage.googleapis.com/mediapipe-models/face_detector/blaze_face_short_range/float16/1/blaze_face_short_range.tflite",
      delegate: "GPU",
    },
    runningMode: runningMode,
  });

  enableCam();
};

// 웹캠 실행 및 얼굴인식 시작(predictWebcam 함수)
const enableCam = async () => {
  if (!faceDetector) {
    alert("얼굴 인식 시스템이 로딩 중입니다. 다시 시도해 주세요");
    return;
  }

  const constraints = {
    video: true,
  };

  try {
    const stream = await navigator.mediaDevices.getUserMedia(constraints);
    if (videoRef.value) {
      is_started.value = true;
      videoRef.value.srcObject = stream;
      videoRef.value.addEventListener("loadeddata", predictWebcam);
    }
  } catch (err) {
    console.error(err);
  }
};

// videoRef.value가 채워질 때까지 대기하는 함수
const waitForVideoRef = async () => {
  while (!videoRef.value) {
    // videoRef.value가 채워질 때까지 대기
    await new Promise((resolve) => setTimeout(resolve, 100)); // 적절한 대기 시간 사용
  }
};

let lastVideoTime = -1;
const predictWebcam = async () => {
  await waitForVideoRef();
  if (runningMode === "IMAGE") {
    runningMode = "VIDEO";
    await faceDetector.setOptions({ runningMode: "VIDEO" });
  }

  let startTimeMs = performance.now();

  if (!videoRef.value) return;

  // detectForVideo 함수를 사용하여 얼굴 추적
  if (videoRef.value.currentTime !== lastVideoTime) {
    lastVideoTime = videoRef.value.currentTime;
    const detections = faceDetector.detectForVideo(videoRef.value, startTimeMs).detections;
    displayVideoDetections(detections);
  }

  window.requestAnimationFrame(predictWebcam);
};

// 대기 함수
function wait(sec: any) {
  return new Promise((resolve) => setTimeout(resolve, sec * 1000));
}

const isPlaying = ref(false);

const displayVideoDetections = async (detections: Detection[]) => {
  isPlaying.value = false;
  await wait(0.5);
  // 예상치 측정
  for (let detection of detections) {
    isPlaying.value = true;
    // 유사도가 89 이상이면 촬영 버튼 출력
    if (Math.round(parseFloat(String(detection.categories[0].score)) * 100) >= 90) {
      // 유사도가 89 이상일때 촬영버튼 출력
      showButton.value = true;
    } else {
      // 유사도가 89 미만일때 촬영버튼 숨김
      showButton.value = false;
    }
  }
  if (isPlaying.value === false) {
    showButton.value = false;
  }
};

// 동물상 분석 모델
let model:any, webcam:any;

// 남자는 강아지, 고양이, 토끼, 곰, 공룡
// 여자는 강아지, 고양이, 토끼, 사슴, 꼬북이
// 강아지 퍼센트
const dog = ref(0);
const cat = ref(0);
const rabbit = ref(0);
const bear = ref(0);
const dino = ref(0);
const deer = ref(0);
const turtle = ref(0);

// 모델 경로

const URL = `src/assets/animal_test_model/${gender.value}`;

// 모델 로딩 후 웹캠 출력
const init = async () => {
  await initializeAnimalModel();
  webcam = await setupWebcam();
  await webcam.play();
  window.requestAnimationFrame(loop);
};

const initializeAnimalModel = async () => {
  const modelURL = URL + "/model.json";
  const metadataURL = URL + "/metadata.json";

  // 모델 및 메타데이터 로드
  // "tmImage" 객체는 라이브러리로부터 추가되어있는 상태임 (window.tmImage)
  // @ts-ignore
  model = await tmImage.load(modelURL, metadataURL);
};

const setupWebcam = async () => {
  const flip = true;
  // @ts-ignore
  const webcam = new tmImage.Webcam(500, 500, flip);

  try {
    await webcam.setup();
  } catch (err) {
    if (err === "Could not open your camera. You may have denied access.") {
      window.alert("카메라가 차단되어있어요. 브라우저 설정을 확인해주세요");
    }
  }

  return webcam;
};

const loop = async () => {
  webcam.update(); // 웹캠 프레임 업데이트 후 stop 될때까지 무한루프
  await predict();
  if (is_working) {
    window.requestAnimationFrame(loop);
  }
};

// 이미지 모델을 기반으로 웹캠 실행
const predict = async () => {
  // 촬영시까지 동작함
  const prediction = await model.predict(webcam.canvas);

  // 촬영시 동물 변수에 값을 담음
  if (is_working === false) {
    // 남자는 강아지, 고양이, 곰, 공룡, 토끼
    if (gender.value == "male") {
      dog.value = prediction[0].probability.toFixed(2);
      cat.value = prediction[1].probability.toFixed(2);
      bear.value = prediction[2].probability.toFixed(2);
      dino.value = prediction[3].probability.toFixed(2);
      rabbit.value = prediction[4].probability.toFixed(2);
      emit(
        "workFinished",
        gender.value,
        dog.value,
        cat.value,
        bear.value,
        dino.value,
        rabbit.value
      );
    } else if (gender.value == "female") {
      // 여자는 강아지, 고양이, 꼬부기, 사슴, 토끼
      dog.value = prediction[0].probability.toFixed(2);
      cat.value = prediction[1].probability.toFixed(2);
      turtle.value = prediction[2].probability.toFixed(2);
      deer.value = prediction[3].probability.toFixed(2);
      rabbit.value = prediction[4].probability.toFixed(2);
      emit(
        "workFinished",
        gender.value,
        dog.value,
        cat.value,
        turtle.value,
        deer.value,
        rabbit.value
      );
    }
  }
};

// 촬영 버튼 클릭시 실행
const stop = function () {
  if (!videoRef.value) return;
  is_working = false;
  videoRef.value.pause();
};
</script>

<style scoped>
video {
  clear: both;
  display: block;
  transform: rotateY(180deg);
  -webkit-transform: rotateY(180deg);
  -moz-transform: rotateY(180deg);
  border-radius: 3rem;
}

section {
  opacity: 1;
  transition: opacity 500ms ease-in-out;
}

.invisible {
  opacity: 0.2;
}
</style>

<style lang="scss" scoped>
$radius: 80px;
$easeIn: cubic-bezier(0.55, 0.085, 0.68, 0.53);
$easeOut: cubic-bezier(0.165, 0.84, 0.44, 1);

.rounded {
  width: $radius;
  height: $radius;
  overflow: hidden;

  border-radius: $radius;

  &:after {
    content: "";
    width: 100%;
    height: 100%;
    position: absolute;
    border-radius: $radius;

    box-sizing: border-box;

    border: $radius * 0.1 solid rgb(196, 153, 243);

    transition: border-width 0.2s $easeOut;
  }

  &:hover {
    &:after {
      border-width: calc($radius / 2);
      transition: border-width 0.2s $easeIn;
    }

    svg {
      fill: rgb(242, 175, 239);
    }
  }

  svg {
    width: 100%;
    height: 100%;
    padding: $radius * 0.25;
    box-sizing: border-box;

    position: absolute;
    top: 50%;
    left: 50%;
    z-index: 1;

    fill: rgb(196, 153, 243);

    transform: translate3d(-50%, -50%, 0);
    transition: fill 0.2s $easeOut;
  }
}

.hide-button .rounded {
  width: $radius;
  height: $radius;
  overflow: hidden;

  border-radius: $radius;

  &:after {
    content: "";
    width: 100%;
    height: 100%;
    position: absolute;
    border-radius: $radius;

    box-sizing: border-box;

    border: $radius * 0.1 solid rgb(157, 157, 157);

    transition: border-width 0.2s $easeOut;
  }

  svg {
    width: 100%;
    height: 100%;
    padding: $radius * 0.25;
    box-sizing: border-box;

    position: absolute;
    top: 50%;
    left: 50%;
    z-index: 1;

    fill: rgb(157, 157, 157);

    transform: translate3d(-50%, -50%, 0);
    transition: fill 0.2s $easeOut;
  }
}

// 꾸미는 부분
.rounded {
  position: absolute;
  transform: translate3d(-50%, -50%, 0);
}

.guide {
  position: absolute;
  top: 0px;
}
</style>
