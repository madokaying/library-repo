<!--显示整体的框架-->
<template>
    <div>
        <div v-if="this.$store.state.bookInfo.bookList.length !== 0">
            <el-row>
                <el-col :span="8" v-for="(book,index) in this.$store.state.bookInfo.bookList"  :key="index"><book-card-view :book = "book"></book-card-view></el-col>
            </el-row>
        </div>
        <div v-else>
            <el-empty description="暂无书籍" :image-size="300"></el-empty>
        </div>
        <div>
            <!--分页功能-->
            <el-pagination
                    background
                    layout="prev, pager, next"
                    :page-size=pageSize
                    :total=this.$store.state.bookInfo.total
                    @current-change="getPageNum">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import BookCardView from "@/components/element_components/card/BookCardView";
    export default {
        name: "ShowCardView",
        components:{
            BookCardView
        },
        data(){
            return{
                pageSize:16,
            }
        },
        methods:{
            getPageNum(currentPage){
                //返回当前页的书籍信息，默认每页显示16本图书信息
                let url = "/book/getBooksList?currentPage=" + currentPage + "&pageSize=" + this.pageSize;
                this.$store.dispatch('syncBookInfo',url);
            }
        },
        mounted() {
            //异步访问数据库中的数据,并赋值给vuex
            this.$store.dispatch('syncBookInfo','/book/getBooksList');
        }
    }
</script>

<style scoped>

</style>