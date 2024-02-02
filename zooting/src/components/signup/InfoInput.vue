<template>
  <div class="input__container">
    <!-- 기본 정보 (닉네임, 생년월일, 성별, 지역)-->
    <div class="input__section">
      <div class="input__title">
      <p>회원님을</p>
      <p>소개해 주세요</p>
      </div>
    
      <div class="input__div">
        <label for="nickname" class="input__label nickname__label">닉네임</label>
        <div class="nickname">
          <input 
          id="nickname" 
          v-model="nickname" 
          placeholder="2~16자 사이의 영어, 한글 또는 숫자"
          maxlength="16" 
          @blur="checkNicknameValidate"
          >
          <button 
          class="duplication-check" 
          @click="checkNicknameDuplication"
          :disabled="nicknameError"
          >
          중복 검사
          </button>
        </div>
        <div v-if="nicknameError" class="error-message">올바른 닉네임을 입력해 주세요</div>
        <div v-if="isDuplication" class="error-message">이미 사용 중인 닉네임입니다</div>
        <div v-if="isCorrectNickname" class="correct-message">사용 가능한 닉네임입니다</div>
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
        <div v-if="birthError" class="error-message">생년월일을 선택해 주세요</div>
      </div>
      
      <div class="input__div">          
        <label for="address" class="input__label">지역</label>
        <select id="address" v-model="address">
          <option value="" disabled selected hidden>거주지를 선택해 주세요</option>
          <option v-for="(area, index) in areas" :key="index">{{ area }}</option>
        </select>
        <div v-if="addressError" class="error-message">거주지를 선택해 주세요</div>
      </div>
    </div>
    
    <!-- 추가 정보 (관심사, 이상형) -->
    <div class="input__section">
      <label for="ideal-type" class="input__label">이상형</label>
      <div class="ideal-type__div">
        <!-- 선택한 성별에 따라 이상형 동물 목록 출력 -->
        <div 
        class="ideal-type__item" 
        v-for="(value, index) in idealAnimalList" :key="index" 
        @click="pushidealAnimal(value)"
        :class="{ 'ideal-type__item--checked': idealAnimalSet.has(value), 'ideal-type__item--no-checked': !idealAnimalSet.has(value) }">
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
      <button class="submit-button" type="button" @click.prevent="saveAdditionalInfo">다음으로</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
// @ts-ignore
import VueTailwindDatepicker from 'vue-tailwind-datepicker'
import { RadioGroup, RadioGroupLabel, RadioGroupOption } from '@headlessui/vue'
import { useAccessTokenStore } from '@/stores/store'

const store = useAccessTokenStore()
const nickname = ref<string>('')
const gender = ref<string>('man')
const birth = ref<string>('')
const address = ref<string>('')
const idealAnimalSet = ref(new Set<string>())
const interestSet = ref(new Set<string>())

const areas:string[] = ([
  '서울', 
  '부산', 
  '대구', 
  '인천', 
  '광주', 
  '대전', 
  '울산', 
  '세종', 
  '경기도', 
  '강원도', 
  '충청북도', 
  '충청남도', 
  '전라북도', 
  '전라남도', 
  '경상북도', 
  '경상남도', 
  '제주도', 
  '해외'
])

const interestList:string[] = [
  '개발',
  '건강',
  '게임', 
  '공연',
  '글쓰기',
  '드라마',
  '맛집', 
  '미술', 
  '문화', 
  '스포츠', 
  '여행', 
  '예술', 
  '영화', 
  '요리', 
  '운동',
  '음식',
  '음악', 
  '정치', 
  '사진', 
  '투자', 
  '학문', 
]

const nicknameError = ref<boolean>(false)
const birthError = ref<boolean>(false)
const addressError = ref<boolean>(false)

const checkNicknameValidate = () => {
  const regex = /^[a-zA-Z가-힣0-9]{2,16}$/
  if (!regex.test(nickname.value)) {
    nicknameError.value = true
  } else {
    nicknameError.value = false
  }
}

const isDuplication = computed(() => store.isDuplication)
const isChecked = ref<boolean>(false)
const checkedNickname = ref<string>('')
const isCorrectNickname = computed(() => {
  return !nicknameError.value && !isDuplication.value && checkedNickname.value === nickname.value && isChecked.value
})

const checkNicknameDuplication = () => {
  if (!nickname.value) {
    nicknameError.value = true
  } else {
    store.checkNicknameDuplication(nickname.value)
    isChecked.value = true
    checkedNickname.value = nickname.value
  }
} 

const getGenderLabel = (value: string) => {
  return value === 'man' ? '남자' : '여자'
}

const formatter = ref<{date: string, month: string}>({
  date: 'YYYY-MM-DD',
  month: 'MMM',
})

const pushidealAnimal = (value:string) => {
  if (idealAnimalSet.value.has(value)) {
    idealAnimalSet.value.delete(value)
  } else {
    idealAnimalSet.value.add(value)
  }
}

const idealAnimalList = computed(() => {
  if (gender.value === 'man') {
    idealAnimalSet.value = new Set<string>()
    return ['강아지', '고양이', '토끼', '사슴', '펭귄']
  } else if (gender.value === 'woman') {
    idealAnimalSet.value = new Set<string>()
    return ['강아지', '고양이', '토끼', '곰', '공룡']
  }
})

const pushInterest = (value:string) => {
  if (interestSet.value.size < 6 || interestSet.value.has(value)) {
    if (interestSet.value.has(value)) {
      interestSet.value.delete(value)
    } else {
      interestSet.value.add(value)
    }
  }
}

const saveAdditionalInfo = () => {
  if (isCorrectNickname.value && birth.value && address.value) {
    const payload: Payload = {
      nickname: nickname.value,
      gender: gender.value,
      birth: birth.value,
      address: address.value,
      interest: Array.from(interestSet.value),
      idealAnimal: Array.from(idealAnimalSet.value),
    }
    console.log(payload)
    store.saveAdditionalInfo(payload)
  } else if (!isCorrectNickname.value) {
    nicknameError.value = true
  }
  if (!birth.value) {
    birthError.value = true
  } else {
    birthError.value = false
  }
   if (!address.value) {
    addressError.value = true
  } else {
    addressError.value = false
  }
}

interface Payload {
  nickname: string
  gender: string
  birth: string
  address: string
  interest:string[]
  idealAnimal: string[]
}
</script>

<style scoped>
.input__container {
  @apply bg-white border border-gray-200 shadow grid grid-cols-1 lg:grid-cols-2 lg:h-full p-8 lg:p-6;
}
.input__section {
  @apply lg:p-8 h-full;
}
.input__title {
  @apply mb-10;
}
.input__title p {
  @apply text-3xl font-bold mb-1;
}
.input__div {
  @apply mb-8;
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
  @apply text-white bg-gradient-to-br from-purple-600 to-blue-500 hover:bg-gradient-to-bl font-medium rounded-lg text-sm px-5 py-2 text-center me-2 my-2 w-full;
}
.gender {
  @apply grid grid-cols-2 gap-3 h-10;
}
.gender__option {
  @apply flex items-center justify-center rounded-lg p-2 text-sm uppercase cursor-pointer;
}
.gender__option--checked {
  @apply text-violet-600 ring-2 ring-inset ring-violet-600 font-bold hover:bg-gray-50;
}
.gender__option--no-checked {
  @apply ring-1 ring-inset ring-gray-300  text-gray-900 hover:bg-gray-50;
}
.ideal-type__div {
  @apply mb-10 grid grid-cols-5 gap-3;
  height: 80px;
}
.ideal-type__item {
  @apply flex border rounded-md justify-center items-center cursor-pointer;
}
.ideal-type__item--checked {
  @apply text-violet-600 ring-2 ring-inset ring-violet-600 font-bold hover:bg-gray-50;
}
.ideal-type__item--no-checked {
  @apply border-gray-300 hover:bg-gray-50;
}
.interest__div {
  @apply mb-10 rounded-md grid grid-cols-4 gap-4 p-3;
  height: 270px;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: transparent transparent;
}
.interest__div::-webkit-scrollbar {
  width: 7px;
}
.interest__div::-webkit-scrollbar-thumb {
  background-color: #d6d6d6;
  border-radius: 4px;
}
.interest__div::-webkit-scrollbar-track {
  background-color: transparent;
}
.interest__item {
  @apply flex justify-center items-center bg-white hover:bg-gray-50 border border-gray-300 rounded-md cursor-pointer;
  height: 50px;
}
.interest__item--checked {
  @apply text-violet-600 ring-2 ring-inset ring-violet-600 font-bold hover:bg-gray-50;
}
.interest__item--no-checked {
  @apply border-gray-300 hover:bg-gray-50;
}
.nickname {
  position: relative;
}
.duplication-check {
  @apply rounded-md text-white text-sm bg-gradient-to-br from-purple-600 to-blue-500 hover:bg-gradient-to-bl h-7 w-20;
  position: absolute;
  top: 0;
  bottom: 0;
  right: 5px;
  margin: auto 2px;
}
.error-message {
  @apply text-red-500 text-sm mt-1;
}
.correct-message {
  @apply text-blue-500 text-sm mt-1;
}
</style>