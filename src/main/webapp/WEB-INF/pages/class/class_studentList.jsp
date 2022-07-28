<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<html>
<head>
    <meta charset="utf-8">
    <title>学生名单</title>
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

    <!--表单，查询出的数据在这里显示-->
    <table class="layui-hide" id="class_studentTable" ></table>

</div>

<script src="/lib/layui-v2.5.5/layui.js" charset="utf-8"></script>
<script>
    llayui.use(['form', 'table'], function () {
        var $ = layui.jquery,
            form = layui.form,
            table = layui.table;

        table.render({
            elem: '#class_studentTable',
            url: '/class_student',
            cols: [[
                {field: 'stuNum', width: 150, title: '学号'},
                {field: 'stuName', width: 150, title: '姓名'},
                {field: 'sex', width: 150, title: '性别'},
                {field:'classCode',width: 150,title: '班级编号'}
            ]],
            limits: [5, 10, 15, 20, 25, 50, 100],
            limit: 15,  <!--默认显示15条-->
            page: true,
            skin: 'line',
            id: 'testReload'
        });
    });
</script>
</body>
</html>
