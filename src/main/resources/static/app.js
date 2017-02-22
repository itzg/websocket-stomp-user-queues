angular.module('MyApp', [
    'ngStomp'
])
    .controller('MyCtrl', function ($scope, $log, $stomp) {

        $scope.currentTime = null;
        $scope.freeMemory = null;
        $scope.random = null;

        function cbConnectError(e) {
            $log.error('STOMP error', e);
        }

        $stomp.connect('/ws', {}, cbConnectError)
            .then(function(frame){
                // Use RabbitMQ's STOMP feature "SEND to arbitrary routing keys and SUBSCRIBE to arbitrary binding patterns"
                var subscription = $stomp.subscribe('/user/exchange/amq.direct/current-time', function(payload, headers, res){
                    $log.debug("received", payload, headers, res);

                    $scope.$apply(function() {
                        $scope.currentTime = payload.value;
                    });
                });

                // Use RabbitMQ's STOMP feature "SEND and SUBSCRIBE to transient and durable topics"
                var subMemory = $stomp.subscribe('/topic/memory', function(payload, headers, res){
                    $log.debug("received /topic/memory", payload, headers, res);
                    $scope.freeMemory = payload.free;
                });

                // Use RabbitMQ's STOMP feature "SEND and SUBSCRIBE to queues created outside the STOMP gateway"
                var subRandom = $stomp.subscribe('/user/amq/queue/random', function(payload, headers, res){
                    $log.debug("received my random", payload, headers, res);
                    $scope.random = payload.value;
                });
            })

    })
;