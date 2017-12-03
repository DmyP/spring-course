<html>
<head>
    <title>Events List</title>
</head>
<body>
    <table>
        <td>Event name</td>
        <td>Event Base Price</td>
        <td>Event Auditorium</td>
        <td>Event Date</td>
        <#list events as event>
            <tr>
                <td>${event.getName()}</td>
                <td>${event.getBasePrice()}</td>
                <td>${event.getAuditorium().getName()}</td>
                <td>${event.getDateTime()}</td>
                <td><button><a href="book?event=${event.getName()}&auditorium=${event.getAuditorium().getName()}&dateTime=${event.getDateTime()}"/>Tickets</button></td>
            </tr>
        </#list>
    </table>
</body>
</html>