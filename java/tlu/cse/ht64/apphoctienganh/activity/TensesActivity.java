package tlu.cse.ht64.apphoctienganh.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import tlu.cse.ht64.apphoctienganh.R;
import tlu.cse.ht64.apphoctienganh.model.Tense;

import java.util.ArrayList;
import java.util.List;

public class TensesActivity extends AppCompatActivity {
    private ListView lvTenses;
    private List<Tense> tenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenses);

        lvTenses = findViewById(R.id.lvTenses);
        tenseList = new ArrayList<>();

        // Hiện tại
        tenseList.add(new Tense(
                "Present Simple",
                "Diễn tả thói quen, sự thật hiển nhiên.",
                "S + V(s/es) (đối với I, you, we, they thì V; đối với he, she, it thì V-s/es).",
                "I go to school every day.",
                "I don't go to school every day.",
                "Do I go to school every day?",
                "She walks to the park every morning."
        ));
        tenseList.add(new Tense(
                "Present Continuous",
                "Diễn tả hành động đang xảy ra tại thời điểm nói.",
                "S + am/is/are + V-ing.",
                "I am reading a book.",
                "I am not reading a book.",
                "Am I reading a book?",
                "They are playing football right now."
        ));
        tenseList.add(new Tense(
                "Present Perfect",
                "Diễn tả hành động đã hoàn thành tính đến hiện tại.",
                "S + have/has + V3/ed.",
                "I have just finished my homework.",
                "I have not finished my homework.",
                "Have I finished my homework?",
                "She has lived here for 5 years."
        ));

        // Quá khứ
        tenseList.add(new Tense(
                "Past Simple",
                "Diễn tả hành động đã hoàn thành trong quá khứ.",
                "S + V2/ed (đối với động từ bất quy tắc thì dùng cột 2).",
                "I went to the zoo yesterday.",
                "I didn't go to the zoo yesterday.",
                "Did I go to the zoo yesterday?",
                "They watched a movie last night."
        ));
        tenseList.add(new Tense(
                "Past Continuous",
                "Diễn tả hành động đang xảy ra tại một thời điểm trong quá khứ.",
                "S + was/were + V-ing.",
                "I was studying at 8 PM last night.",
                "I was not studying at 8 PM last night.",
                "Was I studying at 8 PM last night?",
                "We were playing games when it started to rain."
        ));
        tenseList.add(new Tense(
                "Past Perfect",
                "Diễn tả hành động xảy ra trước một hành động khác trong quá khứ.",
                "S + had + V3/ed.",
                "I had finished my homework before I went out.",
                "I had not finished my homework before I went out.",
                "Had I finished my homework before I went out?",
                "She had left when I arrived."
        ));

        // Tương lai
        tenseList.add(new Tense(
                "Future Simple",
                "Diễn tả hành động sẽ xảy ra trong tương lai.",
                "S + will + V.",
                "I will visit my grandparents tomorrow.",
                "I will not visit my grandparents tomorrow.",
                "Will I visit my grandparents tomorrow?",
                "They will travel to Japan next month."
        ));
        tenseList.add(new Tense(
                "Future Continuous",
                "Diễn tả hành động sẽ đang xảy ra tại một thời điểm trong tương lai.",
                "S + will be + V-ing.",
                "I will be working at 10 AM tomorrow.",
                "I will not be working at 10 AM tomorrow.",
                "Will I be working at 10 AM tomorrow?",
                "She will be studying when you arrive."
        ));
        tenseList.add(new Tense(
                "Future Perfect",
                "Diễn tả hành động sẽ hoàn thành trước một thời điểm trong tương lai.",
                "S + will have + V3/ed.",
                "I will have finished my project by next week.",
                "I will not have finished my project by next week.",
                "Will I have finished my project by next week?",
                "They will have arrived by the time the meeting starts."
        ));

        ArrayAdapter<Tense> adapter = new ArrayAdapter<Tense>(this, R.layout.item_tense, tenseList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tense, parent, false);
                }

                Tense tense = getItem(position);
                TextView tvTenseName = convertView.findViewById(R.id.tvTenseName);
                TextView tvTenseUsage = convertView.findViewById(R.id.tvTenseUsage);
                TextView tvTenseStructure = convertView.findViewById(R.id.tvTenseStructure);
                TextView tvTenseAffirmative = convertView.findViewById(R.id.tvTenseAffirmative);
                TextView tvTenseNegative = convertView.findViewById(R.id.tvTenseNegative);
                TextView tvTenseQuestion = convertView.findViewById(R.id.tvTenseQuestion);
                TextView tvTenseExample = convertView.findViewById(R.id.tvTenseExample);

                tvTenseName.setText(tense.getName());
                tvTenseUsage.setText("Usage: " + tense.getUsage());
                tvTenseStructure.setText("Structure: " + tense.getStructure());
                tvTenseAffirmative.setText("Affirmative: " + tense.getAffirmative());
                tvTenseNegative.setText("Negative: " + tense.getNegative());
                tvTenseQuestion.setText("Question: " + tense.getQuestion());
                tvTenseExample.setText("Example: " + tense.getExample());

                return convertView;
            }
        };
        lvTenses.setAdapter(adapter);
    }
}