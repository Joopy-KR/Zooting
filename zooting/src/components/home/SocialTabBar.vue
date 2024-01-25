<template>
  <div class="tab-bar__container">
    <div>
      <div class="border-b border-gray-200">
        <nav class="flex -mb-px space-x-8" aria-label="Tabs">
          <a v-for="tab in tabs" :key="tab.name" href="#" :class="[tab.current ? 'border-indigo-500 text-indigo-600' : 'border-transparent text-gray-500 hover:border-gray-200 hover:text-gray-700', 'flex whitespace-nowrap border-b-2 py-4 px-1 text-sm font-medium']" :aria-current="tab.current ? 'page' : undefined">
            {{ tab.name }}
            <span v-if="tab.count" :class="[tab.current ? 'bg-indigo-100 text-indigo-600' : 'bg-gray-100 text-gray-900', 'ml-3 rounded-full py-0.5 px-2.5 text-xs font-medium']">{{ tab.count }}</span>
          </a>
        </nav>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const tabs = [
  { name: '친구', href: '#', count: '52', current: true },
  { name: '친구 요청', href: '#', count: '6', current: false },
  { name: '차단', href: '#', count: '4', current: false },
]
</script>

<style scoped>
.tab-bar__container {
  min-height: 50px;
}
</style>