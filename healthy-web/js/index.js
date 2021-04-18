element = layui.element;
var app = new Vue({
    el: '.main',
    data: {
        title: '',
        //用户名
        account: '',
        //角色
        roleName: '',
        //获取的0级菜单
        moduleList: [],
        //被隐藏0级菜单
        moduleListMore: [],
        //菜单数据
        menuData: [],
        //登录密码
        password: "",
        confirmPassword: ""
    },
    created: function () {
        // 获取登录人
        this.getAccount();
        // 获取菜单列表
        this.getMenuList();
    },
    mounted: function(){
        //初始化左侧菜单渲染
        this.renderSystemHandle();
    },
    methods: {
        backToLogin: function () {
            //退出
            sessionStorage.clear();
            location.href = 'login.html';
        },
        editPassword: function () {
            //修改密码
            $('.dialogMenu').css('display', 'none');
            var _this = this;
            layer.open({
                type: 1,
                title: '修改密码',
                content: $('#dialogChangePassword').html(),
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    if(formValidation.checkout('requirer',{
                        dom: '#newpassword',
                        val: $('#newpassword').val(),
                        msg: '新密码不能为空'
                    })){
                        return
                    }
                    app.password=$('#newpassword').val();
                    if(formValidation.checkout('requirer',{
                        dom: '#confirmpassword',
                        val: $('#confirmpassword').val(),
                        msg: '确认密码不能为空'
                    })){
                        return
                    }
                    app.confirmPassword=$('#confirmpassword').val();
                    _this.saveOrUpdate(index);
                }
                ,btn2: function(index, layero){
                     _this.closeChangePassword();
                 }
                ,cancel: function(){
                //右上角关闭回调
                     _this.closeChangePassword();
                 }
            });
        },
        saveOrUpdate:function (index) {
            if (app.password != app.confirmPassword) {
                layer.alert("两次密码不一致");
                return;
            }
            $.ajax({
                url:config.baseUrl+"sys/updatePassword",
                type:"post",
                data:JSON.stringify({password: app.password}),
                success:function (r) {
                    if (r.code == 1) {
                        layer.msg("修改成功");
                        app.closeChangePassword();
                        layer.close(index);
                    }else {
                        layer.msg(r.message);
                    }
                }
            });
        },
        closeChangePassword: function(){
            app.password = '';
            app.confirmPassword = '';
        },
        loginNameDiv:function(){
            if ($('.dialogMenu').css('display') === 'block') {
                $('.dialogMenu').css('display', 'none');
            } else {
                $('.dialogMenu').css('display', 'block');
                $('.dialogMenu').css('z-index', '999');
            }
        },
        getAccount: function () {
            //获取用户信息
            if (JSON.parse(sessionStorage.getItem('loginData'))) {
                this.account = JSON.parse(sessionStorage.getItem('loginData')).account;
                this.roleName = JSON.parse(sessionStorage.getItem('loginData')).roleName;
            }
        },
        getMenuList: function () {
            var _this = this;
            $.ajax({
                url: config.baseUrl + "sys/getMenuList",
                async: false,
                success: function (r) {
                    if (r.code === 1) {
                        _this.menuData = r.data;
                    } else {
                    }
                }
            })
        },
        addTab: function(title,url,is_refresh,event){
            //切换左侧菜单
            xadmin.add_tab(title,url,is_refresh);
        },
        renderSystemHandle: function(){
            //处理vue 导致 layui nav 渲染失败问题
            element.render('nav(systemHandle)');
        },
    }
});


