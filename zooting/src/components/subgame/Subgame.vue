<template>
  <div>
    <div class="canvas-size background-image" id="game">
      <div @click="startGame()">
        <div id="description" class="align-middle neon cursor-pointer blink">
          <div v-if="descriptionShow">
            {{descriptionText}}
          </div>
          <div id="result" v-if="resultShow" class="neon text-center">
            <p style='font-size : 20px;'> Your score is {{ score }}</p>
            <p style='font-size : 18px;'> points + {{ points }} </p>
          </div>
        </div>
      </div>
      <span id="record" class="mt-3 ml-3 neon-mini" v-if="!resultShow && !descriptionShow"> score {{score}}</span>
      <canvas id="canvas" class="z-50"></canvas>
    </div>
  </div>
</template>

<script setup lang="ts">
import StopDogImg from '/assets/images/sub_game/stopdog.png';
import RunningDogImg from '/assets/images/sub_game/runningdog.png';
import HamburgerImg from '/assets/images/sub_game/obstacle_hamburger.png';
import PotatoImg from '/assets/images/sub_game/obstacle_potato.png';
import PizzaImg from '/assets/images/sub_game/obstacle_pizza.png';
import { playJumpSound, playCollisionSound} from "@/components/subgame/sound";
import { addPointsApi} from "@/api/subgame";
import {ref, nextTick} from "vue";
import 'animate.css'
const descriptionText = ref('Click to Start')
const descriptionShow = ref(true);
const resultShow = ref(false);
const points = ref(0);

let canvas:any;
let ctx:any;


const gameState = ref(0); // 0 : end , 1: start, 3: gameover
let timer = 0;

let jumpState = 0; // 0 : default, 1 : jump
let jumpTimer = 0;
let rightMoveState = 0;
let leftMoveState = 0;
let animation:number ;
const score = ref(0);
let detectedCollision = false;

const startGame = function () {
  initGame(); // 캔버스, 변수 초기화
  gameState.value = 1; // 게임 실행
  frameAction();
}



// 게임 재시작시 초기화 함수
function initGame() {
  // text 없애기
  descriptionShow.value = false;
  resultShow.value = false;

  // 캔버스 그리기
  canvas = document.querySelector("#canvas") as HTMLCanvasElement
  if (!canvas) {
    return;
  }
  ctx = canvas.getContext('2d');
  canvas.width = 600; // 300 - canvas 너비
  canvas.height =200;   //215

  // 초기화할 변수들
  timer = 0;
  obstacleArr = [];
  jumpState = 0;
  jumpTimer = 0;
  score.value = 0;
  detectedCollision = false;
  flag = false;
  runner.x = 0;
  dogImg.src = StopDogImg;
  points.value = 0;
}

// frame 단위로 그리기
let obstacleArr:Obstacle[] = [];
let flag = false;
function frameAction () {
  if (!canvas){
    return;
  }
  animation = requestAnimationFrame(frameAction);
  if (!ctx) {
    return;
  }
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  timer++;    // 한 프레임 당 timer += 1


  // timer가 200이 될 때 장애물 추가
  if (timer % (200 + getRandomInt(-3, 3)) == 0 ) {
    let obstacle = new Obstacle();
    obstacleArr.push(obstacle);
  }

  if (score.value>= 30 && score.value <=50) {
    // descriptionShow.value = true;
    // descriptionText.value="Speed Up"
  } else {
    descriptionShow.value = false;
  }

  // 한 프레임마다 출력된 장애물 객체의 위치, 충돌 여부 확인
  obstacleArr.forEach((a, idx, o)=>{
    // 화면 왼쪽으로 사라지는 객체 삭제
    if(a.x < 0) {
      o.splice(idx, 1);
      score.value += 10;
    }
    // 충돌 확인 - 충돌하지 않았다면 객체 위치 왼쪽으로 1씩 이동
    if (! detectedCollision) {
      collisionDetection(runner, a);
      if (score.value > 30) {
        a.x -= 2;
      }else {
        a.x --;
      }
      a.draw();
    }
  })
  // 바닥 그리기
  // drawLine();
  // 캐릭터 그리기
  runner.draw();

  // 좌우 이동
  if (rightMoveState == 1 && runner.x < 600 - runner.width) {
    if (jumpState == 0) {
      runner.x += 1;
    }else {
      runner.x += 0.7            }
  }
  else if (leftMoveState == 1 && runner.x > 0) {
    if (jumpState == 0) {
      runner.x -= 1
    } else {
      runner.x -= 0.7
    }
  }


  // 점프 - 점프 시간 동안 jumptimer 값 상승
  if(jumpState == 1){ // 점프 상태일 때 타이머가 50이 될때까지 올라간다.
    jumpTimer++;
    if (jumpTimer <= 30) { // 30프레임까지는 올라감
      runner.y -= 4;
    } else{ // 그 이후에는 내려옴
      if (runner.y + 3< 160) {
        runner.y += 3;
      }
    }
  }
  if(jumpState == 2) {
    jumpTimer ++ ;
    if (jumpTimer <= 20) { // 30프레임까지는 올라감
      runner.y -= 4;
    } else { // 그 이후에는 내려옴
      if (runner.y + 3 <= 160) {
        runner.y += 3;
      }
    }

  }
  // jumptimer 시간이 40이 넘었다면 jumpstate, jumptimer 값 초기화
  if(jumpTimer > 70){ // jump 시간이 50이 되면 시간 초기화, 점프 아닌 상태로 변경
    jumpState = 0;
    jumpTimer = 0;
  }
  // jumpstate가 0인데 공중에 있는 경우 내려온다
  if(jumpState == 0){
    if(runner.y + 3< 160){
      runner.y += 3 ;
    }
  }
}

// 충돌 시 게임 종료
function collisionDetection(runner:any, obstacle:any) {
  let xValue = 0;
  let yValue = obstacle.y - ( runner.y + runner.height );

  if (obstacle.x > runner.x) {
    xValue = obstacle.x - ( runner.x + runner.width );
  }else {
    xValue = runner.x - (obstacle.x + obstacle.width) ;
  }
  if (detectedCollision) {
    return;
  }
  if( xValue < 0 && yValue < 0 ){ // 충돌!
    if (!animation || !ctx) {
      return;
    }
    console.log(xValue, yValue)
    cancelAnimationFrame(animation);
    playCollisionSound();
    detectedCollision = true;
    setTimeout(()=> {
      if (!ctx) {return;}
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      gameState.value = 3;
      descriptionText.value = "Try again?"
      descriptionShow.value = true;
      resultShow.value = true;
    }, 0)
    if (score.value != 0) {
      points.value = score.value / 10
      // Vue.js의 nextTick 메소드를 사용하여 값이 업데이트될 때까지 대기
      nextTick(() => {
        addPoints(points.value);
      });
    }
  }
}

// 스페이스 누를 때 점프 및 이미지 변경
document.addEventListener('keypress', (e)=>{
  if (e.code == 'Space'){
    if (gameState.value == 1 && runner.y > 150 && jumpState != 2) {
      jumpState = 1; // 점프 중으로 변경
      playJumpSound();
      dogImg.src = RunningDogImg;
      runner.draw();
    }
    else if (gameState.value == 1 && jumpState == 1) {
      jumpState = 2; // 2단점프
      jumpTimer = 0;
      runner.y -= 3;
      playJumpSound();
      dogImg.src = RunningDogImg;
      runner.draw();
    }
  }
})

document.addEventListener('keyup', (e)=>{
  if (e.code == 'Space'){
    if (gameState.value == 1) { // 게임실행 중일 때 스페이스 누르면
      dogImg.src = StopDogImg;
      runner.draw();
    }
  }
})


document.addEventListener('keydown', (e)=>{
  if (e.code == 'ArrowRight'){
    if (gameState.value == 1) { // 게임실행 중일 때 스페이스 누르면
      rightMoveState = 1;
    }
  }
  else if (e.code == 'ArrowLeft') {
    if (gameState.value == 1) {
      leftMoveState = 1;
    }
  }
})
document.addEventListener('keyup', (e)=>{
  if (e.code == 'ArrowRight'){
    rightMoveState = 0;
  }
  else if (e.code == 'ArrowLeft') {
    leftMoveState = 0;
  }
})

// 캐릭터
let dogImg = new Image(); // 이후 수정
dogImg.src = StopDogImg;
let runner = {
  x: 0,
  y: 160,
  width: 40,
  height: 30,
  draw(){
    if (! ctx){
      return;
    }
    ctx.drawImage(dogImg, this.x, this.y, this.width, this.height);
  }
}

// 장애물
let hamburgerImg = new Image();
hamburgerImg.src = HamburgerImg;
let potatoImg = new Image();
potatoImg.src = PotatoImg;
let pizzaImg = new Image();
pizzaImg.src = PizzaImg;
let obstacleImgArr:any[] = [hamburgerImg, potatoImg, pizzaImg];

class Obstacle {
  width:number;
  height:number;
  x:number;
  y:number;
  image:any;
  speed:number;
  constructor() {
    this.width = 32 + getRandomInt(-10, 6);
    this.height = 30 + getRandomInt(-10, 6);
    this.x = 560; // 가장 끝에서 약 30만큼 전
    this.y = 190 - this.height; // 땅위치에서 높이만큼 위에서 시작
    this.image = obstacleImgArr[getRandomInt(0, 3)];
    this.speed = 3 + getRandomInt(-2, 2)
  }
  draw() {
    if (! ctx) {
      return;
    }
    ctx.drawImage(this.image, this.x, this.y, this.width, this.height);
  }
}
// 바닥 그리는 함수
function drawLine () {
  if (!ctx) {
    return;
  }
  //바닥선
  ctx.beginPath();
  ctx.moveTo(0, 190);
  ctx.lineTo(600, 190);
  ctx.stroke();
}
const addPoints = async (points:number) => {
  console.log(points );
  await addPointsApi(
      {points : points as number},
      ({data}: any) => {
        if (data.status == 200 || data.status == 201) {
          console.log("포인트 추가 성공")
        }
      },
      (error: any) => {
        console.log("포인트 증가 실패")
      })
}
// 랜덤 수 추출
function getRandomInt(min:number, max:number) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min)) + min;

}


</script>

<style scoped>
.canvas-size {
  @apply border-[#E1BAFF] border-4 rounded-lg shadow-lg m-5;
  position: relative;
  width : 600px;
  height: 200px;
}
.background-image {
  background: url("/assets/images/sub_game/background.gif") 0 center / 200px 600px repeat-x;
  background-size: 600px 200px;
  animation: movebg 40s linear infinite;

}
@keyframes movebg {
  0% {background-position: 600px center;}
  100% {background-position: 0 center;}
}
.blink {
  animation: blink-effect 1s step-end infinite;
}

@keyframes blink-effect {
  80% {
    opacity: 0;
  }
}

#canvas {
  width: 100%;
  height: 90%;
}
#description {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 40px;
  font-weight: bolder;
}

#record {
  font-size: 20px;
  font-weight: bolder;
}
.neon {
  color: #fff;
  text-shadow: 0 0 5px #8b80cc, 0 0 10px #fff, 0 0 20px #ff0080, 0 0 30px #ff0080, 0 0 40px #ff0080, 0 0 55px #ff0080, 0 0 75px #ff0080;
}
.neon-mini {
  color: #fff;
  text-shadow: 0 0 1px #8b80cc, 0 0 2px #fff, 0 0 4px #ff0080, 0 0 10px #ff0080, 0 0 20px #ff0080, 0 0 30px #ff0080, 0 0 50px #ff0080;}
</style>