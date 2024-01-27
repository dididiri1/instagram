/**
 2. 스토리 페이지
 (1) 스토리 로드하기
 (2) 스토리 스크롤 페이징하기
 (3) 좋아요, 안좋아요
 (4) 댓글쓰기
 (5) 댓글삭제
 */

let page = 0;

$(document).ready(function(){
    storyLoad();
});

// (1) 스토리 로드하기
function storyLoad() {
    $.ajax({
        type: "get",
        url:"/api/v1/members/"+memberId+"/story?page="+page,
        dataType:"json"
    }).done(res=>{
        res.data.forEach((data) =>{
            let storyItem = getStoryItem(data);
            $("#storyList").append(storyItem);
        });
    }).fail(error=>{
        console.log("오류",error);
    });
}

function getStoryItem(data) {
    let item = '';
    item += '<div class="story-list__item">';
    item += '<div class="sl__item__header">';
    item += '<div class="user_content" onclick="profileInfo(\''+data.memberId+'\')">';
    item += '<img class="profile-image" src="'+data.profileImageUrl+'" onerror="this.src=\'/images/person.jpeg\'" />';
    item += '</div>';
    item += '<div class="user_content" onclick="profileInfo(\''+data.memberId+'\')">'+data.username+'</div>';
    item += '</div>';
    item += '<div class="sl__item__img">';
    item += '<img src="'+data.imageUrl+'" />';
    item += '</div>';
    item += '<div class="sl__item__contents">';
    item += '<div class="sl__item__contents__icon">';
    item += '<button>';
    if (data.likeState) {
        item +='<i class="fas fa-heart active" id="storyLikeIcon-'+data.id+'" onclick="toggleLike(\''+data.id+'\')"></i>';
    } else {
        item +='<i class="far fa-heart" id="storyLikeIcon-'+data.id+'" onclick="toggleLike(\''+data.id+'\')"></i>';
    }
    item += '</button>';
    item += '</div>';
    item += '<span class="like"><b id="storyLikeCount-'+data.id+'">'+data.likeCount+' </b>likes</span>';
    item += '<div class="sl__item__contents__content">';
    item += '<p>'+data.caption+'</p>';
    item += '</div>';

    item += '<div id="storyCommentList-'+data.id+'">';
    data.comments.forEach((comment) =>{
        item += '<div class="sl__item__contents__comment" id="storyCommentItem-'+comment.id+'">';
        item += '<p><b>'+comment.username+' :</b> '+comment.content+'</p>';

        if (memberId == comment.memberId) {
            item += '<button onclick="deleteComment('+comment.id+')"> <i class="fas fa-times"></i> </button>';
        }
        item += '</div>';
    });

    item += '</div>';
    item += '<div class="sl__item__input">';
    item += '<input type="text" placeholder="댓글 달기..." id="storyCommentInput-'+data.id+'" />';
    item += '<button type="button" onClick="addComment('+data.id+')">게시</button>';
    item += '</div>';
    item += '</div>';
    item += '</div>';

    return item;
}

// (2) 스토리 스크롤 페이징하기
$(window).scroll(() => {
    let checkNum = $(window).scrollTop() - ($(document).height() - $(window).height());
    if(checkNum < 1 && checkNum > -1){
        page ++;
        storyLoad();
    }
});



// (3) 좋아요, 안좋아요
function toggleLike(imageId) {
    let likeIcon = $("#storyLikeIcon-"+imageId);
    if (likeIcon.hasClass("far")) {

        $.ajax({
            type:"post",
            url:"/api/v1/images/"+imageId+"/likes/"+memberId,
            dataType:"json"
        }).done(res=>{
            let storyLikeCount = $("#storyLikeCount-"+imageId);
            let likeCountStr = storyLikeCount.text();
            let likeCount = Number(likeCountStr) + 1;
            storyLikeCount.text(likeCount);

            likeIcon.addClass("fas");
            likeIcon.addClass("active");
            likeIcon.removeClass("far");
        }).fail(error=>{
            console.log("오류",error);
        });

    } else {

        $.ajax({
            type:"delete",
            url:"/api/v1/images/"+imageId+"/likes/"+memberId,
            dataType:"json"
        }).done(res=>{
            let storyLikeCount = $("#storyLikeCount-"+imageId);
            let likeCountStr = storyLikeCount.text();
            let likeCount = Number(likeCountStr) - 1;
            storyLikeCount.text(likeCount);

            likeIcon.removeClass("fas");
            likeIcon.removeClass("active");
            likeIcon.addClass("far");
        }).fail(error=>{
            console.log("오류",error);
        });
    }
}

// (4) 댓글쓰기
function addComment(imageId) {

    console.log("imageId:"+imageId);

    let commentInput = $("#storyCommentInput-"+imageId);
    let commentList = $("#storyCommentList-"+imageId);

    let data = {
        memberId: memberId,
        imageId: imageId,
        content: commentInput.val()
    }

    if (data.content === "") {
        alert("댓글을 작성해주세요!");
        return;
    }

    $.ajax({
        type:"post",
        url:"/api/v1/comment",
        data: JSON.stringify(data),
        contentType: "application/json;charset=utf-8",
        dataType:"json"
    }).done(res=>{
        let content = '<div class="sl__item__contents__comment" id="storyCommentItem-'+res.data.id+'">';
        content +='<p><b>'+res.data.username+' :</b>'+res.data.content+'</p>';
        content +='<button><i class="fas fa-times"></i></button>';
        content +='</div>';

        commentList.prepend(content);
    }).fail(error=>{
        console.log("오류",error);
    });
    commentInput.val("");
}


// (5) 댓글 삭제
function deleteComment(commentId) {
    $.ajax({
        type:"delete",
        url:"/api/v1/comment/"+commentId,
        dataType:"json"
    }).done(res=>{
        $("#storyCommentItem-"+commentId).remove();
    }).fail(error=>{
        console.log("오류",error);
    });
}

function profileInfo(memberId) {
    location.href = "/member/"+memberId;
}







