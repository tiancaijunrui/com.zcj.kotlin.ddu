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
<script>

    function editFileName(fileName) {
        if (fileName) {
            location.href = "/blog/editFileName/"+fileName;
        }
    }

    function deleteFileName(fileName) {

    }
</script>
</body>
</html>