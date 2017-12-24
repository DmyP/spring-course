<html>
<head>
    <title>${header}</title>
    <style>
        table {
            border-collapse: collapse;
            border="1"
            width: 100%;
        }

        th, td {
            text-align: left;
            padding: 8px;
        }

        tr:nth-child(even){background-color: #f2f2f2}
    </style>
</head>
<body>
    <fieldset>
        <legend><h1>${header}</h1></legend>
    <table>
        <td>User name</td>
        <td>User email</td>
        <td>User birthday</td>
        <td>User account</td>
    <#list users as user>
        <tr>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.birthday}</td>
            <td>${user.userAccount}</td>
        </tr>
    </#list>
    </table>
    </fieldset>
    <a href="javascript:history.back()">Back</a>
    <a href="/">Home</a>
</body>
</form>
</html>