(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-7059c51f"],{2174:function(e,t,r){"use strict";var a=r("4d64d"),n=r.n(a);n.a},"4b88":function(e,t,r){"use strict";r.r(t);var a=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticStyle:{"min-width":"1100px"}},[r("el-breadcrumb",{attrs:{"separator-class":"el-icon-arrow-right"}},[r("el-breadcrumb-item",{attrs:{to:{path:"/home"}}},[e._v("首页")]),r("el-breadcrumb-item",[e._v("主密钥管理")]),r("el-breadcrumb-item",[e._v("密钥列表")])],1),r("el-card",[r("el-row",[r("el-button",{attrs:{type:"primary"},on:{click:function(t){e.addDialogVisible=!0}}},[e._v("创建")])],1),r("el-table",{attrs:{data:e.keyList,stripe:"","row-key":e.getRowKeys,"expand-row-keys":e.expands},on:{"expand-change":e.expandChange}},[r("el-table-column",{attrs:{type:"expand"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-form",{staticClass:"demo-table-expand",attrs:{"label-position":"left",inline:""}},[r("el-form-item",{attrs:{label:"密钥的状态："}},["Enabled"===t.row.ruleItemData.keyState?r("span",[e._v("启用")]):"Disabled"===t.row.ruleItemData.keyState?r("span",[e._v("禁用")]):"PendingDeletion"===t.row.ruleItemData.keyState?r("span",[e._v("待删除")]):"PendingImport"===t.row.ruleItemData.keyState?r("span",[e._v("待导入")]):e._e(),r("el-button",{directives:[{name:"show",rawName:"v-show",value:"Enabled"===t.row.ruleItemData.keyState||"Disabled"===t.row.ruleItemData.keyState,expression:"props.row.ruleItemData.keyState === 'Enabled' ||  props.row.ruleItemData.keyState === 'Disabled'"}],attrs:{size:"mini",type:"primary",plain:""},on:{click:function(r){return e.onEnable(t.row.ruleItemData.keyState,t.row.ruleItemData.keyId)}}},[e._v(" 修改状态 ")])],1),r("el-form-item",{attrs:{label:"密钥材料来源："}},[r("span",[e._v(e._s(t.row.ruleItemData.origin))])]),r("el-form-item",{attrs:{label:"创建日期："}},[r("span",[e._v(e._s(t.row.ruleItemData.creationDate))])]),r("el-form-item",{attrs:{label:"预删除时间："}},[r("span",[e._v(e._s(""===t.row.ruleItemData.deleteDate?"无":t.row.ruleItemData.deleteDate))])]),r("el-form-item",{attrs:{label:"密钥的用途："}},[r("span",[e._v(e._s(t.row.ruleItemData.keyUsage))])]),r("el-form-item",{attrs:{label:"主版本标志符："}},[r("span",[e._v(e._s(t.row.ruleItemData.primaryKeyVersion))])]),r("el-form-item",{attrs:{label:"保护级别："}},[r("span",[e._v(e._s(t.row.ruleItemData.protectionLevel))])]),r("el-form-item",{attrs:{label:"密钥的类型："}},[r("span",[e._v(e._s(t.row.ruleItemData.keySpec))])]),r("el-form-item",{attrs:{label:"密钥的描述："}},[r("span",{attrs:{title:t.row.ruleItemData.description}},[e._v(e._s(""===t.row.ruleItemData.description?"无":t.row.ruleItemData.description))]),r("el-button",{attrs:{size:"mini",type:"primary",plain:""},on:{click:function(r){return e.onDetail(t.row.ruleItemData.keyId,t.row.ruleItemData.description)}}},[e._v(" 更新描述 ")])],1)],1)]}}])}),r("el-table-column",{attrs:{type:"index",label:"序号"}}),r("el-table-column",{attrs:{label:"密钥标识",prop:"keyId"}}),r("el-table-column",{attrs:{label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-button",{attrs:{size:"mini",type:"danger",plain:""},on:{click:function(r){return e.deleOpen(t.row.keyId)}}},[e._v("删除 ")]),r("el-button",{attrs:{size:"mini",type:"primary",plain:""},on:{click:function(r){return e.deleCancel(t.row.keyId)}}},[e._v("撤销删除 ")]),r("el-button",{attrs:{size:"mini",type:"warning",plain:""},on:{click:function(r){return e.deleMaterial(t.row.keyId)}}},[e._v("删除密钥材料 ")]),r("el-button",{attrs:{size:"mini",type:"success",plain:""},on:{click:function(r){return e.openVersion(t.row.keyId)}}},[e._v("查看密钥版本 ")])]}}])})],1),r("el-pagination",{attrs:{"current-page":e.queryInfo.pagenum,"page-sizes":[5,10,15,20],"page-size":e.queryInfo.pagesize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1),r("el-dialog",{attrs:{title:"删除",visible:e.deleDialogVisible,width:"500px"},on:{"update:visible":function(t){e.deleDialogVisible=t},close:e.deleDialogClosed}},[r("el-form",{ref:"deleFormRef",attrs:{model:e.deleForm,rules:e.deleFormRules,"status-icon":"","label-width":"150px"}},[r("el-form-item",{attrs:{label:"预删除周期（天）",prop:"num"}},[r("el-input",{model:{value:e.deleForm.num,callback:function(t){e.$set(e.deleForm,"num","string"===typeof t?t.trim():t)},expression:"deleForm.num"}})],1)],1),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.deleDialogVisible=!1}}},[e._v("取 消")]),r("el-button",{attrs:{type:"primary"},on:{click:e.deleNum}},[e._v("确 定")])],1)],1),r("el-dialog",{attrs:{title:"创建一个主密钥",visible:e.addDialogVisible,width:"500px"},on:{"update:visible":function(t){e.addDialogVisible=t},close:e.addDialogClosed}},[r("el-form",{ref:"addFormRef",attrs:{model:e.addForm,rules:e.addFormRules,"label-width":"130px"}},[r("el-form-item",{attrs:{label:"密钥类型",prop:"keySpec"}},[r("el-select",{attrs:{placeholder:"请选择"},model:{value:e.addForm.keySpec,callback:function(t){e.$set(e.addForm,"keySpec","string"===typeof t?t.trim():t)},expression:"addForm.keySpec"}},[r("el-option",{attrs:{label:"QTEC_SM4",value:"QTEC_SM4"}}),r("el-option",{attrs:{label:"EC_SM9",value:"EC_SM9"}}),r("el-option",{attrs:{label:"EC_SM2",value:"EC_SM2"}})],1)],1),r("el-form-item",{attrs:{label:"密钥用途",prop:"keyUsage"}},[r("el-radio-group",{model:{value:e.addForm.keyUsage,callback:function(t){e.$set(e.addForm,"keyUsage","string"===typeof t?t.trim():t)},expression:"addForm.keyUsage"}},[r("el-radio",{attrs:{label:"ENCRYPT/DECRYPT"}},[e._v("ENCRYPT/DECRYPT")]),r("el-radio",{attrs:{label:"SIGN/VERIFY"}},[e._v("SIGN/VERIFY")])],1)],1),r("el-form-item",{attrs:{label:"材料来源",prop:"origin"}},[r("el-radio-group",{model:{value:e.addForm.origin,callback:function(t){e.$set(e.addForm,"origin","string"===typeof t?t.trim():t)},expression:"addForm.origin"}},[r("el-radio",{attrs:{label:"QTEC_KMS"}},[e._v("QTEC_KMS")]),r("el-radio",{attrs:{label:"EXTERNAL"}},[e._v("EXTERNAL")])],1)],1),r("el-form-item",{attrs:{label:"保护级别",prop:"protectionLevel"}},[r("el-radio-group",{model:{value:e.addForm.protectionLevel,callback:function(t){e.$set(e.addForm,"protectionLevel","string"===typeof t?t.trim():t)},expression:"addForm.protectionLevel"}},[r("el-radio",{attrs:{label:"HSM"}},[e._v("HSM")]),r("el-radio",{attrs:{label:"SOFTWARE"}},[e._v("SOFTWARE")])],1)],1),"QTEC_SM4"===e.addForm.keySpec?r("el-form-item",{attrs:{label:"开启密钥轮转",prop:"enableAutomaticRotation"}},[r("el-radio-group",{model:{value:e.addForm.enableAutomaticRotation,callback:function(t){e.$set(e.addForm,"enableAutomaticRotation","string"===typeof t?t.trim():t)},expression:"addForm.enableAutomaticRotation"}},[r("el-radio",{attrs:{label:"true"}},[e._v("是")]),r("el-radio",{attrs:{label:"false"}},[e._v("否")])],1)],1):e._e(),"QTEC_SM4"===e.addForm.keySpec&&"true"===e.addForm.enableAutomaticRotation?r("el-form-item",{attrs:{label:"轮转周期（天）",prop:"rotationInterval"}},[r("el-input",{model:{value:e.addForm.rotationInterval,callback:function(t){e.$set(e.addForm,"rotationInterval","string"===typeof t?t.trim():t)},expression:"addForm.rotationInterval"}})],1):e._e(),r("el-form-item",{attrs:{label:"密钥的描述",prop:"description"}},[r("el-input",{attrs:{type:"textarea",maxlength:"128","show-word-limit":"",rows:8},model:{value:e.addForm.description,callback:function(t){e.$set(e.addForm,"description","string"===typeof t?t.trim():t)},expression:"addForm.description"}})],1)],1),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.addDialogVisible=!1}}},[e._v("取 消")]),r("el-button",{directives:[{name:"preventReClick",rawName:"v-preventReClick",value:3e3,expression:"3000"}],attrs:{type:"primary"},on:{click:e.submitAdd}},[e._v("确 定")])],1)],1),r("el-dialog",{attrs:{title:"更新描述信息",visible:e.updateDialogVisible,width:"500px"},on:{"update:visible":function(t){e.updateDialogVisible=t},close:e.updateDialogClosed}},[r("el-form",{ref:"updateFormRef",attrs:{model:e.updateForm,"status-icon":"","label-width":"100px"}},[r("el-form-item",{attrs:{label:"密钥的描述",prop:"detail"}},[r("el-input",{attrs:{type:"textarea",rows:8},model:{value:e.updateForm.detail,callback:function(t){e.$set(e.updateForm,"detail","string"===typeof t?t.trim():t)},expression:"updateForm.detail"}})],1)],1),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.updateDialogVisible=!1}}},[e._v("取 消")]),r("el-button",{directives:[{name:"preventReClick",rawName:"v-preventReClick",value:1e3,expression:"1000"}],attrs:{type:"primary"},on:{click:e.updateDetail}},[e._v("确 定")])],1)],1)],1)},n=[],o=(r("a4d3"),r("e01a"),r("4160"),r("d81d"),r("159b"),r("96cf"),r("1da1")),i={data:function(){var e=function(e,t,r){var a=/^[0-9]*[1-9][0-9]*$/;if(a.test(t)){if(t>6&&t<31)return r();r(new Error("请输入7-30的数字"))}r(new Error("请输入7-30的数字"))};return{getRowKeys:function(e){return e.keyId},expands:[],keyList:[],queryInfo:{pagenum:1,pagesize:10},total:0,deleKeyId:"",deleDialogVisible:!1,deleForm:{num:""},deleFormRules:{num:[{required:!0,validator:e,trigger:"blur"}]},addDialogVisible:!1,addForm:{keySpec:"QTEC_SM4",keyUsage:"",origin:"",protectionLevel:"",enableAutomaticRotation:"false",rotationInterval:"",description:""},addFormRules:{rotationInterval:[{required:!0,validator:e,trigger:"blur"}],origin:[{required:!0,message:"请选择材料来源",trigger:"change"}],keyUsage:[{required:!0,message:"请选择密钥用途",trigger:"change"}],protectionLevel:[{required:!0,message:"请选择保护级别",trigger:"change"}]},updateDialogVisible:!1,updateForm:{detail:""}}},beforeRouteEnter:function(e,t,r){var a=window.sessionStorage.getItem("accountTypeLogin");if("9"!==a)return r("/404");r()},created:function(){this.getKeyId()},methods:{openVersion:function(e){this.$router.push({path:"/version",query:{keyId:e}})},getKeyId:function(){var e=this;return Object(o["a"])(regeneratorRuntime.mark((function t(){var r,a;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.$http.post("v1/kms/ListKeys1",{pageNumber:e.queryInfo.pagenum,pageSize:e.queryInfo.pagesize});case 2:if(r=t.sent,a=r.data,0!==a.code){t.next=10;break}a.data.list.map((function(e){e.ruleItemData={}})),e.keyList=a.data.list,e.total=a.data.total,t.next=11;break;case 10:return t.abrupt("return",e.$message.error(""!==a.msg&&null!==a.msg?a.msg:"获取密钥列表失败！"));case 11:case"end":return t.stop()}}),t)})))()},handleSizeChange:function(e){this.queryInfo.pagesize=e,this.getKeyId()},handleCurrentChange:function(e){this.queryInfo.pagenum=e,this.getKeyId()},expandChange:function(e,t){var r=this;return Object(o["a"])(regeneratorRuntime.mark((function a(){return regeneratorRuntime.wrap((function(a){while(1)switch(a.prev=a.next){case 0:r.expands=[],t.length>0&&(e&&r.expands.push(e.keyId),r.getDetail(e.keyId));case 2:case"end":return a.stop()}}),a)})))()},getDetail:function(e){var t=this;return Object(o["a"])(regeneratorRuntime.mark((function r(){var a,n;return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:return r.next=2,t.$http.post("v1/kms/DescribeKey",{keyId:e});case 2:if(a=r.sent,n=a.data,0===n.code){r.next=6;break}return r.abrupt("return",t.$message.error(""!==n.msg&&null!==n.msg?n.msg:"获取详情失败！"));case 6:t.keyList.forEach((function(r,a){r.keyId===e&&(t.keyList[a].ruleItemData=n.data.keyMetadata)}));case 7:case"end":return r.stop()}}),r)})))()},onEnable:function(e,t){var r=this;return Object(o["a"])(regeneratorRuntime.mark((function a(){var n,o,i,s,l;return regeneratorRuntime.wrap((function(a){while(1)switch(a.prev=a.next){case 0:return a.next=2,r.$confirm("确定要修改状态？","提示",{confirmButtonText:"确定",cancelButtontext:"取消",type:"warning"}).catch((function(e){return e}));case 2:if(n=a.sent,"confirm"===n){a.next=5;break}return a.abrupt("return");case 5:if("Enabled"!==e){a.next=16;break}return a.next=8,r.$http.post("v1/kms/DisableKey",{keyId:t});case 8:if(o=a.sent,i=o.data,0===i.code){a.next=12;break}return a.abrupt("return",r.$message.error(""!==i.msg&&null!==i.msg?i.msg:"修改密钥状态失败！"));case 12:r.$message.success("修改密钥状态成功！"),r.getDetail(t),a.next=24;break;case 16:return a.next=18,r.$http.post("v1/kms/EnableKey",{keyId:t});case 18:if(s=a.sent,l=s.data,0===l.code){a.next=22;break}return a.abrupt("return",r.$message.error(""!==l.msg&&null!==l.msg?l.msg:"修改密钥状态失败！"));case 22:r.$message.success("修改密钥状态成功！"),r.getDetail(t);case 24:case"end":return a.stop()}}),a)})))()},deleOpen:function(e){this.deleKeyId=e,this.deleDialogVisible=!0},deleNum:function(){var e=this;this.$refs.deleFormRef.validate(function(){var t=Object(o["a"])(regeneratorRuntime.mark((function t(r){var a,n;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(r){t.next=2;break}return t.abrupt("return");case 2:return t.next=4,e.$http.post("v1/kms/ScheduleKeyDeletion",{keyId:e.deleKeyId,pendingWindowInDays:e.deleForm.num});case 4:if(a=t.sent,n=a.data,0===n.code){t.next=8;break}return t.abrupt("return",e.$message.error(""!==n.msg&&null!==n.msg?n.msg:"预删除密钥失败！"));case 8:e.$message.success("预删除密钥成功！"),e.deleDialogVisible=!1,e.expands=[],e.getKeyId();case 12:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}())},deleDialogClosed:function(){this.$refs.deleFormRef.resetFields()},deleCancel:function(e){var t=this;return Object(o["a"])(regeneratorRuntime.mark((function r(){var a,n,o;return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:return r.next=2,t.$confirm("确定要撤销密钥删除？","提示",{confirmButtonText:"确定",cancelButtontext:"取消",type:"warning"}).catch((function(e){return e}));case 2:if(a=r.sent,"confirm"===a){r.next=5;break}return r.abrupt("return");case 5:return r.next=7,t.$http.post("v1/kms/CancelKeyDeletion",{keyId:e});case 7:if(n=r.sent,o=n.data,0===o.code){r.next=11;break}return r.abrupt("return",t.$message.error(""!==o.msg&&null!==o.msg?o.msg:"撤销密钥删除失败！"));case 11:t.$message.success("撤销密钥删除成功！"),t.expands=[],t.getKeyId();case 14:case"end":return r.stop()}}),r)})))()},addDialogClosed:function(){this.$refs.addFormRef.resetFields()},submitAdd:function(){var e=this;this.$refs.addFormRef.validate(function(){var t=Object(o["a"])(regeneratorRuntime.mark((function t(r){var a,n,o;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(r){t.next=2;break}return t.abrupt("return");case 2:return a={keySpec:e.addForm.keySpec,keyUsage:e.addForm.keyUsage,origin:e.addForm.origin,protectionLevel:e.addForm.protectionLevel,enableAutomaticRotation:e.addForm.enableAutomaticRotation,rotationInterval:e.addForm.rotationInterval+"d",description:e.addForm.description},t.next=5,e.$http.post("v1/kms/CreateKey",a);case 5:if(n=t.sent,o=n.data,0===o.code){t.next=9;break}return t.abrupt("return",e.$message.error(""!==o.msg&&null!==o.msg?o.msg:"创建失败！"));case 9:e.$message.success("成功创建密钥标识："+o.data.keyMetadata.keyId),e.addDialogVisible=!1,e.getKeyId();case 12:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}())},deleMaterial:function(e){var t=this;return Object(o["a"])(regeneratorRuntime.mark((function r(){var a,n,o;return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:return r.next=2,t.$confirm("确定要删除导入的密钥材料？","提示",{confirmButtonText:"确定",cancelButtontext:"取消",type:"warning"}).catch((function(e){return e}));case 2:if(a=r.sent,"confirm"===a){r.next=5;break}return r.abrupt("return");case 5:return r.next=7,t.$http.post("v1/kms/DeleteKeyMaterial",{keyId:e});case 7:if(n=r.sent,o=n.data,0===o.code){r.next=11;break}return r.abrupt("return",t.$message.error(""!==o.msg&&null!==o.msg?o.msg:"删除密钥材料失败！"));case 11:t.$message.success("删除密钥材料成功！"),t.expands=[],t.getKeyId();case 14:case"end":return r.stop()}}),r)})))()},updateDialogClosed:function(){this.$refs.updateFormRef.resetFields()},onDetail:function(e,t){this.deleKeyId=e,this.updateForm.detail=t,this.updateDialogVisible=!0},updateDetail:function(){var e=this;return Object(o["a"])(regeneratorRuntime.mark((function t(){var r,a;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.$http.post("v1/kms/UpdateKeyDescription",{keyId:e.deleKeyId,description:e.updateForm.detail});case 2:if(r=t.sent,a=r.data,0===a.code){t.next=6;break}return t.abrupt("return",e.$message.error(""!==a.msg&&null!==a.msg?a.msg:"更新密钥描述失败！"));case 6:e.$message.success("更新密钥描述成功！"),e.updateDialogVisible=!1,e.getDetail(e.deleKeyId);case 9:case"end":return t.stop()}}),t)})))()}}},s=i,l=(r("2174"),r("2877")),u=Object(l["a"])(s,a,n,!1,null,"68b58b63",null);t["default"]=u.exports},"4d64d":function(e,t,r){}}]);
//# sourceMappingURL=chunk-7059c51f.455ee4e8.js.map