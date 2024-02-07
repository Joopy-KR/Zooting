<template>
  <div class="main__container">
    <div class="flex flex-col items-center justify-center w-screen h-screen" v-show="!isLoaded">
      <p class="mb-8 text-5xl font-bold">미팅 입장 중</p>
      <h3 class="mb-5">잠시만 기다려 주세요</h3>
      <div class="flex items-center justify-center" role="status">
        <svg
          aria-hidden="true"
          class="inline w-3/12 text-gray-200 animate-spin dark:text-gray-600 fill-pink-400"
          viewBox="0 0 100 101"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
            fill="currentColor"
          />
          <path
            d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
            fill="currentFill"
          />
        </svg>
      </div>
    </div>

    <div class="main__container" v-show="isLoaded">
      <!-- 자유 대화 -->
      <VideoChatTalk
      class="left-component"
      :session="session"
      :publisher="publisher"
      :subscribers="subscribers"
      :cameraHeight="cameraHeight"
      :cameraWidth="cameraWidth"
      v-if="currentStatus === 'talk'"
      />

      <!-- 캐치마인드 -->
      <VideoChatCatchMind class="left-component"
      :session="session"
      :publisher="publisher"
      :subscribers="subscribers"
      :cameraHeight="cameraHeight"
      :cameraWidth="cameraWidth"
      v-if="currentStatus === 'CatchMind'"
      />
      
      <!-- 사이드바 -->
      <VideoChatSideBarVue class="right-component"
      :session="session"
      :currentChat="currentChat"
      :currentAnimals="currentAnimals"
      :publisher="publisher"
      :subscribers="subscribers"
      :statusInfo="statusInfo"
      /> 
    </div>
  </div>
</template>

<script setup lang="ts">
// 자유대화 페이지
import VideoChatTalk from '@/components/video-chat/VideoChatTalk.vue'
// 캐치마인드 페이지
import VideoChatCatchMind from '@/components/video-chat/VideoChatCatchMind.vue'
// 사이드바
import VideoChatSideBarVue from '@/components/video-chat/VideoChatSideBar.vue'

import { ref, onMounted, onUnmounted } from 'vue'
import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import { useAccessTokenStore } from '@/stores/store.ts'
axios.defaults.headers.post["Content-Type"] = "application/json"

const { VITE_SERVER_API_URL } = import.meta.env
const store = useAccessTokenStore()

onMounted(() => {
  startSession()
})

onUnmounted(() => {
  leaveSession()
})

// 진행을 위한 비동기 처리 함수
async function startSession() {
  await getUser()
  await joinSession()
}

// 로딩 완료를 나타내기 위한 플래그
const isLoaded = ref(false)


// 현재 진행중인 컴포넌트
const currentStatus = ref('')
const statusInfo = ref('')  // 사이드바에 나타나는 현재 진행중인 프로그램
currentStatus.value = 'CatchMind'

if (currentStatus.value = 'CatchMind') {
  statusInfo.value = '캐치마인드'
} else {
  statusInfo.value = '자유 대화 시간'
}

// 현재 참가중인 동물 목록
const currentAnimals = ref([])

// 카메라 사이즈 조정
const cameraHeight = ref(0)
const cameraWidth = ref(0)

cameraHeight.value = 160
cameraWidth.value = 266

// 현재 채팅 내용 (세션별로 관리해야됨)

const mySessionId = ref("SessionA")
const myUserName = ref<any>('')
const myUserAnimal = ref<any>('')
const myGender = ref<any>('')

async function getUser() {
    await store.getUserInfo()
    myUserName.value = store.userInfo?.nickname
    myUserAnimal.value = store.userInfo?.animal
    myGender.value = store.userInfo?.gender
}

const OV = ref(undefined);
const session = ref(undefined);
const mainStreamManager = ref(undefined);
const publisher = ref(undefined);
const subscribers = ref([]);
const currentChat = ref([])


const joinSession = () => {
  // Openvidu 객체 가져오기
  OV.value = new OpenVidu();

  // 세션 초기화
  session.value = OV.value.initSession();
  
  
  // 세션 관리 이벤트 리스너들
  // 새로운 세션이 들어올때마다 subscribers에 담아주기
  session.value.on("streamCreated", ({ stream }) => {
    const subscriber = session.value.subscribe(stream);
    subscribers.value.push(subscriber);

    // 새로 들어온 subscriber 동물상 현재 참가동물에 넣어주기
    currentAnimals.value.push(JSON.parse(stream.connection.data).animal)
  });
  
  // 세션이 나갈때마다 subscribers에서 빼기
  session.value.on("streamDestroyed", ({ stream }) => {
    currentAnimals.value.pop(JSON.parse(stream.connection.data).animal)
    const index = subscribers.value.indexOf(stream.streamManager, 0);
    if (index >= 0) {
      subscribers.value.splice(index, 1);
    }
  });
  
  // 비동기 예외 발생시
  session.value.on("exception", ({ exception }) => {
    console.warn(exception);
  });


  session.value.on('signal', (event) => {
    const fromData = JSON.parse(event.from.data)
    const chatData: any = {
      'animal': fromData.animal,
      'nickname': fromData.nickname,
      'gender': fromData.gender,
      'message': event.data
    }
    currentChat.value.push(chatData)
  });

  // 현재 참가중인 동물목록에 자신 넣어주기
  currentAnimals.value.push(myUserAnimal.value)

  // 유저 토큰 이용해 세션과 연결하기 (이 부분이 주어지는 토큰으로 대체)

  // Openvidu 배포 서버로부터 토큰 가져오기(이 부분이 바뀌어야함)
  getToken(mySessionId.value).then((token) => {

    // 첫번째 인자는 토큰, 두번째 인자는 모든 유저에 의해 검색 가능
    // 'streamCreated'(Stream.connection.data의 속성), 유저 닉네임으로 DOM에 추가됨
    session.value.connect(token, { nickname: myUserName.value, animal: myUserAnimal.value, gender: myGender.value }).then(() => {

      // 실제 publish 하는 부분, 이 부분에서 카메라와 오디오 소스 설정 가능
      let pub = OV.value.initPublisher(undefined, {
        audioSource: undefined,
        videoSource: undefined,
        publishAudio: true,
        publishVideo: true,
        resolution: "640x480",
        frameRate: 30,
        insertMode: "APPEND",
        mirror: false,
      });

      // mainStream과 Publisher(나 자신)에 정보를 담고
      mainStreamManager.value = pub;
      publisher.value = pub;      

      // stream Publish 하기
      session.value.publish(publisher.value);
    })
    .then(() => {
      isLoaded.value = true
    })
    .catch((error) => {
      console.log("세션과 연결중 에러가 발생했습니다:", error.code, error.message);
    });
  });

  // 사용자가 화면을 나가버릴시 세션 나가기
  window.addEventListener("beforeunload", leaveSession);
};


const leaveSession = () => {
  // session.disconnect 메서드 통해 연결 해제 가능
  if (session.value) session.value.disconnect()

  // 모든 속성 초기화하기
  session.value = undefined
  mainStreamManager.value = undefined
  publisher.value = undefined
  subscribers.value = []
  OV.value = undefined
  currentAnimals.value = []

  // 세션을 이미 나갔으니 화면 나갈때의 이벤트리스트 제거하기
  window.removeEventListener("beforeunload", leaveSession);
};

// 토큰 가져오기 (이후 서버가 줄 예정)
const getToken = async (mySessionId) => {
  const sessionId = await createSession(mySessionId);
  return await createToken(sessionId);
};

// 세션 만들도록 요청하는 axios (이후 서버가 줄 예정)
const createSession = async (sessionId) => {
  const response = await axios.post(VITE_SERVER_API_URL + '/api/sessions', { customSessionId: sessionId }, {
    headers: { 
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${store.getAccessToken()}`
             },
  });
  return response.data; // sessionId가 옴
};

// 토큰 만들어달라고 요청하는 axios (이후 서버가 줄 예정)
const createToken = async (sessionId) => {
  const response = await axios.post(VITE_SERVER_API_URL + '/api/sessions/' + sessionId + '/connections', {}, {
    headers: { 
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${store.getAccessToken()}`
             },
  });
  return response.data; // 토큰이 옴
};
</script>


<style scoped>
.main__container {
  @apply flex w-screen h-screen;
  min-width: 1420px;
}

.left-component {
  @apply flex-grow;
}

.right-component {
  width: 380px;
}
</style>