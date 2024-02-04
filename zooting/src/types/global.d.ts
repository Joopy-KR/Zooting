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
    mbti: string | undefined;
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

declare interface Personality {
    [key: string]: {
        title: string;
        match: string;
        content: string[];
    };
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

declare interface DM {
    dmRoomId: number;
    dmList: DmItem[];
    cursor: number;
}
  
  declare interface DmItem {
    dmRoomId: number;
    id: number;
    sender: string;
    message: string;
    dmFiles: File[];
}
  
  declare interface File {
    fileId: number;
    thumbnailUrl: string;
}
  
export {
    UserInfo, Mask, TokenState, BlockUserReq,
    Personality, Friend, MaskReq, ReportUserReq, DM, DmItem
};