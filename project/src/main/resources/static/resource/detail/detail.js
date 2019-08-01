function Article__addReply() {
	var $replyArea = $('.reply-form form textarea[name="body"]');
	var reply = $replyArea.val();
	
	reply = reply.trim();
	
	if ( reply.length == 0 ) {
		$replyArea.focus();
		return false;
	}
	
	$.post('./doAddReply', {
		articleId : articleId ,
		body : reply ,
		memberId : articleMemberId
	}, function(data) {
		
	},'json');
	
	$replyArea.val('');
	$replyArea.focus();
}

var Article__lastReplyId = -1;

function Article__loadNewReplies() {

	$.get(
		'./getReplies',
		{
			articleId : articleId,
			from : Article__lastReplyId + 1
		},
		function(data) {		
			for ( var i = 0; i < data.length; i++ ) {
				var reply = data[i];
				Article__lastReplyId = reply.id;
				Article_drawReply(reply);
			}
			
			setTimeout(Article__loadNewReplies, 1000);
		}
	);
}

function Article_drawReply(reply) {
	var id = reply.id;
	var body = filteringXSS(reply.body).replace(/(\n|\r\n)/g, '<br>');
	var regDate = reply.regDate;
	var memberId = reply.memberId;
	
	var whoClassName = 'mine';
	
	if ( memberId != sessionId ) {
		whoClassName = 'other';
	}
	
	var html = `
	    <tr class="reply ${whoClassName}" data-id="${id}">
			<td>${reply.extra.writerName} </td>
			<td>
				<div class="reply-read-mode">: ${body}</div>
				<div class="reply-edit-mode">
					<form onsubmit="Article__modifyReply(); return false;" method="POST">
						<textarea name="body" >${reply.body}</textarea>
						<input type="submit" value="수정">
						<input type="button" onclick="Article__hideModifyReply(this);" value="취소">
					</form>
				</div>
			</td>
			<td>${regDate}</td>
			<td class="reply-btn">
				<a href="javascript:;" onclick="Article__deleteReply(this);">삭제</a> 
				<a href="javascript:;" onclick="Article__showModifyReply(this);">수정</a>
			</td>
		</tr>
	    `;

	$('.reply-list table tbody').prepend(html);
};

function Article__modifyReply() {
	var $tr = $('.edit-mode-visible');
	var id = $tr.attr('data-id');
	var $replyArea = $tr.find('form textarea');
	var reply = $replyArea.val();
	
	reply = reply.trim();
	
	
	if ( reply.length == 0 ) {
		$replyArea.focus();
		return false;
	}
	
	$.post('./doModifyReply', {
		id : id ,
		body : reply
	}, function(data) {
		
	},'json');
	

	var $div = $tr.find('.reply-read-mode');
	$div.empty().append(reply);

	$('.edit-mode-visible').removeClass('edit-mode-visible');
}

function Article__showModifyReply(el) {
	var $el = $(el);
	var $tr = $el.closest('tr');
	
	$('tr.edit-mode-visible').removeClass('edit-mode-visible');
	$tr.addClass('edit-mode-visible');
	
	findEditModeTr();
}

function Article__hideModifyReply(el) {
	var $el = $(el);
	var $tr = $el.closest('tr');
	
	$tr.removeClass('edit-mode-visible');
}



function Article__deleteReply(el) {
	var $tr = $(el).closest('tr');
	var id = $tr.attr('data-id');
	
	$.post('./doDeleteReply', {
		id : id
	}, function(data) {
		
	},'json');
	
	$tr.remove();
}

$(function() {
	$('.reply-form input[type="submit"]').click(Article__addReply);
	
	// input 창에서 키보드 눌림 이벤트 발생시 함수를 실행하도록 예약
	
//	$('.reply-form textarea').keydown(function(e) {
//	    // 만약 입력한 키코드가 13, 즉 엔터라면 함수를 실행한다.
//	    if ( e.keyCode == 13 ) {
//	    	Article__addReply();
//	    }
//	});
	
	Article__loadNewReplies();
});