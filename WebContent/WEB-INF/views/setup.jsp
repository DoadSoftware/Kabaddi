<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
/* Basic Reset for the Form */
body, html {
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
  background: url('<c:url value="/resources/Images/bg.png"/>') no-repeat center center fixed;
  background-size: cover;
}

.container {
  margin: 0 auto;
  padding-top: 50px;
}

.card {
  border-radius: 8px;
  box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.1);
}

.card-header {
  background-color: #2E008B;
  color: #fff;
  font-size: 24px;
  padding: 20px;
  border-radius: 8px 8px 0 0;
  text-align: center;
}

.card-body {
  padding: 20px;
  background-color: #f8f8f8;
  border-radius: 0 0 8px 8px;
}

/* Form styling */
.form-group {
  margin-bottom: 15px;
}

label {
  font-size: 18px;
  color: #333;
}

input, select {
  font-size: 16px;
  padding: 10px;
  width: 100%;
  border-radius: 5px;
  border: 1px solid #ccc;
  margin-top: 5px;
}

input:focus, select:focus {
  border-color: #2E008B;
  box-shadow: 0px 0px 5px rgba(46, 0, 139, 0.5);
}

button {
  font-size: 18px;
  background-color: #2E008B;
  color: #FEFEFE;
  padding: 10px 20px;
  border-radius: 5px;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #3a0099;
}

/* Add responsiveness */
@media screen and (max-width: 768px) {
  .form-group {
    margin-left: 0;
    margin-right: 0;
  }

  .col-md-6, .col-sm-6 {
    padding-left: 0;
    padding-right: 0;
  }
}

table {
  width: 100%;
  margin-top: 30px;
  overflow-x: auto;
}

table th, table td {
  padding: 15px;
  text-align: center;
}

table th {
  background-color: #2E008B;
  color: white;
}

table td {
  background-color: #FF9999;
}

table tr:nth-child(even) {
  background-color: #f1f1f1;
}

table td select {
  width: 100%;
  margin: 0;
  padding: 5px;
}

#save_match_div button {
  margin-right: 10px;
}

#matchFileName-validation {
  font-size: 12px;
  color: red;
}
#team_selection_div {
  width: 100%; /* Make sure the container div takes the full width */
  overflow-x: auto; /* Enable horizontal scrolling if necessary */
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
</style>
  <sec:csrfMetaTags/>
  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Setup</title>
	
  <script type="text/javascript" src="<c:url value="/webjars/jquery/1.9.1/jquery.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/bootstrap/3.3.6/js/bootstrap.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/resources/javascript/index.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/select2/4.0.13/js/select2.js"/>"></script>
  
  <link rel="stylesheet" href="<c:url value="/webjars/select2/4.0.13/css/select2.css"/>"/>  
  <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/3.3.6/css/bootstrap.min.css"/>"/>  
	
</head>
<body onload="afterPageLoad('SETUP');">
<div class="header-container">
    <img src="<c:url value='/resources/Images/Design.jpg'/>" alt="Logo">
    <h2>DESIGN ON A DIME</h2>
  </div>
<form:form name="setup_form" method="POST" action="match" enctype="multipart/form-data"
	modelAttribute="session_match">
<div id ="match" class="content py-5" style="width: 100vw; height: 100vh; padding-top: 120px;">
  <div class="container" >
	<div class="row" >
	 <div class="col-12"style ="width: 100%;">
       <span class="anchor"></span>
         <div class="card cad-outline-secondary">
           <div class="card-header">
             <h1 class="mb-0">KABADDI SETUP</h1>
             <h6 style="font-size: 18px; text-transform: uppercase;">${licence_expiry_message}</h6>
           </div>
          <div class="card-body">
	         <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <button style="background-color:#0099FF;color:#FEFEFE; font-size: 18px;" class="btn btn-sm" type="button"
			  		name="cancel_match_setup_btn" id="cancel_match_setup_btn" onclick="processUserSelection(this)">
		  		<i class="fas fa-window-close"></i> Back</button>
	         </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px; font-size: 18px;">
			    <label for="select_existing_kabaddi_matches" class="col-sm-4 col-form-label text-left">Select Kabaddi Match 
			    	<i class="fas fa-asterisk fa-sm text-danger" style="font-size: 18px;"></i></label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_existing_kabaddi_matches" name="select_existing_kabaddi_matches" class="browser-default custom-select custom-select-sm"
			      		onchange="processUserSelection(this)">
				        <option value="new_match">New Match</option>
						<c:forEach items = "${match_files}" var = "match">
				          <option value="${match.name}">${match.name}</option>
						</c:forEach>
			      </select>
			    </div>
			  </div>
			  <div id="matchFileName_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px; font-size: 18px;">
			    <label for="matchFileName" class="col-sm-4 col-form-label text-left">Match Filename <i class="fas fa-asterisk fa-sm text-danger" style="font-size: 7px;"></i></label>
			    <div class="col-sm-6 col-md-6">
		             <input type="text" id="matchFileName" name="matchFileName" class="form-control form-control-sm floatlabel" onblur="processUserSelection(this);"></input>
		              <label id="matchFileName-validation" style="color:red; display: none;"></label> 
			    </div>
			  </div>
			  <div id="tournament_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px; font-size: 18px;">
			    <label for="tournament" class="col-sm-4 col-form-label text-left">Tournament/Series Name <i class="fas fa-asterisk fa-sm text-danger" style="font-size: 7px;"></i></label>
			    <div class="col-sm-6 col-md-6">
		             <input type="text" id="tournament" name="tournament" class="form-control form-control-sm floatlabel" 
		             	onblur="processUserSelection(this);"></input>
			    </div>
			  </div>
			  <div id="matchIdent_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px; font-size: 18px;">
			    <label for="matchIdent" class="col-sm-4 col-form-label text-left">Match Ident <i class="fas fa-asterisk fa-sm text-danger" style="font-size: 7px;"></i></label>
			    <div class="col-sm-6 col-md-6">
		             <input type="text" id="matchIdent" name="matchIdent" 
		             	class="form-control form-control-sm floatlabel" onblur="processUserSelection(this);"></input>
			    </div>
			  </div>
			  <div id="matchId_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px; font-size: 18px;">
			    <label for="matchId" class="col-sm-4 col-form-label text-left">Match Id <i class="fas fa-asterisk fa-sm text-danger" style="font-size: 7px;"></i></label>
			    <div class="col-sm-6 col-md-6">
		             <input type="text" id="matchId" name="matchId" 
		             	class="form-control form-control-sm floatlabel" onblur="processUserSelection(this);"></input>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px; font-size: 18px;">
			    <label for="groundId" class="col-sm-4 col-form-label text-left">Select Ground 
			    	<i class="fas fa-asterisk fa-sm text-danger" style="font-size: 7px;"></i></label>
			    <div class="col-sm-6 col-md-6">
			      <select id="groundId" name="groundId" class="browser-default custom-select custom-select-sm"
			      		onchange="processUserSelection(this)">
						<c:forEach items = "${grounds}" var = "ground">
				          <option value="${ground.groundId}">${ground.fullname}</option>
						</c:forEach>
			      </select>
			    </div>
			  </div>
				  <div style="margin-bottom:5px; font-size: 18px;">
					<div class="row">
					  <div class="col-6 col-sm-3">
					    <label for="homeSubstitutesPerTeam" class="col-form-label text-left">Home Subs</label>
					      <select id="homeSubstitutesPerTeam" name="homeSubstitutesPerTeam" class="browser-default custom-select custom-select-sm">
					      		<c:forEach begin="0" end="15" varStatus="loop">
						          <option value="${loop.index}">${loop.index}</option>
								</c:forEach>
					      </select>
					  </div>
					  <div class="col-6 col-sm-3">
					    <label for="awaySubstitutesPerTeam" class="col-form-label text-left">Away Subs</label>
					      <select id="awaySubstitutesPerTeam" name="awaySubstitutesPerTeam" class="browser-default custom-select custom-select-sm">
					      		<c:forEach begin="0" end="15" varStatus="loop">
						          <option value="${loop.index}">${loop.index}</option>
								</c:forEach>
					      </select>
					  </div>
					   <div class="col-6 col-sm-3">
					    <label for="categoryType" class="col-form-label text-left">Select Category Type: </label>
					      <select id="categoryType" name="categoryType" onchange="processUserSelection(this)" class="browser-default custom-select custom-select-sm">
					        <option value="men">Men</option>
					        <option value="women">Women</option>
				      </select>
					  </div>
					</div>
				  </div>
			  	<table style="font-size: 18px;" class="table table-striped table-bordered"> 
				  <thead>
			        <tr>
			        	<td>Select HOME Team Jersey Color: 
					      <select id="homeTeamJerseyColor" name="homeTeamJerseyColor" class="browser-default custom-select custom-select-sm">
							<c:forEach items = "${teamcolor}" var = "teamcolor">
					          <option value="${teamcolor.colorType}">${teamcolor.colorType}</option>
							</c:forEach>
					      </select>
			        	</td>
			        	<td>Select AWAY Team Jersey Color: 
					      <select id="awayTeamJerseyColor" name="awayTeamJerseyColor" class="browser-default custom-select custom-select-sm">
							<c:forEach items = "${teamcolor}" var = "teamcolor" varStatus="status">
							<c:choose>
								<c:when test="${status.last}">
									<option value="${teamcolor.colorType}" selected="selected">${teamcolor.colorType}</option>
								</c:when>
								<c:otherwise>
						          <option value="${teamcolor.colorType}">${teamcolor.colorType}</option>
								</c:otherwise>
							</c:choose>
							</c:forEach>
					      </select>
			        	</td>
				    </tr>
				  </thead>
				</table>
	        	<table style="font-size: 18px;" class="table table-striped table-bordered"> 
				  <thead>
			        <tr>
			        	<td>Select HOME Team: 
					      <select id="homeTeamId" name="homeTeamId" class="browser-default custom-select custom-select-sm">
							<c:forEach items = "${teams}" var = "team">
					          <option value="${team.teamId}">${team.teamName1}</option>
							</c:forEach>
					      </select>
			        	</td>
			        	<td>Select AWAY Team: 
					      <select id="awayTeamId" name="awayTeamId" class="browser-default custom-select custom-select-sm">
							<c:forEach items = "${teams}" var = "team" varStatus="status">
								<c:choose>
									<c:when test="${status.last}">
							          <option value="${team.teamId}" selected="selected">${team.teamName1}</option>
									</c:when>
									<c:otherwise>
							          <option value="${team.teamId}">${team.teamName1}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
					      </select>
			        	</td>
			        	<td>
						    <button style="background-color:#330000;color:#FEFEFE; font-size: 18px;" class="btn btn-sm" type="button"
						  		name="load_default_team_btn" id="load_default_team_btn" onclick="processUserSelection(this)">
					  		<i class="fas fa-download"></i> Load Teams</button>
			        	</td>
				    </tr>
				  </thead>
				</table>
			  <div id="team_selection_div" class="text-center" style="display:none;">
	         </div>
	         <div id="save_match_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px; font-size: 18px; display:none;">
			    <button style="background-color:#003300;color:#FEFEFE; font-size: 18px;" class="btn btn-sm" type="button"
			  		name="save_match_btn" id="save_match_btn" onclick="processUserSelection(this)">
		  		<i class="fas fa-download"></i> Save Match</button>
			    <button style="background-color:#2E008B;color:#FEFEFE; font-size: 18px;" class="btn btn-sm" type="button"
			  		name="reset_match_btn" id="reset_match_btn" onclick="processUserSelection(this)">
		  		<i class="fas fa-window-close"></i> Reset Match</button>
	         </div>
          </div>
         </div>
       </div>
    </div>
  </div>
</div>
</form:form>
</body>
</html>