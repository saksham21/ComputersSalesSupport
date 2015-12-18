<!DOCTYPE HTML>
<html>
<head>
    <title>Computers Login</title>
    <!-- <ct th:replace="fragments/header :: commonhead"></ct> -->
    <link rel="stylesheet" href="resources/lib/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="resources/lib/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="resources/lib/css/application.css"/>
    <link rel="stylesheet" href="resources/app/css/computers-signin.css"/>
</head>
    <body class="page-sessions">
<!--   <div class="container">
  <form action="j_spring_security_check" class="form-signin" method='POST'>
    <h2 class="form-signin-heading">Enter your login details</h2>
    <label for="inputUserId" class="sr-only">Username</label>
    <input type="text" id="username" name="username" class="form-control" placeholder="User Id" required="required" autofocus="autofocus" />
    <label for="inputPassword" class="sr-only">Password</label>
    <input type="password" id="password" name="password" class="form-control" placeholder="Password" required="required" />
    <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
  </form>
  </div> -->
  
  <div class="card card-container">

        <p class="card-title">Sign in to your account</p>

        

        

         

<form action="j_spring_security_check" method="POST" class="form-signin">
    
            

<div class="form-group ">
    <input type="text"
        class="form-control input-lg"
        id="username"
        name="username"
        value=""
        placeholder="Username" />
</div>

            

<div class="form-group ">
    <input type="password"
        class="form-control input-lg"
        id="password"
        name="password"
        value=""
        placeholder="Password" />
</div>


            <div class="form-group clearfix">
                <div id="remember" class="checkbox pull-left">
                    <label for="checkbox" class="remember">
                        <input type="checkbox" id="checkbox" checked> Remember me
                    </label>
                </div>
                <a href="/forgotPassword" class="forgot-password pull-right">Forgot the password?</a>
            </div>
            <button id="submit" type="submit" class="btn btn-lg btn-primary btn-block btn-signin">Sign in</button>
        
</form>

    </div>
  <!-- <div th:replace="fragments/footer :: footer"></div> -->
</body>
</html>
