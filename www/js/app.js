// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// "starter" is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of "requires"
var app = angular.module("starter", ["ionic"])

app.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    if(window.cordova && window.cordova.plugins.Keyboard) {
      // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
      // for form inputs)
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);

      // Don"t remove this line unless you know what you are doing. It stops the viewport
      // from snapping when text inputs are focused. Ionic handles this internally for
      // a much nicer keyboard experience.
      cordova.plugins.Keyboard.disableScroll(true);
    }
    if(window.StatusBar) {
      StatusBar.styleDefault();
    }
  });
});

app.controller("MainCtrl", function($scope) {
  console.log("Main Controller says: Hello World");
});

app.controller("LevelCtrl", function($scope) {
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

app.config(function($stateProvider, $urlRouterProvider){
  $stateProvider

  .state("main", {
    url: "/main",
    templateUrl: "templates/main.html",
    controller: "MainCtrl"
  })

  .state("level_select", {
    url: "/level_select",
    templateUrl: "templates/level_select.html",
    controller: "LevelCtrl"
  })

  .state("settings", {
    url: "/settings",
    templateUrl: "templates/settings.html"
  });

  $urlRouterProvider.otherwise("/main");
});
