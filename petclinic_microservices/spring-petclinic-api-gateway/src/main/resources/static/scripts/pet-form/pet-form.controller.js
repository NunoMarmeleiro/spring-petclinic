'use strict';

angular.module('petForm')
    .controller('PetFormController', ['$http', '$state', '$stateParams', function ($http, $state, $stateParams) {
        var self = this;
        var ownerId = $stateParams.ownerId || 0;

        $http.get('api/pet/petTypes').then(function (resp) {
            self.types = resp.data;
        }).then(function () {

            var petId = $stateParams.petId || 0;

            if (petId) { // edit
                $http.get("api/pet/owners/" + ownerId + "/pets/" + petId).then(function (resp) {
                    self.pet = resp.data;
                    self.pet.birthDate = new Date(self.pet.birthDate);
                    self.petTypeId = "" + self.pet.type.id;
                });
            } else {
                $http.get('api/owner/owners/' + ownerId).then(function (resp) {
                    self.pet = {
                        owner: resp.data.firstName + " " + resp.data.lastName
                    };
                    self.petTypeId = "1";
                })

            }
        });

        self.submit = function () {
            var id = self.pet.id || 0;

            var data = {
                id: id,
                name: self.pet.name,
                birthDate: self.pet.birthDate,
                typeId: self.petTypeId
            };

            var req;
            if (id) {
                req = $http.put("api/pet/owners/" + ownerId + "/pets/" + id, data);
            } else {
                req = $http.post("api/pet/owners/" + ownerId + "/pets", data);
            }

            req.then(function () {
                $state.go('ownerDetails', {ownerId: ownerId});
            });
        };

        self.deletePet = function () {
            if (confirm("Are you sure you want to delete this owner?")) {
                $http.delete('api/pet/owners/'+ownerId+'/pets/' + self.pet.id)
                    .then(function () {
                        $state.go('ownerDetails', {ownerId: ownerId});
                    })
                    .catch(function (error) {
                        console.error("Error deleting owner:", error);
                    });
            }
        };

    }]);
