<template>
	<div>
		<div class="container">
			<div class="handle-box">
				<el-input v-model="query.mmsi" placeholder="请输入MMSI搜索" class="handle-input mr10"></el-input>
				<el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
				<!-- <el-button type="primary" :icon="Plus" @click = "handleAdd">新增</el-button> -->


			</div>

			<div class="table-container">
				<div class="table-scroll">
					<el-table :data="tableData" border class="table" header-cell-class-name="table-header">
						<el-table-column prop="id" label="ID" width="150%" align="center"></el-table-column>
					<!-- <el-table-column prop="name" label="名称"></el-table-column> -->

					<el-table-column label="MMSI" align="center" width="280%">
						<template #default="scope">{{ scope.row.mmsi }}</template>
					</el-table-column>
<!-- 
					<el-table-column label="名称" align="center" width="110%">
						<template #default="scope">{{ scope.row.name }}</template>
					</el-table-column> -->

					<el-table-column label="类型" align="center" width="110%">
						<template #default="scope">{{ scope.row.type }}</template>
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

	

					<el-table-column label="相关数据" align="center" width="360%" >
						<template #default="scope">
							<el-link target="_blank" style="margin-left: 10px; margin-right: 10px;" >
								<router-link :to="{ path: '/lowlight/' + scope.row.mmsi}" style="color:rgb(48, 92, 105);">微光影像</router-link>
							</el-link>

							<el-link target="_blank" style="margin-left: 10px; margin-right: 10px;" >
								<router-link :to="{ path: '/thermalinfrared/' + scope.row.mmsi}" style="color:rgb(173, 113, 113);">红外影像</router-link>
							</el-link>

							<el-link target="_blank" style="margin-left: 10px; margin-right: 10px;">
								<router-link :to="{ path: '/ais/' + scope.row.mmsi}" style="color:rgb(48, 91, 232);">AIS 数据</router-link>
							</el-link>
							<!-- 跳转事件 -->
							<!-- 等待单独页面完成 -->
							<!-- 8.30 红外、微光数据 数据页面 上传以及下载（下载当前页面，可选择的下载） -->
						</template>
					</el-table-column>

					<el-table-column label="操作" align="center" width="280%">
						<template #default="scope">
							<el-button :icon="Edit" @click="handleEdit(scope.$index, scope.row)" v-permiss="4">编 辑</el-button>
							<el-button :icon="Delete" class="red" @click="handleDelete(scope.$index,scope.row)" v-permiss="5">删  除</el-button>
						</template>
					</el-table-column>
					</el-table>
				</div>
			</div>

			<div class="pagination">
				<el-pagination
					background
					layout="total, prev, pager, next, jumper"
					:current-page.sync="query.pageIndex"
					:page-size="query.pageSize"
					:total="pageTotal"
					@current-change="handlePageChange"
				></el-pagination>
			</div>
		</div>

		<!-- 编辑弹出框 -->
		<el-dialog title="编辑" v-model="editVisible" width="450px">
			<el-form >
				<el-form-item label="MMSI">
					<el-input v-model="editForm.mmsi" disabled style="margin-left: 15px; width: 272px; margin-bottom: 20px;"></el-input>
				</el-form-item>

				<input type="file" style="display: none" ref="fileInput" @change="handleFileChange" accept="image/*">
				<div class="avatar-uploader" align="center">
					<div v-if="editForm.imageUrl" class="upload-text">修改图片</div>
					<div v-else="editForm.imageUrl" class="upload-text">添加图片</div>

					<div class="avatar-container">
					<img v-if="editForm.imageUrl" :src="editForm.imageUrl" class="avatar" @click="openFileInput" title="更改图片" />
					<el-icon v-else class="avatar-uploader-icon" @click="openFileInput" title="添加图片"><Plus /></el-icon>
					</div>
				</div>
			

			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="editVisible = false">取 消</el-button>
					<el-button type="primary" @click="saveEdit">确 定</el-button>
				</span>
			</template>
		</el-dialog>

		<!-- addInfo -->
		<el-dialog title="新增海上目标" v-model="addVisible" width="450px">
			<el-form >
				<el-form-item label="MMSI">
					<el-input v-model="addForm.mmsi" style="margin-left: 15px; width: 272px; margin-bottom: 20px;"></el-input>
				</el-form-item>

				<input type="file" style="display: none" ref="addImgInput" @change="handleImgFileChange" accept="image/*">
				<div class="avatar-uploader" align="center">
					<div v-if="addForm.imageUrl" class="upload-text">修改图片</div>
					<div v-else="addForm.imageUrl" class="upload-text">添加图片</div>

					<div class="avatar-container">
					<img v-if="addForm.imageUrl" :src="addForm.imageUrl" class="avatar" @click="addImgFileInput" title="更改图片" />
					<el-icon v-else class="avatar-uploader-icon" @click="addImgFileInput" title="添加图片"><Plus /></el-icon>
					</div>
				</div>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="addVisible = false">取 消</el-button>
					<el-button type="primary" @click="submitAdd">确 定</el-button>
				</span>
			</template>
		</el-dialog>

	</div>
</template>

<script setup lang="ts">
import axios from 'axios'; // 引入 axios
import { ref, reactive, onMounted} from 'vue';
import { ElMessage, ElMessageBox, UploadProps, formContextKey} from 'element-plus'; // 弹窗msg
import { Delete, Edit, Search, Plus, Apple,Chicken} from '@element-plus/icons-vue'; // 引入图标
import { getAllShips } from '../api/index'; // api 引入
import { useRouter} from 'vue-router'

interface TableItem {
	id: number;
	mmsi: string; // 水上移动业务标识码
	imgpath: string;
}

const tableData = ref<TableItem[]>([]); // 只显示一页的数据
const pageTotal = ref(0); // 表长度

const listData = ref<TableItem[]>([]); // 获取的
const filteredData = ref<TableItem[]>([]); // 筛选出来的用于显示的数据

const query = reactive({
	mmsi: '', // 查询的内容，根据名字进行查询

	pageIndex: 1, // 当前是在第几页
	pageSize: 10 // 每一页的大小
});

// 获取表格数据，定义函数
const getData = () => { // 无参函数
	getAllShips().then(res => {
		listData.value = res.data;
		filteredData.value = listData.value; // 一开始就算是筛选全部

		tableData.value = filteredData.value.slice(0, query.pageSize) // 初始的列表数据
		pageTotal.value = res.data.length; // 长度

	});
};
getData(); // 页面加载触发

// 查询操作
const handleSearch = () => {
	// if(query.mmsi == "") return ; 空搜索应该显示全部
	const res = listData.value.filter(item => item.mmsi.includes(query.mmsi)); // 模糊查询
	filteredData.value = res; 
	tableData.value = filteredData.value.slice(0, query.pageSize);

	pageTotal.value = filteredData.value.length; // 表长度，所有数据
	query.pageIndex = 1; // 查完回到第一页

	if(query.mmsi != "") ElMessage.success("搜索成功，共搜索到"+ filteredData.value.length +"条记录！")
};
// 分页导航
const handlePageChange = (val: number) => {
	query.pageIndex = val;
	let s = ((val - 1) * query.pageSize); // val 就是点击的 数字 第几页
	tableData.value = filteredData.value.slice(s, s + query.pageSize) // 列表数据 [0， 5）
};

// 删除操作
const handleDelete = (index: number, row: any) => {
	// 二次确认删除
	ElMessageBox.confirm('确定要删除吗？', '提示', {
		type: 'warning'
	})
		.then(() => {
			const formData = new FormData();
			formData.append('id', row.id);
	
			axios.post('http://localhost:8080/img/deleteShipInfo', formData).then(res => {
				if(res.data == 'ok') {
					ElMessage.success('删除成功！');
					// 删除

					// 删除这一页的数据，删完就跳转
					tableData.value.splice(index, 1); // 删除当前的一个, 索引下标删除
					pageTotal.value --; // 总数减一
				}
				else {
					ElMessage.error('删除失败！');
				}
			});
		})
		.catch((error) => {
			ElMessage.error('删除失败！');
		});
};

/* 编辑info ******************************************************************************/
const editVisible = ref(false);
let editForm = reactive({
	mmsi: '',
	imageUrl: '',
});

let idx: number = -1; // 记录下标
const handleEdit = (index: number, row: any) => {
	idx = index;
	editForm.mmsi = row.mmsi;
	editForm.imageUrl = row.imgpath;
	editVisible.value = true;
};

let file: File | undefined; // 修改的img
const saveEdit = () => {
	if(file) {
		const formData = new FormData();
		formData.append('file', file);
		formData.append('mmsi', editForm.mmsi);

		axios.post('http://localhost:8080/img/updateShipInfo', formData)
			.then(response => {
			if(response.data == 'ok') {
				editVisible.value = false;
				ElMessage.success(`修改第 ${idx + 1} 行成功`);
				tableData.value[idx].imgpath = editForm.imageUrl;  // 图像地址修改
				// ID 以及 MMSI 不变
			}
			else {
				ElMessage.error("修改失败，请重试！")
			}
			})
			.catch(error => {
				ElMessage.error("修改失败，请重试！")
			});
	}
	else {
		ElMessage.error("修改失败，请重试！")
	}
};

const fileInput = ref<HTMLInputElement | null>(null); // 绑定 input 组件
const openFileInput = () => {
  	fileInput.value?.click(); // 点击 ref 关联 input, 触发input --> handleFileChange
	// 这样就实现了，点击图片 触发input
};

const handleFileChange = (event: Event) => {
  file = (event.target as HTMLInputElement).files?.[0];
  if (file) {
	editForm.imageUrl = URL.createObjectURL(file); // 窗口显示问题
  }
};
/***********************************************************************************/

/* add info ******************************************************************************/
let addForm = reactive({
	mmsi: '',
	imageUrl: '',
});
const addVisible = ref(false); // 弹出框是否可见


const handleAdd = () => { // add 函数， 启动按钮
	addForm.mmsi = '';
	addForm.imageUrl = ''; // 先清空
	addVisible.value = true; // 弹出
};


const addImgInput = ref<HTMLInputElement | null>(null); // 绑定 input 组件 ref
const addImgFileInput = () => { // 被点了
	addImgInput.value?.click(); // 转换到 input 被点了
};

let addImgFile : File | undefined; // 新加的文件
const handleImgFileChange = (event: Event) => {
	addImgFile = (event.target as HTMLInputElement).files?.[0];
	if (addImgFile) {
		addForm.imageUrl = URL.createObjectURL(addImgFile); // 窗口显示问题
	}
};

const submitAdd = () => { // 提交
	if(addImgFile && addForm.mmsi) {
		const formData = new FormData(); // FormDate()
		
		formData.append('file', addImgFile); // 图像文件
		formData.append('mmsi', addForm.mmsi);

		console.log(formData);

		// axios.post(url, formData).then(res => {do something});
		axios.post('http://localhost:8080/img/addShipInfo', formData).then(res => {
			if(res.data == "repeat") {
				ElMessage.error("重复添加，请重试！");
				return ;
			}
			if(res.data == "ok") {
				addVisible.value = false;
				ElMessage.success("添加成功！");
				getData(); // 重新获取表单数据
			}
		})
		.catch(error => {
			ElMessage.error("添加失败，请重试！")
		})

	}
	else {
		ElMessage.error("新增信息不为空，请重试！")
	}
};
/***********************************************************************************/


/* 页面传值 跳转 ais******************************************************************************/
const router = useRouter(); // 路由跳转，例如后续的push("/")
/***********************************************************************************/
</script>

<style scoped>
.handle-box {
	margin-bottom: 20px;
}

.handle-select {
	width: 120px;
}

.handle-input {
	width: 300px;
}
.table {
	width: 100%;
	font-size: 14px;
}
.red {
	color: #F56C6C;
}
.mr10 {
	margin-right: 10px;
}
.table-td-thumb {
	display: block;
	margin: auto;
	width: 40px;
	height: 40px;
}
.avatar-uploader {
  display: flex;
  align-items: center;
}
.upload-text {
  font-size: 14px;
  color: #333;
  margin-right: 10px;
}

.avatar-container {
  display: flex;
  align-items: center;
}
.avatar-uploader .avatar {
  width: 256px;
  height: 256px;
  display: block;
  cursor: pointer; /* 触发title */

  border: 1px solid #c1c1c1; /* 设置边框宽度和颜色 */
  padding: 1px; /* 内边距 */
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 196px;
  height: 196px;
  text-align: center;
  cursor: pointer; /* 触发title */
  border: 1px solid #c1c1c1; /* 设置边框宽度和颜色 */

}

.hovered:hover {
  background-color: #ff0707;
}
.table-container {
  overflow-x: auto;
  white-space: nowrap;
}

.table-scroll {
  display: inline-block;
}
</style>
