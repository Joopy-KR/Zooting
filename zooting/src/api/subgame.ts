import {localAxios} from "@/util/http-commons";
import type {PointsReq} from "@/types/global";

const local = localAxios();

// 포인트 추가
async function addPointsApi(body:PointsReq, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.patch(`/api/members/points`, body).then(success).catch(fail);
}

export {addPointsApi};