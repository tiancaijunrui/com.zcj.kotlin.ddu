<#assign staticPerfix = "/editor.md-master"/>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8"/>
    <title>Simple example - Editor.md examples</title>
    <link rel="stylesheet" href="${staticPerfix!}/examples/css/style.css"/>
    <link rel="stylesheet" href="${staticPerfix!}/css/editormd.css"/>
    <link rel="shortcut icon" href="https://pandao.github.io/editor.md/favicon.ico" type="image/x-icon"/>
</head>
<body>
<div id="layout">
    <header>
        <h1>${fileName!}</h1>
    </header>
    <div id="test-editormd"><textarea style="display:none;">
    <#if content??>
${content!}
    </#if>
    </textarea>
    </div>
</div>
<script src="${staticPerfix!}/examples/js/jquery.min.js"></script>
<script src="${staticPerfix!}/editormd.min.js"></script>
<script type="text/javascript">
    var testEditor;

    $(function () {
        testEditor = editormd("test-editormd", {
            width: "90%",
            height: 640,
            syncScrolling: "single",
            path: "${staticPerfix!}/lib/"
        });
        document.onkeydown = function(e) {
            var keyCode = e.keyCode || e.which || e.charCode;
            var ctrlKey = e.ctrlKey || e.metaKey;
            if(ctrlKey && keyCode == 83) {
                $.ajax({
                    url:"/blog/save/${fileName!}",
                    method:"post",
                    data:{content:$("#test-editormd").find("textarea").val()},
                    async:false,
                    success:function (data) {
                        debugger;
                    }
                });
                e.preventDefault();
                return false;
            }
        }

    });
</script>
</body>
</html>