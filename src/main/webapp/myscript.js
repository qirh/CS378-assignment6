/**
 * 
 */
var xmlhttp;
function myFunction() {
    var x, text;

    // Get the value of input field with id="numb"

    x = document.getElementById("numb").value;

    // If x is Not a Number or less than one or greater than 10

    if (isNaN(x) || x < 1 || x > 10) {
        text = "Invalid input.";
    } else {
        text = "Correct input.";
    }
    document.getElementById("demo").innerHTML = text;
    
    console.log("Testing console.");
    console.log(text);
}
function loadXMLDoc(url,cfunc) {
    if (window.XMLHttpRequest) {
    	xmlhttp=new XMLHttpRequest();
    }
    else {
    	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    } 
    xmlhttp.onreadystatechange=cfunc;
    xmlhttp.open("GET",url,true);
    xmlhttp.send();
}

function foo()
{
loadXMLDoc("http://54.90.86.184:8080/js-example/meetings/myeavesdrop/meetings/",function()
  {
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
	console.log("here");
    xmlDoc = xmlhttp.responseXML;
    
    //titles
    document.write("<link rel=\"stylesheet\" href=\"style.css\"><div id=\"div\"> <h3>Computed count of meetings, with results:</h3> <br></div>");
    
    document.write("<table border=\"1\"><tr><th>Meeting Year</th><th>Meeting Count</th></tr>");
    
    var x=xmlDoc.getElementsByTagName("meeting");
    
    for (i=0;i<x.length;i++)
    { 
    document.write("<tr><td>");
    document.write(x[i].getElementsByTagName("year")[0].childNodes[0].nodeValue);
    document.write("</td><td>");
    document.write(x[i].getElementsByTagName("i")[0].childNodes[0].nodeValue);
    document.write("</td></tr>");
    }
  
    document.write("</table>");

    
    }
  });
}