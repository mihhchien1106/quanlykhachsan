# Đề 6 — Grand Hotel (Java Spring Boot, không AI)

Dự án giữ nguyên HTML/CSS/JS và dữ liệu mẫu từ `Grand_Hotel_AI_Fusion (2)(3).html`, đồng thời bỏ sidebar/menu và cửa sổ AI. Backend viết bằng **Java 21 Spring Boot**, lưu dữ liệu qua **PostgreSQL**. Đề bài yêu cầu PostgreSQL + pgAdmin, Nginx, Prometheus + Grafana, Loki + Promtail và Docker Compose.

## Chạy trên Windows (PowerShell)

```powershell
Copy-Item .env.example .env
# Sửa mật khẩu mạnh trong .env
# Máy cần bật Docker Desktop
docker compose config --quiet
docker compose up -d --build
docker compose ps
```

Truy cập:
- Website **http://localhost:8088**
- pgAdmin **http://localhost:5050** (server: `db`, port 5432, database: `grandhotel`)
- Grafana **http://localhost:3000**
- Prometheus **http://localhost:9090**

Tài khoản demo có sẵn trong file HTML: admin@gmail.com / admin123; letan@gmail.com / letan123. Khách hàng có thể đăng ký. **Cảnh báo:** đây là đăng nhập mô phỏng ở trình duyệt, không an toàn cho triển khai Internet. Đổi sang xác thực máy chủ trước khi sử dụng thực tế.

## Dữ liệu

Có sẵn dữ liệu mẫu về phòng, trạng thái, giá phòng, dịch vụ trong giao diện gốc. Khi mở lần đầu, ứng dụng hiển thị dữ liệu mẫu; thay đổi được gửi về bảng `hotel_state` PostgreSQL, khôi phục lúc truy cập sau. Dữ liệu được đồng bộ dạng snapshot JSON theo khóa, phù hợp bài demo; đây **không phải** mô hình quan hệ chuẩn hoá cho môi trường production. Nếu dùng nhiều trình duyệt cùng lúc, lưu cuối có thể ghi đè dữ liệu trước đó.

## Kiểm tra database

Vào pgAdmin → Add New Server → Host `db`, Username và Password ở `.env`, database `grandhotel`. Chạy `SELECT state_key,updated_at FROM hotel_state ORDER BY updated_at DESC;`.

## Giám sát

Grafana Explore → datasource Prometheus, thử `up`, `http_server_requests_seconds_count`, `pg_up`, `nginx_up`. Grafana Explore → Loki, thử 3 câu LogQL:

```logql
{job="docker"}
{job="docker"} |= "ERROR"
count_over_time({job="docker"}[5m])
```

**Lưu ý:** Promtail config đọc `/var/lib/docker/containers`, hoạt động với Docker Engine trên Linux; Docker Desktop Windows có thể không xuất được log theo đường dẫn này. Khi đó cần thay nguồn scrape bằng Docker logging driver Loki hoặc cơ chế chuyển tiếp log tương thích Docker Desktop.

## Lộ trình commit GitHub

1. `git commit -m "commit 1: Java Spring Boot PostgreSQL pgAdmin Nginx"`
2. `git commit -m "commit 2: Prometheus Grafana exporters"`
3. `git commit -m "commit 3: Loki Promtail hardening docs"`

Phân chia các thay đổi theo ba commit thực tế thay vì tạo ba commit giống nhau. Chụp hình màn hình web, database, các metric, và LogQL để bổ sung báo cáo tối thiểu 10 trang theo yêu cầu môn học.

## Bảo mật / giới hạn

App và nginx chạy non-root; PostgreSQL không mở port host; `backend` internal network; Nginx có header bảo mật; config nhận mật khẩu từ `.env`. pgAdmin, Prometheus và Grafana là cổng local demo: không mở ra Internet. API `/api/state` hiện chưa có xác thực phía server, vì giữ nguyên mô hình demo HTML gốc; cần Spring Security và phân quyền API trước khi sử dụng thật.
