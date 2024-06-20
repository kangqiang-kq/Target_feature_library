<template>
	<div>
		<el-row :gutter="20">
			<el-col :span="12">
				<el-card shadow="hover">
					<template #header>
						<div class="clearfix">
							<span>基础信息</span>
						</div>
					</template>
					<div class="info">
						<div class="info-image">
						<!-- <div class="info-image" @click="showDialog"> -->
							<el-avatar :size="100" :src="avatarImg" />
							<!-- <span class="info-edit">
								<i class="el-icon-lx-camerafill"></i>
							</span> -->
						</div>
						<div class="info-name">{{ name }}</div>
						<div class="info-desc">It is me！</div>
					</div>
				</el-card>
			</el-col>
			<el-col :span="12">
				<el-card shadow="hover">
					<template #header>
						<div class="clearfix">
							<span>账户编辑</span>
						</div>
					</template>
					<el-form ref = "formRef" label-width="90px" :model = "form" :rules="rules">
						<el-form-item label="用户名："> {{ name }} </el-form-item>
						<el-form-item label="旧密码：" prop = "old">
							<el-input type="password" v-model="form.old" placeholder="请输入旧密码"></el-input>
						</el-form-item>
						<el-form-item label="新密码：" prop = "new">
							<el-input type="password" v-model="form.new" placeholder="请输入新密码"></el-input>
						</el-form-item>
						<el-form-item label="个人简介：">
							<el-input v-model="form.desc" disabled="true"></el-input>
						</el-form-item>
						<el-form-item>
							<el-button type="primary" @click="onSubmit()">保存</el-button>
						</el-form-item>
					</el-form>
				</el-card>
			</el-col>
		</el-row>
		<el-dialog title="裁剪图片" v-model="dialogVisible" width="600px">
			<!-- <vue-cropper
				ref="cropper"
				:src="imgSrc"
				:ready="cropImage"
				:zoom="cropImage"
				:cropmove="cropImage"
				style="width: 100%; height: 400px"
			></vue-cropper> -->

			<template #footer>
				<span class="dialog-footer">
					<el-button class="crop-demo-btn" type="primary"
						>选择图片
						<input class="crop-input" type="file" name="image" accept="image/*" @change="setImage" />
					</el-button>
					<el-button type="primary" @click="saveAvatar">上传并保存</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="user">
import axios from 'axios';
import { reactive, ref } from 'vue';
import VueCropper from 'vue-cropperjs';
import { ElMessage, ElNotification} from 'element-plus';
import 'cropperjs/dist/cropper.css';
import type {FormInstance,FormRules } from 'element-plus';
import { useRouter } from 'vue-router';
import { useTagsStore } from '../store/tags';

import avatar from '../assets/img/user.jpg';
const avatarImg = ref(avatar);
const name = localStorage.getItem('ms_username');

// 定义表单数据
const form = reactive({
	old: '',
	new: '',
	desc: 'It is me！'
});
const rules = {
  // 验证规则
  old: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  new: [
    { required: true, message: '请输入新密码', trigger: 'blur' }
  ],
};
const router = useRouter();
const formRef = ref<FormInstance>(); // 表单引用
// 提交修改密码
function onSubmit() {
  formRef.value?.validate((valid) => {
    if (valid) {

	    // console.log(form.old, form.new)
	    // 发起请求
		axios.post('http://localhost:8080/user/changePassword', {username:name, old: form.old, new: form.new})
        .then(response => {
          if(response.data == 'Password changed successfully') {
			ElMessage.success("修改成功，请重新登录！")
			localStorage.clear(); // 清除全部信息
			router.push('/login');
		  }
		  else {
			ElMessage.error("修改失败，请重试！")
		  }
        })
        .catch(error => {
			ElMessage.error("修改失败，请重试！")
        });
		
    } else {
      // 
    }
  });
}

const tags = useTagsStore();
tags.clearTags(); // 清楚提示，如登录成功或者登录失败



const imgSrc = ref('');
const cropImg = ref('');
const dialogVisible = ref(false);
const cropper: any = ref();

const showDialog = () => {
	dialogVisible.value = true;
	imgSrc.value = avatarImg.value;
};

const setImage = (e: any) => {
	const file = e.target.files[0];
	if (!file.type.includes('image/')) {
		return;
	}
	const reader = new FileReader();
	reader.onload = (event: any) => {
		dialogVisible.value = true;
		imgSrc.value = event.target.result;
		cropper.value && cropper.value.replace(event.target.result);
	};
	reader.readAsDataURL(file);
};

const cropImage = () => {
	cropImg.value = cropper.value.getCroppedCanvas().toDataURL();
};

const saveAvatar = () => {
	avatarImg.value = cropImg.value;
	dialogVisible.value = false; // 关闭窗口
};
</script>

<style scoped>
.info {
	text-align: center;
	padding: 35px 0;
}
.info-image {
	position: relative;
	margin: auto;
	width: 100px;
	height: 100px;
	background: #f8f8f8;
	border: 1px solid #eee;
	border-radius: 50px;
	overflow: hidden;
}

.info-edit {
	display: flex;
	justify-content: center;
	align-items: center;
	position: absolute;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.5);
	opacity: 0;
	transition: opacity 0.3s ease;
}
.info-edit i {
	color: #eee;
	font-size: 25px;
}
.info-image:hover .info-edit {
	opacity: 1;
}
.info-name {
	margin: 15px 0 10px;
	font-size: 24px;
	font-weight: 500;
	color: #262626;
}
.crop-demo-btn {
	position: relative;
}
.crop-input {
	position: absolute;
	width: 100px;
	height: 40px;
	left: 0;
	top: 0;
	opacity: 0;
	cursor: pointer;
}
</style>
