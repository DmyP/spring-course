<html>
<head>
    <title>${header}</title>
</head>
<body>
    <h1>${header}</h1>
    <table>
        <td>Auditorium name</td>
        <td>Auditorium seatsNumber</td>
        <td>Auditorium vipSeats</td>
    <#list auditoriums as auditorium>
        <tr>
            <td>${auditorium.name}</td>
            <td>${auditorium.seatsNumber}</td>
            <td>${auditorium.vipSeats}</td>
        </tr>
    </#list>
    </table>
    <a href="javascript:history.back()">Go Back</a>
</body>
</html>