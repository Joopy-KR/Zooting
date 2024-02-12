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

    <!-- 동물상 가면 -->
    <div>
      <div>
        <video id="landmark-video" autoplay playsinline></video>
      </div>
      <div id="three-canvas-box"></div>
    </div>

    <div class="main__container" v-show="isLoaded">
      <!-- 자유 대화 -->
      <VideoChatTalk
      class="left-component"
      :session="session"
      :publisher="publisher"
      :subscribers="subscribers"
      v-if="currentStatus === 'FreeTalk'"
      />

      <!-- 캐치마인드 -->
      <VideoChatCatchMind class="left-component"
      :session="session"
      :publisher="publisher"
      :subscribers="subscribers"
      :drawData="drawData"
      :currentDrawingUserId="currentDrawingUserId"
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
// Vue
import { ref, onMounted, onUnmounted, computed, watch } from 'vue'
import { useAccessTokenStore } from '@/stores/store.ts'
import axios from 'axios'
const { VITE_SERVER_API_URL } = import.meta.env
// Three.js
import * as THREE from 'three'
import { OrbitControls } from 'three/addons/controls/OrbitControls.js'
import { GLTFLoader, GLTF } from 'three/addons/loaders/GLTFLoader.js'
// Face Landmark
import vision from "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.3"
const { FaceLandmarker, FilesetResolver } = vision
// OpenVidu
import { OpenVidu } from "openvidu-browser";

const store = useAccessTokenStore()

onMounted(async () => {
  // 동물상 모델 렌더 시작
  init()
})

onUnmounted(() => {
  leaveSession()
})

// 진행을 위한 비동기 처리 함수
async function startSession() {
  // 유저 정보 받아오기
  await getUser()
  // 세션 연결 시작
  await joinSession()
}

// 마스크 로딩 되었는지 판단할 플래그
const is_mask_loaded = ref(false)

// 세션 로딩 완료를 판단할 플래그
const isLoaded = ref(false)

// 현재 진행중인 컴포넌트
const currentStatus = ref<String>('')  // 현재 진행중인 프로그램 (FreeTalk, CatchMind 등) 
const statusInfo = ref<any>('')  // 사이드바에 나타나는 현재 진행중인 프로그램
currentStatus.value = 'CatchMind'

// 카메라 사이즈 조정
const cameraHeight = ref<Number>(0)
const cameraWidth = ref<Number>(0)

// 초기 변수 속성 설저 
// 현재 진행중인 프로그램 이름
if (currentStatus.value === 'CatchMind') {
    statusInfo.value = "캐치마인드"
    cameraHeight.value = 160
    cameraWidth.value = 266
  } else if (currentStatus.value === 'FreeTalk') {
    statusInfo.value = "자유 대화 시간"
    cameraHeight.value = 260
    cameraWidth.value = 462
  }

// watch로 현재 진행중인 프로그램에 따라 변수 변경
watch(currentStatus.value, ()=> {
  if (currentStatus.value === 'CatchMind') {
    statusInfo.value = "캐치마인드"
    cameraHeight.value = 160
    cameraWidth.value = 266
  } else if (currentStatus.value === 'FreeTalk') {
    statusInfo.value = "자유 대화 시간"
    cameraHeight.value = 260
    cameraWidth.value = 462
  }
})

// 동물상 가면 설정
class BasicScene {
  scene: THREE.Scene
  width: number
  height: number
  camera: THREE.PerspectiveCamera
  renderer: THREE.WebGLRenderer
  controls: OrbitControls
  lastTime: number = 0

  constructor() {
    this.height = cameraHeight.value
    this.width = cameraWidth.value
    this.scene = new THREE.Scene()
    this.camera = new THREE.PerspectiveCamera(60, this.width / this.height, 0.01, 5000)
    this.renderer = new THREE.WebGLRenderer({ antialias: true })
    this.renderer.setSize(this.width, this.height)
    this.renderer.domElement.setAttribute('id', 'threeCanvas')
    const threeCanvas: any = document.getElementById('three-canvas-box')
    threeCanvas.appendChild(this.renderer.domElement)
    // 화면에서 canvas 공간 없애기
    const canvas: any = document.getElementById('threeCanvas')
    canvas.style.visibility = "hidden"
    canvas.style.position = "absolute"
    
    // 조명 추가
    const ambientLight = new THREE.AmbientLight(0xffffff, 2)
    this.scene.add(ambientLight)
    const directionalLight = new THREE.DirectionalLight(0xffffff, 2)
    directionalLight.position.set(0, 10, 0)
    this.scene.add(directionalLight)

    // 카메라 설정
    this.camera.position.z = 0;
    this.controls = new OrbitControls(this.camera, this.renderer.domElement)
    this.controls.enabled = false
    let orbitTarget = this.camera.position.clone()
    orbitTarget.z -= 5
    this.controls.target = orbitTarget
    this.controls.update()

    // 재생되는 비디오로 백그라운드 설정
    const video = document.getElementById('landmark-video') as HTMLVideoElement
    const inputFrameTexture = new THREE.VideoTexture(video)
    inputFrameTexture.colorSpace = THREE.SRGBColorSpace
    const inputFramesDepth = 500  // 카메라와 3D 평면의 거리
    const inputFramesPlane = createCameraPlaneMesh(
      this.camera,
      inputFramesDepth,
      new THREE.MeshBasicMaterial({ map: inputFrameTexture })
    )
    this.scene.add(inputFramesPlane)
    this.render()

    // 가면 벗겨진 여부에 따라 카메라 토글
    watch(is_mask_loaded, () => { 
      if (is_mask_loaded.value === false) {
        this.scene.remove(inputFramesPlane)
      } else {
        this.scene.add(inputFramesPlane)
      }
    })

    // 렌더가 끝났다면 세션 연결 시작하기
    startSession()
  }
  
    // 실제 랜더 함수
    render(time: number = this.lastTime): void {
      this.lastTime = time
      this.renderer.render(this.scene, this.camera)
      requestAnimationFrame((t) => this.render(t))
  }
}

interface MatrixRetargetOptions {
  decompose?: boolean
  scale?: number
}

// 아바타 설정
class Avatar {
  scene: THREE.Scene
  loader: GLTFLoader = new GLTFLoader()
  gltf: GLTF
  root: THREE.Bone
  morphTargetMeshes: THREE.Mesh[] = []
  url: string

  constructor(url: string, scene: THREE.Scene) {
    this.url = url
    this.scene = scene
    this.loadModel(this.url)
  }

  // 모델 로드
  loadModel(url: string) {
    this.url = url
    this.loader.load(
      url,
      (gltf) => {
        if (this.gltf) {
          this.gltf.scene.remove()
          this.morphTargetMeshes = []
        }
        this.gltf = gltf
        this.scene.add(gltf.scene)
        this.init(gltf)
      },
      (progress) =>
        console.log(
          "가면 로딩중",
          100.0 * (progress.loaded / progress.total),
          "%"
        ),
      (error) => console.error(error)
    )
  }

  // gltf 객체 순회 및 초기화
  init(gltf: GLTF) {
    gltf.scene.traverse((object) => {
      if ((object as THREE.Bone).isBone && !this.root) {
        this.root = object as THREE.Bone
      }
      if (!(object as THREE.Mesh).isMesh) {
        return
      }
      
      const mesh = object as THREE.Mesh
      mesh.frustumCulled = false

      if (!mesh.morphTargetDictionary || !mesh.morphTargetInfluences) {
        return
      }
      this.morphTargetMeshes.push(mesh)
    })
  }

  // blendshapes 따라 아바타 모양 조절
  updateBlendshapes(blendshapes: Map<string, number>) {
    for (const mesh of this.morphTargetMeshes) {
      if (!mesh.morphTargetDictionary || !mesh.morphTargetInfluences) {
        continue
      }
      for (const [name, value] of blendshapes) {
        if (!Object.keys(mesh.morphTargetDictionary).includes(name)) {
          continue
        }

        const idx = mesh.morphTargetDictionary[name]
        mesh.morphTargetInfluences[idx] = value
      }
    }
  }

  applyMatrix(
    matrix: THREE.Matrix4,
    matrixRetargetOptions?: MatrixRetargetOptions
  ): void {
    const { decompose = false, scale = 1 } = matrixRetargetOptions || {}
    if (!this.gltf) {
      return
    }
    matrix.scale(new THREE.Vector3(scale, scale, scale))
    this.gltf.scene.matrixAutoUpdate = false
    this.gltf.scene.matrix.copy(matrix)
  }
}

let faceLandmarker: FaceLandmarker | null = null
let video: HTMLVideoElement | null = null
let avatar: Avatar | null = null

// 각각의 가면들 주소 할당
const bear = "/assets/animal_mask/bear/scene.gltf"
const cat = "/assets/animal_mask/cat/scene.gltf"
const deer = "/assets/animal_mask/deer/scene.gltf"
const dino = "/assets/animal_mask/dino/scene.gltf"
const dog = "/assets/animal_mask/dog/scene.gltf"
const penguin = "/assets/animal_mask/penguin/scene.gltf"
const rabbit = "/assets/animal_mask/rabbit/scene.gltf"
const raccoon = "/assets/animal_mask/raccoon_head.glb"

async function init() {
  const scene = ref<BasicScene | null>(null)
  scene.value = new BasicScene()

  // 이 앞에 자료형 붙이지 말것. 에러의 주 원인
  avatar = ref<any>(null)
  const maskURL = ref<any>('')
  const animal = store.userInfo?.animal

  // 가면 바꾸는 변수
  if (animal === '강아지') {
    maskURL.value = dog
  } else if (animal === '고양이') {
    maskURL.value = cat
  } else if (animal === '곰') {
    maskURL.value = bear
  } else if (animal === '공룡') {
    maskURL.value = dino
  } else if (animal === '펭귄') {
    maskURL.value = penguin
  } else if (animal === '토끼') {
    maskURL.value = rabbit
  } else if (animal === '사슴') {
    maskURL.value = deer
  } else {
    maskURL.value = raccoon
  }

  avatar.value = new Avatar(
    maskURL.value,
    scene.value.scene
  )

  runDemo()
}

async function runDemo() {
  await streamWebcamThroughFaceLandmarker();
  const vision = await FilesetResolver.forVisionTasks(
    "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.1.0-alpha-16/wasm"
  )
  faceLandmarker = await FaceLandmarker.createFromModelPath(
    vision,
    "https://storage.googleapis.com/mediapipe-models/face_landmarker/face_landmarker/float16/latest/face_landmarker.task"
  );
  await faceLandmarker.setOptions({
    baseOptions: {
      delegate: "GPU"
    },
    runningMode: "VIDEO",
    outputFaceBlendshapes: true,
    outputFacialTransformationMatrixes: true,
  })
}

// 평면 mesh 생성
function createCameraPlaneMesh(
  camera: THREE.PerspectiveCamera,
  depth: number,
  material: THREE.Material
): THREE.Mesh {
  const viewportSize = getViewportSizeAtDepth(camera, depth)
  const cameraPlaneGeometry = new THREE.PlaneGeometry(
    viewportSize.width,
    viewportSize.height
  )
  cameraPlaneGeometry.translate(0, 6, -depth);

  return new THREE.Mesh(cameraPlaneGeometry, material)
}

function getViewportSizeAtDepth(
  camera: THREE.PerspectiveCamera,
  depth: number
): THREE.Vector2 {
  const viewportHeightAtDepth =
    2 * depth * Math.tan(THREE.MathUtils.degToRad(0.5 * camera.fov))
  const viewportWidthAtDepth = viewportHeightAtDepth * camera.aspect
  return new THREE.Vector2(viewportWidthAtDepth, viewportHeightAtDepth)
}

function detectFaceLandmarks(time: DOMHighResTimeStamp): void {
  if (!faceLandmarker || !avatar) {
    return
  }
  const landmarks = faceLandmarker.detectForVideo(video, time)

  const transformationMatrices = landmarks.facialTransformationMatrixes
  if (transformationMatrices && transformationMatrices.length > 0) {
    let matrix = new THREE.Matrix4().fromArray(
      transformationMatrices[0].data
      )
      
      // 가면 위아래 위치 
      // 너구리 가면은 적용되지 않음.
      if (avatar.value.url !== '/assets/animal_mask/raccoon_head.glb') {
        avatar.value.scene.position.y = -6
      }
      avatar.value?.applyMatrix(matrix, { scale: 50 })
  }
  const blendshapes = landmarks.faceBlendshapes

  is_mask_loaded.value = false
  
  if (blendshapes && blendshapes.length > 0) {
    is_mask_loaded.value = true
    const coefsMap = retarget(blendshapes);
    avatar.value?.updateBlendshapes(coefsMap);
  }
}

// 표정 변화 (현재는 너구리 가면만 적용)
function retarget(blendshapes: Classifications[]) {
  const categories = blendshapes[0].categories
  let coefsMap = new Map<string, number>()
  for (let i = 0; i < categories.length; ++i) {
    const blendshape = categories[i]
    switch (blendshape.categoryName) {
      case "browOuterUpLeft":
        blendshape.score *= 1.2
        break
      case "browOuterUpRight":
        blendshape.score *= 1.2
        break
      case "eyeBlinkLeft":
        blendshape.score *= 1.2
        break
      case "eyeBlinkRight":
        blendshape.score *= 1.2
        break
      default:
    }
    coefsMap.set(categories[i].categoryName, categories[i].score)
  }
  return coefsMap
}

function onVideoFrame(time: DOMHighResTimeStamp): void {
  detectFaceLandmarks(time)
  video?.requestVideoFrameCallback(onVideoFrame)
}

// video 요소 할당 후 플레이
async function streamWebcamThroughFaceLandmarker(): Promise<void> {
  video = document.getElementById('landmark-video') as HTMLVideoElement | null

  function onAcquiredUserMedia(stream: MediaStream): void {
    video!.srcObject = stream
    video!.onloadedmetadata = () => {
      video!.play()
    }
  }

  try {
    const evt = await navigator.mediaDevices.getUserMedia({
      audio: false,
      video: {
        facingMode: "user",
        width: 1280,
        height: 720
      }
    })
    onAcquiredUserMedia(evt)
    video!.requestVideoFrameCallback(onVideoFrame)
  } catch (e: unknown) {
      window.alert('에러가 발생했어요. 비디오 접근 권한을 확인해주세요')
  }
}


// 세션 연결
// 현재 참가중인 동물 목록
const currentAnimals = ref([])

// 현재 채팅 정보 (세션별로 관리됨)
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
const drawData = ref(undefined)
const currentDrawingUserId = ref(undefined)


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

  // 채팅 이벤트 발생시
  session.value.on('signal:chat', (event) => {
    const fromData = JSON.parse(event.from.data)
    const chatData: any = {
      'animal': fromData.animal,
      'nickname': fromData.nickname,
      'gender': fromData.gender,
      'message': event.data
    }
    currentChat.value.push(chatData)
  });

  session.value.on('signal:drawing', (event) => {
    drawData.value = JSON.parse(event.data);
  })

  session.value.on('signal:drawingStart', (event) => {
    const data = JSON.parse(event.data);
    currentDrawingUserId.value = data.userId;
  });

  session.value.on('signal:drawingEnd', (event) => {
    console.log(event.data)
    console.log(currentDrawingUserId.value)
    const data = JSON.parse(event.data);
    if (currentDrawingUserId.value === data.userId) {
      currentDrawingUserId.value = undefined
    }
  })

  // 현재 참가중인 동물목록에 자신 넣어주기
  currentAnimals.value.push(myUserAnimal.value)
  
  const canvasEl = document.getElementById('threeCanvas')
  const videoTrack = canvasEl.captureStream(30).getVideoTracks()[0]

  const token = ref<any>('')
  token.value = store.meetingRoomToken

  // 유저 토큰 이용해 세션과 연결하기
  // 첫번째 인자는 토큰, 두번째 인자는 모든 유저에 의해 검색 가능
  // 'streamCreated'(Stream.connection.data의 속성), 유저 닉네임으로 DOM에 추가됨
  session.value.connect(token.value, { nickname: myUserName.value, animal: myUserAnimal.value, gender: myGender.value }).then(() => {

    // 실제 publish 하는 부분, 이 부분에서 카메라와 오디오 소스 설정 가능
    let pub = OV.value.initPublisher(undefined, {
      audioSource: undefined,
      videoSource: videoTrack,
      publishAudio: true,
      publishVideo: true,
      resolution: "640x480",
      frameRate: 30,
      insertMode: "APPEND",
      mirror: false,
    });
    pub.stream.typeOfVideo = "CUSTOM"

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

  // 동물상 canvas 지우기
  const canvas: any = document.getElementById('threeCanvas')
  canvas.remove()

  // 세션을 이미 나갔으니 화면 나갈때의 이벤트리스트 제거하기
  window.removeEventListener("beforeunload", leaveSession);
}
</script>


<style scoped>
.main__container {
  @apply flex w-screen h-screen;
  min-width: 1420px;
  background-color: white;
  color: black;
}

.left-component {
  @apply flex-grow;
}

.right-component {
  width: 380px;
}

#landmark-video {
  display: none;
}

</style>