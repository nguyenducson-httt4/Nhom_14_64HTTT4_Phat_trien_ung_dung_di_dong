package tlu.cse.ht64.apphoctienganh.adapter;

// Import các lớp cần thiết từ Android SDK và ứng dụng
import android.content.Context; // Lớp ngữ cảnh để truy cập tài nguyên và dịch vụ hệ thống
import android.content.Intent; // Để chuyển đổi giữa các Activity
import android.view.LayoutInflater; // Để chuyển layout XML thành View
import android.view.View; // Lớp cơ sở cho các thành phần giao diện
import android.view.ViewGroup; // Nhóm các View trong RecyclerView
import android.widget.Button; // Nút bấm
import android.widget.ImageView; // Hiển thị hình ảnh (ví dụ: biểu tượng yêu thích)
import android.widget.LinearLayout; // Bố cục tuyến tính
import android.widget.TextView; // Hiển thị văn bản
import android.widget.Toast; // Hiển thị thông báo ngắn

import androidx.annotation.NonNull; // Đánh dấu tham số hoặc giá trị trả về không được null
import androidx.recyclerview.widget.RecyclerView; // Hiển thị danh sách cuộn được

import java.util.List; // Giao diện danh sách

import tlu.cse.ht64.apphoctienganh.R; // Tài nguyên ứng dụng
import tlu.cse.ht64.apphoctienganh.activity.EditWordActivity; // Activity chỉnh sửa từ vựng
import tlu.cse.ht64.apphoctienganh.database.SQLiteHelper; // Quản lý cơ sở dữ liệu SQLite
import tlu.cse.ht64.apphoctienganh.model.Word; // Mô hình dữ liệu từ vựng
import tlu.cse.ht64.apphoctienganh.utils.SessionManager; // Quản lý phiên đăng nhập

// Lớp VocabularyAdapter kế thừa từ RecyclerView.Adapter để hiển thị danh sách từ vựng
public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.WordViewHolder> {
    // Danh sách từ vựng để hiển thị
    private List<Word> wordList;
    // Ngữ cảnh của ứng dụng (thường là Activity)
    private Context context;
    // Đối tượng SQLiteHelper để tương tác với cơ sở dữ liệu
    private SQLiteHelper dbHelper;
    // Đối tượng SessionManager để quản lý phiên đăng nhập
    private SessionManager session;
    // Kiểm tra xem người dùng có phải quản trị viên không (mặc định false)
    private boolean isAdmin = false;
    // Kiểm tra xem adapter có ở chế độ xóa không (mặc định false)
    private boolean deleteMode = false;

    // Constructor khởi tạo adapter
    public VocabularyAdapter(Context context) {
        this.context = context; // Lưu ngữ cảnh
        this.dbHelper = new SQLiteHelper(context); // Khởi tạo SQLiteHelper
        this.session = new SessionManager(context); // Khởi tạo SessionManager
        // Lấy danh sách từ vựng của người dùng từ cơ sở dữ liệu
        this.wordList = dbHelper.getAllWords(session.getUserId());
    }

    // Thiết lập chế độ quản trị viên hoặc xóa
    public void setAdminMode(boolean isAdmin, boolean deleteMode) {
        this.isAdmin = isAdmin; // Cập nhật trạng thái quản trị viên
        this.deleteMode = deleteMode; // Cập nhật trạng thái chế độ xóa
        notifyDataSetChanged(); // Thông báo RecyclerView làm mới toàn bộ dữ liệu
    }

    // Cập nhật danh sách từ vựng mới
    public void updateWordList(List<Word> newWordList) {
        this.wordList = newWordList; // Gán danh sách từ vựng mới
        notifyDataSetChanged(); // Thông báo RecyclerView làm mới dữ liệu
    }

    // Tạo ViewHolder cho mỗi mục từ vựng
    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Chuyển layout item_word.xml thành View
        View view = LayoutInflater.from(context).inflate(R.layout.item_word, parent, false);
        // Trả về ViewHolder mới với View vừa tạo
        return new WordViewHolder(view);
    }

    // Gắn dữ liệu từ vựng vào ViewHolder
    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        // Lấy từ vựng tại vị trí position
        Word word = wordList.get(position);

        // Hiển thị từ tiếng Anh
        holder.tvEnglish.setText(word.getEnglish());
        // Hiển thị phiên âm
        holder.tvPhonetic.setText(word.getPhonetic());
        // Hiển thị nghĩa tiếng Việt
        holder.tvVietnamese.setText(word.getVietnamese());
        // Hiển thị biểu tượng yêu thích (sao sáng nếu yêu thích, sao mờ nếu không)
        holder.ivFavorite.setImageResource(word.isFavorite() ? android.R.drawable.star_on : android.R.drawable.star_off);
        // Hiển thị trạng thái học (Đã học/Chưa học)
        holder.tvLearnedStatus.setText(word.isLearned() ? "Đã học" : "Chưa học");
        // Đặt màu cho trạng thái học (xanh nếu đã học, đỏ nếu chưa học)
        holder.tvLearnedStatus.setTextColor(word.isLearned() ?
                context.getResources().getColor(android.R.color.holo_green_dark) :
                context.getResources().getColor(android.R.color.holo_red_dark));

        // Hiển thị nút "Học" hoặc "Đã học" dựa trên trạng thái học
        holder.btnLearn.setText(word.isLearned() ? "Đã học" : "Học");
        // Vô hiệu hóa nút nếu từ đã học
        holder.btnLearn.setEnabled(!word.isLearned());

        // Hiển thị mặt trước (frontLayout) và ẩn mặt sau (backLayout) ban đầu
        holder.frontLayout.setVisibility(View.VISIBLE);
        holder.backLayout.setVisibility(View.GONE);

        // Xử lý sự kiện nhấn nút "Học"
        holder.btnLearn.setOnClickListener(v -> {
            // Nếu từ chưa được học
            if (!word.isLearned()) {
                // Đánh dấu từ đã học
                word.setLearned(true);
                // Cập nhật trạng thái học vào cơ sở dữ liệu
                dbHelper.updateUserWord(session.getUserId(), word.getId(), true, word.isFavorite());
                // Cập nhật văn bản nút thành "Đã học"
                holder.btnLearn.setText("Đã học");
                // Vô hiệu hóa nút
                holder.btnLearn.setEnabled(false);
                // Cập nhật trạng thái hiển thị thành "Đã học"
                holder.tvLearnedStatus.setText("Đã học");
                // Đặt màu xanh cho trạng thái
                holder.tvLearnedStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            }
        });

        // Xử lý sự kiện nhấn vào mục từ vựng
        holder.itemView.setOnClickListener(v -> {
            // Nếu là quản trị viên
            if (isAdmin) {
                // Nếu ở chế độ xóa
                if (deleteMode) {
                    // Xóa từ khỏi cơ sở dữ liệu
                    dbHelper.deleteWord(word.getId());
                    // Xóa từ khỏi danh sách
                    wordList.remove(position);
                    // Thông báo RecyclerView xóa mục
                    notifyItemRemoved(position);
                    // Cập nhật phạm vi mục còn lại
                    notifyItemRangeChanged(position, wordList.size());
                    // Hiển thị thông báo xóa thành công
                    Toast.makeText(context, "Đã xóa từ", Toast.LENGTH_SHORT).show();
                } else {
                    // Nếu không ở chế độ xóa, mở Activity chỉnh sửa từ
                    Intent intent = new Intent(context, EditWordActivity.class);
                    // Gửi dữ liệu từ vựng qua Intent
                    intent.putExtra("word", word);
                    // Mở EditWordActivity
                    context.startActivity(intent);
                }
            } else {
                // Nếu không phải quản trị viên, chuyển đổi giữa mặt trước và mặt sau
                if (holder.frontLayout.getVisibility() == View.VISIBLE) {
                    // Ẩn mặt trước, hiển thị mặt sau (có thể để học từ)
                    holder.frontLayout.setVisibility(View.GONE);
                    holder.backLayout.setVisibility(View.VISIBLE);
                } else {
                    // Hiển thị mặt trước, ẩn mặt sau
                    holder.frontLayout.setVisibility(View.VISIBLE);
                    holder.backLayout.setVisibility(View.GONE);
                }
            }
        });

        // Xử lý sự kiện nhấn biểu tượng yêu thích
        holder.ivFavorite.setOnClickListener(v -> {
            // Đảo trạng thái yêu thích (bật/tắt)
            word.setFavorite(!word.isFavorite());
            // Cập nhật trạng thái yêu thích vào cơ sở dữ liệu
            dbHelper.updateUserWord(session.getUserId(), word.getId(), word.isLearned(), word.isFavorite());
            // Cập nhật biểu tượng yêu thích
            holder.ivFavorite.setImageResource(word.isFavorite() ? android.R.drawable.star_on : android.R.drawable.star_off);
        });
    }

    // Trả về số lượng từ vựng trong danh sách
    @Override
    public int getItemCount() {
        return wordList.size();
    }

    // Lớp ViewHolder để lưu trữ các thành phần giao diện của mỗi mục từ vựng
    public static class WordViewHolder extends RecyclerView.ViewHolder {
        // TextView hiển thị từ tiếng Anh, phiên âm, nghĩa tiếng Việt, trạng thái học
        TextView tvEnglish, tvPhonetic, tvVietnamese, tvLearnedStatus;
        // ImageView hiển thị biểu tượng yêu thích
        ImageView ivFavorite;
        // LinearLayout cho mặt trước và mặt sau của mục từ vựng
        LinearLayout frontLayout, backLayout;
        // Button để học từ
        Button btnLearn;

        // Constructor khởi tạo ViewHolder
        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            // Liên kết các thành phần giao diện với biến
            tvEnglish = itemView.findViewById(R.id.tvEnglish);
            tvPhonetic = itemView.findViewById(R.id.tvPhonetic);
            tvVietnamese = itemView.findViewById(R.id.tvVietnamese);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            tvLearnedStatus = itemView.findViewById(R.id.tvLearnedStatus);
            frontLayout = itemView.findViewById(R.id.frontLayout);
            backLayout = itemView.findViewById(R.id.backLayout);
            btnLearn = itemView.findViewById(R.id.btnLearn);
        }
    }
}