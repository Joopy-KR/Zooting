<template>
  <div>
    <div class="border-2 canvas-size background-image" id="game" v-if = "pressStart" >
      <span id="description" class="absolute align-middle animate__shakeY neon drag-prevent">Press Spacebar</span>
      <span id="record" class="mt-3 ml-3 neon-mini drag-prevent"></span><i id="score" class="ml-2 neon-mini drag-prevent"></i>
      <canvas id="canvas" class="z-40 drag-prevent"></canvas>
    </div>
  </div>
</template>

<script setup lang="ts">
import './subgame'
import {ref} from "vue";
import 'animate.css'
const pressStart = ref(true);

</script>

<style scoped>
.canvas-size{
  position: relative;
  width : 600px;
  height: 200px;
}
.background-image {
  background: url("/assets/images/sub_game/background.gif") 0 center / 200px 600px repeat-x;
  background-size: 600px 200px;
  animation: movebg 40s linear infinite;

}
@keyframes movebg {
  0% {background-position: 600px center;}
  100% {background-position: 0 center;}
}
#canvas {
  width: 100%;
  height: 90%;
}
#description {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 40px;
  font-weight: bolder;
}

#record {
  font-size: 20px;
  font-weight: bolder;
}
#score {
  font-size: 20px;
  font-weight: bolder;
}
.drag-prevent{
  -ms-user-select: none;
  -moz-user-select: -moz-none;
  -webkit-user-select: none;
  -khtml-user-select: none;
  user-select: none;
}
.neon {
  color: #fff;
  text-shadow: 0 0 5px #8b80cc, 0 0 10px #fff, 0 0 20px #ff0080, 0 0 30px #ff0080, 0 0 40px #ff0080, 0 0 55px #ff0080, 0 0 75px #ff0080;
}
.neon-mini {
  color: #fff;
  text-shadow: 0 0 1px #8b80cc, 0 0 2px #fff, 0 0 4px #ff0080, 0 0 10px #ff0080, 0 0 20px #ff0080, 0 0 30px #ff0080, 0 0 50px #ff0080;}
</style>