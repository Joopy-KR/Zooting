<script setup lang="ts">
import { computed, ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import VueTailwindDatepicker from "vue-tailwind-datepicker";
import SuccessDialog from "@/components/profile/SuccessDialog.vue";
import FailDialog from "@/components/profile/FailDialog.vue";
import { RadioGroup, RadioGroupLabel, RadioGroupOption } from "@headlessui/vue";
import { loadMyInfoApi, updateMyInfoApi, loadUserInfoApi } from "@/api/profile";
import axios from "axios";
import { useAccessTokenStore } from "@/stores/store";

const router = useRoute();

interface Info {
  email: string | null;
  gender: string | null;
  nickname: string | null;
  birth: string | null;
  address: string | null;
  point: number | null;
  personality: string | null;
  animal: string | null;
  interest: string | null;
  idealAnimal: string;
  backgroundId: Number | null;
  maskId: Number | null;
}

const myInfo = ref<Info>({
  email: null,
  gender: null,
  nickname: null,
  birth: null,
  address: null,
  point: null,
  personality: null,
  animal: null,
  interest: null,
  idealAnimal: "[]",
  backgroundId: null,
  maskId: null,
});

// 나의 정보 불러오기
const loadMyInfo = () => {
  loadMyInfoApi(
    ({ data }: any) => {
      myInfo.value!.email = data["result"].email;
      myInfo.value!.gender = data["result"].gender;
      myInfo.value!.nickname = data["result"].nickname;
      myInfo.value!.birth = convertDate(data["result"].birth);
      myInfo.value!.address = data["result"].address;
      myInfo.value!.point = data["result"].point;
      myInfo.value!.personality = data["result"].personality;
      myInfo.value!.animal = data["result"].animal;
      myInfo.value!.interest = data["result"].interest;
      myInfo.value!.idealAnimal = data["result"].idealAnimal;
      initChanges();
    },
    (error: any) => {
      console.log(error);
    }
  );
};

// 나의 정보 수정
const updateMyInfo = () => {
  // 유효성 검증
  if (!myInfo.value!.address) {
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
    address: myInfo.value!.address,
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

const loadUserInfo = (nickname: string) => {
  loadUserInfoApi(
    nickname,
    ({ data }: any) => {
      myInfo.value!.email = data["result"].email;
      myInfo.value!.gender = data["result"].gender;
      myInfo.value!.nickname = data["result"].nickname;
      myInfo.value!.birth = convertDate(data["result"].birth);
      myInfo.value!.address = data["result"].address;
      myInfo.value!.point = data["result"].point;
      myInfo.value!.personality = data["result"].personality;
      myInfo.value!.animal = data["result"].animal;
      myInfo.value!.interest = data["result"].interest;
      myInfo.value!.idealAnimal = data["result"].idealAnimal;
      initChanges();
    },
    (error: any) => {
      console.log(error);
    }
  );
};

const gender = ref<string | null>("man");
const idealTypeSet = ref(new Set<string>());
const birth = ref<string | null>(null);
const failAlert = ref(false);
const failMessage = ref<string>();
const successAlert = ref(false);

const setFailAlert = (isOpen: boolean) => {
  failAlert.value = isOpen;
};
const setSuccessAlert = (isOpen: boolean) => {
  successAlert.value = isOpen;
};

const initChanges = () => {
  gender.value = myInfo.value.gender;
  birth.value = myInfo.value.birth;
  idealTypeSet.value = parseStringToSet(myInfo.value!.idealAnimal);
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

onMounted(() => {
  // loadMyInfo();
  const nickname = router.params.nickname;
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
      <span
        class="inline-flex items-center px-10 py-3 text-xl font-bold text-blue-500 rounded-full bg-blue-50 ring-1 ring-inset ring-blue-700/10"
        >설정</span
      >
      <span
        class="inline-flex items-center px-4 py-3 text-sm text-gray-800 rounded-full bg-gray-50 ring-1 ring-inset ring-gray-500/10"
        >탈퇴</span
      >
    </div>
    <div class="flex flex-col items-start w-full px-16 py-20 mx-10 my-4">
      <div class="w-2/3">
        <div class="relative input__div">
          <label
            for="nickname"
            class="absolute inline-block px-1 bg-white input__label -top-5 left-2"
            >닉네임</label
          >
          <input
            type="text"
            name="nickname"
            id="nickname"
            class="block w-full px-8 py-2 text-2xl font-bold text-center text-gray-900 bg-gray-100 border-0 rounded-md shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:leading-6"
            :value="myInfo!.nickname"
            :disabled="true"
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
            class="text-xl font-bold text-center hover:bg-gray-200"
            :disabled="true"
          />
        </div>
        <div class="input__div">
          <label for="address" class="input__label">지역</label>
          <select id="address" v-model="myInfo.address">
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

.btn__save {
  @apply w-1/6 py-3 px-4 mx-3 text-2xl font-medium text-white bg-orange-400 rounded-full shadow-sm hover:bg-orange-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-orange-300;
}
.btn__cancel {
  @apply w-1/6 py-3 px-4 mx-3 text-2xl font-medium text-white bg-indigo-600 rounded-full shadow-sm hover:bg-indigo-700 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-500;
}
</style>
