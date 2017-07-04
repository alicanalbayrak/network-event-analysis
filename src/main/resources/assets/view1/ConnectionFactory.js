
function ConnectionFactory($http) {

    var urlBase = '/service/connection/summary';

    this.getConnections = function () {
        console.log("Product Factory will call server to get connections");
        return this.http.get(urlBase);
    };


    this.findProduct = function (id) {
        return this.http.get(urlBase + '/' + id);
    };


    (function (self, $http) {
        // log that the controller is called
        console.log("Product Factory constructor called, "+
            "will not call server here since we don't need connections yet.");

        // assign http object to this Factory's http variable
        // we will use that object to fetch the connections
        self.http = $http;

    })(this, $http);

    // return facory instance,
    // angular needs this returned object
    return this;
}