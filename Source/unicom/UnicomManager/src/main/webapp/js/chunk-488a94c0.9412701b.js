(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-488a94c0"],{4373:function(e,t,r){"use strict";var i=r("7eb2"),s=r.n(i);s.a},"7eb2":function(e,t,r){},bae2:function(e,t,r){"use strict";r.r(t);var i=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticStyle:{"min-width":"1000px"}},[r("el-breadcrumb",{attrs:{"separator-class":"el-icon-arrow-right"}},[r("el-breadcrumb-item",{attrs:{to:{path:"/home"}}},[e._v("首页")]),r("el-breadcrumb-item",[e._v("用户管理")]),r("el-breadcrumb-item",[e._v("终端用户")])],1),r("el-card",[r("el-row",[r("el-col",{staticStyle:{float:"left"},attrs:{span:16}},[r("el-button",{attrs:{type:"primary"},on:{click:function(t){e.addDialogVisible=!0}}},[e._v("添加")]),r("el-button",{attrs:{type:"primary"},on:{click:e.exportModel}},[e._v("下载模板")]),r("label",{attrs:{for:"fileId"}},[r("span",{staticClass:"butSpan"},[e._v("导入")])]),r("input",{ref:"fileId",attrs:{type:"file",id:"fileId",hidden:""},on:{change:e.onFileChange}}),r("el-button",{directives:[{name:"show",rawName:"v-show",value:e.multipleSelection.length>0,expression:"multipleSelection.length > 0"}],attrs:{type:"danger",plain:""},on:{click:e.batchDeleteBuild}},[e._v("批量删除")]),r("el-button",{directives:[{name:"show",rawName:"v-show",value:e.multipleSelection.length>0,expression:"multipleSelection.length > 0"}],attrs:{type:"primary",plain:""},on:{click:e.batchExportBuild}},[e._v("批量导出")])],1),r("el-col",{staticStyle:{float:"right"},attrs:{span:8}},[r("el-input",{attrs:{placeholder:"请输入用户名",clearable:""},on:{clear:e.getUserList},model:{value:e.queryInfo.query,callback:function(t){e.$set(e.queryInfo,"query","string"===typeof t?t.trim():t)},expression:"queryInfo.query"}},[r("el-button",{attrs:{slot:"append",icon:"el-icon-search"},on:{click:e.getUserList},slot:"append"})],1)],1)],1),r("el-table",{ref:"multipleTable",attrs:{data:e.userlist,"row-key":e.getRowKeys,border:"",stripe:""},on:{"selection-change":e.handleSelectionChange}},[r("el-table-column",{attrs:{type:"selection","reserve-selection":!0,width:"55"}}),r("el-table-column",{attrs:{type:"index",label:"序号"}}),r("el-table-column",{attrs:{label:"用户名","show-overflow-tooltip":!0,prop:"deviceName"}}),r("el-table-column",{attrs:{label:"用户类型","show-overflow-tooltip":!0,prop:"userType"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("span",[e._v(e._s(0===t.row.userType?"软件用户":"硬件用户"))])]}}])}),r("el-table-column",{attrs:{label:"备注","show-overflow-tooltip":!0,prop:"comments"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("span",[e._v(e._s(null===t.row.comments||""===t.row.comments?"-":t.row.comments))])]}}])}),r("el-table-column",{attrs:{label:"创建时间","show-overflow-tooltip":!0,prop:"createTime"}}),r("el-table-column",{attrs:{label:"操作",width:"300px"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-button",{attrs:{size:"mini",type:"primary"},on:{click:function(r){return e.editById(t.row)}}},[e._v("编辑")]),r("el-button",{attrs:{size:"mini",type:"warning"},on:{click:function(r){return e.psdByName(t.row.deviceId)}}},[e._v("修改密码 ")]),r("el-button",{attrs:{size:"mini",type:"success"},on:{click:function(r){return e.lookById(t.row.deviceName)}}},[e._v("量子密钥管控")])]}}])})],1),r("el-pagination",{attrs:{"current-page":e.queryInfo.pagenum,"page-sizes":[5,10,15,20],"page-size":e.queryInfo.pagesize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1),r("el-dialog",{attrs:{title:"添加终端用户",visible:e.addDialogVisible,width:"520px"},on:{"update:visible":function(t){e.addDialogVisible=t},close:e.addDialogClosed}},[r("el-form",{ref:"addFormRef",attrs:{model:e.addForm,rules:e.addFormRules,"label-width":"80px"}},[r("el-form-item",{attrs:{label:"用户名",prop:"deviceName"}},[r("el-input",{model:{value:e.addForm.deviceName,callback:function(t){e.$set(e.addForm,"deviceName","string"===typeof t?t.trim():t)},expression:"addForm.deviceName"}})],1),r("el-form-item",{attrs:{label:"密码",prop:"pws1"}},[r("el-input",{attrs:{type:"password"},model:{value:e.addForm.pws1,callback:function(t){e.$set(e.addForm,"pws1","string"===typeof t?t.trim():t)},expression:"addForm.pws1"}})],1),r("el-form-item",{attrs:{label:"确认密码",prop:"pws2"}},[r("el-input",{attrs:{type:"password"},model:{value:e.addForm.pws2,callback:function(t){e.$set(e.addForm,"pws2","string"===typeof t?t.trim():t)},expression:"addForm.pws2"}})],1),r("el-form-item",{attrs:{label:"用户类型",prop:"userType"}},[r("el-radio-group",{model:{value:e.addForm.userType,callback:function(t){e.$set(e.addForm,"userType","string"===typeof t?t.trim():t)},expression:"addForm.userType"}},[r("el-radio",{attrs:{label:0}},[e._v("软件用户")]),r("el-radio",{attrs:{label:1}},[e._v("硬件用户")])],1)],1),r("el-form-item",{attrs:{label:"备注",prop:"comments"}},[r("el-input",{attrs:{type:"textarea",maxlength:"200","show-word-limit":"",rows:5},model:{value:e.addForm.comments,callback:function(t){e.$set(e.addForm,"comments","string"===typeof t?t.trim():t)},expression:"addForm.comments"}})],1)],1),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.addDialogVisible=!1}}},[e._v("取 消")]),r("el-button",{directives:[{name:"preventReClick",rawName:"v-preventReClick",value:1e3,expression:"1000"}],attrs:{type:"primary"},on:{click:e.submitAdd}},[e._v("确 定")])],1)],1),r("el-dialog",{attrs:{title:"编辑终端用户",visible:e.editDialogVisible,width:"520px"},on:{"update:visible":function(t){e.editDialogVisible=t},close:e.editDialogClosed}},[r("el-form",{ref:"editFormRef",attrs:{model:e.editForm,rules:e.editFormRules,"label-width":"80px"}},[r("el-form-item",{attrs:{label:"用户名",prop:"deviceName"}},[r("el-input",{attrs:{disabled:""},model:{value:e.editForm.deviceName,callback:function(t){e.$set(e.editForm,"deviceName","string"===typeof t?t.trim():t)},expression:"editForm.deviceName"}})],1),r("el-form-item",{attrs:{label:"备注",prop:"comments"}},[r("el-input",{attrs:{type:"textarea",maxlength:"200","show-word-limit":"",rows:5},model:{value:e.editForm.comments,callback:function(t){e.$set(e.editForm,"comments","string"===typeof t?t.trim():t)},expression:"editForm.comments"}})],1)],1),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.editDialogVisible=!1}}},[e._v("取 消")]),r("el-button",{directives:[{name:"preventReClick",rawName:"v-preventReClick",value:1e3,expression:"1000"}],attrs:{type:"primary"},on:{click:e.submitEdit}},[e._v("确 定")])],1)],1),r("el-dialog",{attrs:{title:"修改密码",visible:e.psdDialogVisible,width:"520px"},on:{"update:visible":function(t){e.psdDialogVisible=t},close:e.psdDialogClosed}},[r("el-form",{ref:"psdFormRef",attrs:{model:e.psdForm,rules:e.psdFormRules,"label-width":"80px"}},[r("el-form-item",{attrs:{label:"新密码",prop:"psd1"}},[r("el-input",{attrs:{type:"password"},model:{value:e.psdForm.psd1,callback:function(t){e.$set(e.psdForm,"psd1","string"===typeof t?t.trim():t)},expression:"psdForm.psd1"}})],1),r("el-form-item",{attrs:{label:"确认密码",prop:"psd2"}},[r("el-input",{attrs:{type:"password"},model:{value:e.psdForm.psd2,callback:function(t){e.$set(e.psdForm,"psd2","string"===typeof t?t.trim():t)},expression:"psdForm.psd2"}})],1)],1),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.psdDialogVisible=!1}}},[e._v("取 消")]),r("el-button",{directives:[{name:"preventReClick",rawName:"v-preventReClick",value:1e3,expression:"1000"}],attrs:{type:"primary"},on:{click:e.psdEnsure}},[e._v("确 定")])],1)],1)],1)},s=[],a=(r("99af"),r("ac1f"),r("1276"),r("96cf"),r("1da1")),o={data:function(){var e=this,t=function(t,r,i){""===r?i(new Error("请再次输入密码")):r!==e.addForm.pws1?i(new Error("两次输入密码不一致")):i()},r=function(t,r,i){""===r?i(new Error("请再次输入密码")):r!==e.psdForm.psd1?i(new Error("两次输入密码不一致")):i()};return{userlist:[],queryInfo:{query:"",pagenum:1,pagesize:10},total:0,addDialogVisible:!1,addForm:{deviceName:"",pws1:"",pws2:"",comments:"",userType:0},addFormRules:{deviceName:[{required:!0,validator:this.Global.valiUser,trigger:"blur"}],pws1:[{required:!0,validator:this.Global.valiPass,trigger:"blur"}],pws2:[{required:!0,validator:t,trigger:"blur"}],comments:[{min:0,max:200,message:"长度在200个字符以内",trigger:"blur"}],userType:[{required:!0,message:"请选择用户类型",trigger:"change"}]},editId:"",editDialogVisible:!1,editForm:{deviceName:"",comments:""},editFormRules:{deviceName:[{required:!0,validator:this.Global.valiUser,trigger:"blur"}],comments:[{min:0,max:200,message:"长度在200个字符以内",trigger:"blur"}]},multipleSelection:[],deleteCode:[],psdDialogVisible:!1,psdForm:{psd1:"",psd2:"",deviceId:""},psdFormRules:{password:[{required:!0,message:"请输入旧密码",trigger:"blur"}],psd1:[{required:!0,validator:this.Global.valiPass,trigger:"blur"}],psd2:[{required:!0,validator:r,trigger:"blur"}]}}},beforeRouteEnter:function(e,t,r){var i=window.sessionStorage.getItem("accountTypeLogin");if("9"!==i)return r("/404");r()},created:function(){this.getUserList()},methods:{getUserList:function(){var e=this;return Object(a["a"])(regeneratorRuntime.mark((function t(){var r,i;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.$http.get("device/getAllDevice/".concat(e.queryInfo.pagenum,"/").concat(e.queryInfo.pagesize,"?ts=").concat((new Date).getTime()),{params:{deviceName:e.queryInfo.query}});case 2:if(r=t.sent,i=r.data,0===i.code){t.next=6;break}return t.abrupt("return",e.$message.error("获取用户信息失败！"));case 6:e.userlist=i.data.list,e.total=i.data.total;case 8:case"end":return t.stop()}}),t)})))()},handleSizeChange:function(e){this.queryInfo.pagesize=e,this.getUserList()},handleCurrentChange:function(e){this.queryInfo.pagenum=e,this.getUserList()},lookById:function(e){this.$router.push({path:"/keyInfo",query:{deviceName:e}})},onFileChange:function(){var e=this;return Object(a["a"])(regeneratorRuntime.mark((function t(){var r,i,s,a;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(r=e.$refs.fileId,"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"===r.files[0].type||"application/vnd.ms-excel"===r.files[0].type){t.next=3;break}return t.abrupt("return",e.$message.error("只能导入扩展名为xlsx/xls的文件，请重新选择！"));case 3:return i=new FormData,i.append("excelFile",r.files[0]),t.next=7,e.$http.post("device/importDeviceUser",i,{headers:{"Content-Type":"multipart/form-data"}});case 7:if(s=t.sent,a=s.data,e.$refs.fileId.value="",0===a.code){t.next=12;break}return t.abrupt("return",e.$message.error(null!==a.msg&&""!==a.msg?a.msg:"导入失败!"));case 12:e.$message.success("导入成功!"),e.getUserList();case 14:case"end":return t.stop()}}),t)})))()},addDialogClosed:function(){this.$refs.addFormRef.resetFields()},submitAdd:function(){var e=this;this.$refs.addFormRef.validate(function(){var t=Object(a["a"])(regeneratorRuntime.mark((function t(r){var i,s,a;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(r){t.next=2;break}return t.abrupt("return");case 2:return i={comments:e.addForm.comments,password:e.addForm.pws2,deviceName:e.addForm.deviceName,userType:e.addForm.userType},t.next=5,e.$http.post("device/addDeviceUser",i);case 5:if(s=t.sent,a=s.data,0===a.code){t.next=9;break}return t.abrupt("return",e.$message.error(null!==a.msg&&""!==a.msg?a.msg:"添加失败！"));case 9:e.$message.success("添加成功！"),e.addDialogVisible=!1,e.getUserList();case 12:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}())},editById:function(e){this.editDialogVisible=!0,this.editForm.comments=e.comments,this.editForm.deviceName=e.deviceName,this.editId=e.deviceId},editDialogClosed:function(){this.$refs.editFormRef.resetFields()},submitEdit:function(){var e=this;this.$refs.editFormRef.validate(function(){var t=Object(a["a"])(regeneratorRuntime.mark((function t(r){var i,s,a;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(r){t.next=2;break}return t.abrupt("return");case 2:return i={comments:e.editForm.comments,deviceName:e.editForm.deviceName,deviceId:e.editId},t.next=5,e.$http.post("device/updateDevice",i);case 5:if(s=t.sent,a=s.data,0===a.code){t.next=9;break}return t.abrupt("return",e.$message.error(null!==a.msg&&""!==a.msg?a.msg:"编辑失败！"));case 9:e.$message.success("编辑成功！"),e.editDialogVisible=!1,e.getUserList();case 12:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}())},getRowKeys:function(e){return e.deviceId},handleSelectionChange:function(e){this.multipleSelection=e},batchExportBuild:function(){for(var e=this,t=0;t<this.multipleSelection.length;t++)this.deleteCode.push(this.multipleSelection[t].deviceId);var r="device/exportDeviceUsers",i={deviceIds:this.deleteCode};this.$http.post("".concat(r),i,{responseType:"blob"}).then((function(t){var r=decodeURI(t.headers["content-disposition"].split(";")[1].split("=")[1]);e.Global.downloadFile(t.data,r),e.deleteCode=[],e.$refs.multipleTable.clearSelection(),e.getUserList()}))},batchDeleteBuild:function(){var e=this;return Object(a["a"])(regeneratorRuntime.mark((function t(){var r,i,s,a;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.$confirm("此操作将删除选中用户，是否继续？","提示",{confimrButtonText:"确定",cancelButtonText:"取消",type:"warning"}).catch((function(e){return e}));case 2:if(r=t.sent,"confirm"===r){t.next=5;break}return t.abrupt("return");case 5:for(i=0;i<e.multipleSelection.length;i++)e.deleteCode.push(e.multipleSelection[i].deviceId);return t.next=8,e.$http.post("device/deleteDevice",{deviceId:e.deleteCode});case 8:if(s=t.sent,a=s.data,0===a.code){t.next=12;break}return t.abrupt("return",e.$message.error(""!==a.msg&&null!==a.msg?a.msg:"删除失败！"));case 12:e.$message.success("删除成功！"),e.deleteCode=[],e.$refs.multipleTable.clearSelection(),e.getUserList();case 16:case"end":return t.stop()}}),t)})))()},psdByName:function(e){this.psdForm.deviceId=e,this.psdDialogVisible=!0},psdEnsure:function(){var e=this;this.$refs.psdFormRef.validate(function(){var t=Object(a["a"])(regeneratorRuntime.mark((function t(r){var i,s,a;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(r){t.next=2;break}return t.abrupt("return");case 2:return i={password:e.psdForm.psd2,deviceId:e.psdForm.deviceId},t.next=5,e.$http.post("device/updateDevice",i);case 5:if(s=t.sent,a=s.data,0===a.code){t.next=9;break}return t.abrupt("return",e.$message.error(null!==a.msg&&""!==a.msg?a.msg:"修改密码失败！"));case 9:e.$message.success("修改密码成功！"),e.psdDialogVisible=!1;case 11:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}())},psdDialogClosed:function(){this.$refs.psdFormRef.resetFields()},exportModel:function(){var e=this,t="device/getDeviceUserModel";this.$http.post("".concat(t),"",{responseType:"blob"}).then((function(t){var r=decodeURI(t.headers["content-disposition"].split(";")[1].split("=")[1]);e.Global.downloadFile(t.data,r)}))}}},n=o,l=(r("4373"),r("2877")),d=Object(l["a"])(n,i,s,!1,null,"6e098330",null);t["default"]=d.exports}}]);
//# sourceMappingURL=chunk-488a94c0.9412701b.js.map