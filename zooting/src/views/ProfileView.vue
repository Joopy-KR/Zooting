<script setup lang="ts">
import InfoSideBar from "@/components/profile/InfoSideBar.vue";
import { ref } from "vue";
import { useRoute } from "vue-router";
import { loadUserInfoApi } from "@/api/profile";

const route = useRoute();
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
    <!--    <div class="w-2/3 h-full">-->
    <!--      <EditInfo />-->
    <!--    </div>-->
    <div class="w-2/3 h-full">
      <router-view name="rcomp" />
    </div>
  </div>
</template>

<style scoped></style>
