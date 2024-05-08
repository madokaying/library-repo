//获取用户数据并同步
import http from "@/utils/http";
import store from '@/store';

/*
* vueInstance传递当前vue组件，使用this即可
* 由于会调用syncData()，且这部分要同步的数据不尽相同，因此使用时请确保当前vue组件内有syncData()方法
* */
export function getAndSyncUserInfo(vueInstance,successMessage,errorMessage) {
    http.post('/user/getUserInfoByUID?UID=' + JSON.parse(localStorage.getItem('userInfo')).UID).then(response => {
        if (response.data.code === 200) {
            //同步用户信息
            store.commit('setUserInfo', response);
            //调用传递进来的函数，立即同步修改页面上的数据
            vueInstance.syncData();
            vueInstance.$message({
                message:successMessage,
                type:'success',
                duration:'2000',
            });
        }else {
            vueInstance.$message({
                message:errorMessage,
                type:'error',
                duration:'2000',
            });
        }
    });
}
