$(document).ready(function() {
    $.ajax({
        type: "get",
        url: "/api/members/"+memberId,
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
});