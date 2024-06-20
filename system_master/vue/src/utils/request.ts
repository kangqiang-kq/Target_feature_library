import axios, {AxiosInstance, AxiosError, AxiosResponse, AxiosRequestConfig} from 'axios';


// 创建axios实例
const service:AxiosInstance = axios.create({
    timeout: 5000,
    baseURL: 'http://localhost:8080/',
    headers: { 'Content-Type': 'application/json;charset=utf-8' }
});

// 请求拦截
service.interceptors.request.use(
    (config: AxiosRequestConfig) => {
        return config;
    },
    (error: AxiosError) => {
        console.log(error);
        return Promise.reject();
    }
);

// 响应拦截
service.interceptors.response.use(
    (response: AxiosResponse) => {
        if (response.status === 200) {
            return response;
        } else {
            Promise.reject();
        }
    },
    (error: AxiosError) => {
        console.log(error);
        return Promise.reject();
    }
);


export default service;
