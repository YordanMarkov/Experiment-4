import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 50, // change the VUs (virtual users) as needed (e.g., 10, 50, 100)
  duration: '30s',
};

export default function () {
  const res = http.get('http://localhost:8080/api/recipes/random');

  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  sleep(1);
}