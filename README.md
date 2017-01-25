# Ionic-Strawman
A basic framework for making puzzle apps in Ionic.

# Setup
* [sbt](http://www.scala-sbt.org)
* [node.js](http://nodejs.org)
* npm install -g cordova ionic@1 bower
* cordova plugin add https://github.com/brodysoft/Cordova-SQLitePlugin.git
* bower install ngCordova
* [karma] (http://karma-runner.github.io/1.0/index.html)
* npm install karma --save-dev
* npm install karma-jasmine karma-chrome-launcher jasmine-core --save-dev
* npm install -g karma-cli
* mv cls-scala-assembly-1.0.jar ionicMQP2016/lib
* cd ionicMQP2016 
* sbt run BlankApp
* cd ..
* ionic serve
