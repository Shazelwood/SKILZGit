package com.hazelwood.skilz;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Hazelwood on 5/20/15.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        String sourceString = String.valueOf(text);
        String string =
                sourceString.replace("[", "<h1>").replace("]", "</h1>") // Header
                .replace("{", "&#8226").replace("}", "</br>"); // Tab

        super.setText(Html.fromHtml(string), BufferType.SPANNABLE);
    }
}
