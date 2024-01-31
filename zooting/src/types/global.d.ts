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

export {
    UserInfo, Mask, TokenState, BlockUserReq,
    Personality, Friend, MaskReq, ReportUserReq
};