<template>
  <div class="main-div">
    <div>
      <div id="webcam-container" style="position: relative;">
        <video id="webcam" ref="videoRef" autoplay playsinline></video>
        <img src="@/assets/tempGuide.png" alt="" class="guide" v-show="is_started">
      </div>
      <a href="javascript:void(0)" class="rounded" @click="stop" v-show="is_started && showButton">
        <svg version="1.1" x="0px" y="0px" width="512px" height="512px" viewBox="0 0 512 512" enable-background="new 0 0 512 512" xml:space="preserve"><path id="instagram-icon" d="M341.205,197.143H460.93v183.795c0,44.77-36.292,81.062-81.062,81.062H132.133c-44.769,0-81.062-36.293-81.062-81.062V197.143H170.26c-12.2,17.141-19.399,38.087-19.399,60.729c0,57.919,46.953,104.872,104.873,104.872c57.919,0,104.872-46.953,104.872-104.872C360.605,235.229,353.405,214.283,341.205,197.143z M460.93,131.062v53.239H330.447c-19.022-19.315-45.465-31.302-74.714-31.302c-29.251,0-55.693,11.986-74.714,31.302H51.07v-53.239c0-27.322,13.532-51.469,34.245-66.154v87.289h16.62v-96.36c3.77-1.516,7.679-2.752,11.704-3.691v100.052h16.621V50.047c0.625-0.015,1.245-0.047,1.873-0.047h9.598v102.196h16.62V50h221.517C424.638,50,460.93,86.292,460.93,131.062z M423.879,96.897c0-7.181-5.822-13.002-13.003-13.002h-43.821c-7.183,0-13.003,5.821-13.003,13.002v44.785c0,7.181,5.82,13.002,13.003,13.002h43.821c7.181,0,13.003-5.821,13.003-13.002V96.897z M174.938,257.872c0-24.188,10.698-45.909,27.593-60.729c5.926-5.197,12.613-9.539,19.875-12.842c10.169-4.625,21.447-7.224,33.327-7.224c11.881,0,23.157,2.599,33.326,7.224c7.263,3.303,13.95,7.645,19.876,12.842c16.895,14.82,27.592,36.542,27.592,60.729c0,44.55-36.243,80.794-80.794,80.794C211.183,338.666,174.938,302.422,174.938,257.872z M196.286,257.872c0,32.979,26.735,59.712,59.714,59.712c32.979,0,59.713-26.733,59.713-59.712c0-32.98-26.733-59.713-59.713-59.713C223.021,198.159,196.286,224.892,196.286,257.872z"/></svg>
      </a>
      <p v-show="!showButton">가이드라인에 얼굴을 맞춰주세요</p>
      <p v-show="is_started">사진은 저장되지 않습니다</p>
    </div>
  </div>
</template>


<script setup type="text/javascript" lang="ts">

import { ref, onMounted, nextTick, defineEmits } from 'vue'
import { FaceDetector, FilesetResolver } from "@mediapipe/tasks-vision"

const emit = defineEmits(['workFinished']);

// 얼굴인식 변수
let faceDetector: FaceDetector
let runningMode: string = "IMAGE"
const videoRef = ref<any>(null)

const is_started = ref(false)
let is_working = true  // 촬영버튼 시 측정 동작을 멈추는 변수

const showButton = ref(true)  // 유사도가 90 이상일때 촬영버튼 출력

onMounted(() => {
  initializeFaceDetector()
  init()
})


// 얼굴 인식 모델
// 얼굴 인식기 초기화
const initializeFaceDetector = async () => {
  await nextTick()

  const vision = await FilesetResolver.forVisionTasks(
    "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.0/wasm"
    )
    
    faceDetector = await FaceDetector.createFromOptions(vision, {
      baseOptions: {
        modelAssetPath:
        "https://storage.googleapis.com/mediapipe-models/face_detector/blaze_face_short_range/float16/1/blaze_face_short_range.tflite",
        delegate: "GPU"
      },
      runningMode: runningMode
    })
    
    enableCam()
  }


// 웹캠 실행 및 얼굴인식 시작(predictWebcam 함수)
const enableCam = async () => {

  if (!faceDetector) {
    alert("얼굴 인식 시스템이 로딩중입니다. 다시 시도해주세요")
    return;
  }

  is_started.value = false

  const constraints = {
    video: true
  };
  
  try {
    const stream = await navigator.mediaDevices.getUserMedia(constraints)
    videoRef.value.srcObject = stream
    videoRef.value.addEventListener("loadeddata", predictWebcam)
  } catch (err) {
    console.error(err)
  }
}


let lastVideoTime = -1;
const predictWebcam = async () => {
  if (runningMode === "IMAGE") {
    runningMode = "VIDEO";
    await faceDetector.setOptions({ runningMode: "VIDEO" })
  }

  let startTimeMs = performance.now()

  // detectForVideo 함수를 사용하여 얼굴 추적
  if (videoRef.value.currentTime !== lastVideoTime) {
    lastVideoTime = videoRef.value.currentTime
    const detections = faceDetector.detectForVideo(
      videoRef.value,
      startTimeMs
    ).detections
    displayVideoDetections(detections)
  }

  window.requestAnimationFrame(predictWebcam)
}

// 대기 함수
function wait(sec: any) {
    return new Promise(resolve => setTimeout(resolve, sec * 1000));
}

const isPlaying = ref(false)

const displayVideoDetections = async (detections: Detection[]) => {
  isPlaying.value = false
  await wait(0.5)
  // 예상치 측정
  for (let detection of detections) {
    isPlaying.value = true
    // 유사도가 90 이상이면 촬영 버튼 출력
    if (Math.round(parseFloat(detection.categories[0].score) * 100) >= 89) {
      // 유사도가 90 이상일때 촬영버튼 출력
      showButton.value = true
    } else {
      // 유사도가 90 미만일때 촬영버튼 숨김
      showButton.value = false
    } 
  }
  if (isPlaying.value === false) {
    showButton.value = false
  }
}
  

// 동물상 분석 모델
let model, webcam

// 강아지 퍼센트
const dog = ref(0)
const cat = ref(0)

// 모델 경로
const URL = "src/assets/animal_test_model"


// 모델 로딩 후 웹캠 출력
const init = async () => {
  await initializeAnimalModel()
  webcam = await setupWebcam()
  await webcam.play()
  is_started.value = true
  window.requestAnimationFrame(loop)
  }
  
  const initializeAnimalModel = async () => {
    const modelURL = URL + "/model.json"
    const metadataURL = URL + "/metadata.json"
    
    // 모델 및 메타데이터 로드
    // "tmImage" 객체는 라이브러리로부터 추가되어있는 상태임 (window.tmImage)
    model = await tmImage.load(modelURL, metadataURL)
  }

  const setupWebcam = async () => {
  const flip = true;
  const webcam = new tmImage.Webcam(500, 500, flip);

  try {
    await webcam.setup();
  } catch (err) {
    if (err === "Could not open your camera. You may have denied access.") {
      window.alert("카메라가 차단되어있어요. 브라우저 설정을 확인해주세요");
    }
  }

  return webcam;
}

const loop = async () => {
  webcam.update()  // 웹캠 프레임 업데이트 후 stop 될때까지 무한루프
  await predict()
  if (is_working) {
    window.requestAnimationFrame(loop)
  }
}

// 이미지 모델을 기반으로 웹캠 실행
const predict = async () => {
  // 촬영시까지 동작함
  const prediction = await model.predict(webcam.canvas)

  // 촬영시 동물 변수에 값을 담음
  if (is_working === false) {
      dog.value = prediction[0].probability.toFixed(2)
      cat.value = prediction[1].probability.toFixed(2)

      console.log("강아지: " + dog.value)
      console.log("고양이: " + cat.value)

      emit('workFinished', dog.value, cat.value)
  }
}

// 촬영 버튼 클릭시 실행
const stop = function() {
  is_working = false
  videoRef.value.pause()
}

</script>


<style scoped>

.main-div {
  @apply flex items-center justify-center
}

video {
  clear: both;
  display: block;
  transform: rotateY(180deg);
  -webkit-transform: rotateY(180deg);
  -moz-transform: rotateY(180deg);
  height: 60vh;
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
    content: '';
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
    
    transform: translate3d(-50%,-50%,0);
    transition: fill 0.2s $easeOut;
  }
}

// 꾸미는 부분
body {
  background: #1E88E5;
}
.rounded {
  position: absolute;
  margin-left: 250px;
  margin-top: 60px;
  transform: translate3d(-50%,-50%,0);
}

.guide {
  position: absolute;
  top: 0px;
}
</style>