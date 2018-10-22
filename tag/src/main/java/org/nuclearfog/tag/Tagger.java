package org.nuclearfog.tag;

import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Tagger {

    private static final String S_CHARS = "\\.\\,\\?\\!\\:\\;\\(\\)\\{\\}\\[\\]\"\'\\+\\-";
    private static final String PATTERN = "[@#][^@#\\s "+S_CHARS+"]+";

    private static final int MODE = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

    /**
     * Make a spannable colored String with click listener
     *
     * @param text  String that should be spannable
     * @param color Text Color
     * @param l     click listener
     * @return Spannable String
     */
    public static Spannable makeText(final String text, final int color, @NonNull final OnTagClickListener l) {
        SpannableStringBuilder sText = new SpannableStringBuilder(text);
        Pattern p = Pattern.compile(PATTERN);
        Matcher m = p.matcher(text);

        while (m.find()) {
            final int start = m.start();
            final int end = m.end();

            sText.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    l.onClick(text.substring(start, end));
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setColor(color);
                    ds.setUnderlineText(false);
                }
            }, start, end, MODE);
        }
        return sText;
    }


    /**
     * Make a spannable String without listener
     *
     * @param text  String that should be spannable
     * @param color Text Color
     * @return Spannable String
     */
    public static Spannable makeText(String text, int color) {
        SpannableStringBuilder sText = new SpannableStringBuilder(text);
        Pattern p = Pattern.compile(PATTERN);
        Matcher m = p.matcher(text);

        while (m.find()) {
            final int start = m.start();
            final int end = m.end();

            ForegroundColorSpan sColor = new ForegroundColorSpan(color);
            sText.setSpan(sColor, start, end, MODE);
        }
        return sText;
    }


    /**
     * Interface definition of Tag click listener
     */
    public interface OnTagClickListener {
        /**
         * Called on tag click
         *
         * @param tag Clicked Tag
         */
        void onClick(String tag);
    }
}