<template>
  <div class="canvas-size border-2">
    <canvas id="canvas" class="z-40 border-4"></canvas>
    <div>
      <span>목숨 : <i id="life"></i></span>
      <span>점수 : <i id="score"></i></span>
    </div>
  </div>
</template>

<script setup lang="ts">
import './subgame'
</script>

<style scoped>
.canvas-size{
  position: relative;
  width : 600px;
  height: 200px;
}
#canvas {
  width: 90%;
  height: 90%;
}
</style>