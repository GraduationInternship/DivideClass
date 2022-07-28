<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<html>
<head>
    <meta charset="utf-8">
    <title>修改班级名称</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="/lib/layui-v2.5.5/css/layui.css" media="all">
    <link rel="stylesheet" href="/css/public.css" media="all">
    <style>
        body {
            background-color: #ffffff;
        }
    </style>
</head>
<body>
<div class="layui-form layuimini-form">

    <input type="hidden" name="classCode"  value="${classCode}">
    <input type="hidden" name="className"  value="${className}">

<%--    只显示，后台不能操作和读取--%>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label ">班级编号</label>
        <div class="layui-input-block">
            <input type="text" name="code" placeholder="${classCode}" disabled="disabled" class="layui-input">
        </div>
    </div>
    <%--    只显示，后台不能操作和读取--%>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label ">旧名称</label>
        <div class="layui-input-block">
            <input type="text" name="oldName" placeholder="${className}" disabled="disabled" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label required">新名称</label>
        <div class="layui-input-block">
            <input type="text" name="newClassName" lay-verify="required" lay-reqtext="新名称不能为空" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn layui-btn-normal" lay-submit lay-filter="saveBtn">确认修改</button>
        </div>
    </div>
</div>
<script src="/lib/layui-v2.5.5/layui.js" charset="utf-8"></script>
<script>
    layui.use(['form'], function () {
        var form = layui.form,
            layer = layui.layer,
            $ = layui.$;

        //监听提交
        form.on('submit(saveBtn)', function (data) {
            var datas=data.field;//form单中的数据信息
            //向后台发送数据提交添加
            $.ajax({
                url:"/updateClassNameSubmit",
                type:"POST",
                data: {
                    classCode: datas.classCode,
                    oldClassName:datas.className,
                    newClassName:datas.newClassName
                },
                success:function(result){
                    if(result.code==0){//如果成功
                        layer.msg("修改成功",{
                            icon:6,
                            time:500
                        },function(){
                            parent.window.location.reload();
                            var iframeIndex = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(iframeIndex);
                        })
                    }else{
                        layer.msg(result.msg);
                    }
                }
            })
            return false;
        });

    });
</script>
</body>
</html>
