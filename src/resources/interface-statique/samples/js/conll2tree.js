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

	var height = $(window).height();
	var index = 0;
	var n = 1;
	var nbPreloadTrees = 3;
	if(height > 1500){nbPreloadTrees = 5}
	if(height > 2400){nbPreloadTrees = 10}
	// var list = document.getElementsByTagName("conll");
	// for (var i = index;i < index+nbPreloadTrees;i++){
	//            drawConll1(list[i],i+1);
	// 			var script = 'clipboard('+(i+1)+');';
	// 			// execScript(script);
	// 			var clipboard = new Clipboard('.btn');
	// 			clipboard.on('success', function(e) {
	// 				// console.log(e.textw);
	// 				myFunction()
	// 			});
	// 			clipboard.on('error', function(e) {
	// 				console.log(e);
	// 			});
	// }
	// index = index + nbPreloadTrees;

// $(window).scroll(function() {
//   if (nearBottomOfPage()) {
//    			for (var i = index;i < index+n;i++){
//         		drawConll(list[i],i+1);
// 				var script = 'clipboard('+(i+1)+');';
// 				// execScript(script);
// 				var clipboard = new Clipboard('.btn');
// 				clipboard.on('success', function(e) {
// 					// console.log(e.textw);
// 					myFunction()
// 				});
// 				clipboard.on('error', function(e) {
// 					console.log(e);
// 				});
// 			}
// 			index = index + n;
//   } 
// });

function nearBottomOfPage() {
  return scrollDistanceFromBottom() < 150;
}

function scrollDistanceFromBottom(argument) {
  return pageHeight() - (window.pageYOffset + self.innerHeight);
}

function pageHeight() {
  return Math.max(document.body.scrollHeight, document.body.offsetHeight);
}


performTask(
    // A set of items.
    // list,
    // Process two items every iteration.
    1
    // Function that will do stuff to the items. Called once for every item. Gets
    // the array with items and the index of the current item (to prevent copying
    // values around which is unnecessary.)
  //   function (items, index) {
  //       // Do stuff with items[index]
  //       // This could also be inline in iteration for better performance.
  //       // console.log(index);
  //       drawConll(list[index],index+1);
		// var script = 'clipboard('+(index+1)+');';
		// // execScript(script);
		// var clipboard = new Clipboard('.btn');
		// clipboard.on('success', function(e) {
		// 	// console.log(e.textw);
		// 	myFunction()
		// });
		// clipboard.on('error', function(e) {
		// 	console.log(e);
		// });
// }
);



var inactivityTime = function () {
    var t;
    window.onload = resetTimer;
    document.onmousemove = resetTimer;
    document.onkeypress = resetTimer;

    function load() {
       for (var i = index;i < index+n;i++){
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
			index = index + n;
    }

    function resetTimer() {
        clearTimeout(t);
        t = setTimeout(load, 1000)
        // 1000 milisec = 1 sec
    }
};
// inactivityTime();


$( ".btn-empty" ).click(function() {
		myFunction()
	});

	// $(window).scroll(function() {

	//     if($(window).scrollTop() == $(document).height() - $(window).height() ) {

	//     	// load_all_js();
 //           // ajax call get data from server and append to the div
 //           for (var i = index;i < index+n;i++){
 //        		drawConll(list[i],i+1);
	// 			var script = 'clipboard('+(i+1)+');';
	// 			// execScript(script);
	// 			var clipboard = new Clipboard('.btn');
	// 			clipboard.on('success', function(e) {
	// 				// console.log(e.textw);
	// 				myFunction()
	// 			});
	// 			clipboard.on('error', function(e) {
	// 				console.log(e);
	// 			});
	// 		}
	// 		index = index + n;
	//     }
	// });

	// var list = document.getElementsByTagName("conll");
	// // for (var i = 0;i < list.length;i++){
	// for (var i = 0;i < 10;i++){
	// 	drawConll(list[i],i+1);
	// 	var script = 'clipboard('+(i+1)+');';
	// 	// execScript(script);
	// 	var clipboard = new Clipboard('.btn');
	// 	clipboard.on('success', function(e) {
	// 		// console.log(e.textw);
	// 		myFunction()
	// 	});
	// 	clipboard.on('error', function(e) {
	// 		console.log(e);
	// 	});
	// }


	



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
This is the classic one with does not load new animation on element, but is important for starter
**/
function drawConll1(elementConll,num) {
	// put the content in a variable
	var content = elementConll.innerHTML;

	//check if an anchor already exist in the url and split
	var url = window.location.href;
	var res = url.split("#",1); 


	// inject the holder div and sentence div
	elementConll.innerHTML = '<div class="wow fadeInLeft" >'//data-wow-delay="0.5s">'
	+ '<form>  <div class="input-group"> <label id="copy-input'+num+'" for="#copy-button'+num+'" style="font-size: 12;">'+res+'#tree'+num+'</label>  <span class="input-group-btn"> <button class="btn btn-primary" type="button" id="copy-button'+num+'" data-clipboard-text="'+res+'#tree'+num+'" data-toggle="tooltip" data-placement="button"  title="Copy to Clipboard"> <img src="../img/icons/clipboard.svg"/> </button> </span> </div> </form>'
	+ '<div id="tree'+num+'"><div id="sentence'+num+'" class="sentences"></div>'
	+ '<div id="holder'+num+'" class="svgholder" style="background-color: white; overflow: auto"> </div></div>';

	data=conllNodesToTree(content);
	$("#sentence"+num).html( data["sentence"] );

	draw("holder"+num,data["tree"]);
}

/** function which uses justDrawConll.js by Kim Gerdes to display graphical trees.
Retrieves the conll tag content, create two additionnal div into it, then replace them by a tree
Allows to draw different formats
Loads new animations for elements
**/
function drawConll(elementConll,num) {
	// put the content in a variable
	var content = elementConll.innerHTML;

	//check if an anchor already exist in the url and split
	var url = window.location.href;
	var res = url.split("#",1); 

	//get the number of max sent to display it
	var list = document.getElementsByTagName("conll");
	var nbMaxSent = list.length;

	div = document.createElement('div');
	// div.id = 'newdiv'+num;
	div.innerHTML = '<form>  <div class="input-group"> <span class="input-group-btn"> <button class="btn btn-success" style="border-radius:50%" type="button" id="num-button'+num+'"  data-toggle="tooltip" data-placement="button"  title="This sentence\'s number"> <strong>'+num+' / '+nbMaxSent+'</strong> </button> </span> <label id="copy-input'+num+'" for="#copy-button'+num+'">'+res+'#tree'+num+'</label>  <span class="input-group-btn"> <button class="btn btn-primary" type="button" id="copy-button'+num+'" data-clipboard-text="'+res+'#tree'+num+'" data-toggle="tooltip" data-placement="button"  title="Copy to Clipboard"> <img src="../img/icons/clipboard.svg"/> </button> </span> </div> </form>'
		+ '<div id="tree'+num+'"><div id="sentence'+num+'" class="sentences"></div>'
		+ '<div id="holder'+num+'" class="svgholder" style="background-color: white; overflow: auto"> </div>';
		div.className = "new";
	elementConll.appendChild(div);

		data=conllNodesToTree(content);
		$("#sentence"+num).html( data["sentence"] );

		draw("holder"+num,data["tree"]);
	    div.className = 'fade';

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

function performTask(numToProcess) {
    var pos = 0;
    var list = document.getElementsByTagName("conll");
    // This is run once for every numToProcess items.
    function iteration() {

        // Calculate last position.
        var j = Math.min(pos + numToProcess, list.length);
        // Start at current position and loop to last position.
        for (var i = pos; i < j; i++) {
   //          // processItem(items, i);
            
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
			// inactivityTime();
        }
        // Increment current position.
        pos += numToProcess;
        console.log(pos);
        // Only continue if there are more items to process.
        if (pos < list.length)
            setTimeout(iteration, 1500); // Wait 10 ms to let the UI update.
    }
    iteration();


}

var inactivityTime = function () {
    var t;
    window.onload = resetTimer;
    document.onmousemove = resetTimer;
    document.onkeypress = resetTimer;

    function load() {
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

    function resetTimer() {
        clearTimeout(t);
        t = setTimeout(load, 3000)
        // 1000 milisec = 1 sec
    }
};