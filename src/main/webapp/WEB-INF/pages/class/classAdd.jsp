<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<html>
<head>
    <meta charset="utf-8">
    <title>班级添加</title>
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
    <div class="layui-form-item">
        <label class="layui-form-label required">班级编号</label>
        <div class="layui-input-block">
            <input type="text" name="classCode" lay-verify="required" lay-reqtext="班级编号不能为空" autocomplete="off" placeholder="请输入班级编号" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label required">班级名称</label>
        <div class="layui-input-block">
            <input type="text" name="className" lay-verify="required" lay-reqtext="班级名称不能为空" placeholder="请输入班级名称" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn layui-btn-normal" lay-submit lay-filter="saveBtn">确认保存</button>
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
                url:"addClassSubmit",
                type:"POST",
                data:datas,
                success:function(result){
                    if(result.code == 0){//如果成功
                        layer.msg('添加成功',{
                            icon:6,
                            time:500
                        },function(){
                            parent.window.location.reload();
                            var iframeIndex = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(iframeIndex);
                        })
                    }else if (result.code == 1){
                        layer.msg("班级编号或班级名称重复！");
                    }else {
                        layer.msg("添加失败");
                    }
                }
            })
            return false;
        });

    });
</script>
</body>
</html>
