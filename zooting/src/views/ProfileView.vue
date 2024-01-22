<script setup lang="ts">
import InfoSideBar from "@/components/profile/InfoSideBar.vue";
import InfoMain from "@/components/profile/InfoMain.vue";
import InfoPersonal from "@/components/profile/InfoPersonal.vue";
import EditMaskList from "@/components/profile/EditMaskList.vue";
import EditInfo from "@/components/profile/EditInfo.vue";
import { ref } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();
console.log(route.params.nickname);

const profile = ref({
  nickname: "",
  interests: ["등산", "수영", "여행", "캠핑", "게임", "노래"],
  introduce: "캐치마인드 개고수 입니다",
  profileImg:
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQCgmII3Iks54cyWu3LiHejR1mcmJb_PVT_unHodbxd5dk_Tg4oN38yn4JN_km5a9xoT4Q&usqp=CAU",
});
</script>
<template>
  <div class="flex flex-row w-screen h-screen divide-x-2 divide-gray-100">
    <div class="w-1/3 h-full">
      <InfoSideBar />
    </div>
    <!-- <div class="w-2/3 h-full">
      <InfoMain />
    </div> -->
    <!-- <div class="w-2/3 h-full">
      <EditMaskList />
    </div> -->
    <div class="w-2/3 h-full">
      <EditInfo />
    </div>
  </div>
</template>

<style scoped></style>
