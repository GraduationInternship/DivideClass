<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="utf-8">
    <title>添加学生</title>
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
        <label class="layui-form-label required">学号</label>
        <div class="layui-input-block">
            <input type="text" name="stuNum" lay-verify="required" lay-reqtext="学号不能为空" placeholder="请输入学号" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label required">学生名</label>
        <div class="layui-input-block">
            <input type="text" name="stuName" lay-verify="required" lay-reqtext="学生名不能为空" placeholder="请输入学生名" autocomplete="off" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label required">密码</label>
        <div class="layui-input-block">
            <input type="password" name="password" lay-verify="required" lay-reqtext="密码不能为空" placeholder="请输入学生密码" autocomplete="off" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label required">性别</label>
        <div class="layui-input-block">
            <input type="radio" name="sex" value="男" title="男" checked="checked"/>
            <input type="radio" name="sex" value="女" title="女" />
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label required">班级</label>
        <div class="layui-input-block">
            <select id="classCode" name="classCode" class="layui-input">
                <c:forEach var="list" items="${classList }">
                    <option value="${list.classCode }" }>${list.className }</option>
                </c:forEach>
            </select>
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

        /**
         * 检查stuNum是否是数字
         */
        function isAllNumber(stuNum){
            for ( var i = 0; i < stuNum.length; i++) {
                if (stuNum.charAt(i) > "9" || stuNum.charAt(i) < "0") {
                    return false;
                }
            }
            return true;
        };

        //监听提交
        form.on('submit(saveBtn)', function (data) {
            var datas=data.field;//form单中的数据信息
            if (!isAllNumber(datas.stuNum)){
                layer.msg("学号含非数字，请重新输入")
            }else{
                //向后台发送数据提交添加
                $.ajax({
                    url:"/addStudentSubmit",
                    type:"POST",
                    // contentType:'application/json',
                    // data:JSON.stringify(datas),
                    data:datas,
                    // data: {
                    //     stuNum:datas.stuNum,
                    //     stuName:datas.stuName,
                    //     password:datas.password,
                    //     sex:datas.sex,
                    //     classCode:datas.classCode
                    // },
                    success:function(result){
                        if(result.code==0){//如果成功
                            console.log("添加成功");
                            layer.msg("添加成功",{
                                icon:6,
                                time:500
                            },function(){
                                parent.window.location.reload();
                                var iframeIndex = parent.layer.getFrameIndex(window.name);
                                parent.layer.close(iframeIndex);
                            })
                        }else if(result.code==1){
                            layer.msg("学号已经存在！！请重新核对学号");
                            console.log("学号已经存在！！请重新核对学号");
                        }else{
                            layer.msg("添加失败");
                            console.log("添加失败");
                        }
                    }
                })
            }
            return false;
        });
    });
</script>
</body>
</html>

