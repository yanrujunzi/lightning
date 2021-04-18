publicSource();
function publicSource() {
    //层级
    var loc = window.location.pathname;
    loc = loc.slice(loc.indexOf('/views/')+6);
    var len = loc.match(/\//g).length;
    var str = '';
    for(var i=0;i<len;i++){
        str += '../'
    }
    document.write(
        '<meta name="renderer" content="webkit|ie-comp|ie-stand">'
        +'<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">'
        +'<meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />'
        +'<meta http-equiv="Cache-Control" content="no-siteapp" />'
        +'<link rel="stylesheet" href="'+str+'lib/bootstrap/css/bootstrap.min.css">'
        +'<link rel="stylesheet" href="'+str+'css/font.css">'
        +'<link rel="stylesheet" href="'+str+'css/xadmin.css">'
        +'<link rel="stylesheet" href="'+str+'css/fonts/iconfont.css">'
        +'<link rel="stylesheet" href="'+str+'css/common/common.css">'
        +'<link rel="stylesheet" href="'+str+'css/common/ui-cover.css">'

        +'<script type="text/javascript" src="'+str+'js/jquery.min.js"></script>'
        // +'<script src="../../lib/layui/layui.js" charset="utf-8"></script>'
        +'<script src="'+str+'/lib/layui/layui.all.js" charset="utf-8"></script>'
        +'<script type="text/javascript" src="'+str+'lib/vue/vue.js"></script>'
        +'<script type="text/javascript" src="'+str+'lib/bootstrap/js/bootstrap.min.js" charset="utf-8"></script>'
        +'<script type="text/javascript" src="'+str+'js/config.js"></script>'
        +'<script type="text/javascript" src="'+str+'js/common.js"></script>'
        +'<script type="text/javascript" src="'+str+'js/formValidation.js"></script>'
        // <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
        // <!--[if lt IE 9]>
        +'<script src="'+str+'lib/ltIE/html5.min.js"></script>'
        +'<script src="'+str+'lib/ltIE/respond.min.js"></script>'
        // <![endif]-->
    )
}
