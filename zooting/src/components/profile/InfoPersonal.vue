<template>
  <div class="input__container relative">
    <div @click="moveToMyPage()" class="flex flex-col items-center ml-4 absolute top-5 left-5">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        stroke-width="1.5"
        stroke="currentColor"
        class="w-10 h-10 stroke-orange-500 fill-rose-100 mx-auto hover:fill-rose-300"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M8.25 9V5.25A2.25 2.25 0 0 1 10.5 3h6a2.25 2.25 0 0 1 2.25 2.25v13.5A2.25 2.25 0 0 1 16.5 21h-6a2.25 2.25 0 0 1-2.25-2.25V15m-3 0-3-3m0 0 3-3m-3 3H15"
        />
      </svg>
      <p class="font-sans font-semibold text-xs tracking-tight text-center">마이페이지</p>
    </div>
    <p class="lg:px-12 lg:pt-24 lg:pb-10 m-4 text-4xl font-bold tracking-tighter text-center">
      아바타 동물상 선택
    </p>
    <!-- 추가 정보 (관심사, 이상형) -->
    <div class="input__section">
      <label for="ideal-type" class="input__label">이상형</label>
      <div class="ideal-type__div">
        <!-- 선택한 성별에 따라 이상형 동물 목록 출력 -->
        <div
          class="ideal-type__item"
          v-for="(value, index) in idealAnimalList"
          :key="index"
          @click="pushidealAnimal(value)"
          :class="{
            'ideal-type__item--checked': idealAnimalSet.has(value),
            'ideal-type__item--no-checked': !idealAnimalSet.has(value),
          }"
        >
          {{ value }}
        </div>
      </div>

      <label for="" class="input__label">관심사 (최대 6개 선택)</label>
      <div class="interest__div">
        <div
          class="interest__item"
          v-for="(interest, index) in interestList"
          :key="index"
          @click="pushInterest(interest)"
          :class="{
            'interest__item--checked': interestSet.has(interest),
            'interest__item--no-checked': !interestSet.has(interest),
          }"
        >
          {{ interest }}
        </div>
      </div>
      <button class="submit-button" type="button" @click.prevent="updateIdealAnimalAndInterests">
        수정하기
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch, onMounted } from "vue";
import VueTailwindDatepicker from "vue-tailwind-datepicker";
import { useAccessTokenStore } from "@/stores/store";
import { useRouter } from "vue-router";
import { updateIdealAnimalAndInterestsApi } from "@/api/profile";

const store = useAccessTokenStore();
const router = useRouter();
const userInfo = ref(store.userInfo);
const gender = ref<string>("man");
const idealAnimalSet = ref(new Set<string>());
const interestSet = ref(new Set<string>());

const interestList: string[] = [
  "개발",
  "건강",
  "게임",
  "공연",
  "글쓰기",
  "드라마",
  "맛집",
  "미술",
  "문화",
  "스포츠",
  "여행",
  "예술",
  "영화",
  "요리",
  "운동",
  "음식",
  "음악",
  "정치",
  "사진",
  "투자",
  "학문",
];

const moveToMyPage = () => {
  if (!userInfo) return;
  router.push({ name: "profile-check", params: { nickname: userInfo.value?.nickname } });
};

const getGenderLabel = (value: string) => {
  return value === "man" ? "남자" : "여자";
};

const pushidealAnimal = (value: string) => {
  if (idealAnimalSet.value.has(value)) {
    idealAnimalSet.value.delete(value);
  } else {
    idealAnimalSet.value.add(value);
  }
};

const idealAnimalList = computed(() => {
  if (gender.value === "man") {
    idealAnimalSet.value = new Set<string>();
    return ["강아지", "고양이", "토끼", "사슴", "펭귄"];
  } else if (gender.value === "woman") {
    idealAnimalSet.value = new Set<string>();
    return ["강아지", "고양이", "토끼", "곰", "공룡"];
  }
});

const pushInterest = (value: string) => {
  if (interestSet.value.size < 6 || interestSet.value.has(value)) {
    if (interestSet.value.has(value)) {
      interestSet.value.delete(value);
    } else {
      interestSet.value.add(value);
    }
  }
};

const parsingGroupedList = (groupedStr: string) => {
  if (groupedStr) {
    const parsedArray = groupedStr.replace("[", "").replace("]", "").split(", ");
    return parsedArray.map((interest: string) => interest.trim());
  } else {
    return [];
  }
};

const updateIdealAnimalAndInterests = async () => {
  // value check
  if (!interestSet.value || !idealAnimalSet.value) {
    return;
  }
  // 이상형 동물의 개수는 1개 이상
  if (idealAnimalSet.value.size <= 0) {
    return;
  }

  await updateIdealAnimalAndInterestsApi(
    {
      interest: interestSet.value.size <= 0 ? [] : Array.from(interestSet.value),
      idealAnimal: Array.from(idealAnimalSet.value),
    },
    ({ data }: any) => store.getUserInfo(),
    (error: any) => console.error(error)
  );
};

watch(
  () => store.userInfo,
  (updateUser, oldUser) => {
    userInfo.value = updateUser;
  }
);

onMounted(async () => {
  if (!store.userInfo) {
    await store.getUserInfo();
    userInfo.value = store.userInfo;
  }
  // 성별 초기화
  if (userInfo.value && userInfo.value.gender) {
    gender.value = userInfo.value.gender;
  }

  // 이상형 초기화
  if (userInfo.value && userInfo.value.idealAnimal) {
    if (!idealAnimalList.value) {
      return;
    }
    const idealAnimals = parsingGroupedList(userInfo.value.idealAnimal);

    for (const animal of idealAnimals) {
      if (idealAnimalList.value.includes(animal)) {
        pushidealAnimal(animal);
      }
    }
  }
  // 관심사 초기화
  if (userInfo.value && userInfo.value.interest) {
    const interests = parsingGroupedList(userInfo.value.interest);

    for (const interest of interests) {
      if (interestList.includes(interest)) {
        pushInterest(interest);
      }
    }
  }
});
</script>

<style scoped>
.input__container {
  @apply bg-white border border-gray-200 shadow lg:h-full;
}
.input__section {
  @apply lg:px-32 md:px-16 py-4;
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
