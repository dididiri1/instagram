$(document).ready(function(){

    $("#loginForm").validate({
        onkeyup:false,
        onclick:false,
        onfocusout:false,
        showErrors:function(errorMap, errorList) {
            if(this.numberOfInvalids()){
                alert(errorList[0].message);
                $(errorList[0].element).focus();
            }
        },
        rules: {
            userid : {
                required: true
            },
            password : {
                required: true
            }
        },
        messages: {
            userid: "아이디를 입력하세요.",
            password: "비밀번호를 입력하세요."
        },
        submitHandler: function(){
            let username = $("input[name=username]").val();
            let password = $("input[name=password]").val();

            let data = {
                username: username,
                password : password
            }
            $.ajax({
                type: "post",
                url: "/api/auth/login",
                data: data,
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                dataType: "json"
            }).done(res => {
                location.href = res.data.returnUrl;
            }).fail(error => {
                alert("아이디 또는 비밀번호가 일치하지 않습니다.")
                return;
            });
        }
    });

});