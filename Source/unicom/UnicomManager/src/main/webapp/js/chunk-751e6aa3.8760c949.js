(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-751e6aa3"],{"87b3":function(n,e,i){},"8d91":function(n,e,i){},bb51:function(n,e,i){"use strict";i.r(e);var t=function(){var n=this,e=n.$createElement,i=n._self._c||e;return i("el-container",{staticClass:"home-container"},[i("el-header",[i("div",[i("strong",[n._v(n._s(n.Global.projectName)+" "),i("i",{staticStyle:{"font-style":"normal"}},[n._v(n._s(n.accountTypeLogin))])])]),i("div",{staticStyle:{"font-size":"17px",cursor:"pointer"},on:{click:n.logoutHandle}},[i("span",[n._v(n._s(n.loginName))]),i("i",{staticClass:"el-icon-switch-button"})])]),i("el-container",{staticStyle:{height:"500px"}},[i("el-aside",{attrs:{width:n.isCollapse?"64px":"220px"}},[i("el-menu",{attrs:{router:"","default-openeds":n.defaultOpen,"unique-opened":"",collapse:n.isCollapse,"collapse-transition":!1,"default-active":n.$route.path}},[i("menu-tree",{attrs:{data:n.menuList}})],1)],1),i("el-main",[i("router-view")],1)],1)],1)},o=[],a=(i("a434"),i("96cf"),i("1da1")),c=function(){var n=this,e=n.$createElement,i=n._self._c||e;return i("div",n._l(n.data,(function(e){return i("label",{key:e.id},[e.children&&e.children.length?i("el-submenu",{attrs:{index:e.id+""}},[i("template",{slot:"title"},[i("i",{class:n.iconsObj[e.id]}),i("span",[n._v(n._s(e.authName))])]),i("label",[i("menu-tree",{attrs:{data:e.children}})],1)],2):i("el-menu-item",{attrs:{index:"/"+e.path}},[i("i",{class:n.iconsObj[e.id]}),i("span",[n._v(n._s(e.authName))])])],1)})),0)},r=[],u={name:"menuTree",data:function(){return{iconsObj:{100:"iconfont icon-jiaoseguanli",101:"iconfont icon-usercenter",102:"iconfont icon-user2",103:"iconfont icon-jiaoseguanli2",104:"iconfont icon-fl-zuzhi",105:"iconfont icon-locking",106:"iconfont icon-a-dingdanziliao",107:"iconfont icon-yingyong1",108:"iconfont icon-xitongguanli1",109:"iconfont icon-ziyuanjk",110:"iconfont icon-rizhi1",111:"iconfont icon-jiaoseguanli1",112:"iconfont icon-ziyuan1",113:"iconfont icon-shouquanguanli",114:"iconfont icon-strategy1",115:"iconfont icon-quanxianguanli2",116:"iconfont icon-category",117:"iconfont icon-usercenter",118:"iconfont icon-suggestion",119:"iconfont icon-meetingover",120:"iconfont icon-user1",121:"iconfont icon-ziyuan",122:"iconfont icon-yingyong",123:"iconfont icon-packaging",124:"iconfont icon-packing-labeling",125:"iconfont icon-tupianfenzu",126:"iconfont icon-xitongguanli",127:"iconfont icon-schedule",128:"iconfont icon-quanxianguanli",129:"iconfont icon-wode",130:"iconfont icon-meetingover",131:"iconfont icon-grouping",132:"iconfont icon-donework",133:"iconfont icon-category",134:"iconfont icon-a-yijianfankuijibiji"}}},components:{menuTree:h},props:["data"]},l=u,s=(i("ca03"),i("2877")),d=Object(s["a"])(l,c,r,!1,null,"06ae2d90",null),h=d.exports,p={name:"Home",components:{menuTree:h},data:function(){return{accountTypeLogin:"",loginName:"",menuList:[],caozuoList:[{id:130,authName:"应用管理",path:"applicationManagerUser",children:[],order:11}],auditList:[{id:127,authName:"日志告警",path:"log",children:[{id:134,authName:"日志审计",path:"log",children:[],order:null},{id:133,authName:"告警信息",path:"alert",children:[],order:null}],order:8},{id:129,authName:"关于",path:"status",children:[],order:5}],adminList:[{id:120,authName:"用户管理",path:"user",children:[{id:100,authName:"系统用户",path:"user",children:[],order:null},{id:102,authName:"终端用户",path:"deviceUser",children:[],order:null},{id:104,authName:"分组管理",path:"group",children:[],order:null}],order:1},{id:128,authName:"权限管理",path:"deviceAuth",children:[{id:111,authName:"终端用户权限",path:"deviceAuth",children:[],order:null},{id:116,authName:"分组权限",path:"groupAuth",children:[],order:null},{id:112,authName:"资源管理",path:"resources",children:[],order:null},{id:125,authName:"访问控制",path:"access",children:[],order:null}],order:9},{id:122,authName:"密钥源配置",path:"keySource",children:[],order:3},{id:132,authName:"量子密钥统计",path:"total",children:[],order:13},{id:130,authName:"应用管理",path:"applicationUser",children:[],order:11},{id:126,authName:"系统管理",path:"manager",children:[],order:7},{id:127,authName:"日志告警",path:"log",children:[{id:134,authName:"日志审计",path:"log",children:[],order:null},{id:133,authName:"告警信息",path:"alert",children:[],order:null}],order:8},{id:129,authName:"关于",path:"status",children:[],order:10}],isCollapse:!1,defaultOpen:[120]}},created:function(){this.loginName=window.sessionStorage.getItem("loginName");var n=window.sessionStorage.getItem("accountTypeLogin");"9"===n?(this.menuList=this.adminList,this.accountTypeLogin="(管理员)"):"2"===n?(this.menuList=this.auditList,this.accountTypeLogin="(安全员)"):"1"===n&&(this.menuList=this.caozuoList,this.accountTypeLogin="(应用管理员)")},methods:{logoutHandle:function(){var n=this;return Object(a["a"])(regeneratorRuntime.mark((function e(){var i;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,n.$confirm("确定要退出登录？","提示",{confirmButtonText:"确定",cancelButtontext:"取消",type:"warning"}).catch((function(n){return n}));case 2:if(i=e.sent,"confirm"===i){e.next=5;break}return e.abrupt("return");case 5:window.sessionStorage.clear(),n.menuList.splice(0,n.menuList.length),n.$router.push("/login");case 8:case"end":return e.stop()}}),e)})))()}}},m=p,f=(i("de16"),Object(s["a"])(m,t,o,!1,null,null,null));e["default"]=f.exports},ca03:function(n,e,i){"use strict";var t=i("87b3"),o=i.n(t);o.a},de16:function(n,e,i){"use strict";var t=i("8d91"),o=i.n(t);o.a}}]);
//# sourceMappingURL=chunk-751e6aa3.8760c949.js.map