<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <el-upload
                    action="#"
                    :limit="1"
                    accept=".xlsx, .xls"
                    :show-file-list="false"
                    :before-upload="beforeUpload"
                    :http-request="handleMany"
                >
                    <el-button class="mr10" type="success" v-if = "showButton">
                        <i class="el-icon-lx-upload" style="font-size: 10px;">
                            批量导入
                        </i>
                    </el-button>
                </el-upload>

                <el-link href="/template.xlsx">
                    <i class="el-icon-lx-down" style="font-size: 12px;">
                        模版下载
                    </i>
                </el-link>
            
                <el-input class="search" size="large" v-model="keyword" clearable placeholder="请输入检索关键字" style="width: 30%; margin-right: 20px; margin-left: 70px;" @keyup.enter="handleSearch">
			    </el-input>

                <el-button text type="primary" @click="exportToExcel">导出Excel</el-button>
            </div>

            <el-table :data="tableData" border class="table"  width = "100%"  height = "70vh">
                <!-- <el-table-column type="selection" width="55" /> 后续再完善吧-->
                <el-table-column prop="id" fixed label="ID"  align="center" width="100%"></el-table-column>
                <el-table-column prop="mmsi" fixed label="MMSI" align="center" width="110%"></el-table-column>

                <el-table-column prop="aisname" label="AisName" align="center" width="110%"></el-table-column>
                <el-table-column prop="callsign" label="CallSign" align="center" width="110%"></el-table-column>
                <el-table-column prop="an" label="An" align="center" width="120%"></el-table-column>
                <el-table-column prop="color" label="Color" align="center" width="110%"></el-table-column>
                <el-table-column prop="course" label="Course" align="center" width="110%"></el-table-column>
                <el-table-column prop="destination" label="Destination" align="center" width="110%"></el-table-column>
                <el-table-column prop="dn" label="Dn" align="center" width="110%"></el-table-column>
                <el-table-column prop="dwt" label="Dwt" align="center" width="110%"></el-table-column>
                <el-table-column prop="eta" label="Eta" align="center" width="180%"></el-table-column>
                <el-table-column prop="fn" label="Fn" align="center" width="150%"></el-table-column>
                <el-table-column prop="heading" label="Heading" align="center" width="110%"></el-table-column>
                <el-table-column prop="hide" label="Hide" align="center" width="110%"></el-table-column>
                <el-table-column prop="imonumber" label="ImoNumber" align="center" width="110%"></el-table-column>
                <el-table-column prop="lat" label="Latitude" align="center" width="180%"></el-table-column>
                <el-table-column prop="length" label="Length" align="center" width="110%"></el-table-column>
                <el-table-column prop="lon" label="Longitude" align="center" width="180%"></el-table-column>
                <el-table-column prop="minotypecode" label="Minotype Code" align="center" width="110%"></el-table-column>
                <el-table-column prop="minotype" label="Minotype" align="center" width="110%"></el-table-column>
                <el-table-column prop="minudiff" label="Minudiff" align="center" width="110%"></el-table-column>
             
                <el-table-column prop="name" label="Name" align="center" width="250%"></el-table-column>
                <el-table-column prop="offseta" label="OffsetA" align="center" width="110%"></el-table-column>
                <el-table-column prop="offsetb" label="OffsetB" align="center" width="110%"></el-table-column>
                <el-table-column prop="offsetc" label="OffsetC" align="center" width="110%"></el-table-column>
                <el-table-column prop="offsetd" label="OffsetD" align="center" width="110%"></el-table-column>
                <el-table-column prop="rs" label="RS" align="center" width="110%"></el-table-column>
                <el-table-column prop="speed" label="Speed" align="center" width="110%"></el-table-column>
                <el-table-column prop="status" label="Status" align="center" width="110%"></el-table-column>
                <el-table-column prop="statuscode" label="StatusCode" align="center" width="110%"></el-table-column>
                <el-table-column prop="trawlsonar" label="Trawlsonar" align="center" width="110%"></el-table-column>
                <el-table-column prop="turnrate" label="Turnrate" align="center" width="110%"></el-table-column>
                <el-table-column prop="type" label="Type" align="center" width="110%"></el-table-column>
                <el-table-column prop="typecode" label="TypeCode" align="center" width="110%"></el-table-column>
                <el-table-column prop="updatetimeinfo" label="UpdateTimeInfo" align="center" width="110%"></el-table-column>
                <el-table-column prop="updatetime" label="UpdateTime" align="center" width="180%"></el-table-column>
                <el-table-column prop="updatetimestamp" label="UpdateTimestamp" align="center" width="180%"></el-table-column>
                <el-table-column prop="width" label="Width" align="center" width="110%"></el-table-column>
                <el-table-column prop="acqtime" label="AcqTime" align="center" width="180%"></el-table-column>
            </el-table>
		

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
    </div>
</template>

<script setup lang="ts" name="import">
import axios from 'axios';
import { ElMessage, UploadProps, ElLoading} from 'element-plus';
import { ref, reactive, computed} from 'vue';
import * as XLSX from 'xlsx';
import { useRoute } from 'vue-router';
import router from '../router';
const route = useRoute();

// 分页渲染********************************************************************************************** //
// 分页导航
const handlePageChange = (val: number) => {
	query.pageIndex = val;
	let s = ((val - 1) * query.pageSize); // val 就是点击的 数字 第几页
	tableData.value = fiterDate.value.slice(s, s + query.pageSize) // 列表数据 [0， 5）
};

// 万条级别数据渲染 卡顿严重
const pageTotal = ref(0); // 表长度
const query = reactive({
	pageIndex: 1, // 当前是在第几页
	pageSize: 100 // 每一页的大小
});
// ****************************************************************************************************** //


// 获取数据*********************************************************************************************** //
const resData = ref<any>([]); // slice
const tableData = ref<any>([]); // 显示的数据
const showButton = ref(true);  // 按钮显示

function getData(): void {
    const loading = ElLoading.service({
            lock: true,
            text: "数据玩命加载中......",
            background: 'rgba(0, 0, 0, 0.7)',
        })
    // 获取数据
    if(route.params.id == undefined) {
        axios.get('http://localhost:8080/ais/getAllAis').then((res) => {
        if(res.status == 200) {
            resData.value = res.data; // 获取后端实数
            pageTotal.value =  resData.value.length
            tableData.value = resData.value.slice(0,100) // 取前100条

            // 一开始默认全部筛选
            fiterDate.value = res.data

            loading.close();
            ElMessage.success("数据加载成功！")
        }
        }).catch(() => {

            loading.close();
            ElMessage.error("数据加载失败！");
        });
    }

    else {
        showButton.value = false; // 隐藏

        const formData = new FormData();
        formData.append('mmsi', route.params.id); // 编译器问题，可忽略

        axios.post('http://localhost:8080/ais/getAis', formData).then((res) => {
        if(res.status == 200) {
            resData.value = res.data; // 获取后端实数
            pageTotal.value =  resData.value.length
            tableData.value = resData.value.slice(0,100) // 取前100条

            // 一开始默认全部筛选
            fiterDate.value = res.data

            loading.close();
            ElMessage.success("数据加载成功！")
        }
        }).catch(() => {

            loading.close();
            ElMessage.error("数据加载失败！");
        });
    }


};
getData(); // 获取原数据
// ****************************************************************************************************** //

// 解析导入*********************************************************************************************** //
const importList = ref<any>([]);
const beforeUpload: UploadProps['beforeUpload'] = async (rawFile) => {
    importList.value = await analysisExcel(rawFile); // excel的数据
    return true;
};
const analysisExcel = (file: any) => {
    return new Promise(function (resolve, reject) {
        const reader = new FileReader();
        reader.onload = function (e: any) {
            const data = e.target.result;
            let datajson = XLSX.read(data, {
                type: 'binary',
            });
            const sheetName = datajson.SheetNames[0];
            const result = XLSX.utils.sheet_to_json(datajson.Sheets[sheetName]);
            resolve(result);
        };
        reader.readAsBinaryString(file);
    });
};
const handleMany = () => {
    const loading = ElLoading.service({
            lock: true,
            text: "正在拼命上传中......",
            background: 'rgba(0, 0, 0, 0.7)',
        })
    axios.post('http://localhost:8080/ais/uploadAis', importList.value).then((res) => {
        if(res.status == 200) {
            // resData.value.push(...importList.value); // push
            // pageTotal.value =  resData.value.length; // 修改 总条数

            // query.pageIndex = 1;
            // tableData.value = resData.value.slice(0,100);

            // 没有id
            getData(); // 重新获取

            loading.close();
            ElMessage.success("导入成功！");
        }
        else {
            loading.close();
            ElMessage.error("导入失败！");
            return ;
        }
    }).catch((error)=> {
        loading.close();
        ElMessage.error("ERROR!")
    })

};
// *********************************************************************************************** //


// 导出 Excel************************************************************************************* //
const exportToExcel = () => {
  const loadingInstance = ElLoading.service({
        lock: true,
        text: "正在导出中......",
        background: 'rgba(0, 0, 0, 0.7)',
  }); 
  // 创建一个 Promise 对象，用于异步延迟
  const delay = () => new Promise(resolve => setTimeout(resolve, 100));

  // 使用 async/await 来确保加载动画先显示
  async function exportAsync() {
    await delay(); // 等待异步延迟
    try {
        const worksheet = XLSX.utils.json_to_sheet(fiterDate.value); // 导出的是什么
        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet1');
        const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
        const blob = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a'); // ?
        link.href = url;
        link.setAttribute('download', 'data.xlsx');

        document.body.appendChild(link);
        link.click(); // 点击下载
        document.body.removeChild(link);

        loadingInstance.close(); // 关闭
        ElMessage.success('导出成功！'); 
    } catch (e) {
        loadingInstance.close(); // 关闭
        ElMessage.error('导出失败！'); 
    }
  }
  
  exportAsync(); 
}
// ********************************************************************************************* //

// 下载选中 //
// ing
// ********************************************************************************************* //


// 检索 //
const fiterDate = ref<any>([]); // 筛选出来的

const keyword = ref(''); // 双向绑定 :v-model，检索
const handleSearch = () => {
    if(keyword.value == '') {
        // fiterDate.value = resData.value

        // pageTotal.value = fiterDate.length // 表长
        // // query.pageIndex = 1 // 回到第一页

        // tableData.value = fiterDate.value.slice(0, 100)
        return ;
    }

    const queryInfo = keyword.value.toLowerCase(); // 小写
    const res = resData.value.filter(row => {
        return Object.values(row).some(value => {
        if (typeof value === 'string') {
            return value.toLowerCase().includes(queryInfo);
        }
        return false;
        });
    });

    fiterDate.value = res
    pageTotal.value = res.length // 表长
    query.pageIndex = 1 // 回到第一页

    tableData.value = fiterDate.value.slice(0, query.pageSize)
    ElMessage.success("检索成功")
    
}
// **********************************************************************************************//
</script>

<style scoped>
.handle-box {
    display: flex;
    margin-bottom: 20px;

    display: flex;
    align-items: baseline; /* 横向基线对齐 */
}

.table {
    width: 100%;
    font-size: 14px;
}
.mr10 {
    margin-right: 20px;
}


</style>
