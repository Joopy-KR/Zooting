<template>
  <div>
    <div class="canvas-size border-2" id="game" v-if = "pressStart" >
      <span id="description" class="shakeY absolute align-middle"> Press Spacebar</span>
      <span id="record"></span><i id="score"></i>
      <canvas id="canvas" class="z-40"></canvas>
    </div>
  </div>
</template>

<script setup lang="ts">
import './subgame'
import {ref} from "vue";
const pressStart = ref(true);

</script>

<style scoped>
.canvas-size{
  background-image: url("/src/assets/images/sub_game/background.gif");
  background-size: 600px 200px;
  position: relative;
  width : 600px;
  height: 200px;
}
#canvas {
  width: 90%;
  height: 90%;
}
#description {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 40px;
  font-weight: bolder;
  color: #eeb9f8;
}

#record {
  font-size: 20px;
  font-weight: bolder;
  color: #eeb9f8;
}
#score {
  font-size: 20px;
  font-weight: bolder;
  color: #eeb9f8;
}
</style>