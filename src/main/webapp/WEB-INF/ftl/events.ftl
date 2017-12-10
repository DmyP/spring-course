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
        <legend><h1>Events</h1></legend>
        <table>
            <td>Event name</td>
            <td>Event Base Price</td>
            <td>Event Auditorium</td>
            <td>Event Date</td>
            <th>Auditorium</th>
            <th>Tickets</th>
            <#list events as event>
                <tr>
                    <td>${event.name}</td>
                    <td>${event.basePrice}</td>
                    <td>${event.auditorium.name}</td>
                    <td>${event.dateTime}</td>
                    <td><a href="/auditorium/${event.auditorium.name}">${event.auditorium.name}</a>
                    <td><a href="/book?event=${event.name}&auditorium=${event.auditorium.name}&dateTime=${event.dateTime}"/>Tickets</></td>
                </tr>
            </#list>
        </table>
    </fieldset>
    <a href="javascript:history.back()">Back</a>
    <a href="/">Home</a>
</body>
</html>