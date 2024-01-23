<template>
  <div>
    <section>
      <h2>Demo: Webcam continuous face landmarks detection</h2>
      <p>
        Hold your face in front of your webcam to get real-time face landmarker detection.
        Click <b>enable webcam</b> below and grant access to the webcam if prompted.
      </p>

      <div class="videoView">
        <button @click="toggleWebcam" class="mdc-button mdc-button--raised">
          <span class="mdc-button__ripple"></span>
          <span class="mdc-button__label">{{ webcamButtonLabel }}</span>
        </button>
        <div style="position: relative;">
          <video ref="webcam" style="position: absolute;" autoplay playsinline></video>
          <canvas class="output_canvas" ref="canvas" style="position: absolute; left: 0px; top: 0px;"></canvas>
        </div>
      </div>

      <div class="blend-shapes">
        <ul class="blend-shapes-list" id="video-blend-shapes"></ul>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue';
import vision from "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.3";
const { FaceLandmarker, FilesetResolver, DrawingUtils } = vision;

let faceLandmarker;
let runningMode = ref<"IMAGE" | "VIDEO">("IMAGE");
let webcamRunning = ref(false);
let videoWidth = 480;
let webcamButtonLabel = ref("ENABLE WEBCAM");

const webcamRef = ref<HTMLVideoElement | null>(null);
const canvasRef = ref<HTMLCanvasElement | null>(null);
const blendShapesRef = ref<HTMLElement | null>(null);

onMounted(async () => {
  await createFaceLandmarker();
  if (webcamRunning.value) {
    await nextTick(); // 딜레이 추가
    toggleWebcam();
  }
});

async function createFaceLandmarker() {
  const filesetResolver = await FilesetResolver.forVisionTasks(
    "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.3/wasm"
  );
  faceLandmarker = await FaceLandmarker.createFromOptions(filesetResolver, {
    baseOptions: {
      modelAssetPath: `https://storage.googleapis.com/mediapipe-models/face_landmarker/face_landmarker/float16/1/face_landmarker.task`,
      delegate: "GPU"
    },
    outputFaceBlendshapes: true,
    runningMode: runningMode.value,
    numFaces: 1
  });

  // webcamRef 초기화
  webcamRef.value = document.getElementById("webcam") as HTMLVideoElement;
  // canvasRef 초기화
  canvasRef.value = document.getElementById("canvas") as HTMLCanvasElement;
  // blendShapesRef 초기화
  blendShapesRef.value = document.getElementById("video-blend-shapes") as HTMLElement;
}

function hasGetUserMedia() {
  return !!(navigator.mediaDevices && navigator.mediaDevices.getUserMedia);
}

async function toggleWebcam() {
  if (!faceLandmarker) {
    console.log("Wait! faceLandmarker not loaded yet.");
    return;
  }

  webcamRunning.value = !webcamRunning.value;
  webcamButtonLabel.value = webcamRunning.value ? "DISABLE PREDICTIONS" : "ENABLE PREDICTIONS";

  const constraints = { video: true };

  if (webcamRunning.value) {
    await nextTick();
    try {
      const stream = await navigator.mediaDevices.getUserMedia(constraints);
      const video = webcamRef.value;
      if (video) {
        video.srcObject = stream;
        video.addEventListener("loadeddata", predictWebcam);
      }
    } catch (error) {
      console.error("Error accessing webcam:", error);
    }
  }
}

let lastVideoTime = -1;
let results: any = undefined;
const drawingUtils = new DrawingUtils(canvasRef.value?.getContext("2d"));

function predictWebcam() {
  const video = webcamRef.value;
  const canvas = canvasRef.value;

  if (!video) {
    console.error("Video element not found.");
    return;
  }

  const radio = video.videoHeight / video.videoWidth;
  video.style.width = videoWidth + "px";
  video.style.height = videoWidth * radio + "px";
  canvas.style.width = videoWidth + "px";
  canvas.style.height = videoWidth * radio + "px";
  canvas.width = video.videoWidth;
  canvas.height = video.videoHeight;

  if (runningMode.value === "IMAGE") {
    runningMode.value = "VIDEO";
    faceLandmarker.setOptions({ runningMode: runningMode.value });
  }

  let startTimeMs = performance.now();
  if (lastVideoTime !== video.currentTime) {
    lastVideoTime = video.currentTime;
    results = faceLandmarker.detectForVideo(video, startTimeMs);
  }

  if (results.faceLandmarks) {
    for (const landmarks of results.faceLandmarks) {
      drawingUtils.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_TESSELATION,
        { color: "#C0C0C070", lineWidth: 1 }
      );
      drawingUtils.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_RIGHT_EYE,
        { color: "#FF3030" }
      );
      drawingUtils.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_RIGHT_EYEBROW,
        { color: "#FF3030" }
      );
      drawingUtils.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_LEFT_EYE,
        { color: "#30FF30" }
      );
      drawingUtils.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_LEFT_EYEBROW,
        { color: "#30FF30" }
      );
      drawingUtils.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_FACE_OVAL,
        { color: "#E0E0E0" }
      );
      drawingUtils.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_LIPS,
        { color: "#E0E0E0" }
      );
      drawingUtils.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_RIGHT_IRIS,
        { color: "#FF3030" }
      );
      drawingUtils.drawConnectors(
        landmarks,
        FaceLandmarker.FACE_LANDMARKS_LEFT_IRIS,
        { color: "#30FF30" }
      );
    }
  }

  drawBlendShapes(blendShapesRef.value, results.faceBlendshapes);

  if (webcamRunning.value) {
    window.requestAnimationFrame(predictWebcam);
  }
}

function drawBlendShapes(el: HTMLElement | null, blendShapes: any[]) {
  if (!el || !blendShapes || blendShapes.length === 0) {
    return;
  }

  console.log(blendShapes[0]);

  let htmlMaker = "";
  blendShapes[0].categories.map((shape) => {
    htmlMaker += `
      <li class="blend-shapes-item">
        <span class="blend-shapes-label">${
          shape.displayName || shape.categoryName
        }</span>
        <span class="blend-shapes-value" style="width: calc(${
          +shape.score * 100
        }% - 120px)">${(+shape.score).toFixed(4)}</span>
      </li>
    `;
  });

  el.innerHTML = htmlMaker;
}
</script>

<style scoped>
/* Copyright 2023 The MediaPipe Authors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

/* Copyright 2022 The MediaPipe Authors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

@use "@material";
body {
  font-family: helvetica, arial, sans-serif;
  margin: 2em;
  color: #3d3d3d;
  --mdc-theme-primary: #007f8b;
  --mdc-theme-on-primary: #f1f3f4;
}

h1 {
  font-style: italic;
  color: #ff6f00;
  color: #007f8b;
}

h2 {
  clear: both;
}

em {
  font-weight: bold;
}

video {
  clear: both;
  display: block;
  transform: rotateY(180deg);
  -webkit-transform: rotateY(180deg);
  -moz-transform: rotateY(180deg);
  width: 100%;
  height: 100%;
  object-fit: cover
}

section {
  opacity: 1;
  transition: opacity 500ms ease-in-out;
}

header,
footer {
  clear: both;
}

.removed {
  display: none;
}

.demos_invisible {
  opacity: 0.2;
}

.note {
  font-style: italic;
  font-size: 130%;
}

.videoView,
.detectOnClick,
.blend-shapes {
  position: relative;
  float: left;
  width: 48%;
  margin: 2% 1%;
  cursor: pointer;
}

.videoView p,
.detectOnClick p {
  position: absolute;
  padding: 5px;
  background-color: #007f8b;
  color: #fff;
  border: 1px dashed rgba(255, 255, 255, 0.7);
  z-index: 2;
  font-size: 12px;
  margin: 0;
}

.highlighter {
  background: rgba(0, 255, 0, 0.25);
  border: 1px dashed #fff;
  z-index: 1;
  position: absolute;
}

canvas {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.output_canvas {
  transform: rotateY(180deg);
  -webkit-transform: rotateY(180deg);
  -moz-transform: rotateY(180deg);
}

.detectOnClick {
  z-index: 0;
}

.detectOnClick img {
  width: 100%;
}

.blend-shapes-item {
  display: flex;
  align-items: center;
  height: 20px;
}

.blend-shapes-label {
  display: flex;
  width: 120px;
  justify-content: flex-end;
  align-items: center;
  margin-right: 4px;
}

.blend-shapes-value {
  display: flex;
  height: 16px;
  align-items: center;
  background-color: #007f8b;
}
</style>
