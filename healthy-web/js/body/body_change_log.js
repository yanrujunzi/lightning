var table;
var nowDateStr = new Date().format("yyyy-MM-dd");
$(function () {
    //config.js已经设置默认参数
    layui.use('table', function () {
        table = layui.table;
        //第一个实例
        table.render({
            url:  config.baseUrl + 'bodyChangeLog/list',
            cols: [[ //表头
                {type: "checkbox", fixed: 'left'},
                {field: 'idBodyLog', title: 'ID',hide:true},
                {field: 'name', title: '人员名称'},
                {field: 'relationship', title: '与本人关系',
                    templet:function (item) {
                        if (item.relationship=='01'){
                            return "本人"
                        } if (item.relationship=='02'){
                            return "子女"
                        } if (item.relationship=='03'){
                            return "父母"
                        } if (item.relationship=='04'){
                            return "配偶"
                        } if (item.relationship=='05'){
                            return "朋友"
                        }else {
                            return "其他"
                        }
                    }},
                {field: 'height', title: '身高(cm)'},
                {field: 'weight', title: '实时体重(kg)'},
                {field: 'waterContent', title: '水分含量'},
                {field: 'bodyFat', title: '体脂比例'},
                {field: 'muscle', title: '肌肉(kg)'},
                {field: 'protein', title: '蛋白质含量'},
                {field: 'bmi', title: 'BMI指标'},
                {field: 'dateRecord', title: '记录日期'},
                {field: 'dateCreate', title: '创建时间'}
            ]]
        });
    });
});
var vm = new Vue({
    el: '#container',
    data: {
        q: {
            name:"",
            relationship:"",
            dateRecord:nowDateStr
        },
        showList: true,
        title: null,
        bodyChangeLog: {},
        index:null,
    },
    methods: {
        query: function () {
            vm.reload();
        },
        reset: function () {
            vm.q.name="";
            vm.q.relationship="";
            vm.q.dateRecord="";
            vm.reload();
        },
        add: function () {
            vm.index=layer.open({
                type: 1,
                title: ['新增'],
                content: $('#modeopen'),
                area: ['80%','80%'],
                btn: ['确定', '取消'],
                yes: function(index, layero){
                    vm.saveOrUpdate();
                }
                ,btn2: function(index, layero){
                    $('#modeopen').hide();
                    vm.bodyChangeLog = {};
                }
                ,cancel: function(){
                    //右上角关闭回调
                    $('#modeopen').hide();
                    vm.bodyChangeLog = {};
                }
            });
        },
        update: function (event) {
            var id = getSelectedRowObj().idBodyLog;
            if (id == null) {
                return;
            }
            vm.getInfo(id)
            vm.index=layer.open({
                type: 1,
                title: ['修改'],
                content: $('#modeopen'),
                area: ['80%','80%'],
                btn: ['确定', '取消'],
                yes: function(index, layero){
                    vm.saveOrUpdate();
                }
                ,btn2: function(index, layero){
                    $('#modeopen').hide();
                    vm.bodyChangeLog = {};
                }
                ,cancel: function(){
                    //右上角关闭回调
                    $('#modeopen').hide();
                    vm.bodyChangeLog = {};
                }
            });
        },
        saveOrUpdate: function (event) {
            if(formValidation.checkout('requirer',{
                dom: '#bodyChangeLogName',
                val: vm.bodyChangeLog.name,
                msg: '人员姓名不能为空'
            })){
                return
            }
            if(formValidation.checkout('requirer',{
                dom: '#weight',
                val: vm.bodyChangeLog.weight,
                msg: '实时体重不能为空'
            })){
                return
            }
            if(formValidation.checkout('requirer',{
                dom: '#dateRecord',
                val: vm.bodyChangeLog.dateRecord,
                msg: '记录时间不能为空'
            })){
                return
            }
            var url = vm.bodyChangeLog.idBodyLog == null ? "bodyChangeLog/save" : "bodyChangeLog/update";
            $.ajax({
                type: "POST",
                url: config.baseUrl + url,
                data: JSON.stringify(vm.bodyChangeLog),
                success: function (r) {
                    if (r.code === 1) {
                        layer.alert("操作成功", function (index) {
                            layer.close(index);
                            vm.reload();
                            $('#modeopen').hide();
                            layer.close(vm.index);
                            vm.bodyChangeLog = {};
                        });
                    } else {
                        layer.alert(r.message);
                    }
                }
            });
        },
        del: function (event) {
            var dataInfo = table.checkStatus("tableData");
            var ids = [];
            for (var i = 0; i < dataInfo.data.length; i++) {
                ids.push(dataInfo.data[i].idBodyLog);
            }
            if (ids.length <= 0) {
                layer.alert("至少选择一条数据");
                return null;
            }
            if (ids == null) {
                return;
            }
            layer.confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: config.baseUrl + "bodyChangeLog/delete",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code == 1) {
                            layer.alert("删除成功", function (index) {
                                layer.close(index)
                                vm.reload();
                            });
                        } else {
                            layer.alert(r.message);
                        }
                    }
                });
            });
        },
        getInfo: function (id) {
            $.get(config.baseUrl + "bodyChangeLog/info/" + id, function (r) {
                if (r.code == 1) {
                    vm.bodyChangeLog = r.data;
                } else {
                    layer.alert(r.message);
                }
            });
        },
        reload: function () {
            table.reload('tableData', {
                where: vm.q,
                page: {
                    curr: 1 //重新从第 1 页开始
                }
            });

        },
        selectBody: function (event) {
            var contenturl= config.webUrl+"views/body/body_manage.html";
            layer.open({
                type: 2,
                title: "选择人员",
                area: ['98%', '90%'],
                shadeClose: false,
                content: contenturl,
                btn: ['选择','取消'],
                btn1: function (index) {
                    var rowData = window["layui-layer-iframe" + index].callbackData();
                    vm.bodyChangeLog.idBody=rowData.idBody;
                    vm.bodyChangeLog.name=rowData.name;
                    vm.bodyChangeLog.relationship=rowData.relationship;
                    vm.bodyChangeLog.height=rowData.height;
                    $("#bodyChangeLogName").val(rowData.name);
                    $("#relationship").val(rowData.relationship);
                    $("#idBody").val(rowData.idBody);
                    $("#height").val(rowData.height);
                    layer.close(index);
                }
            });
        }
    },mounted:function () {
        var $this = this;
        this.$nextTick(() => {
            // 加上延时避免 mounted 方法比页面加载早执行
            setTimeout(() => {
                $this.reload();
            }, 100)});
        layui.use('laydate', function () {
            layui.laydate.render({
                elem: '#qDateRecord'
                , type: 'date'
                ,value:nowDateStr
                ,done:function (value, date) {
                    if (value) {
                        $this.q.dateRecord = value;
                    } else {
                        $this.q.dateRecord = "";
                    }
                }
            });
        });
        layui.laydate.render({
            elem: '#dateRecord'
            , type: 'date'
            , done: function (value, date) {
                if (value) {
                    $this.bodyChangeLog.dateRecord = value;
                }else{
                    $this.bodyChangeLog.dateRecord = "";
                }
            }
        });
    }
});