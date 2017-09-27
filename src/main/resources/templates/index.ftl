<html>
<head>
<#--<script type="text/javascript" src="../static/js/bootstrap.min.js"></script>-->
<#--<link rel="stylesheet" href="../static/css/bootstrap.min.css">-->
    <#include "static.ftl" />
    <style type="text/css">
        .form-group textarea{
            width: 600px;
            height: 120px;
        }
        form{
            margin-left: 100px;
        }
    </style>
</head>
<body>
<div class="page-header">
    <h1>君睿的牛逼之路
        <small>我的日报~~~</small>
    </h1>
</div>
<form method="post" action="/email/toSendFadeng">
    <div class="form-group">
        <label for="todayReport" >今日工作：</label>
        <textarea class="form-control" id="todayReport" placeholder="今日工作" name="email.todayReport"></textarea>
    </div>
    <div class="form-group">
        <label for="tomorrowReport">明日工作：</label>
        <textarea  class="form-control" id="tomorrowReport" placeholder="明日工作" name="email.tomorrowReport"></textarea>
    </div>
    <div class="form-group">
        <label for="remarks">备注：</label>
        <textarea class="form-control" id="remarks" placeholder="备注"  name="email.remarks"></textarea>
    </div>
    <div class="btn-variants">
        <button type="submit" class="btn btn-primary">确定</button>
        <button type="reset" class="btn btn-info">重置</button>
    </div>
</form>
</body>
</html>