import axios from "axios";
import { ref, computed } from "vue";
import { defineStore } from "pinia";
import { useRouter } from "vue-router";
import { loadMyInfoApi } from "@/api/profile";
import type {DM, Friend, PersonalityList, TokenState, UserInfo, Notice, NoticePage} from "@/types/global";
const { VITE_SERVER_API_URL } = import.meta.env;

export const useStore = defineStore("store", () => {
  const personality: PersonalityList = {
    INFP: {
      title: "설레는 봄",
      match: "포근한 겨울",
      content: [
        "남에게 피해를 주는 것을 싫어해서 혼자 해결할 때가 많아요.",
        "아무리 싫은 사람과 연락하더라도 읽씹은 잘 못해요.",
        "괜찮다고 말해도 괜찮지 않을 때가 많아요.",
        "좋아하는 티를 내 주고 감정 표현을 해 주는 사람에게 호감을 느껴요.",
      ],
    },
    ISFP: {
      title: "따스한 봄",
      match: "풍요로운 가을",
      content: [
        "누워 있거나 기대어 있는 것을 가장 좋아해요.",
        "다정하며 사람들과의 화합을 중요시해요.",
        "거절을 잘 못하고 상대방의 감정을 다 받아 주면서 스트레스를 받아요.",
        "첫인상이 좋은 사람에게 신뢰감을 느껴요.",
      ],
    },
    ENFP: {
      title: "꽃 피는 봄",
      match: "따뜻한 겨울",
      content: [
        "혼자 있는 시간은 길고 따분하다고 느껴 활동적인 것을 선호해요.",
        "사람들과 빠르게 친해져 다소 가볍게 아는 사람들이 많아요.",
        "활발하고 긍정적인 편이라 다 이해해 줄 것 같지만 선 넘는 건 참을 수 없어요.",
        "세심하게 나에 대해 기억해 주는 사람에게 호감을 느껴요.",
      ],
    },
    ISFJ: {
      title: "완연한 봄",
      match: "활기찬 여름",
      content: [
        "조용하면서도 수다스러워 다 같이 노는 것을 좋아하지만 혼자서도 잘 놀아요.",
        "함께 있는 상대방에 따라 양극화가 가장 심해요.",
        "다른 사람에게 관대하지만 나에게는 엄격해요.",
        "성실하고 지적인 면모를 보이는 리더쉽 있는 사람에게 호감을 느껴요.",
      ],
    },
    ENTJ: {
      title: "비 오는 여름",
      match: "고독한 가을",
      content: [
        "책임감이 뛰어나 어떤 일이든지 반드시 해내려고 해요.",
        "동시에 여러 개의 목표를 이루려고 하는 목표 지향적인 사람이에요.",
        "나에게 어떤 행동을 강요하는 것을 매우 싫어해요.",
        "갈등 상황에 감정보다는 대화로 문제 해결을 할 줄 아는 사람이 좋아요.",
      ],
    },
    ESTP: {
      title: "푸르른 여름",
      match: "눈 오는 겨울",
      content: [
        "늘 새로운 것을 즐기는 함께하면 지루할 틈이 없는 성격이에요.",
        "임기응변 능력이 뛰어나 사람들에게 인기가 많은 편이에요.",
        "명확하게 말하지 않거나 돌려서 말하는 사람들은 답답해요.",
        "가르치기보다 같이 대화하고 토론이 가능한 사람이 좋아요.",
      ],
    },
    ESFP: {
      title: "활기찬 여름",
      match: "완연한 봄",
      content: [
        "에너지가 넘치고 활발해서 어디에서나 적응력이 뛰어나요.",
        "감정에 솔직하고 본능적인 면모가 있어요.",
        "행사나 파티에 나를 불러 주지 않으면 서운하고 신경 쓰여요.",
        "생일이나 기념일에 깜짝 이벤트를 해 주는 사람에게 크게 감동받아요.",
      ],
    },
    ISTP: {
      title: "쌀쌀한 여름",
      match: "시원한 가을",
      content: [
        "내성적이지만 관심 있는 분야에 대해서는 하루 종일도 이야기할 수 있어요.",
        "손재주가 좋은 편이고 활동적인 것을 좋아하지만 그 과정을 엄청 귀찮아해요.",
        "힘들어도 내 상황을 남에게 털어놓지 않아요.",
        "일부러 밀당을 한다거나 질투심을 유발하는 사람에게 정이 떨어져요.",
      ],
    },
    ESFJ: {
      title: "풍요로운 가을",
      match: "따스한 봄",
      content: [
        "대화하는 것을 좋아하고 활발해서 모임에서 주로 분위기 메이커 역할이에요.",
        "눈치가 빨라 어디서든 사회생활을 잘하고 사람들에게 평판이 좋아요.",
        "남에게 싫은 소리 하는 것을 힘들어해서 늘 꾹 참아요.",
        "다정다감하고 배려심이 깊으며 자상한 사람이 좋아요.",
      ],
    },
    ESTJ: {
      title: "시원한 가을",
      match: "쌀쌀한 여름",
      content: [
        "한 번 목표를 설정하면 끝까지 한 우물만 파는 완벽주의자예요.",
        "좋고 싫음이 확실해서 아니라고 판단되면 가차없이 차단해요.",
        "일을 못하는 사람, 게으른 사람을 보면 답답해요.",
        "본인의 일에 성실하고 배울 점이 많은 사람을 좋아해요.",
      ],
    },
    INTP: {
      title: "고독한 가을",
      match: "비 오는 여름",
      content: [
        "조용하고 차분하며 끝없는 자아성찰을 해요.",
        "해야 하는 일을 계속 미루지만 막상 시작하면 똑똑해서 곧잘 해내요.",
        "평소에는 잘 참는 성격이지만 선을 넘으면 참을 수 없어요.",
        "내가 먼저 좋아하고 호감이 가는 사람이 좋아요.",
      ],
    },
    INFJ: {
      title: "낙엽이 지는 가을",
      match: "눈 부신 겨울",
      content: [
        "새로운 사람들과 잘 어울리지만 혼자만의 시간이 매우 중요해요.",
        "창의력이 뛰어나며 완벽주의자 성향이 있어요.",
        "생각이 너무 많아 긴장이 연속되는 삶을 살고 있어요.",
        "내 속마음을 말할 수 있는 신뢰가 있는 사람을 좋아해요.",
      ],
    },
    ISTJ: {
      title: "눈 오는 겨울",
      match: "푸르른 여름",
      content: [
        "한 가지 일을 끝까지 성실하게 하는 것을 잘해요.",
        "말이 느린 편이지만 매우 신중하며 늘 진지해요.",
        "논점을 흐리거나 제대로 참여하지 않는 사람을 보면 스트레스받아요.",
        "조용히 날 챙겨 주고 먼저 도와주는 사람을 좋아해요.",
      ],
    },
    INTJ: {
      title: "따뜻한 겨울",
      match: "꽃 피는 봄",
      content: [
        "냉정하고 차갑다는 말을 자주 듣지만 내면은 감수성이 풍부해요.",
        "무심한 듯 잘 챙겨 주는 츤데레 성격이에요.",
        "확고한 내 생각이 있어 하나부터 열까지 이유를 말해 줘야 고집을 꺾을 수 있어요.",
        "거짓말을 하지 않고 솔직한 사람이 좋아요.",
      ],
    },
    ENFJ: {
      title: "포근한 겨울",
      match: "설레는 봄",
      content: [
        "사람들에게 늘 도움이 되고 싶어 하는 희생정신이 가득해요.",
        "늘 긍정적이고 사람들을 이끄는 것에 타고난 리더형이에요.",
        "갈등이 생기는 상황을 매우 싫어해요.",
        "나의 능력을 긍정적으로 이야기해 주는 사람을 좋아해요.",
      ],
    },
    ENTP: {
      title: "눈 부신 겨울",
      match: "낙엽이 지는 가을",
      content: [
        "주로 자기가 알고 있는 지식을 이야기하며 누가 태글을 걸면 하루 종일도 토론이 가능해요.",
        "머리가 좋아 다른 사람 파악과 상황 판단이 매우 빨라요.",
        "정해진 틀이나 반복적인 일을 싫어해요.",
        "내 이야기에 귀를 기울여주고 대화가 즐거운 사람에게 호감을 느껴요.",
      ],
    },
  };
  return { personality };
});

export const useAccessTokenStore = defineStore("access-token", () => {
  const API_URL: string = VITE_SERVER_API_URL;
  const router = useRouter();

  const state = ref<TokenState>({
    accessToken: localStorage.getItem("accessToken") || null,
    refreshToken: localStorage.getItem("refreshToken") || null,
  });

  const setAccessToken = function (token: string | null) {
    if (token) {
      localStorage.setItem("accessToken", token);
    } else {
      localStorage.removeItem("accessToken");
    }
    state.value.accessToken = token;
  };

  const getAccessToken = function () {
    if (state.value.accessToken) {
      return state.value.accessToken;
    } else {
      const accessToken = localStorage.getItem("accessToken");
      if (accessToken) {
        state.value.accessToken = accessToken;
        return accessToken;
      } else {
        router.push({ name: "signin" });
        alert("Access token not found");
      }
    }
  };

  const setRefreshToken = function (token: string | null) {
    if (token) {
      localStorage.setItem("refreshToken", token);
    } else {
      localStorage.removeItem("refreshToken");
    }
    state.value.refreshToken = token;
  };

  const getRefreshToken = function () {
    if (state.value.refreshToken) {
      return state.value.refreshToken;
    } else {
      const refreshToken = localStorage.getItem("refreshToken");
      if (refreshToken) {
        state.value.refreshToken = refreshToken;
        return refreshToken;
      } else {
        console.log("Refresh Token not exists!");
      }
    }
  };

  // 유저 정보
  const userInfo = ref<UserInfo | null>(null);

  const getUserInfo = async function () {
    await loadMyInfoApi(
      ({ data }: any) => {
        userInfo.value = data.result;

        if (!isCompletedSignUp.value) {
          router.push({ name: "signup" });
        } else if (!userInfo.value?.animal) {
          router.push({ name: "animal_test" });
        } else if (!userInfo.value?.personality) {
          router.push({ name: "personality_test" });
        }
      },
      (error: any) => {
        console.log(error);
        router.replace({ name: "signin" });
      }
    );
  };

  // 로그인 상태 판별
  const isLogin = computed(() => {
    return !!state.value.accessToken;
  });

  // 로그아웃
  const signOut = function () {
    window.localStorage.clear();
    state.value.accessToken = null;
  };

  // 추가 정보 저장 여부 확인
  const isCompletedSignUp = ref<boolean>(false);
  const checkCompletedSignUp = function () {
    axios({
      method: "get",
      url: `${API_URL}/api/members/privilege/check`,
      headers: {
        accept: "application/json",
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        isCompletedSignUp.value = res.data.result;
        if (!isCompletedSignUp.value) {
          router.push({ name: "signup" });
        } else {
          getUserInfo()
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 추가 정보 저장
  const saveAdditionalInfo = function (payload: {
    nickname: string;
    gender: string;
    birth: string;
    address: string;
    interest: string[];
    idealAnimal: string[];
  }) {
    const { nickname, gender, birth, address, interest, idealAnimal } = payload;
    console.log(payload);
    axios({
      method: "put",
      url: `${API_URL}/api/members`,
      data: {
        nickname,
        gender,
        birth,
        address,
        interest,
        idealAnimal,
      },
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        console.log(res);
        router.push({ name: "animal_test" });
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 닉네임 중복 검사
  const isDuplication = ref<boolean>(false);
  const checkNicknameDuplication = function (params: string) {
    axios({
      method: "get",
      url: `${API_URL}/api/members/nickname/check`,
      params: {
        nickname: params,
      },
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        console.log(res);
        isDuplication.value = res.data.result;
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 성격 테스트 결과 저장
  const setPersonality = function (payload: string) {
    const personality = payload;
    axios({
      method: "put",
      url: `${API_URL}/api/members/characters`,
      data: {
        personality,
      },
      headers: {
        accept: "application/json",
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        console.log(res);
        router.push({ name: "home" });
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 동물상 테스트 결과 저장
  const setAnimalFace = function (payload: number[]) {
    const animalfaceList = payload;
    axios({
      method: "post",
      url: `${API_URL}/api/animalface`,
      data: {
        animalfaceList,
      },
      headers: {
        accept: "application/json",
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 친구 리스트
  const friendList = ref<Friend[]>([]);
  const getFriendList = function () {
    axios({
      method: "get",
      url: `${API_URL}/api/friends`,
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        // console.log(res);
        friendList.value = res.data.result;
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 친구 요청 받은 리스트
  const requestFromList = ref<Friend[]>([]);
  const getRequestFromList = function () {
    axios({
      method: "get",
      url: `${API_URL}/api/friends/request/from`,
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        // console.log(res);
        requestFromList.value = res.data.result;
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 친구 요청 보낸 리스트
  const requestToList = ref<Friend[]>([]);
  const getRequestToList = function () {
    axios({
      method: "get",
      url: `${API_URL}/api/friends/request/to`,
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        // console.log(res);
        requestToList.value = res.data.result;
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 차단 리스트
  const blockList = ref<Friend[]>([]);
  const getBlockList = function () {
    axios({
      method: "get",
      url: `${API_URL}/api/members/blocklist`,
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        blockList.value = res.data.result;
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 친구 요청
  const friendRequest = function (payload: string) {
    const nickname = payload;
    axios({
      method: "post",
      url: `${API_URL}/api/friends`,
      data: {
        nickname,
      },
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        console.log(res);
        getRequestToList();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 친구 요청 수락
  const friendAccept = function (payload: string) {
    const nickname = payload;
    axios({
      method: "post",
      url: `${API_URL}/api/friends/accept`,
      data: {
        nickname,
      },
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        console.log(res);
        getRequestFromList();
        getFriendList();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 친구 요청 거절
  const friendReject = function (params: string) {
    const nickname = params;
    axios({
      method: "delete",
      url: `${API_URL}/api/friends/reject`,
      params: {
        nickname
      },
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        console.log(res);
        getRequestFromList();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 친구 요청 취소
  const friendRequestCancel = function (params: string) {
    const nickname = params;
    axios({
      method: "delete",
      url: `${API_URL}/api/friends/cancel`,
      params: {
        nickname
      },
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        console.log(res);
        getRequestToList();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 차단 해제
  const blockCancel = function (params: string) {
    const nickname = params;
    axios({
      method: "delete",
      url: `${API_URL}/api/block`,
      params: {
        nickname,
      },
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        console.log(res);
        getBlockList();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 친구 삭제
  const friendDelete = function (params: string) {
    const nickname = params;
    axios({
      method: "delete",
      url: `${API_URL}/api/friends/delete`,
      params: {
        nickname
      },
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    })
      .then((res) => {
        console.log(res);
        getFriendList();
      })
      .catch((err) => {
        console.log(err);
      });
  };

    const searchResult = ref<Friend[]>([])

    // 친구 검색
    const friendSearch = function (params: string) {
      const nickname = params
      axios({
        method: "get",
        url: `${API_URL}/api/friends/search`,
        params: {
          nickname,
        },
        headers: {
          Authorization: `Bearer ${getAccessToken()}`,
        },
      })
      .then((res) => {
        searchResult.value = res.data.result;
      })
      .catch((err) => {
        console.log(err);
      });
    };

    // 유저 검색
    const userSearch = function (params: string) {
      const nickname = params;
      axios({
        method: "get",
        url: `${API_URL}/api/members/searchlist`,
        params: {
          nickname,
        },
        headers: {
          Authorization: `Bearer ${getAccessToken()}`,
        },
      })
      .then((res) => {
        searchResult.value = res.data.result;
      })
      .catch((err) => {
        console.log(err);
      });
    };

    // DM 방 입장
    const isEntryDmRoom = ref<boolean>(false);
    const dmInfo = ref<DM | null>(null);
    const receiverInfo = ref<Friend | null>(null);

    const entryDmRoom = function (params: Friend) {
      const receiver = params.email;
      axios({
        method: "get",
        url: `${API_URL}/api/dm/room`,
        params: {
          receiver,
        },
        headers: {
          Authorization: `Bearer ${getAccessToken()}`,
        },
      })
        .then((res) => {
          console.log(res);
          dmInfo.value = res.data.result;
          receiverInfo.value = params;
        })
        .then((res) => {
          isEntryDmRoom.value = true;
        })
        .catch((err) => {
          console.log(err);
        });
    };

    // DM 커서
    const isRefreshing = ref<boolean>(false);
    const pastDmList = ref<DmItem[]>([])
    const cursorDmRoom = function (params: { cursor: number; dmRoomId: number }) {
      const {dmRoomId, cursor} = params;
      axios({
        method: "get",
        url: `${API_URL}/api/dm/room/prev`,
        params: {
          dmRoomId,
          cursor
        },
        headers: {
          Authorization: `Bearer ${getAccessToken()}`,
        },
      })
        .then((res) => {
          console.log(res);
          if (dmInfo.value) {
            dmInfo.value.cursor = res.data.result.cursor;
            pastDmList.value = [...pastDmList.value, ...res.data.result.dmList];
          }
        })
        .then((res) => {
          isRefreshing.value = false
        })
        .catch((err) => {
          console.log(err);
        });
    };

    // DM방 퇴장
    const exitDmRoom = function (params: number) {
      const dmRoomId = params;
      axios({
        method: "put",
        url: `${API_URL}/api/dm/room/exit`,
        params: {
          dmRoomId,
        },
        headers: {
          Authorization: `Bearer ${getAccessToken()}`,
        },
      })
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    };

    const noticePage = ref<NoticePage>();
    const noticeList = ref<Notice[]>([]);
    // 공지사항 리스트
    const getNoticeList = function (params: {'page': number, 'size':number}) {
        axios({
            method: "get",
            url: `${API_URL}/api/notice`,
            headers: {
                Authorization: `Bearer ${getAccessToken()}`,
            },
            params : params,
        })
            .then((res) => {
                noticePage.value = res.data.result;
                noticeList.value = res.data.result["noticeResList"];
            })
            .catch((err) => {
                console.log(err);
            });
    };

  return {
      setAccessToken,
      getAccessToken,
      setRefreshToken,
      getRefreshToken,
      userInfo,
      getUserInfo,
      isLogin,
      signOut,
      isCompletedSignUp,
      checkCompletedSignUp,
      setPersonality,
      saveAdditionalInfo,
      isDuplication,
      checkNicknameDuplication,
      setAnimalFace,
      friendList,
      getFriendList,
      requestFromList,
      getRequestFromList,
      requestToList,
      getRequestToList,
      blockList,
      getBlockList,
      friendRequest,
      friendAccept,
      friendReject,
      friendRequestCancel,
      blockCancel,
      friendDelete,
      friendSearch,
      userSearch,
      searchResult,
      entryDmRoom,
      isEntryDmRoom,
      dmInfo,
      receiverInfo,
      cursorDmRoom,
      isRefreshing,
      pastDmList,
      exitDmRoom,
      noticePage,
      noticeList,
      getNoticeList,
  };
});