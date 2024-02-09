import StopDogImg from '../../../public/assets/images/sub_game/stopdog.png';
import RunningDogImg from '../../../public/assets/images/sub_game/stopdog.png';
import HamburgerImg from '../../../public/assets/images/sub_game/obstacle_hamburger.png';
import PotatoImg from '../../../public/assets/images/sub_game/obstacle_potato.png';
import PizzaImg from '../../../public/assets/images/sub_game/obstacle_pizza.png';
import { playJumpSound, playCollisionSound} from "@/components/subgame/sound";


document.addEventListener('DOMContentLoaded', () => {
    let canvas = document.querySelector("#canvas") as HTMLCanvasElement
    if (!canvas) {
        return;
    }
    let ctx = canvas.getContext('2d');
    canvas.width = 600; // 300 - canvas 너비
    canvas.height =200;   //215


    // 캐릭터
    let dogImg = new Image(); // 이후 수정
    dogImg.src = StopDogImg;
    let runner = {
        x: 0,
        y: 160, // 땅에서 상자 크기만큼 위에서부터 그린다
        width: 40,
        height: 30,
        draw(){
            if (! ctx){
                return;
            }
            // ctx.fillStyle = 'green';
            // ctx.fillRect(this.x, this.y, this.width, this.height);
            ctx.drawImage(dogImg, this.x, this.y, this.width, this.height);
        }
    }

    let hamburgerImg = new Image();
    hamburgerImg.src = HamburgerImg;
    let potatoImg = new Image();
    potatoImg.src = PotatoImg;
    let pizzaImg = new Image();
    pizzaImg.src = PizzaImg;
    let obstacleImgArr:any[] = [hamburgerImg, potatoImg, pizzaImg];

    // 선인장 장애물
    class Obstacle {
        width:number;
        height:number;
        x:number;
        y:number;
        image:any;
        constructor() {
            this.width = 32 + getRandomInt(-10, 6);
            this.height = 30 + getRandomInt(-10, 6);
            this.x = 560; // 가장 끝에서 약 30만큼 전
            this.y = 190 - this.height; // 땅위치에서 높이만큼 위에서 시작
            this.image = obstacleImgArr[getRandomInt(0, 3)];
        }
        draw() {
            if (! ctx) {
                return;
            }
            ctx.drawImage(this.image, this.x, this.y, this.width, this.height);
        }
    }

    let obstacle = new Obstacle();

    function getRandomInt(min:number, max:number) {
        min = Math.ceil(min);
        max = Math.floor(max);
        return Math.floor(Math.random() * (max - min)) + min;

    }

    // 프레임 단위로 선인장들이 다가 오게 하는 함수
    let timer = 0;
    let obstacleArr:Obstacle[] = [];
    let gameState = 0; // 0 : end , 1: start, 3: gameover
    let jumpState = 0; // 0 : default, 1 : jump
    let jumpTimer = 0;
    let animation:number ;
    let score = 0;
    let detectedCollision = false;

    let scoreElement = document.querySelector('#score');

    function frameAction () {
        animation = requestAnimationFrame(frameAction);
        if (!ctx) {
            return;
        }
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        timer++;

        if (timer % 200  == 0 ) {
            let obstacle = new Obstacle();
            obstacleArr.push(obstacle);
        }
        obstacleArr.forEach((a, idx, o)=>{
            if(a.x < 0) {
                o.splice(idx, 1);  // 선인장 객체 배열에서 가장 앞의 것 삭제
                score += 10;
                if (scoreElement) {
                    scoreElement.innerHTML = score.toString();
                }
            }
            if (! detectedCollision) {
                collisionDetection(runner, a);
            }
            a.x --;
            a.draw();
        })
        // 바닥 그리기
        drawLine();
        // 캐릭터 그리기
        runner.draw();
        if(jumpState == 1){ // 점프 상태일 때 타이머가 50이 될때까지 올라간다.
            jumpTimer++;
            if (jumpTimer <= 30) { // 30프레임까지는 올라감
                runner.y -= 4;
            } else { // 그 이후에는 내려옴
                if (jumpTimer <= 40 ) {
                    // 40 프레임까지는 공중에서 멈춘다
                }else {
                    runner.y += 3;
                }
            }
        }
        if(jumpTimer > 40){ // jump 시간이 50이 되면 시간 초기화, 점프 아닌 상태로 변경
            jumpState = 0;
            jumpTimer = 0;
        }
        if(jumpState == 0){ // 점프 상태가 아닐 때는 2씩 내려온다
            if(runner.y < 160){ // 최대 160까지 올라갈 수 있음
                runner.y +=2 ;
            }
        }
    }
    let descriptionElement = document.querySelector("#description");
    let recordElement = document.querySelector("#record");
    let restartNumber = 0;
    // 스페이스바 누르면 게임 화면 시작
    document.addEventListener('keydown', (e)=>{
        if (e.code == 'Space'){
            if (gameState == 0) {
                if (descriptionElement) {
                    descriptionElement.innerHTML = "";
                }
                if (recordElement) {
                    recordElement.innerHTML = "score";
                }
                if(scoreElement) {
                    scoreElement.innerHTML = score.toString();
                }
                initGame();
                gameState = 1; // 게임 실행
                frameAction();

            } else if (gameState == 1 && runner.y > 150) { // 게임실행 중일 때 스페이스 누르면
                jumpState = 1; // 점프 중으로 변경
                playJumpSound();
                dogImg.src = RunningDogImg;
                runner.draw();
            } else if (gameState == 3) {
                restartNumber ++;
                if (restartNumber === 1) {
                    gameState = 0;
                }
            }
        }
    })

    document.addEventListener('keyup', (e)=>{
        if (e.code == 'Space'){
            if (gameState == 1) { // 게임실행 중일 때 스페이스 누르면
                dogImg.src = StopDogImg;
                runner.draw();
            }
        }
    })

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

    function collisionDetection(runner:any, obstacle:any) {
        let xValue = obstacle.x - ( runner.x + runner.width );
        let yValue = obstacle.y - ( runner.y + runner.height );
        if( xValue <= 0 && yValue <= 0 ){ // 충돌!
            // 충돌 시 실행되는 코드
            if (!animation || !ctx) {
                return;
            }
            cancelAnimationFrame(animation);
            playCollisionSound();
            detectedCollision = true;
            setTimeout(()=> {
                setTimeout(()=>{
                    if (!ctx) {return;}
                    ctx.clearRect(0, 0, canvas.width, canvas.height);
                })
                gameState = 3;
                if (descriptionElement) {
                    descriptionElement.innerHTML = "<div class='text-center'><p>Game Over</p><p style='font-size : 20px;'>press Spacebar twice to restart</p></div>"
                }
                if (scoreElement) {
                    scoreElement.innerHTML =""
                }
                if (recordElement) {
                    recordElement.innerHTML =""
                }
            }, 0)
        }
    }
    function initGame() {
        // 초기화할 변수들을 여기에 추가
        timer = 0;
        obstacleArr = [];
        jumpState = 0;
        jumpTimer = 0;
        restartNumber = 0;
        score = 0;
        detectedCollision = false;
        if (scoreElement) {
            scoreElement.innerHTML = score.toString();
        }
    }

});

