<script setup lang="ts"></script>

<template>
  <div class="flex flex-col px-12 py-8">
    <div class="flex flex-row justify-between">
      <span
        class="inline-flex items-center px-10 py-3 text-3xl font-bold text-blue-600 rounded-full bg-blue-50 ring-1 ring-inset ring-blue-700/10"
        >설정</span
      >
      <span
        class="inline-flex items-center px-4 py-3 text-sm text-gray-800 rounded-full bg-gray-50 ring-1 ring-inset ring-gray-500/10"
        >탈퇴</span
      >
    </div>
    <div class="flex flex-col w-2/3 px-16 py-20">
      <div class="relative w-full mx-4 my-3">
        <label
          for="nickname"
          class="absolute inline-block px-1 text-2xl font-medium text-gray-600 bg-white -top-5 left-2"
          >닉네임</label
        >
        <input
          type="text"
          name="nickname"
          id="nickname"
          class="block w-full px-8 py-2 text-3xl text-center text-gray-900 border-0 rounded-md shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:leading-6"
          value="Joopy_KR"
        />
      </div>
    </div>
  </div>
</template>

<style scoped></style>
