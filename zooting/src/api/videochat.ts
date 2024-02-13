import {localAxios} from "@/util/http-commons";
import type { IdealUserSelect } from "@/types/global";

const local = localAxios();

// 최종 선택 송신
async function pickUserApi(body:IdealUserSelect, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.post(`/api/meeting/picks`, body).then(success).catch(fail);
}

export { pickUserApi };