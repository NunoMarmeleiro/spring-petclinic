'use strict';

angular.module('ownerForm')
    .controller('OwnerFormController', ["$http", '$state', '$stateParams', function ($http, $state, $stateParams) {
        var self = this;

        var ownerId = $stateParams.ownerId || 0;

        if (!ownerId) {
            self.owner = {};
        } else {
            $http.get("api/owner/owners/" + ownerId).then(function (resp) {
                self.owner = resp.data;
            });
        }

        self.submitOwnerForm = function () {
            var id = self.owner.id;

            if (id) {
                $http.put('api/owner/owners/' + id, self.owner).then(function () {
                    $state.go('ownerDetails', {ownerId: ownerId});
                });
            } else {
                $http.post('api/owner/owners', self.owner).then(function () {
                    $state.go('owners');
                });
            }
        };

        self.deleteOwner = function () {
            if (confirm("Are you sure you want to delete this owner?")) {
                $http.delete('api/owner/owners/' + self.owner.id)
                    .then(function () {
                        $state.go('owners'); // Redirect to the owners list after deletion
                    })
                    .catch(function (error) {
                        console.error("Error deleting owner:", error);
                    });
            }
        };


    }]);
