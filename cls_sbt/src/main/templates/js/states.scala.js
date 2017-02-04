@(stateList:Array[String])
/**
 * A list of all the levels and states. This is what allows us to switch
 * between the different "pages". Each html page should have an associated
 * javascript controller, even if that controller is unused.
**/

angular.module("states", ["ionic"])

.config(function ($stateProvider, $urlRouterProvider){
  $stateProvider
@for(state <- stateList) {

  .state("@state", {
    url: "/@state",
    templateUrl: "templates/@(state+".html")",
    controller: "@(state.split('_').map(_.capitalize).mkString("")+"Ctrl")"
  })
  }
  .state("level", {
    url: "/level/{levelNum:int}",
    templateUrl: "templates/game.html",
    controller: "LevelCtrl"
  });

  $urlRouterProvider.otherwise("/main");
});
