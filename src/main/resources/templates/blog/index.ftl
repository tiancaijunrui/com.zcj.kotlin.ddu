<html>
<head>
<#include "../static.ftl" />
    <style type="text/css">
        .form-group textarea {
            width: 600px;
            height: 120px;
        }

        form {
            margin-left: 100px;
        }
    </style>
</head>
<body>
<div class="page-header">
    <h1>君睿的牛逼之路
        <small>我的博客~~~</small>
    </h1>
</div>
<div class="container">
    <div class="col-lg-12">
        <div class="input-group">
            <input type="text" class="form-control" placeholder="新增文件名" id="newFileValue">
        <button class="btn btn-default" type="button" onclick="addFile()">新增</button>
        </div>
    </div>
    <div class="col-lg-12">
        <table class="table table-hover">
            <thead>
            <tr>
                <th>#</th>
                <th>文件名称</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <#if blog?? && (blog.fileNameList)??>
                <#list blog.fileNameList as fileName>
                <tr>
                    <th scope="row">${(fileName_index+1)}</th>
                    <td>${fileName!}</td>
                    <td>
                    <button class="btn btn-default btn-sm" onclick="deleteFileName('${fileName!}')">删除</button>
                        <button class="btn btn-default btn-sm" onclick="editFileName('${fileName!}')">编辑</button>
                    </td>
                </tr>
                </#list>
            <#else>
            暂无数据~~
            </#if>
            </tbody>
        </table>
    </div>
</div>
<script>
    function editFileName(fileName) {
        if (fileName) {
            location.href = "/blog/editFileName/" + fileName;
        }
    }
    function deleteFileName(fileName) {
        if (fileName){
            layer.confirm('确定要删除该文件吗?', {
                btn: ['确定','取消'] //按钮
            }, function(){
               $.post("deleteFile",{fileName:fileName},function (data) {
                   if (data.status === "SUCCESS"){
                       location.reload();
                   }
               })
            }, function(){
                layer.closeAll();
            });
        }
    }
    function addFile() {
        var fileName = $("#newFileValue").val();
        if (fileName) {
            $.post("addFile", {fileName: fileName}, function (data) {
                if (data.status === 'SUCCESS') {
                    location.reload();
                }
            })
        }
    }

</script>
</body>
</html>