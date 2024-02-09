<template>
  <div>
    <h1>WebSocket Chat</h1>
    <div id="messages"></div>
    <input type="text" id="messageInput" placeholder="Type your message">
    <input type="file" id="fileInput" multiple>
    <button @click="sendMessage()">Send</button>
  </div>
</template>

<script setup lang="ts">
import axios from "axios";
const { VITE_SERVER_API_URL } = import.meta.env

const socket = new SockJS(`${VITE_SERVER_API_URL}/ws/dm`);
const stompClient = Stomp.over(socket);
const sender = prompt("로그인 이메일.");
const receiver = prompt("받는 사람 이메일.");
stompClient.connect({}, () => {
    console.log('Connected to WebSocket');

    // Subscribe to the WebSocket topic
    stompClient.subscribe('/api/sub/dm/' + sender, (message) => {
        const dmReq = JSON.parse(message.body); // 받은 메시지를 처리
        console.log('Received DM:', dmReq);

        // 출력할 메시지를 추가
        const messagesDiv = document.getElementById('messages');
        messagesDiv.innerHTML += `<p>${dmReq.sender}: ${dmReq.message}</p>`;

    });
});

// Handle disconnection
socket.onclose = () => {
    console.log('Disconnected from WebSocket');
};

// 메시지 전송 함수
async function sendMessage() {
    const messageInput = document.getElementById('messageInput');
    const messageContent = messageInput.value;
    const fileInput = document.getElementById('fileInput');
    const files = fileInput.files;
    let filesList = [];
    // FormData 객체 생성
    const formData = new FormData();
    formData.append('dmRoomId', 6);
    formData.append('sender', sender);
    formData.append('receiver', receiver);
    formData.append('message', messageContent);
    formData.append('files', files);
    // 파일들을 FormData에 추가
    for (let i = 0; i < files.length; i++) {
        formData.append('files', files[i]);
    }
    try {
        if (files.length > 0) {
            // Axios를 사용하여 파일 업로드 요청 보내기
            const response = await axios.post('/api/file/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            //response to fileResDto
            console.log(response.data); // 파일 업로드에 대한 응답 확인
            filesList = response.data.result.map(file => ({
                S3Id: file.S3Id,
                originFileName: file.originFileName,
                fileName: file.fileName,
                imgUrl: file.imgUrl,
                fileDir: file.fileDir,
                thumbnailUrl: file.thumbnailUrl
            }));
        }

            // Send a message to the WebSocket topic with FormData
            stompClient.send('/api/pub/dm/message', {}, JSON.stringify({
                dmRoomId: 6,
                sender: sender,
                receiver: receiver,
                message: messageContent,
                files: filesList
            }));

            // 입력 필드 초기화
            messageInput.value = '';
            fileInput.value = ''; // 파일 입력 필드 초기화
        }

    catch
        (error)
        {
            console.error(error);
        }
    }
</script>

<style scoped>

</style>
<!--
<script setup lang="ts">
import {onMounted, ref, watch} from "vue"
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import {useAccessTokenStore} from "@/stores/store";

const socket = ref(null);
const stompClient = ref(null);
const connection = ref(false);
const store = useAccessTokenStore();

// 소켓 통신 연결 요청
const connect = () => {
  if (!store.getAccessToken()) {
    console.log("not found");
    return
  }
  socket.value = new SockJS(`http://localhost:8080/ws/dm`);
  stompClient.value = Stomp.over(socket.value);

  var headers = {
    "Authorization": "Bearer " + store.getAccessToken(),
    // Add other headers if needed
  };
  stompClient.value.connect(
      headers,
      () => {
        console.log("OKOKOKO");
        // onConnected(props.chatRoom.roomId);
      },
      () => {
        console.log("Could not WebSocket server. Retry!");
        // 일정 시간 이후 재연결 시도
        // setTimeout(this, 5000);
      }
  )
};

// 소켓 클라이언트 Subscribe 요청
const onConnected = () => {
  stompClient.value.subscribe("/api/sub", "uhyeon7399@kakao.com");
  console.log("osnfsf")
  // connection.value = true;
  // stompClient.value.send(
  //     "/pub/chat/enter",
  //     {},
  //     JSON.stringify({
  //       roomId: roomId,
  //       sender: me.value.nickname,
  //       senderId: me.value.id,
  //     })
  // );
};

watch(() => store.isLogin, () => {
  if (store.isLogin) {
    connect();
  }
})

onMounted(() => {
  if (store.isLogin) {
    connect();
  }
})
</script>

<template>

</template>

<style scoped>

</style> -->