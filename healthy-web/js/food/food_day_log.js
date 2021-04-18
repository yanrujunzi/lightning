var table;
var nowDateStr = new Date().format("yyyy-MM-dd");
$(function () {
    //config.js已经设置默认参数
    layui.use('table', function () {
        table = layui.table;
        //第一个实例
        table.render({
            url:  config.baseUrl + 'foodDayLog/list',
            cols: [[ //表头
                {type: "checkbox", fixed: 'left'},
                    {field: 'idFoodLog', title: 'ID',hide:true},
                    {field: 'bodyName', title: '人员姓名'},
                    {field: 'foodName', title: '食物名称'},
                    {field: 'weight', title: '食用重量(克)'},
                    {field: 'calorie', title: '总热量(卡路里)'},
                    {field: 'grubTime', title: '进食日期'},
                    {field: 'dateCreate', title: '创建时间'},
            ]]
        });
    });
});
var vm = new Vue({
    el: '#container',
    data: {
        q: {
            bodyName:"",
            dateRecord:nowDateStr
        },
        showList: true,
        title: null,
        foodDayLog: {},
        index:null,
    },
    methods: {
        query: function () {
            vm.reload();
        },
        reset: function () {
            vm.q.bodyName="";
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
                    vm.foodDayLog = {};
                }
                ,cancel: function(){
                    //右上角关闭回调
                    $('#modeopen').hide();
                    vm.foodDayLog = {};
                }
            });
        },
        update: function (event) {
            var id = getSelectedRowObj().idFoodLog;
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
                    vm.foodDayLog = {};
                }
                ,cancel: function(){
                    //右上角关闭回调
                    $('#modeopen').hide();
                    vm.foodDayLog = {};
                }
            });
        },
        saveOrUpdate: function (event) {
            if(formValidation.checkout('requirer',{
                dom: '#foodName',
                val: vm.foodDayLog.foodName,
                msg: '食物名称不能为空'
            })){
                return
            }
            if(formValidation.checkout('requirer',{
                dom: '#bodyName',
                val: vm.foodDayLog.bodyName,
                msg: '人员名称不能为空'
            })){
                return
            }
            if(formValidation.checkout('requirer',{
                dom: '#weight',
                val: vm.foodDayLog.weight,
                msg: '食用重量不能为空'
            })){
                return
            }
            var url = vm.foodDayLog.idFoodLog == null ? "foodDayLog/save" : "foodDayLog/update";
            //计算热量
            vm.foodDayLog.calorie=vm.foodDayLog.weight*vm.foodDayLog.calorie;
            vm.foodDayLog.calorie=vm.foodDayLog.calorie.toFixed(2);
            $.ajax({
                type: "POST",
                url: config.baseUrl + url,
                data: JSON.stringify(vm.foodDayLog),
                success: function (r) {
                    if (r.code === 1) {
                        layer.alert("操作成功", function (index) {
                            layer.close(index);
                            vm.reload();
                            $('#modeopen').hide();
                            layer.close(vm.index);
                            vm.foodDayLog = {};
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
                ids.push(dataInfo.data[i].idFoodLog);
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
                    url: config.baseUrl + "foodDayLog/delete",
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
            $.get(config.baseUrl + "foodDayLog/info/" + id, function (r) {
                if (r.code == 1) {
                    vm.foodDayLog = r.data;
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
                    vm.foodDayLog.idBody=rowData.idBody;
                    vm.foodDayLog.bodyName=rowData.name;
                    $("#bodyName").val(rowData.name);
                    layer.close(index);
                }
            });
        },
        selectFood: function (event) {
            var contenturl= config.webUrl+"views/food/food_manage.html";
            layer.open({
                type: 2,
                title: "选择食物",
                area: ['98%', '90%'],
                shadeClose: false,
                content: contenturl,
                btn: ['选择','取消'],
                btn1: function (index) {
                    var rowData = window["layui-layer-iframe" + index].callbackData();
                    vm.foodDayLog.idFood=rowData.idFood;
                    vm.foodDayLog.foodName=rowData.name;
                    vm.foodDayLog.calorie=rowData.calorie;
                    $("#foodName").val(rowData.name);
                    layer.close(index);
                }
            });
        }
    },mounted:function () {
        var $this = this;
        this.$nextTick(() => {
            // 加上延时避免 mounted 方法比页面加载早执行
            setTimeout(() => {
                var $this=this;
                $this.reload();
            }, 100)
        });
        layui.use('laydate', function () {
            layui.laydate.render({
                elem: '#qGrubTime'
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
        layui.use('laydate', function () {
            layui.laydate.render({
                elem: '#grubTime'
                , type: 'date',
                done:function (value, date) {
                    if (value) {
                        $this.foodDayLog.grubTime = value;
                    } else {
                        $this.foodDayLog.grubTime = "";
                    }
                }
            });
        })
    }
});