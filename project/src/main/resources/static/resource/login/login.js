function formCheck(form) {
	
	form.loginId.value = form.loginId.value.trim();
	
	if ( form.loginId.value.length == 0 ) {
		alert('ID를 입력해주세요');
		form.loginId.focus();
		
		return false;
	}
	
	form.loginPw.value = form.loginPw.value.trim();
	
	if ( form.loginPw.value.length == 0 ) {
		alert('PW를 입력해주세요');
		form.loginPw.focus();
		
		return false;
	}
	
	form.submit();
}

function findMode(btn) {
	var $findForm = $(btn).parent().next();
	unfindMode();
	$findForm.addClass('visible');
}

function unfindMode() {
	var $findForm = $('.find-form.visible');
	$('.find-form.visible .find-status').empty();
	$('.find-form.visible input[type="text"]').val('');
	$('.find-form.visible input[name="email"]').val('');
	$findForm.removeClass('visible');
}

function findLoginId(btn) {
	
	var $form = $(btn).closest('form');
	
	var name = $form.find('input[name="name"]').val();
	name = name.trim();
	
	if ( name.length == 0 ) {
		alert('이름을 입력해주세요');
		$form.find('input[name="name"]').focus();
		
		return false;
	}
	
	var email = $form.find('input[name="email"]').val();
	email = email.trim();
	
	if ( email.length == 0 ) {
		alert('e-mail을 입력해주세요');
		$form.find('input[name="email"]').focus();
		
		return false;
	}
	
	$form.find('.find-status').empty().append('잠시만 기다려주세요...');
	
	$.post('./findLoginId', {
		name : name,
		email : email
	}, function(data) {
		alert(data.msg);
		
		var result = data.resultCode;
		
		if ( result.substring(0,2) == "S-" ) {
			unfindMode();
		} else {
			$form.find('.find-status').empty().append('※ 다시 시도해주세요..');
		}
	}, 'json');
	
}

function findLoginPw(btn) {

	var $form = $(btn).closest('form');
	
	var loginId = $form.find('input[name="loginId"]').val();
	loginId = loginId.trim();
	
	if ( loginId.length == 0 ) {
		alert('ID를 입력해주세요');
		$form.find('input[name="loginId"]').focus();
		
		return false;
	}
	
	var email = $form.find('input[name="email"]').val();
	email = email.trim();
	
	if ( email.length == 0 ) {
		alert('e-mail을 입력해주세요');
		$form.find('input[name="email"]').focus();
		
		return false;
	}
	
	$form.find('.find-status').empty().append('잠시만 기다려주세요...');
	
	$.post('./findLoginPw', {
		loginId : loginId,
		email : email
	}, function(data) {
		alert(data.msg);
		
		var result = data.resultCode;
		
		if ( result.substring(0,2) == "S-" ) {
			unfindMode();
		} else {
			$form.find('.find-status').empty().append('※ 다시 시도해주세요..');
		}
	}, 'json');
	
}