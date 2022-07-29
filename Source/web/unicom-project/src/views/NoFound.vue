<template>
  <div class="content404">
    <div class="center404">
      <p>出错啦404!</p>
      <div class="footer">
        <div style="position: absolute;left: 20px">页面自动<el-link :underline="false" type="danger" @click="back">跳转</el-link></div>
        <span style="position: absolute;right: 20px">等待{{count}}秒</span></div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "NoFound",
    data(){
      return{
        count:''
      }
    },
    created(){
      this.goGrdoupRecor();
    },
    beforeDestroy(){
      if(this.timer){
        clearInterval(this.timer);
      }
    },
    methods:{
      back(){
        this.$router.go(-2)
      },
      goGrdoupRecor() {
        const TIME_COUNT = 5;
        if (!this.timer) {
          this.count = TIME_COUNT;
          this.show = false;
          this.timer = setInterval(() => {
            if (this.count > 0 && this.count <= TIME_COUNT) {
              this.count--;
            } else {
              this.show = true;
              clearInterval(this.timer);
              this.timer = null;
              this.$router.go(-2)
            }
          }, 1000)
        }
      },
    }
  }
</script>

<style lang="less" scoped>

  .content404 {
    height: 100%;
    background-color: #D2F5F1;
    .center404 {
      width: 500px;
      height: 300px;
      background-color: white;
      /*margin: 0 auto;*/
      position: absolute;
      left: 50%;
      top: 50%;
      margin-left: -250px;
      margin-top: -150px;
      p {
        line-height: 50px;
        font-size: 30px;
        color: #e94c3c;
        margin: 20px;
        padding-bottom: 20px;
        border-bottom: 1px dashed #aacdd5;
      }
      .footer {
        position: relative;
        text-align: center;
        padding: 20px;
        height: 60px;
        color: #289575;
        font-size: 20px;
      }
    }

    a {
      text-decoration: none;
      color: red;
      font-size: 24px;
      position: relative;
      top: -3px;
      left: 10px;

    }
    a:hover {
      text-decoration: none
    }
  }
</style>
