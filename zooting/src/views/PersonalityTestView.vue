<template>
    <div>
        <h1>Personality Test</h1>
        <!-- Description -->
        <div>
            <Description></Description>
        </div>

        <!-- MBTI input -->
        <div>
            <MBTIInput></MBTIInput>
        </div>

        <!-- 남은 문제 수 -->
        <div>
        </div>

        <!-- Qusetion -->
        <div>
            <Question></Question>
        </div>

        <!-- Answer select button -->
        <div>
            <AnswerButton></AnswerButton>
        </div>

        <!-- Result -->
        <div>
            <Result></Result>
        </div>
    </div>
</template>

<script setup lang="ts">
import Description from '@/components/personality-test/Description.vue'
import MBTIInput from '@/components/personality-test/MBTIInput.vue'
import Question from '@/components/personality-test/Question.vue'
import AnswerButton from '@/components/personality-test/AnswerButton.vue'
import Result from '@/components/personality-test/Result.vue'

</script>

<style scoped>

</style>