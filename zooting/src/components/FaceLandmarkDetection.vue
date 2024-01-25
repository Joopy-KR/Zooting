<template>
  <div class="flex flex-col items-center justify-center w-screen h-screen" style="min-width: 800px;">
    <div v-show="is_loaded" style="position: relative;">
      <video id="webcam" autoplay playsinline></video>
      <canvas class="output_canvas" id="canvas"></canvas>
      <!-- <img src="src/assets/animal_mask/dog_face.png" alt=""> -->
    </div>
    
    <div class="flex flex-col items-center justify-center" v-show="!is_loaded">
      <p class="mb-8 text-5xl font-bold">카메라 준비 중</p>
      <h3 class="mb-5">잠시만 기다려 주세요</h3>
      <div class="flex items-center justify-center" role="status">
        <svg aria-hidden="true" class="inline w-3/12 text-gray-200 animate-spin dark:text-gray-600 fill-pink-400" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"/>
          <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"/>
        </svg>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// Copyright 2023 The MediaPipe Authors.

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//      http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


import { ref, onMounted, nextTick } from 'vue'
import vision from "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.3"
const { FaceLandmarker, FilesetResolver, DrawingUtils } = vision


let faceLandmarker: any
let webcamRunning = ref(false)
let videoWidth = 800

// 카메라 로딩이 끝난 후 실행할 함수
const is_loaded = ref(false)

// ref 함수 사용 (값은 이후에 담음)
const webcamRef = ref<HTMLVideoElement | null>(null)
const canvasRef = ref<HTMLCanvasElement | null>(null)
const drawingUtils = ref<any>(null)

onMounted(async () => {
  await createFaceLandmarker()
})


async function createFaceLandmarker() {
  const filesetResolver = await FilesetResolver.forVisionTasks(
    "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.3/wasm"
    );
    faceLandmarker = await FaceLandmarker.createFromOptions(filesetResolver, {
      baseOptions: {
        modelAssetPath: 'src/assets/face_landmark_detection/face_landmarker.task',
        delegate: "GPU"
      },
      outputFaceBlendshapes: true,
      runningMode: "VIDEO",
      numFaces: 3
    })
    

    // webcamRef, canvasRef, deawingUtils를 설정
    webcamRef.value = document.getElementById("webcam") as HTMLVideoElement
    canvasRef.value = document.getElementById("canvas") as HTMLCanvasElement
    drawingUtils.value = new DrawingUtils(canvasRef.value?.getContext("2d"))


    // 다음 작업으로 카메라 실행
    enableWebcam()
}

// 카메라 실행 함수
async function enableWebcam() {
  if (!faceLandmarker) {
    console.log("얼굴 인식 시스템이 로딩중입니다. 다시 시도해주세요");
    return
  }
  
  webcamRunning.value = true
  
  const constraints = { video: true }
  
  try {
    const stream = await navigator.mediaDevices.getUserMedia(constraints)
    const video = webcamRef.value
    if (video) {
      video.srcObject = stream;
      video.addEventListener("loadeddata", predictWebcam);
    }
  } catch (error) {
    console.error("Error accessing webcam:", error);
  }
}

let lastVideoTime = -1;
let results: any = undefined;
let imgX = 0;
let imgY = 0;
let imgZ = 0

const img = new Image()
img.src ="src/assets/animal_mask/dog_face.png"

const predictWebcam = async () => {
  const video: any = webcamRef.value;
  const canvas: any = canvasRef.value;
  
  const radio = video.videoHeight / video.videoWidth;
  video.style.width = videoWidth + "px";
  video.style.height = videoWidth * radio + "px";
  canvas.style.width = videoWidth + "px";
  canvas.style.height = videoWidth * radio + "px";
  canvas.width = video.videoWidth;
  canvas.height = video.videoHeight;
  
  const ctx: any = canvasRef.value?.getContext("2d")
  

  // detectForVideo 함수를 사용하여 얼굴 추적
  let startTimeMs = performance.now();
  if (lastVideoTime !== video.currentTime) {
    lastVideoTime = video.currentTime;
    results = faceLandmarker.detectForVideo(video, startTimeMs);
  }
  
  is_loaded.value = false
  

  // 얼굴에 마스크 그리는 부분
  if (results.faceLandmarks) {
    for (const landmarks of results.faceLandmarks) {
      is_loaded.value = true
      
      // 이미지 좌표를 얼굴의 특정 랜드마크에서 가져오도록 수정
      imgX = landmarks[FaceLandmarker.FACE_LANDMARKS_RIGHT_IRIS[0].start].x * 300;
      imgY = landmarks[FaceLandmarker.FACE_LANDMARKS_RIGHT_IRIS[0].start].y * 150;

      console.log(imgX)
      
      ctx.clearRect(0, 0, canvas.width, canvas.height)
      ctx.drawImage(img, imgX, imgY, 350, 350)


      /**  그리드 그리는 부분
      drawingUtils.value.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_TESSELATION,ㄴ
        { color: "#C0C0C070", lineWidth: 1 }
      )
      drawingUtils.value.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_RIGHT_EYE,
        { color: "#FF3030", lineWidth: 1 }
      )
      drawingUtils.value.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_RIGHT_EYEBROW,
        { color: "#FF3030", lineWidth: 1 }
      )
      drawingUtils.value.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_LEFT_EYE,
        { color: "#30FF30", lineWidth: 1 }
      )
      drawingUtils.value.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_LEFT_EYEBROW,
        { color: "#30FF30", lineWidth: 1 }
      )
      drawingUtils.value.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_FACE_OVAL,
        { color: "#E0E0E0", lineWidth: 1 }
      )
      drawingUtils.value.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_LIPS,
        { color: "#E0E0E0", lineWidth: 1 }
      )
      drawingUtils.value.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_RIGHT_IRIS,
        { color: "#FF3030", lineWidth: 1 }
      )
      drawingUtils.value.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_LEFT_IRIS,
        { color: "#30FF30", lineWidth: 1 }
      )
    */
    }
  }

  if (webcamRunning.value) {
    window.requestAnimationFrame(predictWebcam);
  }
}
</script>

<style scoped>
video {
  clear: both;
  display: block;
  transform: rotateY(180deg);
  -webkit-transform: rotateY(180deg);
  -moz-transform: rotateY(180deg);
  object-fit: cover;
}

canvas {
  position: absolute;
  left: 0;
  top: 0;
  object-fit: cover;
}

.output_canvas {
  transform: rotateY(180deg);
  -webkit-transform: rotateY(180deg);
  -moz-transform: rotateY(180deg);
}

</style>
