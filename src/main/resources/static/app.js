angular.module('MyApp', [
    'ngStomp'
])
    .controller('MyCtrl', function ($scope, $log, $stomp) {

        $scope.currentTime = null;

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
            })

    })
;