<template>
  <div class="flex flex-col h-screen p-4">
    <!-- 카메라 -->
    <div class="grid h-40 grid-cols-4 gap-3">
      <user-video :stream-manager="publisher" 
      :cameraHeight="cameraHeight" 
      :cameraWidth="cameraWidth"
      @send-canvas="sendCanvas"
      />
      <user-video v-for="sub in subscribers" 
      :key="sub.stream.connection.connectionId" 
      :stream-manager="sub" 
      :cameraHeight="cameraHeight" 
      :cameraWidth="cameraWidth"
      @send-canvas="sendCanvas"
      />
    </div>
    <!-- 캐치마인드 게임 -->
    <div class="catch-mind">
      <ul class="colours">
        <li v-for="(color, index) in colors" :key="index" :style="{ backgroundColor: color }" @click="setActiveColour(index)"></li>
      </ul>
      <button class="pencil__refresh btn" @click="clearCanvas">Clear</button>
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

const emit = defineEmits(['sendCanvas'])

const sendCanvas = function(canvas) {
  emit('sendCanvas', canvas)
}

const colors = [
  "#100c08",
  "#759BA9",
  "#77dd77",
  "#ff6961",
  "#ffd1dc"
]

let currentColorIndex = ref(0);
let drawingCanvas = ref(null);
let drawingCtx = ref(null);
let isDrawing = ref(false);
let newlyUp = ref(false);
let lastPoint = { x: null, y: null };

onMounted(() => {
  drawingCanvas.value = document.createElement('canvas');
  drawingCtx.value = drawingCanvas.value.getContext('2d');
  
  drawingCanvas.value.width = window.innerWidth;
  drawingCanvas.value.height = window.innerHeight;
  drawingCanvas.value.style.position = 'fixed';
  drawingCanvas.value.style.left = '0';
  drawingCanvas.value.style.top = '0';
  drawingCanvas.value.style.zIndex = '1';
  
  document.body.appendChild(drawingCanvas.value);
  
  document.addEventListener('mousedown', handleMouseDown);
  document.addEventListener('mouseup', handleMouseUp);
  document.addEventListener('mousemove', handleMouseMove);
  document.addEventListener('touchstart', handleMouseDown, { passive: false });
  document.addEventListener('touchend', handleMouseUp, { passive: false });
  document.addEventListener('touchmove', handleMouseMove, { passive: false });
});

const setActiveColour = (index) => {
  currentColorIndex.value = index;
};

const clearCanvas = () => {
  drawingCtx.value.clearRect(0, 0, drawingCanvas.value.width, drawingCanvas.value.height);
};

const handleMouseDown = (e) => {
  isDrawing.value = true;
};

const handleMouseUp = () => {
  isDrawing.value = false;
  newlyUp.value = true;
  setTimeout(() => (newlyUp.value = false), 50);
};

const handleMouseMove = (e) => {
  if (!isDrawing.value) return;
  let clientX = e.clientX;
  let clientY = e.clientY;

  if (e.touches) {
    clientX = e.touches[0].clientX;
    clientY = e.touches[0].clientY;
  }

  if (lastPoint.x == null || lastPoint.y == null) {
    lastPoint = { x: clientX, y: clientY };
    return;
  }

  drawingCtx.value.beginPath();
  drawingCtx.value.strokeStyle = colors[currentColorIndex.value];
  drawingCtx.value.moveTo(lastPoint.x, lastPoint.y);
  drawingCtx.value.lineTo(clientX, clientY);
  drawingCtx.value.stroke();

  lastPoint = { x: clientX, y: clientY };
};

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
</style> 

<!-- SCSS는 그림판 코드 -->
<style lang="scss" scoped>
#mfPreviewBar {
	display: none !important;
}

html,
body {
  position: fixed;
  overflow: hidden;
	touch-action: none;
}

body {
	background-color: #f7f4f0;
	cursor: url('https://s3-us-west-2.amazonaws.com/s.cdpn.io/605067/cursor.png'), auto;
	font-family: 'Galano Grotesque Semi Bold';
	margin: 0;
	overflow: hidden;
}

button {
	font-family: 'Galano Grotesque Semi Bold';
}

.colours {
	bottom: 30px;
	display: none;
	left: 50%;
	list-style-type: none;
	padding-left: 0;
	position: absolute;
	transform: translateX(-50%);
	z-index: 10;
	
	@media (min-width: 1024px) {
		display: flex;
	}
	
	li {
		border-radius: 50%;
		display: inline-block;
		height: 14px;
		margin: 0 12px;
		width: 14px;
		
		&:nth-child(1){
			background-color: #100c08;
		}
		&:nth-child(2){
			background-color: #759BA9;
		}
		&:nth-child(3){
			background-color: #77dd77;
		}
		&:nth-child(4){
			background-color: #ff6961;
		}
		&:nth-child(5){
			background-color: #ffd1dc;
		}
	}
}

.pencil__refresh {
	background-color: #FCF4EA;
	border: none;
	border-radius: 50%;
	bottom: 18px;
	cursor: pointer;
	height: 26px;
	padding: 4px 1px 0px;
	position: absolute;
	left: 50%;
	text-align: center;
	transform: translateX(-50%);
	width: auto;
	z-index: 3;
				
	@media (min-width: 1024px) {
		bottom: 27px;
		left: 30px;
		transform: none;
	}
}

.pencil__submit {
	bottom: 27px;
	display: none;
	position: absolute;
	right: 30px;
	z-index: 4;
				
	@media (min-width: 1024px) {
	 display: block;
	}
}

.btn {
	background-color: transparent;
	border: none;
	cursor: pointer;
	font-size: 13px;
	letter-spacing: .05em;
	outline: none;
	padding: 5px 10px 1px 10px;
	text-transform: uppercase;
	user-select: none;
	-moz-user-select: none;
}
</style>
