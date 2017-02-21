angular.module('MyApp', [
    'ngStomp'
])
    .controller('MyCtrl', function ($scope, $log, $stomp) {

        $scope.currentTime = null;
        $scope.freeMemory = null;

        function cbConnectError(e) {
            $log.error('STOMP error', e);
        }

        $stomp.connect('/ws', {}, cbConnectError)
            .then(function(frame){
                var subscription = $stomp.subscribe('/user/exchange/amq.direct/current-time', function(payload, headers, res){
                    $log.debug("received", payload, headers, res);

                    $scope.$apply(function() {
                        $scope.currentTime = payload.value;
                    });
                })

                var sub2 = $stomp.subscribe('/topic/memory', function(payload, headers, res){
                    $log.debug("received /topic/memory", payload, headers, res);
                    $scope.freeMemory = payload.free;
                })
            })

    })
;