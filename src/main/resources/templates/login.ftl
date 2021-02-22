<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>login</title>
</head>
<body>
用户名:<input id="username" name="userName">
密码 :<input id="password" name="passWord">
<button onclick="login()">登陆</button>

<script type="text/javascript" src="../static/jquery.min.js"></script>
<script type="text/javascript">
    function login() {
        var data = {
            userName: $("#username").val(),
            passWord: $("#password").val()
        };
        console.log(JSON.stringify(data))
        $.ajax({
            url: "http://localhost:8080/login/type-password",
            contentType: "application/json;charset=UTF-8",
            dataType: "json",
            type: "post",
            data: JSON.stringify(data),
            success: function (resp) {
                console.log(resp.access_token);
                console.log(resp.refresh_token);
            }
        });
    }
</script>
</body>
</html>