package vn.edu.vhu.ltdd.a5intent.a5_231a290089;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.app.ActivityOptionsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "A5_231A290089";

    // Khóa hằng số dùng chung giữa hai Activity
    public static final String EXTRA_CONTACT = "extra_contact";
    public static final String EXTRA_NGUOI_GUI = "extra_nguoi_gui";

    private EditText edtHoTen, edtDienThoai, edtEmail;
    private TextView tvKetQuaTraVe;

    // Đăng ký nhận kết quả trả về bằng Activity Result API hiện đại
    private final ActivityResultLauncher<Intent> chiTietLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Contact daSua = IntentCompat.getParcelableExtra(
                            result.getData(), EXTRA_CONTACT, Contact.class);
                    if (daSua != null) {
                        edtHoTen.setText(daSua.getHoTen());
                        tvKetQuaTraVe.setText(getString(R.string.returned, daSua.getHoTen()));
                        Log.d(TAG, "Nhận kết quả trả về: " + daSua.getHoTen());
                    }
                } else {
                    tvKetQuaTraVe.setText(R.string.returned_cancel);
                    Log.d(TAG, "Người dùng đã hủy hoặc nhấn Back");
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MainActivity: onCreate");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        edtHoTen = findViewById(R.id.edtHoTen);
        edtDienThoai = findViewById(R.id.edtDienThoai);
        edtEmail = findViewById(R.id.edtEmail);
        tvKetQuaTraVe = findViewById(R.id.tvKetQuaTraVe);

        Button btnChiTiet = findViewById(R.id.btnChiTiet);
        Button btnGoi = findViewById(R.id.btnGoi);
        Button btnWeb = findViewById(R.id.btnWeb);
        Button btnChiaSe = findViewById(R.id.btnChiaSe);

        btnChiTiet.setOnClickListener(v -> moManHinhChiTiet());
        btnGoi.setOnClickListener(v -> goiDien());
        btnWeb.setOnClickListener(v -> moTrangWeb());
        btnChiaSe.setOnClickListener(v -> chiase());

        // NC4: Kiểm tra và nhận dữ liệu văn bản nếu app được mở từ nút Chia sẻ của app khác
        xuLyVanBanChiaSeDen();
    }

    // --- INTENT TƯỜNG MINH (EXPLICIT) ---
    private void moManHinhChiTiet() {
        String hoTen = edtHoTen.getText().toString().trim();
        if (hoTen.isEmpty()) {
            edtHoTen.setError(getString(R.string.err_empty));
            edtHoTen.requestFocus();
            return;
        }

        Contact contact = new Contact(
                hoTen,
                edtDienThoai.getText().toString().trim(),
                edtEmail.getText().toString().trim()
        );

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_CONTACT, contact);
        intent.putExtra(EXTRA_NGUOI_GUI, TAG);

        // NC3: Đóng gói animation trượt ngang
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(
                this, R.anim.slide_in_right, R.anim.slide_out_left);

        // Kích hoạt launcher kèm theo bundle animation
        chiTietLauncher.launch(intent, options);
    }

    // --- INTENT NGẦM ĐỊNH (IMPLICIT) ---
    private void goiDien() {
        String sdt = edtDienThoai.getText().toString().trim();
        if (sdt.isEmpty()) {
            edtDienThoai.setError(getString(R.string.err_empty));
            edtDienThoai.requestFocus();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + sdt));
        moAnToan(intent);
    }

    private void moTrangWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.school_url)));
        moAnToan(intent);
    }

    private void chiase() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text,
                edtHoTen.getText().toString(), edtDienThoai.getText().toString()));
        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
    }

    private void moAnToan(Intent intent) {
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.err_no_app, Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Không có ứng dụng nào xử lý Action: " + intent.getAction(), e);
        }
    }

    // --- BÀI NÂNG CAO NC4: BẮT DỮ LIỆU CHIA SẺ TỪ ỨNG DỤNG KHÁC ---
    private void xuLyVanBanChiaSeDen() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            String vanBan = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (vanBan != null && !vanBan.trim().isEmpty()) {
                edtHoTen.setText(vanBan);
                edtHoTen.setSelection(edtHoTen.getText().length());

                Toast.makeText(this, "Đã nhận văn bản chia sẻ từ ứng dụng khác!", Toast.LENGTH_LONG).show();
                Log.d(TAG, "NC4 - Tiếp nhận thành công chuỗi chia sẻ: " + vanBan);
            }
        }
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        xuLyVanBanChiaSeDen();
    }

    // --- GHI LOG VÒNG ĐỜI CHO PHẦN 6 ---
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "MainActivity: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity: onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "MainActivity: onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity: onDestroy");
    }
}