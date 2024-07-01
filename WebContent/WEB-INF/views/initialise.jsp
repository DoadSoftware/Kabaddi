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
</head>
<body>
<div style="display:flex;justify-content: center; align-items:center;">
	<img class="pull-right img-responsive" src="<c:url value="/resources/Images/Idents.jpg"/>" alt="DOAD Logo">
</div>
<form:form name="initialise_form" autocomplete="off" action="match" method="POST">
<div class="content py-5" style="background-size: cover; background-position: center;  color: #2E008B; width: 100vw; height: 100vh;">
 <!-- <img class="pull-right img-responsive" src="<c:url value="/resources/Images/Ident.png"/>" alt="DOAD Logo"> -->
  <div class="container">
	<div class="row">
	 <div class="col-md-8 offset-md-2" style="margin-top:100px; margin-left:150px; background-color: #c1defb; border-radius: 20px; box-shadow: 20px 10px 50px #9AA2A2; min-height: 20vh">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
             <h3 class="mb-0">Initialise</h3>
           </div>
          <div class="card-body">
          
          <div id="initialise_div" style="display:none;">
		   </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px; font-size: 18px;">
			    <label for="selectedBroadcaster" class="col-sm-4 col-form-label text-left">Select Broadcaster </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="selectedBroadcaster" name="selectedBroadcaster" class="browser-default custom-select custom-select-sm" 
			      		onchange="processUserSelection(this)">	
			      	  <option value="KABADDI">KABADDI</option>
			          
			      </select>
			    </div>
			  </div>
			  &nbsp;
		    <button style="background-color:#2E008B;color:#FEFEFE; font-size: 20px;" class="btn btn-sm" type="button"
		  		name="load_scene_btn" id="load_scene_btn" onclick="processUserSelection(this)">
		  		<i class="fas fa-film"></i> Load Scene</button>
	       </div>
	    </div>
       </div>
    </div>
  </div>
</div>
</form:form>
</body>
</html>