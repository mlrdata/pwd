<!--vue模版-->
<template>
	<el-affix :offset="0">
		<div style="background-color: #fff;">
			<div id="top">

				<el-button type="primary" @click="download()" style="margin-right: 20px;">
					加密
				</el-button>

				<el-upload class="upload-demo" action="http://localhost:35589/pwd/decrypt" :data="key" :limit="1"
					:on-success="upload" :show-file-list="false">
					<el-button type="primary">解密</el-button>
				</el-upload>

			</div>
			<div style="padding: 20px;">
				<span>秘钥：</span>
				<el-input v-model="key.userKey" style="width: 240px" placeholder="请输入秘钥" show-password />
			</div>
		</div>
	</el-affix>
	<el-form :model="form" label-width="auto" class="demo-form-inline">
		<el-affix :offset="120">
			<el-button style="margin-left: 700px;" @click="addDomain" type="success" plain>新增项目</el-button>
		</el-affix>

		<div v-for="(plainText, index) in form" :key="index">
			<el-form-item>
				<el-form-item :label="'所属项目' + (index+1)">
					<el-input v-model="plainText.project" />
				</el-form-item>
				<el-form-item label="账户">
					<el-input v-model="plainText.name" />
				</el-form-item>
			</el-form-item>
			<el-form-item>
				<el-form-item label="密码">
					<el-input v-model="plainText.pwd" show-password />
				</el-form-item>
				<el-form-item label="手机号">
					<el-input v-model="plainText.phone" />
				</el-form-item>
			</el-form-item>
			<el-form-item>
				<el-form-item label="身份信息">
					<el-input v-model="plainText.idname" />
				</el-form-item>
				<el-form-item label="身份证号">
					<el-input v-model="plainText.idCard" show-password />
				</el-form-item>
			</el-form-item>
			<el-form-item>
				<el-form-item label="注册时间">
					<el-date-picker v-model="plainText.time" type="date" placeholder="选择日期"  />
				</el-form-item>
				<el-form-item label="其他">
					<el-input v-model="plainText.desc" type="textarea" />
				</el-form-item>
			</el-form-item>

			<el-form-item>
				<el-button class="mt-2" @click.prevent="removeDomain(index)" type="danger" plain>
					删除项目{{index+1}}
				</el-button>
			</el-form-item>
		</div>

	</el-form>
	<el-backtop :right="40" :bottom="40" :visibility-height="100" />
</template>

<!--vue  组件选项定义-->
<script>
	export default {
		data() {
			return {
				form: [{}],

				key: {
					userKey: ""
				},
			}
		},
		created() {


		},
		methods: {
			download() {
				this.$http.post('/pwd/encryption', {
						plainText: JSON.stringify(this.form),
						userKey: this.key.userKey
					})
					.then(response => {
						console.log("resp", response);
						if (response.data.code) {
							this.$message({
								message: response.data.msg,
								type: 'error',
							});
							return;
						}
						// 处理响应数据，例如下载文件
						const url = window.URL.createObjectURL(new Blob([response.data], {
							type: "application/octet-stream"
						}));
						const link = document.createElement('a');
						link.href = url;
						link.setAttribute('download', 'pwd'); // 设置文件名，不带扩展名
						document.body.appendChild(link);
						link.click();
					})
					.catch(error => {
						console.error('Download error:', error);
					});
			},
			upload(res) {
				if (res.code) {
					this.$message({
						message: res.msg,
						type: 'error',
					});
					return;
				}
				this.form = res;
			},
			addDomain() {
				this.form.push({
					plainText: {}
				})
			},
			removeDomain(index) {
				if (index !== -1) {
					this.form.splice(index, 1)
				}
			},
		},
	}
</script>

<!--vue  scoped当前样式只适用于当前组件-->
<style scoped>
	#top {
		padding: 20px;
		display: flex;
	}

	.demo-form-inline {
		max-width: 750px;
	}

	.el-input {
		width: 220px;
	}

	.el-textarea {
		width: 220px;
	}
</style>