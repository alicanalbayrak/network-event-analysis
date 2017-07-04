function ConnectionController($scope, connectionService) {

    var ViewModel = {
        connections : []
    };


    function productLoadListener(connections) {
        ViewModel.connections = connections;
    }

    this.loadConnections = function() {
        this.connectionService.loadConnections(productLoadListener);
    };

    (function (self, $scope, connectionService) {
        console.log("Products controller constructor called");

        self.connectionService = connectionService;

        $scope.controller = self;

        $scope.ViewModel = ViewModel;

    })(this, $scope, connectionService);

}