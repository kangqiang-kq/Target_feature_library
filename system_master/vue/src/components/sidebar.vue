<template>
    <div class="sidebar">
        <el-menu
            class="sidebar-el-menu"
            :default-active="onRoutes"
            :collapse="sidebar.collapse"
            background-color="#324157"
            text-color="#bfcbd9"
            active-text-color="#20a0ff"
            unique-opened
            router
        >
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-sub-menu :index="item.index" :key="item.index" v-permiss="item.permiss">
                        <template #title>
                            <el-icon>
                                <component :is="item.icon"></component>
                            </el-icon>
                            <span>{{ item.title }}</span>
                        </template>
                        <template v-for="subItem in item.subs">
                            <!-- 三级目录 -->
                            <!-- <el-sub-menu
                                v-if="subItem.subs"
                                :index="subItem.index"
                                :key="subItem.index"
                                v-permiss="item.permiss"
                            >
                                <template #title>{{ subItem.title }}</template>
                                <el-menu-item v-for="(threeItem, i) in subItem.subs" :key="i" :index="threeItem.index">
                                    {{ threeItem.title }}
                                </el-menu-item>
                            </el-sub-menu> -->

                            <!-- 二级目录 -->
                            <!-- <el-menu-item v-else :index="subItem.index" v-permiss="item.permiss"> -->
                            <el-menu-item :index="subItem.index" v-permiss="subItem.permiss">
                                <template #title>
                                    <span>{{ subItem.title }}</span>
                                </template>
                            </el-menu-item>
                        </template>
                    </el-sub-menu>
                </template>

                <!-- 一级别目录 -->
                <template v-else>
                    <el-menu-item :index="item.index" :key="item.index" v-permiss="item.permiss">
                        <el-icon>
                            <component :is="item.icon"></component>
                        </el-icon>
                        <template #title>{{ item.title }}</template>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useSidebarStore } from '../store/sidebar';
import { useRoute } from 'vue-router';

const items = [
    {
        icon: 'HomeFilled',
        index: '/dashboard',
        title: '系统首页', // 首页
        permiss: '1', 
    },
    {
        icon: 'Setting', // 外层可以加图标
        index: '1', // 不设置
        title: '目标特性库',
        permiss: '2',
        subs: [

            {
                index: '/ship',
                title: '海上目标', // 侧边栏title
                permiss: '2-1'
            },

            {
                index: '/lowlight',
                title: '微光影像',
                permiss: '2-2'
            },
            {
                index: '/thermalinfrared', // 路由
                title: '热红外影像',
                permiss: '2-3'
            },
            {
                index: '/ais',
                title: 'AIS 数据',
                permiss: '2-4'
            },
        ],
    },
];

const route = useRoute();
const onRoutes = computed(() => {
    return route.path;
});
const sidebar = useSidebarStore();

</script>

<style scoped>
.sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 60px;
    bottom: 0;
    overflow-y: scroll;
}
.sidebar::-webkit-scrollbar {
    width: 0;
}
.sidebar-el-menu:not(.el-menu--collapse) {
    width: 200px;
}
.sidebar > ul {
    height: 100%;
}
</style>
