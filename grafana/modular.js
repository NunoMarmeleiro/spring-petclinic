import http from 'k6/http';
import { check, group } from 'k6';

const BASE_URL = 'http://localhost:8080';

const SCENARIO = __ENV.SCENARIO_TYPE || 'load';

const VUS = parseInt(__ENV.VUS) || 50;


let stages;
if (SCENARIO === 'stress') {
    stages = [
        { duration: '1m', target: 50 },
        { duration: '1m', target: 100 },
        { duration: '1m', target: 150 },
        { duration: '1m', target: 200 },
        { duration: '1m', target: 250 },
        { duration: '1m', target: 0 },
    ];
} else {
    stages = [
        { duration: '30s', target: VUS },
        { duration: '1m', target: VUS },
        { duration: '30s', target: 0 },
    ];
}

export let options = { stages };

const NR_PETS = 2;
const NR_VISITS = 2;
const PET_TYPES = ['bird', 'cat', 'dog', 'hamster', 'lizard', 'snake'];

export function setup() {
    console.log(`Running ${SCENARIO} test`);
    if(SCENARIO === 'load'){
        console.log(`VUs for loading test: ${VUS}`)
    }
}

export default function () {
    const headers = { 'Content-Type': 'application/x-www-form-urlencoded' };

    group('Create Owner', () => {
        const ownerPayload = {
            address: 'Address',
            city: 'City',
            telephone: '1234567890',
            firstName: `OWNER-${__VU}-${__ITER}`,
            lastName: 'LastName',
        };

        const res = http.post(`${BASE_URL}/owners/new`, ownerPayload, {
            headers,
            tags: { name: 'create_owner' },
        });

        check(res, {
            'owner created (200)': (r) => r.status === 200,
            'owner confirmation': (r) => r.body.includes('New Owner Created'),
        });

        const match = res.body.match(/<input[^>]*id="newOwnerId"[^>]*value="(\d+)"[^>]*>/);
        if (!match?.[1]) {
            console.error('Owner ID not found!');
            return;
        }

        const ownerId = match[1];

        group('Add Pets and Visits', () => {
            for (let i = 0; i < NR_PETS; i++) {
                const petType = PET_TYPES[Math.floor(Math.random() * PET_TYPES.length)];
                const petPayload = {
                    name: `PET-${__VU}-${__ITER}-${i}`,
                    birthDate: '2000-01-01',
                    type: petType,
                };

                const petRes = http.post(`${BASE_URL}/owners/${ownerId}/pets/new`, petPayload, {
                    headers,
                    tags: { name: 'add_pet' },
                });

                check(petRes, {
                    'pet created (200)': (r) => r.status === 200,
                    'pet confirmation': (r) => r.body.includes('New Pet has been Added'),
                });

                const petMatch = petRes.body.match(/<input[^>]*id="newPetId"[^>]*value="(\d+)"[^>]*>/);
                if (!petMatch?.[1]) {
                    console.error('Pet ID not found!');
                    return;
                }

                const petId = petMatch[1];

                for (let j = 0; j < NR_VISITS; j++) {
                    const visitPayload = {
                        date: '2010-01-01',
                        description: `VISIT-${__VU}-${__ITER}-${i}-${j}`,
                    };

                    const visitRes = http.post(`${BASE_URL}/owners/${ownerId}/pets/${petId}/visits/new`, visitPayload, {
                        headers,
                        tags: { name: 'add_visit' },
                    });

                    check(visitRes, {
                        'visit created (200)': (r) => r.status === 200,
                        'visit confirmation': (r) => r.body.includes('Your vist has been boked'),
                    });

                    const visitMatch = visitRes.body.match(/<input[^>]*id="newVisitId"[^>]*value="(\d+)"[^>]*>/);
                    if (!visitMatch?.[1]) {
                        console.error('Visit ID not found!');
                        return;
                    }
                }
            }
        });

        group('Delete Owner', () => {
            const deleteRes = http.del(`${BASE_URL}/owners/${ownerId}/delete`, null, {
                tags: { name: 'delete_owner' },
            });

            check(deleteRes, {
                'owner deleted (200)': (r) => r.status === 200,
                'delete confirmation': (r) => r.body.includes('PetClinic'),
            });
        });
    });
}
