import {createRouter, createWebHistory} from "vue-router";
import {useAccessTokenStore} from "@/stores/store";
import HomeView from "@/views/HomeView.vue";
import SignInView from "@/views/SignInView.vue";
import SignUpView from "@/views/SignUpView.vue";
import AnimalTestView from "@/views/AnimalTestView.vue";
import PersonalityTestView from "@/views/PersonalityTestView.vue";
import ProfileView from "@/views/ProfileView.vue";
import Login from "@/views/LoginView.vue";
import ChatTestView from "@/views/ChatTestView.vue";

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
      path: "/chat",
      name: "chat",
      component: ChatTestView,
    },
    {
      path: "/profile/:nickname?",
      name: "profile",
      component: ProfileView,
      beforeEnter: (to, from, next) => {
        if (to.params.nickname) {
          next();
        } else {
          const store = useAccessTokenStore();
          const nickname = store.userInfo?.nickname;
          if (nickname) {
            to.params.nickname = nickname;
            next({ name: "profile", params: { nickname: nickname } });
          } else {
            next(from);
          }
        }
      },
      children: [
        {
          path: "",
          name: "profile-check",
          component: () => import("@/components/profile/InfoMain.vue"),
        },
        {
          path: "info",
          name: "profile-info",
          component: () => import("@/components/profile/EditInfo.vue"),
        },
        {
          path: "mask/list",
          name: "profile-mask-list",
          component: () => import("@/components/profile/EditMaskList.vue"),
        },
        {
          path: "personal/info",
          name: "profile-personal-info",
          component: () => import("@/components/profile/InfoPersonal.vue"),
        },
      ],
    },
    {
      // catch-all 라우트
      path: "/:catchAll(.*)",
      name: 'not-found',
      component: HomeView,
    },
  ],
});

router.beforeEach((to, from) => {
  const store = useAccessTokenStore();

  // 허용되지 않는 경로 접근
  if (to.name === 'not-found') {
    if (store.isLogin) {
      return {name: 'home'};
    } else {
      return {name: 'signin'};
    }
  }

  // 로그인이 된 경우
  if (store.isLogin) {
    if (to.name === 'home') {
      store.getUserInfo()
    }
    return;
  }

  // 로그인이 안한 경우
  if (to.name !== 'login' && to.name !== 'signin') {
    return {name: 'signin'};
  }
});

export default router;
