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

/**
 * This class includes utilities to create  {@link Spannable} strings for TextViews
 * every word starting with '@', '#' or http(s) links will be highlighted
 *
 * @author nuclearfog
 */
public abstract class Tagger {

    private static final String LINK_PATTERN_STRING = "[\n\\s]https?://[^\\s]+";
    private static final String TW_PATTERN_STRING = "[\n\\s][@#][^@#\\s\\^\\!\"\\§\\%\\&\\/\\(\\)\\=\\?\\´\\°\\{\\[\\]\\}\\\\\\`\\+\\-\\*\\'\\~\\.\\,\\;\\:\\<\\>\\|]+";
    private static final Pattern LINK_PATTERN = Pattern.compile(LINK_PATTERN_STRING);
    private static final Pattern TW_PATTERN = Pattern.compile(TW_PATTERN_STRING);
    private static final int MODE = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
    private static final int MAX_LINK_LENGTH = 20;


    /**
     * Make a spannable colored String with click listener
     *
     * @param text  String that should be spannable
     * @param color Text Color
     * @param l     click listener
     * @return Spannable String
     */
    public static Spannable makeText(String text, final int color, @NonNull final OnTagClickListener l) {
        final SpannableStringBuilder sText = new SpannableStringBuilder(text);
        sText.insert(0, " "); // Add whitespace at begin to match if target string is at beginning
        Matcher m = TW_PATTERN.matcher(sText);

        while (m.find()) {
            final int start = m.start();
            final int end = m.end();
            sText.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    l.onClick(sText.toString().substring(start, end - 1));
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setColor(color);
                    ds.setUnderlineText(false);
                }
            }, start, end, MODE);
        }
        sText.delete(0, 1); // Remove first whitespace added at the beginning of this method
        return sText;
    }


    /**
     * Make a spannable colored String with click listener
     * http(s) links included
     *
     * @param text  String that should be spannable
     * @param color Text Color
     * @param l     click listener
     * @return Spannable String
     */
    public static Spannable makeTextWithLinks(String text, final int color, @NonNull final OnTagClickListener l) {
        final SpannableStringBuilder sText = new SpannableStringBuilder(makeText(text, color, l));
        sText.insert(0, " "); // Add whitespace at begin to match if target string is at beginning
        Matcher m = LINK_PATTERN.matcher(sText);

        while (m.find()) {
            int start = m.start();
            int end = m.end();
            final String link = sText.toString().substring(start, end - 1);
            if (start + MAX_LINK_LENGTH < end) {
                sText.replace(start + MAX_LINK_LENGTH, end, "...");
                end = start + MAX_LINK_LENGTH;
            }
            sText.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    l.onClick(link);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setColor(color);
                    ds.setUnderlineText(false);
                }
            }, start, end, MODE);
        }
        sText.delete(0, 1); // Remove first whitespace added at the beginning of this method
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
        sText.insert(0, " "); // Add whitespace at begin to match if target string is at beginning
        Matcher m = TW_PATTERN.matcher(sText);

        while (m.find()) {
            final int start = m.start();
            final int end = m.end();
            ForegroundColorSpan sColor = new ForegroundColorSpan(color);
            sText.setSpan(sColor, start, end, MODE);
        }
        sText.delete(0, 1); // Remove first whitespace added at the beginning of this method
        return sText;
    }


    /**
     * Make a spannable String without listener
     * http(s) links included will be shorted
     *
     * @param text  String that should be spannable
     * @param color Text Color
     * @return Spannable String
     */
    public static Spannable makeTextWithLinks(String text, int color) {
        SpannableStringBuilder sText = new SpannableStringBuilder(makeText(text, color));
        sText.insert(0, " "); // Add whitespace at begin to match if target string is at beginning
        Matcher m = LINK_PATTERN.matcher(sText);

        while (m.find()) {
            int start = m.start();
            int end = m.end();
            if (start + MAX_LINK_LENGTH < end) {
                sText.replace(start + MAX_LINK_LENGTH, end, "...");
                end = start + MAX_LINK_LENGTH;
            }
            ForegroundColorSpan sColor = new ForegroundColorSpan(color);
            sText.setSpan(sColor, start, end, MODE);
        }
        sText.delete(0, 1); // Remove first whitespace added at the beginning of this method
        return sText;
    }


    /**
     * Listener for clickable spans
     */
    public interface OnTagClickListener {
        /**
         * Called when user clicks on a tag
         *
         * @param tag Tag string (starting with '@', '#' or http)
         */
        void onClick(String tag);
    }
}