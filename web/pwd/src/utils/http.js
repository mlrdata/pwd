import axios from 'axios'
import { ElMessage } from 'element-plus'
import host from './env.js'

 axios.defaults.baseURL=host		//后端接口

// 添加响应拦截器
axios.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
	if(response.code == 200){
		return Promise.resolve(response.data);
	}
	
	
	
    return response;
  }, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
  });

//封装的请求对象 
const tmp={
    //get请求
    get(url,params){
      return  axios.get(url,{
            params
        })
    },

    //post请求
    post(url,data){
        return axios.post(url,data);
    },
	put(url,data){
	
	    return axios.put(url,data);
	},
	delete(url,params){
		return axios.delete(url,{params});
	}
	
}

export default {
    install(app){
        app.config.globalProperties.$http=tmp;
    }
}
