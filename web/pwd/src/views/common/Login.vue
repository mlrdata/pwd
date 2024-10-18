<!--vue模版-->
<template>

	<div class="common-layout">
		<el-container>
			<el-header>
				<div style="background-color: #fff;">
					<div id="top">

						<el-button type="primary" @click="download()" style="margin-right: 20px;">
							加密
						</el-button>

						<el-upload class="upload-demo" action="http://localhost:35589/pwd/decrypt" :data="key"
							:limit="1" :on-success="upload" :on-error="upError" :show-file-list="false">
							<el-button type="primary">解密</el-button>
						</el-upload>

						<el-button type="success" @click="refresh()" plain  round style="margin-left: 130px;">
							刷新<el-icon size="large" style="margin-left: 5px;"><RefreshRight /></el-icon>
						</el-button>
						<el-input v-model="fileName" style="margin-left: 60px;" placeholder="可不填,加密文件名最后追加标识" />

					</div>
					<div style="padding: 20px;">
						<span style="color: red;">*</span>
						<span>秘钥：</span>
						<el-input v-model="key.userKey" style="width: 240px" placeholder="请输入秘钥" show-password />

						<el-button style="margin-left: 300px;" @click="addDomain" type="primary" plain>新增分类</el-button>

					</div>
				</div>
			</el-header>
			<el-container>
				<el-aside>
					<el-tree style="max-width: 300px" :data="form" :props="defaultProps" node-key="id" ref="treeRef"
						@node-click="handleCheckAll" />
				</el-aside>
				<el-main>
					<el-form :model="form" label-width="auto" class="demo-form-inline" size="small">

						<el-collapse v-for="(plainText, index) in form" :key="index">
							<el-collapse-item :name="index+1">
								<template #title>
									<el-form-item :label="'所属分类' + (index+1)" :id="'name'+plainText.id">
										<el-input v-model="plainText.title" />

										<el-button class="mt-2" style="margin-left: 100px;"
											@click.prevent="removeDomain(index)" type="danger" plain>
											删除分类{{index+1}}
										</el-button>
									</el-form-item>
								</template>
								<div>
									<el-button @click="additem(index)" type="success" plain>新增项目</el-button>
									<el-collapse-item :name="(index+1)+'_' +(index2+1)"
										v-for="(item, index2) in plainText.child" :key="index2" :id="'name'+item.id">

										<template #title>
											<el-form-item>
												<el-form-item :label="'项目名称' +(index+1)+'_' +(index2+1)">
													<el-input v-model="item.title" />
												</el-form-item>
												<el-form-item label="账户">
													<el-input v-model="item.name" />
												</el-form-item>
											</el-form-item>
										</template>
										<div>
											<el-form-item>
												<el-form-item label="密码">
													<el-input v-model="item.pwd" show-password />
												</el-form-item>
												<el-form-item label="手机号">
													<el-input v-model="item.phone" />
												</el-form-item>
											</el-form-item>
											<el-form-item>
												<el-form-item label="身份信息">
													<el-input v-model="item.idname" />
												</el-form-item>
												<el-form-item label="身份证号">
													<el-input v-model="item.idCard" show-password />
												</el-form-item>
											</el-form-item>
											<el-form-item>
												<el-form-item label="注册时间">
													<el-date-picker v-model="item.time" type="date"
														placeholder="选择日期" />
												</el-form-item>
												<el-form-item label="其他">
													<el-input v-model="item.desc" type="textarea" />
												</el-form-item>
											</el-form-item>

											<el-form-item>
												<el-button class="mt-2" @click.prevent="removeItem(index,index2)"
													type="danger" plain>
													删除项目{{index+1}}_{{+index2+1}}
												</el-button>
											</el-form-item>

										</div>
									</el-collapse-item>
								</div>
							</el-collapse-item>
						</el-collapse>
					</el-form>
				</el-main>
			</el-container>
		</el-container>
	</div>

	<el-backtop :right="80" :bottom="80" :visibility-height="100" target=".el-main" />

</template>

<!--vue  组件选项定义-->
<script>
	export default {
		data() {
			return {
				form: [{
					id: "C1",
					number: "1",
					title: '-',
					child: [{
						id: "1_1",
						number: "1_1",
						title: '-',
					}]
				}],
				key: {
					userKey: ""
				},
				fileName: '',
				defaultProps: {
					children: 'child',
					label: 'title',
				},
			}
		},
		created() {
			
		},
		methods: {
			download() {
				this.$http.post('/pwd/encryption', {
						plainText: JSON.stringify(this.form),
						userKey: this.key.userKey,
						fileName: this.fileName
					})
					.then(response => {
						if (response.data.code) {
							this.$message({
								message: response.data.msg,
								type: 'error',
							});
							return;
						} else if (response.status == 200) {
							this.$message({
								message: "加密文件生成成功，文件保存在项目所在文件夹",
								type: 'success',
							});
						}
					})
					.catch(error => {
						this.$message({
							message: "加密失败，请检查服务是否正常启动",
							type: 'error',
						});
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
				if (res) {
					res.forEach((item, i) => {
						item.id = "C" + (i + 1); // 注意i从0开始，所以使用i + 1
						item.child.forEach((child, j) => {
							child.id = (i + 1) + "_" + (j + 1); // 同样，j从0开始
						});
					});
					this.form = res;
				}
			},
			upError(res) {
				this.$message({
					message: "文件上传失败，请检查服务是否正常启动",
					type: 'error',
				});
			},
			addDomain() {
				let index = this.form.length + 1;
				if (this.form.length == 0) {
					this.form.push({
						id: "C" + index,
						title: '-',
						child: [{
							id: index + "_1",
							title: '-',
						}]
					});
				} else {
					let id = Number(this.form[this.form.length - 1].id.slice(1));
					this.$refs.treeRef.insertAfter({
						id: "C" + (id + 1),
						title: '-',
						child: [{
							id: (id + 1) + "_1",
							title: '-',
						}]
					}, "C" + id);
				}

			},
			removeDomain(index) {
				this.$refs.treeRef.remove(this.form[index]);
			},
			handleCheckAll(index) {
				this.$nextTick(() => {
					let memberDom = document.querySelector('#name' + index.id);
					memberDom.scrollIntoView({
						behavior: 'smooth',
						block: 'center'
					})
				})
			},
			additem(index) {
				let fid = this.form[index].id.slice(1);
				if (this.form[index].child.length == 0) {
					this.$refs.treeRef.append({
						id: fid + "_1",
						title: '-',
					}, "C" + fid);
				} else {
					let index2 = this.form[index].child.length;
					let zid = this.form[index].child[index2 - 1].id;
					const index3 = zid.indexOf("_");
					const result = Number(zid.slice(index3 + 1));
					this.$refs.treeRef.append({
						id: fid + "_" + (result + 1),
						title: '-',
					}, "C" + fid);
				}
			},
			removeItem(index, index2) {
				this.$refs.treeRef.remove(this.form[index].child[index2]);
			},
			refresh() {
				location.reload();
			},
		},
	}
</script>

<!--vue  scoped当前样式只适用于当前组件-->
<style scoped>
	#top {
		/* padding: 20px; */
		padding: 15px 60px 5px 60px;
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

	.common-layout {
		background-color: #fff;
		height: 100%;
	}

	:deep(.el-container) {
		background-color: #fff;
		height: 90%;
	}

	:deep(.el-header) {
		background-color: #fff;
		height: auto;
		margin: 10px;
		border-radius: 10px;
		border: rgb(197.7, 225.9, 255) solid 10px;
	}

	:deep(.el-aside) {
		background-color: #fff;
		height: 100%;
		border-radius: 5px;
		border: rgb(216.8, 235.6, 255) solid 5px;
		margin: 10px;
		padding-top: 10px;
		padding-right: 20px;
		width: auto;
	}

	:deep(.el-main) {
		background-color: #fff;
		border: rgb(235.9, 245.3, 255) solid 20px;
		border-radius: 10px;
		height: 100%;
		margin: 10px;

	}



	/**
	  头部样式 
	*/
	.header {
		color: #fff;
		display: flex;
		/*   弹性布局*/
		align-items: center;
		/** 控制容器内的元素  垂直方向的对齐方式 */
		justify-content: flex-end;
		/** 控制容器内的元素 水平方向的对齐方式 */
	}

	:deep(.el-dropdown) {
		margin-right: 30px;
	}

	/**左侧导航  */

	.leftnav a {
		display: block;
		width: 196px;
		height: 70px;
		line-height: 70px;
		color: #fff;
		font-size: 16px;
		font-weight: bold;
		text-decoration: none;
		margin: auto;
		text-align: center;
	}

	:deep(.el-collapse-item) {
		border: none;
	}

	:deep(.el-collapse) {
		border: none;
	}

	:deep(.el-collapse-item__wrap) {
		padding-top: 5px;
		border: none;
	}

	:deep(.el-collapse-item__header) {
		border: none;
		height: auto;
	}

	:deep(.el-form-item) {
		margin-bottom: 10px;
	}
</style>