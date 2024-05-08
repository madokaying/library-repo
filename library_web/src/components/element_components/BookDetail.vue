<template>
    <div class="book-wrapper">
        <div class="menu">

        </div>
        <div class="content">

        </div>
    </div>
</template>

<script>
    import http from '@/utils/http'
    export default {
        name: "BookId",
        props:{
            bookId:Object,
        },
        data(){
            return{
                book:Object,
            }
        },
        methods:{
            goBack(){
                this.$router.go(-1);
            }
        },
        mounted() {
            http.post('book/getBookDetailById?bookId=' + this.bookId).then(res => {
                if (res.data.code === 200){
                    this.book = res.data.data;
                }else {
                    this.$message({
                        message:res.data.msg,
                        type:'error',
                        duration:'2000',
                    })
                }
            })
        }
    }
</script>

<style scoped>
    .book-wrapper {
        display: flex;
        border-radius: 10px;
        height: 750px;
        overflow: hidden;
        margin: 6px;
    }

    .book-wrapper .menu {
        display: inline-block;
        width: 20%;
    }

    .book-wrapper .content {
        display: inline-block;
        width: 80%;
    }
</style>
