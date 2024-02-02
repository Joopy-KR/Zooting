<template>
  <div id="conatiner" class="container">
    <!-- style="overflow: hidden;" -->
    <div v-show="is_loaded">
      <video id="video" ref="videoRef" autoplay playsinline></video>
    </div>
    
    <div class="flex flex-col items-center justify-center" id="loading" v-show="!is_loaded" style="background-color: black; width: 533px; height: 300px;">
      <p class="mb-8 text-5xl font-bold text-white">가면 벗겨짐</p>
      <h3 class="mb-5 text-white">카메라에 얼굴을 맞춰주세요</h3>
      <div class="flex items-center justify-center" role="status">
        <svg aria-hidden="true" class="inline w-3/12 text-gray-200 animate-spin dark:text-gray-600 fill-pink-400" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"/>
          <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"/>
        </svg>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts" type="module">
import { ref, onMounted, onUnmounted,watch } from 'vue'
import * as THREE from 'three'
import { OrbitControls } from 'three/addons/controls/OrbitControls.js'
import { GLTFLoader, GLTF } from 'three/addons/loaders/GLTFLoader.js'
import vision from "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.3"
const { FaceLandmarker, FilesetResolver } = vision

// 라우터 떠날때 캔버스 초기화 하기
onUnmounted(() => {
  const canvas: any = document.querySelector("canvas")
  canvas.remove()
})

const props = defineProps<{
  streamManager: any
}>()

const videoRef = ref();

// 로딩 되었는지 판단할 변수
const is_loaded = ref(false)

class BasicScene {
  scene: THREE.Scene
  width: number
  height: number
  camera: THREE.PerspectiveCamera
  renderer: THREE.WebGLRenderer
  controls: OrbitControls
  lastTime: number = 0

  constructor() {
    // const containerElement = document.getElementById("conatiner")
    // this.height = window.innerHeight;
    this.height = 140
    this.width = (this.height * 1920) / 1080
    this.scene = new THREE.Scene()
    this.camera = new THREE.PerspectiveCamera(60, this.width / this.height, 0.01, 5000)
    this.renderer = new THREE.WebGLRenderer({ antialias: true })
    this.renderer.setSize(this.width, this.height)
    document.body.appendChild(this.renderer.domElement)

    // 부모 엘리먼트.. 이 부분 다시 자세하게 봐보자!
    // // 부모 엘리먼트 찾기
    // let containerElement: any = document.getElementById("conatiner");
    // let parentElement = this.renderer.domElement.parentElement;

    // if (parentElement) {
    //   containerElement = parentElement;
    //   console.log(containerElement);
    // } else {
    //   console.error("부모 엘리먼트를 찾을 수 없습니다.");
    // }
    
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
    const video = document.getElementById("video") as HTMLVideoElement
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
    
    // 가면이 벗겨지면 카메라 끄기
    const canvas: any = document.querySelector("canvas")
    watch(is_loaded, () => {
      if (is_loaded.value === false) {
        canvas.style.visibility = "hidden"
      } else {
        canvas.style.visibility = "visible"
      }
    })
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

onMounted(() => {
  init()
  props.streamManager.addVideoElement(videoRef.value);
});

// 각각의 가면들 주소 할당
const bear = "src/assets/animal_mask/bear/scene.gltf"
const cat = "src/assets/animal_mask/cat/scene.gltf"
const deer = "src/assets/animal_mask/deer/scene.gltf"
const dino = "src/assets/animal_mask/dino/scene.gltf"
const dog = "src/assets/animal_mask/dog/scene.gltf"
const penguin = "src/assets/animal_mask/penguin/scene.gltf"
const rabbit = "src/assets/animal_mask/rabbit/scene.gltf"
const raccoon = "src/assets/animal_mask/raccoon_head.glb"


async function init() {
  const scene = ref<BasicScene | null>(null)
  scene.value = new BasicScene()

  avatar = ref<Avatar | null>(null)

  const maskURL = ref<any>('')

  // 가면 바꾸는 변수
  maskURL.value = dog 

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
  );
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
      if (avatar.value.url !== 'src/assets/animal_mask/raccoon_head.glb' ) {
        avatar.value.scene.position.y = -6
      }
      
      avatar.value?.applyMatrix(matrix, { scale: 50 })
  }
  const blendshapes = landmarks.faceBlendshapes

  is_loaded.value = false
  
  if (blendshapes && blendshapes.length > 0) {
    is_loaded.value = true
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
  video = document.getElementById("video") as HTMLVideoElement | null

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
      window.alert('비디오 접근 권한을 허용해주세요')
  }
}
</script>



<style>
canvas {
  transform: scaleX(-1);
  /* z-index: 0; */
  /* margin-inline-start: 3.5rem; */
}
</style>

<style scoped>

video {
  display: none;
}

</style>
