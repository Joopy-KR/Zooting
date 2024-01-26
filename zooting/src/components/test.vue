<template>
  <div class="container">
    <video id="video" ref="video" autoplay playsinline></video>
  </div>
</template>

<script setup lang="ts" type="module">
import { ref, onMounted, onUnmounted } from 'vue';
import * as THREE from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { GLTFLoader, GLTF } from 'three/addons/loaders/GLTFLoader.js';
import vision from "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.3";
const { FaceLandmarker, FilesetResolver } = vision;


class BasicScene {
  scene: THREE.Scene;
  width: number;
  height: number;
  camera: THREE.PerspectiveCamera;
  renderer: THREE.WebGLRenderer;
  controls: OrbitControls;
  lastTime: number = 0;

  constructor() {
    this.height = window.innerHeight;
    this.width = (this.height * 1280) / 720;
    this.scene = new THREE.Scene();
    this.camera = new THREE.PerspectiveCamera(60, this.width / this.height, 0.01, 5000);
    this.renderer = new THREE.WebGLRenderer({ antialias: true });
    this.renderer.setSize(this.width, this.height);
    THREE.ColorManagement.legacy = false
    this.renderer.outputEncoding = THREE.sRGBEncoding;
    document.body.appendChild(this.renderer.domElement);

    // Set up the basic lighting for the scene
    const ambientLight = new THREE.AmbientLight(0xffffff, 1.5);
    this.scene.add(ambientLight);
    const directionalLight = new THREE.DirectionalLight(0xffffff, 1.5);
    directionalLight.position.set(0, 1, 0);
    this.scene.add(directionalLight);
    console.log(this.scene)

    // Set up the camera position and controls
    this.camera.position.z = 0;
    this.controls = new OrbitControls(this.camera, this.renderer.domElement);
    this.controls.enabled = false
    let orbitTarget = this.camera.position.clone();
    orbitTarget.z -= 5;
    this.controls.target = orbitTarget;
    this.controls.update();

    // Add a video background
    const video = document.getElementById("video") as HTMLVideoElement
    const inputFrameTexture = new THREE.VideoTexture(video);
    if (!inputFrameTexture) {
      throw new Error("Failed to get the 'input_frame' texture!");
    }
    inputFrameTexture.encoding = THREE.sRGBEncoding;
    const inputFramesDepth = 500;
    const inputFramesPlane = createCameraPlaneMesh(
      this.camera,
      inputFramesDepth,
      new THREE.MeshBasicMaterial({ map: inputFrameTexture })
    );
    this.scene.add(inputFramesPlane);

    // Render the scene
    this.render();

    window.addEventListener("resize", this.resize.bind(this));
  }

  resize() {
    this.width = window.innerWidth;
    this.height = window.innerHeight;
    this.camera.aspect = this.width / this.height;
    this.camera.updateProjectionMatrix();

    this.renderer.setSize(this.width, this.height);
    this.renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));

    this.renderer.render(this.scene, this.camera);
  }

  render(time: number = this.lastTime): void {
    const delta = (time - this.lastTime) / 1000;
    this.lastTime = time;
    this.renderer.render(this.scene, this.camera);
    requestAnimationFrame((t) => this.render(t));
  }
}

interface MatrixRetargetOptions {
  decompose?: boolean;
  scale?: number;
}

class Avatar {
  scene: THREE.Scene;
  loader: GLTFLoader = new GLTFLoader();
  gltf: GLTF;
  root: THREE.Bone;
  morphTargetMeshes: THREE.Mesh[] = [];
  url: string;

  constructor(url: string, scene: THREE.Scene) {
    this.url = url;
    this.scene = scene;
    this.loadModel(this.url);
  }

  loadModel(url: string) {
    this.url = url;
    this.loader.load(
      url,
      (gltf) => {
        if (this.gltf) {
          this.gltf.scene.remove();
          this.morphTargetMeshes = [];
        }
        this.gltf = gltf;
        console.log();
        this.scene.add(gltf.scene);
        this.init(gltf);
      },
      (progress) =>
        console.log(
          "Loading model...",
          100.0 * (progress.loaded / progress.total),
          "%"
        ),
      (error) => console.error(error)
    );
  }

  init(gltf: GLTF) {
    gltf.scene.traverse((object) => {
      if ((object as THREE.Bone).isBone && !this.root) {
        this.root = object as THREE.Bone;
        console.log(object);
      }
      if (!(object as THREE.Mesh).isMesh) {
        return;
      }

      const mesh = object as THREE.Mesh;
      mesh.frustumCulled = false;

      if (!mesh.morphTargetDictionary || !mesh.morphTargetInfluences) {
        return;
      }
      this.morphTargetMeshes.push(mesh);
    });
  }

  updateBlendshapes(blendshapes: Map<string, number>) {
    for (const mesh of this.morphTargetMeshes) {
      if (!mesh.morphTargetDictionary || !mesh.morphTargetInfluences) {
        continue;
      }
      for (const [name, value] of blendshapes) {
        if (!Object.keys(mesh.morphTargetDictionary).includes(name)) {
          continue;
        }

        const idx = mesh.morphTargetDictionary[name];
        mesh.morphTargetInfluences[idx] = value;
      }
    }
  }

  applyMatrix(
    matrix: THREE.Matrix4,
    matrixRetargetOptions?: MatrixRetargetOptions
  ): void {
    const { decompose = false, scale = 1 } = matrixRetargetOptions || {};
    if (!this.gltf) {
      return;
    }
    matrix.scale(new THREE.Vector3(scale, scale, scale));
    this.gltf.scene.matrixAutoUpdate = false;
    this.gltf.scene.matrix.copy(matrix);
  }

  offsetRoot(offset: THREE.Vector3, rotation?: THREE.Vector3): void {
    if (this.root) {
      this.root.position.copy(offset);
      if (rotation) {
        let offsetQuat = new THREE.Quaternion().setFromEuler(
          new THREE.Euler(rotation.x, rotation.y, rotation.z)
        );
        this.root.quaternion.copy(offsetQuat);
      }
    }
  }
}

let faceLandmarker: FaceLandmarker | null = null;
let video: HTMLVideoElement | null = null;
let avatar: Avatar | null = null;

onMounted(() => {
  init();
});


async function init() {
  const scene = ref<BasicScene | null>(null);
  scene.value = new BasicScene();

  avatar = ref<Avatar | null>(null); // 전역 avatar에 할당
  avatar.value = new Avatar(
    // "https://assets.codepen.io/9177687/raccoon_head.glb",
    "https://assets.codepen.io/9177687/raccoon_head.glb",
    scene.value.scene
  );

  video = document.getElementById("video") as HTMLVideoElement | null;

  if (!video) {
    throw new Error("Video element not found");
  }


  const inputFrameTexture = new THREE.VideoTexture(video);
  if (!inputFrameTexture) {
    throw new Error("Failed to get the 'input_frame' texture!");
  }
  inputFrameTexture.encoding = THREE.sRGBEncoding;

  const inputFramesDepth = 500;
  const inputFramesPlane = createCameraPlaneMesh(
    scene.value.camera,
    inputFramesDepth,
    new THREE.MeshBasicMaterial({ map: inputFrameTexture })
  );
  scene.value.scene.add(inputFramesPlane);

  video.onloadedmetadata = () => {
    video?.play();
  };

  window.addEventListener("resize", scene.value?.resize.bind(scene.value));

  runDemo();
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
  });

  console.log("Finished Loading MediaPipe Model.");
}

function createCameraPlaneMesh(
  camera: THREE.PerspectiveCamera,
  depth: number,
  material: THREE.Material
): THREE.Mesh {
  if (camera.near > depth || depth > camera.far) {
    console.warn("Camera plane geometry will be clipped by the `camera`!");
  }
  const viewportSize = getViewportSizeAtDepth(camera, depth);
  const cameraPlaneGeometry = new THREE.PlaneGeometry(
    viewportSize.width,
    viewportSize.height
  );
  cameraPlaneGeometry.translate(0, 0, -depth);

  return new THREE.Mesh(cameraPlaneGeometry, material);
}

function getViewportSizeAtDepth(
  camera: THREE.PerspectiveCamera,
  depth: number
): THREE.Vector2 {
  const viewportHeightAtDepth =
    2 * depth * Math.tan(THREE.MathUtils.degToRad(0.5 * camera.fov));
  const viewportWidthAtDepth = viewportHeightAtDepth * camera.aspect;
  return new THREE.Vector2(viewportWidthAtDepth, viewportHeightAtDepth);
}

function detectFaceLandmarks(time: DOMHighResTimeStamp): void {
  if (!faceLandmarker || !avatar) {
    return;
  }
  const landmarks = faceLandmarker.detectForVideo(video, time);

  const transformationMatrices = landmarks.facialTransformationMatrixes;
  if (transformationMatrices && transformationMatrices.length > 0) {
    let matrix = new THREE.Matrix4().fromArray(
      transformationMatrices[0].data
    );
    avatar.value?.applyMatrix(matrix, { scale: 40 });
  }

  const blendshapes = landmarks.faceBlendshapes;
  if (blendshapes && blendshapes.length > 0) {
    const coefsMap = retarget(blendshapes);
    avatar.value?.updateBlendshapes(coefsMap);
  }
}

function retarget(blendshapes: Classifications[]) {
  const categories = blendshapes[0].categories;
  let coefsMap = new Map<string, number>();
  for (let i = 0; i < categories.length; ++i) {
    const blendshape = categories[i];
    switch (blendshape.categoryName) {
      case "browOuterUpLeft":
        blendshape.score *= 1.2;
        break;
      case "browOuterUpRight":
        blendshape.score *= 1.2;
        break;
      case "eyeBlinkLeft":
        blendshape.score *= 1.2;
        break;
      case "eyeBlinkRight":
        blendshape.score *= 1.2;
        break;
      default:
    }
    coefsMap.set(categories[i].categoryName, categories[i].score);
  }
  return coefsMap;
}

function onVideoFrame(time: DOMHighResTimeStamp): void {
  detectFaceLandmarks(time);
  video?.requestVideoFrameCallback(onVideoFrame);
}

async function streamWebcamThroughFaceLandmarker(): Promise<void> {
  video = document.getElementById("video") as HTMLVideoElement | null;

  function onAcquiredUserMedia(stream: MediaStream): void {
    video!.srcObject = stream;
    video!.onloadedmetadata = () => {
      video!.play();
    };
  }

  try {
    const evt = await navigator.mediaDevices.getUserMedia({
      audio: false,
      video: {
        facingMode: "user",
        width: 1280,
        height: 720
      }
    });
    onAcquiredUserMedia(evt);
    video!.requestVideoFrameCallback(onVideoFrame);
  } catch (e: unknown) {
    console.error(`Failed to acquire camera feed: ${e}`);
  }
}
</script>

<style>
canvas {
  position: absolute;
  transform: scaleX(-1);
  z-index: 0;
  top: 0;
}
</style>

<style scoped>

video {
  display: none;
}

</style>
