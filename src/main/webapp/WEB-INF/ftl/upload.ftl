<html>
<head>
	<title>Upload page</title>
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
		<legend><h1>Upload page</h1></legend>
		<p> Please Select file: </p>
		<form method="POST" enctype="multipart/form-data" action="/upload/users">
			<table>
					<td>Users upload</td>
					<td><input type="file" name="file"/></td>
					<td><input type="submit" value="Upload"/></td>
			</table>
		</form>
		<form method="POST" enctype="multipart/form-data" action="/upload/events">
			<table>
					<td>Events upload</td>
					<td><input type="file" name="file"/></td>
					<td><input type="submit" value="Upload"/></td>
			</table>
		</form>
	</fieldset>
    <a href="javascript:history.back()">Back</a>
    <a href="/">Home</a>
</body>
</html>