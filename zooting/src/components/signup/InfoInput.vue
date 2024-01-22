<template>
  <div class="input__container">
    <!-- 기본 정보 (닉네임, 생년월일, 성별, 지역)-->
    <div class="input__section">
      <div class="input__title">
        <p>회원님을</p>
        <p>소개해 주세요</p>
      </div>

      <div class="input__div">
        <label for="nickname" class="input__label">닉네임</label>
        <input id="nickname" v-model="nickname" data-popover-target="popover-nickname" data-popover-placement="bottom" placeholder="2~16자 사이로 입력해 주세요." required>
      </div>

      <div class="input__div">
        <label for="gender" class="input__label">성별</label>
        <RadioGroup v-model="gender">
          <div class="gender">
            <RadioGroupOption as="template" v-for="gender in ['man', 'woman']" :key="gender" :value="gender" v-slot="{ checked }">
              <div :class="[checked ? 'gender__option--checked' : 'gender__option--no-checked', 'gender__option']">
                <RadioGroupLabel as="span">{{ getGenderLabel(gender) }}</RadioGroupLabel>
              </div>
            </RadioGroupOption>
          </div>
        </RadioGroup>
      </div>

      <div class="input__div">
        <label for="birth" class="input__label">생년월일</label>
        <VueTailwindDatepicker id="birth" v-model="birth" as-single :formatter="formatter" weekdays-size="min" class="birth__datepicker"/>
      </div>
      
      <div class="input__div">          
        <label for="address" class="input__label">지역</label>
        <select id="address" v-model="address">
          <option value="" disabled selected hidden>사는 지역을 선택해 주세요.</option>
          <option v-for="(area, index) in areas" :key="index">{{ area }}</option>
        </select>
      </div>
    </div>
    
    <!-- 추가 정보 (관심사, 이상형) -->
    <div class="input__section">
      <label for="ideal-type" class="input__label">이상형</label>
      <div class="ideal-type__div">
        <!-- 선택한 성별에 따라 이상형 동물 목록 출력 -->
        <div 
        class="ideal-type__item" 
        v-for="(value, index) in idealTypeList" :key="index" 
        @click="pushIdealType(`animal${index+1}`)"
        :class="{ 'ideal-type__item--checked': idealTypeSet.has(`animal${index+1}`), 'ideal-type__item--no-checked': !idealTypeSet.has(`animal${index+1}`) }">
        {{ value }}
        </div>
      </div>
      
      <label for="" class="input__label">관심사 (최대 6개 선택)</label>
      <div class="interest__div">
        <div 
        class="interest__item" 
        v-for="(interest, index) in interestList" :key="index" 
        @click="pushInterest(interest)"
        :class="{ 'interest__item--checked': interestSet.has(interest), 'interest__item--no-checked': !interestSet.has(interest) }"
        >
          {{ interest }}
        </div>
      </div>
      <button class="submit-button" type="submit">Submit</button>
    </div>
    
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import VueTailwindDatepicker from 'vue-tailwind-datepicker'
import { RadioGroup, RadioGroupLabel, RadioGroupOption } from '@headlessui/vue'

const nickname = ref<string>('')
const gender = ref<string>('man')
const birth = ref<string>('')
const address = ref<string>('')
const idealTypeSet = ref(new Set<string>())
const interestSet = ref(new Set<string>())

const getGenderLabel = (value: string) => {
  return value === 'man' ? '남자' : '여자'
}

const formatter = ref<{date: string, month: string}>({
  date: 'YYYY-MM-DD',
  month: 'MMM',
})

const areas:string[] = (['서울', '부산', '대구', '인천', '광주', '대전', '울산', '세종', '경기도', '강원도', '충청북도', '충청남도', '전라북도', '전라남도', '경상북도', '경상남도', '제주도', '해외'])

const pushIdealType = (value:string) => {
  if (idealTypeSet.value.has(value)) {
    idealTypeSet.value.delete(value)
  } else {
    idealTypeSet.value.add(value)
  }
  console.log(idealTypeSet.value)
}

const idealTypeList = computed(() => {
  if (gender.value === 'man') {
    idealTypeSet.value = new Set<string>()
    return ['강아지', '고양이', '토끼', '사슴', '꼬북이']
  } else if (gender.value === 'woman') {
    idealTypeSet.value = new Set<string>()
    return ['강아지', '고양이', '토끼', '곰', '공룡']
  }
})

const interestList:string[] = ['관심사1', '관심사2', '관심사3', '관심사4', '관심사5', '관심사6', '관심사7', '관심사8', '관심사9', '관심사10', '관심사11', '관심사12', '관심사13']

const pushInterest = (value:string) => {
  if (interestSet.value.size < 6 || interestSet.value.has(value)) {
    if (interestSet.value.has(value)) {
      interestSet.value.delete(value)
    } else {
      interestSet.value.add(value)
    }
    console.log(interestSet.value)
  }
}
</script>

<style scoped>
.input__container {
  @apply bg-white border border-gray-200 shadow grid grid-cols-1 md:grid-cols-2 md:h-full p-8 md:p-0;
}
.input__section {
  @apply md:p-8 h-full;
}
.input__title {
  @apply mb-5;
}
.input__title p {
  @apply text-2xl font-bold mb-1;
}
.input__div {
  @apply mb-6;
}
.input__label {
  @apply block mb-2 text-sm font-medium text-gray-900;
}
.input__div input {
  @apply border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-violet-500 focus:border-violet-500 block w-full h-10 p-2.5;
}
.input__div select {
  @apply border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-violet-500 focus:border-violet-500 block w-full h-10;
}
.submit-button {
  @apply text-white bg-violet-600 hover:bg-violet-800 focus:outline-none focus:ring-indigo-300 font-medium rounded-lg text-sm w-full px-5 py-2 my-2 text-center;
}
.gender {
  @apply grid grid-cols-2 gap-3;
}
.gender__option {
  @apply flex items-center justify-center rounded-lg p-2 text-sm uppercase md:flex-1;
}
.gender__option--checked {
  @apply bg-violet-600 text-white hover:bg-violet-500;
}
.gender__option--no-checked {
  @apply ring-1 ring-inset ring-gray-300  text-gray-900 hover:bg-gray-50;
}
.ideal-type__div {
  @apply mb-6 grid grid-cols-5 gap-3;
  height: 80px;
}
.ideal-type__item {
  @apply border rounded-md text-center;
}
.ideal-type__item--checked {
  @apply bg-violet-600 hover:bg-violet-500 text-white;
}
.ideal-type__item--no-checked {
  @apply border-gray-300 hover:bg-gray-50;
}
.interest__div {
  @apply mb-6 border border-gray-300 rounded-md grid grid-cols-4 gap-4 p-3;
  height: 205px;
  overflow-y: auto;
}
.interest__item {
  @apply bg-white hover:bg-gray-50 border border-gray-300 rounded-md text-center;
  height: 50px;
}
.interest__item--checked {
  @apply bg-violet-600 hover:bg-violet-500 text-white;
}
.interest__item--no-checked {
  @apply border-gray-300 hover:bg-gray-50;
}
</style>