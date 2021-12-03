(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2f9af5b8"],{"20a8":function(e,t,r){"use strict";var a=r("b4d7"),o=r.n(a);o.a},"2fef":function(e,t,r){"use strict";r.r(t);var a=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticStyle:{"min-width":"1000px"}},[r("el-breadcrumb",{attrs:{"separator-class":"el-icon-arrow-right"}},[r("el-breadcrumb-item",{attrs:{to:{path:"/home"}}},[e._v("首页")]),r("el-breadcrumb-item",[e._v("权限管理")]),r("el-breadcrumb-item",[e._v("策略授权")])],1),r("el-card",[r("el-row",[r("el-button",{attrs:{type:"primary"},on:{click:e.openAdd}},[e._v("添加授权")])],1),r("el-table",{attrs:{data:e.userList,stripe:""}},[r("el-table-column",{attrs:{type:"index",label:"序号"}}),r("el-table-column",{attrs:{label:"策略名称",prop:"strategyName","show-overflow-tooltip":!0}}),r("el-table-column",{attrs:{label:"被授权主体","show-overflow-tooltip":!0},scopedSlots:e._u([{key:"default",fn:function(t){return[null!==t.row.deviceInfo?r("el-row",[r("span",[e._v(" "+e._s(null==t.row.deviceInfo.deviceName||""==t.row.deviceInfo.deviceName?"-":t.row.deviceInfo.deviceName)+" ")])]):null!==t.row.roleInfo?r("el-row",[r("span",[e._v(" "+e._s(null==t.row.roleInfo.roleCode||""==t.row.roleInfo.roleCode?"-":t.row.roleInfo.roleCode)+" ")])]):null!==t.row.groupInfo?r("el-row",[r("span",[e._v(" "+e._s(null==t.row.groupInfo.groupName||""==t.row.groupInfo.groupName?"-":t.row.groupInfo.groupName)+" ")])]):null!==t.row.appUserInfo?r("el-row",[r("span",[e._v(" "+e._s(null==t.row.appUserInfo.appUserName||""==t.row.appUserInfo.appUserName?"-":t.row.appUserInfo.appUserName)+" ")])]):e._e()]}}])}),r("el-table-column",{attrs:{label:"主体类型"},scopedSlots:e._u([{key:"default",fn:function(t){return[null!==t.row.deviceInfo?r("span",[e._v("终端用户")]):null!==t.row.roleInfo?r("span",[e._v("角色")]):null!==t.row.groupInfo?r("span",[e._v("分组")]):null!==t.row.appUserInfo?r("span",[e._v("应用用户")]):e._e()]}}])}),r("el-table-column",{attrs:{label:"创建时间",prop:"createTime",width:"200px"}}),r("el-table-column",{attrs:{label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-button",{attrs:{size:"mini",type:"danger"},on:{click:function(r){return e.deleById(t.row.strategyAuthId)}}},[e._v("取消授权")])]}}])})],1),r("el-pagination",{attrs:{"current-page":e.queryInfo.pagenum,"page-sizes":[5,10,15,20],"page-size":e.queryInfo.pagesize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1),r("el-dialog",{attrs:{title:"添加授权",visible:e.addDialogVisible,width:"755px"},on:{"update:visible":function(t){e.addDialogVisible=t},close:e.addDialogClosed}},[r("el-form",{ref:"addFormRef",attrs:{model:e.addForm,rules:e.addFormRules,"label-width":"130px"}},[r("el-form-item",{attrs:{label:"被授权主体类型:",prop:"authType"}},[r("el-radio-group",{on:{change:e.changeAuthType},model:{value:e.addForm.authType,callback:function(t){e.$set(e.addForm,"authType","string"===typeof t?t.trim():t)},expression:"addForm.authType"}},[r("el-radio",{attrs:{label:1}},[e._v("终端用户")]),r("el-radio",{attrs:{label:4}},[e._v("应用用户")]),r("el-radio",{attrs:{label:2}},[e._v("角色")]),r("el-radio",{attrs:{label:3}},[e._v("分组")])],1)],1),1===e.addForm.authType?r("el-form-item",{attrs:{label:"被授权主体:",prop:"valueUser"}},[r("el-select",{ref:"inputValue",attrs:{multiple:"",filterable:"",remote:"","reserve-keyword":"",placeholder:"请输入终端用户","remote-method":e.remoteMethod,loading:e.loading},model:{value:e.addForm.valueUser,callback:function(t){e.$set(e.addForm,"valueUser",t)},expression:"addForm.valueUser"}},e._l(e.optionsUser,(function(e){return r("el-option",{key:e.deviceId,attrs:{label:e.deviceName,value:e.deviceId}})})),1)],1):2===e.addForm.authType?r("el-form-item",{attrs:{label:"被授权主体:",prop:"valueCode"}},[r("el-select",{ref:"inputValue2",attrs:{multiple:"",filterable:"",remote:"","reserve-keyword":"",placeholder:"请输入角色","remote-method":e.remoteMethod2,loading:e.loading},model:{value:e.addForm.valueCode,callback:function(t){e.$set(e.addForm,"valueCode",t)},expression:"addForm.valueCode"}},e._l(e.optionsCode,(function(e){return r("el-option",{key:e.roleId,attrs:{label:e.roleCode,value:e.roleId}})})),1)],1):3===e.addForm.authType?r("el-form-item",{attrs:{label:"被授权主体:",prop:"valueGroup"}},[r("el-select",{ref:"inputValue3",attrs:{multiple:"",filterable:"",remote:"","reserve-keyword":"",placeholder:"请输入分组","remote-method":e.remoteMethod3,loading:e.loading},model:{value:e.addForm.valueGroup,callback:function(t){e.$set(e.addForm,"valueGroup",t)},expression:"addForm.valueGroup"}},e._l(e.optionsGroup,(function(e){return r("el-option",{key:e.groupId,attrs:{label:e.groupName,value:e.groupId}})})),1)],1):e._e(),4===e.addForm.authType?r("el-form-item",{attrs:{label:"被授权主体:",prop:"valueAppUser"}},[r("el-select",{ref:"inputValue",attrs:{multiple:"",filterable:"",remote:"","reserve-keyword":"",placeholder:"请输入应用用户","remote-method":e.remoteMethod4,loading:e.loading},model:{value:e.addForm.valueAppUser,callback:function(t){e.$set(e.addForm,"valueAppUser",t)},expression:"addForm.valueAppUser"}},e._l(e.optionsAppUser,(function(e){return r("el-option",{key:e.userId,attrs:{label:e.userName,value:e.userId}})})),1)],1):e._e()],1),r("div",{staticStyle:{display:"flex"}},[r("span",{staticClass:"supSpan"},[e._v("选择策略:")]),r("el-transfer",{attrs:{data:e.transferData,titles:["策略","策略名称"]},model:{value:e.checked,callback:function(t){e.checked=t},expression:"checked"}})],1),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.addDialogVisible=!1}}},[e._v("取 消")]),r("el-button",{directives:[{name:"preventReClick",rawName:"v-preventReClick",value:1e3,expression:"1000"}],attrs:{type:"primary"},on:{click:e.submitAdd}},[e._v("确 定")])],1)],1)],1)},o=[],n=(r("99af"),r("c975"),r("96cf"),r("1da1")),s={data:function(){return{userList:[],queryInfo:{pagenum:1,pagesize:10},total:0,isShow:!1,addDialogVisible:!1,queryValue:"",optionsUser:[],optionsCode:[],optionsGroup:[],optionsAppUser:[],loading:!1,addForm:{authType:1,valueUser:[],valueCode:[],valueGroup:[],valueAppUser:[]},addFormRules:{authType:[{required:!0,message:"必填项不能为空",trigger:"change"}],valueUser:[{required:!0,message:"必填项不能为空",trigger:"change"}],valueCode:[{required:!0,message:"必填项不能为空",trigger:"change"}],valueGroup:[{required:!0,message:"必填项不能为空",trigger:"change"}],valueAppUser:[{required:!0,message:"必填项不能为空",trigger:"change"}]},transferData:[],checked:[]}},beforeRouteEnter:function(e,t,r){var a=window.sessionStorage.getItem("accountTypeLogin");if("9"!==a)return r("/404");r()},created:function(){this.getUserList()},methods:{getUserList:function(){var e=this;return Object(n["a"])(regeneratorRuntime.mark((function t(){var r,a;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.$http.get("strategy/getStrategyAuthInfo/".concat(e.queryInfo.pagenum,"/").concat(e.queryInfo.pagesize,"?ts=").concat((new Date).getTime()));case 2:if(r=t.sent,a=r.data,0===a.code){t.next=6;break}return t.abrupt("return",e.$message.error("获取信息失败！"));case 6:e.userList=a.data.list,e.total=a.data.total;case 8:case"end":return t.stop()}}),t)})))()},handleSizeChange:function(e){this.queryInfo.pagesize=e,this.getUserList()},handleCurrentChange:function(e){this.queryInfo.pagenum=e,this.getUserList()},deleById:function(e){var t=this;return Object(n["a"])(regeneratorRuntime.mark((function r(){var a,o,n;return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:return r.next=2,t.$confirm("此操作将取消授权，是否继续？","提示",{confimrButtonText:"确定",cancelButtonText:"取消",type:"warning"}).catch((function(e){return e}));case 2:if(a=r.sent,"confirm"===a){r.next=5;break}return r.abrupt("return");case 5:return r.next=7,t.$http.post("strategy/delStrategyAuth?strategyAuthId=".concat(e));case 7:if(o=r.sent,n=o.data,0===n.code){r.next=11;break}return r.abrupt("return",t.$message.error(""!==n.msg&&null!==n.msg?n.msg:"取消授权失败！"));case 11:t.$message.success("取消授权成功！"),t.getUserList();case 13:case"end":return r.stop()}}),r)})))()},changeAuthType:function(e){},formatMethod:function(e){return null!==e.userName&&-1!==e.userName.indexOf("".concat(this.queryValue))?e.userName:null!==e.phone&&-1!==e.phone.indexOf("".concat(this.queryValue))?e.phone:null!==e.email&&-1!==e.email.indexOf("".concat(this.queryValue))?e.email:void 0},remoteMethod:function(e){var t=this;return Object(n["a"])(regeneratorRuntime.mark((function r(){var a,o;return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:if(""===e){r.next=11;break}return t.queryValue=e,t.loading=!0,r.next=5,t.$http.get("device/queryDeviceUser?ts=".concat((new Date).getTime()),{params:{deviceName:e}});case 5:if(a=r.sent,o=a.data,0===o.code){r.next=9;break}return r.abrupt("return",t.optionsUser=[]);case 9:t.loading=!1,t.optionsUser=o.data;case 11:case"end":return r.stop()}}),r)})))()},remoteMethod4:function(e){var t=this;return Object(n["a"])(regeneratorRuntime.mark((function r(){var a,o;return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:if(""===e){r.next=10;break}return t.loading=!0,r.next=4,t.$http.get("v1/kms/queryAppUser?ts=".concat((new Date).getTime()),{params:{appUserName:e}});case 4:if(a=r.sent,o=a.data,0===o.code){r.next=8;break}return r.abrupt("return",t.optionsAppUser=[]);case 8:t.loading=!1,t.optionsAppUser=o.data;case 10:case"end":return r.stop()}}),r)})))()},remoteMethod2:function(e){var t=this;return Object(n["a"])(regeneratorRuntime.mark((function r(){var a,o;return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:if(""===e){r.next=10;break}return t.loading=!0,r.next=4,t.$http.get("role/searchRole?ts=".concat((new Date).getTime()),{params:{roleCode:e}});case 4:if(a=r.sent,o=a.data,0===o.code){r.next=8;break}return r.abrupt("return",t.optionsCode=[]);case 8:t.loading=!1,t.optionsCode=o.data;case 10:case"end":return r.stop()}}),r)})))()},remoteMethod3:function(e){var t=this;return Object(n["a"])(regeneratorRuntime.mark((function r(){var a,o;return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:if(""===e){r.next=10;break}return t.loading=!0,r.next=4,t.$http.get("group/queryGroupInfo?ts=".concat((new Date).getTime()),{params:{groupName:e}});case 4:if(a=r.sent,o=a.data,0===o.code){r.next=8;break}return r.abrupt("return",t.optionsGroup=[]);case 8:t.loading=!1,t.optionsGroup=o.data;case 10:case"end":return r.stop()}}),r)})))()},openAdd:function(){this.addDialogVisible=!0,this.getFetchData()},addDialogClosed:function(){this.optionsUser=[],this.optionsCode=[],this.addForm.valueUser=[],this.addForm.valueCode=[],this.checked=[],this.$refs.addFormRef.resetFields()},submitAdd:function(){var e=this;this.$refs.addFormRef.validate(function(){var t=Object(n["a"])(regeneratorRuntime.mark((function t(r){var a,o,n;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(r){t.next=2;break}return t.abrupt("return");case 2:if(!(e.checked.length<1)){t.next=4;break}return t.abrupt("return",e.$message.error("请选择策略！"));case 4:return 1===e.addForm.authType?a={authType:1,deviceId:e.addForm.valueUser,strategyId:e.checked}:2===e.addForm.authType?a={authType:2,roleId:e.addForm.valueCode,strategyId:e.checked}:3===e.addForm.authType?a={authType:3,groupId:e.addForm.valueGroup,strategyId:e.checked}:4===e.addForm.authType&&(a={authType:4,appUserId:e.addForm.valueAppUser,strategyId:e.checked}),t.next=7,e.$http.post("strategy/addStrategyAuth",a);case 7:if(o=t.sent,n=o.data,0===n.code){t.next=11;break}return t.abrupt("return",e.$message.error(null!==n.msg&&""!==n.msg?n.msg:"添加授权失败！"));case 11:e.addDialogVisible=!1,e.$message.success("添加授权成功！"),e.getUserList();case 14:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}())},getFetchData:function(){var e=this;return Object(n["a"])(regeneratorRuntime.mark((function t(){var r,a,o,n,s;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.$http.get("strategy/getStrategy/0/0?ts=".concat((new Date).getTime()));case 2:if(r=t.sent,a=r.data,0===a.code){t.next=6;break}return t.abrupt("return",e.$message.error("获取策略信息失败！"));case 6:for(o=a.data.list,n=[],s=0;s<o.length;s++)n.push({key:o[s].strategyId,label:o[s].strategyName});e.transferData=n;case 10:case"end":return t.stop()}}),t)})))()}}},u=s,l=(r("20a8"),r("2877")),i=Object(l["a"])(u,a,o,!1,null,"cb71befa",null);t["default"]=i.exports},b4d7:function(e,t,r){}}]);
//# sourceMappingURL=chunk-2f9af5b8.c449b1bc.js.map