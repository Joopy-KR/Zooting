<template>
  <div class="flex flex-col h-screen p-4">
    <!-- 카메라 -->
    <div class="grid h-40 grid-cols-4 gap-3">
      <user-video :stream-manager="publisher" 
      :cameraHeight="cameraHeight" 
      :cameraWidth="cameraWidth"
      />
      <user-video v-for="sub in subscribers" 
      :key="sub.stream.connection.connectionId" 
      :stream-manager="sub" 
      :cameraHeight="cameraHeight" 
      :cameraWidth="cameraWidth"
      />
    </div>
    <!-- 캐치마인드 게임 -->
    <div class="catch-mind">
      <canvas class="canvas" width="1000" height="450">이 브라우저는 캔버스를 지원하지 않습니다.</canvas>
      <div class="control" id="control">
      <button class="color-btn" data-color="black"></button>
      <button class="color-btn" data-color="red"></button>
      <button class="color-btn" data-color="green"></button>
      <button class="color-btn" data-color="blue"></button>
      <button @click="eraseall">지우개</button>
    </div>
  </div>
</div>
</template>

<script setup lang="ts">
import UserVideo from '@/components/video-chat/UserVideo.vue' 
import { ref, onMounted } from 'vue';

defineProps({
  session: Object,
  publisher: Object,
  subscribers: Object,
  cameraHeight: Number,
  cameraWidth: Number
  })

let canvas: HTMLCanvasElement | null = null;
let context: CanvasRenderingContext2D | null = null;
let drawingMode: any;

onMounted(() => {
  canvas = document.querySelector('.canvas');
  if (canvas) {
    context = canvas.getContext('2d');
    // 여기에 필요한 초기화 코드를 추가할 수 있습니다.

    canvas.addEventListener('mousedown', downHandler);
    canvas.addEventListener('mousemove', moveHandler);
    canvas.addEventListener('mouseup', upHandler);

    const control: any = document.getElementById("control")
    control.addEventListener('click', setColor)
  }
});

function eraseall() {
  context.clearRect(0, 0, canvas.width, canvas.height)
  context.beginPath()
}

function downHandler() {
  drawingMode = true;
}

function upHandler() {
  drawingMode = false;
}

function moveHandler(event) {
  if (!drawingMode) return;

  let x = event.offsetX;
  let y = event.offsetY;

  context?.beginPath();
  context?.arc(x, y, 4, 0, Math.PI * 2, false);
  context?.fill();
}

let colVal = 'black';

function setColor(e){
  colVal = e.target.getAttribute('data-color');
  context.fillStyle = colVal; // 펜 색상
}
</script>


<style scoped>
.camera-box {
  @apply bg-slate-400;
  /* width: 248px;  */
  height: 140px;
}

.catch-mind {
  @apply border-2 border-gray-300 border-solid flex-grow;
}



/* 여기부터 그림판 */

  .color-btn {
    width: 30px;
    height: 30px;
    border : none;
    border-radius: 50%;
  }
  .color-btn[data-color="black"]{
    background: black;
  }
  .color-btn[data-color="red"]{
    background: red;
  }
  .color-btn[data-color="green"]{
    background: green;
  }
  .color-btn[data-color="blue"]{
    background: blue;
  }
</style> 
