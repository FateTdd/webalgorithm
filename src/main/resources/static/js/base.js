var AUTH_TOKEN="Authorization";
function initMineDataPicker(_id){
    var $input = $("#"+_id);
   var datepickerObj= $input.datetimepicker({
        format: $input.data('format') ? $input.data('format') : false,
        useCurrent: $input.data('use-current') ? $input.data('use-current') : false,
        locale: moment.locale('' + ($input.data('locale') ? $input.data('locale') : '') + ''),
        showTodayButton: $input.data('show-today-button') ? $input.data('show-today-button') : false,
        showClear: $input.data('show-clear') ? $input.data('show-clear') : false,
        showClose: $input.data('show-close') ? $input.data('show-close') : false,
        sideBySide: $input.data('side-by-side') ? $input.data('side-by-side') : false,
        inline: $input.data('inline') ? $input.data('inline') : false,
    });
   return datepickerObj;
}
var compareFile = function (xFile, yFile) {
    var x=eval(xFile.fileName.split(".")[0].replace("db",""));
    var y=eval(yFile.fileName.split(".")[0].replace("db",""));
    if (x < y) {
        return -1;
    } else if (x > y) {
        return 1;
    } else {
        return 0;
    }
}
var compareAnswer= function (xAnswer, yAnswer) {
    var x=eval(xAnswer.feedback.queryNum);
    var y=eval(yAnswer.feedback.queryNum);
    if (x < y) {
        return -1;
    } else if (x > y) {
        return 1;
    } else {
        return 0;
    }
}
//时间戳转换方法    date:时间戳数字
function formatDate(_datetime) {
    var date = new Date(_datetime);
    var YY = date.getFullYear() + '-';
    var MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
    var hh = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    var mm = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    var ss = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
    return YY + MM + DD +" "+hh + mm + ss;
}
/**
 * 发起授权请求
 *
 * @param url 地址
 * @param type 类型
 * @param data 参数
 * @param success 成功回调
 * @param error 失败回调
 */
function ajaxReq(url, type, data, success, error) {
    // if(data!=null){
    //     data.access_token = getAccessToken();
    // }
    $.ajax({
        url: url,
        type: type,
        data: data,
        success: function(ret){
            if(ret.code==403){
                if(doLogout){
                    doLogout();
                }else{
                    parent.logout();
                }
                return;
            }
            success(ret);
        },
        error: error,
        statusCode: {
            // 401: function () {
            //     flaush();
            // }
        },
        beforeSend: function(request) {
            var tmpToken=window.localStorage.getItem(AUTH_TOKEN);
            if(tmpToken&&tmpToken!=''){
                request.setRequestHeader(AUTH_TOKEN,tmpToken);
            }
        }
    });
}

/**
 * Get请求
 * @param url 地址
 * @param success 成功回调
 * @param fail 失败回调
 */
function ajaxGet(url, success, fail) {
    ajaxReq(url, 'GET', null, success, fail);
}

/**
 * Post请求
 * @param url 地址
 * @param data 请求参数
 * @param success 成功回调
 * @param fail 失败回调
 */
function ajaxPost(url, data, success, fail) {
    ajaxReq(url, 'POST', data, success, fail);
}

/**
 * Put请求
 * @param url 地址
 * @param data 请求参数
 * @param success 成功回调
 * @param fail 失败回调
 */
function ajaxPut(url, data, success, fail) {
    ajaxReq(url, 'PUT', data, success, fail);
}

/**
 * Patch请求
 * @param url 地址
 * @param data 请求参数
 * @param success 成功回调
 * @param fail 失败回调
 */
function ajaxPatch(url, data, success, fail) {
    ajaxReq(url, 'PATCH', data, success, fail);
}

/**
 * Delete请求
 * @param url 地址
 * @param success 成功回调
 * @param fail 失败回调
 */
function ajaxDelete(url, success, fail) {
    ajaxReq(url, 'DELETE', null, success, fail);
}

/**
 * Post请求
 * @param url 地址
 * @param data 请求参数
 * @param success 成功回调
 * @param fail 失败回调
 */
function ajaxDelete(url, data, success, fail) {
    ajaxReq(url, 'DELETE', data, success, fail);
}
/**
 *
 * @param url 地址
 * @param type 类型
 * @param data 参数
 * @param success 成功回调
 * @param error 失败回调
 */
function ajaxJson(url, type, data, success, error) {
    $.ajax({
        url: url,
        type: type,
        dataType: "json",
        data: data,
        contentType: 'application/json',
        success: success,
        error: error,
        beforeSend: function(request) {
            var tmpToken=window.localStorage.getItem(AUTH_TOKEN);
            if(tmpToken&&tmpToken!=''){
                request.setRequestHeader(AUTH_TOKEN,tmpToken);
            }
        }
    })
}
/**
 * 获取 url后面的参数
 * @returns {Object}
 * @constructor
 */
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

/**
 * 获取 url后面的参数(针对中文参数)
 * @returns {Object}
 * @constructor
 */
function getUrlCHParam(key){
    // 获取参数
    var url = window.location.search;
    // 正则筛选地址栏
    var reg = new RegExp("(^|&)"+ key +"=([^&]*)(&|$)");
    // 匹配目标参数
    var result = url.substr(1).match(reg);
    //返回参数值
    return result ? decodeURIComponent(result[2]) : null;
}

/**
 * 浏览器是哪个系统
 * @returns {{isTablet: boolean, isPhone: boolean, isAndroid: boolean, isPc: boolean}}
 * @constructor
 */
OS = function () {
    var ua = navigator.userAgent,
        isWindowsPhone = /(?:Windows Phone)/.test(ua),
        isSymbian = /(?:SymbianOS)/.test(ua) || isWindowsPhone,
        isAndroid = /(?:Android)/.test(ua),
        isFireFox = /(?:Firefox)/.test(ua),
        isChrome = /(?:Chrome|CriOS)/.test(ua),
        isTablet = /(?:iPad|PlayBook)/.test(ua) || (isAndroid && !/(?:Mobile)/.test(ua)) || (isFireFox && /(?:Tablet)/.test(ua)),
        isPhone = /(?:iPhone)/.test(ua) && !isTablet,
        isPc = !isPhone && !isAndroid && !isSymbian;
    return {
        isTablet: isTablet,
        isPhone: isPhone,
        isAndroid: isAndroid,
        isPc: isPc
    };
};

/**
 * 文件下载
 * @param _fileName 文件名
 * @param _savePath 文件的存储路径
 */
function doDownload(_fileName,_savePath){
    var form = $("<form>");
    form.attr("style","display:none");
    form.attr("target","");
    form.attr("method","post");
    form.attr("action","../../file/download.do");
    var input1 = $("<input>");
    input1.attr("type","hidden");
    input1.attr("name","fileName");
    input1.attr("value",_fileName);
    var input2 = $("<input>");
    input2.attr("type","hidden");
    input2.attr("name","savePath");
    input2.attr("value",decodeURIComponent(_savePath));
    $("body").append(form);
    form.append(input1);
    form.append(input2);
    form.submit();
    form.remove();
}
/**
 * 模板文件下载
 * @param _fileName 文件名
 * @param _savePath 文件的存储路径
 */
function doDownloadTemplate(_fileName,_savePath){
    var form = $("<form>");
    form.attr("style","display:none");
    form.attr("target","");
    form.attr("method","post");
    form.attr("action","../../file/downloadTempalte.do");
    var input1 = $("<input>");
    input1.attr("type","hidden");
    input1.attr("name","fileName");
    input1.attr("value",_fileName);
    var input2 = $("<input>");
    input2.attr("type","hidden");
    input2.attr("name","savePath");
    input2.attr("value",decodeURIComponent(_savePath));
    $("body").append(form);
    form.append(input1);
    form.append(input2);
    form.submit();
    form.remove();
}

//预览图片
function previewImg(_this) {
    layer.open({
        type: 1,
        title: false,
        closeBtn: 1,
        area: ['80%', '90%'],
        skin: 'layui-layer-nobg', //没有背景色
        shadeClose: true,
        content: $(_this).parent().find(".preview")
    });
}
//jquery表单转化为JSON $("#post_form").serializeJson()
(function($){
    $.fn.serializeJson=function(){
        var serializeObj={};
        var array=this.serializeArray();
        var str=this.serialize();
        $(array).each(function(){
            if(serializeObj[this.name]){
                if($.isArray(serializeObj[this.name])){
                    serializeObj[this.name].push(this.value);
                }else{
                    serializeObj[this.name]=[serializeObj[this.name],this.value];
                }
            }else{
                serializeObj[this.name]=this.value;
            }
        });
        return serializeObj;
    };
    //重写转json方法 取消对disbale的忽略
    $.fn.serializeJsonTsxq=function(){
        var serializeObj={};
        var array=this.newSerArray();
        var str=this.serialize();
        $(array).each(function(){
            if(serializeObj[this.name]){
                if($.isArray(serializeObj[this.name])){
                    serializeObj[this.name].push(this.value);
                }else{
                    serializeObj[this.name]=[serializeObj[this.name],this.value];
                }
            }else{
                serializeObj[this.name]=this.value;
            }
        });
        return serializeObj;
    };
    var rcheckableType = ( /^(?:checkbox|radio)$/i );
    var
        rCRLF = /\r?\n/g,
        rsubmitterTypes = /^(?:submit|button|image|reset|file)$/i,
        rsubmittable = /^(?:input|select|textarea|keygen)/i;
    $.fn.newSerArray=function(){
        return this.map( function() {

            // Can add propHook for "elements" to filter or add form elements
            var elements = jQuery.prop( this, "elements" );
            return elements ? jQuery.makeArray( elements ) : this;
        } )
            .filter( function() {
                var type = this.type;

                // Use .is( ":disabled" ) so that fieldset[disabled] works
                return this.name &&
                    rsubmittable.test( this.nodeName ) && !rsubmitterTypes.test( type ) &&
                    ( this.checked || !rcheckableType.test( type ) );
            } )
            .map( function( i, elem ) {
                var val = jQuery( this ).val();

                if ( val == null ) {
                    return null;
                }

                if ( Array.isArray( val ) ) {
                    return jQuery.map( val, function( val ) {
                        return { name: elem.name, value: val.replace( rCRLF, "\r\n" ) };
                    } );
                }

                return { name: elem.name, value: val.replace( rCRLF, "\r\n" ) };
            } ).get();
    };


    $.fn.populateForm = function(data){
        return this.each(function(){
            var formElem, name;
            if(data == null){this.reset(); return; }
            for(var i = 0; i < this.length; i++){
                formElem = this.elements[i];
                //checkbox的name可能是name[]数组形式
                name = (formElem.type == "checkbox")? formElem.name.replace(/(.+)\[\]$/, "$1") : formElem.name;
                if(data[name] == undefined) continue;

                switch(formElem.type){

                    case "checkbox":
                        if(data[name] == ""){
                            formElem.checked = false;
                        }else{
                            //数组查找元素
                            if(data[name].indexOf(formElem.value) > -1){
                                formElem.checked = true;
                            }else{
                                formElem.checked = false;
                            }
                        }
                        break;
                    case "radio":
                        var ffidd=data[name];
                        if(data[name] == ""&&data[name]!=0){
                            formElem.checked = false;
                        }else if(formElem.value == data[name]){
                            formElem.checked = true;
                        }
                        break;
                    case "button": break;
                    default: formElem.value = data[name];
                }
            }
        });
    };
    $.fn.populateFormNoCheckbox = function(data){
        return this.each(function(){
            var formElem, name;
            if(data == null){this.reset(); return; }
            for(var i = 0; i < this.length; i++){
                formElem = this.elements[i];
                //checkbox的name可能是name[]数组形式
                name = (formElem.type == "checkbox")? formElem.name.replace(/(.+)\[\]$/, "$1") : formElem.name;
                if(data[name] == undefined) continue;

                switch(formElem.type){

                    case "radio":
                        var ffidd=data[name];
                        if(data[name] == ""&&data[name]!=0){
                            formElem.checked = false;
                        }else if(formElem.value == data[name]){
                            formElem.checked = true;
                        }
                        break;
                    case "button": break;
                    default: formElem.value = data[name];
                }
            }
        });
    };
    $.fn.populateFormsdhz = function(data){
        return this.each(function(){
            var formElem, name;
            if(data == null){this.reset(); return; }
            for(var i = 1; i < this.length; i++){
                formElem = this.elements[i];
                //checkbox的name可能是name[]数组形式
                name = (formElem.type == "checkbox")? formElem.name.replace(/(.+)\[\]$/, "$1") : formElem.name;
                switch(formElem.type){

                    case "checkbox":
                        if(data[name] == ""){
                            formElem.checked = false;
                        }else{
                            //数组查找元素
                            if(data[name].indexOf(formElem.value) > -1){
                                formElem.checked = true;
                            }else{
                                formElem.checked = false;
                            }
                        }
                        break;
                    case "radio":
                        var ffidd=data[name];
                        if(data[name] == ""&&data[name]!=0){
                            formElem.checked = false;
                        }else if(formElem.value == data[name]){
                            formElem.checked = true;
                        }
                        break;
                    case "button": break;
                    default:
                        if(data[name]==null){
                            formElem.value ="";
                        }else{
                            formElem.value = data[name];
                        }

                }

                // if(data[name] == undefined) continue;
            }
        });
    };
})(jQuery);