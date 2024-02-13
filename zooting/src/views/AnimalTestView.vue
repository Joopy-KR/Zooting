<template>
    <div class="outer-container base-bg">
      <div class="inner-container">
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
            :all-animal="allAnimal"
            />
        </transition>
      </div>
    </div>
  </template>


<script setup>
  import { ref, onMounted } from 'vue'
  import Camera from '@/components/animal-test/Camera.vue'
  import Result from '@/components/animal-test/Result.vue'
  import { useAccessTokenStore } from "@/stores/store"

  const store = useAccessTokenStore()

  // 닮은 동물상
  const resultAnimal = ref('')

  // 각각의 동물상이 몇 퍼센트 나왔는지 전달할 객체
  const allAnimal = ref({})

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

    // 남자는 강아지, 고양이, 곰, 공룡, 토끼
    if (gender.value === 'male') {
      allAnimal.value["gender"] = 'male'
      allAnimal.value["dog"] = (Number(args[1]) * 100).toFixed(0)
      allAnimal.value["cat"] = (Number(args[2]) * 100).toFixed(0)
      allAnimal.value["bear"] = (Number(args[3]) * 100).toFixed(0)
      allAnimal.value["dino"] = (Number(args[4]) * 100).toFixed(0)
      allAnimal.value["rabbit"] = (Number(args[5]) * 100).toFixed(0)

      if (maxIdx.value === 1) {
          resultAnimal.value = '강아지'
      } else if (maxIdx.value === 2) {
          resultAnimal.value = '고양이'
      } else if (maxIdx.value === 3) {
          resultAnimal.value = '곰'
      } else if (maxIdx.value === 4) {
          resultAnimal.value = '공룡'
      } else if (maxIdx.value === 5) {
          resultAnimal.value = '토끼'
      }
    // 여자는 강아지, 고양이, 펭귄, 사슴, 토끼
    } else if (gender.value === 'female') {
      allAnimal.value["gender"] = 'female'
      allAnimal.value["dog"] = (Number(args[1]) * 100).toFixed(0)
      allAnimal.value["cat"] = (Number(args[2]) * 100).toFixed(0)
      allAnimal.value["turtle"] = (Number(args[3]) * 100).toFixed(0)
      allAnimal.value["deer"] = (Number(args[4]) * 100).toFixed(0)
      allAnimal.value["rabbit"] = (Number(args[5]) * 100).toFixed(0)

      if (maxIdx.value === 1) {
          resultAnimal.value = '강아지'
      } else if (maxIdx.value === 2) {
          resultAnimal.value = '고양이'
      } else if (maxIdx.value === 3) {
          resultAnimal.value = '펭귄'
      } else if (maxIdx.value === 4) {
          resultAnimal.value = '사슴'
      } else if (maxIdx.value === 5) {
          resultAnimal.value = '토끼'
      }
    }

    saveTestResult()

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
  const animalRatioReq = (animal, ratio) => {
    return {
      animal: animal,
      percentage: Number(ratio),
    }
  }
  // 동물상 비율 저장
  const saveTestResult = () => {
    const animalFaceReqList = [];
    animalFaceReqList.push({animal:"강아지", percentage: Number(allAnimal.value["dog"])});
    animalFaceReqList.push({animal:"고양이", percentage: Number(allAnimal.value["cat"])});
    animalFaceReqList.push({animal:"토끼", percentage: Number(allAnimal.value["rabbit"])});

    if (allAnimal.value["gender"] === 'female') {
      animalFaceReqList.push({animal:"사슴", percentage: Number(allAnimal.value["deer"])});
      animalFaceReqList.push({animal:"펭귄", percentage: Number(allAnimal.value["turtle"])});
    } else {
      animalFaceReqList.push({animal:"곰", percentage: Number(allAnimal.value["bear"])})
      animalFaceReqList.push({animal:"공룡", percentage: Number(allAnimal.value["dino"])});
    }

    store.setAnimalFace(animalFaceReqList);
  }
</script>


<style scoped>
.outer-container {
    @apply flex flex-col justify-center items-center p-6 w-screen h-screen;
    overflow-y: auto;
}

.inner-container {
  @apply flex flex-col items-center justify-center w-full h-full bg-white border border-gray-200 shadow;
}

  .fade-enter-active,
  .fade-leave-active {
    transition: opacity 0.5s ease;
  }

  .fade-enter-from,
  .fade-leave-to {
    opacity: 0;
  }
.base-bg {
  background-image: url("/assets/images/login/background.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}
</style>
  