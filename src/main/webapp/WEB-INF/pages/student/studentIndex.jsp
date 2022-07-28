<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>学生管理</title>
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
            <div class="layui-form-item layui-form ">
                学号：
                <div class="layui-inline">
                    <input class="layui-input" name="stuNum" id="stuNum"  autocomplete="off">
                </div>
                学生名：
                <div class="layui-inline">
                    <input class="layui-input" name="stuName" id="stuName" autocomplete="off">
                </div>
                班级：
                <div class="layui-inline">
                    <select id="classCode" name="classCode" class="layui-input">
                        <option value="-1">请选择...</option>
                        <c:forEach var="list" items="${classList }">
                            <option value="${list.classCode }">${list.className }</option>
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
            <a class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="update">修改</a>
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
            url: '/studentAll',//查询所有学生数据
            toolbar: '#toolbarDemo',
            defaultToolbar: ['filter', 'exports', 'print', {
                title: '提示',
                layEvent: 'LAYTABLE_TIPS',
                icon: 'layui-icon-tips'
            }],
            cols: [[
                {type: "checkbox", width: 100},
                {field: 'stuNum', width: 150, title: '学号'},
                {field: 'stuName', width: 150, title: '学生名'},
                {field: 'sex', width: 150, title: '性别'},
                {field: 'className', width: 150, title: '班级'},
                {title: '操作', minWidth: 150, toolbar: '#currentTableBar', align: "center"}
            ]],
            limits: [5,10, 15, 20, 25, 50, 100],
            limit: 10,  <!--默认显示10条-->
            page: true,
            skin: 'line',
            id:'testReload'
        });

        var $ = layui.$, active = {
            reload: function(){
                var stuNum = $('#stuNum').val();
                var stuName = $('#stuName').val();
                var classCode = $('#classCode').val();
                console.log(name)
                for ( var i = 0; i < stuNum.length; i++) {
                    if (stuNum.charAt(i) > "9" || stuNum.charAt(i) < "0") {
                        layer.msg("学号含有非数字字符");
                        return;
                    }
                }
                //执行重载
                table.reload('testReload', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    ,where: {
                        stuNum: stuNum,
                        stuName:stuName,
                        classCode:classCode
                    },
                    url: "/searchStudent",
                    method: "POST"
                }, 'data');
            }
        };

        $('.demoTable .layui-btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

        /**
         * tool操作栏监听事件
         */
        table.on('tool(currentTableFilter)', function (obj) {
            var data=obj.data;
            if (obj.event === 'update') {  // 监听修改操作
                var index = layer.open({
                    title: '修改学生信息',
                    type: 2,
                    shade: 0.2,
                    maxmin:true,
                    shadeClose: true,
                    area: ['55%', '65%'],
                    content: '/queryStudentByStuNum?stuNum='+data.stuNum,
                });
                $(window).on("resize", function () {
                    layer.full(index);
                });
            } else if (obj.event === 'delete') {  // 监听删除操作
                layer.confirm('确定是否删除', function (index) {
                    //调用删除功能
                    deleteStudentByStuNums(data.stuNum,index);
                    layer.close(index);
                });
            }
        });

        //监听表格复选框选择
        table.on('checkbox(currentTableFilter)', function (obj) {
            console.log(obj)
        });

        /**
         * 获取选中记录的StuNum信息
         */
        function getCheackStuNum(data){
            var arr=new Array();
            for(var i=0;i<data.length;i++){
                arr.push(data[i].stuNum);
            }
            //拼接stuNum,变成一个字符串
            return arr.join(",");
        };


        /**
         * 提交删除功能
         */
        function deleteStudentByStuNums(stuNums ,index){
            //向后台发送请求
            $.ajax({
                url: "/deleteStudentByStuNums",
                type: "POST",
                data: {stuNums: stuNums},
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
                        layer.msg("删除失败");
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
                    title: '添加学生',
                    type: 2,
                    shade: 0.2,
                    maxmin:true,
                    shadeClose: true,
                    area: ['55%', '65%'],
                    content: '/studentAdd',
                });
                $(window).on("resize", function () {
                    layer.full(index);
                });
            } else if (obj.event === 'delete') {
                /*
                  1、提示内容，必须删除大于0条
                  2、获取要删除记录的学号信息
                  3、提交删除功能 ajax
                */
                //获取选中的记录信息
                var checkStatus=table.checkStatus(obj.config.id);
                var data=checkStatus.data;
                if(data.length==0){//如果没有选中信息
                    layer.msg("请选择要删除的记录信息");
                }else{
                    //获取记录信息的学号集合,拼接的stuNums
                    var stuNums=getCheackStuNum(data);
                    layer.confirm('确定是否删除', function (index) {
                        //调用删除功能
                        deleteStudentByStuNums(stuNums,index);
                        layer.close(index);
                    });
                }
            }
        });

    });
</script>

</body>
</html>
