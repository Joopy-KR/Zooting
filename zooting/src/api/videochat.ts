import {localAxios} from "@/util/http-commons";
import type { IdealUserSelect, PointsReq } from "@/types/global";

const local = localAxios();

// 최종 선택 송신
async function pickUserApi(body:IdealUserSelect, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.post(`/api/meeting/picks`, body).then(success).catch(fail);
}

// 탈주시 포인트 차감
async function subPointsApi(body:PointsReq, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.patch(`/api/members/penalty/points`, body).then((resp) => console.log(resp)).catch(fail);
}

// 최종 선택한 이성 보내기

export { pickUserApi, subPointsApi };