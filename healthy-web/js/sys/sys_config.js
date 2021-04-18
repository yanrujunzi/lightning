var table;
$(function () {
    layui.use('table', function () {
        table = layui.table;
        //第一个实例
        table.render({
            url:  config.baseUrl + 'sysConfig/list',
            cols: [[ //表头
                {type: "checkbox", fixed: 'left'},
                    {field: 'configKey', title: 'key值'},
                    {field: 'configValue', title: 'value值'},
                    {field: 'remark', title: '备注'},
                    {field: 'creator', title: '配置人'},
                    {field: 'createTime', title: '配置时间'}
            ]]
        });
    });
});
var vm = new Vue({
    el: '#container',
    data: {
        q: {},
        showList: true,
        title: null,
        sysConfig: {}
    },
    methods: {
    	validate:function(){
    		var configKey = vm.sysConfig.configKey;
        	if(configKey == null || configKey.length==0){
        		layer.tips('Key值不能为空', '#configKey', {
                    tips: [2, 'red'],
                    time: 2000
                });
                return false;
        	}
            
        	var configValue = vm.sysConfig.configValue;
        	
        	if(configValue == null || configValue.length==0){
        		layer.tips('Value值不能为空', '#configValue', {
                    tips: [2, 'red'],
                    time: 2000
                });
                return false;
        	}
        	
        	return true;
    	},
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.sysConfig = {};
            
            //新增
            layer.open({
                type: 1,
                //多tab类型切换额
                title: ['新增'],
                content: $('#modeopen'),
                area: ['80%','80%'],
                btn: ['确定', '取消'],
                yes: function(index, layero){
                	var isValidate = vm.validate();
                    if(!isValidate) return;
                    vm.saveOrUpdate();
                    layer.close(index);
                    $('#modeopen').hide();
                }
                ,btn2: function(index, layero){
                    $('#modeopen').hide();
                }
                ,cancel: function(){
                    $('#modeopen').hide();
                }
        	});
        },
        update: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id);
             //修改
            layer.open({
                type: 1,
                //多tab类型切换额
                title: ['修改'],
                content: $('#modeopen'),
                area: ['80%','80%'],
                btn: ['确定', '取消'],
                yes: function(index, layero){
                	var isValidate = vm.validate();
                    if(!isValidate) return;
                    vm.saveOrUpdate();
                    layer.close(index);
                    $('#modeopen').hide();
                }
                ,btn2: function(index, layero){
                    $('#modeopen').hide();
                }
                ,cancel: function(){
                    $('#modeopen').hide();
                }
       	 	});
        },
        saveOrUpdate: function (event) {
            var url = vm.sysConfig.id == null ? "sysConfig/save" : "sysConfig/update";
            $.ajax({
                type: "POST",
                url: config.baseUrl + url,
                data: JSON.stringify(vm.sysConfig),
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
        },
        del: function (event) {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }
            layer.confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: config.baseUrl + "sysConfig/delete",
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
            $.get(config.baseUrl + "sysConfig/info/" + id, function (r) {
                if (r.code == 1) {
                    vm.sysConfig = r.data;
                } else {
                    layer.alert(r.message);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            table.reload('tableData', {
                where: vm.q,
                page: {
                    curr: 1 //重新从第 1 页开始
                }
            });

        }
    }
});
