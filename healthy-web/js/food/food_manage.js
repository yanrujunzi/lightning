var table;
$(function () {
    //config.js已经设置默认参数
    layui.use('table', function () {
        table = layui.table;
        //第一个实例
        table.render({
            url:  config.baseUrl + 'foodManage/list',
            cols: [[ //表头
                {type: "checkbox", fixed: 'left'},
                    {field: 'idFood', title: 'ID',hide:true},
                    {field: 'name', title: '食物名称'},
                    {field: 'calorie', title: '卡路里(每克/KJ)'},
                    {field: 'dateCreate', title: '创建时间'}
            ]]
        });
    });
});
var vm = new Vue({
    el: '#container',
    data: {
        q: {
        },
        showList: true,
        title: null,
        foodManage: {},
        index:null,
    },
    mounted: function () {
        this.$nextTick(() => {
            // 加上延时避免 mounted 方法比页面加载早执行
            setTimeout(() => {
                var $this=this;
                $this.reload();
            }, 100)
        });

    },
    methods: {
        query: function () {
            vm.reload();
        },
        reset: function () {
            vm.q.name="";
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
                    vm.foodManage = {};
                }
                ,cancel: function(){
                    //右上角关闭回调
                    $('#modeopen').hide();
                    vm.foodManage = {};
                }
            });
        },
        update: function (event) {
            var id = getSelectedRowObj().idFood;
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
                    vm.foodManage = {};
                }
                ,cancel: function(){
                    //右上角关闭回调
                    $('#modeopen').hide();
                    vm.foodManage = {};
                }
            });
        },
        saveOrUpdate: function (event) {
            if(formValidation.checkout('requirer',{
                dom: '#foodManageName',
                val: vm.foodManage.name,
                msg: '食物名称不能为空'
            })){
                return
            }
            var url = vm.foodManage.idFood == null ? "foodManage/save" : "foodManage/update";
            $.ajax({
                type: "POST",
                url: config.baseUrl + url,
                data: JSON.stringify(vm.foodManage),
                success: function (r) {
                    if (r.code === 1) {
                        layer.alert("操作成功", function (index) {
                            layer.close(index);
                            vm.reload();
                            $('#modeopen').hide();
                            layer.close(vm.index);
                            vm.foodManage = {};
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
                ids.push(dataInfo.data[i].idFood);
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
                    url: config.baseUrl + "foodManage/delete",
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
            $.get(config.baseUrl + "foodManage/info/" + id, function (r) {
                if (r.code == 1) {
                    vm.foodManage = r.data;
                } else {
                    layer.alert(r.message);
                }
            });
        },
        reload: function () {
            /*vm.showList = true;*/
            table.reload('tableData', {
                where: vm.q,
                page: {
                    curr: 1 //重新从第 1 页开始
                }
            });
        }
    }
});
function callbackData() {
    var dataInfo = table.checkStatus("tableData");
    var ids = [];
    for (var i = 0; i < dataInfo.data.length; i++) {
        ids.push(dataInfo.data[i].idFood);
    }
    if (ids.length <= 0) {
        layer.alert("至少选择一条数据");
        return null;
    }
    if (ids == null) {
        return;
    }
    var id=ids[0];
    for(var i=0;i<table.cache.tableData.length;i++){
        if(table.cache.tableData[i].idFood==id){
            return table.cache.tableData[i];
        }
    }
}