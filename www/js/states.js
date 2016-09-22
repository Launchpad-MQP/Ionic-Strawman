angular.module("states", ["ionic"])

.config(function($stateProvider, $urlRouterProvider){
  $stateProvider

  .state("main", {
    url: "/main",
    templateUrl: "templates/main.html",
    controller: "MainCtrl"
  })

  .state("level_select", {
    url: "/level_select",
    templateUrl: "templates/level_select.html",
    controller: "LevelSelectCtrl"
  })

  .state("level", {
    url: "/level/{levelNum:int}",
    templateUrl: "templates/level.html",
    controller: "LevelCtrl"
  })

  .state("settings", {
    url: "/settings",
    templateUrl: "templates/settings.html"
  });

  $urlRouterProvider.otherwise("/main");
});
