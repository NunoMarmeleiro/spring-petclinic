import http from 'k6/http';
import { check, group } from 'k6';
import { Trend } from 'k6/metrics';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";


const deleteOwnerTrend = new Trend('delete_owner_duration', true);
const postOwnerTrend = new Trend('post_owner_duration', true);


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
const PET_TYPES = ['bird', 'cat', 'dog', 'hamster', 'lizard', 'snake'];

export function setup() {
    console.log(`Running ${SCENARIO} test`);
    if(SCENARIO === 'load'){
        console.log(`VUs for loading test: ${VUS}`)
    }
}



export function handleSummary(data) {

    const postCount = data.metrics.post_owner_duration.values.count;
    const deleteCount = data.metrics.delete_owner_duration.values.count;

    const count = data.metrics.iteration_duration.values.count;
    const avg = data.metrics.iteration_duration.values.avg;
    const duration = (count * avg) / 1000;
   
    const postThroughput = postCount / duration;
    const deleteThroughput = deleteCount / duration;

    console.log(`Duration: ${duration.toFixed(2)}s`);

    console.log(`Post Owner Requests: ${postCount}`);
    console.log(`Post Owner Throughput: ${postThroughput.toFixed(2)} reqs/s`);

    console.log(`Delete Owner Requests: ${deleteCount}`);
    console.log(`Delete Owner Throughput: ${deleteThroughput.toFixed(2)} reqs/s`);

    data.metrics.post_owner_duration.values.rate = postThroughput;
    data.metrics.delete_owner_duration.values.rate = deleteThroughput;

    const vus = __ENV.VUS || 'default';
    const scenario = __ENV.SCENARIO_TYPE || 'load';

    const filename = `nonmodular-results-${scenario}-${vus}vus.html`;

    return {
        [filename]: htmlReport(data),
    };
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

        const ownerRes = http.post(`${BASE_URL}/owners/new`, ownerPayload, {
            headers,
            tags: { name: 'create_owner' },
        });
        const duration = ownerRes.timings.duration;
        postOwnerTrend.add(duration);
        check(ownerRes, {
            'owner created (200)': (r) => r.status === 200,
            'owner confirmation': (r) => r.body.includes('New Owner Created'),
        });

        const match = ownerRes.body.match(/<input[^>]*id="newOwnerId"[^>]*value="(\d+)"[^>]*>/);
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
                        'visit confirmation': (r) => r.body.includes('Your visit has been booked'),
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
            const duration = deleteRes.timings.duration;
            deleteOwnerTrend.add(duration);
            check(deleteRes, {
                'owner deleted (200)': (r) => r.status === 200,
                'delete confirmation': (r) => r.body.includes('PetClinic'),
            });
        });
    });
}
