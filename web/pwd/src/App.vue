<!--vue模板-->
<template>
 <div class="root">
      <!-- 路由出口 -->
  <!-- 路由匹配到的组件将渲染在这里 -->
  <router-view></router-view>
    </div>
</template>

<!--vue 组件选项定义-->
<script>
   export default{
       data(){
         return {

         }
       }
   }
</script>

<!--vue样式 scoped当前样式只适用于当前组件-->
<style scoped>
  .root{
    width: 100%;
    height: 100%;
  }
</style>