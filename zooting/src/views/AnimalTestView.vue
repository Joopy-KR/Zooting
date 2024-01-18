<template>
    <div class="flex flex-col items-center justify-center h-screen">
      <!-- 설명 출력 -->
      <transition name="fade">
          <p v-if="isDescription" class="text-4xl font-bold">가면으로 사용할 동물상을 분석할게요</p>
      </transition>
  
      <!-- 동물상 테스트 출력 -->
      <transition name="fade">
          <Camera 
          v-if="isAnimal"
          @work-finished="isAnimalFinished"
          />
      </transition>

    <!-- 설명 출력 -->
      <transition name="fade">
        <p v-if="isLoading" class="text-4xl font-bold">동물상을 분석하는 중이에요</p>
      </transition>

      <!-- 결과 출력 -->
      <transition name="fade">
        <Result
        v-if="isResult"
        :result-animal="resultAnimal"
        />
      </transition>
    </div>
  </template>


<script setup>
  import { ref, onMounted } from 'vue'
  import Camera from '@/components/animal-test/Camera.vue'
  import Result from '@/components/animal-test/Result.vue'
  
  // 닮은 동물상
  const resultAnimal = ref('')

  
  // 설명 화면
  const isDescription = ref(false)
  
  // 동물상 테스트 화면
  const isAnimal = ref(false)

  // 동물상 테스트 종료 후 결과 출력 전, 처리하는 함수
  const gender = ref(0)

  async function isAnimalFinished(...args) {
    const maxVal = ref(-1)
    const maxIdx = ref(-1)
    args.forEach((item, index) => {
        if (item === 'male') {
          gender.value = 'male'
        } else if (item === 'female') {
          gender.value = 'female'
        }
        
        if (Number(item) > Number(maxVal.value)) {
            maxVal.value = Number(item)
            maxIdx.value = Number(index)
        }
    })

    if (gender.value === 'male') {
      if (maxIdx.value === 1) {
          resultAnimal.value = '강아지'
      } else if (maxIdx.value === 2) {
          resultAnimal.value = '고양이'
      } else if (maxIdx.value === 3) {
          resultAnimal.value = '토끼'
      } else if (maxIdx.value === 4) {
          resultAnimal.value = '곰'
      } else if (maxIdx.value === 5) {
          resultAnimal.value = '공룡'
      }
    } else if (gender.value === 'female') {
      if (maxIdx.value === 1) {
          resultAnimal.value = '강아지'
      } else if (maxIdx.value === 2) {
          resultAnimal.value = '고양이'
      } else if (maxIdx.value === 3) {
          resultAnimal.value = '토끼'
      } else if (maxIdx.value === 4) {
          resultAnimal.value = '사슴'
      } else if (maxIdx.value === 5) {
          resultAnimal.value = '꼬부기'
      }
    }
    // 다음 컴포넌트로 이동
    await wait(0.5)
    isAnimal.value = false
    await wait(0.5)
    isLoading.value = true
    await wait(1.5)
    isLoading.value = false
    await wait(0.5)
    isResult.value = true
  }
  
  // 로딩 화면
  const isLoading = ref(false)


  // 결과 화면
  const isResult = ref(false)
  
  // 대기 함수
  function wait(sec) {
      return new Promise(resolve => setTimeout(resolve, sec * 1000));
    }


  // 페이지를 돌리는 자동함수
  onMounted(Run)

  async function Run() {
    await wait(0.5)
    isDescription.value = true
    await wait(1.5)
    isDescription.value = false
    await wait(1)
    isAnimal.value = true
  }

</script>


<style scoped>

  .fade-enter-active,
  .fade-leave-active {
    transition: opacity 0.5s ease;
  }
  
  .fade-enter-from,
  .fade-leave-to {
    opacity: 0;
  }
</style>
  