(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-54563e51"],{"4cb3":function(e,t,r){},"5adc":function(e,t,r){"use strict";var n=r("4cb3"),a=r.n(n);a.a},"5e96":function(e,t,r){"use strict";r.r(t);var n=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticStyle:{"min-width":"1000px"}},[r("el-breadcrumb",{attrs:{"separator-class":"el-icon-arrow-right"}},[r("el-breadcrumb-item",{attrs:{to:{path:"/home"}}},[e._v("首页")]),r("el-breadcrumb-item",[e._v("用户管理")]),r("el-breadcrumb-item",{attrs:{to:{path:"/group"}}},[e._v("分组管理")]),r("el-breadcrumb-item",[e._v("终端用户")])],1),r("el-card",[r("el-row",[r("el-button",{attrs:{type:"primary"},on:{click:function(t){e.addDialogVisible=!0}}},[e._v("添加成员")]),r("el-button",{directives:[{name:"show",rawName:"v-show",value:e.multipleSelection.length>0,expression:"multipleSelection.length > 0"}],attrs:{plain:""},on:{click:e.batchDeleteBuild}},[e._v("批量删除")])],1),r("el-table",{ref:"multipleTable",attrs:{data:e.userList,"row-key":e.getRowKeys,stripe:""},on:{"selection-change":e.handleSelectionChange}},[r("el-table-column",{attrs:{type:"selection","reserve-selection":!0,width:"55"}}),r("el-table-column",{attrs:{type:"index",label:"序号"}}),r("el-table-column",{attrs:{label:"用户名",prop:"deviceName"}}),r("el-table-column",{attrs:{label:"备注","show-overflow-tooltip":!0,prop:"comments"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("span",[e._v(e._s(null===t.row.comments||""===t.row.comments?"-":t.row.comments))])]}}])}),r("el-table-column",{attrs:{label:"创建时间",prop:"createTime"}}),r("el-table-column",{attrs:{label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-button",{attrs:{size:"mini",type:"danger"},on:{click:function(r){return e.deleById(t.row.id)}}},[e._v("删除")])]}}])})],1),r("el-pagination",{attrs:{"current-page":e.queryInfo.pagenum,"page-sizes":[5,10,15,20],"page-size":e.queryInfo.pagesize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1),r("el-dialog",{attrs:{title:"添加成员",visible:e.addDialogVisible,width:"500px"},on:{"update:visible":function(t){e.addDialogVisible=t},close:e.addDialogClosed}},[r("el-form",{ref:"addFormRef",attrs:{model:e.addForm,rules:e.addFormRules,"label-width":"110px"}},[r("el-form-item",{attrs:{label:"终端用户：",prop:"value"}},[r("el-select",{ref:"inputValue",attrs:{multiple:"",filterable:"",remote:"","reserve-keyword":"",placeholder:"请输入用户名","remote-method":e.remoteMethod,loading:e.loading},model:{value:e.addForm.value,callback:function(t){e.$set(e.addForm,"value",t)},expression:"addForm.value"}},e._l(e.options,(function(e){return r("el-option",{key:e.deviceId,attrs:{label:e.deviceName,value:e.deviceId}})})),1)],1)],1),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.addDialogVisible=!1}}},[e._v("取 消")]),r("el-button",{directives:[{name:"preventReClick",rawName:"v-preventReClick",value:1e3,expression:"1000"}],attrs:{type:"primary"},on:{click:e.submitAdd}},[e._v("确 定")])],1)],1)],1)},a=[],o=(r("99af"),r("96cf"),r("1da1")),i={data:function(){return{userList:[],queryInfo:{pagenum:1,pagesize:10},total:0,addDialogVisible:!1,queryValue:"",options:[],loading:!1,addForm:{value:[]},addFormRules:{value:[{required:!0,message:"必填项不能为空",trigger:"change"}]},multipleSelection:[],deleteCode:[]}},beforeRouteEnter:function(e,t,r){var n=window.sessionStorage.getItem("accountTypeLogin");if("9"!==n)return r("/404");r()},created:function(){this.getUserList()},methods:{getUserList:function(){var e=this;return Object(o["a"])(regeneratorRuntime.mark((function t(){var r,n;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.$http.get("groupDeviceUser/list/".concat(e.queryInfo.pagenum,"/").concat(e.queryInfo.pagesize,"?ts=").concat((new Date).getTime()),{params:{groupId:e.$route.query.groupId}});case 2:if(r=t.sent,n=r.data,0===n.code){t.next=6;break}return t.abrupt("return",e.$message.error("获取用户信息失败！"));case 6:e.userList=n.data.list,e.total=n.data.total;case 8:case"end":return t.stop()}}),t)})))()},handleSizeChange:function(e){this.queryInfo.pagesize=e,this.getUserList()},handleCurrentChange:function(e){this.queryInfo.pagenum=e,this.getUserList()},remoteMethod:function(e){var t=this;return Object(o["a"])(regeneratorRuntime.mark((function r(){var n,a;return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:if(""===e){r.next=11;break}return t.queryValue=e,t.loading=!0,r.next=5,t.$http.get("device/queryDeviceUser?ts=".concat((new Date).getTime()),{params:{deviceName:e}});case 5:if(n=r.sent,a=n.data,0===a.code){r.next=9;break}return r.abrupt("return",t.options=[]);case 9:t.loading=!1,t.options=a.data;case 11:case"end":return r.stop()}}),r)})))()},addDialogClosed:function(){this.$refs.addFormRef.resetFields(),this.options=[]},submitAdd:function(){var e=this;return Object(o["a"])(regeneratorRuntime.mark((function t(){return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:e.$refs.addFormRef.validate(function(){var t=Object(o["a"])(regeneratorRuntime.mark((function t(r){var n,a;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(r){t.next=2;break}return t.abrupt("return");case 2:return t.next=4,e.$http.post("groupDeviceUser/add",{groupId:e.$route.query.groupId,deviceId:e.addForm.value});case 4:if(n=t.sent,a=n.data,0===a.code){t.next=8;break}return t.abrupt("return",e.$message.error(null!==a.msg&&""!==a.msg?a.msg:"添加失败！"));case 8:e.$message.success("添加成功！"),e.addDialogVisible=!1,e.getUserList();case 11:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}());case 1:case"end":return t.stop()}}),t)})))()},deleById:function(e){var t=this;return Object(o["a"])(regeneratorRuntime.mark((function r(){var n;return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:return r.next=2,t.$confirm("此操作将删除该用户，是否继续？","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).catch((function(e){return e}));case 2:if(n=r.sent,"confirm"===n){r.next=5;break}return r.abrupt("return");case 5:t.deleteCode.push(e),t.deleteMethod();case 7:case"end":return r.stop()}}),r)})))()},getRowKeys:function(e){return e.id},handleSelectionChange:function(e){this.multipleSelection=e},batchDeleteBuild:function(){var e=this;return Object(o["a"])(regeneratorRuntime.mark((function t(){var r,n;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.$confirm("此操作将删除勾选用户，是否继续？","提示",{confimrButtonText:"确定",cancelButtonText:"取消",type:"warning"}).catch((function(e){return e}));case 2:if(r=t.sent,"confirm"===r){t.next=5;break}return t.abrupt("return");case 5:for(n=0;n<e.multipleSelection.length;n++)e.deleteCode.push(e.multipleSelection[n].id);e.deleteMethod();case 7:case"end":return t.stop()}}),t)})))()},deleteMethod:function(){var e=this;return Object(o["a"])(regeneratorRuntime.mark((function t(){var r,n;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.$http.post("groupDeviceUser/delete",{ids:e.deleteCode});case 2:if(r=t.sent,n=r.data,0===n.code){t.next=6;break}return t.abrupt("return",e.$message.error(""!==n.msg&&null!==n.msg?n.msg:"删除失败！"));case 6:e.$message.success("删除成功！"),e.deleteCode=[],e.$refs.multipleTable.clearSelection(),e.getUserList();case 10:case"end":return t.stop()}}),t)})))()}}},s=i,u=(r("5adc"),r("2877")),l=Object(u["a"])(s,n,a,!1,null,"16c3fd18",null);t["default"]=l.exports}}]);
//# sourceMappingURL=chunk-54563e51.8c59a2e6.js.map