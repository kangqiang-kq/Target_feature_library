import { defineStore } from 'pinia';

interface ObjectList {
	[key: string]: string[];
}

// 自定义权限指令
export const usePermissStore = defineStore('permiss', {
	state: () => {
		const keys = localStorage.getItem('ms_keys'); // 获取权限列表
		return {
			key: keys ? JSON.parse(keys) : <string[]>[],
			defaultList: <ObjectList>{
				// 权限加载
				admin: ['1'], // 待定
				user: ['1','2','2-1','2-2','2-3','2-3','2-4','2-5','2-6','4','5'], // 权限 sidebar 之中
				// 4, 5 是控制按钮的
			}
		};
	},
	actions: {
		handleSet(val: string[]) {
			this.key = val;
		}
	}
});
