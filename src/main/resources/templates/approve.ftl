<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>授权页面</title>
</head>
<body>
<h1>AUTH2 APPROVE</h1>
<h3>Do you approve ?</h3>
<ul style="list-style: none">
    <li>read</li>
    <li>write</li>
    <li>del</li>
</ul>
<button onclick="approve()">yes</button>
<script type="text/javascript" src="../static/jquery.min.js"></script>
<script>
    var code;
    var state;
    $(document).ready(function () {
        var url = window.location.search.substring(1);
        console.log(url)
        var arr = url.split("&");
        console.log(arr)
        for (var i = 0; i < arr.length; i++) {
            var param = arr[i].split("=");
            if (param[0] == 'code') {
                code = param[1];
                console.log('code=' + code)
            }
            if (param[0] == 'state') {
                state = param[1];
                console.log('state=' + state)
            }
        }
    });

    function approve() {
        var data = {
            code: code,
            state: state
        };
        console.log(JSON.stringify(data))
        $.ajax({
            url: "http://localhost:8080/login/type-authorization",
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