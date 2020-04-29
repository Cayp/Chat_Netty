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
    //对响应数据做操作
    if(parseInt(response.data.code, 10) <= '2000000') {
        //console.log('请求成功');
        return response
    }
    if(response.data.code === '2000401' || response.data.code === 2000401) {
        console.log('已过期重新登陆', response.data.code);
        window.location.href = '/login';
        return Promise.reject(response);
    }
    else {
        console.log('请求失败', response.data.code);
        alert(response.data.message);
        return Promise.reject(response);
    }
}, error => {
    //对响应数据错误做操作
    console.log('请求error', error.message);
    return Promise.reject(error);
})

export  {myAxios};