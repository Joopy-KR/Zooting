<template>
  <div class="match__container">

    <div class="match__page">
      <!-- <img src="/images/heart-matching.png" class="animate__bounceIn" alt=""> -->
    </div>

    <div class="match__ready">
      <button class="learn-more">Ready</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import 'animate.css'
</script>


<style scoped>
.match__container {
  @apply flex flex-col items-center col-span-5 py-5;
}
.match__page {
  @apply flex flex-grow justify-center;
}
/* .match__page img {
  @apply cursor-pointer h-auto object-contain;
  width: 350px
} */
.match__ready {
  @apply h-32 flex items-center;
}
</style>

<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css?family=Rubik:700&display=swap');

$bg: #fff;
$text: #382b22;
$light-pink: #fff0f0;
$pink: #ffe9e9;
$dark-pink: #f9c4d2;
$pink-border: #b18597;
$pink-shadow: #ffe3e2;

* {
  box-sizing: border-box;
  &::before, &::after {
    box-sizing: border-box;
  }
}

body {
  font-family: 'Rubik', sans-serif;
  font-size: 1rem;
  line-height: 1.5;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0;
  min-height: 100vh;
  background: $bg;
}

button {
  position: relative;
  display: inline-block;
  cursor: pointer;
  outline: none;
  border: 0;
  vertical-align: middle;
  text-decoration: none;
  font-size: inherit;
  font-family: inherit;
  width: 300px;
  height: 80px;
  &.learn-more {
    font-weight: 600;
    color: $text;
    text-transform: uppercase;
    padding: 1.25em 2em;
    background: $light-pink;
    border: 2px solid $pink-border;
    border-radius: 0.75em;
    transform-style: preserve-3d;
    transition: transform 150ms cubic-bezier(0, 0, 0.58, 1), background 150ms cubic-bezier(0, 0, 0.58, 1);
    &::before {
      position: absolute;
      content: '';
      width: 100%;
      height: 100%;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: $dark-pink;
      border-radius: inherit;
      box-shadow: 0 0 0 2px $pink-border, 0 0.625em 0 0 $pink-shadow;
      transform: translate3d(0, 0.75em, -1em);
      transition: transform 150ms cubic-bezier(0, 0, 0.58, 1), box-shadow 150ms cubic-bezier(0, 0, 0.58, 1);
    }
    &:hover {
      background: $pink;
      transform: translate(0, 0.25em);
      &::before {
        box-shadow: 0 0 0 2px $pink-border, 0 0.5em 0 0 $pink-shadow;
        transform: translate3d(0, 0.5em, -1em);
      }
    }
    &:active {
      background: $pink;
      transform: translate(0em, 0.75em);
      &::before {
        box-shadow: 0 0 0 2px $pink-border, 0 0 $pink-shadow;
        transform: translate3d(0, 0, -1em);
      }
    }
  }
}
</style>