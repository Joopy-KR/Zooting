import {localAxios} from "@/util/http-commons";

const local = localAxios();
// 최근 미팅 기록
async function getMeetingLogApi(success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.get(`/api/meeting/log`).then(success).catch(fail);
}
export {
    getMeetingLogApi
}