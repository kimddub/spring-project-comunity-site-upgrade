function formCheck(form) {
	form.name.value = form.name.value.trim();
	
	if ( form.name.value.length == 0 ) {
		alert('이름을 입력해주세요');
		form.name.focus();
		
		return false;
	}
	
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
	
	form.email.value = form.email.value.trim();
	
	if ( form.email.value.length == 0 ) {
		alert('e-mail을 입력해주세요');
		form.email.focus();
		
		return false;
	}
	
	form.submit();
}