<template>
  <div class="ready__container">
    <div>
      <div>
        <ReadyParticipation />
      </div>
  
      <!-- <div>
        <ReadyInvitation />
      </div> -->
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
  background-color: #F1E4FF;
}
</style>