if (window != top){
    top.location.href = location.href;
}
var form = layui.form;

    var vm = new Vue({
        el: '#container',
        data: {
            sysUser: {},
            //要输入的验证码
            captcha: "",
            //验证码唯一标识传入后端
            captchaId: "",
            //验证码base64图片
            imgData:"",
            //记住密码样式
            rememberPassword: true,
        },
        methods: {
            login: function () {
                var _this = this;
                if (!$.trim(this.sysUser.account)) {
                    layer.alert("请输入用户名");
                    return;
                }
                if (!$.trim(this.sysUser.password)) {
                    layer.alert("请输入用户名");
                    return;
                }
                $.ajax({
                    url: config.baseUrl + "sys/getPublicKey",
                    type: "GET",
                    success: function (r) {
                        if (r.code == 1) {
                            // 公私密钥请求成功
                            var loginId = r.data.loginId;
                            var publicKey = r.data.publicKey;

                            var postData = {};
                            postData.account = vm.sysUser.account;
                            postData.loginId = loginId;
                            postData.captchaId = vm.captchaId;
                            postData.captcha = vm.captcha;

                            var jsEncrypt = new JSEncrypt();
                            jsEncrypt.setPublicKey(publicKey);
                            postData.encryptPassword = jsEncrypt.encrypt(vm.sysUser.password);

                            if (!$.trim(postData.account)) {
                                layer.alert("登录账号不能为空");
                                return;
                            }
                            if (!$.trim(postData.encryptPassword)) {
                                layer.alert("密码不能为空");
                                return;
                            }
                            if (!$.trim(postData.captcha)) {
                                layer.alert("验证码不能为空");
                                return;
                            }
                            $.ajax({
                                url: config.baseUrl + "sys/login",
                                type:"POST",
                                data: JSON.stringify(postData),
                                success:function (r) {
                                    if (r.code == 1) {
                                        sessionStorage.setItem("loginData", r.data.userInfo);
                                        sessionStorage.setItem("token", r.data.token);
                                        location.href="index.html";
                                    }else {
                                        vm.captcha = "";
                                        vm.reloadCaptcha();
                                        layer.alert(r.message);
                                    }
                                }
                            });
                        } else {
                            layer.alert(r.message);
                        }
                    }
                });
            },
            reloadCaptcha:function() {
                $.ajax({
                    url: config.baseUrl + "common/createCaptcha?random="+new Date().getTime(),
                    type: 'GET',
                    success: function (r) {
                        if (r.code == 1) {
                            vm.imgData = r.data.imgBase64;
                            vm.captchaId = r.data.captchaId;
                        }else {
                            layer.alert(r.message);
                        }
                    }
                });
            }
        },
        mounted:function () {

            $(document).keypress(function(e)
            {
                switch(e.which)
                {
                    case 13 :vm.login();break;
                }
            });
        }
    });
    vm.reloadCaptcha();
// });
