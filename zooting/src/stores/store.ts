import axios from 'axios'
import { ref, computed } from "vue"
import { defineStore } from "pinia"
import { useRouter } from 'vue-router'

const router = useRouter()

export const useStore = defineStore("store", () => {
  return {}
}, { persist: true })

export const useAccessTokenStore = defineStore ( "access-token", () => {
  const state = ref<AccessTokenState>({
    accessToken: null,
  })

  const setAccessToken = function (token: string | null) {
    if (token) {
      localStorage.setItem("accessToken", token)
    } else {
      localStorage.removeItem("accessToken")
    }
    state.value.accessToken = token
  }

  const getAccessToken = function () {
    if (state.value.accessToken) {
      return state.value.accessToken
    } else {
      const accessToken = localStorage.getItem("accessToken")
      if (accessToken) {
        state.value.accessToken = accessToken
        return accessToken
      } else {
        // TODO: go to home page
        alert("Access token not found")
      }
    }
  }

  const isLogin = computed(() => {
    if (state.value.accessToken) {
      return true 
    } else {
      return false
    }
  })

  // const signOut = function () {
  //   axios({
  //     method: 'post',
  //     url: ``,
  //     headers: {
  //       Authorization: `Bearer ${state.value.accessToken}`
  //     }
  //   })
  //   .then((res)=>{
  //     console.log(res)
  //     window.localStorage.clear()
  //     state.value.accessToken = null
  //     router.push({ name: 'signin' })
  //   })
  //   .catch((err)=> {
  //     console.log(err)
  //   })
  // }

  return {
    setAccessToken,
    getAccessToken,
    isLogin,
    // signOut,
  }
}, { persist: true })

interface AccessTokenState {
  accessToken: string | null
}