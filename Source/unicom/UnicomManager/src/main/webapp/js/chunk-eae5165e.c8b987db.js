(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-eae5165e"],{c4eb:function(e,t,r){"use strict";r.r(t);var i=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"sourceWrap"},[r("el-breadcrumb",{attrs:{"separator-class":"el-icon-arrow-right"}},[r("el-breadcrumb-item",{attrs:{to:{path:"/home"}}},[e._v("首页")]),r("el-breadcrumb-item",[e._v("密钥源配置")])],1),r("el-card",[r("el-table",{attrs:{data:e.userList,stripe:"",height:"400"}},[r("el-table-column",{attrs:{type:"index",label:"序号"}}),r("el-table-column",{attrs:{label:"密钥源","show-overflow-tooltip":!0,prop:"keySource"},scopedSlots:e._u([{key:"default",fn:function(t){return[1===t.row.keySource?r("span",[e._v("QRNG")]):2===t.row.keySource?r("span",[e._v("密码卡")]):3===t.row.keySource?r("span",[e._v("高速QRNG")]):4===t.row.keySource?r("span",[e._v("QKD")]):e._e()]}}])}),r("el-table-column",{attrs:{label:"优先级",prop:"priority"}}),r("el-table-column",{attrs:{label:"IP",prop:"sourceIp"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("span",[e._v(e._s(null===t.row.sourceIp||""===t.row.sourceIp?"-":t.row.sourceIp))])]}}])}),r("el-table-column",{attrs:{label:"备用IP",prop:"sourceIp2"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("span",[e._v(e._s(null===t.row.sourceIp2||""===t.row.sourceIp2?"-":t.row.sourceIp2))])]}}])}),r("el-table-column",{attrs:{label:"操作",width:"300px"},scopedSlots:e._u([{key:"default",fn:function(t){return[5===t.row.priority?r("el-button",{attrs:{type:"warning",size:"mini"},on:{click:function(r){return e.addByName(t.row.priority,t.row.id)}}},[e._v("启用 ")]):r("el-button",{attrs:{type:"danger",size:"mini"},on:{click:function(r){return e.deleteByName(t.row.priority,t.row.id)}}},[e._v("禁用 ")]),r("el-button",{attrs:{type:"success",size:"mini"},on:{click:function(r){return e.editMethod(t.row.priority,t.row.id)}}},[e._v("修改优先级 ")]),3===t.row.keySource||4===t.row.keySource?r("el-button",{attrs:{type:"primary",size:"mini"},on:{click:function(r){return e.editIpMethod(t.row)}}},[e._v("修改IP ")]):e._e()]}}])})],1)],1),r("el-dialog",{attrs:{title:"添加密钥源配置",visible:e.addDialogVisible,width:"500px"},on:{"update:visible":function(t){e.addDialogVisible=t},close:e.addDialogClosed}},[r("el-form",{ref:"addFormRef",attrs:{model:e.addForm,rules:e.addFormRules,"label-width":"80px"}},[r("el-form-item",{attrs:{label:"优先级",prop:"priority"}},[r("el-select",{staticStyle:{width:"100%"},attrs:{placeholder:"请选择"},model:{value:e.addForm.priority,callback:function(t){e.$set(e.addForm,"priority",t)},expression:"addForm.priority"}},[r("el-option",{attrs:{label:1,value:1}}),r("el-option",{attrs:{label:2,value:2}}),r("el-option",{attrs:{label:3,value:3}}),r("el-option",{attrs:{label:4,value:4}})],1)],1)],1),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.addDialogVisible=!1}}},[e._v("取 消")]),r("el-button",{attrs:{type:"primary"},on:{click:e.submitAdd}},[e._v("确 定")])],1)],1),r("el-dialog",{attrs:{title:"修改IP",visible:e.editDialogVisible,width:"500px"},on:{"update:visible":function(t){e.editDialogVisible=t},close:e.editDialogClosed}},[r("el-form",{ref:"editFormRef",attrs:{model:e.editForm,rules:e.editFormRules,"label-width":"80px"}},[r("el-form-item",{attrs:{label:"IP",prop:"sourceIp"}},[r("el-input",{model:{value:e.editForm.sourceIp,callback:function(t){e.$set(e.editForm,"sourceIp","string"===typeof t?t.trim():t)},expression:"editForm.sourceIp"}})],1),r("el-form-item",{attrs:{label:"备用IP",prop:"sourceIp2"}},[r("el-input",{model:{value:e.editForm.sourceIp2,callback:function(t){e.$set(e.editForm,"sourceIp2","string"===typeof t?t.trim():t)},expression:"editForm.sourceIp2"}})],1)],1),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.editDialogVisible=!1}}},[e._v("取 消")]),r("el-button",{attrs:{type:"primary"},on:{click:e.submitEdit}},[e._v("确 定")])],1)],1),r("el-dialog",{attrs:{title:"修改优先级",visible:e.keyDialogVisible,width:"500px"},on:{"update:visible":function(t){e.keyDialogVisible=t},close:e.keyDialogClosed}},[r("el-form",{ref:"keyFormRef",attrs:{model:e.keyForm,rules:e.keyFormRules,"label-width":"80px"}},[r("el-form-item",{attrs:{label:"优先级",prop:"priority"}},[r("el-select",{staticStyle:{width:"100%"},attrs:{placeholder:"请选择"},model:{value:e.keyForm.priority,callback:function(t){e.$set(e.keyForm,"priority",t)},expression:"keyForm.priority"}},[r("el-option",{attrs:{label:1,value:1}}),r("el-option",{attrs:{label:2,value:2}}),r("el-option",{attrs:{label:3,value:3}}),r("el-option",{attrs:{label:4,value:4}})],1)],1)],1),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.keyDialogVisible=!1}}},[e._v("取 消")]),r("el-button",{directives:[{name:"preventReClick",rawName:"v-preventReClick",value:1e3,expression:"1000"}],attrs:{type:"primary"},on:{click:e.keyEnsure}},[e._v("确 定")])],1)],1)],1)},o=[],a=(r("96cf"),r("1da1")),s={data:function(){var e=function(e,t,r){var i=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;if(""!==t){if(i.test(t))return r();r(new Error("请输入正确的IP"))}};return{userList:[],total:0,addDialogVisible:!1,addId:null,addName:"",addForm:{priority:null},addFormRules:{priority:[{required:!0,message:"请选择",trigger:"change"}]},editDialogVisible:!1,editId2:"",editForm:{sourceIp:"",sourceIp2:""},editFormRules:{sourceIp:[{required:!0,validator:this.Global.valiIp,trigger:"blur"}],sourceIp2:[{required:!1,validator:e,trigger:"blur"}]},keyDialogVisible:!1,editPriority:"",editId:null,keyForm:{priority:null},keyFormRules:{priority:[{required:!0,message:"请选择",trigger:"change"}]}}},beforeRouteEnter:function(e,t,r){var i=window.sessionStorage.getItem("accountTypeLogin");if("9"!==i)return r("/404");r()},created:function(){this.getUserList()},methods:{getUserList:function(){var e=this;return Object(a["a"])(regeneratorRuntime.mark((function t(){var r,i;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.$http.get("sourceConfig/get?ts=".concat((new Date).getTime()));case 2:if(r=t.sent,i=r.data,0===i.code){t.next=6;break}return t.abrupt("return",e.$message.error(""!==i.msg&&null!==i.msg?i.msg:"获取数据失败！"));case 6:e.userList=i.data,e.total=i.data.total;case 8:case"end":return t.stop()}}),t)})))()},deleteByName:function(e,t){var r=this;return Object(a["a"])(regeneratorRuntime.mark((function i(){var o,a,s;return regeneratorRuntime.wrap((function(i){while(1)switch(i.prev=i.next){case 0:return i.next=2,r.$confirm("此操作将禁用该密钥源配置，是否继续？","提示",{confirmButtonText:"确定",cancelButtontext:"取消",type:"warning"}).catch((function(e){return e}));case 2:if(o=i.sent,"confirm"===o){i.next=5;break}return i.abrupt("return");case 5:return i.next=7,r.$http.get("sourceConfig/delete?ts=".concat((new Date).getTime()),{params:{priority:e,id:t}});case 7:if(a=i.sent,s=a.data,0===s.code){i.next=11;break}return i.abrupt("return",r.$message.error(""!==s.msg&&null!==s.msg?s.msg:"禁用失败！"));case 11:r.$message.success("禁用成功！"),r.getUserList();case 13:case"end":return i.stop()}}),i)})))()},addByName:function(e,t){var r=this;return Object(a["a"])(regeneratorRuntime.mark((function i(){return regeneratorRuntime.wrap((function(i){while(1)switch(i.prev=i.next){case 0:r.addDialogVisible=!0,r.addId=t,r.addName=e;case 3:case"end":return i.stop()}}),i)})))()},submitAdd:function(){var e=this;this.$refs.addFormRef.validate(function(){var t=Object(a["a"])(regeneratorRuntime.mark((function t(r){var i,o;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(r){t.next=2;break}return t.abrupt("return");case 2:return t.next=4,e.$http.get("sourceConfig/add?ts=".concat((new Date).getTime()),{params:{priority:e.addForm.priority,id:e.addId}});case 4:if(i=t.sent,o=i.data,0===o.code){t.next=8;break}return t.abrupt("return",e.$message.error(""!==o.msg&&null!==o.msg?o.msg:"启用失败！"));case 8:e.$message.success("启用成功！"),e.addDialogVisible=!1,e.getUserList();case 11:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}())},addDialogClosed:function(){this.$refs.addFormRef.resetFields()},editIpMethod:function(e){this.editDialogVisible=!0,this.editForm.sourceIp=e.sourceIp,this.editForm.sourceIp2=e.sourceIp2,this.editId2=e.id},submitEdit:function(){var e=this;this.$refs.editFormRef.validate(function(){var t=Object(a["a"])(regeneratorRuntime.mark((function t(r){var i,o;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(r){t.next=2;break}return t.abrupt("return");case 2:return t.next=4,e.$http.post("sourceConfig/update",{id:e.editId2,sourceIp:e.editForm.sourceIp,sourceIp2:e.editForm.sourceIp2});case 4:if(i=t.sent,o=i.data,0===o.code){t.next=8;break}return t.abrupt("return",e.$message.error(""!==o.msg&&null!==o.msg?o.msg:"修改IP失败！"));case 8:e.$message.success("修改IP成功！"),e.editDialogVisible=!1,e.getUserList();case 11:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}())},editDialogClosed:function(){this.$refs.editFormRef.resetFields()},editMethod:function(e,t){this.keyDialogVisible=!0,this.editPriority=e,this.editId=t},keyEnsure:function(){var e=this;this.$refs.keyFormRef.validate(function(){var t=Object(a["a"])(regeneratorRuntime.mark((function t(r){var i,o;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(r){t.next=2;break}return t.abrupt("return");case 2:return t.next=4,e.$http.get("sourceConfig/updatePriority?ts=".concat((new Date).getTime()),{params:{id:e.editId,newPriority:e.keyForm.priority,oldPriority:e.editPriority}});case 4:if(i=t.sent,o=i.data,0===o.code){t.next=8;break}return t.abrupt("return",e.$message.error(""!==o.msg&&null!==o.msg?o.msg:"修改优先级失败！"));case 8:e.$message.success("修改优先级成功！"),e.keyDialogVisible=!1,e.getUserList();case 11:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}())},keyDialogClosed:function(){this.$refs.keyFormRef.resetFields()}}},n=s,l=(r("e9f5"),r("2877")),u=Object(l["a"])(n,i,o,!1,null,null,null);t["default"]=u.exports},e9f5:function(e,t,r){"use strict";var i=r("f1e3"),o=r.n(i);o.a},f1e3:function(e,t,r){}}]);
//# sourceMappingURL=chunk-eae5165e.c8b987db.js.map