function Member__modifyEmail(btn) {
	var $tr = $(btn).closest('tr');
	
	$tr.addClass('email-modify-mode');
	
	var $emailModifyMode = $('.email-modify-form input[name="emailModifyMode"]');
	$emailModifyMode.val('yes');
	
	$('.emailCode-form').css('display','inline-block');
	$('.emailAuthStatus h5').css("color","black");
	$('.email-modify-form input[name="AuthCodeSubmitBtn"]').css('display','inline-block');
	
}

function Member__cancelModifyEmail(btn) {
	var $tr = $(btn).closest('tr');
	
	$tr.removeClass('email-modify-mode');
	
	$('.emailAuthStatus h5').empty().append('e-mail 미인증 상태');
	var $newEmail = $tr.find('input[name="newEmail"]');
	$newEmail.val('');
	
	var $emailModifyMode = $('.email-modify-form input[name="emailModifyMode"]');
	$emailModifyMode.val('no');
	
	var $emailModifyAuth = $('.email-modify-form input[name="emailModifyAuth"]');
	$emailModifyAuth.val('no');
	
}

function getEmailAuthCode() {
	$('.emailCode-form').css('display','block');
	$('.emailAuthStatus h5').css("color","black");
	
	var newEmail = $('#modify-form input[name="newEmail"]').val();
	newEmail = newEmail.trim();
	
	if ( newEmail.length == 0 ) {
		alert('변경할 e-mail을 입력해주세요');
		$form.find('input[name="newEmail"]').focus();
		
		return false;
	}
	
	$('.emailAuthStatus h5').empty().append('잠시만 기다려주세요..');
	
	$.post('./sendNewEmailAuthCode', {
		
		email : newEmail
		
	}, function(data) {
		alert(data.msg);
		$('.emailAuthStatus h5').empty().append(data.msg);
		// ??왜 $(..).val() 은 바로 못쓰지????
		var $hidedAuthCode = $('.emailCode-form input[name="authCode"]');
		
		if (data.resultCode.substring(0,2) == "S-") {
			$hidedAuthCode.val(data.authCode);
		}
		
	}, 'json');
}

function checkEmailAuthCode() {
	// 보안코드를 이메일로 발송했다
	// AJAX로 가져왔다 (컨트롤러나 DB에 저장하기 좀 그래서 여기로 가져옴)??
	// 그러면 서비스까지 안가고 여기서 인증이 완성되게 해도되나??
	var $authCode = $('.emailCode-form input[name="authCode"]');
	var $newEmailAuthCode = $('.emailCode-form input[name="newEmailAuthCode"]');
	
	if ( $authCode.val() == $newEmailAuthCode.val() ) {
		
		$('.emailAuthStatus h5').css("color","red");
		$('.emailAuthStatus h5').empty().append('인증완료');
		
		// 이메일 변경 권한 추가해서 디비 모디파이에 넘기기
		var $emailModifyAuth = $('.email-modify-form input[name="emailModifyAuth"]');
		$emailModifyAuth.val('yes');
		
		$('.email-modify-form input[name="AuthCodeSubmitBtn"]').css('display','none');
		
	} else {
		
		$('.emailAuthStatus h5').css("color","red");
		$('.emailAuthStatus h5').empty().append('인증실패');
		
	}
	
	$('.emailCode-form').css('display','none');
}

function Member__modifyPw() {
	var $tr = $('.password-form');
	
	$tr.addClass('password-modify-mode');
	$tr.find('input[name="modifyPassword"]').val("yes");
}

function Member__cancelModifyPw() {
	var $tr = $('.password-form');
	
	$tr.removeClass('password-modify-mode');
	$tr.find('input[name="passwordChange"]').val("no");
}

function formCheck(form){
	var $form = $(form);
	
	if ( form.name.value.trim().length == 0 ) {
		alert('이름을 입력하세요');
		form.name.focus();
		
		return false;
	}
	
	if ( form.loginPw.value.trim().length == 0 ) {
		alert('PW를 입력하세요');
		form.loginPw.focus();
		
		return false;
	}
	
	var $modifyPassword = $form.find('input[name="modifyPassword"]');
	
	if( $modifyPassword.val() == "yes" ) {
		if ( form.newLoginPw.value.trim().length == 0 ) {
			alert('새 PW를 입력하세요');
			form.newLoginPw.focus();
			
			return false;
		}
	} 
	
	var $emailModifyMode = $form.find('.email-modify-form input[name="emailModifyMode"]');
	var $emailModifyAuth = $form.find('.email-modify-form input[name="emailModifyAuth"]');
	
	if ( $emailModifyMode.val() == "yes" ) {
		if ( form.newEmail.value.trim().length == 0 ) {
			alert('새 e-mail을 입력하세요');
			form.newEmail.focus();
			
			return false;
		}
		
		if ( $emailModifyAuth.val() == "no" ) {
			alert('새 e-mail 인증을 해주세요');
			form.newEmail.focus();
			
			return false;
		}
	}

	//alert('비밀번호 변경 모드? ' + $modifyPassword.val());
	//alert('이메일 변경 모드? ' + $emailModifyMode.val());
	//alert('이메일 인증? ' + $emailModifyAuth.val());
	
	$form.submit();
}

function doWithdraw() {
	if ( confirm('정말로 탈퇴하시겠습니까?') == false ) {
		return false;
	}
	
	var loginPw = $('#modify-form input[name="loginPw"]').val();
	var $withdrawForm = $('#withdraw-form');
	$withdrawForm.find('input[name="loginPw"]').val(loginPw);
	$withdrawForm.submit();
	
	return true;
}

