import {localAxios} from "@/util/http-commons";
import type {ReportUserReq} from "@/types/global";

const local = localAxios();

// 유저 신고
async function reportUserApi(body: ReportUserReq, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.post(`/api/reports`, body).then(success).catch(fail);
}

export {reportUserApi};