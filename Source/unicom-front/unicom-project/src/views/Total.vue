<template>
  <div class="totalContent">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item v-if="$route.query.urlType" :to="{path:'/applicationManagerUser'}">应用管理</el-breadcrumb-item>
      <el-breadcrumb-item>量子密钥统计</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="height: calc(100% - 28px);">
      <div class="total-header">
        <div class="header-wrap">
          <img src="../assets/image/10.png" alt="">
          <ul>
            <li>{{deviceNum}}</li>
            <li>设备总量</li>
          </ul>
        </div>
        <div class="header-wrap">
          <img src="../assets/image/9.png" alt="">
          <ul>
            <li>{{onlineNum}}</li>
            <li>在线数量</li>
          </ul>
        </div>
        <div class="header-wrap">
          <img src="../assets/image/8.png" alt="">
          <ul>
            <li>{{offlineNum}}</li>
            <li>离线数量</li>
          </ul>
        </div>
      </div>
      <div class="total-center">
        <div class="pie"></div>
        <div class="barRight1"></div>
      </div>

    </el-card>
  </div>
</template>
<script>
  import * as echarts from 'echarts';
import Cookies from 'js-cookie'
  export default {
    data() {
      return {
        timer: null,//定时器名称
        deviceNum: '',
        onlineNum: '',
        offlineNum: '',
        onlineKeyNum: '',
        total: 0,
        userList: [],
        queryInfo: {
          pagenum: 1,
          pagesize: 4
        },
      }
    },
    beforeRouteEnter: (to, form, next) => {
      const loginType =Cookies.get('accountTypeLogin');
      if (loginType === '9' || loginType === '1') {
        next()
      } else {
        return next('/404')
      }
    },
    created() {
      this.getTotalInfo();
    },
    methods: {
      async getTotalInfo() {
        let url='';
        if(this.$route.query.urlType){
          url=`status/getCurrentAppStatusInfo?appId=${this.$route.query.id}`
        }else{
          url=`status/getDeviceStatusInfo`
        }
        const {data: res} = await this.$http.get(`${url}`);
        if (res.code !== 0) return this.$message.error('获取数据信息失败！');
        this.deviceNum = res.data.deviceStatusInfo.deviceNum;
        this.onlineNum = res.data.deviceStatusInfo.onlineNum;
        this.offlineNum = res.data.deviceStatusInfo.offlineNum;
        this.onlineKeyNum = res.data.deviceStatusInfo.onlineKeyNum;
        this.userList = res.data.logs;
        this.barRight1(res.data);
        this.chartNum(res.data);
      },
      jishi() {
        this.timer = setInterval(() => {
          this.getTotalInfo()
        }, 5000)
      },
      barRight1(num) {
        let myChart = echarts.getInstanceByDom(document.querySelector('.barRight1'));
        if (myChart == null) {
          myChart = echarts.init(document.querySelector('.barRight1'));
        }
        myChart.setOption({
          title: [{text: `加解密数据统计`, left: '50%', top: '85%', textAlign: 'center'}],
          color: ['#2f89cf'],
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "shadow"
            }
          },
          grid: {
            left: "0%",
            top: "10px",
            right: "0%",
            bottom: "22%",
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: ['加密数量', '解密数量', '加解密数据总量'],
            axisTick: {
              alignWithLabel: true
            },
            axisLabel: {
              color: '#909399',
              fontSize: '12'
            },
            axisLine: {
              show: true
            }
          },
          yAxis: {
            type: 'value',
            axisLabel: {
              color: "#909399",
              fontSize: 12,
              formatter: function (value, index) {
                if (value >= 10000 && value < 10000000) {
                  value = value / 10000 + "万";
                } else if (value >= 10000000) {
                  value = value / 10000000 + "千万";
                }
                return value;
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#909399",
                width: 1
              }
            },
            // y轴分割线的颜色
            splitLine: {
              lineStyle: {
                color: "rgba(255,255,255,.1)"
              }
            },

          },
          series: [{
            name: '合计',
            type: 'bar',
            barWidth: '25%',
            data: [num.deviceStatusInfo.encDataNum, num.deviceStatusInfo.decDataNum, num.deviceStatusInfo.encDataNum + num.deviceStatusInfo.decDataNum],
            itemStyle: {
              borderRadius: 5
            }
          }]
        });
        window.addEventListener("resize", function () {
          myChart.resize();
        });
      },
      chartNum(num) {
        let myChart = echarts.getInstanceByDom(document.querySelector('.pie'));
        if (myChart == null) {
          myChart = echarts.init(document.querySelector('.pie'));
        }
        const keyDistributionNum = num.deviceStatusInfo.keyDistributionNum;
        const onlineKeyNum = num.deviceStatusInfo.onlineKeyNum;
          myChart.setOption({
          title: {text: `密钥总量:${keyDistributionNum}     密钥使用量:${onlineKeyNum}`, left: '45%', top: '85%', textAlign: 'center',
          textStyle:{
            overflow:'break',
            width:290
          }},
          series: [
            {
              type: 'gauge',
              radius: '50%',
              startAngle: 90,
              endAngle: -270,
              center: ['50%', '50%'],
              itemStyle: {
                color: '#409EFF',
              },
              pointer: {
                show: false
              },
              progress: {
                show: true,
                overlap: false,
                roundCap: true,
                clip: false,
                itemStyle: {
                  borderWidth: 1,
                  borderColor: '#409EFF'
                }
              },
              axisLine: {
                lineStyle: {
                  width: 15
                }
              },
              splitLine: {
                show: false,
                distance: 0,
                length: 10
              },
              axisTick: {//刻度样式
                show: false
              },
              axisLabel: {
                show: false,
                distance: 50
              },
              data: [
                {value: ((onlineKeyNum / keyDistributionNum) * 100).toFixed(2)}
              ],
              detail: {////仪表盘详情，用于显示数据
                valueAnimation: true,
                fontSize: 18,
                color: 'auto',
                formatter: '{value}%',
                offsetCenter: ['0%', '0%'],
              }
            }
          ]
        });
        window.addEventListener("resize", function () {
          myChart.resize();
        });
      },
    },
    beforeDestroy() {  // 销毁定时器
      clearInterval(this.timer);
    },
  }
</script>
<style lang="less" scoped>
  .totalContent {
    position: relative;
    min-width: 1080px;
    height: 100%;
    min-height: 670px;
    .total-header {
      display: flex;
      padding: 30px;
      margin-bottom:100px;
      .header-wrap {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 250px;
        height: 100px;
        margin-right: 80px;
        padding: 0 30px;
        background-color: #ecf5ff;
        border-radius: 6px;
        font-family: serif;
        img {
          width: 52px;
          height: 52px;
        }
        ul {
          flex: 1;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          li:nth-of-type(1) {
            padding-bottom: 15px;
            color: #f19916;
            font-size: 30px;
            font-family: 'electronicFont';
          }
          li:nth-of-type(2) {
            color: #248df9;
            font-size: 16px;
            letter-spacing: 2px;
          }
        }
      }
    }
    .total-center {
      display: flex;
      padding: 10px 30px;
      .pie {
        width: 400px;
        height: 300px;
        margin-right: 20px;
      }
      .barRight1 {
        width: 400px;
        height: 300px;
      }
    }
  }
</style>
