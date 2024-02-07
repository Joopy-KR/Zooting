document.addEventListener('DOMContentLoaded', () => {

    let canvas = document.querySelector("#canvas") as HTMLCanvasElement
    let ctx = canvas.getContext('2d');
    canvas.width = 600; // 300 - canvas 너비
    canvas.height =200;   //215


    // 캐릭터
    let dino = {
        x: 0,
        y: 170, // 땅에서 상자 크기만큼 위에서부터 그린다
        width: 20,
        height: 20,
        draw(){
            if (! ctx){
                return;
            }
            ctx.fillStyle = 'green';
            ctx.fillRect(this.x, this.y, this.width, this.height);
        }
    }
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
            ctx.fillStyle = 'red';
            ctx.fillRect(this.x, this.y, this.width, this.height);

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
    let animation:number;
    let life = 5;
    let score = 0;

    let lifeElement = document.querySelector('#life');
    if (lifeElement) {
     lifeElement.innerHTML = life.toString();
    }
    let scoreElement = document.querySelector('#score');
    if(scoreElement) {
        scoreElement.innerHTML = score.toString();
    }




    function frameAction () {
        animation = requestAnimationFrame(frameAction);
        if (!ctx) {
            return;
        }
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        timer++;

        if (timer % 120 == 0 ) {
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
        if (jumpTimer > 40) {
            jumpState = 0;
            jumpTimer = 0;
        }
        if(jumpState == 1){
            jumpTimer++;
            dino.y-= 0.8;
        }
        if(jumpState == 0) {
            if(dino.y < 170) {
                dino.y ++ ;
            }
        }
    }

    // 스페이스바 누르면 게임 화면 시작
    document.addEventListener('keydown', (e)=>{
        if (e.code == 'Space'){
            if (gameState == 0) {
                gameState = 1; // 게임 실행
                frameAction();
            } else if (gameState == 1) { // 게임실행 중일 때 스페이스 누르면
                jumpState = 1; // 점프 중으로 변경
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
            life --;
            if (lifeElement) {
                lifeElement.innerHTML = life.toString();
            }
            if (!animation || !ctx) {
                return;
            }
            alert('게임오버');
            cancelAnimationFrame(animation);
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            // location.reload();
        }
    }

});

