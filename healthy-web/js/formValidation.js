var formValidation = (function () {
    /**
     *textarea输入限制控制
     **/
    $(function () {

        $('[lengthlimit]').on('change.common input.common',function () {
            var $TextDom = $(this).find('textarea');
            var totalNum = parseInt($(this).attr('lengthlimit'));
            if($TextDom.length){
                var currentNum = $TextDom.val().length;
                if(currentNum<=totalNum){
                    $(this).find('.lengthlimit-current').text(currentNum)
                }else{
                    $TextDom.val($TextDom.val().slice(0,totalNum+1))
                }
            }else{
                $(this).val($(this).val().slice(0,totalNum+1))
            }
        })
    });
    function layerTips(msg,dom) {
        layer.tips(msg||'输入错误', dom,{
            skin: 'layui-tips-warning',
        })
    }

    //校验规则
    var rules = {
        //非空
        requirer: function(val){
            var val = $.trim(val);
            return {
                check: !val,
                msg: '此项不能为空'
            }
        },
        //手机号
        phone: function (val) {
            return  {
                check: !/^1(3|4|5|6|7|8|9)\d{9}$/.test(val),
                msg: '请输入正确的手机号'
            }
        },
        phoneNumber: function (val) {
            return  {
                check: !/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(val) && !/^1(3|4|5|6|7|8|9)\d{9}$/.test(val),
                msg: '请输入正确的手机号或固定电话'
            }
        },
        //身份证号
        cardNo: function (val) {
            return  {
                check: !checkIDCard(val),
                msg: '请输入正确的身份证号'
            }
        },
        //数字、字母和下划线
        character: function (val) {
            return {
                check: !/^\w+$/.test(val),
                msg: '内容仅由数字、字母及下划线组成'
            }
        },
        // 中文
        chinese: function (val) {
            return {
                check: !/^[\u4e00-\u9fa5]+$/gi.test(val),
                msg: '内容仅由汉字组成'
            }
        },
        // 中文、数字、字母和下划线
        characterAndChar: function (val) {
            return {
                check: !/^[\u4E00-\u9FA5A-Za-z0-9_]{1,32}$/.test(val),
                msg: '内容由1-32位中文、数字、字母及下划线组成'
            }
        },
        // 邮箱
        email: function (val) {
            return {
                check: !/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(val),
                msg: '正确的邮箱地址'
            }
        },
        //数字
        number: function (val) {
            return {
                check: !/^\d{1,8}$/.test(val),
                msg: '只能为数字'
            }
        },
        // url
        url: function (val) {
            return {
                check: !/^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w\.-]+)+[\w\-\._~:/?#[\]@!\$&'\*\+,;=.]+$/.test(val),
                msg: 'url'
            }
        }
    }
    function checkout(rule,option) {
        //第一个参数为检验规则名，第二个参数为{} dom[val,msg]
        //dom必填，为tip依附的dom;val为校验的值，不填则取dom内的val();msg为覆写掉的提示信息
        if(!option.dom){
            return
        }
        var theVal = option.val || $(option.dom).val();
        var theRule = rules[rule](theVal);
        if(theRule.check){
            layerTips(option.msg||theRule.msg,option.dom);
            return theRule.check
        }

    }
    return{
        checkout: checkout
    }

    function checkIDCard(code){
            var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
            var tip = "";
            var pass= true;

            if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
                tip = "身份证号格式错误";
                pass = false;
            }

            else if(!city[code.substr(0,2)]){
                tip = "地址编码错误";
                pass = false;
            }
            else{
                //18位身份证需要验证最后一位校验位
                if(code.length == 18){
                    code = code.split('');
                    //∑(ai×Wi)(mod 11)
                    //加权因子
                    var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
                    //校验位
                    var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
                    var sum = 0;
                    var ai = 0;
                    var wi = 0;
                    for (var i = 0; i < 17; i++)
                    {
                        ai = code[i];
                        wi = factor[i];
                        sum += ai * wi;
                    }
                    var last = parity[sum % 11];
                    if(parity[sum % 11] != code[17]){
                        tip = "校验位错误";
                        pass =false;
                    }
                }
            }
            //if(!pass) alert(tip);
            return pass;
    }
})();