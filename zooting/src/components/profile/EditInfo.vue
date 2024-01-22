<script setup lang="ts">
import { computed, ref } from "vue";
import VueTailwindDatepicker from "vue-tailwind-datepicker";
import { RadioGroup, RadioGroupLabel, RadioGroupOption } from "@headlessui/vue";

const nickname = ref<string>("duckbill");
const gender = ref<string>("man");
const birth = ref<string>("1996-04-13");
const address = ref<string>("서울");
const idealTypeSet = ref(new Set<string>());

const areas: string[] = [
  "서울",
  "부산",
  "대구",
  "인천",
  "광주",
  "대전",
  "울산",
  "세종",
  "경기도",
  "강원도",
  "충청북도",
  "충청남도",
  "전라북도",
  "전라남도",
  "경상북도",
  "경상남도",
  "제주도",
  "해외",
];

const getGenderLabel = (value: string) => {
  console.log(gender.value);
  return value === "man" ? "남자" : "여자";
};
const pushIdealType = (value: string) => {
  if (idealTypeSet.value.has(value)) {
    idealTypeSet.value.delete(value);
  } else {
    idealTypeSet.value.add(value);
  }
  console.log(idealTypeSet.value);
};
const idealTypeList = computed(() => {
  if (gender.value === "man") {
    idealTypeSet.value = new Set<string>();
    return ["강아지", "고양이", "토끼", "사슴", "꼬북이"];
  } else if (gender.value === "woman") {
    idealTypeSet.value = new Set<string>();
    return ["강아지", "고양이", "토끼", "곰", "공룡"];
  }
});
const formatter = ref<{ date: string; month: string }>({
  date: "YYYY-MM-DD",
  month: "MMM",
});
</script>

<template>
  <div class="flex flex-col px-12 py-8">
    <div class="flex flex-row justify-between">
      <span
        class="inline-flex items-center px-10 py-3 text-xl font-bold text-blue-500 rounded-full bg-blue-50 ring-1 ring-inset ring-blue-700/10"
        >설정</span
      >
      <span
        class="inline-flex items-center px-4 py-3 text-sm text-gray-800 rounded-full bg-gray-50 ring-1 ring-inset ring-gray-500/10"
        >탈퇴</span
      >
    </div>
    <div class="flex flex-col w-2/3 px-16 py-20">
      <div class="relative input__div">
        <label for="nickname" class="absolute inline-block px-1 input__label bg-white -top-5 left-2"
          >닉네임</label
        >
        <input
          type="text"
          name="nickname"
          id="nickname"
          class="block w-full px-8 py-2 text-2xl font-bold text-center text-gray-900 border-0 rounded-md shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:leading-6"
          :value="nickname"
        />
      </div>
      <div class="input__div">
        <label for="gender" class="input__label">성별</label>
        <RadioGroup v-model="gender" :disabled="true">
          <div class="gender">
            <RadioGroupOption
              as="template"
              v-for="gender in ['man', 'woman']"
              :key="gender"
              :value="gender"
              v-slot="{ checked }"
            >
              <div
                :class="[
                  checked ? 'gender__option--checked' : 'gender__option--no-checked',
                  'gender__option',
                ]"
              >
                <RadioGroupLabel as="span">{{ getGenderLabel(gender) }}</RadioGroupLabel>
              </div>
            </RadioGroupOption>
          </div>
        </RadioGroup>
      </div>
      <div class="input__div">
        <label for="birth" class="input__label">생년월일</label>
        <VueTailwindDatepicker
          id="birth"
          v-model="birth"
          as-single
          :formatter="formatter"
          weekdays-size="min"
          class="font-bold text-center text-lg hover:bg-gray-200"
          :disabled="true"
        />
      </div>
      <div class="input__div">
        <label for="address" class="input__label">지역</label>
        <select id="address" v-model="address">
          <option value="" disabled selected hidden>사는 지역을 선택해 주세요.</option>
          <option v-for="(area, index) in areas" :key="index">{{ area }}</option>
        </select>
      </div>
      <div class="input__div">
        <label for="ideal-type" class="input__label">이상형</label>
        <div class="ideal-type__div">
          <!-- 선택한 성별에 따라 이상형 동물 목록 출력 -->
          <div
            class="ideal-type__item"
            v-for="(value, index) in idealTypeList"
            :key="index"
            @click="pushIdealType(`animal${index + 1}`)"
            :class="{
              'ideal-type__item--checked': idealTypeSet.has(`animal${index + 1}`),
              'ideal-type__item--no-checked': !idealTypeSet.has(`animal${index + 1}`),
            }"
          >
            {{ value }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.input__div {
  @apply w-full mx-4 my-3;
}
.input__label {
  @apply text-xl font-semibold text-gray-900;
}
.input__div select {
  @apply border border-gray-300 text-gray-900 text-xl font-semibold rounded-lg focus:ring-violet-500 focus:border-violet-500 block w-full px-4 py-3;
}
.gender {
  @apply grid grid-cols-2 gap-3;
}
.gender__option {
  @apply flex items-center justify-center rounded-lg p-2 text-sm uppercase md:flex-1 bg-gray-200;
}
.gender__option--checked {
  @apply bg-violet-600 text-white font-bold text-lg;
}
.gender__option--no-checked {
  @apply ring-1 ring-inset ring-gray-300 text-lg font-bold text-gray-900 hover:bg-gray-200;
}
.ideal-type__div {
  @apply mb-6 grid grid-cols-5 gap-3;
  height: 80px;
}
.ideal-type__item {
  @apply border rounded-md text-center flex justify-center items-center;
}
.ideal-type__item--checked {
  @apply bg-violet-600 hover:bg-violet-500;
}
.ideal-type__item--no-checked {
  @apply border-gray-300 hover:bg-gray-50;
}
</style>
