<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
</head>
<body>
<h3>${oneFile}</h3>
<form method="post" action="upload" enctype="multipart/form-data">
    文件：<input type="file" name="file">
    <input type="submit">
</form>
<h3>${downloadFile}</h3>
<a href="download">下载文件</a>
<h3>${moreFile}</h3>
<form method="post" action="/batch" enctype="multipart/form-data">
    文件1：<input type="file" name="file"><br>
    文件2：<input type="file" name="file">
    <input type="submit">
</form>
</body>
</html>