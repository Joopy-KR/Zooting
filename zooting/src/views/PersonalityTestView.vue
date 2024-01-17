<template>
  <div>
    <!-- Description -->
    <Description 
      v-if="!istestStart"
      @test-start="testStart"
    />

    <!-- Qusetion -->
    <Question 
      v-if="istestStart && !istestComplete"
      :test="currentTest"
      @select-answer="handleAnswerSelected"
    />

    <!-- Result -->
    <Result 
      v-if="istestComplete"
      :test-result="testResult"
    />
  </div>
</template>

<script setup lang="ts">
import Description from '@/components/personality-test/Description.vue'
import Question from '@/components/personality-test/Question.vue'
import Result from '@/components/personality-test/Result.vue'
import { ref, computed } from 'vue'
  
const istestStart = ref<boolean>(false)
const testStart = () => {
  istestStart.value = !istestStart.value
}

const istestComplete = ref<boolean>(false)
const testResult = ref<string>('')
const currentTestIndex = ref<number>(0)
const currentTest = computed(() => test[currentTestIndex.value])

const handleAnswerSelected = (answer: number) => {
  if (currentTestIndex.value === 0) {
    if (answer === 1){
      testResult.value += 'I'
    } else {
      testResult.value += 'E'
    }
  } else if (currentTestIndex.value === 1) {
    if (answer === 1){
      testResult.value += 'S'
    } else {
      testResult.value += 'N'
    }
  } else if (currentTestIndex.value === 2) {
    if (answer === 1) {
      testResult.value += 'T'
    } else {
      testResult.value += 'F'
    }
  } else {
    if (answer === 1) {
      testResult.value += 'P'
    } else {
      testResult.value += 'J'
    }
  }

  if (currentTestIndex.value < test.length - 1) {
    currentTestIndex.value++
  } else {
    istestComplete.value = true
  }
}

interface Test {
  question: string
  answer1: string
  answer2: string
}

const test: Test[] = [
  {
    question: '벚꽃이 만개한 어느 봄, 친구에게 연락이 온다. "벚꽃 축제 가자. 너 빼고 다 모였어!"',
    answer1: '"갑자기 어떻게 나가..." 다음에 보자며 이불 속으로 들어간다.',
    answer2: '"나 빼고 만날 수는 없지!" 친구들이 있는 장소로 달려간다.'
  },
  {
    question: '여름휴가로 바다에 놀러왔다!',
    answer1: '"와 바다 시원하다." 좋긴 하지만 별생각이 없다.',
    answer2: '"깊은 곳으로 갔다가 표류되면 어떡하지?" 바다를 보며 온갖 상상을 한다.'
  },
  {
    question: '지나가는 아이가 다가와 가을이 어떤 계절인지 물어본다.',
    answer1: '"가을은 9-11월이고, 단풍이 드는 계절이야." 가을에 대한 객관적인 사실을 설명한다.',
    answer2: '"가을에는 맛있는 걸 많이 먹을 수 있어! 근데 너무 짧아서 아쉬워..." 가을에 대한 내 느낌과 감정을 설명한다.'
  },
  {
    question: '추운 겨울, 창밖을 보니 눈이 오고 있다. "아 헬스장 가야 되는데..."',
    answer1: '"오늘은 홈 트레이닝 해야겠다!" 계획을 곧바로 수정한다.',
    answer2: '"춥긴 하지만..." 계획대로 헬스장을 간다.'
  },
]
</script>

<style scoped>

</style>