var nowDateStr = new Date().format("yyyy-MM-dd");
var sevenDaysAgoStr = new Date(new Date().getTime() - 7 * 24 * 60 * 60 * 1000).format("yyyy-MM-dd");
var table,zTreeObj;
$(function () {
    layui.use('table', function () {
        table = layui.table;
        //第一个实例
        table.render({
            url:  config.baseUrl + 'sysRole/list',
            cols: [[ //表头
                {type: "checkbox", fixed: 'left'},
                    {field: 'roleCnName', title: '角色中文名'},
                    {field: 'roleName', title: '角色英文名'},
                    {field: 'status', title: '状态',templet:function (item) {
                             if (item.status==1){
                                return "<span class='status-normal'>启用</span>"
                            }else {
                                return "<span class='status-danger'>禁用</span>"
                            }
                    }},
                    {field: 'createTime', title: '创建时间'},
                    {field: 'updateTime', title: '修改时间'},
            ]]
        });
        table.on('rowDouble(sortTable)', function (obj) {
            var data = obj.data;
            layer.open({
                type: 2,
                area: ['80%', '80%'],
                fix: false, //不固定
                maxmin: true,
                shadeClose: true,
                shade: 0.4,
                title: "角色详情",
                btn: ['关闭'],
                content: config.webUrl + "views/sys/sys_role_detail.html?id=" + data.id
            });
        })
    });
});
var canUsefulMenus = [];
var vm = new Vue({
    el: '#container',
    data: {
        q: {
            roleName: '',
            status: "",
            startTime: null,
            endTime: null
        },
        showList: true,
        title: null,
        sysRole: {},
        settings:{
            data: {
                simpleData: {//简单数据模式
                    enable: true,
                    idKey: "id",
                    pIdKey: "parentId",
                    rootPId: 0
                }
            },
            check: {
                enable: true,
                chkStyle: "checkbox",
                chkboxType: { "Y": "ps", "N": "s" }
            },
            callback:{
                onClick:function (e) {
                    e.preventDefault();
                }
            }
        }
    },
    methods: {
    	reset: function(){
            const _this = this;
            _this.q={
            	roleName: '',
                status: "",
                startTime: null,
                endTime: null
            }
            vm.reload();
        },
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.sysRole = {"status":1,roleMenuList:canUsefulMenus};
            zTreeObj = $.fn.zTree.init($("#zTree"), vm.settings,canUsefulMenus);
            zTreeObj.expandAll(true);
            //新增
            layer.open({
                type: 1,
                //多tab类型切换额
                title: ['新增'],
                content: $('#modeopen'),
                area: ['80%','80%'],
                btn: ['确定', '取消'],
                yes: function(index, layero){
                   var bool = vm.saveOrUpdate();
                    if (bool) {
                        layer.close(index);
                    }
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

            vm.getInfo(id)
             //新增
            layer.open({
                type: 1,
                //多tab类型切换额
                title: ['修改'],
                content: $('#modeopen'),
                area: ['80%','80%'],
                btn: ['确定', '取消'],
                yes: function(index, layero){
                   vm.saveOrUpdate();
                   layer.close(index);
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
            var url = vm.sysRole.id == null ? "sysRole/save" : "sysRole/update";
            var ids = [];
            this.getAllSelectNodeIds(zTreeObj.getNodes(),ids);

            var roleMenus = [];
            if (ids && ids.length > 0) {
                for (var i = 0; i < ids.length; i++) {
                    roleMenus[i] = {id: ids[i]}
                }
            }
            vm.sysRole.roleMenuList = roleMenus;

            if(formValidation.checkout('requirer',{
                dom: '#roleCnNameInput',
                val: vm.sysRole.roleCnName,
                msg: '角色中文名不能为空'
            })){
                return false
            }

            if(formValidation.checkout('chinese',{
                dom: '#roleCnNameInput',
                val: vm.sysRole.roleCnName,
                msg: '角色中文名仅能由汉字组成'
            })){
                return false
            }

            if(formValidation.checkout('requirer',{
                dom: '#roleNameInput',
                val: vm.sysRole.roleName,
                msg: '角色英文名不能为空'
            })){
                return false
            }

            if(formValidation.checkout('character',{
                dom: '#roleNameInput',
                val: vm.sysRole.roleName,
                msg: '角色英文名仅能由数字、字母及下划线组成'
            })){
                return false
            }

            $.ajax({
                type: "POST",
                url: config.baseUrl + url,
                data: JSON.stringify(vm.sysRole),
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
                    url: config.baseUrl + "sysRole/delete",
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
            $.get(config.baseUrl + "sysRole/info/" + id, function (r) {
                if (r.code == 1) {
                    vm.sysRole= r.data;

                    zTreeObj = $.fn.zTree.init($("#zTree"), vm.settings, r.data.roleMenuList);
                    zTreeObj.expandAll(true);

                } else {
                    layer.alert(r.message);
                }
            });
        },
        givePermissions: function (id) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            var postData = {};
            postData.roleId = id;

        },
        reload: function () {
            vm.showList = true;
            table.reload('tableData', {
                where: vm.q,
                page: {
                    curr: 1 //重新从第 1 页开始
                }
            });

        },
        getAllSelectNodeIds:function (nodes,ids) {
            if (!nodes) {
                return;
            }
            if (nodes && nodes.length>0){
                for (var i = 0; i < nodes.length; i++) {
                    if (nodes[i].checked) {
                        ids.push(nodes[i].id)
                    }
                    if (nodes[i].children && nodes[i].children.length > 0) {
                        this.getAllSelectNodeIds(nodes[i].children,ids);
                    }

                }
            }
        }
    },
    mounted:function () {
        $.ajax({
            url: config.baseUrl + "sysMenu/queryCanUsefulMenus",
            success:function (r) {
                if (r.code == 1) {
                    canUsefulMenus =  r.data;
                    zTreeObj = $.fn.zTree.init($("#zTree"), vm.settings,canUsefulMenus);
                    zTreeObj.expandAll(true);
                }else{
                    layer.alert(r.message);
                }
            }
        })
        var $this = this;
        layui.use('laydate', function () {
            layui.laydate.render({
                elem: '#startTime'
                , max: nowDateStr
                , type: 'date',
                done:function (value, date) {
                    if (value) {
                        $this.q.startTime = value;
                    } else {
                        $this.q.startTime = "";
                    }
                }
            });
            layui.laydate.render({
                elem: '#endTime'
                , max: nowDateStr
                , type: 'date'
                , done: function (value, date) {
                    if (value) {
                        $this.q.endTime = value;
                    }else{
                        $this.q.endTime = "";
                    }
                }
            });
        })
    }
});
