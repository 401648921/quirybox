<script src="http://cdn.loveqiao.com/jquery.js"></script>
var fileInput = $("#putPic");
   // info = document.getElementById('test-file-info'),
   // preview = document.getElementById('test-image-preview');
// 监听change事件:
fileInput.click(function () {
    // 检查文件是否选择:
    if(!fileInput.value) {
        window.alert("没有选择文件");
        return;
    }
    // 获取File引用:
    var file = fileInput.files[0];
    //判断文件大小
    var size = file.size;
    if(size >= 1*1024*1024){
        alert('文件大于1兆不行!');
        return false;
    }
    if(file.type !== 'image/jpeg' && file.type !== 'image/png' && file.type !== 'image/gif') {
        alert('不是有效的图片文件!');
        return;
    }
    })

