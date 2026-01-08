<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Initialise Screen</title>
  <script type="text/javascript" src="<c:url value="/webjars/jquery/1.9.1/jquery.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/bootstrap/3.3.6/js/bootstrap.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/resources/javascript/index.js"/>"></script>
  <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/3.3.6/css/bootstrap.min.css"/>"/>  
  <style type="text/css">
	  html, body {
	  margin: 0;
	  padding: 0;
	  height: 100%;
	  width: 100%;
	  font-family: Arial, sans-serif;
	  background: url('<c:url value="/resources/Images/bg.png"/>') no-repeat center center fixed;
	  background-size: cover;
	}
	.header-container {
    background-color: #0080FE;
    color: white;
    padding: 3px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    position: fixed;
    top: 10px;
    left: 0;
    width: calc(100% - 72px);
    z-index: 1000; /* Ensure header is above other content */
    margin-left: 34px;
    box-shadow: 10px 4px 4px #9AA2A2;
  }
  .header-container img {
    max-width: 140px;
    max-height: 97px;
    width: calc(100% - 40px);
    margin-right: 10px;
    top: 0;
    bottom: 0;
    left: 0;
    
  }

  .header-container h2 {
	  margin: 0;
	  font-family: 'Arial Black', sans-serif;
	  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
	  font-size: calc(1.5rem + 3vw); /* scales with screen size */
	  text-transform: uppercase;
	  letter-spacing: 3px;
	  white-space: nowrap;
	  margin-left: auto;
	  text-align: center;
	  margin-right: calc(30% + 4vw); /* responsive margin */
	}
  .initialise-heading {
	  text-align: center;
	  text-transform: uppercase;
	  font-weight: bold;
	  font-size: 4em;
	  font-family: 'Arial Black', sans-serif;
	  color: #fff;
	  text-shadow:
	    2px 2px 0 #000,
	    4px 4px 0 #2E008B,
	    6px 6px 8px rgba(0, 0, 0, 0.7);
	  background: linear-gradient(90deg, #FF00FF, #00FFFF, #FFFF00);
	  -webkit-background-clip: text;
	  -webkit-text-fill-color: transparent;
	  margin-top: 40px;
	}
  	 select ,label{
		 text-align: center;
		 font-size: 1.5em;
	 }
	 button {
	  padding: 15px 30px;
	  border-radius: 10px;
	  font-size: 20px;
	  transition: all 0.3s ease;
	}
	
	button:hover {
	  background-color: #1d0066; /* Darken the background on hover */
	}
  </style>
</head>
<body>
<div class="header-container">
    <img src="<c:url value='/resources/Images/Design.jpg'/>" alt="Logo">
    <h2>DESIGN ON A DIME</h2>
  </div>
<form:form name="initialise_form" autocomplete="off" action="match" method="POST">
<div class="content py-5" style="background-size: cover; background-position: center;  color: #2E008B; width: 100vw; height: 100vh; padding-top: 320px;">
  <div class="container">
	<div class="row">
	 <div class="col-8" style="background-color: #c1defb; border-radius: 20px; padding:10px; padding-top:10px;box-shadow: 20px 10px 50px #9AA2A2; min-height: 20vh">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
			<h1 class="mb-0 initialise-heading">Initialise</h1>
           </div>
          <div class="card-body">
          
          <div id="initialise_div" style="display:none;">
		   </div>
			  <div class="form-group row row-bottom-margin ml-2">
			    <label for="selectedBroadcaster" class="col-sm-4 col-form-label text-left">
			    <h3 style=" text-transform: uppercase;font-weight: bold" padding-left:30px;>Select Broadcaster</h3> </label>
			    <div class="col-sm-6 col-md-6" style="padding-top: 20px;">
			      <select id="selectedBroadcaster" name="selectedBroadcaster" class="browser-default custom-select custom-select-sm" 
			      		onchange="processUserSelection(this)">	
			      	  <option value="KABADDI">KABADDI</option>
			          
			      </select>
			    </div>
			  </div>
			  &nbsp;
		    <button style="background-color:#2E008B;color:#FEFEFE; font-size: 20px;" class="btn btn-sm" type="button"
		  		name="submit_btn" id="submit_btn" onclick="processUserSelection(this)">
		  		<i class="fas fa-film"></i> Submit</button>
	       </div>
	    </div>
       </div>
    </div>
  </div>
</div>
</form:form>
</body>
</html>