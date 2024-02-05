import {localAxios} from "@/util/http-commons";
import type {FriendRequest} from "@/types/global";

const local = localAxios();

// 친구 추가
async function addFriendApi(body: FriendRequest, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.post(`/api/friends`, body).then(success).catch(fail);
}
// 친구 삭제
async function disableFriendApi(nickname: string, success: any, fail: any) {
    local.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem("accessToken")}`;
    await local.delete(`/api/friends/delete?nickname=${nickname}`).then(success).catch(fail);
}

export {addFriendApi, disableFriendApi};