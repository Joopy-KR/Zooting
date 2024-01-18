<template>
  <div class="input__container">
    <!-- 기본 정보 (닉네임, 생년월일, 성별, 지역)-->
    <div class="input__section">
      <div class="input__title">
        <p>회원님을</p>
        <p>소개해 주세요</p>
      </div>

      <div class="input__div">
        <label for="nickname">닉네임</label>
        <input data-popover-target="popover-nickname" data-popover-placement="bottom" id="nickname" placeholder="2~16자 사이로 입력해 주세요." required>
      </div>

      <div class="input__div">
        <label for="gender">성별</label>
        <input type="text" name="" id="">
      </div>

      <div class="input__div">
        <label for="birth">생년월일</label>
        <vue-tailwind-datepicker v-model="dateValue" as-single id="birth"/>
      </div>
      
      <div class="input__div">          
        <label for="address">지역</label>
        <select id="address">
          <option v-for="(area, index) in areas" :key="index">{{ area }}</option>
        </select>
      </div>
    </div>
    
    <!-- 추가 정보 (취미, 이상형) -->
    <div class="input__section">
      관심사
      <div class="div0">

      </div>

      이상형
      <div class="div0">

      </div>
      <div class="consent">
        <input id="consent" type="checkbox" required>
        <label for="consent">개인정보 제3자 제공 동의</label>
      </div>
      
      <button class="submit-button" type="submit">Submit</button>
    </div>
    
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import VueTailwindDatepicker from 'vue-tailwind-datepicker'

const dateValue = ref([])
const areas = ref<string[]>(['서울특별시', '부산광역시', '대구광역시', '인천광역시', '광주광역시', '대전광역시', '울산광역시', '세종특별자치시', '경기도', '강원도', '충청북도', '충청남도', '전라북도', '전라남도', '경상북도', '경상남도', '제주특별자치도', '해외'])

</script>

<style scoped>
.input__container {
  @apply bg-white border border-gray-200 rounded-lg shadow flex flex-row h-full;
}
.input__section {
  @apply w-1/2 p-14;
}
.input__title {
  @apply mb-12;
}
.input__title p {
  @apply text-5xl font-bold mb-3;
}
.input__div {
  @apply mb-10;
}
.input__div label {
  @apply block mb-2 text-lg font-medium text-gray-900;
}
.input__div input {
  @apply bg-gray-50 border border-gray-300 text-gray-900 text-lg rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full h-12 p-2.5;
}
.input__div select {
  @apply bg-gray-50 border border-gray-300 text-gray-900 text-lg rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full h-12;
}
.consent {
  @apply flex items-center h-5 mb-6;
}
.consent input {
  @apply w-4 h-4 border border-gray-300 rounded bg-gray-50 focus:ring-blue-300;
}
.consent label {
  @apply text-lg font-medium text-gray-900 ms-2 dark:text-gray-300;
}

.submit-button {
  @apply text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-lg w-full px-5 py-2.5 text-center;
}
.div0 {
  @apply w-full h-1/3 bg-slate-400 mb-10;
}
</style>