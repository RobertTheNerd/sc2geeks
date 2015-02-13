/**
 * Created by robert on 10/7/14.
 */
(function(){
  var page_size = 15;
  var progamerEventEditController = function($scope, $routeParams, dataService){
    $scope.event_id = parseInt($routeParams.event_id, 10);
    $scope.event = {};
    $scope.event_stats = {};
    $scope.players = [];
    $scope.total_count = 0;
    $scope.page_no = 1;
    $scope.page_size = page_size;
    $scope.progamers = [];
    $scope.selected_progamers = {};
    $scope.filter_progamers = true;


    init();

    $scope.pageChanged = function() {
      console.log('page changed: ', $scope.page_no);
      $scope.selected_progamers = {};
      getPlayers();
    };

    $scope.toggleFilterProgamers = function() {
      $scope.selected_progamers = {};
      $scope.page_no = 1;
      getPlayers();
    }

    $scope.getAllProgamers = function(callback){
      callback($scope.progamers);
    };

    $scope.toggleAllDone = function() {
      dataService.updateProgamersForEventAllDone($scope.event_id, $scope.event_stats.all_progamers_assigned)
        .then(function(response) {
          if (response.status == 200) {
            getEventStatus();
          }
        });
    };

    $scope.saveProgamer = function (index) {
      if (typeof($scope.selected_progamers[index]) === 'undefined')
        return;

      var progamer = $scope.selected_progamers[index];
      var player = $scope.players[index];
      dataService.assignProgamerForEventPlayer(player.event_id, player.player_url, progamer.id)
        .then(function(response) {
          if (response.status === 200 && response.data.updated === 1) {
            player.progamer_id = progamer.id;
            player.progamer_name = progamer.name;
            player.$edit = false;
          }
        });
    };

    $scope.deleteProgamer = function(index) {
      var player = $scope.players[index];
      dataService.removeProgamerForEventPlayer(player.event_id, player.player_url)
        .then(function(response){
          player.$edit = false;
          if (response.status == 200 && response.data.updated === 1) {
            delete player.progamer_id;
            player.progamer_name = '';
          }
        });
    };

    $scope.progamerChange = function(progamer, index) {
      $scope.selected_progamers[index] = progamer;
      $scope.saveProgamer(index);
    };

    function init() {
      getEvent();
      getEventStatus();
      getPlayers();
      fetchProgamers();
    }

    function getEvent() {
      dataService.getEventById($scope.event_id).then(function(response) {
        if (response.status === 200)
          $scope.event = response.data;
      });
    }

    function getEventStatus(){
      dataService.getEventStatusById($scope.event_id).then(function(response) {
        if (response.status === 200){
          $scope.event_stats = response.data;
        }
      });
    }

    function getPlayers() {
      dataService.getEventPlayers($scope.event_id, page_size, $scope.page_no, $scope.filter_progamers)
        .then(function(data) {
          $scope.total_count = data.total_count;
          $scope.players = data.players;
          for (var i = 0; i < $scope.players.length; i ++) {
            player = $scope.players[i];
            player.progamer_name = player.progamer_name || '';
            player.getProgamer = function() {
              if (typeof(this.progamer_id) === 'undefined')
                return null;

              for (i = 0; i < $scope.progamers.length; i ++) {
                if ($scope.progamers[i].id == this.progamer_id)
                  return $scope.progamers[i];
              }
            };
          }
        });
    }

    function fetchProgamers() {
      dataService.getAllProgamers().then(function(data) {
        $scope.progamers = data;
        for (var i = 0; i < $scope.progamers.length; i ++) {
          $scope.progamers[i].id = $scope.progamers[i]._id;
          delete $scope.progamers[i]._id;
        }
      });
    }
  };

  angular.module('sc2geeksApp').controller('progamerEventEditController', progamerEventEditController);
}());