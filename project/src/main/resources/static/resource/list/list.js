function Article__sortChanged(el) {
    var $el = $(el);
    var newValue = $el.val();
    
    var newUrl = getNoDomainUrl();
    newUrl = replaceUrlParam(newUrl, 'sort', newValue);
    
    // 현재 URL을 가져온다.
    // 그 URL에서 sort 부분만 수정한다.
    
    location.href = newUrl;
}