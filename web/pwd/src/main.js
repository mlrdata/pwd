import { createApp } from 'vue'
import App from './App.vue'
import './assets/main.css'

//引入路由
import router from './router'
//引入elementui
import 'element-plus/dist/index.css'
import ElementPlus from 'element-plus'
//引入图标库
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
//引入中文语言包
import zhCn from 'element-plus/es/locale/lang/zh-cn'
//引入服务器地址 
import host from './utils/env.js'
//引入session.js
import session from './utils/session.js'
//引入当前年月日.js
import nowDate from './utils/nowDate.js'
//引入请求库http.js
import http from './utils/http.js'
//添加事件管理者'passive'，来阻止'touchstart'事件，让页面更加流畅。 解决chrome下的warning问题
import "default-passive-events"; 


const app=createApp(App);


//将路由实例安装到vue上
app.use(router);
//安装elementui
app.use(ElementPlus,{
    locale: zhCn,
  });
//安装插件
app.use(http);

//增加全局属性
app.config.globalProperties.$host=host;
app.config.globalProperties.$session=session;

app.config.globalProperties.$nowDate=nowDate;
//将图标库全部注册为全局组件 
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
//挂载 
app.mount("#app");
