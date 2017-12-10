<html>
<head>
    <title>${header}</title>
</head>
<body>
    <form action="/login" method="POST">
        <fieldset>
            <legend>Please Login</legend>
            <label for="username">Username</label>
            <input type="text" id="username" name="username"/>
            <label for="password">Password</label>
            <input type="password" id="password" name="password"/>
            <label for="remember-me">Remember Me: </label>
            <input type="checkbox" id="remember-me" name="remember-me"/>
            <button type="submit">Log in</button>
        </fieldset>
    </form>
</body>
</html>