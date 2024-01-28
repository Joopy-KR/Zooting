import { localAxios } from "@/util/http-commons";

const local = localAxios();

interface MaskReq {
  type: string;
  page: number;
  size: number;
}

async function getMaskListApi(params: MaskReq, success: any, fail: any) {
  local.defaults.headers["Authorization"] = `Bearer ${await localStorage.getItem("accessToken")}`;
  await local
    .get(`/api/items/mask?page=${params.page}&size=${params.size}`)
    .then(success)
    .catch(fail);
  // await local
  //   .get(`/api/items/mask?type=${params.type}&page=${params.page}&size=${params.size}`)
  //   .then(success)
  //   .catch(fail);
}

async function getMyMaskListApi(param: string, success: any, fail: any) {
  local.defaults.headers["Authorization"] = `Bearer ${await localStorage.getItem("accessToken")}`;
  await local.get("/api/mask").then(success).catch(fail);
  // await local.get(`/api/mask?type=${param}`).then(success).catch(fail);
}

export { getMaskListApi, getMyMaskListApi };
