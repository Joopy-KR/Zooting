<script setup lang="ts">
import { computed, ref, onMounted, watch } from "vue";
import { useRoute, useRouter, onBeforeRouteUpdate } from "vue-router";
import VueTailwindDatepicker from "vue-tailwind-datepicker";
import SuccessDialog from "@/components/profile/SuccessDialog.vue";
import FailDialog from "@/components/profile/FailDialog.vue";
import { RadioGroup, RadioGroupLabel, RadioGroupOption } from "@headlessui/vue";
import {
  loadMyInfoApi,
  updateMyInfoApi,
  loadUserInfoApi,
  checkNicknameApi,
  updateNicknameApi,
} from "@/api/profile";
import { useAccessTokenStore } from "@/stores/store";

const store = useAccessTokenStore();

const route = useRoute();
const router = useRouter();

const nickname = ref<string | null>();
const isNicknameUpdatable = ref<boolean>(false);
const isNicknameVerify = ref<boolean>(false);
const gender = ref<string>("man");
const idealTypeSet = ref(new Set<string>());
const birth = ref<string | null>(null);
const failAlert = ref(false);
const failMessage = ref<string>();
const successAlert = ref(false);

interface UserInfo {
  email: string | null;
  gender: string | null;
  nickname: string | null;
  birth: string | null;
  address: string | null;
  point: number | null;
  personality: string | null;
  animal: string | null;
  interest: string | null;
  introduce: string | null;
  idealAnimal: string;
  backgroundImgUrl: string | null;
  mbti: string | null;
  maskImgUrl: string | null;
}

const userInfo = ref<UserInfo>({
  email: null,
  gender: null,
  nickname: null,
  birth: null,
  address: null,
  point: null,
  personality: null,
  animal: null,
  interest: null,
  introduce: null,
  idealAnimal: "[]",
  backgroundImgUrl: null,
  maskImgUrl: null,
  mbti: null,
});

const setUserInfo = (data: any) => {
  userInfo.value!.email = data["result"].email;
  userInfo.value!.gender = data["result"].gender;
  userInfo.value!.nickname = data["result"].nickname;
  userInfo.value!.birth = convertDate(data["result"].birth);
  userInfo.value!.address = data["result"].address;
  userInfo.value!.point = data["result"].point;
  userInfo.value!.personality = data["result"].personality;
  userInfo.value!.animal = data["result"].animal;
  userInfo.value!.interest = data["result"].interest;
  userInfo.value!.idealAnimal = data["result"].idealAnimal;
  userInfo.value!.backgroundImgUrl = data["result"].backgroundImgUrl;
  userInfo.value!.maskImgUrl = data["result"].maskImgUrl;
  userInfo.value!.mbti = data["result"].mbti;
  initChanges();
};
// 나의 정보 불러오기
const loadMyInfo = () => {
  loadMyInfoApi(
    ({ data }: any) => setUserInfo(data),
    (error: any) => {
      console.log(error);
    }
  );
};

const loadUserInfo = (nickname: string) => {
  loadUserInfoApi(
    nickname,
    ({ data }: any) => setUserInfo(data),
    (error: any) => {
      console.log(error);
    }
  );
};

const toggleNicknameUpdateStatus = () => {
  isNicknameUpdatable.value = !isNicknameUpdatable.value;
};

// 나의 정보 수정
const updateMyInfo = () => {
  // 유효성 검증
  if (!userInfo.value!.address) {
    failMessage.value = "정확한 주소를 입력해 주세요";
    failAlert.value = true;
    return;
  }
  if (idealTypeSet.value.size == 0) {
    failMessage.value = "이상형 동물상을 선택해 주세요";
    failAlert.value = true;
    return;
  }

  const data = {
    address: userInfo.value!.address,
    idealAnimal: Array.from(idealTypeSet.value),
  };

  updateMyInfoApi(
    data,
    () => {
      loadMyInfo();
      successAlert.value = true;
    },
    (error: any) => console.log(error)
  );
};

const executeUpdateNickname = () => {
  if (!nickname.value) return;

  const data = {
    nickname: nickname.value,
  };

  updateNicknameApi(
    data,
    ({ data }: any) => {
      if (nickname.value) {
        userInfo.value.nickname = nickname.value;
      }
      // 로컬스토리지 유저 정보 업데이트
      store.getUserInfo();

      // 마이페이지로 리다이렉트
      router.replace({
        name: "home",
      });

      isNicknameUpdatable.value = false;
      isNicknameVerify.value = false;
    },
    (error: any) => {
      console.log(error);
      nickname.value = userInfo.value.nickname;
      isNicknameUpdatable.value = false;
      isNicknameVerify.value = false;
    }
  );
};

const cancelUpdateNickname = () => {
  nickname.value = userInfo.value.nickname;
  isNicknameUpdatable.value = false;
  isNicknameVerify.value = false;
};

const checkNickname = async (name: string) => {
  if (!name) {
    return;
  }
  checkNicknameApi(
    name,
    ({ data }: any) => {
      if (data["result"] === true) {
        isNicknameVerify.value = false; // 닉네임 중복
      } else {
        isNicknameVerify.value = true; // 닉네임 중복 X
      }
    },
    (error: any) => console.log(error)
  );
};

const setFailAlert = (isOpen: boolean) => {
  failAlert.value = isOpen;
};
const setSuccessAlert = (isOpen: boolean) => {
  successAlert.value = isOpen;
};

const initChanges = () => {
  if (userInfo.value.gender) {
    gender.value = userInfo.value.gender;
  }
  if (userInfo.value.nickname) {
    nickname.value = userInfo.value.nickname;
  }
  birth.value = userInfo.value.birth;
  idealTypeSet.value = parseStringToSet(userInfo.value!.idealAnimal);
};

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
  return value === "man" ? "남자" : "여자";
};
const pushIdealType = (value: string) => {
  if (idealTypeSet.value.has(value)) {
    idealTypeSet.value.delete(value);
  } else {
    idealTypeSet.value.add(value);
  }
};
const idealTypeList = computed(() => {
  if (gender.value === "man") {
    return ["강아지", "고양이", "토끼", "사슴", "꼬북이"];
  } else if (gender.value === "woman") {
    return ["강아지", "고양이", "토끼", "곰", "공룡"];
  }
});
const formatter = ref<{ date: string; month: string }>({
  date: "YYYY-MM-DD",
  month: "MMM",
});
const parseStringToSet = (stringList: string) => {
  try {
    // 대괄호를 제거하고 쉼표로 구분하여 배열로 변환
    const parsedList = stringList
      .replace(/^\[|\]$/g, "") // 대괄호 제거
      .split(", ")
      .map((item) => item.trim()); // 양쪽 공백 제거

    // 만약 배열이 아니면 예외 발생
    if (!Array.isArray(parsedList)) {
      throw new Error("Invalid input: Not an array.");
    }

    return new Set<string>(parsedList);
  } catch (error) {
    return new Set<string>(); // 파싱에 실패하면 null 반환
  }
};

const convertDate = (inputDate: string) => {
  // 주어진 문자열을 Date 객체로 변환
  const originalDate = new Date(inputDate);

  // 날짜를 하루 더함
  originalDate.setDate(originalDate.getDate() + 1);

  // 날짜를 원하는 형식으로 포맷
  const formattedDate = originalDate.toISOString().split("T")[0];

  return formattedDate;
};

const moveToMyPage = () => {
  router.push({
    name: "profile-check",
    params: { nickname: userInfo.value.nickname },
  });
};

const updateNicknameValue = (event: any) => {
  nickname.value = event.target.value;
};

watch(nickname, async (newValue, oldValue) => {
  if (!newValue) return;

  await checkNickname(newValue);
  console.log(isNicknameVerify.value);
});

onMounted(() => {
  // loadMyInfo();
  const nickname = route.params.nickname;
  if (!nickname) {
    loadMyInfo();
  } else {
    if (typeof nickname === "string") {
      loadUserInfo(nickname);
    }
  }
});
</script>

<template>
  <FailDialog
    title="업데이트 실패!"
    :message="failMessage"
    :fail-alert="failAlert"
    @set-fail-alert="setFailAlert"
  />
  <SuccessDialog
    title="업데이트 성공!"
    message="회원정보 업데이트 완료!"
    :success-alert="successAlert"
    @set-success-alert="setSuccessAlert"
  />
  <div class="flex flex-col px-12 py-8">
    <div class="flex flex-row justify-between">
      <div class="flex flex-row">
        <span
          class="flex items-center px-6 py-3 text-lg font-bold text-blue-500 rounded-full bg-blue-50 ring-1 ring-inset ring-blue-700/10"
          >설정</span
        >
        <div @click="moveToMyPage()" class="flex flex-col items-center ml-4">
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
      </div>
      <span
        class="inline-flex items-center px-4 py-3 text-sm text-gray-800 rounded-full bg-gray-50 ring-1 ring-inset ring-gray-500/10"
        >탈퇴</span
      >
    </div>
    <div class="flex justify-center items-center w-full">
      <div class="flex flex-col items-center lg:py-14 lg:px-10 w-full">
        <div class="relative input__div">
          <label
            for="nickname"
            class="absolute inline-block bg-transparent input__label -top-7 left-10"
            >닉네임</label
          >
          <input
            type="text"
            name="nickname"
            id="nickname"
            :class="isNicknameUpdatable ? 'input__nickname_enabled' : 'input__nickname_disabled'"
            :value="nickname"
            @input="updateNicknameValue"
            :disabled="!isNicknameUpdatable"
          />
          <div v-if="!isNicknameUpdatable">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="btn__nickname_enabled"
              @click="toggleNicknameUpdateStatus"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10"
              />
            </svg>
          </div>
          <div v-if="isNicknameUpdatable">
            <svg
              v-if="isNicknameVerify"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="btn__nickname_disabled"
              @click="executeUpdateNickname()"
            >
              <path stroke-linecap="round" stroke-linejoin="round" d="m4.5 12.75 6 6 9-13.5" />
            </svg>
            <svg
              v-if="!isNicknameVerify"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="btn__nickname_disabled"
              @click="cancelUpdateNickname()"
            >
              <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
            </svg>

            <p v-if="!isNicknameVerify" class="absolute -bottom-7 right-0 text-red-600">
              닉네임이 중복 됩니다.
            </p>
          </div>
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
            class="text-lg font-bold text-center hover:bg-gray-200"
            :disabled="true"
          />
        </div>
        <div class="input__div">
          <label for="address" class="input__label">지역</label>
          <select id="address" v-model="userInfo.address">
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
              @click="pushIdealType(value)"
              :class="{
                'ideal-type__item--checked': idealTypeSet.has(value),
                'ideal-type__item--no-checked': !idealTypeSet.has(value),
              }"
            >
              {{ value }}
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="flex flex-row justify-end w-5/6 mx-10">
      <button type="button" @click="initChanges" class="btn__save">초기화</button>
      <button type="button" @click="updateMyInfo" class="btn__cancel">저장</button>
    </div>
  </div>
</template>

<style scoped>
.input__nickname_enabled {
  @apply block w-full px-8 py-2 text-lg font-bold text-center text-gray-900 border-0 rounded-md shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:leading-6;
}
.input__nickname_disabled {
  @apply block w-full px-8 py-2 text-lg font-bold text-center text-gray-900 bg-gray-100 border-0 rounded-md shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:leading-6;
}
.btn__nickname_enabled {
  @apply w-8 h-8 absolute bottom-1 right-12 hover:stroke-indigo-600;
}
.btn__nickname_disabled {
  @apply w-8 h-8 absolute bottom-1 right-12 fill-orange-300 hover:stroke-orange-700;
}
.input__div {
  @apply w-5/6 px-10;
}
.input__label {
  @apply font-semibold text-gray-900;
}
.input__div select {
  @apply border border-gray-300 text-gray-900 text-lg font-semibold rounded-lg focus:ring-violet-500 focus:border-violet-500 block w-full px-4 py-3;
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

.btn__save {
  @apply w-1/6 py-3 px-4 mx-3 text-xl font-semibold text-white bg-orange-400 rounded-full shadow-sm hover:bg-orange-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-orange-300;
}
.btn__cancel {
  @apply w-1/6 py-3 px-4 mx-3 text-xl font-semibold text-white bg-indigo-600 rounded-full shadow-sm hover:bg-indigo-700 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-500;
}
</style>
