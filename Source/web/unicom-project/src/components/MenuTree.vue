<template>
<div>
  <label v-for="item in data" :key="item.id">
    <el-submenu :index="item.id +''" v-if="item.children && item.children.length">
      <template slot="title">
        <i :class="iconsObj[item.id]"></i>
        <span>{{item.authName}}</span>
      </template>
      <label>
        <menu-tree :data="item.children"></menu-tree>
      </label>
    </el-submenu>
    <el-menu-item :index="'/'+item.path" v-else>
      <i :class="iconsObj[item.id]"></i>
      <span>{{item.authName}}</span>
    </el-menu-item>
  </label>
</div>
</template>
<script>
  import menuTree from "./MenuTree";
  export default {
    name: "menuTree",
    data() {
      return {
        iconsObj: {
          '100': 'iconfont icon-jiaoseguanli',
          '101': 'iconfont icon-usercenter',
          '102': 'iconfont icon-user2',
          '103': 'iconfont icon-jiaoseguanli2',
          '104': 'iconfont icon-fl-zuzhi',
          '105': 'iconfont icon-locking',
          '106': 'iconfont icon-a-dingdanziliao',
          '107': 'iconfont icon-yingyong1',
          '108': 'iconfont icon-xitongguanli1',
          '109': 'iconfont icon-ziyuanjk',
          '110': 'iconfont icon-rizhi1',
          '111': 'iconfont icon-jiaoseguanli1',
          '112': 'iconfont icon-ziyuan1',
          '113': 'iconfont icon-shouquanguanli',
          '114': 'iconfont icon-strategy1',
          '115': 'iconfont icon-quanxianguanli2',
          '116': 'iconfont icon-category',
          '117': 'iconfont icon-usercenter',
          '118': 'iconfont icon-suggestion',
          '119': 'iconfont icon-meetingover',
          '120': 'iconfont icon-user1',
          '121': 'iconfont icon-ziyuan',
          '122': 'iconfont icon-yingyong',
          '123': 'iconfont icon-packaging',
          '124': 'iconfont icon-packing-labeling',
          '125': 'iconfont icon-tupianfenzu',
          '126': 'iconfont icon-xitongguanli',
          '127': 'iconfont icon-schedule',
          '128': 'iconfont icon-quanxianguanli',
          '129': 'iconfont icon-wode',
          '130': 'iconfont icon-meetingover',
          '131': 'iconfont icon-grouping',
          '132': 'iconfont icon-donework',
          '133': 'iconfont icon-category',
          '134': 'iconfont icon-a-yijianfankuijibiji',
        },
      };
    },
    components: {
      menuTree
    },
    props: ["data"],
  };
</script>
<style lang="less" scoped>
  .iconfont {
    margin-right: 10px;
  }
</style>
