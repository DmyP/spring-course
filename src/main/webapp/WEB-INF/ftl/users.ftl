<html>
<head>
    <title>Users List</title>
</head>
<body>
<table>
    <td>User name</td>
    <td>User email</td>
    <td>User birthday</td>
<#list users as user>
    <tr>
        <td>${user.getName()}</td>
        <td>${user.getEmail()}</td>
        <td>${user.getBirthday}</td>
    </tr>
</#list>
</table>
</body>
</form>
</html>