import http from 'k6/http';
import { check, group } from 'k6';
import { Trend } from 'k6/metrics';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";

const deleteOwnerTrend = new Trend('delete_owner_duration', true);


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
} else if (SCENARIO === 'test' ) {
    stages = [
        { duration: '1s', target: 1 }
    ];
} else {
    stages = [
        { duration: '30s', target: VUS },
        { duration: '1m', target: VUS },
        { duration: '30s', target: 0 },
    ];
}

export let options = { 
    summaryTrendStats: [
        "avg",
        "min",
        "med",
        "max",
        "p(50)",
        "p(90)",
        "p(95)",
        "p(99)",
        "p(99.99)",
        "count"
      ],
      summaryTimeUnit: "ms",
    stages
 };

const NR_PETS = 2;
const NR_VISITS = 2;
const PET_TYPES = [1,2,3,4,5,6];

export function setup() {
    console.log(`Running ${SCENARIO} test`);
    if(SCENARIO === 'load'){
        console.log(`VUs for loading test: ${VUS}`)
    }
}

export function handleSummary(data) {
    const deleteCount = data.metrics.delete_owner_duration.values.count;


    const count = data.metrics.iteration_duration.values.count;
    const avg = data.metrics.iteration_duration.values.avg;
    const duration = (count * avg) / 1000;

    const deleteThroughput = deleteCount / duration;
    

    console.log(`Delete Requests: ${deleteCount}`);
    console.log(`Duration: ${duration.toFixed(2)}s`);
    console.log(`Delete Throughput: ${deleteThroughput.toFixed(2)} reqs/s`);

    data.metrics.delete_owner_duration.values.rate = deleteThroughput;

    const vus = __ENV.VUS || 'default';
    const scenario = __ENV.SCENARIO_TYPE || 'load';

    const filename = `microservices-results-${scenario}-${vus}vus.html`;

    return {
        [filename]: htmlReport(data),
    };
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
            const duration = deleteRes.timings.duration;
            deleteOwnerTrend.add(duration);
            check(deleteRes, {
                'owner deleted (204)': (r) => r.status === 204
            });
        });
    });
}
