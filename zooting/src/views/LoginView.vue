<script setup lang="ts">
import {onMounted} from "vue";
import {useRouter} from "vue-router";
import {useAccessTokenStore} from "@/stores/store";
import {checkPrivilegeApi} from "@/api/member";

const router = useRouter();
const store = useAccessTokenStore();

const checkPrivilege = async (): Promise<String[] | undefined> => {
  return new Promise<string[] | undefined>((resolve, reject) => {
    const store = useAccessTokenStore();
    if (!store.getAccessToken()) {
      resolve(undefined);
    }

    checkPrivilegeApi(
        ({data}: any) => resolve(data["result"]),
        (error: any) => {
          console.log(error);
          reject(error);
        }
    );
  });
}

const handleSocialLoginRedirect = async () => {
  try {
    const token = getTokenFromURL();
    if (token["accessToken"]) {
      store.setAccessToken(token["accessToken"]);
      if (token["refreshToken"]) {
        store.setRefreshToken(token["refreshToken"]);
      }
      await store.getUserInfo();

      //
      // ANOMYNOUS 권한의 경우 signup 페이지로 이동

      const check = await checkPrivilege().then((privilege) => {
        if (!privilege) {
          return true;
        }
        if (privilege.includes("ANONYMOUS")) {
          return true;
        }
        return false;
      })

      if (check) {
        await router.push({name: "signup"});
      } else {
        await router.push({name: "home"});
      }
    }
  } catch (error) {
    console.error("소셜 로그인 실패: ", error);
    router.push({name: "signin"});
  }
};

const getTokenFromURL = () => {
  const urlParams = new URLSearchParams(window.location.search);
  const accessToken = urlParams.get("access-token");
  const refreshToken = urlParams.get("refresh-token");
  return {
    accessToken: accessToken,
    refreshToken: refreshToken,
  };
};

onMounted(() => {
  handleSocialLoginRedirect();
});
</script>

<template>
  <div></div>
</template>

<style scoped></style>