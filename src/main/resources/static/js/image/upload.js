
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

function imageChoose(obj) {
    let f = obj.files[0];

    if (!f.type.match("image.*")) {
        alert("이미지를 등록해야 합니다.");
        return;
    }

    let reader = new FileReader();
    reader.onload = (e) => {
        $("#imageUploadPreview").attr("src", e.target.result);
    }
    reader.readAsDataURL(f);
}