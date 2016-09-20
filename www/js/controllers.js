var app = angular.module("controllers", ["ionic"])

app.controller("MainCtrl", function($scope) {
  console.log("Main Controller says: Hello World");
})

.controller("LevelCtrl", function($scope) {
  var table = document.getElementsByClassName("levelTable")[0];
  var height = 5;
  var width = 4;
  for (var i=0; i<height; i+=1) {
    var row = table.insertRow(i);
    for (var j=0; j<width; j+=1) {
      var levelNo = i*width+j;
      var cell = row.insertCell(j);
      cell.setAttribute("name", j+"_"+i);
      var btn = document.createElement("button");
      btn.setAttribute("class", "button button-outline button-calm");
      btn.innerHTML = levelNo;
      btn.setAttribute("onmousedown", "loadLevel("+levelNo+")");
      cell.appendChild(btn);
    }
  }
});