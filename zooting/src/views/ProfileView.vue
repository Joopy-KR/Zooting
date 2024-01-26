<script setup lang="ts">
import InfoSideBar from "@/components/profile/InfoSideBar.vue";
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { loadUserInfoApi } from "@/api/profile";

const route = useRoute();

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

const userInfo = ref<Info>({
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

const loadUserInfo = (nickname: string) => {
  loadUserInfoApi(
    nickname,
    ({ data }: any) => {
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

      console.log(userInfo.value);
    },
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
  const formattedDate = originalDate.toISOString().split("T")[0];

  return formattedDate;
};

onMounted(() => {
  // loadMyInfo();
  const nickname = route.params.nickname;
  if (typeof nickname === "string") {
    loadUserInfo(nickname);
  }
});
</script>
<template>
  <div class="flex flex-row w-screen h-screen divide-x-2 divide-gray-100">
    <div class="w-1/3 h-full">
      <InfoSideBar :user-info="userInfo" />
    </div>
    <!-- <div class="w-2/3 h-full">
      <InfoMain />
    </div> -->
    <!-- <div class="w-2/3 h-full">
      <EditMaskList />
    </div> -->
    <!--    <div class="w-2/3 h-full">-->
    <!--      <EditInfo />-->
    <!--    </div>-->
    <div class="w-2/3 h-full">
      <router-view name="rcomp" :user-info="userInfo" />
    </div>
  </div>
</template>

<style scoped></style>
