import {localAxios} from "@/util/http-commons";

const local = localAxios();

// 배경 이미지 로딩 (페이징)
async function loadBackgroundImageApi(params: { page: number, size: number }, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.get(`/api/items/background?page=${params.page}&size=${params.size}`).then(success).catch(fail);
}

// 내 배경 이미지 로딩
async function loadMyBackgroundImageApi(success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.delete(`/api/items/background-inventory`).then(success).catch(fail);
}

// 배경 이미지 구입
async function purchaseBackgroundImageApi(body: { backgroundId: number }, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`
    await local.post(`/api/backgrounds`, body).then(success).catch(fail);
}

// 배경 이미지 변경
async function changeDefaultBackgroundImageApi(body: { backgroundId: number }, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.patch('/api/members/background', body).then(success).catch(fail);
}

export {loadBackgroundImageApi, loadMyBackgroundImageApi, purchaseBackgroundImageApi, changeDefaultBackgroundImageApi};