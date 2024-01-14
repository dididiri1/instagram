/**
 1. 유저 프로필 페이지
 (1) 유저 프로필 페이지 구독하기, 구독취소
 (2) 구독자 정보 모달 보기
 (3) 구독자 정보 모달에서 구독하기, 구독취소
 (4) 유저 프로필 사진 변경
 (5) 사용자 정보 메뉴 열기 닫기
 (6) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
 (7) 사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달
 (8) 구독자 정보 모달 닫기
 (9) 유저 프로필 페이지 조회
 */

$(document).ready(function(){
    getMemberProfile();
});

// (9) 유저 프로필 페이지 조회
function getMemberProfile() {
    $.ajax({
        type: "get",
        url: "/api/v1/members/"+pageMemberId+"/profile/"+memberId,
        dataType: "json",
    }).done(res => {
        console.log(res);
        memberProfileInfo(res.data);
        addImageItem(res.data);
    }).fail(error => {
        if(error.responseJSON.data == null){
            alert(error.responseJSON.message);
        }else{
            alert(JSON.stringify(error.responseJSON.data));
        }
    });
}

function memberProfileInfo(data) {
    $("#profileName").text(data.name);
    $("#imageCount").text(data.imageCount);
    $("#subscribeCount").text(data.subscribeCount);

    if (data.pageOwnerState) {
        $("#imageUploadBtn").removeClass("none");
    } else {
        if (data.subscribeState) {
            $("#unSubscribeBtn").removeClass("none");
        } else {
            $("#inSubscribeBtn").removeClass("none");
        }
    }
}

function addImageItem(data) {
    let item = '';
    for (let i = 0; i < data.images.length; i++) {
        item += '<div class="img-box">';
        item += '<a href=""> <img src="'+data.images[i].imageUrl+'"/></a>';
        item += '<div class="comment">';
        item += '<a href="#" class=""> <i class="fas fa-heart"></i><span>0</span></a>';
        item += '</div>';
        item += '</div>';
    }

    $("#image-content-item").html(item);
}

// (1) 유저 프로필 페이지 구독하기, 구독취소
function toggleSubscribe(toUserId, obj) {
    console.log(toUserId);
    console.log(obj);

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

// (2) 구독자 정보  모달 보기
function subscribeInfoModalOpen(pageUserId) {
    $(".modal-subscribe").css("display", "flex");

    $.ajax({
        type:"get",
        url:"/api/v1/member/"+pageUserId+"/subscribe",
        dataType:"json",
    }).done(res=>{
        console.log(res);

        res.data.forEach((u)=>{
            console.log(u.userid)
            let item = getSubscribeModalItem(u);
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
 * @param u.id
 * @param u.userid
 * @param u.profileImageUrl
 * @param u.subscribeState
 * @param u.equalUserState
 */
function getSubscribeModalItem(u) {
    let item = '<div class="subscribe__item" id="subscribeModalItem-'+u.id+'">' +
        '<div class="subscribe__img">' +
        '<img src="/upload/'+u.profileImageUrl+'" onerror="this.src=\'/images/person.jpeg\'"/>' +
        '</div>' +
        '<div class="subscribe__text">' +
        '<h2>'+u.userid+'</h2>' +
        '</div>' +
        '<div class="subscribe__btn">';
    if(!u.equalUserState) { // 동일 유저가 아닐 떄 버튼이 만들어져야함.
        if(u.subscribeState) { // 구독한 상태
            item += '<button class="cta blue" onclick="toggleSubscribe(\''+u.id+'\', this)">구독취소</button>';
        }	else{ // 구동안한 상태
            item += '<button class="cta" onclick="toggleSubscribe(\''+u.id+'\', this)">구독하기</button>';
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
function profileImageUpload(pageUserId, principalId) {

    if(pageUserId != principalId){
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

        // 서버에 이미지를 전송
        let profileImageForm = $("#userProfileImageForm")[0];

        // FormData 객체를 이용하면 form 태그의 필드와 그 값을 나타내는 일련의 key/value 쌍을 담을 수 있다.
        let formData = new FormData(profileImageForm);

        $.ajax({
            type:"put",
            url:"/api/v1/user/"+principalId+"/profileImageUrl",
            data:formData,
            contentType: false, // 필수 : x-www-form-urlencoded로 파싱되는 것을 방지
            processData: false, // 필수 : contentType을 false로 줬을 때 QuertString 자동 설정됨. 해제됨
            enctype: "multipart/form=data",
        }).done(res=>{
            // 사진 전송 성공시 이미지 변경
            let reader = new FileReader();
            reader.onload = (e) => {
                $("#userProfileImage").attr("src", e.target.result);
            }
            reader.readAsDataURL(f); // 이 코드 실행시 reader.onload 실행됨.
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
    console.log("클릭: "+obj)
    $(obj).css("display", "flex");
}

function closePopup(obj) {
    $(obj).css("display", "none");
}


// (6) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
function modalInfo() {
    $(".modal-info").css("display", "none");
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

