import { createRouter, createWebHistory } from "vue-router"
import { useAccessTokenStore } from "../stores/store"
import HomeView from "@/views/HomeView.vue"
import SignInView from "@/views/SignInView.vue"
import SignUpView from "@/views/SignUpView.vue"
import AnimalTestView from "@/views/AnimalTestView.vue"
import PersonalityTestView from "@/views/PersonalityTestView.vue"
import ProfileView from "@/views/ProfileView.vue"
import Login from "@/views/LoginView.vue"

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/signin",
      name: "signin",
      component: SignInView,
    },
    {
      path: "/login",
      name: "login",
      component: Login,
    },
    {
      path: "/signup",
      name: "signup",
      component: SignUpView,
    },
    {
      path: "/animal_test",
      name: "animal_test",
      component: AnimalTestView,
    },
    {
      path: "/personality_test",
      name: "personality_test",
      component: PersonalityTestView,
    },
    {
      path: "/profile/:nickname",
      name: "profile",
      component: ProfileView,
    },
  ],
})

router.beforeEach((to, from) => {
  const store = useAccessTokenStore()

  if ((to.name === 'home' || to.name === 'signup' || to.name === 'animal_test' || to.name === 
  'personality_test') && !store.isLogin) {
    return { name: 'signin' }
  }

  if (to.name === 'signin' && store.isLogin) {
    return { name: 'home' }
  }

  // 회원가입 및 테스트 완료 했으면 해당 페이지로 가지 못하게 막기 && 이용권 구매했을 경우 풀기
})

export default router