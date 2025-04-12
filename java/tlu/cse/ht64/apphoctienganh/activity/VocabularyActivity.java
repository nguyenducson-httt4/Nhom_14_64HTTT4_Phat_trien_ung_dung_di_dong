package tlu.cse.ht64.apphoctienganh.activity;

// Import các lớp cần thiết từ Android SDK và ứng dụng
import android.content.Intent; // Để chuyển đổi giữa các Activity
import android.os.Bundle; // Lưu trữ trạng thái Activity
import android.text.Editable; // Đối tượng văn bản có thể chỉnh sửa
import android.text.TextWatcher; // Theo dõi thay đổi trong ô nhập liệu
import android.util.Log; // Ghi log để debug
import android.view.View; // Lớp cơ sở cho các thành phần giao diện
import android.widget.AdapterView; // Xử lý sự kiện chọn mục trong Spinner
import android.widget.ArrayAdapter; // Bộ điều hợp cho Spinner
import android.widget.Button; // Nút bấm
import android.widget.EditText; // Ô nhập liệu
import android.widget.Spinner; // Menu thả xuống
import android.widget.TextView; // Hiển thị văn bản
import android.widget.Toast; // Hiển thị thông báo ngắn

import androidx.appcompat.app.AppCompatActivity; // Lớp cơ sở cho Activity
import androidx.recyclerview.widget.LinearLayoutManager; // Quản lý bố cục danh sách
import androidx.recyclerview.widget.RecyclerView; // Hiển thị danh sách cuộn được

import java.util.ArrayList; // Danh sách động
import java.util.List; // Giao diện danh sách

import tlu.cse.ht64.apphoctienganh.R; // Tài nguyên ứng dụng
import tlu.cse.ht64.apphoctienganh.adapter.VocabularyAdapter; // Bộ điều hợp cho RecyclerView
import tlu.cse.ht64.apphoctienganh.database.SQLiteHelper; // Quản lý cơ sở dữ liệu SQLite
import tlu.cse.ht64.apphoctienganh.model.Word; // Mô hình dữ liệu từ vựng
import tlu.cse.ht64.apphoctienganh.utils.SessionManager; // Quản lý phiên đăng nhập

public class VocabularyActivity extends AppCompatActivity {
    // TAG dùng để ghi log, giúp xác định log từ Activity này
    private static final String TAG = "VocabularyActivity";
    // Spinner để chọn danh mục từ vựng (Daily, Work, v.v.)
    private Spinner spinnerCategory;
    // RecyclerView để hiển thị danh sách từ vựng
    private RecyclerView rvWords;
    // EditText để nhập từ khóa tìm kiếm từ vựng
    private EditText etSearchVocabulary;
    // TextView hiển thị thông báo khi không có từ vựng
    private TextView tvNoWords;
    // Button để chuyển đến màn hình thêm từ mới
    private Button btnAddWord;
    // Đối tượng SQLiteHelper để tương tác với cơ sở dữ liệu
    private SQLiteHelper dbHelper;
    // Đối tượng SessionManager để quản lý phiên đăng nhập
    private SessionManager session;
    // Danh sách tất cả từ vựng lấy từ cơ sở dữ liệu
    private List<Word> wordList;
    // Danh sách từ vựng đã được lọc (theo tìm kiếm hoặc danh mục)
    private List<Word> filteredWordList;
    // Bộ điều hợp để liên kết danh sách từ vựng với RecyclerView
    private VocabularyAdapter adapter;
    // ID của người dùng hiện tại
    private int userId;
    // Kiểm tra xem người dùng có phải quản trị viên không (mặc định false)
    private boolean isAdmin = false;
    // Kiểm tra xem Activity có ở chế độ xóa không (mặc định false)
    private boolean deleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Gọi phương thức onCreate của lớp cha để khởi tạo Activity
        super.onCreate(savedInstanceState);
        // Liên kết Activity với layout activity_vocabulary.xml
        setContentView(R.layout.activity_vocabulary);

        // Liên kết các thành phần giao diện với biến Java thông qua ID
        spinnerCategory = findViewById(R.id.spinnerCategory); // Spinner danh mục
        rvWords = findViewById(R.id.rvWords); // RecyclerView từ vựng
        etSearchVocabulary = findViewById(R.id.etSearchVocabulary); // Ô tìm kiếm
        tvNoWords = findViewById(R.id.tvNoWords); // Thông báo không có từ
        btnAddWord = findViewById(R.id.btnAddWord); // Nút thêm từ mới

        // Khởi tạo đối tượng SQLiteHelper để quản lý cơ sở dữ liệu
        dbHelper = new SQLiteHelper(this);
        // Khởi tạo đối tượng SessionManager để quản lý phiên đăng nhập
        session = new SessionManager(this);

        // Kiểm tra trạng thái đăng nhập của người dùng
        if (!session.isLoggedIn()) {
            // Nếu chưa đăng nhập, hiển thị thông báo
            Toast.makeText(this, "Please log in to continue", Toast.LENGTH_SHORT).show();
            // Chuyển đến LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
            // Đóng Activity hiện tại
            finish();
            // Thoát khỏi onCreate
            return;
        }

        // Lấy ID người dùng từ phiên đăng nhập
        userId = session.getUserId();
        // Ghi log ID người dùng để debug
        Log.d(TAG, "User ID: " + userId);
        // Kiểm tra tính hợp lệ của ID
        if (userId == -1) {
            // Nếu ID không hợp lệ, hiển thị thông báo
            Toast.makeText(this, "Invalid user session. Please log in again.", Toast.LENGTH_SHORT).show();
            // Đăng xuất người dùng
            session.logout();
            // Chuyển đến LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
            // Đóng Activity hiện tại
            finish();
            // Thoát khỏi onCreate
            return;
        }

        // Lấy dữ liệu từ Intent để kiểm tra chế độ
        isAdmin = getIntent().getBooleanExtra("isAdmin", false); // Có phải quản trị viên không
        deleteMode = getIntent().getBooleanExtra("deleteMode", false); // Có ở chế độ xóa không

        // Tải danh sách từ vựng và thiết lập giao diện
        loadWords();

        // Gắn sự kiện click cho nút thêm từ mới
        btnAddWord.setOnClickListener(v -> {
            // Tạo Intent để mở AddWordActivity
            Intent intent = new Intent(this, AddWordActivity.class);
            // Mở Activity và mong nhận kết quả trả về với mã yêu cầu 1
            startActivityForResult(intent, 1);
        });

        // Nếu là quản trị viên hoặc ở chế độ xóa, hiển thị ô tìm kiếm
        if (isAdmin || deleteMode) {
            // Hiển thị ô tìm kiếm
            etSearchVocabulary.setVisibility(View.VISIBLE);
            // Ẩn Spinner danh mục
            spinnerCategory.setVisibility(View.GONE);
            // Gắn TextWatcher để lọc từ vựng khi nhập
            etSearchVocabulary.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Không làm gì trước khi văn bản thay đổi
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Lấy chuỗi tìm kiếm, loại bỏ khoảng trắng và chuyển thành chữ thường
                    String query = s.toString().trim().toLowerCase();
                    // Xóa danh sách từ vựng đã lọc
                    filteredWordList.clear();
                    // Nếu chuỗi tìm kiếm không rỗng
                    if (!query.isEmpty()) {
                        // Duyệt qua danh sách từ vựng
                        for (Word word : wordList) {
                            // Nếu từ tiếng Anh chứa chuỗi tìm kiếm, thêm vào danh sách lọc
                            if (word.getEnglish().toLowerCase().contains(query)) {
                                filteredWordList.add(word);
                            }
                        }
                    } else {
                        // Nếu chuỗi rỗng, sao chép toàn bộ danh sách từ vựng
                        filteredWordList.addAll(wordList);
                    }
                    // Cập nhật danh sách trong adapter để hiển thị kết quả
                    adapter.updateWordList(filteredWordList);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Không làm gì sau khi văn bản thay đổi
                }
            });
        }
    }

    // Phương thức tải danh sách từ vựng và thiết lập giao diện
    private void loadWords() {
        // Lấy tất cả từ vựng của người dùng từ cơ sở dữ liệu
        wordList = dbHelper.getAllWords(userId);
        // Tạo bản sao danh sách từ vựng để dùng cho việc lọc
        filteredWordList = new ArrayList<>(wordList);

        // Kiểm tra nếu không có từ vựng
        if (wordList == null || wordList.isEmpty()) {
            // Ghi log thông báo không có từ vựng
            Log.d(TAG, "No words available for user ID: " + userId);
            // Ẩn RecyclerView
            rvWords.setVisibility(View.GONE);
            // Ẩn Spinner danh mục
            spinnerCategory.setVisibility(View.GONE);
            // Ẩn ô tìm kiếm
            etSearchVocabulary.setVisibility(View.GONE);
            // Hiển thị thông báo không có từ
            tvNoWords.setVisibility(View.VISIBLE);
            // Hiển thị nút thêm từ mới
            btnAddWord.setVisibility(View.VISIBLE);
        } else {
            // Nếu có từ vựng, hiển thị RecyclerView
            rvWords.setVisibility(View.VISIBLE);
            // Hiển thị Spinner nếu không phải quản trị viên hoặc chế độ xóa
            if (!isAdmin && !deleteMode) {
                spinnerCategory.setVisibility(View.VISIBLE);
            }
            // Ẩn thông báo không có từ
            tvNoWords.setVisibility(View.GONE);
            // Ẩn nút thêm từ mới
            btnAddWord.setVisibility(View.GONE);

            // Tạo danh sách danh mục cho Spinner
            List<String> categories = new ArrayList<>();
            categories.add("All"); // Tất cả
            categories.add("Daily"); // Hàng ngày
            categories.add("Work"); // Công việc
            categories.add("Travel"); // Du lịch
            categories.add("Education"); // Giáo dục
            categories.add("Game"); // Trò chơi

            // Tạo ArrayAdapter để liên kết danh sách danh mục với Spinner
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
            // Thiết lập giao diện cho các mục thả xuống
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Gán adapter cho Spinner
            spinnerCategory.setAdapter(categoryAdapter);

            // Thiết lập RecyclerView
            rvWords.setLayoutManager(new LinearLayoutManager(this)); // Sử dụng bố cục dọc
            adapter = new VocabularyAdapter(this); // Khởi tạo adapter
            adapter.updateWordList(filteredWordList); // Cập nhật danh sách từ vựng
            rvWords.setAdapter(adapter); // Gán adapter cho RecyclerView

            // Gắn sự kiện chọn mục cho Spinner
            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Lấy danh mục được chọn
                    String selectedCategory = categories.get(position);
                    List<Word> filteredList;
                    // Nếu chọn "All", lấy tất cả từ vựng
                    if (selectedCategory.equals("All")) {
                        filteredList = dbHelper.getAllWords(userId);
                    } else {
                        // Nếu chọn danh mục cụ thể, lấy từ vựng theo danh mục
                        filteredList = dbHelper.getWordsByCategory(userId, selectedCategory);
                    }
                    // Cập nhật danh sách từ vựng
                    wordList = filteredList;
                    filteredWordList = new ArrayList<>(wordList);
                    // Cập nhật adapter để hiển thị danh sách mới
                    adapter.updateWordList(filteredWordList);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Không làm gì nếu không chọn mục nào
                }
            });

            // Thiết lập chế độ quản trị viên hoặc xóa cho adapter
            adapter.setAdminMode(isAdmin, deleteMode);
        }
    }

    // Xử lý kết quả trả về từ Activity khác
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Gọi phương thức của lớp cha
        super.onActivityResult(requestCode, resultCode, data);
        // Kiểm tra mã yêu cầu và kết quả
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Hiển thị thông báo thêm từ thành công
            Toast.makeText(this, "Đã thêm từ mới", Toast.LENGTH_SHORT).show();
            // Tải lại danh sách từ vựng
            loadWords();
        }
    }
}