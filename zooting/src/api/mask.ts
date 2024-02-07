import {localAxios} from "@/util/http-commons";
import type {MaskReq} from "@/types/global";

const local = localAxios();


// 서버의 마스크 리스트 가져오기
async function getMaskListApi(params: MaskReq, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local
        .get(`/api/items/mask?page=${params.page}&size=${params.size}&animal=${params.animal}`)
        .then(success)
        .catch(fail);
}

// 해금 마스크 정보 가져오기
async function getMyMaskListApi(param: string, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.get("/api/items/mask-inventory").then(success).catch(fail);
    // await local.get(`/api/mask?type=${param}`).then(success).catch(fail);
}

// 마스크 구매
async function purchaseMaskApi(body: { maskId: number }, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.post("/api/mask").then(success).catch(fail);
}

// 내 디폴트 마스크 변경
async function changeDefaultMaskApi(maskId: number, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`
    await local.patch(`/api/mask?maskId=${maskId}`).then(success).catch(fail);
}

export {getMaskListApi, getMyMaskListApi, purchaseMaskApi, changeDefaultMaskApi};
