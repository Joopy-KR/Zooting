<template>
  <div class="recent__container">
    <div class="item__container">
      recent list item
    </div>
    <div class="item__container">
      recent list item
    </div>
    <div class="item__container">
      recent list item
    </div>
    <div class="item__container">
      recent list item
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.recent__container {
  @apply flex-grow grid grid-cols-2 grid-rows-2;
  background-color: darkorange;
  max-height: 250px;
  min-height: 250px;
}
.recent__container::-webkit-scrollbar {
  width: 7px;
  background-color: white;
}
.recent__container::-webkit-scrollbar-thumb {
  background-color: #d6d6d6;
  border-radius: 4px;
}
.recent__container::-webkit-scrollbar-track {
  background-color: transparent;
}
.item__container {
  background-color:darkgreen;
  height: 70px;
}
</style>