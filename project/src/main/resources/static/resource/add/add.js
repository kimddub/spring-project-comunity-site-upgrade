function formCheck(form) {
	form.title.value = form.title.value.trim();
	
	if ( form.title.value.length == 0 ) {
		alert('제목을 입력해주세요');
		form.title.focus();
		
		return false;
	}
	
	form.body.value = form.body.value.trim();
	
	if ( form.body.value.length == 0 ) {
		alert('내용을 입력해주세요');
		form.body.focus();
		
		return false;
	}
	
	form.submit();
}

var fileBoxCount = 0;

function currentFileStatus() {
	if (fileBoxCount < 1) {
		$('.article-file .fileStatus').removeClass('hasFile');
	} else {
		$('.article-file .fileStatus').addClass('hasFile');
	}
}

function addFile(btn, k){
	var file = document.createElement("input");
	var button = document.createElement("button");		
	var container = document.createElement("div");
	var inputType = document.createElement("input");
	var inputType2 = document.createElement("input");
	var type=["image/jpeg", "image/png", "image/gif"];


	inputType.setAttribute("type", "hidden");
	inputType.setAttribute("name", "type");

	inputType2.setAttribute("type", "hidden");
	inputType2.setAttribute("name", "type2");
	
	file.setAttribute("type", "file");
	file.setAttribute("name", "files");
	file.addEventListener("change", function(){
		
		if(!type.includes(this.files[0].type)){
			alert("이미지 파일이 아닙니다.");
			this.value="";
			$(this).next().val("");
			$(this).next().next().val("");
		}else{
			$(this).next().next().val(k);
			$(this).next().next().next().val(this.files[0].type);
		}
	});

	button.setAttribute("type", "button");
	button.innerHTML = "제거";
	button.addEventListener("click", function(){	
		--fileBoxCount;
		currentFileStatus();		
		$(this).parent().remove();
	});
	
	++fileBoxCount;
	currentFileStatus();
	$(container).append(file,button,inputType, inputType2);
	$(".fileList").append(container);
}