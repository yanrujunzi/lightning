var table;
$(function () {
    layui.use('table', function () {
        table = layui.table;
        //第一个实例
        table.render({
            url: config.baseUrl + 'sysUser/list',  //数据接口
            cols: [[ //表头
                {type: "checkbox", fixed: 'left'},
                {field: 'account', title: '账号'},
                {field: 'username', title: '用户姓名'},
                {field: 'mobile', title: '手机号'},
                {field: 'email', title: '邮箱'},
                {field: 'status', title: '状态',
                    templet:function (item) {
                        if (item.status==1){
                            return "<span class='status-normal'>启用</span>"
                        }else {
                            return "<span class='status-danger'>禁用</span>"
                        }
                }},
                {field: 'editTime', title: '修改时间'},
                {field: 'createTime', title: '创建时间'}
            ]]
        });
        table.on('sort(sortTable)', function (obj) {
            table.reload('tableData', {
                initSort: obj,
                where: {
                    orderName: toLineName(obj.field),
                    order: obj.type
                }
            });
        });
    });
});
var vm = new Vue({
    el: '#container',
    data: {
        q: {},
        showList: true,
        title: null,
        sysUser: {},
        allRoles:[],
        //用户拥有的id
        userRoleIds:[],
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
                chkStyle: "radio",
                radioType: "all"   //对所有节点设置单选
            }
        }
    },
    mounted:function(){
        //table
        this.tableHandle();
        //table 高度适应resize
        this.winResize();
        //日期选择器
        this.dateRander();
        //文件上传
        this.uploadRander();
    },
    methods: {
    	reset: function(){
            const _this = this;
            _this.q={
            	account: ''
            }
            vm.reload();
        },
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.sysUser = {"status":1};
            vm.userRoleIds = [];
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
            vm.showList = false;
            vm.userRoleIds = [];
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
            var url = vm.sysUser.id == null ? "sysUser/save" : "sysUser/update";
            var userRoleList = [];
            if (vm.userRoleIds) {
                for (var i = 0;i<vm.userRoleIds.length;i++){
                    userRoleList.push({id: vm.userRoleIds[i]});
                }
            }
            vm.sysUser.userRoleList = userRoleList;

            if(formValidation.checkout('requirer',{
                dom: '#account',
                val: vm.sysUser.account,
                msg: '账号不能为空'
            })){
                return false;
            }

            if(formValidation.checkout('requirer',{
                dom: '#username',
                val: vm.sysUser.username,
                msg: '用户名不能为空'
            })){
                return false;
            }

            if(formValidation.checkout('phone',{
                dom: '#mobile',
                val: vm.sysUser.mobile,
                msg: '输入正确的手机号'
            })){
                return false;
            }

            if(formValidation.checkout('email',{
                dom: '#email',
                val: vm.sysUser.email,
                msg: '输入正确的邮箱'
            })){
                return false;
            }

            var roleIsNotNull = vm.userRoleIds.length > 0 ? true : null;
            if(formValidation.checkout('requirer',{
                dom: '#userRoleIds',
                val: roleIsNotNull,
                msg: '角色选择不能为空'
            })){
                return false;
            }

            $.ajax({
                type: "POST",
                url: config.baseUrl + url,
                data: JSON.stringify(vm.sysUser),
                success: function (r) {
                    if (r.code === 1) {
                        layer.alert("操作成功", function (index) {
                            layer.close(index);
                            vm.q = {};
                            vm.q.account = "";
                            vm.reload();
                        });
                    } else {
                        layer.alert(r.message);
                    }
                }
            });
            return true;
        },
        del: function (event) {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }
            layer.confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: config.baseUrl + "sysUser/delete",
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
        resetPwd: function () {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }
            layer.confirm('确定要重置选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: config.baseUrl + "sysUser/resetPwd",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code == 1) {
                            layer.alert("重置成功", function (index) {
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
            $.get(config.baseUrl + "sysUser/info/" + id, function (r) {
                if (r.code == 1) {
                    vm.sysUser = r.data;
                    if (vm.sysUser.userRoleList) {
                        for (var i = 0; i < vm.sysUser.userRoleList.length; i++) {
                            vm.userRoleIds.push(vm.sysUser.userRoleList[i].id);
                        }
                    }
                    var nodes = zTreeObj.getNodes();
                    if (nodes && nodes.length > 0) {
                        var allNodes = zTreeObj.transformToArray(nodes);
                        for (var i = 0; i < allNodes.length; i++) {
                            if (allNodes[i].id == r.data.groupId) {
                                allNodes[i].checked = true;
                            }else {
                                allNodes[i].checked = false;
                            }
                            zTreeObj.updateNode(allNodes[i]);
                        }
                    }
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
        },
        reset: function () {
            this.q.account = null;
        }
    },
    mounted:function () {
        $.ajax({
            url:config.baseUrl+"sysRole/queryAllRoles",
            success:function (r) {
                if (r.code == 1) {
                    vm.allRoles = r.data;
                }
            }
        });
    }
});
