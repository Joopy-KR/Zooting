<script setup lang="ts">
import InfoSideBar from "@/components/profile/InfoSideBar.vue";
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {checkIsMyProfileApi, loadMyInfoApi, loadUserInfoApi} from "@/api/profile";
import {useAccessTokenStore} from "@/stores/store";

const store = useAccessTokenStore();
const route = useRoute();

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
  userInfo.value!.introduce = data["result"].introduce;
  userInfo.value!.interest = data["result"].interest;
  userInfo.value!.idealAnimal = data["result"].idealAnimal;
  userInfo.value!.backgroundImgUrl = data["result"].backgroundImgUrl;
  userInfo.value!.maskImgUrl = data["result"].maskImgUrl;
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

// 나의 정보 불러오기
const loadMyInfo = () => {
  loadMyInfoApi(
    ({ data }: any) => setUserInfo(data),
    (error: any) => {
      console.log(error);
    }
  );
};

const convertDate = (inputDate: string) => {
  // 주어진 문자열을 Date 객체로 변환
  const originalDate = new Date(inputDate);

  // 날짜를 하루 더함
  originalDate.setDate(originalDate.getDate() + 1);

  // 날짜를 원하는 형식으로 포맷
  return originalDate.toISOString().split("T")[0];
};

const isMyProfile = ref<boolean>(false);

const checkIsMyProfile = async (nickname: string) => {
  await checkIsMyProfileApi(
    nickname,
    ({ data }: any) => {
      const myProfile: boolean = data["result"].myProfile;

      if (myProfile !== undefined) {
        isMyProfile.value = myProfile;
      }
    },
    (error: any) => console.log(error)
  );
};

onMounted(async () => {
  const nickname = route.params.nickname;
  if (typeof nickname === "string") {
    await checkIsMyProfile(nickname);

    if (!isMyProfile.value) {
      loadUserInfo(nickname); // 다른 사람 프로필 조회
    } else {
      loadMyInfo(); // 내 정보 조회
    }
  } else {
    loadMyInfo();
  }
});
</script>

<template>
  <div class="flex flex-row w-screen h-screen divide-x-2 divide-gray-100">
    <div class="w-1/3">
      <InfoSideBar :user-info="userInfo" :is-my-profile="isMyProfile" />
    </div>
    <div class="w-2/3">
      <router-view :user-info="userInfo" :is-my-profile="isMyProfile" />
    </div>
  </div>
</template>

<style scoped></style>
