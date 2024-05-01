import Vue from 'vue'
import Vuex from 'vuex'
import http from '@/http/http'
import { Loading } from 'element-ui';


Vue.use(Vuex)

export default new Vuex.Store({
    //数据，相当于data
    state:{
        //记录用户登录状态
        userInfo:{
            token:'',//jwt
            nickname:'',//用户昵称
            avatar:'',//头像地址
            background:'',//背景地址
            email:'',//邮箱
            signature:'',//个性签名
            maxBorrow:'',//最多可借书数
            needToPay:'',//待缴金额
            role:'',//用户角色
            UID:'',//角色UID
        },
        //记录查询到的书籍信息
        bookInfo:{
            bookList:[],
            total:0,
        },
        options:{
            lock: false,
            text: '加载中，请稍后',//加载动画的文字
            spinner: 'el-icon-loading',//加载动画的图标
            background: 'rgba(0, 0, 0, 0.7)'//加载动画的背景
        },
    },
    getters: {

    },
    //里面定义方法，操作state方发
    mutations: {
        //加载方法
        showLoading(state,val){
            let loadingInstance = Loading.service(state.options);
            val? Loading.service(state.options) : loadingInstance.close() //这里判断调用方法时候的参数值，打开服务的时候传true，关闭服务的时候传false
        },
        //设置bookInfo的值
        setBookInfo(state,res){
            state.bookInfo.bookList =res.data.data.records;
            state.bookInfo.total = res.data.data.total;
            // this.commit('showLoading',false);
        },
        //登录时设置UID和token
        setTokenAndUID(state,res){
            state.userInfo.token = res.data.data.token;
            state.userInfo.UID = res.data.data.UID;
            state.userInfo.role = res.data.data.role;
            localStorage.setItem("userInfo",JSON.stringify(state.userInfo));
            // this.commit('showLoading',false);
        },
        //专门用于更新同步用户的数据
        setUserInfo(state,res){
            state.userInfo.token = JSON.parse(localStorage.getItem('userInfo')).token;
            state.userInfo.role = JSON.parse(localStorage.getItem('userInfo')).role;
            state.userInfo.UID = res.data.data.id;
            state.userInfo.nickname = res.data.data.nickname;
            state.userInfo.avatar = res.data.data.avatar;
            state.userInfo.background = res.data.data.background;
            state.userInfo.email = res.data.data.email;
            state.userInfo.signature = res.data.data.signature;
            state.userInfo.maxBorrow = res.data.data.maxBorrow
            state.userInfo.needToPay = res.data.data.needToPay;
            localStorage.setItem("userInfo",JSON.stringify(state.userInfo));
            // this.commit('showLoading',false);
        },
    },
    // 操作异步操作mutation
    actions: {
        syncBookInfo(context,url){
            http.get(url).then(res => {
                 setTimeout(() => {
                    context.commit('setBookInfo', res);
                 }, 1000);
            })
        },
        //异步同步用户信息部分由于使用了localStorage，前端调用时会因为localStorage还没存完数据就继续执行后续代码，故这部分代码暂且弃用，异步部分在前端来实现
        syncUserInfo(context){
            let UID = JSON.parse(localStorage.getItem('userInfo')).UID;
            let url = '/user/getUserInfoByUID?UID=' + UID;
            http.get(url).then(res => {
                context.commit('setUserInfo', res);
            })
        }
    },
    modules: {

    },
})