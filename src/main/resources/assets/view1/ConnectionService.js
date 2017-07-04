function ConnectionService(connectionFactory) {


    var factory = null;

    this.loadConnections = function(listener) {
        factory.getConnections()
            .then(function (response) {
                var connections = response.data;
                listener(connections);
            }, function (error) {
                console.error('Unable to load connections, will use default connections');
                var products = factory.createDefaultProducts();
                listener(products);
            });
    };


    (function (self, connectionFactory) {

        console.log("Products service constructor called");

        factory = connectionFactory;

    })(this, connectionFactory);

}