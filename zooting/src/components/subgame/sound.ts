import JumpSound from '@/assets/sounds/Jump High.mp3';
import CollisionSound from '@/assets/sounds/Metal Clang Sound.mp3';
import GameOverSound from '@/assets/sounds/Jump High.mp3';

const jumpSound = new Audio(JumpSound);
const collisionSound = new Audio(CollisionSound);
const gameOverSound = new Audio(GameOverSound);

export function playJumpSound() {
    playSound(jumpSound);
}
export function playCollisionSound() {
    playSound(collisionSound);
}
export function playGameOverSound() {
    playSound(gameOverSound);
}


function playSound(sound:any) {
    sound.currentTime = 0
    sound.play()
}