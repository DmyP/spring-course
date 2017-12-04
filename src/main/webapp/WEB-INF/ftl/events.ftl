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
        <#list events as event>
            <tr>
                <td>${event.name}</td>
                <td>${event.basePrice}</td>
                <td>${event.auditorium.name}</td>
                <td>${event.dateTime}</td>
                <td><button><a href="book?event=${event.name}&auditorium=${event.auditorium.name}&dateTime=${event.dateTime}"/>Tickets</button></td>
            </tr>
        </#list>
    </table>
    <form action="/events/load" method="Post" enctype="multipart/form-data">
        <input type="file" name="file">
        <button>Upload</button>
    </form>
</body>
</html>