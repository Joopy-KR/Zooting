<template>
  <div>
    <div class="canvas-size border-2" id="game" v-if = "pressStart" >
      <span id="description" class="animate__shakeY absolute align-middle neon">Press Spacebar</span>
      <span id="record" class="neon-mini ml-3 mt-3"></span><i id="score" class="neon-mini ml-2"></i>
      <canvas id="canvas" class="z-40"></canvas>
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
  background-image: url("/src/assets/images/sub_game/background.gif");
  background-size: 600px 200px;
  position: relative;
  width : 600px;
  height: 200px;
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
.neon {
  color: #fff;
  text-shadow: 0 0 5px #8b80cc, 0 0 10px #fff, 0 0 20px #ff0080, 0 0 30px #ff0080, 0 0 40px #ff0080, 0 0 55px #ff0080, 0 0 75px #ff0080;
}
.neon-mini {
  color: #fff;
  text-shadow: 0 0 1px #8b80cc, 0 0 2px #fff, 0 0 4px #ff0080, 0 0 10px #ff0080, 0 0 20px #ff0080, 0 0 30px #ff0080, 0 0 50px #ff0080;}
</style>