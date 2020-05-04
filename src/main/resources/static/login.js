
$(document).ready(function () {
   $("#signUpTo").click(function () {
        open("register");
   })
});
function refresh() {
    document.getElementById('captcha_img').src="/kaptcha?"+Math.random();
}
var loginName = $("#loginText");
var loginPassword = $("#pwd");
var loginCode = $("#testCode");



var csrf = $("#csrf").val();
var csrgHead = $("#csrfHead").val();

$("#loginUser").click(function () {
    if(loginName.val().length>20||loginName.val().length<6){
        window.alert("用户名异常");
        return false;
    }
    if(loginPassword.val().length>12||loginPassword.val().length<6){
        window.alert("密码异常");
        return false;
    }
    if(loginCode.val().length!=4){
        window.alert("验证码错误");
        return false;
    }
    $.ajax({
        type:"post",
        url:"/testCode",
        data:{verifyCodeActual:loginCode.val()},
        dataType:'json',
        success:function (data) {
            if(data['result']!= "succeed"){
                window.alert("验证码错误");
            }else{
                loginPassword.val(hex_md5(loginPassword.val()));
                $("#loginFrom").submit();
            }
        },
        beforeSend: function(request) {
            request.setRequestHeader(csrgHead, csrf);
        },
        error:function () {
            window.alert("服务器超时");
        }
    })
})