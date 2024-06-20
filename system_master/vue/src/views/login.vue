<template>
	<div class="login-wrap">
		<div class="ms-login">
			<div class="ms-title">数据管理系统</div>
			<el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
				<el-form-item prop="username">
					<el-input v-model="param.username" placeholder="username">
						<template #prepend>
							<el-button :icon="User"></el-button>
						</template>
					</el-input>
				</el-form-item>
				<el-form-item prop="password">
					<el-input
						type="password"
						placeholder="password"
						v-model="param.password"
						@keyup.enter="submitForm(login)"
					>
						<template #prepend>
							<el-button :icon="Lock"></el-button>
						</template>
					</el-input>
				</el-form-item>
				<div class = "btn">
					<el-button type="primary" @click="submitForm(login)">登 录</el-button>
					<el-button type="primary" @click="resetForm(login)">重 置</el-button>
				</div>

				<p class="login-tips">Tips: 若无账户，请联系管理员进行添加</p>
			</el-form>
		</div>
	</div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useTagsStore } from '../store/tags';
import { usePermissStore } from '../store/permiss';
import { useRouter } from 'vue-router';
import { ElMessage, ElNotification} from 'element-plus';
import type { FormInstance, FormRules } from 'element-plus';
import { Lock, User } from '@element-plus/icons-vue';
import { userLogin } from '../api/index'
const router = useRouter(); // 路由跳转，例如后续的push("/")

// 定义数据类型，可以去掉
// interface LoginInfo {
// 	username: string;
// 	password: string;
// }

// reactive<LoginInfo>
const param = reactive ({ 
	username: 'user',
	password: '121200'
});

const rules: FormRules = {
	// 验证规则
	username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
	password: [{ required: true, message: '请输入密码', trigger: 'blur' }] // blur是失去焦点事件， change是改变后验证
};
const permiss = usePermissStore(); 

const login = ref<FormInstance>(); // 表单对象
// const login = ref(); 简化

const submitForm = (formEl: FormInstance | undefined) => {
	if (!formEl) return;
	formEl.validate((valid: boolean) => {
		if (valid) {
			userLogin(param).then((res: any) => { // 调用函数
                if(res.data == 'error') {
					ElMessage.error('用户名或者密码错误');
					return false;
				}

				ElMessage.success('登录成功'); // 弹窗消息
				localStorage.setItem('ms_username', param.username); // 用户名存储，本地
				const keys = permiss.defaultList[param.username == 'admin' ? 'admin' : 'user'];

				permiss.handleSet(keys); // 权限模块
				localStorage.setItem('ms_keys', JSON.stringify(keys)); 
                router.push('/')
            })

		} else {
			ElMessage.error('登录失败');
			return false;
		}
	});
};

// 重置
const resetForm = (formEl) => {
  if (!formEl) return;
  formEl.resetFields()
}

// const tags = useTagsStore();
// tags.clearTags(); // 清楚提示，如登录成功或者登录失败
</script>

<style scoped>
.login-wrap {
	position: relative;
	width: 100%;
	height: 100%;
	background-image: url(../assets/img/background.png);
	background-size: 100%;
}
.ms-title {
	width: 100%;
	line-height: 50px;
	text-align: center;
	font-size: 20px;
	color: #252525;
	border-bottom: 1px solid #ddd;
}
.ms-login {
	position: absolute;
	left: 50%;
	top: 50%;
	width: 350px;
	margin: -190px 0 0 -175px;
	border-radius: 5px;
	background: rgba(255, 255, 255, 0.3);
	overflow: hidden;
}
.ms-content {
	padding: 30px 30px;
}
.btn {
	text-align: left;
}
.btn button {
	width: 100px;
	height: 36px;
	margin-bottom: 10px;
	margin-left: 30px; 
}

.login-tips {
	font-size: 12px;
	line-height: 30px;
	color: #fff;
}
</style>
