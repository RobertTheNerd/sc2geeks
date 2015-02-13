/**
 * Created by robert on 10/7/14.
 */
(function(){
  var page_size = 15;
  var progamerEventListController = function($scope, dataService){
    console.log('Started');
    $scope.events = [];
    $scope.page_no = 1;
    $scope.total_count = 0;
    $scope.page_size = page_size;
    $scope.filter_all_assigned = true;  // show events that is not all-assigned only

    init();

    $scope.pageChanged = function() {
      console.log('page changed: ', $scope.page_no);
      getProgamers();
    };

    $scope.toggleFilterAllAssigned = function() {
      $scope.page_no = 1;
      getProgamers();
    }

    function init() {
      getProgamers();
    }

    function getProgamers() {
      dataService.getEventStats(page_size, $scope.page_no, $scope.filter_all_assigned).then(function(data) {
        $scope.total_count = data.total_count;
        $scope.events = data.events;
        console.log(data);
      });
    }

  };

  angular.module('sc2geeksApp').controller('progamerEventListController', progamerEventListController);
}());