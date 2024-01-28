import { localAxios } from "@/util/http-commons";

const local = localAxios();

async function loadMyInfoApi(success: any, fail: any) {
  local.defaults.headers["Authorization"] = `Bearer ${await localStorage.getItem("accessToken")}`;
  await local.get(`/api/members`).then(success).catch(fail);
}

async function updateMyInfoApi(body: any, success: any, fail: any) {
  local.defaults.headers["Authorization"] = `Bearer ${await localStorage.getItem("accessToken")}`;
  await local.patch(`api/members/info`, body).then(success).catch(fail);
}

async function loadUserInfoApi(param: string, success: any, fail: any) {
  local.defaults.headers["Authorization"] = `Bearer ${await localStorage.getItem("accessToken")}`;
  await local.get(`api/members/info?nickname=${param}`).then(success).catch(fail);
}

async function checkIsMyProfileApi(param: string, success: any, fail: any) {
  local.defaults.headers["Authorization"] = `Bearer ${await localStorage.getItem("accessToken")}`;
  await local.get(`api/members/myprofile/check?nickname=${param}`).then(success).catch(fail);
}

export { loadMyInfoApi, updateMyInfoApi, loadUserInfoApi, checkIsMyProfileApi };
