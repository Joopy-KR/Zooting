import axios from "axios";
import { httpStatusCode } from "./http-status";

const { VITE_SERVER_API_URL } = import.meta.env;

// local vue api axios instance
function localAxios() {
  const instance = axios.create({
    baseURL: VITE_SERVER_API_URL,
    // withCredentials: true,
    // headers: {
    //   "Content-Type": "application/json;charset=utf-8",
    // },
  });
  // Request 발생 시 적용할 내용.
  instance.defaults.headers.common["Authorization"] = "";
  instance.defaults.headers.post["Content-Type"] = "application/json";
  instance.defaults.headers.put["Content-Type"] = "application/json";
  instance.defaults.headers.patch["Content-Type"] = "application/json";
  instance.defaults.headers.delete["Content-Type"] = "application/json";

  // Request, Response 시 설정한 내용을 적용.
  instance.interceptors.request.use((config) => {
    return config;
  }),
    (error: any) => {
      return Promise.reject(error);
    };

  // accessToken의 값이 유효하지 않은 경우,
  // refreshToken을 이용해 재발급 처리.
  // https://maruzzing.github.io/study/rnative/axios-interceptors%EB%A1%9C-%ED%86%A0%ED%81%B0-%EB%A6%AC%ED%94%84%EB%A0%88%EC%8B%9C-%ED%95%98%EA%B8%B0/

  let isTokenRefreshing = false;
  let refreshSubscribers: any = [];

  const onTokenRefreshed = (accessToken: string) => {
    refreshSubscribers.map((callback: any) => callback(accessToken));
  };

  const addRefreshSubscriber = (callback: any) => {
    refreshSubscribers.push(callback);
  };

  instance.interceptors.response.use(
    // 요청 성공
    (response) => {
      // console.log(response);
      return response;
    },
    // 요청 실패
    async (error) => {
      const {
        config,
        response: { status },
      } = error;

      const originalRequest = config;
      if (status === httpStatusCode.UNAUTHORIZED) {
        if (!isTokenRefreshing) {
          isTokenRefreshing = true;

          const refreshToken = localStorage.getItem("refreshToken");
          // refreshToken이 없는 경우 로그인 페이지로 리다이렉트
          if (!refreshToken) {
            return Promise.reject(error);
          }
          const { data } = await axios.post(VITE_SERVER_API_URL + "/api/token/refresh", {
            refreshToken: refreshToken,
          });

          const newAccessToken = await data["result"].accessToken;
          const newRefreshToken = await data["result"].refreshToken;

          if (newAccessToken) {
            localStorage.setItem("accessToken", newAccessToken);
          }
          if (newRefreshToken) {
            localStorage.setItem("refreshToken", newRefreshToken);
          }

          isTokenRefreshing = false;

          instance.defaults.headers.common["Authorization"] = `Bearer ${newAccessToken}`;
          onTokenRefreshed(newAccessToken);
        } else if (status === httpStatusCode.FORBIDDEN) {
          alert(error.response.data.message);
        }

        const retryOriginalRequest = new Promise((resolve) => {
          addRefreshSubscriber((accessToken: string) => {
            originalRequest.headers.Authorization = `Bearer ${accessToken}`;
            resolve(instance(originalRequest));
          });
        });

        return retryOriginalRequest;
      }
      return Promise.reject(error);
    }
  );
  return instance;
}

export { localAxios };
