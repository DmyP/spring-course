<html>
<head>
    <title>${header}</title>
</head>
<body>
    <h1>${header}</h1>
    <p>Available seats:</p>
    <form action="/book/tickets" method="POST">
        <input type="hidden" name="event" value="${event}">
        <input type="hidden" name="auditorium" value="${auditorium}">
        <input type="hidden" name="dateTime" value="${dateTime}">
        <input type="hidden" name="user" value="admin">
        <#list seats as seat>
            <input type="checkbox" id=${seat} name="seats" value=${seat} >
            <label for=${seat}>Seat # ${seat}  </label>
        </#list>
        <input type="submit" value="book"/>
    </form>
    <#if tickets??>
        <form action="/book/tickets/toPdf" method="POST">
            <table>
                <td>Event: </td>
                <td>Date: </td>
                <td>Auditorium: </td>
                <td>Seats: </td>
                <td>Price: </td>
                <#list tickets as ticket>
                    <td>${ticket.getEvent().getName()}</td>
                    <td>${ticket.getDateTime()}</td>
                    <td>${ticket.getEvent().getAuditorium().getName()}</td>
                    <td>${ticket.getSeats()}</td>
                    <td>${ticket.getPrice()}</td>
                </#list>
            </table>
            <input type="hidden" name="eventName" value="${event}">
            <input type="hidden" name="dateTime" value="${dateTime}">
            <input type="hidden" name="auditorium" value="${auditorium}">
            <input type="submit" value="Export to PDF"/>
        </form>
    </#if>
    <a href="javascript:history.back()">Go Back</a>
</body>
</html>