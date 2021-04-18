var active = "dev";



var baseUrl = "";
var webUrl = "";
if (active == "dev") {
    baseUrl = "http://localhost/healthy/";
    webUrl = "http://localhost/healthy-web/";
}

var config = {
    /*请求接口的基础路径*/
    baseUrl: baseUrl,
    /*html部署的地址*/
    webUrl: webUrl
};
var loginDataStr = sessionStorage.getItem("loginData");
var loginData = {};
var token="";
if (loginDataStr) {
    loginData = JSON.parse(loginDataStr);
    token=sessionStorage.getItem("token");
}else {

}

// 对于href连接后边是需要跟token值
function wrapUrl(url) {
    var result = "";
    if (url) {
        if (url.indexOf("?") > 0) {
            result = url + "&token=" + token;
        }else {
            result = url + "?token=" + token;
        }
    }
    return result;
}

//layer table全局默认设置
if (layui&&layui.table){
    layui.table.set({
        elem: '#tableData',
        height: 'full-170',
        headers:getLoginHeaders(),
        request: { pageName: 'pageNo',limitName: 'pageSize'},
        response: { statusCode:1},
        page: true,
        limits: [10, 30, 50],
        autoSort: false,
        parseData: function (result) { //res 即为原始返回的数据
            if (result&&result.code == 0 && (result.message && result.message.substr(0, 3) == "401")) {
                //过期登录
                window.location.href = config.webUrl + "login.html";
                return;
            }
            return {
                "code": result.code,
                "msg": result.message,
                "count": result.data&&result.data.totalCount?result.data.totalCount:0,
                "data": result.data&&result.data.result ? result.data.result : []
            };
        }
    });
    layui.table.on('sort(sortTable)', function (obj) {
        table.reload('tableData', {
            initSort: obj,
            where: {
                orderName: toLineName(obj.field),
                order: obj.type
            }
        });
    });
}
//layer upload全局默认设置
if (layui&&layui.upload){
    layui.upload.set({
        headers:getLoginHeaders()
    });
}

//复写公用ajax
if(typeof $ !== "undefined"){
    ajaxPublic();
}
function ajaxPublic() {
    var _ajax = $.ajax,
        loading = null;
    $.ajax = function (opt) {
        var _success = opt && opt.success || function (a, b) {},
            _error = opt && opt.error || function (a, b) {},
            _beforeSend = opt && opt.beforeSend || function (a, b) {};
        /**
         * 如果不需要loading图，请传入参数silent：true
         */
        var _opt = $.extend(opt, {
            beforeSend: !opt.silent? function(){
                loading = layer.load(2);
                _beforeSend();
            }:function(){
                _beforeSend();
            },
            success: function (result, states) {
                layer.close(loading);
                if (result&&result.code == 0 && (result.message && result.message.substr(0, 3) == "401")) {
                    //过期登录
                    window.location.href = config.webUrl + "login.html";
                    return;
                }
                _success(result, states);
            },
            complete: function(){
                layer.close(loading);

            },
            error: function (err) {
                if (err.responseJSON && err.responseJSON.code == 0 && (err.responseJSON.message && err.responseJSON.message.substr(0, 3) == "401")) {
                    //过期登录
                    window.location.href = config.webUrl + "login.html";
                    return;
                }
                layer.close(loading);
                _error(err)
            }
        });
        if(!_opt.headers){
            _opt.headers = getLoginHeaders();
        }
        if (opt['contentType'] == null) {
            //上传的时候contentType为false所以要判断
            _opt.headers['Content-Type'] = "application/json;charset=utf-8";
        }
        _ajax(_opt);
    };
}


function getLoginHeaders() {
    return {Authorization: token};
}
//layerui全局设置
if (layui&&layui.laydate){
    layui.laydate.set({
        theme: '#4192fe'
    })
}




