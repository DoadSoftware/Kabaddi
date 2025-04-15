<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
  <sec:csrfMetaTags/>
  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Kabaddi</title>
  <script type="text/javascript" src="<c:url value="/webjars/jquery/1.9.1/jquery.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/bootstrap/3.3.6/js/bootstrap.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/resources/javascript/index.js"/>"></script>
  <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/3.3.6/css/bootstrap.min.css"/>"/>  
  <script type="text/javascript">
	$(document).on("keydown", function(e){
	  
	  if($('#waiting_modal').hasClass('show')) {
		  e.cancelBubble = true;
		  e.stopImmediatePropagation();
    	  e.preventDefault();
		  return false;
	  }
	  
      var evtobj = window.event? event : e;
      
      switch(e.target.tagName.toLowerCase())
      {
      case "input": case "textarea":
    	 break;
      default:
    	  e.preventDefault();
	      var whichKey = '';
		  var validKeyFound = false;
	    
	      if(evtobj.ctrlKey) {
	    	  whichKey = 'Control';
	      }
	      if(evtobj.altKey) {
	    	  if(whichKey) {
	        	  whichKey = whichKey + '_Alt';
	    	  } else {
	        	  whichKey = 'Alt';
	    	  }
	      }
	      if(evtobj.shiftKey) {
	    	  if(whichKey) {
	        	  whichKey = whichKey + '_Shift';
	    	  } else {
	        	  whichKey = 'Shift';
	    	  }
	      }
	      
		  if(evtobj.keyCode) {
	    	  if(whichKey) {
	    		  if(!whichKey.includes(evtobj.key)) {
	            	  whichKey = whichKey + '_' + evtobj.key;
	    		  }
	    	  } else {
	        	  whichKey = evtobj.key;
	    	  }
		  }
		  validKeyFound = false;
		  if (whichKey.includes('_')) {
			  whichKey.split("_").forEach(function (this_key) {
				  switch (this_key) {
				  case 'Control': case 'Shift': case 'Alt':
					break;
				  default:
					validKeyFound = true;
					break;
				  }
			  });
		   } else {
			  if(whichKey != 'Control' && whichKey != 'Alt' && whichKey != 'Shift') {
				  validKeyFound = true;
			  }
		   }
			  
		   if(validKeyFound == true) {
			   console.log('whichKey = ' + whichKey);
			   processUserSelectionData('LOGGER_FORM_KEYPRESS',whichKey);
		   }
	      }
	  });
 </script>
 <style>
 
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
	#match{
	    box-shadow: 20px 10px 20px #9AA2A2;
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

  #homeTeam ,#awayTeam{
  	margin: 0;
  	background-color: #0080FE;
    color: white;
    font-family: 'Arial Black', sans-serif;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
    font-size: 2em;
    text-transform: uppercase;
    letter-spacing: 3px;
    white-space: nowrap;
  }
	#match_time_hdr {
	  background-color: black;
	  color: #00ff00;
	  font-size: calc(1rem + 1vw); /* responsive font size */
	  padding: calc(1vh + 10px) calc(2vw + 10px); /* responsive padding */
	  border-radius: 15px;
	  width: 40%;
	  box-shadow:
	    2px 2px 5px rgba(0, 0, 0, 0.5),
	    inset 2px 2px 8px rgba(0, 255, 0, 0.3),
	    0 0 15px #00ff00;
	  text-shadow:
	    1px 1px 2px #006600,
	    2px 2px 3px #003300;
	  letter-spacing: 0.3vw; /* adjusts spacing with width */
	  text-align: center;
	  white-space: nowrap;
	}

    #event_header{
      background-color: black;
      color: #00ff00;
      font-size: 1em;
      padding: 2px 10px;
      border-radius: 15px;
      box-shadow:
        2px 2px 5px rgba(0, 0, 0, 0.5),
        inset 2px 2px 8px rgba(0, 255, 0, 0.3),
        0 0 15px #00ff00;
      text-shadow:
        1px 1px 2px #006600,
        2px 2px 3px #003300;
      letter-spacing: 5px;
    }
     #events_hdr{
      background-color: #99CCFF;
      color: black;
      font-size: 1.2em;
      padding: 20px 40px;
      border-radius: 15px;
      box-shadow:
        2px 2px 3px rgba(0, 0, 0, 0.5),
        inset 2px 2px 4px rgba(0, 255, 0, 0.3),
        0 0 5px #00ff00;
      text-shadow:
        1px 1px 2px #006600,
        2px 2px 3px #003300;
      letter-spacing: 5px;
    }
    #kabaddi_div table {
   	  margin-bottom: 10px;
      width: 100%;
	}

	#kabaddi_div h6 {
	    margin: 0;
	    font-weight: bold;
	}
	table {
	  width: 100%;
	  border:none;
	  margin-top: 30px;
	  overflow-x: auto;
	}
	
	table th, table td {
	  padding: 15px;
	  border:none;
	}
    body {
	  overflow-x: hidden; /* Prevent horizontal scroll globally */
	  background-color: #A7E3E8; 
	  color: #2E008B; 
	}
	input[type="button"] , button {
	  border: none;
	  background-color: #0080FE;
      color: white;
	  padding: 10px 20px;
	  text-align: center;
	  text-decoration: none;
	  display: inline-block;
	  font-size: 1.5em;
	  cursor: pointer;
	  border-radius: 15px;
	  box-shadow: 0 4px #999; /* Shadow for 3D effect */
	  transition: all 0.2s ease;
	}
	 select ,label{
		 text-align: center;
		 font-size: 1.2em;
	 }
	input[type="button"]:active,.btn:active {
	  box-shadow: 0 2px #666; /* Inner shadow when button is pressed */
	  transform: translateY(4px); /* Moves the button down slightly */
	}
	
  </style>
</head>
<body onload="afterPageLoad('MATCH');">
<div class="header-container">
    <img src="<c:url value='/resources/Images/Design.jpg'/>" alt="Logo">
    <h2>DESIGN ON A DIME</h2>
  </div>
<form:form name="kabaddi_form" autocomplete="off" action="match" method="POST" 
	modelAttribute="session_match" enctype="multipart/form-data">
<div id ="match" class="content py-5" style="width: 100vw; height: 100vh; padding-top: 120px;">
  <div class="container" style="width: 80vw;">
	<div class="row">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
	          </div>
           </div>
          <div class="card-body">
          <div id="select_graphic_options_div" style="display:none;">
			  </div>
			  <div class="panel-group" id="match_configuration">
			    <div class="panel panel-default">
			      <div class="panel-heading">
			        <h5 style="font-size: 2em;" class="panel-title">
			          <a data-toggle="collapse" data-parent="#match_configuration" href="#load_setup_match">Configuration</a>
			        </h5>
			      </div>
			      <div id="load_setup_match" class="panel-collapse collapse">
					<div class="panel-body">
					    <div style="font-size: 18px;" class="col-sm-4 col-md-4">
<!-- 						    <button style="background-color:#2E008B;color:#FEFEFE;display:none;" class="btn btn-sm" type="button"
						  		name="open_clock_btn" id="open_clock_btn" 
						  		onclick="window.open('clock','_blank')">Open Clock</button> -->
						    <button style="background-color:#2E008B;color:#FEFEFE; font-size: 18px;" class="btn btn-sm" type="button"
						  		name="setup_match_btn" id="setup_match_btn" onclick="processUserSelection(this)">
						  		<i class="fas fa-tools"></i> Setup Match</button>
						 </div>
					    <div style="font-size: 18px;" class="col-sm-8 col-md-8">
						    <label for="select_kabaddi_matches" class="col-sm-5 col-form-label text-left">Select Kabaddi Match</label>
						      <select id="select_kabaddi_matches" name="select_kabaddi_matches" 
						      		class="browser-default custom-select custom-select-sm">
									<c:forEach items = "${match_files}" var = "match">
							          <option value="${match.name}">${match.name}</option>
									</c:forEach>
						      </select>
						    <button style="background-color:#2E008B;color:#FEFEFE; font-size: 18px;" class="btn btn-sm" type="button"
						  		name="load_match_btn" id="load_match_btn" onclick="processUserSelection(this)">
						  		<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true" style="display:none"></span>
						  		<i class="fas fa-download"></i> Load Match</button>
					    </div>
				    </div>
			      </div>
			    </div>
			  </div> 
		    <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			  <div id="select_event_div" style="display:none;"></div>
			  <div id="kabaddi_div" style="display:none;"></div>
           </div>
          </div>
         </div>
       </div>
    </div>
 </div>
 <input type="hidden" id="selected_player_id" name="selected_player_id"></input>
 <%-- <input type="hidden" id="which_keypress" name="which_keypress" value="${session_match.which_key_press}"/>--%>
 <input type="hidden" name="selectedBroadcaster" id="selectedBroadcaster" value="${session_selected_broadcaster}"/>
 <input type="hidden" id="matchFileTimeStamp" name="matchFileTimeStamp" value="${session_match.matchFileTimeStamp}"></input>
</form:form>
</body>
</body>
</html>