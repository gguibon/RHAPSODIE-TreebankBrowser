/**
Script to automatically transform conll tags (<conll>) content into simple arborator trees.
Requires arborator files in the header of the HTML page:
	<!-- Arborator files -->
	<script src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/raphael.js"></script>
	<script type="text/javascript" src="js/arborator.view.min.js"></script>
	<script type="text/javascript" src="js/justDrawConll.js"></script>
@author <a href="https://github.com/gguibon">Gaël Guibon</a>
@see <a href="http://arborator.ilpga.fr/">Arborator</a> by Kim Gerdes
**/

//auto launch drawConllTag on every conll tags.
$(document).ready(function(){

	var list = document.getElementsByTagName("conll");
	for (var i = 0;i < list.length;i++){
		drawConll(list[i],i+1);
		var script = 'clipboard('+(i+1)+');';
		// execScript(script);
		var clipboard = new Clipboard('.btn');
		clipboard.on('success', function(e) {
			// console.log(e.textw);
			myFunction()
		});
		clipboard.on('error', function(e) {
			console.log(e);
		});
	}


	$( ".btn-empty" ).click(function() {
		myFunction()
	});



});

/** 
function which convert conll raw input into arborator json tree object to graphically visualize them.
The format is one word per line, and tabulation separated values (not spaces !)
@ deprecated
@see drawConll function beacause this function uses a string to parse with a jsonObject. Not very optimized.
**/
function drawConllTag(elementConll,num) {
	var lines = elementConll.innerHTML.split('\n');
	var arrayWords = [];

	// iterate over each word to add its json object to the array
	for(var i = 0;i < lines.length;i++){
		var cols = lines[i].split('\t');
		var wordJson = '"'+cols[0]+'": '
		+ '{"cat": "'+cols[3]+'", '
		+ '"gov": {"'+cols[6]+'": "'+cols[7]+'"}, "index": "'+cols[0]+'", '
		+ '"lemma": "'+cols[2]+'", "lexid": "'+cols[0]+'", "nb": "'+cols[0]+'", "t": "'+cols[1]+'", "token": "'+cols[1]+'"}';
		arrayWords.push(wordJson);
	}

	// join the word json objects all together as one
	var jsonObj = "{" + arrayWords.join() + "}";
	elementConll.innerHTML = jsonObj;

	// inject the holder div and intialize the script content
	elementConll.innerHTML = '<div id="holder'+num+'" class="svgholder" style="background-color: white; overflow: auto"> </div>';
	var scriptContent =  '$(document).ready(function(){draw("holder'+num+'",'
		+ jsonObj 
		+ ');});';

	// script injection
	var script= document.createElement('script');
	script.type= 'text/javascript';
	script.text = scriptContent;
	elementConll.appendChild(script);
}

/**
execute a script even after page load
**/
function execScript(scriptContent){
	var body = document.getElementsByTagName("body");
	// script injection
	var script= document.createElement('script');
	script.type= 'text/javascript';
	script.text = scriptContent;
	body[0].appendChild(script);
}

/** function which uses justDrawConll.js by Kim Gerdes to display graphical trees.
Retrieves the conll tag content, create two additionnal div into it, then replace them by a tree
Allows to draw different formats
**/
function drawConll(elementConll,num) {
	// put the content in a variable
	var content = elementConll.innerHTML;

	//check if an anchor already exist in the url and split
	var url = window.location.href;
	var res = url.split("#",1); 

	// inject the holder div and sentence div
	elementConll.innerHTML = '<div class="wow fadeInLeft" data-wow-delay="0.5s">'
	+ '<form>   <div class="input-group"> <input type="text" class="form-control" value="'+res+'#tree'+num+'" placeholder="Some path" id="copy-input'+num+'"> <span class="input-group-btn"> <button class="btn btn-primary" type="button" id="copy-button'+num+'" data-clipboard-text="'+res+'#tree'+num+'" data-toggle="tooltip" data-placement="button"  title="Copy to Clipboard"> Copy </button> </span> </div> </form>'
	+ '<div id="tree'+num+'"><div id="sentence'+num+'" class="sentences"></div>'
	+ '<div id="holder'+num+'" class="svgholder" style="background-color: white; overflow: auto"> </div></div>';

	data=conllNodesToTree(content);
	$("#sentence"+num).html( data["sentence"] );
	draw("holder"+num,data["tree"]);

}


function clipboard(num){
	// Initialize the tooltip.
	$('#copy-button'+num).tooltip();

  // When the copy button is clicked, select the value of the text box, attempt
  // to execute the copy command, and trigger event to update tooltip message
  // to indicate whether the text was successfully copied.
  $('#copy-button'+num).bind('click', function() {
  	var input = document.querySelector('#copy-input'+num);
  	input.setSelectionRange(0, input.value.length + 1);
  	try {
  		var success = document.execCommand('copy');
  		if (success) {
  			$('#copy-button'+num).trigger('copied', ['Copied!']);
  		} else {
  			$('#copy-button'+num).trigger('copied', ['Copy with Ctrl-c']);
  		}
  	} catch (err) {
  		$('#copy-button'+num).trigger('copied', ['Copy with Ctrl-c']);
  	}
  });

  // Handler for updating the tooltip message.
  $('#copy-button'+num).bind('copied', function(event, message) {
  	$(this).attr('title', message)
  	.tooltip('fixTitle')
  	.tooltip('show')
  	.attr('title', "Copy to Clipboard")
  	.tooltip('fixTitle');
  });
}


/**
function to show a snackbar
**/
function myFunction() {
    // Get the snackbar DIV
    var x = document.getElementById("snackbar");

    // Add the "show" class to DIV
    x.className = "show";

    // After 3 seconds, remove the show class from DIV
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}

