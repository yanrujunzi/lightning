var table;
$(function () {
    //config.js已经设置默认参数
    layui.use('table', function () {
        table = layui.table;
        //第一个实例
        table.render({
            url:  config.baseUrl + 'sysModule/list',
            cols: [[ //表头
                {type: "checkbox", fixed: 'left'},
                    {field: 'name', title: '模块名称'},
                    {field: 'code', title: '模块编码'},
                    {field: 'imageUrl', title: '模块图片路径'},
                    {field: 'remark', title: '备注'}
            ]]
        });
    });
});
var vm = new Vue({
    el: '#container',
    data: {
        q: {},
        title: null,
        sysModule: {},
        moduleId:null,

        // 可以选择关联的设备种类
        canSelectDeviceCategories: [],

        // 已经关联的设备种类
        moduleDeviceCategoryIds: [],

        // 可以选择的0级菜单
        canSelectMenus: [],

        // 已经关联的0级菜单
        moduleMenuIds: []
    },
    methods: {
        saveRelevance: function () {
            var data = {
                moduleId: vm.moduleId,
                moduleDeviceCategoryIds:vm.moduleDeviceCategoryIds,
                moduleMenuIds:vm.moduleMenuIds
            }
            $.ajax({
                type:"POST",
                url: config.baseUrl + "sysModule/updateModuleRelevance",
                data: JSON.stringify(data),
                success:function (r) {
                    if (r.code === 1) {
                        layer.closeAll()
                        layer.alert("操作成功")
                    } else {
                        layer.alert(r.message)
                    }
                }
            })
        },
        relevance: function(){
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            vm.moduleId = id;

            $.ajax({
                type:"GET",
                url: config.baseUrl + "sysModule/moduleCategoriesAndMenus?id=" + id,
                success:function (r) {
                    if (r.code === 1) {
                        vm.canSelectDeviceCategories = r.data.canSelectDeviceCategories;
                        vm.canSelectMenus = r.data.canSelectMenus;
                        vm.moduleDeviceCategoryIds = r.data.moduleDeviceCategoryIds;
                        vm.moduleMenuIds = r.data.moduleMenuIds;
                    } else {
                        alert(r.message)
                        return ;
                    }
                }
            })

            var index = layer.open({
                type: 1,
                area: ['90%', '600px'],
                fix: false,
                maxmin: true,
                shadeClose: true,
                shade:0.4,
                title: "模块关联",
                content: $("#relevanceLayer"),
                end:function () {
                    $("#relevanceLayer").css("display","none");
                    vm.canSelectDeviceCategories = []
                    vm.moduleDeviceCategoryIds = []
                    vm.canSelectMenus = []
                    vm.moduleMenuIds = []
                }
            });

        },
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.title = "新增";
            vm.sysModule = {};

            //新增
            layer.open({
                type: 1,
                //多tab类型切换额
                title: ['新增'],
                content: $('#modeopen'),
                area: ['90%','98%'],
                btn: ['确定', '取消'],
                yes: function(index, layero){
                    var bool = vm.saveOrUpdate();
                    if (bool) {
                        layer.close(index);
                    }
                }
                ,end: function () {
                    $('#modeopen').hide();
                }
            });
        },
        update: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            vm.title = "修改";

            vm.getInfo(id)

            layer.open({
                type: 1,
                //多tab类型切换额
                title: ['修改'],
                content: $('#modeopen'),
                area: ['80%','80%'],
                btn: ['保存', '取消'],
                yes: function(index, layero){
                    var bool =  vm.saveOrUpdate();
                    if (bool) {
                        layer.close(index);
                    }
                }
                ,end: function () {
                    $('#modeopen').hide();
                }
            });
        },
        saveOrUpdate: function (event) {
            if(formValidation.checkout('requirer',{
                dom: '#nameInput',
                val: vm.sysModule.name,
                msg: '模块名称必填'
            })){
                return false
            }
            if(formValidation.checkout('requirer',{
                dom: '#codeInput',
                val: vm.sysModule.code,
                msg: '模块编码必填'
            })){
                return false
            }
            var url = vm.sysModule.id == null ? "sysModule/save" : "sysModule/update";
            $.ajax({
                type: "POST",
                url: config.baseUrl + url,
                data: JSON.stringify(vm.sysModule),
                success: function (r) {
                    if (r.code === 1) {
                        layer.alert("操作成功", function (index) {
                            layer.close(index);
                            vm.reload();
                        });
                    } else {
                        layer.alert(r.message);
                    }
                }
            });
            return true
        },
        del: function (event) {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }
            layer.confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: config.baseUrl + "sysModule/delete",
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
            $.get(config.baseUrl + "sysModule/info/" + id, function (r) {
                if (r.code == 1) {
                    vm.sysModule = r.data;
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
        reset: function () {
            this.q.name = "";
            vm.reload();
        }
    },
    mounted:function () {

    }
});