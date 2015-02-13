/**
 * Created by robert on 10/7/14.
 */
(function(){
  var dataService = function($http){
    var baseUrl = '/api'
    var factory = {};

    factory.getEventStats = function(page_size, page_no, filter_all_assigned){
      var url = baseUrl + '/eventStats?filter_all_assigned=' + filter_all_assigned + "&" + buildPaginationUrl(page_size, page_no);
      return $http.get(url).then(function (response) {
        return response.data;
      });
    };

    factory.getEventStatusById = function(event_id) {
      var url = baseUrl + '/eventStats/' + event_id;
      return $http.get(url).then(function (response) {
        return response;
      });
    };

    factory.getEventById = function(event_id) {
      var url = baseUrl + '/event/' + event_id;
      return $http.get(url).then(function (response) {
        return response;
      });
    };

    factory.getEventPlayers = function(event_id, page_size, page_no, filter_progamers) {
      var url = baseUrl + '/eventPlayers?event_id=' + event_id + "&filter_progamers=" + filter_progamers + '&' + buildPaginationUrl(page_size, page_no);
      return $http.get(url).then(function (response) {
        return response.data;
      });
    };

    factory.getAllProgamers = function() {
      var url = baseUrl + '/progamer';
      return $http.get(url).then(function (response) {
        return response.data;
      });
    };

    factory.assignProgamerForEventPlayer = function(event_id, player_url, progamer_id) {
      var url = baseUrl + '/progamer/eventPlayer';
      return $http.post(url, {event_id: event_id, player_url: player_url, progamer_id: progamer_id})
        .then(function(response) {
          return response;
        });
    };

    factory.removeProgamerForEventPlayer = function(event_id, player_url) {
      var url = baseUrl + '/progamer/event/' + event_id + '/player/' + encodeURIComponent(player_url);
      return $http.delete(url)
        .then(function(response) {
          return response;
        });
    };

    factory.updateProgamersForEventAllDone = function (event_id, all_done) {
      var url = baseUrl + '/eventStats';
      return $http.post(url, {event_id: event_id, all_done: all_done})
        .then(function(response) {
          return response;
        });
    };

    return factory;
  };

  function buildPaginationUrl(page_size, page_no) {
    return 'page_size=' + page_size + '&page_no=' + page_no;
  }

  angular.module('sc2geeksApp').factory('dataService', dataService);
}());