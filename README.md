# INT4211 - LẬP TRÌNH TRÊN CÁC THIẾT BỊ DI ĐỘNG
## BÁO CÁO THỰC HÀNH LAB A5: CHUYỂN MÀN HÌNH & TRUYỀN DỮ LIỆU BẰNG INTENT

---

### THÔNG TIN SINH VIÊN
* **Họ và tên:** Đỗ Lê Tuấn Đạt
* **Mã số sinh viên (MSSV):** 231A290089
* **Lớp học phần:** Lập trình thiết bị di động (INT4211)
* **Môi trường thực thi:** Android Studio trên macOS (Apple Silicon)
* **Thiết bị thử nghiệm:** Máy ảo Pixel 8 (Android 17, API 37.1 – aarch64)

---

### CÁC MỤC ĐÃ HOÀN THÀNH

#### 1. Màn hình Danh bạ mini & Truyền nhận dữ liệu hai chiều
* **Intent tường minh & Parcelable:** 
  * Khởi tạo lớp `Contact` đóng gói dữ liệu gồm 3 trường: họ tên, số điện thoại, email bằng cơ chế `Parcelable` chuẩn Android giúp tối ưu tốc độ truyền nhận.
  * Truyền cả đối tượng `Contact` sang `DetailActivity` qua `intent.putExtra(EXTRA_CONTACT, contact)`.
  * Màn hình 2 nhận đối tượng bằng phương thức an toàn `IntentCompat.getParcelableExtra(...)`.
* **Nhận kết quả trả về bằng Activity Result API:**
  * Sử dụng `registerForActivityResult` kết hợp `StartActivityForResult` để nhận dữ liệu phản hồi mà không dùng phương thức cũ `onActivityResult`.
  * Xử lý trọn vẹn cả 2 trường hợp: `RESULT_OK` (người dùng bấm Lưu & quay lại) và `RESULT_CANCELED` (người dùng bấm Hủy hoặc nút Back).

#### 2. Ba Intent ngầm định & Bẫy lỗi an toàn
* **Gọi điện (`ACTION_DIAL`):** Mở bàn phím quay số với số điện thoại đã điền sẵn `tel:...`, không cần xin quyền nguy hiểm.
* **Mở trang web (`ACTION_VIEW`):** Điều hướng mở trang web trường `https://vhu.edu.vn` bằng trình duyệt mặc định.
* **Chia sẻ (`ACTION_SEND`):** Bọc trong `Intent.createChooser` hiển thị danh sách ứng dụng chia sẻ văn bản.
* **Phòng ngừa crash:** Bọc toàn bộ Intent ngầm định trong khối `try-catch ActivityNotFoundException` nhằm tương thích trên Android 11+ với cơ chế giới hạn hiển thị gói.

#### 3. Khảo nghiệm chuỗi Callback vòng đời (Phần 6)
* Ghi log `Log.d` đầy đủ các hàm: `onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, `onRestart`, `onDestroy` trên cả hai Activity.
* Khảo sát chính xác 5 kịch bản thực nghiệm, chứng minh cơ chế Back Stack và thời điểm kích hoạt của `onPause` / `onStop`.

#### 4. Bài nâng cao NC3: Hiệu ứng chuyển màn hình (Transition Animation)
* Xây dựng hiệu ứng trượt ngang trong `res/anim/` (`slide_in_right.xml`, `slide_out_left.xml`).
* Kích hoạt chuyển cảnh qua `ActivityOptionsCompat.makeCustomAnimation(...)` khi mở màn hình chi tiết.

#### 5. Bài nâng cao NC4: Nhận dữ liệu chia sẻ từ ứng dụng khác
* Đăng ký thẻ `<intent-filter>` với action `ACTION_SEND` và `mimeType="text/plain"` trong AndroidManifest.
* Bắt chuỗi văn bản từ `getIntent().getStringExtra(Intent.EXTRA_TEXT)` và tự động điền vào ô Họ tên khi ứng dụng khác chia sẻ sang.
