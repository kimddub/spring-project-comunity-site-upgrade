var fileBoxCount = originFileCount;

function currentFileStatus() {
	if (fileBoxCount < 1) {
		$('.article-file .fileStatus').removeClass('hasFile');
	} else {
		$('.article-file .fileStatus').addClass('hasFile');
	}
}


function addFile(btn, type1){
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
			$(this).next().next().val("");
			$(this).next().next().next().val("");
		}else{
			$(this).next().next().val(type1);
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

function addDeleteList(btn , id){
	if(confirm($(btn).prev().html() + " 을 삭제하시겠 습니까?")){
		var file = document.createElement("input");			
		
		
		file.setAttribute("type", "hidden");
		file.setAttribute("name", "deleteFiles");
		file.setAttribute("value", id);
		
	
		$(".deleteList").append(file);
		--fileBoxCount;
		currentFileStatus();
		$(btn).parent().remove();
	}
}