import { localAxios } from "@/util/http-commons";

const local = localAxios();

// 닉네임 중복 확인
async function checkNicknameApi(param: string, success: any, fail: any) {
  local.defaults.headers["Authorization"] = `Bearer ${await localStorage.getItem("accessToken")}`;
  await local.get(`/api/members/nickname/check?nickname=${param}`).then(success).catch(fail);
}
// 닉네임 업데이트
async function updateNicknameApi(body: any, success: any, fail: any) {
  local.defaults.headers["Authorization"] = `Bearer ${await localStorage.getItem("accessToken")}`;
  await local.put(`/api/members/nickname`, body).then(success).catch(fail);
}
// 내 정보 가져오기
async function loadMyInfoApi(success: any, fail: any) {
  local.defaults.headers["Authorization"] = `Bearer ${await localStorage.getItem("accessToken")}`;
  await local.get(`/api/members`).then(success).catch(fail);
}

// 내 정보 업데이트
// body로는 json 데이터
// success, fail에는 각각 성공시 반환되는 json 데이터와 실패시 생기는 error 리턴
async function updateMyInfoApi(body: any, success: any, fail: any) {
  local.defaults.headers["Authorization"] = `Bearer ${await localStorage.getItem("accessToken")}`;
  await local.patch(`api/members/info`, body).then(success).catch(fail);
}
// 유저 정보 가져오기
async function loadUserInfoApi(param: string, success: any, fail: any) {
  local.defaults.headers["Authorization"] = `Bearer ${await localStorage.getItem("accessToken")}`;
  await local.get(`api/members/info?nickname=${param}`).then(success).catch(fail);
}
// 내 프로필인지 확인
async function checkIsMyProfileApi(param: string, success: any, fail: any) {
  local.defaults.headers["Authorization"] = `Bearer ${await localStorage.getItem("accessToken")}`;
  await local.get(`api/members/myprofile/check?nickname=${param}`).then(success).catch(fail);
}
// 내 자기소개 업데이트
async function updateIntroduceApi(body: any, success: any, fail: any) {
  local.defaults.headers["Authorization"] = `Bearer ${await localStorage.getItem("accessToken")}`;
  await local.put(`/api/members/introduce`, body).then(success).catch(fail);
}
// 이상형, 관심사 업데이트
interface UpdateIdealAnimalsAndInterests {
  interest: string[];
  idealAnimal: string[];
}
async function updateIdealAnimalAndInterestsApi(
  body: UpdateIdealAnimalsAndInterests,
  success: any,
  fail: any
) {
  local.defaults.headers["Authorization"] = `Bearer ${await localStorage.getItem("accessToken")}`;
  await local.put(`/api/members/interests`, body).then(success).catch(fail);
}

export {
  loadMyInfoApi,
  updateMyInfoApi,
  loadUserInfoApi,
  checkIsMyProfileApi,
  updateIntroduceApi,
  checkNicknameApi,
  updateNicknameApi,
  updateIdealAnimalAndInterestsApi,
};
