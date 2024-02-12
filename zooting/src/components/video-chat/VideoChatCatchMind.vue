<template>
  <div class="flex flex-col h-screen p-4">
    <!-- 카메라 -->
    <div class="grid h-40 grid-cols-4 gap-3">
      <user-video :stream-manager="publisher" />
      <user-video v-for="sub in subscribers" 
      :key="sub.stream.connection.connectionId" 
      :stream-manager="sub" 
      />
    </div>
    <!-- 캐치마인드 게임 -->
		<div class="catchMind--body">
			<canvas id="jsCanvas" class="canvas"></canvas>
			<div class="controls">
				<div class="flex flex-col controls__colors" id="jsColors">
						<div class="controls__color" id="change-black" style="background-color: #2c2c2c;"></div>
						<div class="controls__color" id="change-red" style="background-color: #FF3B30;"></div>
						<div class="controls__color" id="change-orange" style="background-color: #FF9500;"></div>
						<div class="controls__color" id="change-green" style="background-color: #4CD963;"></div>
						<div class="controls__color" id="change-blue" style="background-color: #0579FF;"></div>
				</div>
				<div class="controls__btns">
					<button id="all-clear">전체 지우기</button>
				</div>
			</div>
		</div>
  </div>
</template>

<script setup lang="ts">
import UserVideo from '@/components/video-chat/UserVideo.vue'
import { ref, onMounted, onUnmounted, watch } from 'vue';

const props = defineProps({
	session: Object,
  publisher: Object,
  subscribers: Object,
	drawData: Object,
})

let painting = false;

onMounted(() => {
  const canvas: any = document.getElementById("jsCanvas");
  if (canvas) {
    const ctx = canvas.getContext("2d");
    canvas.width = 1000;
    canvas.height = 700;
    ctx.strokeStyle = "#2c2c2c";
    ctx.lineWidth = 1.5;

    function startPainting() {
        painting = true;
    }

    function stopPainting() {
        painting = false;
    }

    function onMouseMove(event: any) {
        const x = event.offsetX;
        const y = event.offsetY;
				const data = { isPainting: painting, x: event.offsetX, y: event.offsetY, color: ctx.strokeStyle, lineWidth: ctx.lineWidth }
        if(!painting) {
            ctx.beginPath();
            ctx.moveTo(x, y);
						sendDrawing(data)
        } else {
            ctx.lineTo(x, y);
            ctx.stroke();
						sendDrawing(data)
        }
    }

		// 그리기 송신
		function sendDrawing(data) {
			props.session?.signal({
				data: JSON.stringify(data), // 그리기 이벤트 데이터
				to: [],
				type: 'drawing', // 이벤트 타입 지정
			})
			.catch(error => console.error(error));
		}

    canvas.addEventListener("mousemove", onMouseMove);
    canvas.addEventListener("mousedown", startPainting);
    canvas.addEventListener("mouseup", stopPainting);
    canvas.addEventListener("mouseleave", stopPainting);

		const changeBlack = document.getElementById("change-black")
		changeBlack?.addEventListener("click", () => ctx.strokeStyle = "#2c2c2c")
		const changeRed = document.getElementById("change-red")
		changeRed?.addEventListener("click", () => ctx.strokeStyle = "#FF3B30")
		const changeOrange = document.getElementById("change-orange")
		changeOrange?.addEventListener("click", () => ctx.strokeStyle = "#FF9500")
		const changeGreen = document.getElementById("change-green")
		changeGreen?.addEventListener("click", () => ctx.strokeStyle = "#4CD963")
		const changeBlue = document.getElementById("change-blue")
		changeBlue?.addEventListener("click", () => ctx.strokeStyle = "#0579FF")

		const allClear = document.getElementById("all-clear")
		allClear?.addEventListener("click", () => ctx.clearRect(0, 0, canvas.width, canvas.height))
  }
})

onUnmounted(() => {
  const canvas: any = document.getElementById("jsCanvas")
	canvas.remove()
})

watch(() => props.drawData, (newVal, oldVal) => {
  if (newVal) {
    // 캔버스에 그림을 그리는 로직...
    const canvas = document.getElementById("jsCanvas");
    if (canvas) {
      const ctx = canvas.getContext("2d");
			ctx.strokeStyle = newVal.color;
			ctx.lineWidth = newVal.lineWidth;
      if (!newVal.isPainting) {
				ctx.beginPath()
				ctx.moveTo(newVal.x, newVal.y)
				console.log(11)
			} else {
				ctx.lineTo(newVal.x, newVal.y); // 선 그리기
				ctx.stroke(); // 선 완성
				console.log(22)
			}
    }
  }
}, { deep: true });

</script>


<style scoped>
.catchMind--body {
  background-color: #f6f9fc;
  display: flex;
	justify-content: center;
  align-items: center;
  padding-top: 50px;
}

.canvas {
  width: 1000px;
  height: 700px;
  background-color: white;
  border-radius: 30px;
  box-shadow: 0 4px 6px rgba(50, 50, 93, 0.11), 0 1px 3px rgba(0, 0, 0, 0.08);
}

.controls {
  margin-top: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.controls .controls__colors {
  display: flex;
}

.controls__colors .controls__color {
  width: 50px;
  height: 50px;
  border-radius: 25px;
  box-shadow: 0 4px 6px rgba(50, 50, 93, 0.11), 0 1px 3px rgba(0, 0, 0, 0.08);
  cursor: pointer;
	margin-bottom: 10px;
	margin-left: 10px;
}

.controls .controls__btns {
  margin-bottom: 30px;
	margin-left: 10px;
}

.controls__btns button {
  all: unset;
  cursor: pointer;
  background-color: white;
  padding: 5px 0;
  width: 80px;
  text-align: center;
  border-radius: 5px;
  border: 2px solid rgba(0, 0, 0, 0.2);
  color: rgba(0, 0, 0, 0.4);
  text-transform: uppercase;
  font-weight: 800;
  font-size: 12px;
  box-shadow: 0 4px 6px rgba(50, 50, 93, 0.11), 0 1px 3px rgba(0, 0, 0, 0.08);
}

.controls__btns button:active {
  transform: scale(0.97);
}

.controls .controls__range {
  margin-bottom: 30px;
}
</style> 
