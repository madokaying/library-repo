const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

//跨域配置
module.exports = {
  devServer: {
    // 设置vue启动的端口号-
    port: 8081,
    // 代理配置
    proxy: {
      // 这里的api 表示如果我们的请求地址有/api的时候,就出触发代理机制
      // localhost:8888/api/abc  => 代理给另一个服务器
      // 本地的前端  =》 本地的后端  =》 代理我们向另一个服务器发请求 （行得通）
      // 本地的前端  =》 另外一个服务器发请求 （跨域 行不通）
      // 当我们的本地的请求 有/api的时候，就会代理我们的请求地址向另外一个服务器发出请求
      '/api': {
        target: 'http://localhost:8080', // 我们要代理的地址
        changeOrigin: true, // 是否跨域 需要设置此值为true 才可以让本地服务代理我们发出请求
        // 路径重写
        pathRewrite: {
          // 重写路径，替换请求地址中的指定路径
          '^/api': '/' // 将请求地址中的/api替换为空，也就是请求地址中不会包含/api/就需要这么做
        }
      },
    }
  }
}
/**注意：如果我们需要 代理的请求地址 和我们 本身的请求地址 一样的话，那么本身的地址就不需要写了，因为代理地址一样，也是和本身的地址请求，这样就无法解决跨域问题 如：
 这里地址一样，本身的地址就不需要再写
 VUE_APP_BASE_API = '/api'
 **/



