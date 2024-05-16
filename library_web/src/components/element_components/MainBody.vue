<template>
  <div>
    <el-row>
      <el-col
          :xs="24"
          :lg="17">
        <div v-if="units === 'homePage'">
          <show-card-view></show-card-view>
        </div>
      </el-col>
      <el-col
          :xs="0"
          :lg="7">
        <user-card-view></user-card-view>
        <search-card></search-card>
      </el-col>
    </el-row>
    <el-row>
      <!--TODO 这里放一个带背景的展示框，这里写一下思路，方便后续修改：vue内设置一个满背景，然后设置一个透明div，内部显示部分想显示的信息-->
    </el-row>
  </div>
</template>

<script>
import UserCardView from "@/components/element_components/card/UserCardView";
import SearchCard from "@/components/element_components/card/SearchCard";
import ShowCardView from "@/components/element_components/ShowCardView";

export default {
  name: "MainBody",
  props: [
    'units',
  ],
  components: {
    UserCardView,
    SearchCard,
    ShowCardView,
  },
}
</script>

<style scoped>

</style>
