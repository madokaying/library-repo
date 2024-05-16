import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

Vue.use(VueRouter)
//避免vue-router在3.0版本以上重复点击菜单报错的问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

const routes = [
    {
        path: '/',
        redirect: '/mainBody/homePage'
    },
    {
        path: '/mainBody/:units',
        name: 'mainBody',
        component: () => import('@/components/element_components/MainBody'),
        props: true,//不声明props:true的话传参无效
    },
    {
        path: '/showCardView',//配置访问路径
        name: 'showCardView',//名字
        //配合<router-link to = "/xx">和<router-view>
        component: () => import('@/components/element_components/ShowCardView')//导入view，在vue中用
    },
    {
        path: '/myInfo/:units',
        name: 'myInfo',
        component: () => import('@/components/element_components/MyInfo'),
        props: true,
    },
    {
        path: '/bookDetail/:bookId',
        name: 'bookDetail',
        component: () => import('@/components/element_components/BookDetail'),
        props: true,
    }
]

const router = new VueRouter({
    routes,
//页面跳转显示在顶部
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition
        } else {
            return {x: 0, y: 0}
        }
    }
})

// 全局前置守卫
// router.beforeEach((to, from, next) => {
//     //如果当前路由是即将跳转的路由时取消导航，若不是则放行，不设置这个用push重复进入同一个路由会报错
//     if (to.path === from.path) {
//         // 取消导航
//         next(false);
//     } else {
//         next();
//     }
// });

// //前置守卫
// router.beforeEach(() => {
//     this.$store.commit('showLoading',true);
// })
// //后置钩子
// router.afterEach(() =>{
//     this.$store.commit('showLoading',false)
// })


export default router
