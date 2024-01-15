import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import SignInView from '@/views/SignInView.vue'
import SignUpView from '@/views/SignUpView.vue'
import AnimalTestView from '@/views/AnimalTestView.vue'
import PersonalityTestView from '@/views/PersonalityTestView.vue'
import ProfileView from '@/views/ProfileView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/signin',
      name: 'signin',
      component: SignInView
    },
    {
      path: '/signup',
      name: 'signup',
      component: SignUpView
    },
    {
      path: '/animal_test',
      name: 'animal_test',
      component: AnimalTestView
    },
    {
      path: '/personality_test',
      name: 'personality_test',
      component: PersonalityTestView
    },
    {
      path: '/profile/:nickname',
      name: 'profile',
      component: ProfileView
    },
  ]
})

export default router
