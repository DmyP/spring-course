<html>
<head>
    <title>${header}</title>
</head>
<body>
    <h1>${header}</h1>
    <table>
        <td>User name</td>
        <td>User email</td>
        <td>User birthday</td>
    <#list users as user>
        <tr>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.birthday}</td>
        </tr>
    </#list>
    </table>
    <form action="/user/load" method="Post" enctype="multipart/form-data">
        <input type="file" name="file">
        <button>Upload</button>
    </form>
</body>
</form>
</html>