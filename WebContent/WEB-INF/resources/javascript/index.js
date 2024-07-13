var match_data,team_id,leaderboard,home_team,away_team,home_team_name,away_team_name;
var isTrue = true;
var graphics,preGraphic;
function millisToMinutesAndSeconds(millis) {
  var m = Math.floor(millis / 60000);
  var s = ((millis % 60000) / 1000).toFixed(0);
  return (m < 10 ? '0' + m : m) + ":" + (s < 10 ? '0' + s :s);
}

function displayMatchTime() {
	processKabaddiProcedures('READ_CLOCK',null);
}

function startMatchTimeUpdate() {
   matchTimeInterval = setInterval(displayMatchTime, 5000);
}

function getPlayerMatchStats(playerId){
	var value='';
	if(match_data.events != null && match_data.events.length > 0)
	{
		for(var k = 0; k < match_data.events.length; k++){
			if(match_data.events[k].eventPlayerId == playerId){
				if(match_data.events[k].eventType == 'yellow'){
					value = value + 'Y';
				}else if(match_data.events[k].eventType == 'red'){
					value = value + 'R';
				}
			}else if(match_data.events[k].eventPlayerId == 0){
				if(match_data.events[k].offPlayerId == playerId){
					value = '(OFF) ' + value;
				}else if(match_data.events[k].onPlayerId == playerId){
					value = '(ON) ' + value;
				}
			}
			else{
				value = value + '';
			}
		}
	}else{
		value = value + '';
	}
	return value ;
}
function processWaitingButtonSpinner(whatToProcess) 
{
	switch (whatToProcess) {
	case 'START_WAIT_TIMER': 
		$('.spinner-border').show();
		$(':button').prop('disabled', true);
		break;	
	case 'END_WAIT_TIMER': 
		$('.spinner-border').hide();
		$(':button').prop('disabled', false);
		break;
	}
}
function afterPageLoad(whichPageHasLoaded)
{	
	switch (whichPageHasLoaded) {
	case 'SETUP':
		$('#homeTeamId').select2();
		$('#awayTeamId').select2();
		$('#homeTeamFormationId').select2();
		$('#awayTeamFormationId').select2();
		$('#homeTeamJerseyColor').select2();
		$('#awayTeamJerseyColor').select2();
		break;
	case 'MATCH':
		addItemsToList('LOAD_EVENTS',null);
		break;
	}
}
function initialiseForm(whatToProcess, dataToProcess)
{
	switch (whatToProcess) {
	case 'TIME':
	
		if(match_data) {
			if(document.getElementById('match_time_hdr')) {
				document.getElementById('match_time_hdr').innerHTML = 'MATCH TIME : ' + 
					millisToMinutesAndSeconds(match_data.clock.matchTotalMilliSeconds);
					alert(millisToMinutesAndSeconds(match_data.clock.matchTotalMilliSeconds))
			}
		}
		break;
	//case 'UPDATE-CONFIG':
		//document.getElementById('configuration_file_name').value = $('#select_configuration_file option:selected').val();
		//document.getElementById('selectedBroadcaster').value = dataToProcess.broadcaster;
		//document.getElementById('vizIPAddress').value = dataToProcess.ipAddress;
		//document.getElementById('vizPortNumber').value = dataToProcess.portNumber;
		//document.getElementById('vizSecondaryIPAddress').value = dataToProcess.secondaryipAddress;
		//document.getElementById('vizSecondaryPortNumber').value = dataToProcess.secondaryportNumber;
		//break;
/*	case 'MATCH':
	
		if(match_data) {
			document.getElementById('select_match_halves').value = match_data.clock.matchHalves;
		} else {
			document.getElementById('select_match_halves').selectedIndex = 0;
		}
		break;*/
		
	case 'SETUP':
		
		if(dataToProcess) {
			document.getElementById('matchFileName').value = dataToProcess.matchFileName;
			document.getElementById('tournament').value = dataToProcess.tournament;
			document.getElementById('matchIdent').value = dataToProcess.matchIdent;
			document.getElementById('matchId').value = dataToProcess.matchId;
			document.getElementById('groundId').value = dataToProcess.groundId;
			document.getElementById('homeSubstitutesPerTeam').value = dataToProcess.homeSubstitutesPerTeam;
			document.getElementById('awaySubstitutesPerTeam').value = dataToProcess.awaySubstitutesPerTeam;
			document.getElementById('homeTeamId').value = dataToProcess.homeTeamId;
			document.getElementById('awayTeamId').value = dataToProcess.awayTeamId;
			document.getElementById('homeTeamJerseyColor').value = dataToProcess.homeTeamJerseyColor;
			document.getElementById('awayTeamJerseyColor').value = dataToProcess.awayTeamJerseyColor;
			addItemsToList('LOAD_TEAMS',dataToProcess);
			document.getElementById('save_match_div').style.display = '';
		} else {
			document.getElementById('matchFileName').value = '';
			document.getElementById('tournament').value = '';
			document.getElementById('matchIdent').value = '';
			document.getElementById('matchId').value = '';
			document.getElementById('groundId').selectedIndex = 0;
			document.getElementById('homeSubstitutesPerTeam').selectedIndex = 0;
			document.getElementById('awaySubstitutesPerTeam').selectedIndex = 0;
			document.getElementById('homeTeamId').selectedIndex = 0;
			document.getElementById('awayTeamId').selectedIndex = 1;
			document.getElementById('homeTeamJerseyColor').selectedIndex = 0;
			document.getElementById('awayTeamJerseyColor').selectedIndex = 1;
			addItemsToList('LOAD_TEAMS',null);
			document.getElementById('save_match_div').style.display = 'none';
		}
		$('#homeTeamId').prop('selectedIndex', document.getElementById('homeTeamId').options.selectedIndex).change();
		$('#awayTeamId').prop('selectedIndex', document.getElementById('awayTeamId').options.selectedIndex).change();
		
		$('#homeTeamJerseyColor').prop('selectedIndex', document.getElementById('homeTeamJerseyColor').options.selectedIndex).change();
		$('#awayTeamJerseyColor').prop('selectedIndex', document.getElementById('awayTeamJerseyColor').options.selectedIndex).change();
		break;
	}
}
function uploadFormDataToSessionObjects(whatToProcess)
{
	var formData = new FormData();
	var url_path;

	$('input, select, textarea').each(
		function(index){  
			if($(this).is("select")) {
				formData.append($(this).attr('id'),$('#' + $(this).attr('id') + ' option:selected').val());  
			} else {
				formData.append($(this).attr('id'),$(this).val());  
			}	
		}
	);
	
	switch(whatToProcess.toUpperCase()) {
	case 'RESET_MATCH':
		url_path = 'reset_and_upload_match_setup_data';
		break;
	case 'SAVE_MATCH':
		url_path = 'upload_match_setup_data';
		break;
	}
	
	$.ajax({    
		headers: {'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')},
        url : url_path,     
        data : formData,
        cache: false,
        contentType: false,
        processData: false,
        type: 'POST',     
        success : function(data) {
        	switch(whatToProcess.toUpperCase()) {
			case 'RESET_MATCH_BEFORE_SETUP_MATCH':
        		processWaitingButtonSpinner('END_WAIT_TIMER');
        		break;
        	case 'RESET_MATCH':
        		alert('Match has been reset');
        		processWaitingButtonSpinner('END_WAIT_TIMER');
        		break;
        	case 'SAVE_MATCH':
        		document.setup_form.method = 'post';
        		document.setup_form.action = 'back_to_match';
        	   	document.setup_form.submit();
        		break;
        	}
        	
        },    
        error : function(e) {    
       	 	console.log('Error occured in uploadFormDataToSessionObjects with error description = ' + e);     
        }    
    });		
	
}
function processUserSelectionData(whatToProcess,dataToProcess){

	switch (whatToProcess) {
	case 'LOGGER_FORM_KEYPRESS':
		switch (dataToProcess) {
		
		case 'Escape': '27'
			$('#select_graphic_options_div').empty();
			document.getElementById('select_graphic_options_div').style.display = 'none';
			$("#select_event_div").show();
			$("#match_configuration").show();
			$("#kabaddi_div").show();
			break;
		
		}
		
		break;
	}
}
function processUserSelection(whichInput)
{	
	switch ($(whichInput).attr('name')) {
	/*case 'select_configuration_file':
		processKabaddiProcedures('GET-CONFIG-DATA');
		break;*/
	case 'overwrite_match_stats_index':

		document.getElementById('overwrite_match_stats_player_id').selectedIndex = 0;
		document.getElementById('overwrite_match_stats_type').value = '';
		document.getElementById('overwrite_match_stats_total_seconds').value = '';
	
		match_data.matchStats.forEach(function(ms,index,arr){
			if ($('#overwrite_match_stats_index option:selected').val() == ms.statsId) {
				document.getElementById('overwrite_match_stats_player_id').value = ms.playerId;
				document.getElementById('overwrite_match_stats_type').value = ms.statsType;
				document.getElementById('overwrite_match_stats_total_seconds').value = ms.totalMatchMilliSeconds;
			}
		});

		break;

	case 'load_scene_btn':
	
		/*if(checkEmpty($('#vizIPAddress'),'IP Address Blank') == false
			|| checkEmpty($('#vizPortNumber'),'Port Number Blank') == false) {
			return false;
		}*/
    
	  	document.initialise_form.submit();
		break;
	
	case 'cancel_graphics_btn':
		$('#select_graphic_options_div').empty();
		document.getElementById('select_graphic_options_div').style.display = 'none';
		$("#select_event_div").show();
		$("#match_configuration").show();
		$("#kabaddi_div").show();
		break;
	case 'playerRole':
	     //alert(whichInput.id);
	     //alert($('.player_role_dropsowns option:selected').val())
	     player_role_id = whichInput.id;
	     player_role_value = whichInput.value;
	     processWaitingButtonSpinner('START_WAIT_TIMER');
	     processKabaddiProcedures('POPULATE_RAIDER',whichInput);
	     $('.player_role_dropsowns').not($(whichInput)).prop('selectedIndex', 0);
		break;
			
	case 'selectedBroadcaster':
		//$('#vizPortNumber').attr('value','6100');
		//$('label[for=vizScene], input#vizScene').hide();
		//$('label[for=which_scene], select#which_scene').hide();
		//$('label[for=which_layer], select#which_layer').hide();
		break;
	case 'homeTeam': case 'awayTeam':
		$('#selected_team_name').html(whichInput.innerHTML);
		team_id = whichInput.value;
		//$('#selected_team_id').val(whichInput.value);
		document.getElementById('select_event_div').style.display = '';
		break;	
	case 'homePlayers': case 'awayPlayers':
		$('#selected_player_name').html(whichInput.innerHTML);
		$('#selected_player_id').val(whichInput.value);
		document.getElementById('select_event_div').style.display = '';
		break;
	case 'log_teams_score_overwrite_btn': case 'log_match_stats_overwrite_btn': case 'log_match_subs_overwrite_btn':
		processWaitingButtonSpinner('START_WAIT_TIMER');
		switch ($(whichInput).attr('name')) {
		case 'log_teams_score_overwrite_btn': 
			processKabaddiProcedures('LOG_OVERWRITE_TEAM_SCORE',whichInput);
			break;
		case 'log_match_stats_overwrite_btn':
			processKabaddiProcedures('LOG_OVERWRITE_MATCH_STATS',whichInput);
			break;
		case 'log_match_subs_overwrite_btn':
			processKabaddiProcedures('LOG_OVERWRITE_MATCH_SUBS',whichInput);
			break;
		}
		break;
	case 'number_of_undo_txt':
		if(whichInput.value < 0 && whichInput.value > match_data.events.length) {
			alert('Number of undos is invalid.\r\n Must be a positive number and less than the number of events available [' + match_data.events.length + ']');
			whichInput.selected = true;
			return false;
		}
		break;	
	case 'selectTeam': case 'selectCaptianWicketKeeper':
		addItemsToList('POPULATE-PLAYER',match_data);
		break;
	case 'select_existing_kabaddi_matches':
		if(whichInput.value.toLowerCase().includes('new_match')) {
			initialiseForm('SETUP',null);
		} else {
			processWaitingButtonSpinner('START_WAIT_TIMER');
			processKabaddiProcedures('LOAD_SETUP',$('#select_existing_kabaddi_matches option:selected'));
		}
		break;
	case 'log_undo_btn':
		if(match_data.events.length > 0) {
			if($('#number_of_undo_txt').val() > match_data.events.length) {
				if(confirm('Number of undo [' + $('#number_of_undo_txt').val() + '] is bigger than number of events [' 
						+ match_data.events.length + ']. We will make both of them similiar') == false) {
					return false;
				}
				$('#number_of_undo_txt').val(match_data.events.length);
			}
			processWaitingButtonSpinner('START_WAIT_TIMER');
			processKabaddiProcedures('UNDO',$('#number_of_undo_txt'));
		} else {
			alert('No events found');
		}
		break;
	case 'log_replace_btn':
		processKabaddiProcedures('REPLACE',match_data);
		break;
	case 'log_raider_btn':
		processKabaddiProcedures('RAIDER',match_data);
		break;	
	case 'cancel_match_setup_btn':
		document.setup_form.method = 'post';
		document.setup_form.action = 'back_to_match';
	   	document.setup_form.submit();
		break;
	case 'matchFileName':
		if(document.getElementById('matchFileName').value) {
			document.getElementById('matchFileName').value = 
				document.getElementById('matchFileName').value.replace('.xml','') + '.xml';
		}
		break;
	case 'save_match_btn': case 'reset_match_btn':
		switch ($(whichInput).attr('name')) {
		case 'reset_match_btn':
	    	if (confirm('The setup selections of this match will be retained ' +
	    			'but the match data will be deleted permanently. Are you sure, you want to RESET this match?') == false) {
	    		return false;
	    	}
			break;
		}
		if (!checkEmpty(document.getElementById('matchFileName'),'Match Name')) {
			return false;
		} 
		if($('#homeTeamId option:selected').val() == $('#awayTeamId option:selected').val()) {
			alert('Both teams cannot be same. Please choose different home and away team');
			return false;
		}
		for(var tm = 1;tm <= 2;tm++) {
			for(var i = 1;i < 7;i++) {
				for(var j = i+1;j <= 7;j++) {
					if(tm == 1) {
						if(document.getElementById('homePlayer_' + i).selectedIndex == document.getElementById('homePlayer_' + j).selectedIndex) {
							alert(document.getElementById('homePlayer_' + i).options[
								document.getElementById('homePlayer_' + i).selectedIndex].text.toUpperCase() + 
								' selected multiple times for HOME team');
							return false;
						}
					} else {
						if(document.getElementById('awayPlayer_' + i).selectedIndex == document.getElementById('awayPlayer_' + j).selectedIndex) {
							alert(document.getElementById('awayPlayer_' + i).options[
								document.getElementById('awayPlayer_' + i).selectedIndex].text.toUpperCase() + 
								' selected multiple times for AWAY team');
							return false;
						}
					}
				}
			}
		}
		switch ($(whichInput).attr('name')) {
		case 'save_match_btn': 
			uploadFormDataToSessionObjects('SAVE_MATCH');
			break;
		case 'reset_match_btn':
			processWaitingButtonSpinner('START_WAIT_TIMER');
			uploadFormDataToSessionObjects('RESET_MATCH');
			break;
		}
		break;
	case 'load_default_team_btn':
		processWaitingButtonSpinner('START_WAIT_TIMER');
		if($('#homeTeamId option:selected').val() == $('#awayTeamId option:selected').val()) {
			alert('Both teams cannot be same. Please choose different home and away team');
    		processWaitingButtonSpinner('END_WAIT_TIMER');
			return false;
		}
		processKabaddiProcedures('LOAD_TEAMS',whichInput);
		document.getElementById('save_match_div').style.display = '';
		break;
	case 'setup_match_btn':
		document.kabaddi_form.method = 'post';
		document.kabaddi_form.action = 'setup';
	   	document.kabaddi_form.submit();
	   	processWaitingButtonSpinner('START_WAIT_TIMER');
		break;
	case 'load_match_btn':
		processWaitingButtonSpinner('START_WAIT_TIMER');
		processKabaddiProcedures('LOAD_MATCH',$('#select_kabaddi_matches option:selected'));
		break;
	case 'log_event_btn':
		if(whichInput.id.toLowerCase() == 'undo') {
    		if(match_data == null || match_data.events.length <= 0) {
    			alert('No events found to perform undoes');
    			return false;
    		}
    		addItemsToList('LOAD_UNDO',match_data);
		} else if(whichInput.id.toLowerCase() == 'replace'){
			addItemsToList('LOAD_REPLACE',match_data);
			addItemsToList('POPULATE-OFF_PLAYER',match_data);
			addItemsToList('POPULATE-ON_PLAYER',match_data);
		}else if(whichInput.id.toLowerCase() == 'raider'){
			addItemsToList('LOAD_RAIDER',match_data);
		} else if(whichInput.id.toLowerCase() == 'penalty'){
			processKabaddiProcedures('RESET_PENALTY', null);
			addItemsToList('LOAD_PENALTY',match_data);
		}else {
			processWaitingButtonSpinner('START_WAIT_TIMER');
			processKabaddiProcedures('LOG_EVENT',whichInput);
		}
		break;
	case 'Home_goal_btn':
		processKabaddiProcedures('LOG_EVENT',whichInput);
		break;	
	case 'cancel_undo_btn': case 'cancel_overwrite_btn': case 'cancel_event_btn': case 'cancel_replace_btn': case 'cancel_penalty_btn':
		document.getElementById('select_event_div').style.display = 'none';
		addItemsToList('LOAD_EVENTS',match_data); 
		processWaitingButtonSpinner('END_WAIT_TIMER');
		break;
	case 'select_teams':
		addItemsToList('POPULATE-OFF_PLAYER',match_data);
		addItemsToList('POPULATE-ON_PLAYER',match_data);
		break;	

	default:
		switch ($(whichInput).attr('id')) {
		case 'overwrite_teams_total': case 'overwrite_match_time': 
			addItemsToList('LOAD_' + $(whichInput).attr('id').toUpperCase(),null);
			document.getElementById('select_event_div').style.display = '';
			break;
		default:
			if($(whichInput).attr('id').includes('_btn') && $(whichInput).attr('id').split('_').length >= 4) {
	    		switch ($(whichInput).attr('id').split('_')[1]) {
	    		case 'increment':
	    			$('#' + $(whichInput).attr('id').split('_')[0] + '_' + $(whichInput).attr('id').split('_')[2] 
						+ '_' + $(whichInput).attr('id').split('_')[3] + '_txt').val(
						parseInt($('#' + $(whichInput).attr('id').split('_')[0] + '_' + $(whichInput).attr('id').split('_')[2] 
						+ '_' + $(whichInput).attr('id').split('_')[3] + '_txt').val()) + 1
					);
					break;
	    		case 'decrement':
					if(parseInt($('#' + $(whichInput).attr('id').split('_')[0] + '_' + $(whichInput).attr('id').split('_')[2] 
						+ '_' + $(whichInput).attr('id').split('_')[3] + '_txt').val()) > 0) {
		    			
						$('#' + $(whichInput).attr('id').split('_')[0] + '_' + $(whichInput).attr('id').split('_')[2] 
							+ '_' + $(whichInput).attr('id').split('_')[3] + '_txt').val(
							parseInt($('#' + $(whichInput).attr('id').split('_')[0] + '_' + $(whichInput).attr('id').split('_')[2] 
							+ '_' + $(whichInput).attr('id').split('_')[3] + '_txt').val()) - 1
						);
						
					}
					break;
				}				
				processWaitingButtonSpinner('START_WAIT_TIMER');
				processKabaddiProcedures('LOG_STAT',whichInput);
			}
			break;
		}
		break;
	}
}
function processKabaddiProcedures(whatToProcess, whichInput)
{
	var value_to_process; 
	
	switch(whatToProcess) {
	//case 'GET-CONFIG-DATA':
		//value_to_process = $('#select_configuration_file option:selected').val();
		//break;
	case 'READ-MATCH-AND-POPULATE':
		value_to_process = graphics;
		if ((graphics !== preGraphic)) {
		    clearInterval(matchTimeInterval); // Clear the existing interval
		    matchTimeInterval = setInterval(function() {
		        processKabaddiProcedures('READ-MATCH-AND-POPULATE', graphics);
		    }, 5000);
		    preGraphic = graphics;
		}
		break;
	case 'READ_CLOCK':
		valueToProcess = $('#matchFileTimeStamp').val();
		break;
	case 'CANCLE-OPTION':
		value_to_process = 'cancle';
		break;	
	case 'LOG_STAT':
		value_to_process = whichInput.id;
		break;
	case 'LOG_OVERWRITE_TEAM_SCORE': case 'LOG_OVERWRITE_MATCH_STATS': case 'LOG_OVERWRITE_MATCH_SUBS': 
		switch (whatToProcess) {
		case 'LOG_OVERWRITE_TEAM_SCORE':
			value_to_process = $('#overwrite_home_team_score').val() + ',' + $('#overwrite_away_team_score').val();
			break;
		case 'LOG_OVERWRITE_MATCH_STATS':
			value_to_process = $('#overwrite_match_stats_index option:selected').val() 
				+ ',' + $('#overwrite_match_stats_player_id option:selected').val()+ ',' + $('#overwrite_match_stats_type option:selected').val()
				+ ',' + $('#overwrite_match_stats_total_seconds').val();
			break;
		case 'LOG_OVERWRITE_MATCH_SUBS':
			value_to_process = $('#overwrite_match_sub_index option:selected').val() + ',' + $('#overwrite_match_player_id option:selected').val()
				+ ',' + $('#overwrite_match_subs_player_id option:selected').val();
			break;
		}
		break;
		
	case 'LOAD_TEAMS':
		value_to_process = $('#homeTeamId option:selected').val() + ',' + $('#awayTeamId option:selected').val();
		break;

	case 'LOAD_MATCH': case 'LOAD_SETUP':
		value_to_process = whichInput.val();
		break;
		
	case 'LOG_EVENT':
		value_to_process =  whichInput.id + ',' + team_id;
		break;
		
	case 'POPULATE_RAIDER':
		value_to_process =  whichInput.id.split('_')[1] + ',' + whichInput.value;
		break;	
	
	case 'UNDO':
		value_to_process = $('#number_of_undo_txt').val();
		break;
	case 'REPLACE':
		value_to_process = $('#select_player option:selected').val() + ',' + $('#select_sub_player option:selected').val();
		break;
	case 'RAIDER':
		
		value_to_process = $('#select_teams option:selected').val() + ',' + $('#select_bonus').is(':checked')
				+ ',' + $('#select_super_tackel').is(':checked') + ',' + $('#select_super_raid').is(':checked') 
				+ ',' + $('#select_All_Out option:selected').val() + ',' + $('#select_success_UnSuccess').is(':checked') 
				+ ',' + $('#select_Touches option:selected').val();
		
		break;	
	
	case 'POPULATE-PENALTY':
		switch ($('#selectedBroadcaster').val()) {
			
			case 'KABADDI':
				value_to_process = '/Default/Penalty';
				break;
		}
		break;
	
	case 'POPULATE-RED_CARD':
		switch ($('#selectedBroadcaster').val()) {
		
		case 'KABADDI':
			value_to_process = $('#selecthometeamredcard').val() + ',' + $('#selectawayteamredcard').val();
			break;
		}
		break;
		
	}
	if(whatToProcess != 'LOAD_TEAMS'){
		if(match_data){
			value_to_process = match_data.matchFileName + ',' + value_to_process;
		}
	}

	$.ajax({    
        type : 'Get',     
        url : 'processKabaddiProcedures.html',     
        data : 'whatToProcess=' + whatToProcess + '&valueToProcess=' + value_to_process, 
        dataType : 'json',
        success : function(data) {
			match_data = data;
			//alert(whatToProcess);
        	switch(whatToProcess) {
			//case 'GET-CONFIG-DATA':
				//initialiseForm('UPDATE-CONFIG',data);
				//break;
			case 'READ_CLOCK':
				
				if(data){
					if($('#matchFileTimeStamp').val() != data.matchFileTimeStamp) {
						document.getElementById('matchFileTimeStamp').value = data.matchFileTimeStamp;
						session_match = data;
						addItemsToList('LOAD_MATCH',data);
						addItemsToList('LOAD_EVENTS',data);
						//alert(data.api_Match.homeTeamStats.points[0].totalPoints)
						document.getElementById('select_event_div').style.display = 'none';
					}
					addItemsToList('LOAD_MATCH',data);
					
					if(match_data.clock) {
						if(document.getElementById('match_time_hdr')) {
							document.getElementById('match_time_hdr').innerHTML = 'MATCH TIME : ' + 
								millisToMinutesAndSeconds(match_data.clock.matchTotalMilliSeconds);
						}
					}
				}
				match_data = data;
				break;
						
    		case 'LOG_OVERWRITE_TEAM_SCORE': case 'LOG_OVERWRITE_MATCH_STATS': case 'LOG_OVERWRITE_MATCH_SUBS': 
    		case 'UNDO': case 'REPLACE': case 'RAIDER':
        		addItemsToList('LOAD_MATCH',data);
				addItemsToList('LOAD_EVENTS',data);
				document.getElementById('select_event_div').style.display = 'none';
        		break;
        	case 'LOAD_TEAMS':
        		addItemsToList('LOAD_TEAMS',data);
        		break;
        	
			case 'LOG_EVENT': case 'LOAD_MATCH':
				
        		addItemsToList('LOAD_MATCH',data);
	        	switch(whatToProcess) {
	        	case 'LOAD_MATCH':
					document.getElementById('kabaddi_div').style.display = '';
					document.getElementById('select_event_div').style.display = 'none';
					setInterval(displayMatchTime, 2000);
					break;
				}
        		break;
        	case 'LOAD_SETUP':
        		initialiseForm('SETUP',data);
        		break;
        	}
    		processWaitingButtonSpinner('END_WAIT_TIMER');
	    },    
	    error : function(e) {    
	  	 	console.log('Error occured in ' + whatToProcess + ' with error description = ' + e);     
	    }    
	});
}
function addItemsToList(whatToProcess, dataToProcess)
{
	var max_cols,div,linkDiv,anchor,row,cell,header_text,select,option,tr,th,thead,text,table,
		tbody,playerName,api_value_home,api_value_away,teamName;
	var cellCount = 0;
	var addSelect = false;
	
	switch (whatToProcess) {
	
	case 'POPULATE-PLAYER':
		$('#selectPlayer').empty();
		if(match_data.homeTeamId ==  $('#selectTeam option:selected').val()){
			match_data.homeSquad.forEach(function(hs,index,arr){
				$('#selectPlayer').append(
					$(document.createElement('option')).prop({
	                value: hs.playerId,
	                text: hs.jersey_number + ' - ' + hs.full_name
		        }))					
			});
			match_data.homeSubstitutes.forEach(function(hsub,index,arr){
				$('#selectPlayer').append(
					$(document.createElement('option')).prop({
					value: hsub.playerId,
					text: hsub.jersey_number + ' - ' + hsub.full_name + ' (SUB)'
				}))
			});
		}
		else {
			match_data.awaySquad.forEach(function(as,index,arr){
				$('#selectPlayer').append(
					$(document.createElement('option')).prop({
	                value: as.playerId,
	                text: as.jersey_number + ' - ' + as.full_name
		        }))					
			});
			match_data.awaySubstitutes.forEach(function(asub,index,arr){
				$('#selectPlayer').append(
					$(document.createElement('option')).prop({
					value: asub.playerId,
					text: asub.jersey_number + ' - ' + asub.full_name + ' (SUB)'
				}))
			});
		}
		
		break;
	
	case 'LOAD_OVERWRITE_TEAMS_SCORE':

		$('#select_event_div').empty();

		table = document.createElement('table');
		table.setAttribute('class', 'table table-bordered');
				
		tbody = document.createElement('tbody');
		row = tbody.insertRow(tbody.rows.length);
		
		max_cols = 1;
		for(var i = 0; i <= max_cols; i++) {
			
		    option = document.createElement('input');
		    option.type = 'text';
		    header_text = document.createElement('label');

			switch (whatToProcess) {
			case 'LOAD_OVERWRITE_TEAMS_SCORE':
				switch(i) {
				case 0:
					header_text.innerHTML = match_data.homeTeam.teamName4 + ' Score';
					option.id = 'overwrite_home_team_score';
					option.value = match_data.homeTeamScore;
					break;
				case 1:
					header_text.innerHTML = match_data.awayTeam.teamName4 + ' Score';
					option.id = 'overwrite_away_team_score';
					option.value = match_data.awayTeamScore;
					break;
				}
				break;
			}
			
			header_text.htmlFor = option.id;
			row.insertCell(i).appendChild(header_text).appendChild(option);
		}

	    option = document.createElement('input');
	    option.type = 'button';
		switch (whatToProcess) {
		case 'LOAD_OVERWRITE_TEAMS_SCORE':
		    option.name = 'log_teams_score_overwrite_btn';
		    option.value = 'Log Team Score Overwrite';
			break;
		}
	    option.id = option.name;
	    option.setAttribute('onclick','processUserSelection(this);');
	    
	    div = document.createElement('div');
	    div.append(option);

		option = document.createElement('input');
		option.type = 'button';
		option.name = 'cancel_overwrite_btn';
		option.id = option.name;
		option.value = 'Cancel';
		option.setAttribute('onclick','processUserSelection(this)');

	    div.append(document.createElement('br'));
	    div.append(option);
	    
	    max_cols = max_cols + 1;
	    row.insertCell(max_cols).appendChild(div);

		table.appendChild(tbody);
		document.getElementById('select_event_div').appendChild(table);
		
		break;
	
	case 'LOAD_OVERWRITE_MATCH_SUB':
		$('#select_event_div').empty();

		table = document.createElement('table');
		table.setAttribute('class', 'table table-bordered');
				
		tbody = document.createElement('tbody');
		row = tbody.insertRow(tbody.rows.length);
		
		select = document.createElement('select');
		select.style = 'width:75%';
		select.id = 'overwrite_match_sub_index';
		select.name = select.id;
		select.setAttribute('onchange',"processUserSelection(this)");
		if(match_data.events != null && match_data.events.length > 0){
			for(var i = 0; i < match_data.events.length; i++) {
				if(match_data.events[(match_data.events.length - 1) - i].eventType == 'replace') {
					option = document.createElement('option');
					option.value = match_data.events[(match_data.events.length - 1) - i].eventNumber;
				    option.text = match_data.events[(match_data.events.length - 1) - i].onPlayerId + ' - ' + match_data.events[(match_data.events.length - 1) - i].eventType;
				    select.appendChild(option);
				}
			}
		}
		header_text = document.createElement('label');
		header_text.innerHTML = 'SUBS';
		header_text.htmlFor = select.id;
		row.insertCell(0).appendChild(header_text).appendChild(select);
		
		select = document.createElement('select');
		select.style = 'width:75%';
		select.id = 'overwrite_match_player_id';
		
		option = document.createElement('option');
		option.value = '0';
		option.text = '';
		select.appendChild(option);
		
		match_data.homeSquad.forEach(function(hp,index,arr){
			option = document.createElement('option');
			option.value = hp.playerId;
		    option.text = hp.jersey_number + ' - ' + hp.full_name + ' ('+ match_data.homeTeam.teamName4 +')';
		    select.appendChild(option);
		});
		match_data.awaySquad.forEach(function(as,index,arr){
			option = document.createElement('option');
			option.value = as.playerId;
		    option.text = as.jersey_number + ' - ' + as.full_name + ' ('+ match_data.awayTeam.teamName4 +')';
		    select.appendChild(option);
		});
		
	    header_text = document.createElement('label');
		header_text.innerHTML = 'Player';
		header_text.htmlFor = select.id;
		row.insertCell(1).appendChild(header_text).appendChild(select);
		
		select = document.createElement('select');
		select.style = 'width:75%';
		select.id = 'overwrite_match_subs_player_id';
		
		option = document.createElement('option');
		option.value = '0';
		option.text = '';
		select.appendChild(option);
		
		match_data.homeSubstitutes.forEach(function(hsub,index,arr){
			option = document.createElement('option');
			option.value = hsub.playerId;
		    option.text = hsub.jersey_number + ' - ' + hsub.full_name + ' ('+ match_data.homeTeam.teamName4 +') - Sub';
		    select.appendChild(option);
		});
		match_data.awaySubstitutes.forEach(function(asub,index,arr){
			option = document.createElement('option');
			option.value = asub.playerId;
		    option.text = asub.jersey_number + ' - ' + asub.full_name + ' ('+ match_data.awayTeam.teamName4 +') - Sub';
		    select.appendChild(option);
		});
		
	    header_text = document.createElement('label');
		header_text.innerHTML = 'Sub Player';
		header_text.htmlFor = select.id;
		row.insertCell(2).appendChild(header_text).appendChild(select);
		
		option = document.createElement('input');
	    option.type = 'button';
	    option.name = 'log_match_subs_overwrite_btn';
	    option.value = 'Log Match Subs Overwrite';
	    option.id = option.name;
	    option.setAttribute('onclick','processUserSelection(this);');
	    
	    div = document.createElement('div');
	    div.append(option);

		option = document.createElement('input');
		option.type = 'button';
		option.name = 'cancel_overwrite_btn';
		option.id = option.name;
		option.value = 'Cancel';
		option.setAttribute('onclick','processUserSelection(this)');

	    div.append(document.createElement('br'));
	    div.append(option);
	    
	    row.insertCell(3).appendChild(div);

		table.appendChild(tbody);
		document.getElementById('select_event_div').appendChild(table);
		break;
		
	case 'LOAD_OVERWRITE_MATCH_STATS':

		$('#select_event_div').empty();

		table = document.createElement('table');
		table.setAttribute('class', 'table table-bordered');
				
		tbody = document.createElement('tbody');
		row = tbody.insertRow(tbody.rows.length);

		select = document.createElement('select');
		select.style = 'width:75%';
		select.id = 'overwrite_match_stats_index';
		select.name = select.id;
		select.setAttribute('onchange',"processUserSelection(this)");
		
		match_data.matchStats.forEach(function(ms,index,arr){
			option = document.createElement('option');
			option.value = ms.statsId;
		    option.text = '('+ ms.statsCount +') ' + ms.stats_type;
			if(match_data.homeTeamId == ms.teamId){
				option.text = option.text + ' [' + match_data.homeTeam.teamName4 + ']';
				//alert(match_data.homeTeam.teamName1);
			}else if(match_data.awayTeamId == ms.teamId){
				option.text = option.text + ' [' + match_data.awayTeam.teamName4 + ']';
				//alert(match_data.awayTeam.teamName1);
			}
		    option.text = option.text + ' [' + ms.totalMatchMilliSeconds + ']';
		    select.appendChild(option);
		});
	    header_text = document.createElement('label');
		header_text.innerHTML = 'Stats';
		header_text.htmlFor = select.id;
		row.insertCell(0).appendChild(header_text).appendChild(select);

		select = document.createElement('select');
		select.style = 'width:75%';
		select.id = 'overwrite_match_stats_player_id';
		
		for(i = 0;i <= 15;i++){
			option = document.createElement('option');
			option.value = i;
		    option.text = 'Points - ' + i;
		    select.appendChild(option);
		}
		
	    header_text = document.createElement('label');
		header_text.innerHTML = 'Points';
		header_text.htmlFor = select.id;
		row.insertCell(1).appendChild(header_text).appendChild(select);
		
		match_data.matchStats.forEach(function(ms,index,arr){
			option = document.createElement('input');
			option.type = "text";
			option.id = 'overwrite_match_stats_total_seconds';
			option.value = ms.totalMatchMilliSeconds;
		});
		
		header_text = document.createElement('label');
		header_text.innerHTML = 'Time';
		header_text.htmlFor = option.id;
		row.insertCell(2).appendChild(header_text).appendChild(option);

	    option = document.createElement('input');
	    option.type = 'button';
	    option.name = 'log_match_stats_overwrite_btn';
	    option.value = 'Log Match Stats Overwrite';
	    option.id = option.name;
	    option.setAttribute('onclick','processUserSelection(this);');
	    
	    div = document.createElement('div');
	    div.append(option);

		option = document.createElement('input');
		option.type = 'button';
		option.name = 'cancel_overwrite_btn';
		option.id = option.name;
		option.value = 'Cancel';
		option.setAttribute('onclick','processUserSelection(this)');

	    div.append(document.createElement('br'));
	    div.append(option);
	    
	    row.insertCell(3).appendChild(div);

		table.appendChild(tbody);
		document.getElementById('select_event_div').appendChild(table);
		
		break;		
		
	case 'LOAD_TEAMS':

		//var otherSquadWithoutSubs, player_ids;
		
		$('#team_selection_div').empty();
		document.getElementById('team_selection_div').style.display = 'none';
		
		if (dataToProcess)
		{
			if(dataToProcess.homeSquad.length <= 0 || dataToProcess.awaySquad.length <= 0) {
				if(dataToProcess.homeSquad.length <= 0) {
					alert(dataToProcess.homeTeam.teamName1 + ' has no players in the database');
				} else if(dataToProcess.awaySquad.length <= 0) {
					alert(dataToProcess.awayTeam.teamName1 + ' has no players in the database');
				}
				return false;
			}
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
			table.setAttribute('id', 'setup_teams');
			tr = document.createElement('tr');
			for (var j = 0; j <= 3; j++) {
			    th = document.createElement('th'); //column
			    switch (j) {
				case 0:
				    text = document.createTextNode(dataToProcess.homeTeam.teamName1); 
					break;
				case 1:
				    text = document.createTextNode(dataToProcess.homeTeam.teamName4 + ' captain'); 
					break;
				case 2:
				    text = document.createTextNode(dataToProcess.awayTeam.teamName1); 
					break;
				case 3:
				    text = document.createTextNode(dataToProcess.awayTeam.teamName4 + ' captain'); 
					break;
				}
				th.style.fontSize = '20px';
			    th.appendChild(text);
			    tr.appendChild(th);
			}
			
			thead = document.createElement('thead');
			thead.appendChild(tr);
			table.appendChild(thead);

			tbody = document.createElement('tbody');
			max_cols = parseInt(6 + parseInt($('#homeSubstitutesPerTeam option:selected').val()));
			if(parseInt($('#homeSubstitutesPerTeam option:selected').val()) < parseInt($('#awaySubstitutesPerTeam option:selected').val())) {
				max_cols = parseInt(6 + parseInt($('#awaySubstitutesPerTeam option:selected').val()));
			}else if(parseInt($('#homeSubstitutesPerTeam option:selected').val()) > parseInt($('#awaySubstitutesPerTeam option:selected').val())) {
				max_cols = parseInt(6 + parseInt($('#homeSubstitutesPerTeam option:selected').val()));
			}

			for(var i=0; i <= max_cols; i++) {
				
				row = tbody.insertRow(tbody.rows.length);
				
				for(var j = 0; j <= 3; j++) {
					
					addSelect = false;
					switch(j) {
					case 0: case 1:
						if(i <= parseInt(6 + parseInt($('#homeSubstitutesPerTeam option:selected').val()))) {
							addSelect = true;
						}
						break;
					case 2: case 3:
						if(i <= parseInt(6 + parseInt($('#awaySubstitutesPerTeam option:selected').val()))) {
							addSelect = true;
						}
						break;
					}

					if(addSelect == true) {

						select = document.createElement('select');
						select.style = 'width:75%';
						
						switch(j) {
						case 0: case 2:
						
							if(j == 0) {
								select.name = 'selectHomePlayers';
								select.id = 'homePlayer_' + (i + 1);
							} else if(j == 2) {
								select.name = 'selectAwayPlayers';
								select.id = 'awayPlayer_' + (i + 1);
							}
							if(j == 0) {
								dataToProcess.homeSquad.forEach(function(hp,index,arr){
									option = document.createElement('option');
									option.value = hp.playerId;
								    option.text = hp.jersey_number + ' - ' + hp.full_name;
								    select.appendChild(option);
								});
								dataToProcess.homeSubstitutes.forEach(function(hp,index,arr){
									option = document.createElement('option');
									option.value = hp.playerId;
								    option.text = hp.jersey_number + ' - ' + hp.full_name;
								    select.appendChild(option);
								});
								dataToProcess.homeOtherSquad.forEach(function(hs,index,arr){
									option = document.createElement('option');
									option.value = hs.playerId;
								    option.text = hs.jersey_number + ' - ' + hs.full_name;
								    select.appendChild(option);
								});
								
							} else if (j == 2) {
								
								dataToProcess.awaySquad.forEach(function(ap,index,arr){
									option = document.createElement('option');
									option.value = ap.playerId;
								    option.text = ap.jersey_number + ' - ' + ap.full_name;
								    select.appendChild(option);
								});
								dataToProcess.awaySubstitutes.forEach(function(ap,index,arr){
									option = document.createElement('option');
									option.value = ap.playerId;
								    option.text = ap.jersey_number + ' - ' + ap.full_name;
								    select.appendChild(option);
								});
								dataToProcess.awayOtherSquad.forEach(function(as,index,arr){
									option = document.createElement('option');
									option.value = as.playerId;
								    option.text = as.jersey_number + ' - ' + as.full_name;
								    select.appendChild(option);
								});
							}
						    select.selectedIndex = i;
							break;
						
						case 1: case 3:
						
							if(j == 1) {
								select.name = 'selectHomeCaptainGoalKeeper';
								select.id = 'homeCaptainGoalKeeper_' + (i + 1);
							} else {
								select.name = 'selectAwayCaptainGoalKeeper';
								select.id = 'awayCaptainGoalKeeper_' + (i + 1);
							}
							for(var k = 0; k <= 1; k++) {
								option = document.createElement('option');
								switch (k) {
								case 0:
									option.value = '';
								    option.text = '';
									break;
								case 1:
									option.value = 'captain';
								    option.text = 'Captain';
									break;
								}
							    select.appendChild(option);
							}
							if(i <= 6) {
								switch(j) {
								case 1:
									select.value = dataToProcess.homeSquad[i].captainGoalKeeper;
									break;
								case 3:
									select.value = dataToProcess.awaySquad[i].captainGoalKeeper;
									break;
								}
							}
							if(i > 6 && (i-7) <= dataToProcess.homeSubstitutes.length -1){
								switch(j) {
								case 1:
									select.value = dataToProcess.homeSubstitutes[i-7].captainGoalKeeper;
									break;
								}
							}
							if(i > 6 && (i-7) <= dataToProcess.awaySubstitutes.length -1){
								switch(j) {
								case 3:
									select.value = dataToProcess.awaySubstitutes[i-7].captainGoalKeeper;
									break;
								}
							}
							break;
						}
						select.style.fontSize = '30px';
						row.insertCell(j).appendChild(select);
						removeSelectDuplicates(select.id);
						$(select).select2();
					} else {
						select = document.createElement('label');
						select.style.fontSize = '30px';
						row.insertCell(j).appendChild(select);
					}
				}
			}
		
			table.appendChild(tbody);
			document.getElementById('team_selection_div').appendChild(table);
			document.getElementById('team_selection_div').style.display = '';
		} 
		break;
		
	case 'POPULATE-OFF_PLAYER':
		
		$('#select_player').empty();
		
		if(dataToProcess.homeTeamId ==  $('#select_teams option:selected').val()){
			dataToProcess.homeSquad.forEach(function(hs,index,arr){
				$('#select_player').append(
					$(document.createElement('option')).prop({
	                value: hs.playerId,
	                text: hs.jersey_number + ' - ' + hs.full_name
		        }))					
			});
		}
		else {
			dataToProcess.awaySquad.forEach(function(as,index,arr){
				$('#select_player').append(
					$(document.createElement('option')).prop({
	                value: as.playerId,
	                text: as.jersey_number + ' - ' + as.full_name
		        }))					
			});
		}
		break;
		
	case 'POPULATE-ON_PLAYER':
		
		$('#select_sub_player').empty();
		if(dataToProcess.homeTeamId ==  $('#select_teams option:selected').val()){
			dataToProcess.homeSubstitutes.forEach(function(hsub,index,arr){
				$('#select_sub_player').append(
					$(document.createElement('option')).prop({
	                value: hsub.playerId,
	                text: hsub.jersey_number + ' - ' + hsub.full_name
		        }))					
			});
		}
		else {
			dataToProcess.awaySubstitutes.forEach(function(asub,index,arr){
				$('#select_sub_player').append(
					$(document.createElement('option')).prop({
	                value: asub.playerId,
	                text: asub.jersey_number + ' - ' + asub.full_name
		        }))					
			});
		}
		break;
				
	case 'LOAD_REPLACE':
		
		$('#select_event_div').empty();
		
		table = document.createElement('table');
		table.setAttribute('class', 'table table-bordered');
				
		tbody = document.createElement('tbody');
		row = tbody.insertRow(tbody.rows.length);
		
		select = document.createElement('select');
		select.id = 'select_teams';
		select.name = select.id;
		
		option = document.createElement('option');
		option.value = dataToProcess.homeTeamId;
		option.text = dataToProcess.homeTeam.teamName1;
		select.appendChild(option);
		
		option = document.createElement('option');
		option.value = dataToProcess.awayTeamId;
		option.text = dataToProcess.awayTeam.teamName1;
		select.appendChild(option);
		
		header_text = document.createElement('label');
		header_text.innerHTML = 'Teams: '
		header_text.htmlFor = select.id;
		select.setAttribute('onchange',"processUserSelection(this)");
		row.insertCell(0).appendChild(header_text).appendChild(select);
		
		select = document.createElement('select');
		select.id = 'select_player';
		select.name = select.id;
		
		header_text = document.createElement('label');
		header_text.innerHTML = 'Player: '
		header_text.htmlFor = select.id;
		row.insertCell(1).appendChild(header_text).appendChild(select);

	    select = document.createElement('select');
		select.id = 'select_sub_player';
		select.name = select.id;
		
		header_text = document.createElement('label');
		header_text.innerHTML = 'Sub-Player: '
		header_text.htmlFor = select.id;
		row.insertCell(2).appendChild(header_text).appendChild(select);
		
	    div = document.createElement('div');

	    option = document.createElement('input');
	    option.type = 'button';
	    option.name = 'log_replace_btn';
	    option.id = option.name;
	    option.value = 'Replace Player';
	    option.setAttribute('onclick','processUserSelection(this);');
	    
	    div.append(option);

		option = document.createElement('input');
		option.type = 'button';
		option.name = 'cancel_replace_btn';
		option.id = option.name;
		option.value = 'Cancel';
		option.setAttribute('onclick','processUserSelection(this)');

	    div.append(document.createElement('br'));
	    div.append(option);

	    row.insertCell(3).appendChild(div);

		table.appendChild(tbody);
		document.getElementById('select_event_div').appendChild(table);
		
		break;
		
	case 'LOAD_UNDO':

		$('#select_event_div').empty();
		
		if(dataToProcess.events.length > 0) {

			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
			row = tbody.insertRow(tbody.rows.length);
			
			select = document.createElement('select');
			select.id = 'select_undo';
			dataToProcess.events = dataToProcess.events.reverse();
			var max_loop = dataToProcess.events.length;
			if(max_loop > 5) {
				max_loop = 5;
			}
			for(var i = 0; i < max_loop; i++) {
				option = document.createElement('option');
				option.value = dataToProcess.events[i].eventNumber;
			    option.text = dataToProcess.events[i].eventNumber + '. ' + dataToProcess.events[i].eventType;
			    select.appendChild(option);
			}
			header_text = document.createElement('label');
			header_text.innerHTML = 'Last Five Events: '
			header_text.htmlFor = select.id;
			row.insertCell(0).appendChild(header_text).appendChild(select);

		    option = document.createElement('input');
		    option.type = 'text';
		    option.name = 'number_of_undo_txt';
		    option.value = '1';
		    option.id = option.name;
		    option.setAttribute('onblur','processUserSelection(this)');
			header_text = document.createElement('label');
			header_text.innerHTML = 'Number of undos: '
			header_text.htmlFor = option.id;
			row.insertCell(1).appendChild(header_text).appendChild(option);
			
		    div = document.createElement('div');

		    option = document.createElement('input');
		    option.type = 'button';
		    option.name = 'log_undo_btn';
		    option.id = option.name;
		    option.value = 'Undo Last Event';
		    option.setAttribute('onclick','processUserSelection(this);');
		    
		    div.append(option);

			option = document.createElement('input');
			option.type = 'button';
			option.name = 'cancel_undo_btn';
			option.id = option.name;
			option.value = 'Cancel';
			option.setAttribute('onclick','processUserSelection(this)');

		    div.append(document.createElement('br'));
		    div.append(option);

		    row.insertCell(2).appendChild(div);

			table.appendChild(tbody);
			document.getElementById('select_event_div').appendChild(table);

		} else {
			return false;
		}
		
		break;
		
	case 'LOAD_RAIDER':

		$('#select_event_div').empty();
		
		table = document.createElement('table');
		table.setAttribute('class', 'table table-bordered');
				
		tbody = document.createElement('tbody');
		row = tbody.insertRow(tbody.rows.length);
		
		select = document.createElement('select');
		select.id = 'select_teams';
		select.name = select.id;
		
		option = document.createElement('option');
		option.value = dataToProcess.homeTeamId;
		option.text = dataToProcess.homeTeam.teamName1;
		select.appendChild(option);
		
		option = document.createElement('option');
		option.value = dataToProcess.awayTeamId;
		option.text = dataToProcess.awayTeam.teamName1;
		select.appendChild(option);
		
		header_text = document.createElement('label');
		header_text.innerHTML = 'Teams: '
		header_text.htmlFor = select.id;
		select.setAttribute('onchange',"processUserSelection(this)");
		row.insertCell(0).appendChild(header_text).appendChild(select);
		
		select = document.createElement('input');
		select.type = 'checkbox';
		select.id = 'select_bonus';

		header_text = document.createElement('label');
		header_text.innerHTML = 'Bonus: '
		header_text.htmlFor = select.id;
		select.setAttribute('onchange',"processUserSelection(this)");
		row.insertCell(1).appendChild(header_text).appendChild(select);
		
		select = document.createElement('input');
		select.type = 'checkbox';
		select.id = 'select_super_tackel';
		
		header_text = document.createElement('label');
		header_text.innerHTML = 'Super Tackel: '
		header_text.htmlFor = select.id;
		select.setAttribute('onchange',"processUserSelection(this)");
		row.insertCell(2).appendChild(header_text).appendChild(select);
		
		select = document.createElement('input');
		select.type = 'checkbox';
		select.id = 'select_super_raid';
		
		header_text = document.createElement('label');
		header_text.innerHTML = 'Super Raid: '
		header_text.htmlFor = select.id;
		select.setAttribute('onchange',"processUserSelection(this)");
		row.insertCell(3).appendChild(header_text).appendChild(select);
		
		
		select = document.createElement('select');
		select.id = 'select_All_Out';
		select.name = select.id;
		
		option = document.createElement('option');
		option.value = "0";
		option.text = "";
		select.appendChild(option);
		
		option = document.createElement('option');
		option.value = dataToProcess.homeTeamId;
		option.text = dataToProcess.homeTeam.teamName1;
		select.appendChild(option);
		
		option = document.createElement('option');
		option.value = dataToProcess.awayTeamId;
		option.text = dataToProcess.awayTeam.teamName1;
		select.appendChild(option);
		
		header_text = document.createElement('label');
		header_text.innerHTML = 'All Out: '
		header_text.htmlFor = select.id;
		select.setAttribute('onchange',"processUserSelection(this)");
		row.insertCell(4).appendChild(header_text).appendChild(select);
		
		select = document.createElement('input');
		select.type = 'checkbox';
		select.id = 'select_success_UnSuccess';
		
		header_text = document.createElement('label');
		header_text.innerHTML = 'Raid UnSuccessful: '
		header_text.htmlFor = select.id;
		select.setAttribute('onchange',"processUserSelection(this)");
		row.insertCell(5).appendChild(header_text).appendChild(select);
		
		select = document.createElement('select');
		select.id = 'select_Touches';
		select.name = select.id;
		
		for(var ibound = 0; ibound <= 7; ibound++) 
		{
	    	option = document.createElement('option');
			
			option.value = ibound;
			option.text = ibound;
		    
		    select.appendChild(option);
		}
		
		header_text = document.createElement('label');
		header_text.innerHTML = 'No. Of Touches: '
		header_text.htmlFor = select.id;
		select.setAttribute('onchange',"processUserSelection(this)");
		row.insertCell(6).appendChild(header_text).appendChild(select);
		
		
	    div = document.createElement('div');

		option = document.createElement('input');
	    option.type = 'button';
	    option.name = 'log_raider_btn';
	    option.id = option.name;
	    option.value = 'Log Raider';
	    option.setAttribute('onclick','processUserSelection(this);');
        
	    div.append(option);

		option = document.createElement('input');
		option.type = 'button';
		option.name = 'cancel_undo_btn';
		option.id = option.name;
		option.value = 'Cancel';
		option.setAttribute('onclick','processUserSelection(this)');

	    div.append(document.createElement('br'));
	    div.append(option);

	    row.insertCell(7).appendChild(div);

		table.appendChild(tbody);
		document.getElementById('select_event_div').appendChild(table);

		break;	
	
	case 'LOAD_EVENTS':
		
		$('#select_event_div').empty();

		header_text = document.createElement('label');
		header_text.id = 'selected_team_name';
		header_text.innerHTML = '';
		document.getElementById('select_event_div').appendChild(header_text);
		
		table = document.createElement('table');
		table.setAttribute('class', 'table table-bordered');
				
		tbody = document.createElement('tbody');
		
		for(var iRow = 0;iRow <= 0;iRow++) {
			
			row = tbody.insertRow(tbody.rows.length);
			max_cols = 4;
			
			for(var iCol = 0;iCol <= max_cols;iCol++) {
				
				cell = row.insertCell(iCol);
				
				option = document.createElement('input');
				option.type = 'button';
				option.name = 'log_event_btn';
				
				switch (iRow) {
				case 0:
					
					switch (iCol) {
					case 0:
						option.id = 'points';
						option.value = 'Points';
						option.style.fontSize = '18px';
						option.style.width = '150px';
						option.style.height = '30px';
						break;
					case 1:
						option.id = 'replace';
						option.value = 'Replace';
						option.style.fontSize = '18px';
						option.style.width = '150px';
						option.style.height = '30px';
						break;
					case 2:
						option.id = 'undo';
						option.value = 'Undo';
						option.style.fontSize = '18px';
						option.style.width = '150px';
						option.style.height = '30px';
						break;
					case 3:
						option.id = 'overwrite';
						option.value = 'Overwrite';
						option.style.fontSize = '18px';
						option.style.width = '150px';
						option.style.height = '30px';
						break;
					/*case 4:
						option.id = 'raider';
						option.value = 'Raider';
						option.style.width = '120px';
						break;	*/
					case 4:
						option.name = 'cancel_event_btn';
						option.id = option.name;
						option.value = 'Cancel';
						option.style.fontSize = '18px';
						option.style.width = '150px';
						option.style.height = '30px';
						break;	
					}
					
					break;
					
				}
				
				if(option.id) {
					
					switch (option.id) {
					case 'overwrite': case 'points':
						
						option.setAttribute('data-toggle', 'dropdown');
						option.setAttribute('aria-haspopup', 'true');
						option.setAttribute('aria-expanded', 'false');					
						
						div = document.createElement('div');
					    div.append(option);
					    div.className='dropdown';
					    
					    linkDiv = document.createElement('div');
					    linkDiv.id = option.id + '_div';
					    linkDiv.className='dropdown-menu';
					    linkDiv.setAttribute('aria-labelledby',option.id);

						switch (option.id) {
						case 'stats':
					
							for(var ibound = 1; ibound <= 8; ibound++) 
							{
						    	anchor = document.createElement('a');
							    anchor.className = 'btn btn-success';
			
								switch(ibound) {
								case 1:
								    anchor.id = 'off_side';
								    anchor.innerText = 'Off Side';
									break;
								case 2:
								    anchor.id = 'assists';
								    anchor.innerText = 'Assists';
									break;
								}
								switch(ibound){
									case 1: case 2: case 3: case 4: case 5: case 6: case 7:
										 anchor.setAttribute('onclick','processWaitingButtonSpinner("START_WAIT_TIMER");processKabaddiProcedures("LOG_EVENT",this);');
										break;
								}
							    anchor.style = 'display:block;';
							    linkDiv.append(anchor);
							}
							break;
							
						case 'overwrite': 
							
							for(var ibound = 1; ibound <= 1; ibound++) 
							{
						    	anchor = document.createElement('a');
							    anchor.className = 'btn btn-success';
			
							    switch(ibound) {
								case 1:
								    anchor.id = 'overwrite_team_score';
								    anchor.innerText = 'Team Score';
								    anchor.setAttribute('onclick','addItemsToList("LOAD_OVERWRITE_TEAMS_SCORE",this);');
									break;
								/*case 2:
								    anchor.id = 'overwrite_match_stats';
								    anchor.innerText = 'Match Stats';
								    anchor.setAttribute('onclick','addItemsToList("LOAD_OVERWRITE_MATCH_STATS",this);');
									break;*/
								}
							    
							    anchor.style = 'display:block;';
							    linkDiv.append(anchor);
							}
							break;
						
						case 'points':
						
							for(var ibound = 0; ibound <= 15; ibound++) 
							{
						    	anchor = document.createElement('a');
							    anchor.className = 'btn btn-success';
								
								anchor.id = ibound;
								anchor.innerText = ibound;
							    
							    anchor.setAttribute('onclick','processWaitingButtonSpinner("START_WAIT_TIMER");processKabaddiProcedures("LOG_EVENT",this);');
							    anchor.style = 'display:block;';
							    linkDiv.append(anchor);
							}
							break;	
								
						case 'card':
							
							for(var ibound = 1; ibound <= 2; ibound++) 
							{
						    	anchor = document.createElement('a');
							    anchor.className = 'btn btn-success';
								
								if(ibound == 1) {
								    anchor.id = 'yellow';
								    anchor.innerText = 'Yellow Card';
							    } else if(ibound == 2) {
								    anchor.id = 'red';
								    anchor.innerText = 'Red Card';
							    }
							    
							    anchor.setAttribute('onclick','processWaitingButtonSpinner("START_WAIT_TIMER");processKabaddiProcedures("LOG_EVENT",this);');
							    anchor.style = 'display:block;';
							    linkDiv.append(anchor);
							}
							break;
						}
					    div.append(linkDiv);				    
						cell.append(div);
						break;
						
					default:
					
						option.onclick = function() {processUserSelection(this)};
						cell.appendChild(option);
						
						break;
					
					}
				}
			}
		}
			
		table.appendChild(tbody);
		document.getElementById('select_event_div').appendChild(table);

		break;
				
	case 'LOAD_MATCH':
		
		$('#kabaddi_div').empty();
		
		if (dataToProcess)
		{
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
			tbody = document.createElement('tbody');

			table.appendChild(tbody);
			document.getElementById('kabaddi_div').appendChild(table);

			row = tbody.insertRow(tbody.rows.length);
			header_text = document.createElement('h6');
			header_text.id = 'match_time_hdr';
			header_text.innerHTML = 'Match Time: 00:00:00';
			header_text.style.fontSize = '15px';
			row.insertCell(0).appendChild(header_text);
			
			if(dataToProcess.events != null && dataToProcess.events.length > 0) {
				max_cols = dataToProcess.events.length;
				if (max_cols > 20) {
					max_cols = 20;
				}
				header_text = document.createElement('h6');
				for(var i = 0; i < max_cols; i++) {
					if(dataToProcess.events[(dataToProcess.events.length - 1) - i].eventTeamId != 0){
						if(dataToProcess.events[(dataToProcess.events.length - 1) - i].eventTeamId == dataToProcess.homeTeamId){
							teamName = ' {'+ dataToProcess.homeTeam.teamName4 +'}' ;
						}
										
						if(dataToProcess.events[(dataToProcess.events.length - 1) - i].eventTeamId == dataToProcess.awayTeamId){
							teamName = ' {'+ dataToProcess.awayTeam.teamName4 +'}';
						}				
					}else{
						teamName = '';
					}
					
					if(dataToProcess.events[(dataToProcess.events.length - 1) - i].eventType) {
						if(header_text.innerHTML) {
							header_text.innerHTML = header_text.innerHTML + ', ' + dataToProcess.events[(dataToProcess.events.length - 1) - i].eventType.replaceAll('_',' ') + teamName; // .match(/\b(\w)/g).join('')
						} else {
							header_text.innerHTML = dataToProcess.events[(dataToProcess.events.length - 1) - i].eventType.replaceAll('_',' ') + teamName; // .match(/\b(\w)/g).join('')
						}
					}
				}
				header_text.innerHTML = 'Events: ' + header_text.innerHTML;
				header_text.style.fontSize = '15px';
				row.insertCell(1).appendChild(header_text);
			}

			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
			//table.style.border = '5px solid black';
			
			tbody = document.createElement('tbody');
			row = tbody.insertRow(tbody.rows.length);
			Select = document.createElement('label');
			Select.innerHTML = 'MAIN FILE';
			Select.style.fontSize = '22px';
		    row.insertCell(0).appendChild(Select);
			table.appendChild(tbody);
			document.getElementById('kabaddi_div').appendChild(table);
			
			//Teams Score and other details
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
			table.style.border = '10px solid black';
			tbody = document.createElement('tbody');
			for(var i = 0; i < 1; i++){
				row = tbody.insertRow(tbody.rows.length);
				//row.style.border = '3px solid black';
				for (var j = 0; j <= 1; j++) {
				    anchor = document.createElement('a');
				    
				    switch (j) {
					case 0:
					    anchor.name = 'homeTeam';
					    anchor.id = 'homeTeam';
						anchor.value = dataToProcess.homeTeamId;
						anchor.innerHTML = dataToProcess.homeTeam.teamName1 + ': ' + dataToProcess.homeTeamScore ;
						anchor.style.fontSize = '30px';
						anchor.style.color = 'green';
						/*if(dataToProcess.homeTeamScore !== null && dataToProcess.homeTeamScore !== undefined && dataToProcess.api_Match.homeTeamScore !== null 
								 && dataToProcess.api_Match.homeTeamScore !== undefined){
							if(dataToProcess.homeTeamScore != dataToProcess.api_Match.homeTeamScore){
								anchor.style.color = 'red';
							}else if(dataToProcess.homeTeamScore == dataToProcess.api_Match.homeTeamScore){
								anchor.style.color = 'green';
							}
						}*/
						
						break;	
					case 1:
						anchor.name = 'awayTeam';
						anchor.id = 'awayTeam';
						anchor.value = dataToProcess.awayTeamId;
						anchor.innerHTML = dataToProcess.awayTeam.teamName1 + ': ' + dataToProcess.awayTeamScore ;
						anchor.style.fontSize = '30px';
						anchor.style.color = 'green';
						/*if(dataToProcess.awayTeamScore !== null && dataToProcess.awayTeamScore !== undefined && dataToProcess.api_Match.awayTeamScore !== null 
								 && dataToProcess.api_Match.awayTeamScore !== undefined){
							if(dataToProcess.awayTeamScore != dataToProcess.api_Match.awayTeamScore){
								anchor.style.color = 'red';
							}else if(dataToProcess.awayTeamScore == dataToProcess.api_Match.awayTeamScore){
								anchor.style.color = 'green';
							}
						}*/
						
						break;
					}
					anchor.setAttribute('onclick','processUserSelection(this);');
					anchor.style.cursor = 'pointer';
				    row.insertCell(j).appendChild(anchor);
				}
			}
			//tbody.appendChild(tr);
			table.appendChild(tbody);
			document.getElementById('kabaddi_div').appendChild(table);
			
			
			/*table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
			//table.style.border = '5px solid black';
			tbody = document.createElement('tbody');
			row = tbody.insertRow(tbody.rows.length);
			Select = document.createElement('label');
			Select.innerHTML = 'API';
			Select.style.fontSize = '22px';
		    row.insertCell(0).appendChild(Select);
			table.appendChild(tbody);
			document.getElementById('kabaddi_div').appendChild(table);*/
			
			
			
			/*// API SCORES
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
			table.style.border = '10px solid black';
			tbody = document.createElement('tbody');
			for(var i = 0; i < 1; i++){
				row = tbody.insertRow(tbody.rows.length);
				for (var j = 0; j <= 1; j++) {
				    Select = document.createElement('label');
				    switch (j) {
					case 0:
					    Select.name = 'apihomeTeam';
					    Select.id = 'apihomeTeam';
						Select.value = dataToProcess.homeTeamId;
						Select.innerHTML = dataToProcess.homeTeam.teamName1 + ': ' + dataToProcess.api_Match.homeTeamScore;
						Select.style.fontSize = '30px';
						break;
					case 1:
						Select.name = 'apiawayTeam';
						Select.id = 'apiawayTeam';
						Select.value = dataToProcess.awayTeamId;
						Select.innerHTML = dataToProcess.awayTeam.teamName1 + ': ' + dataToProcess.api_Match.awayTeamScore;
						Select.style.fontSize = '30px';
						break;	
					}
					Select.setAttribute('onclick','processUserSelection(this);');
				    row.insertCell(j).appendChild(Select);
				}
			}
			
			
			table.appendChild(tbody);
			document.getElementById('kabaddi_div').appendChild(table);	*/		
		}
		break;
	}
}
function removeSelectDuplicates(select_id)
{
	var this_list = {};
	$("select[id='" + select_id + "'] > option").each(function () {
	    if(this_list[this.text]) {
	        $(this).remove();
	    } else {
	        this_list[this.text] = this.value;
	    }
	});
}
function checkEmpty(inputBox,textToShow) {

	var name = $(inputBox).attr('id');
	
	document.getElementById(name + '-validation').innerHTML = '';
	document.getElementById(name + '-validation').style.display = 'none';
	$(inputBox).css('border','');
	if(document.getElementById(name).value.trim() == '') {
		$(inputBox).css('border','#E11E26 2px solid');
		document.getElementById(name + '-validation').innerHTML = textToShow + ' required';
		document.getElementById(name + '-validation').style.display = '';
		document.getElementById(name).focus({preventScroll:false});
		return false;
	}
	return true;	
}	
