
$(document).ready(function() {

    getMember();

    $("#profileUpdate").validate({
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
            name : {
                required: true
            },
            email : {
                required: true
            }
        },
        messages: {
            name: "이름을 입력해주세요.",
            email: "이메일을 입력해주세요."
        },
        submitHandler: function(){
            updateMember();
        }
    });
});

function getMember() {
    $.ajax({
        type: "get",
        url: "/api/v1/members/"+memberId,
        dataType: "json",
    }).done(res => {
        $("input[name=name]").val(res.data.name);
        $("input[name=username]").val(res.data.username);
        $("input[name=email]").val(res.data.email);

    }).fail(error => {
        if(error.responseJSON.data == null){
            alert(error.responseJSON.message);
        }else{
            alert(JSON.stringify(error.responseJSON.data));
        }
    });
}

function updateMember() {
    let formData = new FormData($("#profileUpdate")[0]);
    $.ajax({
        type: "patch",
        url: "/api/v1/members/"+memberId,
        data: JSON.stringify(Object.fromEntries(formData)),
        contentType: "application/json;charset=utf-8",
        dataType: 'json',
    }).done(res => {
        location.href = "/member/"+memberId
    }).fail(error => {
        if(error.responseJSON.data == null){
            alert(error.responseJSON.message);
        }else{
            alert(JSON.stringify(error.responseJSON.data));
        }
    });
}