<html>
<head>
	<title>Upload page</title>
</head>
<body>
	<h1>Upload page</h1>
	<p> Please Select file: </p>
	<form method="POST" enctype="multipart/form-data" action="/upload/users">
		<table>
			<tr>
				<td>Users upload</td>
				<td><input type="file" name="file"/></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Upload"/></td>
			</tr>
		</table>
	</form>
	<form method="POST" enctype="multipart/form-data" action="/upload/events">
		<table>
			<tr>
				<td>Events upload</td>
				<td><input type="file" name="file"/></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Upload"/></td>
			</tr>
		</table>
	</form>
    <a href="javascript:history.back()">Go Back</a>
</body>
</html>