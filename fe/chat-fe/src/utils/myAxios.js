import axios from 'axios';
import Cookies from 'js-cookie'

const myAxios = axios;
myAxios.defaults.timeout = 100000;
myAxios.defaults.withCredentials = true;

myAxios.interceptors.request.use(config => {
    //发送请求操作，统一再请求里加上authorization
    config.headers['authorization'] = Cookies.get("authorization");
    return config;
}, error => {
    //发送请求错误操作
    console.log('请求失败')
    return Promise.reject(error);
})

myAxios.interceptors.response.use(response => {
    if (response.data.code !== 40001) {
        console.log("success")
        return response
    }
    if (response.data.code === 40001) {
        window.location.href = '/auth/login';
        return Promise.reject(response);
    } 
}, error => {
    //对响应数据错误做操作
    console.log('请求error', error.message);
    return Promise.reject(error);
})

export  {myAxios};