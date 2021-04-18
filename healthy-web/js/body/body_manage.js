var table;
$(function () {
    //config.js已经设置默认参数
    layui.use('table', function () {
        table = layui.table;
        //第一个实例
        table.render({
            url:  config.baseUrl + 'bodyManage/list',
            cols: [[ //表头
                {type: "checkbox", fixed: 'left'},
                    {field: 'idBody', title: 'ID',hide:true},
                    {field: 'name', title: '用户名称'},
                    {field: 'relationship', title: '人员关系',
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
                    {field: 'birthdDate', title: '出生日期'},
                    {field: 'expectedWeight', title: '期望体重(kg)'},
                    {field: 'dateCreate', title: '创建时间'},
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
        bodyManage: {},
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
                    vm.bodyManage = {};
                }
                ,cancel: function(){
                    //右上角关闭回调
                    $('#modeopen').hide();
                    vm.bodyManage = {};
                }
            });
        },
        update: function (event) {
            var id = getSelectedRowObj().idBody;
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
                    vm.bodyManage = {};
                }
                ,cancel: function(){
                    //右上角关闭回调
                    $('#modeopen').hide();
                    vm.bodyManage = {};
                }
            });
        },
        saveOrUpdate: function (event) {
            if(formValidation.checkout('requirer',{
                dom: '#bodyManageName',
                val: vm.bodyManage.name,
                msg: '用户名称不能为空'
            })){
                return
            }

            if(formValidation.checkout('requirer',{
                dom: '#relationship',
                val: vm.bodyManage.relationship,
                msg: '人员关系不能为空'
            })){
                return
            }

            if(formValidation.checkout('requirer',{
                dom: '#height',
                val: vm.bodyManage.height,
                msg: '身高不能为空'
            })){
                return
            }

            var url = vm.bodyManage.idBody == null ? "bodyManage/save" : "bodyManage/update";
            $.ajax({
                type: "POST",
                url: config.baseUrl + url,
                data: JSON.stringify(vm.bodyManage),
                success: function (r) {
                    if (r.code === 1) {
                        layer.alert("操作成功", function (index) {
                            layer.close(index);
                            vm.reload();
                            $('#modeopen').hide();
                            layer.close(vm.index);
                            vm.bodyManage = {};
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
                ids.push(dataInfo.data[i].idBody);
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
                    url: config.baseUrl + "bodyManage/delete",
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
            $.get(config.baseUrl + "bodyManage/info/" + id, function (r) {
                if (r.code == 1) {
                    vm.bodyManage = r.data;
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
    },mounted:function () {
        var $this = this;
        layui.use('laydate', function () {
            layui.laydate.render({
                elem: '#birthdDate'
                , type: 'date',
                done:function (value, date) {
                    if (value) {
                        $this.bodyManage.birthdDate = value;
                    } else {
                        $this.bodyManage.birthdDate = "";
                    }
                }
            });
        })
    }
});
function callbackData() {
    var dataInfo = table.checkStatus("tableData");
    var ids = [];
    for (var i = 0; i < dataInfo.data.length; i++) {
        ids.push(dataInfo.data[i].idBody);
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
        if(table.cache.tableData[i].idBody==id){
            return table.cache.tableData[i];
        }
    }
}