<script>
import http from '@/utils/http'
export default {
  name: "MyLibrary",
  data(){
    return{

    }
  },
  methods:{
    //获取借书申请表
    getBorrowList(){
      http.post('/')
    },
  },
  mounted() {

  }
}
</script>

<template>
<div>

</div>
</template>

<style scoped>

</style>
