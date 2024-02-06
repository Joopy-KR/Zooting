<template>
  <div class="main__container">
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
    /> 
  </div>

  <div class="flex justify-between">
    <button class="mr-10 btn btn-lg btn-success" @click="joinSession()">
      매칭이 완료되었어요
    </button>
    <input class="btn btn-large btn-danger" type="button" id="buttonLeaveSess ion" @click="leaveSession"
    value="세션 나가기"  v-if="session"/>
  </div>
</template>

<script setup lang="ts">
// 자유대화 페이지
import VideoChatTalk from '@/components/video-chat/VideoChatTalk.vue'
// 캐치마인드 페이지
import VideoChatCatchMind from '@/components/video-chat/VideoChatCatchMind.vue'
// 사이드바
import VideoChatSideBarVue from '@/components/video-chat/VideoChatSideBar.vue'

import { ref } from 'vue'
import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import { useAccessTokenStore } from '@/stores/store.ts'
axios.defaults.headers.post["Content-Type"] = "application/json"

const { VITE_SERVER_API_URL } = import.meta.env

// 현재 진행중인 컴포넌트
const currentStatus = ref('')
currentStatus.value = 'CatchMind'

// 카메라 사이즈 조정
const cameraHeight = ref(0)
const cameraWidth = ref(0)

cameraHeight.value = 160
cameraWidth.value = 266

const store = useAccessTokenStore()

const mySessionId = ref("SessionA");
const myUserName = ref("Participant" + Math.floor(Math.random() * 100));

const OV = ref(undefined);
const session = ref(undefined);
const mainStreamManager = ref(undefined);
const publisher = ref(undefined);
const subscribers = ref([]);


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
  });
  
  // 세션이 나갈때마다 subscribers에서 빼기
  session.value.on("streamDestroyed", ({ stream }) => {
    const index = subscribers.value.indexOf(stream.streamManager, 0);
    if (index >= 0) {
      subscribers.value.splice(index, 1);
    }
  });
  
  // 비동기 예외 발생시
  session.value.on("exception", ({ exception }) => {
    console.warn(exception);
  });

  
  // 유저 토큰 이용해 세션과 연결하기 (이 부분이 주어지는 토큰으로 대체)

  // Openvidu 배포 서버로부터 토큰 가져오기(이 부분이 바뀌어야함)
  getToken(mySessionId.value).then((token) => {

    // 첫번째 인자는 토큰, 두번째 인자는 모든 유저에 의해 검색 가능
    // 'streamCreated'(Stream.connection.data의 속성), 유저 닉네임으로 DOM에 추가됨
    session.value.connect(token, { clientData: myUserName.value }).then(() => {

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