<script setup lang="ts">
import InfoSideBar from "@/components/profile/InfoSideBar.vue";
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {checkIsMyProfileApi, loadMyInfoApi, loadUserInfoApi} from "@/api/profile";
import type {UserInfo} from "@/types/global";

const route = useRoute();

const userInfo = ref<UserInfo>({
  email: undefined,
  nickname: undefined,
  address: undefined,
  animal: undefined,
  backgroundId: undefined,
  backgroundImgUrl: undefined,
  birth: undefined,
  gender: undefined,
  idealAnimal: "[]",
  interest: undefined,
  introduce: undefined,
  maskId: undefined,
  maskImgUrl: undefined,
  personality: undefined,
  point: undefined,
  memberStatus: undefined,
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
  userInfo.value!.backgroundId = data["result"].backgroundId
  userInfo.value!.backgroundImgUrl = data["result"].backgroundImgUrl;
  userInfo.value!.maskId = data["result"].maskId;
  userInfo.value!.maskImgUrl = data["result"].maskImgUrl;
  userInfo.value!.memberStatus = data["result"].memberStatus;
};

const loadUserInfo = async (nickname: string) => {
  await loadUserInfoApi(
      nickname,
      ({data}: any) => setUserInfo(data),
      (error: any) => {
        console.log(error);
      }
  );
};

// 나의 정보 불러오기
const loadMyInfo = async () => {
  await loadMyInfoApi(
      ({data}: any) => setUserInfo(data),
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
      ({data}: any) => {
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
      await loadUserInfo(nickname); // 다른 사람 프로필 조회
    } else {
      await loadMyInfo(); // 내 정보 조회
    }
  } else {
    await loadMyInfo();
  }
  console.log(isMyProfile.value);
});
</script>

<template>
  <div class="flex flex-row w-screen h-screen divide-x-2 divide-gray-100">
    <div class="w-1/3">
      <InfoSideBar :user-info="userInfo" :nickname="route.params.nickname" :is-my-profile="isMyProfile" @load-user-info="loadUserInfo"/>
    </div>
    <div class="w-2/3">
      <router-view :user-info="userInfo" :nickname="route.params.nickname" :is-my-profile="isMyProfile" @load-my-info="loadMyInfo"/>
    </div>
  </div>
</template>

<style scoped></style>
