//选择一条记录详情
function getSelectedRowObj() {
    var dataInfo = table.checkStatus("tableData");
    if (dataInfo.data.length != 1) {
        layer.alert("请选择一条数据");
        return null;

    }
    return dataInfo.data[0];
}
//选择一条记录 返回记录id
function getSelectedRow() {
    var dataInfo = table.checkStatus("tableData");
    var ids = [];
    for (var i = 0; i < dataInfo.data.length; i++) {
        ids.push(dataInfo.data[i].id);
    }
    if (ids.length != 1) {
        layer.alert("请选择一条数据");
        return null;

    }
    return ids[0];
}

//选择多条记录 返回多个id
function getSelectedRows() {
    var dataInfo = table.checkStatus("tableData");
    var ids = [];
    for (var i = 0; i < dataInfo.data.length; i++) {
        ids.push(dataInfo.data[i].id);
    }
    if (ids.length <= 0) {
        layer.alert("至少选择一条数据");
        return null;
    }
    return ids;
}

/*下划线转驼峰*/
function toHumpName(name) {
    return name.replace(/\_(\w)/g, function(all, letter){
        return letter.toUpperCase();
    });
}

/* 驼峰转换下划线*/
function toLineName(name) {
    return name.replace(/([A-Z])/g,"_$1").toLowerCase();
}

/**
 * 获取链接后边的参数
 * @returns {Object}
 */
function getRequestParams() {
    var url = location.search;
    var theRequest = {};
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        var arr = str.split("&");
        if (arr) {
            for(var i = 0; i < arr.length; i ++) {
                var keyValue = arr[i].split("=");
                if (keyValue && keyValue.length >= 1) {
                    if (keyValue.length == 2) {
                        theRequest[keyValue[0]]=unescape(keyValue[1]);
                    }else {
                        theRequest[keyValue[0]] = null;
                    }

                }
            }
        }
    }
    return theRequest;
}

/**
 *
 * @param url 地址
 * @param obj 要拼接参数的对象
 * @returns {string}
 */
function buildExportUrl(url, obj) {
    var suffixUrl = url.indexOf("?") >= 0?"&":"?";
    if(obj){
        for (var key in obj) {
            if (key == 'pageNo' || key == 'pageSize') {
                continue;
            }
            if (!isEmpty(obj[key])) {
                //有value才拼接
                suffixUrl = suffixUrl + key + "=" + encodeURIComponent(obj[key]) + "&";
            }
        }

    }
    return url + suffixUrl;
}
function isEmpty(param) {
    if (param == null || $.trim(param+"") == "") {
        return true;
    }
    return false;
}

/**
 *
 * @param json json对象
 * @param key a.b.c
 */
function easyGetJsonVal(json,key) {
    if (json == null) {
        return "";
    }
    if (!(json instanceof Object)) {
        return "";
    }
    if (key.indexOf(".") < 0) {
        return json[key] == null ? "" : json[key];
    }
    var head = key.substr(0, key.indexOf("."));
    var tail = key.substr(key.indexOf(".") + 1);
    return easyGetJsonVal(json[head], tail);
}
/**
 *
 * @param value 数字
 * @param num 小数最多为多少位
 * @returns {boolean} 是否是数字并且小数不能超过多少为
 */
function isNumber(value, num) {
    if (isEmpty(value)) {
        return false;
    }
    if (isNaN(value)) {
        return false;
    }
    if (value.indexOf(".") >= 0) {
        if (value.substr(value.indexOf(".")+1).length > num) {
            return false;
        }
    }
    return true;
}
function ajaxPost(url,data,callback){
    $.ajax({
        url:url,
        type:"post",
        data:data,
        success: callback
    })
}
function ajaxGet(url,data,callback){
    $.ajax({
        url:url,
        type:"get",
        data:data,
        success: callback
    })
}
function throttle(action, delay) {
    var statTime = 0;
    return function() {
        var currTime = +new Date();
        // console.log(currTime)
        if (currTime - statTime > delay) {
            action.apply(this, arguments);
            statTime = currTime;
        }
    }
}
/** * 对Date的扩展，将 Date 转化为指定格式的String * 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q)
 可以用 1-2 个占位符 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) * eg: * (new
 Date()).pattern("yyyy-MM-dd hh:mm:ss.S")==> 2006-07-02 08:09:04.423
 * (new Date()).pattern("yyyy-MM-dd E HH:mm:ss") ==> 2009-03-10 二 20:09:04
 * (new Date()).pattern("yyyy-MM-dd EE hh:mm:ss") ==> 2009-03-10 周二 08:09:04
 * (new Date()).pattern("yyyy-MM-dd EEE hh:mm:ss") ==> 2009-03-10 星期二 08:09:04
 * (new Date()).pattern("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
 */
Date.prototype.format=function(fmt) {
    var o = {
        "M+" : this.getMonth()+1, //月份
        "d+" : this.getDate(), //日
        "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时
        "H+" : this.getHours(), //小时
        "m+" : this.getMinutes(), //分
        "s+" : this.getSeconds(), //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S" : this.getMilliseconds() //毫秒
    };
    var week = {
        "0" : "/u65e5",
        "1" : "/u4e00",
        "2" : "/u4e8c",
        "3" : "/u4e09",
        "4" : "/u56db",
        "5" : "/u4e94",
        "6" : "/u516d"
    };
    if(/(y+)/.test(fmt)){
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    if(/(E+)/.test(fmt)){
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);
    }
    for(var k in o){
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}
/**
 * table上的搜索重置
 */
// $('.conditions-box').on('click','button',function () {
//     alert()
//     $(this).parents('.conditions-box').find('input,select').val('')
// });
/**
 * 下拉菜单
 */
$.fn.dropMenuInit = function(){
    $(this).css({
        'position': 'relative'
    });
    $(this).find('ul').css({
        'display': 'none',
        'position': 'absolute',
        'z-index': 99,
        'border': '1px solid #EEEEEE',
        'border-top': 0,
        'border-bottom': 0,
        'border-radius': '4px',
        "background":"white"
    });
    $(this).find('li').css({
        'border-bottom': '1px solid #EEEEEE',
        'cursor': 'pointer',
        'text-indent': '14px',
        'text-align': 'left',
        'color':"#333"

    });
    $(this).find('li').hover(function(){
        $(this).css("background","#f2f2f2");
        $(this).css("color","#66afe9");
    }, function(){
        $(this).css("background","white");
        $(this).css("color","#333");
    });
    $(this).mouseover(function(){
        var ul = $(this).find('ul');
        var btn = ul.prev();
        var width = btn.innerWidth();
        var height = btn.innerHeight()+5;
        ul.css({
            'width':width+'px',
            "display":"block"
        });
        ul.find('li').css({
            'height': height+'px',
            'line-height':height+'px'
        });
        ul.show();
    });
    $(this).mouseout(function(){
        $(this).find('ul').hide();
    })
};
Vue.component('select-list', {//标签名叫做select-list
    props: ['queryUrl','selectValue','selectModel',"selectId"],
    data: function () { //初始参数
        return {
            myResult: this.selectModel
        }
    },
    computed: {//计算属性
        dataList:function() {//codeList代表下拉框的选项，配合后端的字典配置使用
            var data; //用来接收请求结果
            $.ajax({
                type: "GET",
                url: config.baseUrl +this.queryUrl,
                async:false,
                success: function(r){
                    data=r;
                }
            });
            if(data.code === 1){
                this.selectModel="";
                return data.data.result;
            }else{
                console.log(r.msg);
                return "";
            }
        }
    },
    methods:{//方法，主要用作回调，因为下拉框可以手动选择，所以通过change方法监听，如果值变化了就调用下边的open方法，给父组件传过去选择的值，也就是selectModel
        open:function(villageId){//子类给父类传值，我在父类里边没有取这边传过去的值，而是直接去主动获取的，这边主要起一个监听作用
            if(villageId=="reset"){
                this.selectModel="";
            }
            this.$emit('func', this.selectModel)
        }
    },//模板
    template: '<select v-model="selectModel" v-bind:id="selectId" class="form-control" @change="open()" style="font-size:13px"  >'+
        '<option value="" selected >{{selectValue}}</option>'+
        '<option v-for="item in dataList" v-bind:value="item.id">{{item.name}}</option>'+
        '</select>'
});

//弹窗详情基本样式模板
Vue.component('detail-normal',{
    //dataObj数据键值对 num一行展示几对 1/3 默认2
    props: ['dataList','num'],
    data: function () {
        return {
            detailOne: 'vue-detail-box-one',
            detailThree: 'vue-detail-box-three'
        }
    },
    mounted: function(){

    },
    template:
        '<div class="vue-detail-box" :class="{vuedetailboxone: num===1,vuedetailboxthree: num===3}">\n' +
        '    <div v-for="(item,i) in dataList">\n' +
        '        <div>{{item[0]}}</div>\n' +
        '        <div :title="item[1]">{{item[1]}}</div>\n' +
        '    </div>\n' +
        '</div>',

});
Vue.component('select-code', {
    props: ['codeType','selectValue','selectModel',"selectId"],
    data: function () {
        return {
            code: {
                mark:this.codeType,
            }
        }
    },
    watch: {//这边主要是监听父组件是否刷新，如果父组件刷新，那么就把值同步到子组件
        selectModel:function(val) {
            if(!this.selectModel){
                this.open();
            }
            this.mark = val;//②监听外部对props属性result的变更，并同步到组件内的data属性mark中
        }
    },
    computed: {
        codeList:function() {
            var data;
            $.ajax({
                type: "GET",
                url: config.baseUrl+"sys/code/selectByMark?mark="+this.codeType,
                async:false,
                success: function(r){
                    data=r;
                }
            });
            if(('code' in data) && data.code === 1){
                return data.data;
            }else{
                console.log(r.msg);
            }
        }
    },
    methods:{
        open:function(){//子类给父类传值，我在父类里边没有取这边传过去的值，而是直接去主动获取的，这边主要起一个监听作用
            this.$emit('func', this.selectModel)
        }
    },
    template: '<select v-model="selectModel" v-bind:id="selectId" class="form-control" @change="open()" style="font-size:13px"  >'+
        '<option value="">{{selectValue}}</option>'+
        '<option v-for="item in codeList" v-bind:value="item.value">{{item.name}}</option>'+
        '</select>'

});
var codeMap = new Map(); //定义一个map数组，用来缓存不同类型对应的字典值
function codeFormat (cellvalue, codeType)  //二个默认的参数
{
    var codeList = new Map();
    codeList = codeMap.get(codeType);
    if (!codeList) {
        codeList = new Map();
        $.ajax({
            type: "GET",
            url: config.baseUrl + "sys/code/selectByMark?mark=" + codeType,
            async: false,
            success: function (r) {
                if (r.code === 1) {
                    var arr = new Array();
                    arr = r.data;
                    for (var i = 0; i < arr.length; i++) {
                        codeList.set(arr[i].value+'', arr[i].name);
                    }
                    codeMap.set(codeType, codeList)
                } else {
                    console.log(r.msg);
                }
            }
        });
    }
    //渲染列值
    if(cellvalue==null){
        return '';
    }
    return codeList.get(cellvalue+'');
}

//上传文件（图片）组件
Vue.component('upload-public',{
    //domId上传图片触发的dom id(唯一); uploadUrl: 上传地址; showUrls: 重置图片列表[](回显与清空)
    props: ['domId','uploadUrl','showUrls','num'],
    data: function () {
        return {
            upload: layui.upload,
            // picList: [],
            uploadInstance: null
        }
    },
    computed: {
        picList:{
            get: function () {
                return this.showUrls || []
            },
            set: function () {
                //用于回显 或清空（由父级自由控制）
                return this.showUrls || []
            }
        }
    },
    created: function(){

    },
    mounted: function(){

        this.uploadFiles();
    },
    methods: {
        uploadFiles: function () {
            //上传图片
            var _this = this;

            this.uploadInstance = layui.upload.render({
                elem: '#'+_this.domId //绑定元素
                ,url: _this.uploadUrl //上传接口
                ,auto: false
                ,choose: function () {
                    if(_this.num>1&&_this.picList.length){
                        if(_this.picList.length>=_this.num){
                            layer.msg('限制上传数量为'+_this.num+'请删除后重新上传');
                            return
                        }
                    }else if(_this.num == 1){
                        _this.picList.splice(0,_this.picList.length);
                    }
                    _this.uploadInstance.upload();
                }
                ,done: function(res){
                    //上传完毕回调
                    if(res.code === 1){
                        layer.msg("图片上传成功");
                        _this.picList.push(res.data);
                        _this.returnTheData();
                    }else{
                        layer.msg(res.message);
                    }
                }
                ,error: function(){
                    //请求异常回调
                    layer.msg("图片上传失败");
                }
            });
        },
        deleteFiles: function (index) {
            var _this = this;
            layer.confirm('确认删除该文件？', function(i){
                _this.picList.splice(index,1);
                _this.returnTheData();
                layer.close(i);
            });
        },
        returnTheData: function () {
            //返回给父组件数据和layui上传组件实例
            //一张图就一个字符串，多个返回数组
            this.$emit('fn',this.picList,this.uploadInstance);
        }
    },
    template:
        '<div class="upload-box">\n' +
        '     <div>\n' +
        '          <button type="button" class="layui-btn layui-btn-normal" :id="domId">\n' +
        '                            <i class="layui-icon">&#xe67c;</i>上传图片\n' +
        '          </button>' +
        '     </div>\n'+
        '     <div class="upload-pic-box">\n' +
        '           <div v-for="(item,i) in picList">\n' +
        '                 <img :src="item" />\n' +
        '                 <span @click="deleteFiles(i)"></span>\n' +
        '           </div>\n' +
        '     </div>\n' +
        '</div>'
});
