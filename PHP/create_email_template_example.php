<?php

// Login details are censored.

$servername = "XXXXX";
$username = "XXXXX";
$password = "XXXXX";
$dbname = "XXXXX";

if (isset($_POST['submit'])) {
    // This is a submission.

	$userID = get_user_id_for_censored();
	$template_name = $_POST['template_name'];
	$text_input = $_POST['text_input'];
	
	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);
	// Check connection
	if ($conn->connect_error) {
  		echo "\n<p>Connection failed: $conn->connect_error</p>";
	} else {
		$sql = "SELECT name from censoreddb.email_templates WHERE name = '$template_name'";
		$result = $conn->query($sql);
		if ($result->num_rows > 0) {
			$sql = "UPDATE censoreddb.email_templates SET template = '$text_input' WHERE name = '$template_name'";
			echo "\n<p>$sql</p>";
			$result = $conn->query($sql);
		} else {
			$sql = "INSERT INTO censoreddb.email_templates (name, template, author) VALUES ('$template_name', '$text_input', '$userID')";
			echo "\n<p>$sql</p>";
			$result = $conn->query($sql);
		}
		$conn->close();
		echo "\n<p>Created template $template_name.</p>";
	}
} else {
    // This is not a submission.

	$templates = array();
	$institutions = array();
	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);
	// Check connection
	if ($conn->connect_error) {
  		echo "\n<p>Connection failed: $conn->connect_error</p>";
	} else {
		$sql = "SELECT * FROM censoreddb.institution WHERE status = 1 AND deleted <> 1 AND call_num IS NOT NULL";
		$result = $conn->query($sql);
		if ($result->num_rows > 0) {
			while($row = $result->fetch_assoc()) {
				array_push($institutions, $row);
			}
		}
		$sql = "SELECT name, template FROM censoreddb.email_templates";
		$result = $conn->query($sql);
		if ($result->num_rows > 0) {
			while($row = $result->fetch_assoc()) {
				array_push($templates, $row);
			}
		}
		$conn->close();
	}
?>
		<style>/* Style the tab */
            .tab {
                overflow: hidden;
                border: 1px solid #ccc;
                background-color: #f1f1f1;
                width: 1840;
            }

            /* Style the buttons that are used to open the tab content */
            .tab button {
                background-color: inherit;
                float: left;
                border: none;
                outline: none;
                cursor: pointer;
                padding: 14px 16px;
                transition: 0.3s;
            }

            /* Change background color of buttons on hover */
            .tab button:hover {
                background-color: #ddd;
            }

            /* Create an active/current tablink class */
            .tab button.active {
                background-color: #ccc;
            }

            /* Style the tab content */
            .tabcontent {
                display: none;
                padding: 6px 12px;
                border: 1px solid #ccc;
                border-top: none;
                width: 1100px;
                height: 100%;
            }

            .column {
                float: left;
                width: 33%;
            }

            .rcolumn {
                float: right;
                width: 33%;
            }

            /* Clear floats after the columns */
            .row:after {
                content: "";
                display: table;
                clear: both;
            }

            nav ul{height:200px; width:80%;}
            nav ul{overflow:hidden; overflow-y:scroll;}

            .vertical-center {
                margin: 0;
                position: relative;
                top: 50%;
                -ms-transform: translateY(-50%);
                transform: translateY(-50%);
            }

            .horizontal-center {
                margin: 0;
                position: relative;
                left: 33%;
                -ms-transform: translateX(-33%);
                transform: translateX(-33%);
            }

            .importantlink {
            }

            a.importantlink:link, a.importantlink:visited {
                background-color: #f44336;
                color: white;
                padding: 14px 25px;
                text-align: center;
                text-decoration: none;
                display: inline-block;
            }

            a.importantlink:hover, a.importantlink:active {
                background-color: red;
            }
			#text_input {
			  	white-space: pre-line;
				border-color: green;
				border:1px solid;
				background-color: darkgreen;
				color: white;
				font-size: 12px;
                font-weight: bold;
			}
			.error_item {
				color: red;
			}
			input[type="text"] {
				border:1px solid green;
			}
            table.scrolldown {
                width: 100%;
    
                /* border-collapse: collapse; */
                border-spacing: 0;
                border: 2px solid black;
                height: 400px;
            }
    
            /* To display the block as level element */
            table.scrolldown tbody,
            table.scrolldown thead {
                display: block;
            }
    
            thead tr th {
                height: 40px;
                line-height: 40px;
            }
    
            table.scrolldown tbody {
    
                /* Set the height of table body */
                height: 400px;
    
                /* Set vertical scroll */
                overflow-y: auto;
    
                /* Hide the horizontal scroll */
                overflow-x: hidden;
            }
    
            tbody {
                border-top: 2px solid black;
                height: 100%;
            }
    
            tbody td,
            thead th {
                border-right: 2px solid black;
            }
            th, td {
                max-width: 150px;
                width: 150px;
                min-width: 150px;
                text-align: center;
            }
            #url_head {
                max-width: 40px;
                width: 40px;
                min-width: 40px;
                text-align: center;
            }
            td.url_th,
            td.url_td {
                max-width: 40px;
                width: 40px;
                min-width: 40px;
                text-align: center;
            }
            #status_head {
                width: 70px;
                min-width: 70px;
                text-align: center;
            }
            th.bit_td,
            td.bit_td {
                width: 70px;
                min-width: 70px;
                text-align: center;
            }
            th.medium_td,
            td.medium_td {
                width: 100px;
                min-width: 100px;
                text-align: center;
            }
            th.long_td,
            td.long_td {
                width: 250px;
                min-width: 250px;
                text-align: center;
            }
			td {
				font-size: 11px;
				color: black;
			}
			td a {
				font-size: 11px;
                font-weight: bold;
				color: blue;
				text-decoration: underline;
			}
			
			.modal {
  				display: none; /* Hidden by default */
  				position: fixed; /* Stay in place */
  				z-index: 1; /* Sit on top */
  				padding-top: 100px; /* Location of the box */
  				left: 0;
  				top: 0;
  				width: 100%; /* Full width */
  				height: 100%; /* Full height */
  				overflow: auto; /* Enable scroll if needed */
  				background-color: rgb(0,0,0); /* Fallback color */
  				background-color: rgba(0,0,0,0.9); /* Black w/ opacity */
			}
			.modal-content, #caption {  
  				-webkit-animation-name: zoom;
  				-webkit-animation-duration: 0.6s;
  				animation-name: zoom;
 				 animation-duration: 0.6s;
			}

			@-webkit-keyframes zoom {
  				from {-webkit-transform:scale(0)} 
  				to {-webkit-transform:scale(1)}
			}

			@keyframes zoom {
  				from {transform:scale(0)} 
  				to {transform:scale(1)}
			}
			
			.close {
  				position: absolute;
  				top: 15px;
  				right: 35px;
  				color: #f1f1f1;
  				font-size: 40px;
  				font-weight: bold;
  				transition: 0.3s;
				}

			.close:hover,
			.close:focus {
  				color: #bbb;
  				text-decoration: none;
  				cursor: pointer;
			}

			/* 100% Image Width on Smaller Screens */
			@media only screen and (max-width: 700px){
  				.modal-content {
    				width: 100%;
  				}
			}
		</style>
		<div id="myModal" class="modal">
		  	<span id='example_close' class="close">&times;</span>
		  	<img class="modal-content" id="img01">
		  	<div id="caption"></div>
		</div>
		<p>
			Note to Tony: This is not part of the original specification. It is experimental and will be eliminated if you do not want it. The idea is to allow you to create email templates whose text can be derived from the Institution table. The purpose is to allow you to send bulk emails to inform clients of website related changes. For example, inform them that the site has been updated and users are required to set new passwords.
		</p>
        <form id="template_form" action="http://CENSORED_URL" target="dummyframe" method="post">
			<input type="hidden" id="submission_type" name="submission_type" value="template">
            <div style="padding-left: 10px; padding-right: 10px; width: 1100px;">
                <h3 class="misc_row" style="color:green;">
                    Edit Email Template Text
                </h3>
				<div class="misc_row" style="border:1px solid green; height: 40px; align-items: center; justify-content: left; margin-top: 5px; background-color: #E7FFE7;">
					<input style="float:left" type="radio" id="mode_template" class="tablinks" name="mode" value="template" onclick='selectTab("Create")' checked/>
					<label style="float:left" for="html">Create Templates</label>
					<input style="float:left" type="radio" id="mode_email" class="tablinks" name="mode" value="email" onclick='selectTab("Send")' />
					<label style="float:left" for="css">Send Emails</label>
				</div>
				<div class="misc_row" style="border:1px solid green; height: 50px;align-items: center; justify-content: left; background-color: #E7FFE7;">
					<span>Select template</span>
					<select id="template_selector" style="width:max-content;" onChange="templateSelect()">
					</select>
                	<span>or enter a name: </span>
                	<input type="text" id="template_name" name="template_name" size='10' maxlength='30' required/>
					<span id="emaillist_prompt1" style="visibility:hidden;">Select email list</span>
					<select id="emaillist_selector" style="width:max-content; visibility:hidden;" onChange="templateSelect()">
					</select>
                	<span id="emaillist_prompt2" style="visibility:hidden;">or enter a name: </span>
                	<input type="text" id="emaillist_name" style="visibility:hidden;" name="template_name" size='10' maxlength='30'/>
                	<input id="submit" style="float:right; visibility:hidden;" type="submit" name="submit" value="Submit">
				</div>
				<div id='Create' class='tabcontent' style='float: left;'>
                	<div class="misc_row" style="margin-bottom: 20px; height: 300px;">
						<h6 style="color:green;">Template</h6>
                    	<textarea class="textentry" id='text_input' name='text_input' cols="80" rows="12" placeholder="Type your email template here..." required></textarea>
                    	<div style='float: right; width:300px; height:300px;'>
							<div>
								<h6 style="color:green;">How too...</h6>
								<input type="button" onclick="showExample();" value="Show Example Template"/>
								<br>
								<h6 style="color:green;">Warnings are listed here...</h6>
								<p id='balanced_indicator' style='color:red; visiblity: hidden;'>CURLY BRACKETS ARE UNBLANCED</p>
								<p id='invalid_nestings_title' style='color:red; visiblity: hidden;'>INVALID NESTINGS IN TEXT</p>
								<ul id="invalid_nestings_list">
								</ul>						
								<p id='invalid_fields_title' style='color:red; visiblity: hidden;'>INVALID FIELDS IN TEXT</p>
								<ul id="invalid_field_list">
								</ul>							
							</div>
                    	</div>
                	</div>
					<div class="misc_row" style="margin-top: 0px; margin-left: 200px; height: 40px; display: flex; justify-content: left;">
						<button type="button" onclick="displayTemplateAppliedToFirstInstitution()">First</button>&nbsp;&nbsp;
						<button type="button" onclick="displayTemplateAppliedToPreviousInstitution()">Previous</button>&nbsp;&nbsp;
						<button type="button" onclick="displayTemplateAppliedToNextInstitution()">Next</button>&nbsp;&nbsp;
						<button type="button" onclick="displayTemplateAppliedToLastInstitution()">Last</button>
					</div>
					<div class="misc_row" style="margin-top: 20px; height: 600px;">
						<h6 style="color:green;">As Seen by Recipient</h6>
                    	<!-- <textarea id='text_output' cols="80" rows="12" readonly></textarea> -->
						<div id='text_output' style='border-style: solid; border-width: 4px; border-color: green; margin-left: 4:px; padding-left: 4:px;'></div>
                    	<div style='float: right; width:300px; height:300px;'>
							<div>
								<h6 style="color:green;">Sample Filters...</h6>
								<span>CU Type</span>
								<select id="select_chart_type" class="filter" style="width:max-content;" onChange="chartTypeSelect()">
								</select>
								<span>League ID</span>
								<select id="select_leagueid" class="filter" style="width:max-content;" onChange="leagueIDSelect()">
								</select>
								<span>Parent Call Number</span>
								<select id="select_parent_call_num" class="filter" style="width:max-content;" onChange="parentCallNumSelect()">
								</select>
							</div>
							<h6 style="color:green;">Select your sample...</h6>
							<div id='create_table_holder' style="padding-right: 10px;">
								<div id="institutions_div" style="display: block; max-width: 200px; padding-right: 10px;">
								</div>
            				</div>
                    	</div>
					</div>
				</div>
				<div id='Send' class='tabcontent' style='float: left;'>
					<div class='column'>
						<h6 style="color:green;">Template</h6>
						<textarea id='email_template' cols="40" rows="20" style="padding-left: 5px; padding-right: 5px;" readonly></textarea>
					</div>
					<div class='column'>
						<h6 style="color:green;">Email List</h6>
						<div id='edit_table_holder' style="padding-right: 10px;">
						</div>
					</div>
					<div class='column'>
						<h6 style="color:green;">Email Operations</h6>
						<div>
							<input type="hidden" id="emaillist_contents" name="emaillist_contents" value="">
							<input type='button' id='create_emaillist_button' value='Creat Email List' onclick='createEmailList();'/>
							<span id='email_list_info_span'></span>
							<br>
							<input type="checkbox" id='save_email_list' name='save_email_list'>
							<label for='save_email_list'>Save email list when submitted?</label>
							<br>
							<input type="checkbox" id='use_html' name='use_html'>
							<label for='use_html'>Email contents should be in HTML?</label>
							<div style='border-style: solid; border-width: 4px; border-color: green; margin-left: 4:px; padding-left: 4:px;'>
								<h6 style="color:green; margin-left: 4:px; padding-left: 4:px;">Send Test Email Only</h6>
								<input type="checkbox" id='test_email_only' name='test_email_only'>
								<label for='use_html'>Send test email only?</label>
								<br>
								<span>Test email address:</span>
								<input type="email" id="test_email_address" name="test_email_address" size='16' maxlength='30'/>
							</div>
						</div>
					</div>
				</div>
            </div>
        </form>
		<iframe name="dummyframe" id="dummyframe" style="display: none;"></iframe>
        <script>
            const validatePattern = /{[\w_]+}/ig;
			const urlPattern = /(https:\/\/www\.|http:\/\/www\.|https:\/\/|http:\/\/)?[a-zA-Z]{2,}(\.[a-zA-Z]{2,})(\.[a-zA-Z]{2,})?\/[a-zA-Z0-9]{2,}|((https:\/\/www\.|http:\/\/www\.|https:\/\/|http:\/\/)?[a-zA-Z]{2,}(\.[a-zA-Z]{2,})(\.[a-zA-Z]{2,})?)|(https:\/\/www\.|http:\/\/www\.|https:\/\/|http:\/\/)?[a-zA-Z0-9]{2,}\.[a-zA-Z0-9]{2,}\.[a-zA-Z0-9]{2,}(\.[a-zA-Z0-9]{2,})?/g;
			const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/ig;
			const linkPattern = /{link:[^}]*}/ig;
			const linePattern = /.+\n*/ig;
			const institutions = <?php echo json_encode($institutions); ?> ;
			var filters = {};
			var templates = <?php echo json_encode($templates); ?> ;
			var template = null;
			var chartTypes = [];
			var leagueIDs = [];
			var parentCallNums = [];
			var selectedChartType = null;
			var selectedLeagueID = null;
			var selectedParentCallNum = null;
			var currentInstitutionIndex = 0;
			var preselectTab = 'Create';
			var modal = document.getElementById("myModal");
			
			function showExample() {
				let modalImg = document.getElementById("img01");
				modal.style.display = "block";
				modalImg.src = 'http://CENSORED_URL/wp-content/uploads/2025/01/example_template.png';
				captionText.innerHTML = "Template Example";
			}
			
			function selectTab(tabName) {      
    			console.log('selectTab');
				let table = $j('#institutions_div');
				if (tabName == 'Create') {
					$j('#submission_type').val('template');
					$j('#emaillist_prompt1').css("visibility", "hidden");
					$j('#emaillist_prompt2').css("visibility", "hidden");
					$j('#emaillist_selector').css("visibility", "hidden");
					$j('#emaillist_name').css("visibility", "hidden");
					table.detach();
					$j('#create_table_holder').append(table);
				} else {
					$j('#submission_type').val('email');
					$j('#emaillist_prompt1').css("visibility", "visible");
					$j('#emaillist_prompt2').css("visibility", "visible");
					$j('#emaillist_selector').css("visibility", "visible");
					$j('#emaillist_name').css("visibility", "visible");
					$j('#email_template').val($j('#text_input').val());
					table.detach();
					$j('#edit_table_holder').append(table);
				}
    			let i = 0;
    			let tabcontent = document.getElementsByClassName('tabcontent');
    			let tablinks = document.getElementsByClassName('tablinks');
    			let elem = null;

    			for (i = 0; i < tabcontent.length; i++) {
        			elem = tabcontent[i];
        			elem.style.display = 'none';
    			}

    			for (i = 0; i < tablinks.length; i++) {
        			tablinks[i].className = tablinks[i].className.replace(' active', '');
    			}

    			let tabElement = document.getElementById(tabName);

    			if (tabElement !== null) {
        			tabElement.style.display = 'block';
    			}
			}
			
			var email_list_length = 0;
			
			function filteredInstitutionsToString () {
				email_list_length = 0;
				if (filteredInstitutions == null) return '';
				let checkedInstitutions = filteredInstitutions.filter(function (e) { return  e['_checked'] == true; });
				email_list_length = checkedInstitutions.length;
				let arrayOfCallNums = checkedInstitutions.map(function (e) { return e['call_num']; });
				let callNumsAsString = arrayOfCallNums.join(',');
				console.log('callNumsAsString=' + callNumsAsString);
				return encodeURIComponent(callNumsAsString);
			}
			
			function convertEmailStringToInstitutions(str) {
				if (! str) return null;
				let decodedStr = decodeURIComponent(str);
				let arrayOfCallNums = decodedStr.split(',');
				let newFilteredInstitutions = filteredInstitutions.filter(function (i) { return arrayOfCallNums.indexOf(i['call_num']) != -1; });
				filteredInstitutions = newFilteredInstitutions;
				redisplayInstitutionsTable3();
			}
			
			function createEmailList() {
				let encodedList = filteredInstitutionsToString();
				let displayText = "Email list has " + email_list_length + ' recipients.';
				$j('#email_list_info_span').text(displayText);
				$j('#emaillist_contents').val(encodedList);
			}
			
			function displayTemplateAppliedToFirstInstitution() {
				currentInstitutionIndex = 0;
				displayTemplateAppliedToCurrentInstitution();
			}
			
			function displayTemplateAppliedToLastInstitution() {
				currentInstitutionIndex = Math.max(0, filteredInstitutions.length - 1);
				displayTemplateAppliedToCurrentInstitution();
			}
			
			function displayTemplateAppliedToNextInstitution() {
				currentInstitutionIndex = Math.min(filteredInstitutions.length - 1, currentInstitutionIndex + 1);
				displayTemplateAppliedToCurrentInstitution();
			}
			
			function displayTemplateAppliedToPreviousInstitution() {
				currentInstitutionIndex = Math.max(0, currentInstitutionIndex - 1);
				displayTemplateAppliedToCurrentInstitution();
			}
			
			function displayTemplateAppliedToCurrentInstitution() {
				$j('#submit').css('visibility', 'hidden');
         		let output = $j('#text_output');
                let template = $j('#text_input').val();
				let invalidFieldList = $j('#invalid_field_list');
				let balancedIndicator = $j('#balanced_indicator');
				let invalidFieldsTitle = $j('#invalid_fields_title');
				let invalidNestingsTitle = $j('#invalid_nestings_title');
				let invalidNestingsList = $j('#invalid_nestings_list');
				let allIsValid = true;
                unfn = [];
				invalidNestings = [];					
                invalidNestingsList.find('li').remove();
                invalidFieldList.find('li').remove();
				if (validateBracketBalance(template, invalidNestings)) {
					balancedIndicator.css('visibility', 'hidden');
				} else {
					allIsValid = false;
					balancedIndicator.css('visibility', 'visible');
				}
				if (invalidNestings.length == 0) {
					invalidNestingsTitle.css('visibility', 'hidden');
					console.log('invalid nestings: ' + JSON.stringify(invalidNestings));
				} else {
					allIsValid = false;
					invalidNestingsTitle.css('visibility', 'visible');
                    let inCount = invalidNestings.length;
                    for (i = 0; i < inCount; i++)
                        invalidNestingsList.append($j('<li class="error_item">' + invalidNestings[i] + '</li>'));
				}
                if (validateText(template, unfn)) {
                    console.log('valid');
					invalidFieldsTitle.css('visibility', 'hidden');
                    // output.val(generateEmailTextForInstitution(filteredInstitutions[currentInstitutionIndex], template));
                    output.empty();
					output.append($j(generateEmailTextForInstitution(filteredInstitutions[currentInstitutionIndex], template)));
					if (allIsValid)
						$j('#submit').css('visibility', 'visible');
                } else {
                    console.log('invalid');						
					invalidFieldsTitle.css('visibility', 'visible');
                    let unfnCount = unfn.length;
                    for (i = 0; i < unfnCount; i++)
                        invalidFieldList.append($j('<li class="error_item">' + unfn[i] + '</li>'));
					$j('#submit').css('visibility', 'hidden');
                }
			}
			
			function gatherFieldRanges() { // leagueid
				let chartTypeSet = {};
				let leagueIDSet = {};
				let parentCallNumSet = {};
				let count = filteredInstitutions.length;
				let prop = null;
				for (i = 0; i < count; i++) {
					let institution = filteredInstitutions[i];
					let ct = institution['chart_type'];
					if (ct)	chartTypeSet[ct] = true;
					let lid = institution['leagueid'];
					if (lid) leagueIDSet[lid] = true;
					let pcn = institution['parent_call_num'];
					if (pcn) parentCallNumSet[pcn] = true;
				}
				for (prop in chartTypeSet) {
					if (chartTypeSet.hasOwnProperty(prop)) {
						chartTypes.push(Number(prop));
					}
				}
				for (prop in leagueIDSet) {
					if (leagueIDSet.hasOwnProperty(prop)) {
						leagueIDs.push(Number(prop));
					}
				}
				for (prop in parentCallNumSet) {
					if (parentCallNumSet.hasOwnProperty(prop)) {
						parentCallNums.push(Number(prop));
					}
				}
				chartTypes.sort(function (v1, v2) { return v1 - v2; });
				leagueIDs.sort(function (v1, v2) { return v1 - v2; });
				parentCallNums.sort(function (v1, v2) { return v1 - v2; });
				addFieldsToSelect('select_chart_type', chartTypes);
				addFieldsToSelect('select_leagueid', leagueIDs);
				addFieldsToSelect('select_parent_call_num', parentCallNums);
			}
			
			function chartTypeSelect() {
				let val = $j('#select_chart_type').val();
				selectedChartType = (val) ? val : null;
			}
			
			function leagueIDSelect() {
				let val = $j('#select_leagueid').val();
				selectedLeagueID = (val) ? val : null;
			}
			
			function parentCallNumSelect() {
				let val = $j('#select_parent_call_num').val();
				selectedParentCallNum = (val) ? val : null;
			}
			
			function addFieldsToSelect(id, values) {
				let fs = $j('#' + id);
				let fv = fs.val();
				fs.append($j('<option>', {value: '', text: '', selected: true}));
				for (i = 0; i < values.length; i++) {
					let v = values[i];
					fs.append($j('<option>', {value: v, text: v, selected: (v == fv) ? true : false}));
				}
			}
			
			function getTemplateData(name) {
				for (i = 0; i < templates.length; i++)
					if (templates[i]['name'] == name)
						return templates[i];
				return null;
			}
			
			function templateSelect() {
				var templateName = $j('#template_selector').find(":selected").val();
				
				if (! templateName) {
					template = null;
					$j('#text_input').val('');
					$j('#text_output').val('');
					return;
				}
				
				var templateData = getTemplateData(templateName);
				var templateText = (templateData) ? templateData['template'] : '';
				template = templateName;
				$j('#template_name').val(templateName);
				$j('#text_input').val(templateText);
				displayTemplateAppliedToCurrentInstitution();
			}

            function validateText(text, unknownFieldNames) {
                if (unknownFieldNames == undefined)
                    unknownFieldNames = [];
                let allFieldsIterator = text.matchAll(validatePattern);
                for (match of allFieldsIterator) {
                    let token = match[0];
                    let tokenLen = token.length;
                    let fieldName = token.substring(1, tokenLen - 1);
                    console.log('fieldName=' + fieldName);
                    if (! Object.hasOwn(institutionFieldMap, fieldName)) {
                        console.log('adding unknown field name ' + fieldName);
                        unknownFieldNames.push(fieldName);
                    }
                }

                return unknownFieldNames.length == 0;
            }
			
			function validateBracketBalance(text, invalidNestings) {
                if (invalidNestings == undefined)
                    invalidNestings = [];
				if (text) {
					let len = text.length;
					let count = 0;
					let nesting = '';
					let exceeded = false;
					for (i = 0; i < len; i++) {
						let ch = text.charAt(i);
						switch (ch) {
							case '{':
								if (count == 0) {
									nesting = '{';
									exceeded = false;
								} else if (count > 0) {
									nesting += ch;
								}
								count += 1;
								break;
							case '}':
								if (count == 1) {
									nesting += '}';
									if (exceeded) {
										invalidNestings.push(nesting);
										nesting = '';
										exceeded = false;
									}
								} else if (count > 1) {
									nesting += ch;
								}
								count -= 1;
								break;
							default:
								nesting += ch;
								break;
						}
						
						if (count > 1) exceeded = true;
					}
					
					return count == 0;
				} else {
					return true;
				}
			}
			
			function translateLinkToHTML(link) {
				if ((link == undefined) || (link == null)) return '';
				
				let text = link['text'];
				let target = link['target'];
				let type = link['type'];
				
				if (type == undefined) {
					return '<span>Invalid link: ' + text + ', ' + target + '</span>';
				} else if (type == 'url') {
					return '<a href="' + target + '">' + text + '</a>';
				} else if (type == 'email') {
					return '<a href="mailto:' + target + '">' + text + '</a>';
				} else {
					return '<span>Invalid link: ' + text + ', ' + target + '</span>';
				}
			}

            function generateEmailTextForInstitution(institution, text) {
                let clearText = text.split(validatePattern);
                let fieldNames = [];
                let allFieldsIterator = text.matchAll(validatePattern);
                for (match of allFieldsIterator) {
                    let token = match[0];
                    let tokenLen = token.length;
                    let fieldName = token.substring(1, tokenLen - 1);
                    fieldNames.push(fieldName);
                }
                let clearTextLen = clearText.length;
                let fieldNamesLen = fieldNames.length;
                let instantiatedText = '';

                for (i = 0; i < clearTextLen; i++) {
                    instantiatedText += clearText[i];
                    if (i < fieldNamesLen) {
                        instantiatedText += institution[fieldNames[i]];
                    }
                }
												
				// process links / linkPattern
                
				let links = [];
				let allLinksIterator = instantiatedText.matchAll(linkPattern);
				let offset = "link:".length;
				for (match of allLinksIterator) {
                    let token = match[0];
                    let tokenLen = token.length;
                    let linkText = token.substring(1, tokenLen - 1);
					let linkBody = linkText.substring(offset);
					let linkSubparts = linkBody.split(',');
					let readableText = linkSubparts[0];
					let linkTarget = (linkSubparts.length > 1) ? linkSubparts[1] : linkSubparts[0];
					let linkInfo = {text: readableText, target: linkTarget};
					
					// Is it a link to a URL or an email?
					
					if ((linkTarget.indexOf('@') != -1) && linkTarget.matchAll(emailPattern)) {
						linkInfo['type'] = 'email';
					} else if (linkTarget.matchAll(linkPattern)) {
						linkInfo['type'] = 'url';
					}
					
					links.push(linkInfo);
				}
				
				// translateLinkToHTML
				
				console.log(JSON.stringify(links));
				
				let clearText2 = instantiatedText.split(linkPattern);
				let clearText2Len = clearText2.length;
                let linksLen = links.length;
                let instantiatedText2 = '';

                for (i = 0; i < clearText2Len; i++) {
                    instantiatedText2 += clearText2[i];
                    if (i < clearText2Len) {
                        instantiatedText2 += translateLinkToHTML(links[i]);
                    }
                }
				
				// linePattern
				var textLines = instantiatedText2.split('\n');
				var htmlLines = textLines.map(function (line) { return '<p>' + line + '</p>'; });

                return htmlLines.join('\n');
            }

            $j(document).ready(function() {
				filteredInstitutions = institutions;
				gatherFieldRanges();
				$j('.filter').change(function() {
					console.log("filter change");
					let id = $j(this).attr('id');
					let pos = id.indexOf('_');
					let fieldName = id.substring(pos + 1);
					let value = $j(this).val();
					if ((value != null) && (value != ''))
						filters[fieldName] = value;
					else
						delete filters[fieldName];
					console.log(JSON.stringify(filters));
					filteredInstitutions = filterInstitutions3(filters);Re
					redisplayInstitutionsTable3();
					currentInstitutionIndex = 0;
				});
				$j("#example_close").click(function () { $j('#myModal').css('display', 'none'); });
				$j('#submit').css('visibility', 'hidden');
				$j('#balanced_indicator').css('visibility', 'hidden');
				$j('#invalid_nestings_title').css('visibility', 'hidden');
				$j('#invalid_fields_title').css('visibility', 'hidden');
				let ts = $j('#template_selector');
				ts.append($j('<option>', {value: '', text: '', selected: true}));
				for (i = 0; i < templates.length; i++) {
					let tn = templates[i]['name'];
					console.log("Adding name: " + tn);
					ts.append($j('<option>', {value: tn, text: tn, selected: (tn == template) ? true : false}));
				}
				$j('#template_form').on('submit', function () {
					let templateName = $j('#template_name').val();
					let input = $j('#text_input');
					let templateText = input.val();
					templateText = templateText.replaceAll('\r', '\\r');
					templateText = templateText.replaceAll('\n', '\\n');
					templateText = templateText.replaceAll('\'', '\\\'');
					$j(this).val(template);
					if (templateName) {
						let templateData = getTemplateData(templateName);
						if (templateData) {
							templateData['template'] = templateText;
						} else {
							templates.push({name: templateName, template: $j('#text_input').val()});
						}
					}
				});
                var unfn = [];
				var invalidNestings = [];
                $j('#text_input').on('keyup', displayTemplateAppliedToCurrentInstitution);
                generateInstitutionTable3($j('#institutions_div'));
				
				if (preselectTab != '')
					selectTab(preselectTab);
				else
					selectTab('Create');
            });
        </script>
<?php
}
?>