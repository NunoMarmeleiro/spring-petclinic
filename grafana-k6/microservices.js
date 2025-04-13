import http from 'k6/http';
import { check, group } from 'k6';

const BASE_URL = 'http://localhost:8080/api';

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
} else if (SCENARIO === 'test' ){
    stages = [
        { vus: VUS },
    ]
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
const PET_TYPES = [1,2,3,4,5,6];

export function setup() {
    console.log(`Running ${SCENARIO} test`);
    if(SCENARIO === 'load'){
        console.log(`VUs for loading test: ${VUS}`)
    }
}

export default function () {
    const headers = { 'Content-Type': 'application/json' };

    group('Create Owner', () => {
        const ownerPayload = JSON.stringify({
            address: 'Address',
            city: 'City',
            telephone: '1234567890',
            firstName: `OWNER-${__VU}-${__ITER}`,
            lastName: 'LastName',
        });

        const ownerRes = http.post(`${BASE_URL}/owner/owners`, ownerPayload, {
            headers,
            tags: { name: 'create_owner' },
        });

        check(ownerRes, {
            'owner created (201)': (r) => r.status === 201
        });
        const ownerId = JSON.parse(ownerRes.body).id;

        group('Add Pets and Visits', () => {
            for (let i = 0; i < NR_PETS; i++) {
                const petType = PET_TYPES[Math.floor(Math.random() * PET_TYPES.length)];
                const petPayload = JSON.stringify({
                    name: `PET-${__VU}-${__ITER}-${i}`,
                    birthDate: '2000-01-01',
                    typeId: petType,
                });

                const petRes = http.post(`${BASE_URL}/pet/owners/${ownerId}/pets`, petPayload, {
                    headers,
                    tags: { name: 'add_pet' },
                });
                check(petRes, {
                    'pet created (201)': (r) => r.status === 201
                });
                const petId = JSON.parse(petRes.body).id;

                for (let j = 0; j < NR_VISITS; j++) {
                    const visitPayload = JSON.stringify({
                        date: '2010-01-01',
                        description: `VISIT-${__VU}-${__ITER}-${i}-${j}`,
                    });

                    const visitRes = http.post(`${BASE_URL}/visit/owners/${ownerId}/pets/${petId}/visits`, visitPayload, {
                        headers,
                        tags: { name: 'add_visit' },
                    });

                    check(visitRes, {
                        'visit created (201)': (r) => r.status === 201
                    });
                }
            }
        });

        group('Delete Owner', () => {
            const deleteRes = http.del(`${BASE_URL}/owner/owners/${ownerId}`, null, {
                tags: { name: 'delete_owner' },
            });

            check(deleteRes, {
                'owner deleted (204)': (r) => r.status === 204
            });
        });
    });
}
