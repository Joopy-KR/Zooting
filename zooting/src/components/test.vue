<template>
  <div>
    <section id="demos" class="test_invisible">
      <h2>Demo: Webcam continuous face landmarks detection</h2>
      <p>
        Hold your face in front of your webcam to get real-time face landmarker detection.
        Click <b>enable webcam</b> below and grant access to the webcam if prompted.
      </p>

      <div id="liveView" class="videoView">
        <button @click="enableCam" id="webcamButton" class="mdc-button mdc-button--raised">
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
import { ref, onMounted } from "vue";
import vision from "@mediapipe/tasks-vision@0.10.3";
const { FaceLandmarker, FilesetResolver, DrawingUtils } = vision;

const demosSection = document.getElementById("demos");
const videoBlendShapes = document.getElementById("video-blend-shapes");

let faceLandmarker;
let runningMode: "IMAGE" | "VIDEO" = "IMAGE";
let webcamRunning: Boolean = false;
const videoWidth = 480;

const webcamButtonLabel = ref("ENABLE WEBCAM");

// Before we can use HandLandmarker class we must wait for it to finish
// loading. Machine Learning models can be large and take a moment to
// get everything needed to run.
const createFaceLandmarker = async () => {
  const filesetResolver = await FilesetResolver.forVisionTasks(
    "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.3/wasm"
  );
  faceLandmarker = await FaceLandmarker.createFromOptions(filesetResolver, {
    baseOptions: {
      modelAssetPath: `https://storage.googleapis.com/mediapipe-models/face_landmarker/face_landmarker/float16/1/face_landmarker.task`,
      delegate: "GPU"
    },
    outputFaceBlendshapes: true,
    runningMode,
    numFaces: 1
  });
  demosSection.classList.remove("test_invisible");
};
onMounted(createFaceLandmarker);

const video = ref(null);
const canvas = ref(null);
const drawingUtils = new DrawingUtils();

const enableCam = () => {
  if (!faceLandmarker) {
    console.log("Wait! faceLandmarker not loaded yet.");
    return;
  }

  if (webcamRunning) {
    webcamRunning = false;
    webcamButtonLabel.value = "ENABLE WEBCAM";
  } else {
    webcamRunning = true;
    webcamButtonLabel.value = "DISABLE WEBCAM";
  }

  // getUsermedia parameters.
  const constraints = {
    video: true
  };

  // Activate the webcam stream.
  navigator.mediaDevices.getUserMedia(constraints).then((stream) => {
    video.value.srcObject = stream;
    video.value.addEventListener("loadeddata", predictWebcam);
  });
};

let lastVideoTime = -1;
let results = undefined;

const predictWebcam = () => {
  const radio = video.value.videoHeight / video.value.videoWidth;
  video.value.style.width = videoWidth + "px";
  video.value.style.height = videoWidth * radio + "px";
  canvas.value.style.width = videoWidth + "px";
  canvas.value.style.height = videoWidth * radio + "px";
  canvas.value.width = video.value.videoWidth;
  canvas.value.height = video.value.videoHeight;

  // Now let's start detecting the stream.
  if (runningMode === "IMAGE") {
    runningMode = "VIDEO";
    faceLandmarker.setOptions({ runningMode: runningMode });
  }

  let startTimeMs = performance.now();
  if (lastVideoTime !== video.value.currentTime) {
    lastVideoTime = video.value.currentTime;
    results = faceLandmarker.detectForVideo(video.value, startTimeMs);
  }

  if (results && results.faceLandmarks) {
    drawingUtils.drawConnectors(
      results.faceLandmarks,
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

  drawBlendShapes(videoBlendShapes, results.faceBlendshapes);

  // Call this function again to keep predicting when the browser is ready.
  if (webcamRunning) {
    window.requestAnimationFrame(predictWebcam);
  }


const drawBlendShapes = (el: HTMLElement, blendShapes: any[]) => {
  if (!blendShapes || blendShapes.length === 0) {
    return;
  }

  console.log(blendShapes[0]);

  let htmlMaker = "";
  blendShapes[0].categories.map((shape) => {
    htmlMaker += `
      <li class="blend-shapes-item">
        <span class="blend-shapes-label">${shape.displayName || shape.categoryName}</span>
        <span class="blend-shapes-value" style="width: calc(${+shape.score * 100}% - 120px)">
          ${(+shape.score).toFixed(4)}
        </span>
      </li>
    `;
  });

  el.innerHTML = htmlMaker;
};
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

.test_invisible {
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

.canvas {
  z-index: 1;
  position: absolute;
  pointer-events: none;
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