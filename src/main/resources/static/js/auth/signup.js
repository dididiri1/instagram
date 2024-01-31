

$(document).ready(function(){

    $("#form").validate({
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
            username : {
                required: true
            },
            password : {
                required: true
            },
            email : {
                required: true
            },
            name : {
                required: true
            }
        },
        messages: {
            username: "유저명을 입력해주세요.",
            password: "패스워드를 입력해주세요.",
            email: "이메일을 입력해주세요.",
            name: "이름을 입력해주세요."
        },
        submitHandler: function(){
            createMember();
        }
    });
});

function createMember() {
    let formData = new FormData($("#form")[0]);
    $.ajax({
        type: "post",
        url: "/api/v1/members/new",
        data: JSON.stringify(Object.fromEntries(formData)),
        contentType: "application/json;charset=utf-8",
        dataType: 'json',
    }).done(res => {
        location.href = "/";
    }).fail(error => {
        if(error.responseJSON.data == null){
            alert(error.responseJSON.message);
        }else{
            alert(JSON.stringify(error.responseJSON.data));
        }
    });
}