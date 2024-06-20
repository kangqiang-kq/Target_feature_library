import request from '../utils/request';
import service from '../utils/request';


// 用户请求
// 获取table数据
export const fetchData = () => { // 不传参数的写法
    return request({
        url: 'http://localhost:8081/table.json',
        method: 'get'
    });
};

export function userLogin(data) { // 传参写法
    return service(
        {
            url: '/user/login',
            method: 'post',
            data, // 数据
        },
    )
}


// 获取所有的渔船对象
export function getAllShips() { // 无参
    return service(
        {
            url: '/img/getAllShips',
            method: 'get',
        },
    )
}