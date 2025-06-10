import random
from locust import HttpUser, task, between

class CouponUser(HttpUser):
    wait_time = between(0.01, 0.02)  # 빠른 부하

    ports = [8080, 8081, 8082]

    @task
    def issue_coupon(self):
        # 랜덤 포트 선택
        port = random.choice(self.ports)
        # base_url을 직접 바꿈
        self.client.base_url = f"http://localhost:{port}"

        payload = {
            "couponId": 16,
            "userId": random.randint(1, 10000000)
        }
        self.client.post("/v3/issue", json=payload)
