<template>
  <div class="ready__container">
    <div>
      <img src="/images/logo_lg.png" alt="">
    </div>

    <div>
      <div>
        <ReadyParticipation />
      </div>
  
      <div>
        <ReadyInvitation />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ReadyParticipation from './ReadyParticipation.vue'
import ReadyInvitation from './ReadyInvitation.vue'

</script>

<style scoped>
.ready__container {
  @apply flex-auto;
  background-color: bisque;
}
</style>