<template>
  <div class="flex flex-col items-center justify-center">
    <p class="text-xl">회원님은 <span class="font-bold">{{ resultAnimal }}상</span>이시네요</p>
    
    <div class="flex">
      <img src="@/assets/animal_illustration/dog_illust.png" alt="강아지 사진" style="height: 300;"
      v-if="resultAnimal === '강아지'">
      <img src="@/assets/animal_illustration/cat_illust.png" alt="고양이 사진"
      v-if="resultAnimal === '고양이'">
      <img src="@/assets/animal_illustration/bear_illust.png" alt="곰 사진" style="height: 300;"
      v-if="resultAnimal === '곰'">
      <img src="@/assets/animal_illustration/dino_illust.png" alt="공룡 사진" style="height: 300;"
      v-if="resultAnimal === '공룡'">
      <img src="@/assets/animal_illustration/rabbit_illust.png" alt="토끼 사진" style="height: 300;"
      v-if="resultAnimal === '토끼'">
      <img src="@/assets/animal_illustration/turtle_illust.png" alt="꼬부기 사진" style="height: 300;"
      v-if="resultAnimal === '꼬부기'">
      <img src="@/assets/animal_illustration/deer_illust.png" alt="사슴 사진" style="height: 300;"
      v-if="resultAnimal === '사슴'">

      <div id="chart"></div>
    </div>
  </div>

</template>

<script setup>

const props = defineProps({
  resultAnimal: String,
  allAnimal: Object,
})

import ApexCharts from 'apexcharts'

const options = {
  series: [
        {
          name: "분석 결과 (%)",
          color: "#C499F3",
          data: Object.values(props.allAnimal).slice(1),
        },
      ],
      chart: {
        sparkline: {
          enabled: false,
        },
        type: "bar",
        width: "100%",
        height: 300,
        toolbar: {
          show: false,
        }
      },
      fill: {
        opacity: 1,
      },
      plotOptions: {
        bar: {
          horizontal: true,
          columnWidth: "100%",
          borderRadiusApplication: "end",
          borderRadius: 5,
          dataLabels: {
            position: "top",
          },
        },
      },
      dataLabels: {
        enabled: false,
      },
      xaxis: {
        labels: {
          show: false,
          style: {
            fontFamily: "Inter, sans-serif",
            cssClass: 'text-xs font-normal fill-gray-500 dark:fill-gray-400'
          },
          formatter: function(value) {
            return value + "%"
          }
        },
        categories: props.allAnimal.gender === "male" ? ["강아지", "고양이", "곰", "공룡", "토끼"] : ["강아지", "고양이", "꼬부기", "사슴", "토끼"],
        axisTicks: {
          show: false,
        },
        axisBorder: {
          show: false,
        },
      },
      yaxis: {
        labels: {
          show: true,
          style: {
            fontFamily: "Inter, sans-serif",
            cssClass: 'text-xs font-normal fill-gray-500 dark:fill-gray-400'
          }
        }
      },
      grid: {
        show: true,
        strokeDashArray: 4,
        padding: {
          left: 2,
          right: 2,
          top: -20
        },
      },
      fill: {
        opacity: 1,
      }
}

let chart = null;

// 'onMounted' hook을 사용하여 컴포넌트가 마운트된 후에 차트를 렌더링하도록 변경
import { onMounted } from 'vue';

onMounted(() => {
  chart = new ApexCharts(document.querySelector("#chart"), options);
  chart.render();
})

</script>

<style scoped>

#chart {
  max-width: 650px;
  margin: 35px auto;
}
</style>