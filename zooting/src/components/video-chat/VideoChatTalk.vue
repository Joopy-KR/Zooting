<template>
  <div class="flex flex-col items-center justify-center h-screen">
    <div id="video-container" class="grid grid-cols-2 gap-8" v-if="session">
      <user-video :stream-manager="publisher" id="publishVideoRef"/>
      <user-video v-for="sub in subscribersComputed" :key="sub.stream.connection.connectionId" :stream-manager="sub"/>
    </div>
    <button class="btn btn-lg btn-success" @click="joinSession()" v-if="!session">
      매칭이 완료되었어요
    </button>
    <input class="btn btn-large btn-danger" type="button" id="buttonLeaveSess ion" @click="leaveSession"
      value="Leave session" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import axios from "axios";
import { OpenVidu, Subscriber } from "openvidu-browser";
import UserVideo from '@/components/video-chat/UserVideo.vue' 
import { useAccessTokenStore } from '@/stores/store.ts'
axios.defaults.headers.post["Content-Type"] = "application/json";


const APPLICATION_SERVER_URL = 'https://i10a702.p.ssafy.io/'

const store = useAccessTokenStore()

const mySessionId = ref("SessionA");
const myUserName = ref("Participant" + Math.floor(Math.random() * 100));

const OV = ref(undefined);
const session = ref(undefined);
const mainStreamManager = ref(undefined);
const publisher = ref(undefined);
const subscribers = ref([]);

const subscribersComputed = computed(() => subscribers.value)


const joinSession = () => {
  // --- 1) Get an OpenVidu object ---
  OV.value = new OpenVidu();

  // --- 2) Init a session ---
  session.value = OV.value.initSession();
  
  // --- 3) Specify the actions when events take place in the session ---

  // On every new Stream received...
  session.value.on("streamCreated", ({ stream }) => {

    const subscriber = session.value.subscribe(stream);
    subscribers.value.push(subscriber);
    console.log(stream.streamManager)
    console.log(subscriber)
  });
  
  // On every Stream destroyed...
  session.value.on("streamDestroyed", ({ stream }) => {
    const index = subscribers.value.indexOf(stream.streamManager, 0);
    if (index >= 0) {
      subscribers.value.splice(index, 1);
    }
  });
  
  // On every asynchronous exception...
  session.value.on("exception", ({ exception }) => {
    console.warn(exception);
  });

  
  // --- 4) Connect to the session with a valid user token ---

  // Get a token from the OpenVidu deployment
  getToken(mySessionId.value).then((token) => {

    // First param is the token. Second param can be retrieved by every user on event
    // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
    session.value.connect(token, { clientData: myUserName.value }).then(() => {
      
      // --- 5) Get your own camera stream with the desired properties ---
      // Init a publisher passing undefined as targetElement (we don't want OpenVidu to insert a video
      // element: we will manage it on our own) and with the desired properties
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

      // Set the main video in the page to display our webcam and store our Publisher
      mainStreamManager.value = pub;
      publisher.value = pub;
      console.log(pub);
      

      // --- 6) Publish your stream ---
      session.value.publish(publisher.value);
    }).catch((error) => {
      console.log("There was an error connecting to the session:", error.code, error.message);
    });
  });

  window.addEventListener("beforeunload", leaveSession);
};


const leaveSession = () => {
  // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
  if (session.value) session.value.disconnect();

  // Empty all properties...
  session.value = undefined;
  mainStreamManager.value = undefined;
  publisher.value = undefined;
  subscribers.value = [];
  OV.value = undefined;

  // Remove beforeunload listener
  window.removeEventListener("beforeunload", leaveSession);
};

const getToken = async (mySessionId) => {
  const sessionId = await createSession(mySessionId);
  return await createToken(sessionId);
};


const createSession = async (sessionId) => {
  const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions', { customSessionId: sessionId }, {
    headers: { 
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${store.getAccessToken()}`
             },
  });
  return response.data; // The sessionId
};

const createToken = async (sessionId) => {
  const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connections', {}, {
    headers: { 
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${store.getAccessToken()}`
             },
  });
  return response.data; // The token
};
</script>

<style scoped>
.camera-box {
  @apply bg-slate-400;
  width: 462px; 
  height: 260px;
}


</style>  