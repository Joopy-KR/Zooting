import StopDogImg from '@/assets/images/sub_game/stopdog.png';
import RunningDogImg from '@/assets/images/sub_game/runningdog.png';
import HamburgerImg from '@/assets/images/sub_game/obstacle_hamburger.png';
import PotatoImg from '@/assets/images/sub_game/obstacle_potato.png';
import PizzaImg from '@/assets/images/sub_game/obstacle_pizza.png';

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
    let dino = {
        x: 0,
        y: 160, // 땅에서 상자 크기만큼 위에서부터 그린다
        width: 50,
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

    let obstacleImg = new Image();
    obstacleImg.src = HamburgerImg;
    // 선인장 장애물
    class Cactus {
        width:number;
        height:number;
        x:number;
        y:number;
        constructor() {
            this.width = 20 + getRandomInt(-5, 6);
            this.height = 20 + getRandomInt(-5, 6);
            this.x = 560; // 가장 끝에서 약 30만큼 전
            this.y = 190 - this.height; // 땅위치에서 높이만큼 위에서 시작

        }
        draw() {
            if (! ctx) {
                return;
            }
            // ctx.fillStyle = 'red';
            // ctx.fillRect(this.x, this.y, this.width, this.height);
            // let num = getRandomInt(1, 3);
            // if (num === 1) {
            //     obstacleImg.src = HamburgerImg;
            // }else if (num === 2) {
            //     obstacleImg.src = PizzaImg;
            // }else {
            //     obstacleImg.src = PotatoImg;
            // }
            ctx.drawImage(obstacleImg, this.x, this.y, this.width, this.height);
        }
    }

    let cactus = new Cactus();

    function getRandomInt(min:number, max:number) {
        min = Math.ceil(min);
        max = Math.floor(max);
        return Math.floor(Math.random() * (max - min)) + min;

    }

    // 프레임 단위로 선인장들이 다가 오게 하는 함수
    let timer = 0;
    let cactusArr:Cactus[] = [];
    let gameState = 0; // 0 : end , 1: start
    let jumpState = 0; // 0 : default, 1 : jump
    let jumpTimer = 0;
    let animation:number ;
    let score = 0;

    let scoreElement = document.querySelector('#score');

    function frameAction () {
        animation = requestAnimationFrame(frameAction);
        if (!ctx) {
            return;
        }
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        timer++;

        if (timer % 200 == 0 ) {
            let cactus = new Cactus();
            cactusArr.push(cactus);
        }
        cactusArr.forEach((a, idx, o)=>{
            if(a.x < 0) {
                o.splice(idx, 1);  // 선인장 객체 배열에서 가장 앞의 것 삭제
                score += 10;
                if (scoreElement) {
                    scoreElement.innerHTML = score.toString();
                }
            }
            collisionDetection(dino, a);
            a.x --;
            a.draw();
        })
        // 바닥 그리기
        drawLine();
        // 캐릭터 그리기
        dino.draw();
        // 스페이스 연타로 점프할때 jumptimer가 40이 되면 스페이스가 jump mode가 아니게 된다
        if (jumpTimer > 10) {
            console.log('44')
            console.log(jumpTimer)
            jumpState = 0;
            jumpTimer = 0;
        }
        if(jumpState == 1){
            console.log('55')
            console.log(jumpTimer, dino.y)
            jumpTimer += 20;
            dino.y-= 70;
        }
        if(jumpState == 0) {    // 타이머가 다 돼서 state가 0이 되고 높이가 135보다 높으면 2.5씩 떨어진다
            if(dino.y < 150) {
                jumpState = 0
                if (dino.y + 20 <= 160) {
                    dino.y += 2 ;
                } else if (dino.y + 20 >160) {
                    dino.y = 160
                }
                console.log('!!!!')
            }

        }
        if (jumpTimer > 10) {
            console.log('44')
            console.log(jumpTimer)
            jumpState = 0;
            jumpTimer = 0;
        }
    }
    let descriptionElement = document.querySelector("#description");
    let recordElement = document.querySelector("#record");
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

            } else if (gameState == 1 && dino.y > 150) { // 게임실행 중일 때 스페이스 누르면
                jumpState = 1; // 점프 중으로 변경
                dogImg.src = RunningDogImg;
                dino.draw();
            }
        }
    })

    document.addEventListener('keyup', (e)=>{
        if (e.code == 'Space'){
            if (gameState == 1) { // 게임실행 중일 때 스페이스 누르면
                jumpState = 1; // 점프 중으로 변경
                dogImg.src = StopDogImg;
                dino.draw();
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

    function collisionDetection(dino:any, cactus:any) {
        let xValue = cactus.x - ( dino.x + dino.width );
        let yValue = cactus.y - ( dino.y + dino.height );
        if( xValue < 0 && yValue < 0 ){ // 충돌!
            // 충돌 시 실행되는 코드
            if (!animation || !ctx) {
                return;
            }
            console.log(1)
            cancelAnimationFrame(animation);
            console.log(2)
            setTimeout(()=> {
                if (!ctx) {return;}
                ctx.clearRect(0, 0, canvas.width, canvas.height);
                gameState = 0;
                if (descriptionElement) {
                    descriptionElement.innerHTML = "Game Over"
                }
                if (scoreElement) {
                    scoreElement.innerHTML =""
                }
                if (recordElement) {
                    recordElement.innerHTML =""
                }
            }, 0)
            // location.reload();
            console.log(3)
        }
    }
    function initGame() {
        // 초기화할 변수들을 여기에 추가
        timer = 0;
        cactusArr = [];
        jumpState = 0;
        jumpTimer = 0;
        score = 0;
        if (scoreElement) {
            scoreElement.innerHTML = score.toString();
        }
    }

});

