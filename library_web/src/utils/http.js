// http.js
//让axios请求头每次都携带token

import axios from 'axios';
// import store from '@/store'

const instance = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000, // 设置请求超时时间
});

// 在请求发送之前添加拦截器
instance.interceptors.request.use(
    config => {
        // 若localStorage内存在userInfo，则给请求头加上token
        if (localStorage.getItem('userInfo')) {
            const token = JSON.parse(localStorage.getItem('userInfo')).token;
            if (token) {
                config.headers.token = token;
            }
        }
        // store.commit('showLoading', true);
        return config;
    },
    error => {
        // 对请求错误做些什么
        return Promise.reject(error);
    }
);

//后端响应后关闭加载界面
instance.interceptors.response.use(
    res => {
        // store.commit('showLoading', false);
        return Promise.resolve(res);
    },
    error => {
        // store.commit('showLoading', false);
        return Promise.resolve(error);
    }
)

export default instance;
