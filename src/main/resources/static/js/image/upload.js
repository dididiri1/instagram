
$(document).ready(function(){
    $('#imageUpload').validate({
        onkeyup:false,
        onclick:false,
        onfocusout:false,
        showErrors:function(errorMap, errorList) {
            if(this.numberOfInvalids()){
                alert(errorList[0].message);
                $(errorList[0].element).focus();
            }
        },
        rules:{
            caption: {
                required : true
            }
        },
        messages: {
            caption: "사진설명을 입력해주세요."
        },
        submitHandler: function(){
            imageCreate();
        }
    });
});

function imageCreate() {
    let formData = new FormData($("#imageUpload")[0]);
    $.ajax({
        type: "post",
        url: "/api/v1/images",
        data: formData,
        enctype: 'multipart/form-data',
        contentType : false,
        processData : false,
    }).done(res => {
        location.href = "/member/"+memberId;
    }).fail(error => {
        if(error.responseJSON.data == null){
            alert(error.responseJSON.message);
        }else{
            alert(JSON.stringify(error.responseJSON.data));
        }
    });
}