<template>
  <div class="tab-bar__container">
    <div>
      <div class="tab-bar__div">
        <nav class="tab-bar" aria-label="Tabs">
          <div 
            v-for="tab in tabs" :key="tab.name" 
            :class="[tab.name === cuurentTab ? 'tab-bar__item--active' : 'tab-bar__item--inactive', 'tab-bar__item']"
            @click="selectTab(tab.name)"
          >
            {{ tab.name }}
            <span 
              v-if="tab.count" 
              :class="[tab.name === cuurentTab ? 'tab-bar__text--active' : 'tab-bar__text--inactive', 'tab-bar__text']"
            >
              {{ tab.count }}
            </span>
          </div>
        </nav>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const props = defineProps<{
  tabs: {
    name: string
    count: number
  }[]
}>()

const emit = defineEmits(['selectTab'])

const cuurentTab = ref<string>('친구')

const selectTab = (selectedTab: string) => {
  cuurentTab.value = selectedTab
  emit('selectTab', selectedTab)
}
</script>

<style scoped>
.tab-bar__container {
  @apply bg-[#A297F6];
}
.tab-bar__div {
  @apply border-b border-gray-200 px-4;
}
.tab-bar {
  @apply flex -mb-px space-x-8;
}
.tab-bar__item {
  @apply w-1/3 whitespace-nowrap border-b-2 py-4 px-1 font-bold text-center cursor-pointer;
  position: relative;
}
.tab-bar__item--active {
  @apply border-[#e6ff75] text-white;
}
.tab-bar__item--inactive {
  @apply border-transparent text-gray-300 hover:text-gray-200;
}
.tab-bar__text {
  @apply ml-2 rounded-full py-0.5 px-2 text-xs font-medium;
  position: absolute;
}
.tab-bar__text--active {
  @apply bg-indigo-50 text-[#8072EF];
}
.tab-bar__text--inactive {
  @apply bg-gray-100 text-gray-900;
}
</style>