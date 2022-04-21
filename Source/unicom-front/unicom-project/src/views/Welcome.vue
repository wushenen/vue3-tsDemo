<template>
  <div class="bg">
    <img src="../assets/image/pic_welcome.png" alt="">
  </div>
</template>
<script>
  export default {
    data() {
      return {
      }
    },
    created() {
    },
    methods: {
    }
  }
</script>
<style lang="less" scoped>
  .bg {
    position: relative;
    height: 700px;
    > img {
      position: absolute;
      width: 800px;
      height: 700px;
      left: 50%;
      top: 50%;
      margin-left: -400px;
      margin-top: -350px;
    }
  }
</style>
