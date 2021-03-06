/**
 * A list of all the levels and states. This is what allows us to switch
 * between the different "pages". Each html page should have an associated
 * javascript controller, even if that controller is unused.
**/

angular.module("states", ["ionic"])

.config(function ($stateProvider, $urlRouterProvider){
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
  

  .state("settings", {
    url: "/settings",
    templateUrl: "templates/settings.html",
    controller: "SettingsCtrl"
  })
  
  .state("level", {
    url: "/level/{levelNum:int}",
    templateUrl: "games/dummy.html",
    controller: "LevelCtrl"
  })

  $urlRouterProvider.otherwise("/main")
})
