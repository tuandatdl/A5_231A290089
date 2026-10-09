package vn.edu.vhu.ltdd.a5intent.a5_231a290089;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "A5_231A290089";

    private Contact contact;
    private EditText edtHoTenMoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "DetailActivity: onCreate");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        TextView tvThongTin = findViewById(R.id.tvThongTin);
        TextView tvNguoiGui = findViewById(R.id.tvNguoiGui);
        edtHoTenMoi = findViewById(R.id.edtHoTenMoi);
        Button btnLuu = findViewById(R.id.btnLuu);
        Button btnHuy = findViewById(R.id.btnHuy);

        // Lấy dữ liệu gửi từ MainActivity sang
        contact = IntentCompat.getParcelableExtra(
                getIntent(), MainActivity.EXTRA_CONTACT, Contact.class);
        String nguoiGui = getIntent().getStringExtra(MainActivity.EXTRA_NGUOI_GUI);

        if (contact == null) {
            tvThongTin.setText(R.string.no_data);
            Log.w(TAG, "Không nhận được Contact từ Intent");
            return;
        }

        tvThongTin.setText(getString(R.string.detail_format,
                contact.getHoTen(), contact.getDienThoai(), contact.getEmail()));
        tvNguoiGui.setText(getString(R.string.sent_by, nguoiGui));
        edtHoTenMoi.setText(contact.getHoTen());

        btnLuu.setOnClickListener(v -> luuVaQuayLai());
        btnHuy.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }

    private void luuVaQuayLai() {
        String hoTenMoi = edtHoTenMoi.getText().toString().trim();
        if (hoTenMoi.isEmpty()) {
            edtHoTenMoi.setError(getString(R.string.err_empty));
            return;
        }

        contact.setHoTen(hoTenMoi);
        Intent ketQua = new Intent();
        ketQua.putExtra(MainActivity.EXTRA_CONTACT, contact);
        setResult(RESULT_OK, ketQua);
        finish();

        // NC3: Hiệu ứng trượt khi đóng Activity
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    // --- GHI LOG VÒNG ĐỜI CHO PHẦN 6 ---
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "DetailActivity: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "DetailActivity: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "DetailActivity: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "DetailActivity: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DetailActivity: onDestroy");
    }
}