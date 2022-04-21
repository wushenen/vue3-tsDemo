const projectName='量子密钥云平台';
const valiPass = (rule, value, cb) => {//密码
  const reg = /^(?![0-9_]+$)(?![a-zA-Z]+$)[0-9A-Za-z_!@#$%&*]{8,16}$/;
  if (reg.test(value)) {
    return cb()
  }
  cb(new Error('请输入数字和字母或特殊字符（_!@#$%&*）组成的字符，长度为8-16位'))
};
const valiUser = (rule, value, cb) => {//用户名
  const reg = /^[a-zA-Z0-9_]{4,16}$/;
  if (reg.test(value)) {
    return cb()
  }
  cb(new Error('请输入数字、字母、下划线组成的字符，长度为4-16位'))
};
const valiPing = (rule, value, cb) => {//PIN码
  const reg = /^[0-9]{8}$/;
  if (reg.test(value)) {
    return cb()
  }
  cb(new Error('请输入数字,长度为8位'))
};
const valiEmail = (rule, value, cb) => {
  const reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
  if (reg.test(value)) {
    return cb()
  }
  cb(new Error('请输入正确的邮箱！'))
};
const valiPhone = (rule, value, cb) => {
  const reg = /^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
  if (reg.test(value)) {
    return cb()
  }
  cb(new Error('请输入正确的手机号！'))
};
const valiPostcode= (rule, value, cb) => {
  const reg = /[1-9]\d{5}(?!\d)/;
  if (reg.test(value)) {
    return cb()
  }
  cb(new Error('请输入正确的6位数邮编！'))
};
const startHttps = (rule, value, cb) => {
  const reg = /^http[s]{0,1}:\/\/([\w.]+\/?)\S*/;
  if (reg.test(value)) {
    return cb()
  }
  cb(new Error('请输入以‘http://或https://’开头的字符'))
};
const valiNumber = (rule, value, cb) => {
    const reg = /^[1-9]\d*$/;
    if (reg.test(value)) {
      return cb()
    }
    cb(new Error('请输入正整数'))
};

const valiResourceCode = (rule, value, cb) => {
    const reg = /^[a-zA-Z0-9*]{1,256}$/;
    if (reg.test(value)) {
      return cb()
    }
    cb(new Error('只能输入数字、字母或*'))
};

const valiIp = (rule, value, cb) => {
  const regIP = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
  if (regIP.test(value)) {
    return cb()
  }
  cb(new Error('请输入正确的IP！'))
};
const valiPort = (rule, value, cb) => {
  const regId = /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
  if (regId.test(value)) {
    return cb()
  }
  cb(new Error('请输入正确的端口号！'))
};
const valiSerialNo = (rule, value, cb) => {
  const reg = /^[a-zA-Z0-9]{16}$/;
  if (reg.test(value)) {
    return cb()
  }
  cb(new Error('请输入数字、字母组成的字符，长度为16位'))
};
/**那么base64编码之后的字符串具有哪些特点：
 * 字符串只可能包含A-Z，a-z，0-9，+，/，=字符
 * 字符串长度是4的倍数
 * =只会出现在字符串最后，可能没有或者一个等号或者两个等号*/
const checkBase64 = (rule, value, cb) => {
  const reg = /^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$/;
  if (reg.test(value)) {
    return cb()
  }
  cb(new Error('请输入base64格式的字符串'))
};
const downloadFile = (res, fileName) => {
  if (!res) return;
  if (window.navigator.msSaveBlob) { // IE以及IE内核的浏览器
    try {
      window.navigator.msSaveBlob(res, fileName);//res为接口返回数据，这里请求的时候已经处理了，如果没处理需要在此之前自行处理var data=new Blob([res.data])注意是数组形式，fileName就是下载之后的文件名
      //window.navigator.msSaveOrOpenBlob(res,fileName);此方法类似上面的方法
    } catch (e) {
      console.log(e)
    }
  } else {
    let url = window.URL.createObjectURL(new Blob([res]));
    let link = document.createElement('a');
    link.style.display = 'none';
    link.href = url;
    link.setAttribute('download', fileName);//文件名
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);//下载完成移除元素
    window.URL.revokeObjectURL(url);// 释放掉blob对象
  }
};
export default
{
  projectName,
  valiIp,
  valiPort,
  valiPass,
  valiUser,
  valiPing,
  valiEmail,
  valiPhone,
  valiPostcode,
  startHttps,
  valiNumber,
  valiResourceCode,
  valiSerialNo,
  checkBase64,
  downloadFile
}
