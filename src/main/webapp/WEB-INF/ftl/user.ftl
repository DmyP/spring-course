<html xmlns:th="http://www.thymeleaf.org" xmlns:tiles="http://www.thymeleaf.org">
<head>
    <title>${header}</title>
</head>
<body>
    <h1>${header}</h1>
    <table>
        <td>User name</td>
        <td>User email</td>
        <td>User birthday</td>
        <tr>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.birthday}</td>
        </tr>
    </table>
    <a href="javascript:history.back()">Back</a>
    <a href="/">Home</a>
</body>
</form>
</html>