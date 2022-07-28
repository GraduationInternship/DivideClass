<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--在jstl前添加了jsp，修复了页面错误--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>班级管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="/lib/layui-v2.5.5/css/layui.css" media="all">
    <link rel="stylesheet" href="/css/public.css" media="all">
    <script src="/lib/layui-v2.5.5/layui.js" charset="utf-8"></script>
</head>
<body>
<div class="layuimini-container">
    <div class="layuimini-main">

        <div class="demoTable">
            <div class="layui-form-item layui-form">
                班级编号：
                <div class="layui-inline">
                    <input class="layui-input" name="classCode" id="classCode" autocomplete="off">
                </div>
                班级名称：
                <div class="layui-inline">
                    <select id="className" name="className" class="layui-input">
                        <c:forEach var="list" items="${classList}">
                            <option value=""></option>
                            <option value="${list.className}">${list.className}</option>
                        </c:forEach>
                    </select>
                </div>
                <button class="layui-btn" data-type="reload">搜索</button>
            </div>
        </div>

        <script type="text/html" id="toolbarDemo">
            <div class="layui-btn-container">
                <button class="layui-btn layui-btn-normal layui-btn-sm data-add-btn" lay-event="add"> 添加 </button>
                <button class="layui-btn layui-btn-sm layui-btn-danger data-delete-btn" lay-event="delete"> 删除 </button>
            </div>
        </script>

        <!--表单，查询出的数据在这里显示-->
        <table class="layui-hide" id="currentTableId" lay-filter="currentTableFilter"></table>

        <script type="text/html" id="currentTableBar">
            <a class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="edit">修改名称</a>
            <a class="layui-btn layui-btn-warm layui-btn-xs data-count-edit" lay-event="student">查看学生名单</a>
            <a class="layui-btn layui-btn-xs layui-btn-danger data-count-delete" lay-event="delete">删除</a>
        </script>

    </div>
</div>

<script>
    layui.use(['form', 'table'], function () {
        var $ = layui.jquery,
            form = layui.form,
            table = layui.table;

        table.render({
            elem: '#currentTableId',
            url: '/classAll',//查询全部数据
            toolbar: '#toolbarDemo',
            defaultToolbar: ['filter', 'exports', 'print', {
                title: '提示',
                layEvent: 'LAYTABLE_TIPS',
                icon: 'layui-icon-tips'
            }],
            cols: [[
                {type: "checkbox", width: 50},
                {field: 'classCode', width: 150, title: '班级编号'},
                {field: 'className', width: 150, title: '班级名称'},
                {field: 'sum', width: 150, title: '人数'},
                {title: '操作', minWidth: 150, toolbar: '#currentTableBar', align: "center"}
            ]],
            limits: [5, 10, 15, 20, 25, 50, 100],
            limit: 15,  <!--默认显示15条-->
            page: true,
            skin: 'line',
            id: 'testReload'
        });

        var $ = layui.$, active = {
            reload: function () {
                var classCode = $('#classCode').val();
                var className = $('#className').val();
                console.log(name)
                //执行重载
                table.reload('testReload', {
                    page: {
                        curr: 1, //重新从第 1 页开始
                        limit: 15
                    }
                    , where: {
                        classCode: classCode,
                        className: className
                    },
                    url: "/searchClass",
                    method: "POST"
                }, 'data');
            }
        };

        $('.demoTable .layui-btn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

        /**
         * tool操作栏监听事件
         */
        table.on('tool(currentTableFilter)', function (obj) {
            var data=obj.data;
            if (obj.event === 'edit') {  // 监听修改操作
                var index = layer.open({
                    title: '修改班级名称',
                    type: 2,
                    shade: 0.2,
                    maxmin:true,
                    shadeClose: true,
                    area: ['50%', '40%'],
                    content: '/queryClassByClassCode?classCode='+data.classCode+'&className='+data.className,
                });
                $(window).on("resize", function () {
                    layer.full(index);
                });
            } else if (obj.event === 'delete') {  // 监听删除操作
                layer.confirm('确定是否删除', function (index) {
                    //调用删除功能
                    deleteInfoByClassCodes(data.classCode,index);
                    layer.close(index);
                });
            }else if (obj.event === 'student') {
                var index = layer.open({
                    title: '学生名单',
                    type: 2,
                    shade: 0.2,
                    maxmin:true,
                    shadeClose: true,
                    area: ['60%', '60%'],
                    content: '/queryStudentByClassCode?classCode='+data.classCode+'&className='+data.className,
                });
                $(window).on("resize", function () {
                    layer.full(index);
                });
            }
        });

        //监听表格复选框选择
        table.on('checkbox(currentTableFilter)', function (obj) {
            console.log(obj)
        });

        /**
         * 获取选中记录的id信息
         */
        function getCheckClassCodes(data){
            var arr=new Array();
            for(var i=0;i<data.length;i++){
                arr.push(data[i].classCode);
            }
            //拼接,变成一个字符串
            return arr.join(",");
        };


        /**
         * 提交删除功能
         */
        function deleteInfoByClassCodes(classCodes ,index){
            //向后台发送请求
            $.ajax({
                url: "/deleteClassByClassCodes",
                type: "POST",
                data: {classCodes: classCodes},
                success: function (result) {
                    if (result.code == 0) {//如果成功
                        layer.msg('删除成功', {
                            icon: 6,
                            time: 500
                        }, function () {
                            parent.window.location.reload();
                            var iframeIndex = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(iframeIndex);
                        });
                    } else {
                        layer.msg("人数不为0的班级不能删除！");
                    }
                }
            })
        };

        /**
         * toolbar栏监听事件
         */
        table.on('toolbar(currentTableFilter)', function (obj) {
            if (obj.event === 'add') {  // 监听添加操作
                var index = layer.open({
                    title: '添加班级',
                    type: 2,
                    shade: 0.2,
                    maxmin:true,
                    shadeClose: true,
                    area: ['50%', '40%'],
                    content: '/classAdd',
                });
                $(window).on("resize", function () {
                    layer.full(index);
                });
            } else if (obj.event === 'delete') {
                //获取选中的记录信息
                var checkStatus=table.checkStatus(obj.config.id);
                var data=checkStatus.data;
                if(data.length==0){//如果没有选中信息
                    layer.msg("请选择要删除的记录信息");
                }else{
                    //获取记录信息的集合
                    var classCodes=getCheckClassCodes(data);
                    layer.confirm('确定是否删除', function (index) {
                        //调用删除功能
                        deleteInfoByClassCodes(classCodes,index);
                        layer.close(index);
                    });
                }
            }
        });

    });
</script>

</body>
</html>
