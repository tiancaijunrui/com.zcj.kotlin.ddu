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
        <h1>${fileName!} <span id="saveMsg" style="font-size: 15px"></span></h1>
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
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "H+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };
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
                        if (data.status === 'SUCCESS'){
                            $("#saveMsg").html("上次保存时间："+new Date().Format("yyyy-MM-dd HH:mm:ss"))
                        }
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