
$(document).ready(function(){
    getMemberProfile();
});

// 유저 프로필 페이지 조회
function getMemberProfile() {
    $.ajax({
        type: "get",
        url: "/api/v1/members/"+pageMemberId+"/profile/"+memberId,
        dataType: "json",
    }).done(res => {
        console.log(res);
        memberProfileInfo(res.data);
        addImageItem(res.data, pageMemberId);
    }).fail(error => {
        location.href = "/error/404";
    });
}

function memberProfileInfo(data) {
    $("#bio").text(data.bio);
    if (data.profileImageUrl != null) {
        $("#userProfileImage").attr("src", data.profileImageUrl);
    }
    $("#profileName").text(data.name);
    $("#imageCount").text(data.imageCount);
    $("#subscribeCount").text(data.subscribeCount);

    if (data.pageOwnerState) {
        $("#modalInfo").removeClass("none");
        $("#profileUploadBtn").removeClass("none");
        $("#imageUploadBtn").removeClass("none");
    } else {
        $("#modalMemberInfo").removeClass("none");
        if (data.subscribeState) {
            $("#unSubscribeBtn").removeClass("none");
        } else {
            $("#inSubscribeBtn").removeClass("none");
        }
    }
}

function addImageItem(data, memberId) {
    let item = '';
    for (let i = 0; i < data.images.length; i++) {
        item += '<div class="img-box" onclick="myStoryOpen(\''+memberId+'\')">';
        item += '<a href=""> <img src="'+data.images[i].imageUrl+'"/></a>';
        item += '<div class="comment">';
        item += '<a href="#" class=""> <i class="fas fa-heart"></i><span>'+data.images[i].likeCount+'</span></a>';
        item += '</div>';
        item += '</div>';
    }

    $("#image-content-item").html(item);
}

// (1) 유저 프로필 페이지 구독하기, 구독취소
function toggleSubscribe(toUserId, obj) {
    if ($(obj).text() === "구독취소") {
        $.ajax({
            type:"delete",
            url:"/api/v1/subscribe/"+memberId+"/"+toUserId,
            dataType:"json"
        }).done(res => {
            $(obj).text("구독하기");
            $(obj).toggleClass("blue");
        }).fail(error => {
            console.log("구독취소실패",error);
        });
    } else {
        $.ajax({
            type:"post",
            url:"/api/v1/subscribe/"+memberId+"/"+toUserId,
            dataType:"json",
        }).done(res=>{
            $(obj).text("구독취소");
            $(obj).toggleClass("blue");
        }).fail(error=>{
            console.log("구독하기실패",error);
            if(error.responseJSON.data == null){
                alert(error.responseJSON.message);
            }else{
                alert(JSON.stringify(error.responseJSON.data));
            }
        });
    }
}

function toggleItemSubscribe(toUserId, obj) {
    if ($(obj).text() === "구독취소") {
        $.ajax({
            type:"get",
            url:"/api/v1/members/"+toUserId,
            dataType:"json",
        }).done(res=>{
            $("#ms-username").text(res.data.username);
            if (res.data.profileImageUrl != null) {
                $("#ms-profileImageUrl").attr("src", res.data.profileImageUrl);
            } else {
                $("#ms-profileImageUrl").attr("src", "https://kangmin-s3-bucket.s3.ap-northeast-2.amazonaws.com/storage/test/default.png");
            }
            $("#toMemberId").val(toUserId);
            $(".modal-subscribe-info").css("display", "flex");
        }).fail(error=>{
            console.log("error",error);
            if(error.responseJSON.data == null){
                alert(error.responseJSON.message);
            }else{
                alert(JSON.stringify(error.responseJSON.data));
            }
        });
        $(".modal-subscribe-info").css("display", "flex");
    } else {
        $.ajax({
            type:"post",
            url:"/api/v1/subscribe/"+memberId+"/"+toUserId,
            dataType:"json",
        }).done(res=>{
            $(obj).text("구독취소");
            $(obj).toggleClass("blue");
        }).fail(error=>{
            console.log("구독하기실패",error);
            if(error.responseJSON.data == null){
                alert(error.responseJSON.message);
            }else{
                alert(JSON.stringify(error.responseJSON.data));
            }
        });
    }
}

function cancelSubscribe() {
    let toMemberId = $("#toMemberId").val();
    $.ajax({
        type:"delete",
        url:"/api/v1/subscribe/"+memberId+"/"+toMemberId,
        dataType:"json"
    }).done(res => {
        $(".modal-subscribe-info").css("display", "none");
        $("#unSubscribe-"+toMemberId).text("구독하기");
        $("#unSubscribe-"+toMemberId).toggleClass("blue");
    }).fail(error => {
        console.log("구독취소실패",error);
    });
}

// (2) 구독자 정보  모달 보기
function subscribeInfoModalOpen(pageUserId) {
    $(".modal-subscribe").css("display", "flex");
    $(".subscribe-list").children().remove();
    $.ajax({
        type:"get",
        url:"/api/v1/members/"+pageUserId+"/subscribe/"+memberId,
        dataType:"json",
    }).done(res=>{
        console.log(res);
        res.data.forEach((m)=>{
            let item = getSubscribeModalItem(m);
            $(".subscribe-list").append(item);
        });
    }).fail(error=>{
        console.log("구독불러오기실패",error);
        if(error.responseJSON.data == null){
            alert(error.responseJSON.message);
        }else{
            alert(JSON.stringify(error.responseJSON.data));
        }
    });
}

/**
 * @param m.memberId
 * @param m.username
 * @param m.profileImageUrl
 * @param m.subscribeState
 * @param m.equalUserState
 */
function getSubscribeModalItem(m) {
    let item = '<div class="subscribe__item" id="subscribeModalItem-'+m.memberId+'">' +
        '<div class="subscribe__img">' +
        '<img class="user_content" onclick="profileInfo(\''+m.memberId+'\')" src="'+m.profileImageUrl+'" onerror="this.src=\'/images/person.jpeg\'"/>' +
        '</div>' +
        '<div class="subscribe__text">' +
        '<h2 class="user_content" onclick="profileInfo(\''+m.memberId+'\')">'+m.username+'</h2>' +
        '</div>' +
        '<div class="subscribe__btn">';
    if(!m.equalMemberState) {
        if(m.subscribeState) {
            item += '<button class="cta blue" id="unSubscribe-'+m.memberId+'" onclick="toggleItemSubscribe(\''+m.memberId+'\', this)">구독취소</button>';
        }	else{
            item += '<button class="cta" id="inSubscribe-'+m.memberId+'" onclick="toggleItemSubscribe(\''+m.memberId+'\', this)">구독하기</button>';
        }
    }
    item += '</div>' +
        '</div>';

    return item;
}


// (3) 구독자 정보 모달에서 구독하기, 구독취소
function toggleSubscribeModal(obj) {
    if ($(obj).text() === "구독취소") {
        $(obj).text("구독하기");
        $(obj).toggleClass("blue");
    } else {
        $(obj).text("구독취소");
        $(obj).toggleClass("blue");
    }
}

// (4) 유저 프로필 사진 변경
function profileImageUpload(pageMemberId, memberId) {
    if(pageMemberId != memberId){
        alert("프로필 사진을 수정할 수 없는 유저입니다.");
        return;
    }

    $("#userProfileImageInput").click();

    $("#userProfileImageInput").on("change", (e) => {
        let f = e.target.files[0];

        if (!f.type.match("image.*")) {
            alert("이미지를 등록해야 합니다.");
            return;
        }

        let profileImageForm = $("#userProfileImageForm")[0];
        let formData = new FormData(profileImageForm);

        $.ajax({
            type:"put",
            url:"/api/v1/members/"+memberId+"/profileImage",
            data:formData,
            contentType: false,
            processData: false,
            enctype: "multipart/form=data",
        }).done(res=>{
            let reader = new FileReader();
            reader.onload = (e) => {
                $("#userProfileImage").attr("src", e.target.result);
            }
            reader.readAsDataURL(f);
        }).fail(error=>{
            console.log("오류",error);
            if(error.responseJSON.data == null){
                alert(error.responseJSON.message);
            }else{
                alert(JSON.stringify(error.responseJSON.data));
            }
        });
    });
}


// (5) 사용자 정보 메뉴 열기 닫기
function popup(obj) {
    $(obj).css("display", "flex");
}

function closePopup(obj) {
    $(obj).css("display", "none");
}


// (6) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
function modalInfo() {
    $(".modal-info").css("display", "none");
}

function modalSubscribeInfo() {
    $(".subscribe-info").css("display", "none");
}

// (7) 사용자 프로필 이미지 메뉴(사진업로드, 취소) 모달
function modalImage() {
    $(".modal-image").css("display", "none");
}

// (8) 구독자 정보 모달 닫기
function modalClose() {
    $(".modal-subscribe").css("display", "none");
    location.reload();
}

function imageInfoModalOpen() {
    $(".modal-image-info").css("display", "flex");

}

function modalInfoClose() {
    $(".modal-image-info").css("display", "none");
}

function myStoryOpen(memberId) {
    location.href = "/story/"+memberId;
}

function profileInfo(memberId) {
    location.href = "/member/"+memberId;
}
