declare interface UserInfo {
    email: string | undefined;
    gender: string | undefined;
    nickname: string | undefined;
    birth: string | undefined;
    address: string | undefined;
    point: number | undefined;
    personality: string | undefined;
    animal: string | undefined;
    interest: string | undefined;
    introduce: string | undefined;
    idealAnimal: string;
    backgroundId: number | undefined,
    backgroundImgUrl: string | undefined;
    maskId: number | undefined,
    maskImgUrl: string | undefined;
    memberStatus: UserStatus | undefined;
}

declare interface UserStatus {
    isBlock: boolean | undefined,
    isFriend: boolean | undefined,
    isReport: boolean | undefined,
}

declare interface Mask {
    maskId: number;
    animal: string;
    description: string;
    price: number;
    fileName: string;
    imgUrl: string;
    status: boolean;
    isSelected: boolean;
}

declare interface TokenState {
    accessToken: string | null;
    refreshToken: string | null;
}

declare interface PersonalityList {
    [key: string]: Personality;
}

declare interface Personality {
    title: string;
    match: string;
    content: string[];
}

declare interface Friend {
    email: string;
    nickname: string;
    animal: string;
    gender: string;
}

declare interface MaskReq {
    page: number;
    size: number;
    animal: string | undefined;
}

declare interface ReportUserReq {
    nickname: string | undefined;
    reason: string | undefined;
}

declare interface BlockUserReq {
    nickname: string | undefined;
}

declare interface FriendRequest {
    nickname: string | undefined;
}

declare interface DM {
    dmRoomId: number;
    dmList: DmItem[];
    cursor: number;
}

declare interface DmItem {
    dmRoomId: number;
    sender: string;
    message: string;
}

declare interface Notice {
    noticeId: number | null;
    title: string | null;
    content: string | null;
    createdAt: string | null;
}

declare interface NoticePage {
    noticeList: Notice[];
    currentPage: number;
    totalPage: number;
}

declare interface Notify {
    title: string,
    message: any,
}

export {
    UserInfo, Mask, TokenState, BlockUserReq,
    PersonalityList, Personality, Friend, MaskReq, ReportUserReq,
    DM, DmItem, FriendRequest, Notice, NoticePage, Notify
};