const delete_elements = document.getElementsByClassName("delete");
Array.from(delete_elements).forEach(function(element) {
    element.addEventListener('click',function(){
        if(confirm("정말로 삭제하시겠습니까?")){
            location.href=this.dataset.uri;
        };
    });
});

//파일 업로드 새로고침
document.addEventListener("DOMContentLoaded", function () {
    const imageElement = document.querySelector("img.uploaded-image");

    if (imageElement) {
        setInterval(() => {
            imageElement.src = imageElement.src.split("?")[0] + "?t=" + new Date().getTime();
        }, 5000); 
    }
});








