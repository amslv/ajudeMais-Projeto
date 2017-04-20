/**
 * @ngdoc Service
 * @name authInterceptor
 *
 * @description
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */

(function () {
    angular.module('amApp').factory("authInterceptor", authInterceptor);

    authInterceptor.$inject = ['$rootScope', '$q', '$sessionStorage'];

    function authInterceptor($rootScope, $q, $sessionStorage) {
        var service = {
            request: request,
            response: response
        };

        return service;

        function request(config) {
            config.headers = config.headers || {};
            var token = $sessionStorage.authToken;
            if (token && (!config.url.startsWith('https://viacep.com.br/ws'))) {
                config.headers.Authorization = token;
            }
            return config;
        }

        function response(config) {
            var token = config.headers.Authorization;
            if (token) {
                $sessionStorage.authToken = token;
            }
            return config;
        }
    }
})();







