import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';
import { usePermissStore } from '../store/permiss';
import Home from '../views/home.vue'; 


// 路由
const routes: RouteRecordRaw[] = [
    {
        path: '/',
        redirect: '/dashboard',
    },

    {
        path: '/',
        name: 'Home',
        component: Home,
        children: [
            // 以下的组件是加载到home之中的
            {
                path: '/dashboard',
                name: 'dashboard',
                meta: {
                    title: '系统首页',
                },
                component: () => import(/* webpackChunkName: "dashboard" */ '../views/dashboard.vue'),
            },

            {
                path: '/ship', 
                name: 'ship',
                meta: {
                    title: '海上目标', // 选项title
                },
                component: () => import(/* webpackChunkName: "ship" */ '../views/ship.vue'), // 设置路由
            },
            {
                path: '/ais',
                name: 'ais',
                meta: {
                    title: 'AIS 数据',
                },
                component: () => import(/* webpackChunkName: "ais" */ '../views/ais.vue'),
            },

            {
                path: '/ais/:id',
                name: 'ais_mmsi',
                meta: {
                    title: 'AIS(Limited)',
                },
                component: () => import(/* webpackChunkName: "ais_mmsi" */ '../views/ais.vue'),
            },

            {
                path: '/lowlight',
                name: 'lowlight',
                meta: {
                    title: '微光影像',
                },
                component: () => import(/* webpackChunkName: "lowlight" */ '../views/lowlight.vue'),
            },

            {
                path: '/lowlight/:mmsi',
                name: 'lowlight_mmsi',
                meta: {
                    title: '微光影像(Limited)',
                },
                component: () => import(/* webpackChunkName: "lowlight_mmsi" */ '../views/lowlight.vue'),
            },

            {
                path: '/thermalinfrared', // 路由
                name: 'thermalinfrared',
                meta: {
                    title: '热红外影像', // 导航title
                },
                component: () => import(/* webpackChunkName: "thermalinfrared" */ '../views/thermalinfrared.vue'),
            },
            {
                path: '/thermalinfrared/:mmsi', // 路由
                name: 'thermalinfrared_mmsi',
                meta: {
                    title: '热红外影像(Limited)', // 导航title
                },
                component: () => import(/* webpackChunkName: "thermalinfrared_mmsi" */ '../views/thermalinfrared.vue'),
            },


            {
                path: '/user',
                name: 'user',
                meta: {
                    title: '个人中心',
                },
                component: () => import(/* webpackChunkName: "user" */ '../views/user.vue'),
            },
        ],
    },
    {
        path: '/login', // 登录路由
        name: 'Login',
        meta: {
            title: '登录',
        },
        component: () => import(/* webpackChunkName: "login" */ '../views/login.vue'),
    },
    {
        path: '/403',
        name: '403',
        meta: {
            title: '没有权限',
        },
        component: () => import(/* webpackChunkName: "403" */ '../views/403.vue'),
    },
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title} | vue-manage-system`;
    const role = localStorage.getItem('ms_username');
    const permiss = usePermissStore();
    if (!role && to.path !== '/login') {
        next('/login'); // 用户没登录
    } else if (to.meta.permiss && !permiss.key.includes(to.meta.permiss)) {
        // 如果没有权限，则进入403
        next('/403');
    } else {
        next();
    }
});

export default router;
