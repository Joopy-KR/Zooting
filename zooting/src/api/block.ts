import {localAxios} from "@/util/http-commons";
import type {BlockUserReq} from "@/types/global";

const local = localAxios();

// 유저 차단
async function blockUserApi(body: BlockUserReq, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.post(`/api/reports`, body).then(success).catch(fail);
}

// 유저 차단 해제
async function disableBlockUserApi(body: BlockUserReq, success:any, fail:any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.delete(`/api/reports`, body).then(success).catch(fail);
}

export {blockUserApi, disableBlockUserApi};