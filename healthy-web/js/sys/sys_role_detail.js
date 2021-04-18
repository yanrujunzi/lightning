var vm = new Vue({
    el: '#container',
    data: {
        sysRole: {}
    },
    methods: {
    },
    mounted:function () {
        $.ajax({
            type:"GET",
            url: config.baseUrl + "sysRole/info/" + getRequestParams()['id'],
            success: function (r) {
                if (r.code == 1) {
                    vm.sysRole = r.data;
                    /*if (r.data.roleMenuList) {
                        $("#menuJsonView").JSONView(r.data.roleMenuList, { collapsed: true });
                    }*/
                } else {
                    layer.alert(r.message);
                }
            }
        })
    }
});