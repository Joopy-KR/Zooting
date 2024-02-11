import StopDogImg from '/assets/images/sub_game/stopdog.png';
import RunningDogImg from '/assets/images/sub_game/runningdog.png';
import HamburgerImg from '/assets/images/sub_game/obstacle_hamburger.png';
import PotatoImg from '/assets/images/sub_game/obstacle_potato.png';
import PizzaImg from '/assets/images/sub_game/obstacle_pizza.png';
import { playJumpSound, playCollisionSound} from "@/components/subgame/sound";

 window.onload = function () {
     console.log('흠냐')
    let canvas:any;
    let ctx:any;
    let descriptionElement = document.querySelector("#description");
    let recordElement = document.querySelector("#record");
    let scoreElement = document.querySelector('#score');

    let gameState = 0; // 0 : end , 1: start, 3: gameover
    let timer = 0;

    let jumpState = 0; // 0 : default, 1 : jump
    let jumpTimer = 0;
    let animation:number ;
    let score = 0;
    let detectedCollision = false;

    // description 클릭시 게임 시작 및 재시작
    if (! descriptionElement){
        return;
    }
    descriptionElement.addEventListener('click', ()=>{
        initGame(); // 캔버스, 변수 초기화
        gameState = 1; // 게임 실행
        frameAction();
    })

    
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
        if (timer % 200 == 0 ) {
            let obstacle = new Obstacle();
            obstacleArr.push(obstacle);
        }
        // 한 프레임마다 출력된 장애물 객체의 위치, 충돌 여부 확인
        obstacleArr.forEach((a, idx, o)=>{
            // 화면 왼쪽으로 사라지는 객체 삭제
            if(a.x < 0) {
                o.splice(idx, 1);
                score += 10;
                if (scoreElement) {
                    scoreElement.innerHTML = score.toString();
                }
            }
            // 충돌 확인 - 충돌하지 않았다면 객체 위치 왼쪽으로 1씩 이동
            if (! detectedCollision) {
                collisionDetection(runner, a);
                a.x --;
                a.draw();
            }
        })
        // 바닥 그리기
        drawLine();
        // 캐릭터 그리기
        runner.draw();

        // 점프 - 점프 시간 동안 jumptimer 값 상승
        if(jumpState == 1){ // 점프 상태일 때 타이머가 50이 될때까지 올라간다.
            jumpTimer++;
            if (jumpTimer <= 30) { // 30프레임까지는 올라감
                runner.y -= 4;
            } else { // 그 이후에는 내려옴
                if (jumpTimer <= 40 ) {
                    // 40 프레임까지는 공중에서 멈춘다
                }
            }
        }
        // jumptimer 시간이 40이 넘었다면 jumpstate, jumptimer 값 초기화
        if(jumpTimer > 40){ // jump 시간이 50이 되면 시간 초기화, 점프 아닌 상태로 변경
            jumpState = 0;
            jumpTimer = 0;
        }
        // jumpstate가 0인데 공중에 있는 경우 내려온다
        if(jumpState == 0){
            if(runner.y < 160){
                runner.y += 3 ;
            }
        }
    }


    // 스페이스 누를 때 점프 및 이미지 변경
    document.addEventListener('keydown', (e)=>{
        e.preventDefault()
        if (e.code == 'Space'){
             if (gameState == 1 && runner.y > 150) {
                jumpState = 1; // 점프 중으로 변경
                playJumpSound();
                dogImg.src = RunningDogImg;
                runner.draw();
            }
        }
    })

    document.addEventListener('keyup', (e)=>{
        e.preventDefault()
        if (e.code == 'Space'){
            if (gameState == 1) { // 게임실행 중일 때 스페이스 누르면
                dogImg.src = StopDogImg;
                runner.draw();
            }
        }
    })


    // 충돌 시 게임 종료
    function collisionDetection(runner:any, obstacle:any) {
        let xValue = obstacle.x - ( runner.x + runner.width );
        let yValue = obstacle.y - ( runner.y + runner.height );

        if (detectedCollision) {
            return;
        }
        if( xValue <= 0 && yValue <= 0 ){ // 충돌!
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
                canvas.remove();
                gameState = 3;
                if (descriptionElement) {
                    descriptionElement.innerHTML = "<div class='text-center'><p>Try again?</p><p style='font-size : 20px;'>your score is " + score.toString() + "</p></div>"
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
    
    // 게임 재시작시 초기화 함수
    function initGame() {
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
        score = 0;
        detectedCollision = false;
        flag = false;
        dogImg.src = StopDogImg;

        if (descriptionElement) {
            descriptionElement.innerHTML = "";
        }
        if (recordElement) {
            recordElement.innerHTML = "score";
        }
        if (scoreElement) {
            scoreElement.innerHTML = score.toString();
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
    // 랜덤 수 추출
    function getRandomInt(min:number, max:number) {
        min = Math.ceil(min);
        max = Math.floor(max);
        return Math.floor(Math.random() * (max - min)) + min;

    }

};

