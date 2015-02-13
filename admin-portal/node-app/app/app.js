(function(){
  var app = angular.module('sc2geeksApp',
    ['ngRoute', 'ui.bootstrap', 'acute.select']);

  app.config(['$routeProvider', function ($routeProvider) {
    var viewBase = '/app/sc2geeks/views/';

    $routeProvider
      .when('/progamer/events', {
        controller: 'progamerEventListController',
        templateUrl: viewBase + 'progamer/eventList.html'
      })
      .when('/progamer/event/:event_id', {
        controller: 'progamerEventEditController',
        templateUrl: viewBase + 'progamer/eventPlayerList.html'
      })
      .otherwise({ redirectTo: '/progamer/events' });

  }])
    .run(function(acuteSelectService){
      acuteSelectService.updateSetting("templatePath", "/js/acute.select/template");
    });

}());