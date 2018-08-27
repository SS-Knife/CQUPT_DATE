package com.example.cqupt_date.app.Chat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class LetterView extends LinearLayout {

    public LetterView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        updateLetters();
    }

    public LetterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        updateLetters();
    }

    private void updateLetters() {
        setLetters(getSortLetters());
    }

    /**
     * 设置快速滑动的字母集合
     */
    public void setLetters(List<Character> letters) {
        removeAllViews();
        for (Character content : letters) {
            TextView view = new TextView(getContext());
            LayoutParams param = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1.0f);
            view.setLayoutParams(param);
            view.setText(content.toString());
            addView(view);
        }

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = Math.round(event.getX());
                int y = Math.round(event.getY());
                for (int i = 0; i < getChildCount(); i++) {
                    TextView child = (TextView) getChildAt(i);
                    if (y > child.getTop() && y < child.getBottom()) {
                        MemberLetterEvent letterEvent = new MemberLetterEvent();
                        letterEvent.letter = child.getText().toString().charAt(0);
                        EventBus.getDefault().post(letterEvent);
                    }
                }
                return true;
            }
        });
    }

    /**
     * 默认的只包含 A-Z 的字母
     */
    private List<Character> getSortLetters() {
        List<Character> letterList = new ArrayList<Character>();
        for (char c = 'A'; c <= 'Z'; c++) {
            letterList.add(c);
        }
        return letterList;
    }
}
