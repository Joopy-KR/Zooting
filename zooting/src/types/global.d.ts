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

export { UserInfo, Mask };