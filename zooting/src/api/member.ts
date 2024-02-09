import {localAxios} from "@/util/http-commons";

const local = localAxios();

// 닉네임 중복 확인
async function checkPrivilegeApi(success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.get(`/api/members/privilege/check`).then(success).catch(fail);
}

export {checkPrivilegeApi};