/**
 2. 스토리 페이지
 (1) 스토리 로드하기
 (2) 스토리 스크롤 페이징하기
 (3) 좋아요, 안좋아요
 (4) 댓글쓰기
 (5) 댓글삭제
 */

let page = 0;


// (1) 스토리 로드하기
function storyLoad() {
    $.ajax({
        type: "get",
        url:"/api/v1/members/"+memberId+"/story?page="+page,
        dataType:"json"
    }).done(res=>{
        res.data.forEach((image) =>{
            let storyItem = getStoryItem(image);
            $("#storyList").append(storyItem);
        });
    }).fail(error=>{
        console.log("오류",error);
    });
}

storyLoad();

function getStoryItem(image) {
    let item = '';
    item += '<div class="story-list__item">';
    item += '<div class="sl__item__header">';
    item += '<div>';
    item += '<img class="profile-image" src="/images/person.jpeg" onerror="this.src=\'/images/person.jpeg\'" />';
    item += '</div>';
    item += '<div>'+image.username+'</div>';
    item += '</div>';
    item += '<div class="sl__item__img">';
    item += '<img src="'+image.imageUrl+'" />';
    item += '</div>';
    item += '<div class="sl__item__contents">';
    item += '<div class="sl__item__contents__icon">';
    item += '<button>';
    if (image.likeState) {
        item +='<i class="fas fa-heart active" id="storyLikeIcon-'+image.id+'" onclick="toggleLike(\''+image.id+'\')"></i>';
    } else {
        item +='<i class="far fa-heart" id="storyLikeIcon-'+image.id+'" onclick="toggleLike(\''+image.id+'\')"></i>';
    }
    item += '</button>';
    item += '</div>';
    item += '<span class="like"><b id="storyLikeCount-'+image.id+'">'+image.likeCount+' </b>likes</span>';
    item += '<div class="sl__item__contents__content">';
    item += '<p>'+image.caption+'</p>';
    item += '</div>';
    item += '<div id="storyCommentList-1">';
    item += '<div class="sl__item__contents__comment" id="storyCommentItem-'+image.id+'">';
    item += '<p><b>Lovely :</b> 부럽습니다.</p>';
    item += '<button> <i class="fas fa-times"></i> </button>';
    item += '</div>';
    item += '</div>';
    item += '<div class="sl__item__input">';
    item += '<input type="text" placeholder="댓글 달기..." id="storyCommentInput-'+image.id+'" />';
    item += '<button type="button" onClick="addComment('+image.id+')">게시</button>';
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
        data: data,
        dataType:"json"
    }).done(res=>{

    }).fail(error=>{
        console.log("오류",error);
    });

    let content = `
			  <div class="sl__item__contents__comment" id="storyCommentItem-2""> 
			    <p>
			      <b>GilDong :</b>
			      댓글 샘플입니다.
			    </p>
			    <button><i class="fas fa-times"></i></button>
			  </div>
	`;
    commentList.prepend(content);
    commentInput.val("");
}

// (5) 댓글 삭제
function deleteComment() {

}







