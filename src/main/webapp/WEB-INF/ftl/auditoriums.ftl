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
    </fieldset>
    <a href="javascript:history.back()">Back</a>
    <a href="/">Home</a>
</body>
</html>