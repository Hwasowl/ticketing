import random
from locust import task, FastHttpUser

class IssueCoupon(FastHttpUser):
    connection_timeout = 10.0
    network_timeout = 10.0

    @task
    def issue(self):
        payload = {
            "userId" : random.randint(1, 10000000),
            "couponId" : 13,
        }
        with self.rest("POST", "/v3/issue", json=payload):
            pass
