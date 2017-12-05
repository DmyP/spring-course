<html>
<head>
    <title>${header}</title>
</head>
<body>
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
    <a href="javascript:history.back()">Go Back</a>
</body>
</html>