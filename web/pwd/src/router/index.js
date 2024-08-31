// //配置路由模板 
// //引入路由
import {
	createRouter,
	createWebHashHistory,
} from 'vue-router'
// //引入组件 
import Login from '@/views/common/Login.vue'

const routes = [{
		path: "/",
		component: Login
	},

]


let routerr = createRouter({
	history: createWebHashHistory(),
	routes
});


export default routerr;
