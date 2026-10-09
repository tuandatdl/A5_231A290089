package vn.edu.vhu.ltdd.a5intent.a5_231a290089;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Đối tượng Contact được đóng gói bằng Parcelable để truyền dữ liệu qua Intent.
 * Thứ tự ghi trong writeToParcel và đọc trong constructor Parcel phải trùng khớp 100%.
 */
public class Contact implements Parcelable {
    private String hoTen;
    private String dienThoai;
    private String email;

    public Contact(String hoTen, String dienThoai, String email) {
        this.hoTen = hoTen;
        this.dienThoai = dienThoai;
        this.email = email;
    }

    protected Contact(Parcel in) {
        hoTen = in.readString();
        dienThoai = in.readString();
        email = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hoTen);
        dest.writeString(dienThoai);
        dest.writeString(email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public String getEmail() {
        return email;
    }
}