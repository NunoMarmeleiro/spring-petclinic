import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = 'http://localhost:8080'; 

/*
export let options = {
    stages: [
        { duration: '10s', target: 20 },  // Ramp up to 20 VUs in 10 sec
        { duration: '30s', target: 50 },  // Stay at 50 VUs for 30 sec
        { duration: '10s', target: 0 }    // Ramp down to 0 VUs in 10 sec
    ],
    thresholds: {
        'http_req_duration': ['p(95)<500'], // 95% of requests should be <500ms
        'http_req_failed': ['rate<0.01'],   // Failure rate should be <1%
    },
};
*/

export let options = {
    vus: 1,
    duration: '1s',
};

export default function () {
    const createOwnerUrl  = `${BASE_URL}/owners/new`;

    const ownerPayload  = {
        address: 'teste',
        city: 'teste',
        telephone: '1234567890',
        firstName: 'teste',
        lastName: 'teste',
    };

    const headers = {
        'Content-Type': 'application/x-www-form-urlencoded',
    };

    const ownerRes  = http.post(createOwnerUrl, ownerPayload, { headers: headers });

    check(ownerRes , {
        'status is 200': (r) => r.status === 200,
        'response contains title': (r) => r.body.includes('PetClinic'),
        'alert owner created': (r) => r.body.includes('New Owner Created'),
    });

    const newOwnerIdMatch = ownerRes.body.match(/<input[^>]*id="newOwnerId"[^>]*value="(\d+)"[^>]*>/);    
    let ownerId = null;
    
    if (newOwnerIdMatch && newOwnerIdMatch[1]) {
        ownerId = newOwnerIdMatch[1];
        console.log(`Created Owner ID: ${ownerId}`);
    } else {
        console.error('Owner ID not found!');
        return;
    }

    const addPetUrl = `${BASE_URL}/owners/${ownerId}/pets/new`;

    const petPayload = {
        name: 'lil bro2',
        birthDate: '2000-10-10',
        type: 'bird',
    };

    const petRes = http.post(addPetUrl, petPayload, { headers: headers });
    check(petRes , {
        'status is 200': (r) => r.status === 200,
        'response contains title': (r) => r.body.includes('PetClinic'),
        'alert owner created': (r) => r.body.includes('New Pet has been Added'),
    });

    const newPetIdMatch = petRes.body.match(/<input[^>]*id="newPetId"[^>]*value="(\d+)"[^>]*>/);    
    let petId = null;
    
    if (newPetIdMatch && newPetIdMatch[1]) {
        petId = newPetIdMatch[1];
        console.log(`Created Pet ID: ${petId}`);
    } else {
        console.error('Pet ID not found!');
        return;
    }


    const addVisitUrl = `${BASE_URL}/owners/${ownerId}/pets/${petId}/visits/new`;

    const visitPayload = {
        birthDate: '2000-01-01',
        description: 'test',
    };

    const visitRes = http.post(addVisitUrl, visitPayload, { headers: headers });
    check(visitRes , {
        'status is 200': (r) => r.status === 200,
        'response contains title': (r) => r.body.includes('PetClinic'),
        'alert owner created': (r) => r.body.includes('Your vist has been boked'),
    });

    const newVisitIdMatch = visitRes.body.match(/<input[^>]*id="newVisitId"[^>]*value="(\d+)"[^>]*>/);
    let visitId = null;
    
    if (newVisitIdMatch && newVisitIdMatch[1]) {
        visitId = newVisitIdMatch[1];
        console.log(`Visit Pet ID: ${visitId}`);
    } else {
        console.error('Visit ID not found!');
        return;
    }


    sleep(1); // pause for 1 second between iterations
}


