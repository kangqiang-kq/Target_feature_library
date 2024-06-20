<template>
	<div class="container">
		<h2>热红外影像</h2>
		<div class="search-box">
			<el-button type="primary" @click="uploadImages"  class="upload-button"  text title="影像导入" v-if = "showButton">
				<i class="el-icon-lx-upload" style="font-size: 12px;">
                        影像导入
                </i>
			</el-button>

			<el-input class="search" size="large" v-model="query.keyword" clearable placeholder="请输入检索关键字" @keyup.enter = "handleSearch">>
			</el-input>
			<el-button type="success" @click="downloadImages" class="bordered-button" text title="导出当前页面的数据">
				<i class="el-icon-lx-down" style="font-size: 12px;">
                        影像导出
                </i>
			</el-button>
		</div>
		
		<div class="table-container">
				<div class="table-scroll">
					<el-table :data="imgList" border class="table" header-cell-class-name="table-header">
						<el-table-column prop="id" label="ID" width="150%" align="center"></el-table-column>
						<!-- <el-table-column prop="name" label="名称"></el-table-column> -->

						<el-table-column label="MMSI" align="center" width="280%">
							<template #default="scope">
								<div class="ellipsis">
									{{ scope.row.mmsi }}
								</div>
							</template>
						</el-table-column>
						<el-table-column label="来源" align="center" width = "500%">
							<template #default="scope">
								<div class="ellipsis">
									{{ scope.row.source }}
								</div>
							</template>
						</el-table-column>
						<el-table-column label="位置" align="center" width="200%">
							<template #default="scope">
								<div class="ellipsis">
									{{ scope.row.position }}
								</div>
							</template>
						</el-table-column>
						<el-table-column label="图片" align="center" width="300%">
							<template #default="scope">
								<el-image
									class="table-td-thumb"
									:src= "scope.row.imgpath"
									:z-index="10"
									:preview-src-list= "[scope.row.imgpath]" 
									preview-teleported
								>
								<!-- pre 大图 -->
								</el-image>
							</template>
						</el-table-column>
					</el-table>
				</div>
			</div>

		<!-- <ul>
			<li class="img-li" v-for="(item, index) in list" :key="index">
				<div class="img-li-content">
					<el-image
							:src= "item.imgpath"
							:z-index="10"
							:preview-src-list= "[item.imgpath]" 
							preview-teleported
						>

						</el-image>
					<span>{{ item.name }}</span>
				</div>
			</li>
		</ul> -->
		<!-- 表格 -->
		<div class="pagination">
				<el-pagination
					background
					layout="total, prev, pager, next, jumper"
					:current-page.sync="query.pageIndex"
					:page-size="query.pageSize"
					:total="query.pageTotal"
					@current-change="handlePageChange"
				></el-pagination>
		</div>
	</div>

	<el-dialog title="热红外影像导入" v-model="isVisible" width="850px" @close="handleDialogClose">
		<div class="dialog">
		<el-upload
			ref="uploadRef"
			class="upload-demo"
			action="http://localhost:8080/thermalInfrared/uploadImages"
			:auto-upload="false" 

			multiple
			:limit="1000"

			:on-exceed="handleExceed"
		>
			<!-- trigger 标记触发器 -->
			<template #trigger> 
			<el-button type="primary">select file</el-button>
			</template>

			<el-button class="ml-3" type="success" @click="submitUpload" style="margin-left: 30px;">
			upload to server
			</el-button>


	
		<template #tip>
			<div class="el-upload__tip">
				The limit is 1000.
			</div>
		</template>

		</el-upload>
		</div>
	</el-dialog>

</template>

<script setup lang="ts" name="icon">
import { computed, ref, reactive} from 'vue';
import {ElLoading,ElMessage, uploadBaseProps} from 'element-plus'
import avatar from '../assets/img/user.jpg'; // import img

import JSZip from 'jszip'; // 未知报错，有相关库
import { saveAs } from 'file-saver'; // node_modules 有， vscode编译器问题，可忽略
import axios from 'axios'
import { useRoute } from 'vue-router';

const route = useRoute();
const imgList = ref<any[]>([]) // 显示的数据
const filteredData = ref<any[]>([]); // 筛选出来的用于显示的数据
const resData = ref<any[]>([]); // 后端返回的数据
const showButton = ref(true);  // 按钮显示

const getData = () => {
	const loading = ElLoading.service({
            lock: true,
            text: "玩命加载中......",
            background: 'rgba(0, 0, 0, 0.7)',
        })
	if(route.params.mmsi == undefined) {
		axios.get('http://localhost:8080/thermalInfrared/getAllInfo').then((res) => {
		if(res.status == 200) {
			resData.value = res.data; 
			filteredData.value = res.data; // 一开始就算是筛选全部

			imgList.value = filteredData.value.slice(0, query.pageSize) // 初始的列表数据
			query.pageTotal = res.data.length; // 长度

			loading.close();
			ElMessage.success("数据加载成功！");
		}
	}).catch(() => {
		ElMessage.error("数据加载失败！");
	})
	}

	else {
		showButton.value = false; // 隐藏

		const formData = new FormData();
		formData.append('mmsi', route.params.mmsi); // 编译器问题，可忽略

		axios.post('http://localhost:8080/thermalInfrared/getInfo', formData).then((res) => {
		if(res.status == 200) {
			resData.value = res.data; // 获取后端实数
			filteredData.value = res.data; // 一开始就算是筛选全部

			imgList.value = filteredData.value.slice(0, query.pageSize) // 初始的列表数据
			query.pageTotal = res.data.length; // 长度

			loading.close();
			ElMessage.success("数据加载成功！")
		}
		}).catch(() => {

			loading.close();
			ElMessage.error("数据加载失败！");
		});
	}

}
getData(); // 获取数据

// 分页显示 ************************************************
const query = reactive({
	keyword: '', // 查询的内容，根据名字进行查询
	pageTotal: 0, // 总的页面数量
	pageIndex: 1, // 当前是在第几页
	pageSize: 10 // 每一页的大小
});
const handlePageChange = (val: number) => {
	query.pageIndex = val;
	let s = ((val - 1) * query.pageSize); // val 就是点击的 数字 第几页
	imgList.value = filteredData.value.slice(s, s + query.pageSize) // 列表数据 [0， 5）
};
// *******************************************************

// 搜索 ****************************************** 
const handleSearch = () => {
    if(query.keyword == '') {
        return ;
    }

    const queryInfo = query.keyword.toLowerCase(); // 小写
    const res = resData.value.filter(item => 
	item.mmsi.toLowerCase().includes(queryInfo) || item.source.toLowerCase().includes(queryInfo) || item.position.toLowerCase().includes(queryInfo));

    filteredData.value = res
    query.pageTotal = res.length // 表长
    query.pageIndex = 1 // 回到第一页

    imgList.value = filteredData.value.slice(0, query.pageSize)
	ElMessage.success("检索成功")
}
// ****************************************** 




// 导出********************************************************************* //
// 创建一个 Promise 对象，用于异步延迟
const delay = () => new Promise(resolve => setTimeout(resolve, 500));

async function downloadImages() {
  await delay(); // 等待异步延迟
  if(filteredData.value.length == 0) {
	ElMessage.error("无数据可导出")
	return;
  }

  const zip = new JSZip();
  // 导出选中的
  filteredData.value.forEach((item) => {
    const fileExtension = item.imgpath.split(';')[0].split('/')[1];
    const imageName = `${item.source + "_" + item.mmsi + "_" + item.position}.${fileExtension}`;
    const imageBlob = base64ToBlob(item.imgpath);
    zip.file(imageName, imageBlob);
  });


  zip.generateAsync({ type: 'blob' }).then((content) => {
    const zipName = 'thermalinfrared_image_folder.zip';
    saveAs(content, zipName);

  })

  ElMessage.success("下载完成！")
}
// 将Base64转换为Blob对象
function base64ToBlob(base64) {
  const byteCharacters = atob(base64.split(',')[1]);
  const byteArrays = new Uint8Array(byteCharacters.length);

  for (let i = 0; i < byteCharacters.length; i++) {
    byteArrays[i] = byteCharacters.charCodeAt(i);
  }

  const contentType = base64.split(';')[0].split(':')[1];
  const blob = new Blob([byteArrays], { type: contentType });
  return blob;
}


// 导入图像
const isVisible = ref(false); // 关闭
const uploadImages= ()=> {
	isVisible.value = true;
	// console.log(1)
}

import type { UploadInstance, UploadProps} from 'element-plus'

const uploadRef = ref<UploadInstance>() // 文件类型
const submitUpload = () => {
	// console.log(uploadRef.value)
	uploadRef.value?.submit()

	// 是否有值， ==》 submit到 action, 怎么接收呢
	// @RequestParam MultipartFile file
	// ResponseEntity 状态码是200就行
}

const handleExceed: UploadProps['onExceed'] = (files, uploadFiles) => {
  ElMessage.warning(
    `The limit is 1000, you selected ${files.length} files this time, add up to ${
      files.length + uploadFiles.length
    } totally`
  )
}

const handleDialogClose=()=> {
	getData(); 
}

</script>

<style scoped>
.example-p {
	height: 45px;
	display: flex;
	align-items: center;
}
.search-box {
	/* text-align: center; */
	margin-top: 30px;
	margin-bottom: 20px;
}
.search {
	width: 300px;
	margin-right: 30px;
}
ul,
li {
	list-style: none;
}
.img-li {
	display: inline-block;
	padding: 10px;
	width: 120px;
	height: 120px;
}
.img-li-content {
	display: flex;
	height: 100%;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	cursor: pointer;
}
.img-li-content i {
	font-size: 36px;
	color: #606266;
}
.img-li-content span {
	margin-top: 10px;
	color: #787878;
}
.bordered-button {
  /* border: 1px solid #1ba848; */
  padding: 15px 10px;
  /* 可选：添加其他样式 */
}
.upload-button {
	border: 1px solid #7ec5ff;
	padding: 15px 10px;
	margin-right: 30px;
}
.dialog{
	display: flex;
    align-items: baseline; /* 横向基线对齐 */
}

.table-container {
  overflow-x: auto;
  white-space: nowrap;
}

.table-scroll {
  display: inline-block;
}
.table-td-thumb {
	display: block;
	margin: auto;
	width: 40px;
	height: 40px;
}

.ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
