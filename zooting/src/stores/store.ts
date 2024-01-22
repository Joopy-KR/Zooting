import axios from 'axios'
import { ref, computed } from "vue"
import { defineStore } from "pinia"
import { useRouter } from 'vue-router'

const router = useRouter()
const API_URL = 'http://i10a702.p.ssafy.io'

export const useStore = defineStore("store", () => {
  return {}
}, { persist: true })

export const useAccessTokenStore = defineStore ( "access-token", () => {
  const state = ref<AccessTokenState>({
    accessToken: localStorage.getItem("accessToken") || null,
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
        router.push({ name: 'signin' })
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

  const signOut = function () {
      window.localStorage.clear()
      state.value.accessToken = null
  }

  const userInfo = ref(null)

  const getUserInfo = function () {
    return new Promise((resolve, reject) => {
      axios({
        method: 'get',
        url: `${API_URL}/api/members/`,
        headers: {
          accept: 'application/json',
          Authorization: `Bearer ${state.value.accessToken}`
        }
      })
      .then(res => {
        console.log(res)
        userInfo.value = res.data
        console.log(userInfo.value)
        resolve(res.data)
      })
      .catch(err => {
        console.log(err)
        reject(err)
      })
    })
  }
  
  const saveAdditionalInfo = function (
    payload : {
    nickname: string
    gender: string
    birth: string
    address: string
    interest:string[]
    idealAnimal: string[]
    }) {
    const { nickname, gender, birth, address, interest, idealAnimal } = payload

    axios({
      method: 'put',
      url: `${API_URL}/api/members`,
      data: {
        nickname,
        gender,
        birth,
        address,
        interest,
        idealAnimal
      },
      headers: {
        Authorization: `Token ${state.value.accessToken}`
      }
    })
    .then(res => {
      console.log(res)
    })
    .catch(err => {
      console.log(err)
    })
  }

  return {
    setAccessToken,
    getAccessToken,
    isLogin,
    signOut,
    userInfo,
    getUserInfo,
    saveAdditionalInfo,
  }
}, { persist: true })

interface AccessTokenState {
  accessToken: string | null
}