package tlu.cse.ht64.apphoctienganh.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tlu.cse.ht64.apphoctienganh.model.User;
import tlu.cse.ht64.apphoctienganh.model.Word;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EnglishLearning.db";
    private static final int DATABASE_VERSION = 5;

    // Tên bảng và cột
    private static final String TABLE_USERS = "users";
    private static final String TABLE_WORDS = "words";
    private static final String TABLE_USER_WORDS = "user_words";

    // Cột bảng users
    private static final String COL_USER_ID = "id";
    private static final String COL_EMAIL = "email";
    private static final String COL_PHONE = "phone";
    private static final String COL_PASSWORD = "password";
    private static final String COL_ROLE = "role";

    // Cột bảng words
    private static final String COL_WORD_ID = "id";
    private static final String COL_ENGLISH = "english";
    private static final String COL_PHONETIC = "phonetic";
    private static final String COL_VIETNAMESE = "vietnamese";
    private static final String COL_IMAGE_FILE = "image_file";
    private static final String COL_AUDIO_FILE = "audio_file";
    private static final String COL_CATEGORY = "category";

    // Cột bảng user_words
    private static final String COL_USER_WORD_USER_ID = "user_id";
    private static final String COL_USER_WORD_WORD_ID = "word_id";
    private static final String COL_IS_LEARNED = "is_learned";
    private static final String COL_IS_FAVORITE = "is_favorite";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        initializeSampleData();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PHONE + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT)";
        db.execSQL(createUsersTable);

        // Tạo bảng words
        String createWordsTable = "CREATE TABLE " + TABLE_WORDS + " (" +
                COL_WORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ENGLISH + " TEXT, " +
                COL_PHONETIC + " TEXT, " +
                COL_VIETNAMESE + " TEXT, " +
                COL_IMAGE_FILE + " TEXT, " +
                COL_AUDIO_FILE + " TEXT, " +
                COL_CATEGORY + " TEXT)";
        db.execSQL(createWordsTable);

        // Tạo bảng user_words
        String createUserWordsTable = "CREATE TABLE " + TABLE_USER_WORDS + " (" +
                COL_USER_WORD_USER_ID + " INTEGER, " +
                COL_USER_WORD_WORD_ID + " INTEGER, " +
                COL_IS_LEARNED + " INTEGER, " +
                COL_IS_FAVORITE + " INTEGER, " +
                "FOREIGN KEY(" + COL_USER_WORD_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "), " +
                "FOREIGN KEY(" + COL_USER_WORD_WORD_ID + ") REFERENCES " + TABLE_WORDS + "(" + COL_WORD_ID + "))";
        db.execSQL(createUserWordsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_WORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Phương thức để chèn dữ liệu mẫu
    private void initializeSampleData() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Kiểm tra và tạo tài khoản admin mặc định
        Cursor userCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
        userCursor.moveToFirst();
        int userCount = userCursor.getInt(0);
        userCursor.close();

        if (userCount == 0) {
            Log.d("SQLiteHelper", "Creating default admin account...");
            ContentValues adminValues = new ContentValues();
            adminValues.put(COL_EMAIL, "admin@example.com");
            adminValues.put(COL_PHONE, "1234567890");
            adminValues.put(COL_PASSWORD, "admin123");
            adminValues.put(COL_ROLE, "admin");
            db.insert(TABLE_USERS, null, adminValues);
            Log.d("SQLiteHelper", "Default admin account created: email=admin@example.com, password=admin123");
        }

        // Kiểm tra và chèn dữ liệu từ vựng mẫu
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_WORDS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count == 0) {
            Log.d("SQLiteHelper", "Inserting sample data...");
            db = this.getWritableDatabase();
            db.beginTransaction();
            try {
                // Daily (50 từ)
                insertWord(db, "hello", "/həˈloʊ/", "Xin chào", "Daily");
                insertWord(db, "goodbye", "/ˌɡʊdˈbaɪ/", "Tạm biệt", "Daily");
                insertWord(db, "morning", "/ˈmɔːrnɪŋ/", "Buổi sáng", "Daily");
                insertWord(db, "night", "/naɪt/", "Buổi tối", "Daily");
                insertWord(db, "eat", "/iːt/", "Ăn", "Daily");
                insertWord(db, "drink", "/drɪŋk/", "Uống", "Daily");
                insertWord(db, "sleep", "/sliːp/", "Ngủ", "Daily");
                insertWord(db, "wake", "/weɪk/", "Thức dậy", "Daily");
                insertWord(db, "walk", "/wɔːk/", "Đi bộ", "Daily");
                insertWord(db, "run", "/rʌn/", "Chạy", "Daily");
                insertWord(db, "play", "/pleɪ/", "Chơi", "Daily");
                insertWord(db, "read", "/riːd/", "Đọc", "Daily");
                insertWord(db, "write", "/raɪt/", "Viết", "Daily");
                insertWord(db, "talk", "/tɔːk/", "Nói chuyện", "Daily");
                insertWord(db, "listen", "/ˈlɪsn/", "Nghe", "Daily");
                insertWord(db, "watch", "/wɑːtʃ/", "Xem", "Daily");
                insertWord(db, "cook", "/kʊk/", "Nấu ăn", "Daily");
                insertWord(db, "clean", "/kliːn/", "Dọn dẹp", "Daily");
                insertWord(db, "wash", "/wɑːʃ/", "Rửa", "Daily");
                insertWord(db, "brush", "/brʌʃ/", "Đánh răng", "Daily");
                insertWord(db, "shower", "/ˈʃaʊər/", "Tắm", "Daily");
                insertWord(db, "dress", "/dres/", "Mặc quần áo", "Daily");
                insertWord(db, "work", "/wɜːrk/", "Làm việc", "Daily");
                insertWord(db, "study", "/ˈstʌdi/", "Học", "Daily");
                insertWord(db, "rest", "/rest/", "Nghỉ ngơi", "Daily");
                insertWord(db, "laugh", "/læf/", "Cười", "Daily");
                insertWord(db, "cry", "/kraɪ/", "Khóc", "Daily");
                insertWord(db, "smile", "/smaɪl/", "Cười mỉm", "Daily");
                insertWord(db, "think", "/θɪŋk/", "Suy nghĩ", "Daily");
                insertWord(db, "feel", "/fiːl/", "Cảm thấy", "Daily");
                insertWord(db, "see", "/siː/", "Nhìn thấy", "Daily");
                insertWord(db, "hear", "/hɪr/", "Nghe thấy", "Daily");
                insertWord(db, "touch", "/tʌtʃ/", "Chạm", "Daily");
                insertWord(db, "smell", "/smel/", "Ngửi", "Daily");
                insertWord(db, "taste", "/teɪst/", "Nếm", "Daily");
                insertWord(db, "buy", "/baɪ/", "Mua", "Daily");
                insertWord(db, "sell", "/sel/", "Bán", "Daily");
                insertWord(db, "give", "/ɡɪv/", "Cho", "Daily");
                insertWord(db, "take", "/teɪk/", "Lấy", "Daily");
                insertWord(db, "open", "/ˈoʊpən/", "Mở", "Daily");
                insertWord(db, "close", "/kloʊz/", "Đóng", "Daily");
                insertWord(db, "sit", "/sɪt/", "Ngồi", "Daily");
                insertWord(db, "stand", "/stænd/", "Đứng", "Daily");
                insertWord(db, "lie", "/laɪ/", "Nằm", "Daily");
                insertWord(db, "jump", "/dʒʌmp/", "Nhảy", "Daily");
                insertWord(db, "sing", "/sɪŋ/", "Hát", "Daily");
                insertWord(db, "dance", "/dæns/", "Nhảy múa", "Daily");
                insertWord(db, "swim", "/swɪm/", "Bơi", "Daily");
                insertWord(db, "draw", "/drɔː/", "Vẽ", "Daily");
                insertWord(db, "paint", "/peɪnt/", "Sơn", "Daily");

                // Work (50 từ)
                insertWord(db, "job", "/dʒɑːb/", "Công việc", "Work");
                insertWord(db, "office", "/ˈɔːfɪs/", "Văn phòng", "Work");
                insertWord(db, "meeting", "/ˈmiːtɪŋ/", "Cuộc họp", "Work");
                insertWord(db, "email", "/ˈiːmeɪl/", "Thư điện tử", "Work");
                insertWord(db, "phone", "/foʊn/", "Điện thoại", "Work");
                insertWord(db, "computer", "/kəmˈpjuːtər/", "Máy tính", "Work");
                insertWord(db, "boss", "/bɔːs/", "Sếp", "Work");
                insertWord(db, "employee", "/ɪmˈplɔɪiː/", "Nhân viên", "Work");
                insertWord(db, "project", "/ˈprɑːdʒekt/", "Dự án", "Work");
                insertWord(db, "deadline", "/ˈdedlaɪn/", "Hạn chót", "Work");
                insertWord(db, "salary", "/ˈsæləri/", "Lương", "Work");
                insertWord(db, "contract", "/ˈkɑːntrækt/", "Hợp đồng", "Work");
                insertWord(db, "client", "/ˈklaɪənt/", "Khách hàng", "Work");
                insertWord(db, "manager", "/ˈmænɪdʒər/", "Quản lý", "Work");
                insertWord(db, "team", "/tiːm/", "Đội nhóm", "Work");
                insertWord(db, "task", "/tæsk/", "Nhiệm vụ", "Work");
                insertWord(db, "report", "/rɪˈpɔːrt/", "Báo cáo", "Work");
                insertWord(db, "schedule", "/ˈskedʒuːl/", "Lịch trình", "Work");
                insertWord(db, "plan", "/plæn/", "Kế hoạch", "Work");
                insertWord(db, "budget", "/ˈbʌdʒɪt/", "Ngân sách", "Work");
                insertWord(db, "presentation", "/ˌpreznˈteɪʃn/", "Bài thuyết trình", "Work");
                insertWord(db, "document", "/ˈdɑːkjumənt/", "Tài liệu", "Work");
                insertWord(db, "file", "/faɪl/", "Tệp", "Work");
                insertWord(db, "desk", "/desk/", "Bàn làm việc", "Work");
                insertWord(db, "chair", "/tʃer/", "Ghế", "Work");
                insertWord(db, "printer", "/ˈprɪntər/", "Máy in", "Work");
                insertWord(db, "scanner", "/ˈskænər/", "Máy quét", "Work");
                insertWord(db, "internet", "/ˈɪntərnet/", "Mạng Internet", "Work");
                insertWord(db, "software", "/ˈsɔːftwer/", "Phần mềm", "Work");
                insertWord(db, "hardware", "/ˈhɑːrdwer/", "Phần cứng", "Work");
                insertWord(db, "server", "/ˈsɜːrvər/", "Máy chủ", "Work");
                insertWord(db, "network", "/ˈnetwɜːrk/", "Mạng", "Work");
                insertWord(db, "data", "/ˈdeɪtə/", "Dữ liệu", "Work");
                insertWord(db, "analysis", "/əˈnæləsɪs/", "Phân tích", "Work");
                insertWord(db, "research", "/rɪˈsɜːrtʃ/", "Nghiên cứu", "Work");
                insertWord(db, "strategy", "/ˈstrætədʒi/", "Chiến lược", "Work");
                insertWord(db, "marketing", "/ˈmɑːrkɪtɪŋ/", "Tiếp thị", "Work");
                insertWord(db, "sales", "/seɪlz/", "Bán hàng", "Work");
                insertWord(db, "product", "/ˈprɑːdʌkt/", "Sản phẩm", "Work");
                insertWord(db, "service", "/ˈsɜːrvɪs/", "Dịch vụ", "Work");
                insertWord(db, "customer", "/ˈkʌstəmər/", "Khách hàng", "Work");
                insertWord(db, "support", "/səˈpɔːrt/", "Hỗ trợ", "Work");
                insertWord(db, "training", "/ˈtreɪnɪŋ/", "Đào tạo", "Work");
                insertWord(db, "development", "/dɪˈveləpmənt/", "Phát triển", "Work");
                insertWord(db, "design", "/dɪˈzaɪn/", "Thiết kế", "Work");
                insertWord(db, "production", "/prəˈdʌkʃn/", "Sản xuất", "Work");
                insertWord(db, "quality", "/ˈkwɑːləti/", "Chất lượng", "Work");
                insertWord(db, "innovation", "/ˌɪnəˈveɪʃn/", "Đổi mới", "Work");
                insertWord(db, "finance", "/ˈfaɪnæns/", "Tài chính", "Work");
                insertWord(db, "accounting", "/əˈkaʊntɪŋ/", "Kế toán", "Work");

                // Travel (50 từ)
                insertWord(db, "travel", "/ˈtrævl/", "Du lịch", "Travel");
                insertWord(db, "airplane", "/ˈerpleɪn/", "Máy bay", "Travel");
                insertWord(db, "train", "/treɪn/", "Tàu hỏa", "Travel");
                insertWord(db, "hotel", "/hoʊˈtel/", "Khách sạn", "Travel");
                insertWord(db, "map", "/mæp/", "Bản đồ", "Travel");
                insertWord(db, "ticket", "/ˈtɪkɪt/", "Vé", "Travel");
                insertWord(db, "luggage", "/ˈlʌɡɪdʒ/", "Hành lý", "Travel");
                insertWord(db, "passport", "/ˈpæspɔːrt/", "Hộ chiếu", "Travel");
                insertWord(db, "tourist", "/ˈtʊrɪst/", "Du khách", "Travel");
                insertWord(db, "guide", "/ɡaɪd/", "Hướng dẫn viên", "Travel");
                insertWord(db, "destination", "/ˌdestɪˈneɪʃn/", "Điểm đến", "Travel");
                insertWord(db, "journey", "/ˈdʒɜːrni/", "Hành trình", "Travel");
                insertWord(db, "trip", "/trɪp/", "Chuyến đi", "Travel");
                insertWord(db, "vacation", "/veɪˈkeɪʃn/", "Kỳ nghỉ", "Travel");
                insertWord(db, "beach", "/biːtʃ/", "Bãi biển", "Travel");
                insertWord(db, "mountain", "/ˈmaʊntn/", "Núi", "Travel");
                insertWord(db, "city", "/ˈsɪti/", "Thành phố", "Travel");
                insertWord(db, "village", "/ˈvɪlɪdʒ/", "Làng", "Travel");
                insertWord(db, "forest", "/ˈfɔːrɪst/", "Rừng", "Travel");
                insertWord(db, "river", "/ˈrɪvər/", "Sông", "Travel");
                insertWord(db, "lake", "/leɪk/", "Hồ", "Travel");
                insertWord(db, "museum", "/mjuˈziːəm/", "Bảo tàng", "Travel");
                insertWord(db, "park", "/pɑːrk/", "Công viên", "Travel");
                insertWord(db, "zoo", "/zuː/", "Sở thú", "Travel");
                insertWord(db, "culture", "/ˈkʌltʃər/", "Văn hóa", "Travel");
                insertWord(db, "language", "/ˈlæŋɡwɪdʒ/", "Ngôn ngữ", "Travel");
                insertWord(db, "food", "/fuːd/", "Thức ăn", "Travel");
                insertWord(db, "drink", "/drɪŋk/", "Đồ uống", "Travel");
                insertWord(db, "money", "/ˈmʌni/", "Tiền", "Travel");
                insertWord(db, "currency", "/ˈkɜːrənsi/", "Tiền tệ", "Travel");
                insertWord(db, "exchange", "/ɪksˈtʃeɪndʒ/", "Đổi tiền", "Travel");
                insertWord(db, "camera", "/ˈkæmərə/", "Máy ảnh", "Travel");
                insertWord(db, "photo", "/ˈfoʊtoʊ/", "Ảnh", "Travel");
                insertWord(db, "video", "/ˈvɪdioʊ/", "Video", "Travel");
                insertWord(db, "souvenir", "/ˌsuːvəˈnɪr/", "Quà lưu niệm", "Travel");
                insertWord(db, "market", "/ˈmɑːrkɪt/", "Chợ", "Travel");
                insertWord(db, "shop", "/ʃɑːp/", "Cửa hàng", "Travel");
                insertWord(db, "restaurant", "/ˈrestərɑːnt/", "Nhà hàng", "Travel");
                insertWord(db, "cafe", "/kæˈfeɪ/", "Quán cà phê", "Travel");
                insertWord(db, "bar", "/bɑːr/", "Quán bar", "Travel");
                insertWord(db, "street", "/striːt/", "Đường phố", "Travel");
                insertWord(db, "traffic", "/ˈtræfɪk/", "Giao thông", "Travel");
                insertWord(db, "car", "/kɑːr/", "Xe hơi", "Travel");
                insertWord(db, "bus", "/bʌs/", "Xe buýt", "Travel");
                insertWord(db, "taxi", "/ˈtæksi/", "Taxi", "Travel");
                insertWord(db, "bike", "/baɪk/", "Xe đạp", "Travel");
                insertWord(db, "boat", "/boʊt/", "Thuyền", "Travel");
                insertWord(db, "ship", "/ʃɪp/", "Tàu", "Travel");
                insertWord(db, "adventure", "/ədˈventʃər/", "Cuộc phiêu lưu", "Travel");
                insertWord(db, "explore", "/ɪkˈsplɔːr/", "Khám phá", "Travel");

                // Education (50 từ)
                insertWord(db, "school", "/skuːl/", "Trường học", "Education");
                insertWord(db, "teacher", "/ˈtiːtʃər/", "Giáo viên", "Education");
                insertWord(db, "student", "/ˈstuːdnt/", "Học sinh", "Education");
                insertWord(db, "book", "/bʊk/", "Sách", "Education");
                insertWord(db, "pen", "/pen/", "Bút", "Education");
                insertWord(db, "pencil", "/ˈpensl/", "Bút chì", "Education");
                insertWord(db, "exam", "/ɪɡˈzæm/", "Kỳ thi", "Education");
                insertWord(db, "homework", "/ˈhoʊmwɜːrk/", "Bài tập về nhà", "Education");
                insertWord(db, "class", "/klæs/", "Lớp học", "Education");
                insertWord(db, "study", "/ˈstʌdi/", "Học", "Education");
                insertWord(db, "lesson", "/ˈlesn/", "Bài học", "Education");
                insertWord(db, "subject", "/ˈsʌbdʒekt/", "Môn học", "Education");
                insertWord(db, "math", "/mæθ/", "Toán học", "Education");
                insertWord(db, "science", "/ˈsaɪəns/", "Khoa học", "Education");
                insertWord(db, "history", "/ˈhɪstəri/", "Lịch sử", "Education");
                insertWord(db, "geography", "/dʒiˈɑːɡrəfi/", "Địa lý", "Education");
                insertWord(db, "literature", "/ˈlɪtrətʃər/", "Văn học", "Education");
                insertWord(db, "art", "/ɑːrt/", "Mỹ thuật", "Education");
                insertWord(db, "music", "/ˈmjuːzɪk/", "Âm nhạc", "Education");
                insertWord(db, "sport", "/spɔːrt/", "Thể thao", "Education");
                insertWord(db, "library", "/ˈlaɪbreri/", "Thư viện", "Education");
                insertWord(db, "desk", "/desk/", "Bàn học", "Education");
                insertWord(db, "chair", "/tʃer/", "Ghế", "Education");
                insertWord(db, "board", "/bɔːrd/", "Bảng", "Education");
                insertWord(db, "chalk", "/tʃɔːk/", "Phấn", "Education");
                insertWord(db, "marker", "/ˈmɑːrkər/", "Bút lông", "Education");
                insertWord(db, "notebook", "/ˈnoʊtbʊk/", "Sổ tay", "Education");
                insertWord(db, "paper", "/ˈpeɪpər/", "Giấy", "Education");
                insertWord(db, "bag", "/bæɡ/", "Cái cặp", "Education");
                insertWord(db, "uniform", "/ˈjuːnɪfɔːrm/", "Đồng phục", "Education");
                insertWord(db, "schedule", "/ˈskedʒuːl/", "Thời khóa biểu", "Education");
                insertWord(db, "test", "/test/", "Bài kiểm tra", "Education");
                insertWord(db, "grade", "/ɡreɪd/", "Điểm số", "Education");
                insertWord(db, "score", "/skɔːr/", "Điểm", "Education");
                insertWord(db, "pass", "/pæs/", "Đậu", "Education");
                insertWord(db, "fail", "/feɪl/", "Trượt", "Education");
                insertWord(db, "learn", "/lɜːrn/", "Học", "Education");
                insertWord(db, "teach", "/tiːtʃ/", "Dạy", "Education");
                insertWord(db, "read", "/riːd/", "Đọc", "Education");
                insertWord(db, "write", "/raɪt/", "Viết", "Education");
                insertWord(db, "speak", "/spiːk/", "Nói", "Education");
                insertWord(db, "listen", "/ˈlɪsn/", "Nghe", "Education");
                insertWord(db, "question", "/ˈkwestʃən/", "Câu hỏi", "Education");
                insertWord(db, "answer", "/ˈænsər/", "Câu trả lời", "Education");
                insertWord(db, "group", "/ɡruːp/", "Nhóm", "Education");
                insertWord(db, "project", "/ˈprɑːdʒekt/", "Dự án", "Education");
                insertWord(db, "research", "/rɪˈsɜːrtʃ/", "Nghiên cứu", "Education");
                insertWord(db, "knowledge", "/ˈnɑːlɪdʒ/", "Kiến thức", "Education");
                insertWord(db, "skill", "/skɪl/", "Kỹ năng", "Education");
                insertWord(db, "degree", "/dɪˈɡriː/", "Bằng cấp", "Game");

                db.setTransactionSuccessful();
                Log.d("SQLiteHelper", "Sample data inserted successfully");
            } catch (Exception e) {
                Log.e("SQLiteHelper", "Error inserting sample data: " + e.getMessage());
            } finally {
                db.endTransaction();
                db.close();
            }
        } else {
            Log.d("SQLiteHelper", "Sample data already exists, skipping insertion");
        }
    }

    // Phương thức hỗ trợ để chèn một từ
    private void insertWord(SQLiteDatabase db, String english, String phonetic, String vietnamese, String category) {
        ContentValues values = new ContentValues();
        values.put(COL_ENGLISH, english);
        values.put(COL_PHONETIC, phonetic);
        values.put(COL_VIETNAMESE, vietnamese);
        values.put(COL_IMAGE_FILE, (String) null);
        values.put(COL_AUDIO_FILE, (String) null);
        values.put(COL_CATEGORY, category);
        long wordId = db.insert(TABLE_WORDS, null, values);

        // Thêm từ vựng vào bảng user_words cho tất cả user
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_ID}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            ContentValues userWordValues = new ContentValues();
            userWordValues.put(COL_USER_WORD_USER_ID, cursor.getInt(0));
            userWordValues.put(COL_USER_WORD_WORD_ID, wordId);
            userWordValues.put(COL_IS_LEARNED, 0);
            userWordValues.put(COL_IS_FAVORITE, 0);
            db.insert(TABLE_USER_WORDS, null, userWordValues);
        }
        cursor.close();
    }

    // Thêm người dùng mới
    public void addUser(User user, String password) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL, user.getEmail());
        values.put(COL_PHONE, user.getPhone());
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, user.getRole());

        // Kiểm tra email đã tồn tại
        Cursor emailCursor = db.query(TABLE_USERS, new String[]{COL_EMAIL},
                COL_EMAIL + "=?", new String[]{user.getEmail()},
                null, null, null);
        boolean emailExists = emailCursor.moveToFirst();
        emailCursor.close();

        // Kiểm tra số điện thoại đã tồn tại
        Cursor phoneCursor = db.query(TABLE_USERS, new String[]{COL_PHONE},
                COL_PHONE + "=?", new String[]{user.getPhone()},
                null, null, null);
        boolean phoneExists = phoneCursor.moveToFirst();
        phoneCursor.close();

        if (emailExists && phoneExists) {
            db.close();
            throw new SQLiteException("Both email and phone number already exist");
        } else if (emailExists) {
            db.close();
            throw new SQLiteException("Email already exists");
        } else if (phoneExists) {
            db.close();
            throw new SQLiteException("Phone number already exists");
        }

        try {
            long result = db.insertOrThrow(TABLE_USERS, null, values);
            Log.d("SQLiteHelper", "User added successfully with ID: " + result);

            // Sau khi thêm user, liên kết tất cả từ vựng hiện có với user mới
            Cursor wordCursor = db.rawQuery("SELECT " + COL_WORD_ID + " FROM " + TABLE_WORDS, null);
            while (wordCursor.moveToNext()) {
                ContentValues userWordValues = new ContentValues();
                userWordValues.put(COL_USER_WORD_USER_ID, result);
                userWordValues.put(COL_USER_WORD_WORD_ID, wordCursor.getInt(0));
                userWordValues.put(COL_IS_LEARNED, 0);
                userWordValues.put(COL_IS_FAVORITE, 0);
                db.insert(TABLE_USER_WORDS, null, userWordValues);
            }
            wordCursor.close();
        } catch (SQLiteException e) {
            Log.e("SQLiteHelper", "Failed to add user: " + e.getMessage());
            throw new SQLiteException("Failed to add user: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    // Xác thực người dùng bằng email và password
    public User authenticateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_USER_ID, COL_EMAIL, COL_PHONE, COL_ROLE},
                COL_EMAIL + "=? AND " + COL_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(0), // id
                    cursor.getString(1), // email
                    cursor.getString(2), // phone
                    cursor.getString(3)  // role
            );
            Log.d("SQLiteHelper", "User authenticated: " + user.getEmail());
        } else {
            Log.d("SQLiteHelper", "Authentication failed for email: " + email);
        }
        cursor.close();
        db.close();
        return user;
    }

    // Lấy thông tin người dùng bằng userId
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_USER_ID, COL_EMAIL, COL_PHONE, COL_ROLE},
                COL_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(0), // id
                    cursor.getString(1), // email
                    cursor.getString(2), // phone
                    cursor.getString(3)  // role
            );
            Log.d("SQLiteHelper", "User retrieved by ID: " + user.getId());
        } else {
            Log.d("SQLiteHelper", "No user found with ID: " + userId);
        }
        cursor.close();
        db.close();
        return user;
    }

    // Thêm từ vựng mới
    public void addWord(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ENGLISH, word.getEnglish());
        values.put(COL_PHONETIC, word.getPhonetic());
        values.put(COL_VIETNAMESE, word.getVietnamese());
        values.put(COL_IMAGE_FILE, word.getImageFile());
        values.put(COL_AUDIO_FILE, word.getAudioFile());
        values.put(COL_CATEGORY, word.getCategory());
        long wordId = db.insert(TABLE_WORDS, null, values);

        // Thêm từ vựng vào bảng user_words cho tất cả user
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_ID}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            ContentValues userWordValues = new ContentValues();
            userWordValues.put(COL_USER_WORD_USER_ID, cursor.getInt(0));
            userWordValues.put(COL_USER_WORD_WORD_ID, wordId);
            userWordValues.put(COL_IS_LEARNED, 0);
            userWordValues.put(COL_IS_FAVORITE, 0);
            db.insert(TABLE_USER_WORDS, null, userWordValues);
        }
        cursor.close();
        db.close();
    }

    // Lấy tất cả từ vựng của một user
    public List<Word> getAllWords(int userId) {
        List<Word> wordList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT w.*, uw." + COL_IS_LEARNED + ", uw." + COL_IS_FAVORITE + " " +
                "FROM " + TABLE_WORDS + " w " +
                "LEFT JOIN " + TABLE_USER_WORDS + " uw ON w." + COL_WORD_ID + " = uw." + COL_USER_WORD_WORD_ID + " " +
                "WHERE uw." + COL_USER_WORD_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        while (cursor.moveToNext()) {
            Word word = new Word(
                    cursor.getInt(0), // id
                    cursor.getString(1), // english
                    cursor.getString(2), // phonetic
                    cursor.getString(3), // vietnamese
                    cursor.getString(4), // image_file
                    cursor.getString(5), // audio_file
                    cursor.getInt(7) == 1, // is_learned (column 7)
                    cursor.getInt(8) == 1, // is_favorite (column 8)
                    cursor.getString(6) // category
            );
            wordList.add(word);
        }
        cursor.close();
        db.close();
        return wordList;
    }

    // Lấy từ vựng theo category
    public List<Word> getWordsByCategory(int userId, String category) {
        List<Word> wordList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT w.*, uw." + COL_IS_LEARNED + ", uw." + COL_IS_FAVORITE + " " +
                        "FROM " + TABLE_WORDS + " w " +
                        "LEFT JOIN " + TABLE_USER_WORDS + " uw ON w." + COL_WORD_ID + " = uw." + COL_USER_WORD_WORD_ID + " " +
                        "WHERE uw." + COL_USER_WORD_USER_ID + " = ? AND w." + COL_CATEGORY + " = ?",
                new String[]{String.valueOf(userId), category});
        while (cursor.moveToNext()) {
            Word word = new Word(
                    cursor.getInt(0), // id
                    cursor.getString(1), // english
                    cursor.getString(2), // phonetic
                    cursor.getString(3), // vietnamese
                    cursor.getString(4), // image_file
                    cursor.getString(5), // audio_file
                    cursor.getInt(7) == 1, // is_learned (column 7)
                    cursor.getInt(8) == 1, // is_favorite (column 8)
                    cursor.getString(6) // category
            );
            wordList.add(word);
        }
        cursor.close();
        db.close();
        return wordList;
    }

    // Cập nhật từ vựng
    public void updateWord(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ENGLISH, word.getEnglish());
        values.put(COL_PHONETIC, word.getPhonetic());
        values.put(COL_VIETNAMESE, word.getVietnamese());
        values.put(COL_IMAGE_FILE, word.getImageFile());
        values.put(COL_AUDIO_FILE, word.getAudioFile());
        values.put(COL_CATEGORY, word.getCategory());
        db.update(TABLE_WORDS, values, COL_WORD_ID + "=?", new String[]{String.valueOf(word.getId())});
        db.close();
    }

    // Cập nhật trạng thái từ vựng của user
    public void updateUserWord(int userId, int wordId, boolean isLearned, boolean isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_IS_LEARNED, isLearned ? 1 : 0);
        values.put(COL_IS_FAVORITE, isFavorite ? 1 : 0);
        db.update(TABLE_USER_WORDS, values,
                COL_USER_WORD_USER_ID + "=? AND " + COL_USER_WORD_WORD_ID + "=?",
                new String[]{String.valueOf(userId), String.valueOf(wordId)});
        db.close();
    }

    // Xóa từ vựng
    public void deleteWord(int wordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_WORDS, COL_USER_WORD_WORD_ID + "=?", new String[]{String.valueOf(wordId)});
        db.delete(TABLE_WORDS, COL_WORD_ID + "=?", new String[]{String.valueOf(wordId)});
        db.close();
    }

    // Lấy danh sách từ đã học
    public List<Word> getLearnedWords(int userId) {
        List<Word> wordList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT w.*, uw." + COL_IS_LEARNED + ", uw." + COL_IS_FAVORITE + " " +
                        "FROM " + TABLE_WORDS + " w " +
                        "LEFT JOIN " + TABLE_USER_WORDS + " uw ON w." + COL_WORD_ID + " = uw." + COL_USER_WORD_WORD_ID + " " +
                        "WHERE uw." + COL_USER_WORD_USER_ID + " = ? AND uw." + COL_IS_LEARNED + " = 1",
                new String[]{String.valueOf(userId)});
        while (cursor.moveToNext()) {
            Word word = new Word(
                    cursor.getInt(0), // id
                    cursor.getString(1), // english
                    cursor.getString(2), // phonetic
                    cursor.getString(3), // vietnamese
                    cursor.getString(4), // image_file
                    cursor.getString(5), // audio_file
                    cursor.getInt(7) == 1, // is_learned (column 7)
                    cursor.getInt(8) == 1, // is_favorite (column 8)
                    cursor.getString(6) // category
            );
            wordList.add(word);
        }
        cursor.close();
        db.close();
        return wordList;
    }

    // Lấy danh sách từ yêu thích
    public List<Word> getFavoriteWords(int userId) {
        List<Word> wordList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT w.*, uw." + COL_IS_LEARNED + ", uw." + COL_IS_FAVORITE + " " +
                        "FROM " + TABLE_WORDS + " w " +
                        "LEFT JOIN " + TABLE_USER_WORDS + " uw ON w." + COL_WORD_ID + " = uw." + COL_USER_WORD_WORD_ID + " " +
                        "WHERE uw." + COL_USER_WORD_USER_ID + " = ? AND uw." + COL_IS_FAVORITE + " = 1",
                new String[]{String.valueOf(userId)});
        while (cursor.moveToNext()) {
            Word word = new Word(
                    cursor.getInt(0), // id
                    cursor.getString(1), // english
                    cursor.getString(2), // phonetic
                    cursor.getString(3), // vietnamese
                    cursor.getString(4), // image_file
                    cursor.getString(5), // audio_file
                    cursor.getInt(7) == 1, // is_learned (column 7)
                    cursor.getInt(8) == 1, // is_favorite (column 8)
                    cursor.getString(6) // category
            );
            wordList.add(word);
        }
        cursor.close();
        db.close();
        return wordList;
    }
}