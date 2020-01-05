package org.nuclearfog.tag;

import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import java.util.Stack;
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
    private static final int MAX_LINK_LENGTH = 30;


    /**
     * Make a spannable colored String with click listener
     *
     * @param text  String that should be spannable
     * @param color Text Color
     * @param l     click listener
     * @return Spannable String
     */
    public static Spannable makeText(String text, final int color, @NonNull final OnTagClickListener l) {
        SpannableStringBuilder sText = new SpannableStringBuilder(" " + text);

        /// Add '@' & '#' highlighting + listener
        Matcher m = TW_PATTERN.matcher(sText);
        while (m.find()) {
            int end = m.end();
            int start = m.start() + 1;
            final CharSequence tag = sText.subSequence(start, end);
            sText.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    l.onTagClick(tag.toString());
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setColor(color);
                    ds.setUnderlineText(false);
                }
            }, start, end, MODE);
        }
        sText.subSequence(1, sText.length() - 1);
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
        SpannableStringBuilder sText = new SpannableStringBuilder(" " + text);

        /// Add '@' & '#' highlighting + listener
        Matcher twMatcher = TW_PATTERN.matcher(sText);
        while (twMatcher.find()) {
            int end = twMatcher.end();
            int start = twMatcher.start() + 1;
            final CharSequence twStr = sText.subSequence(start, end);
            sText.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    l.onTagClick(twStr.toString());
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setColor(color);
                    ds.setUnderlineText(false);
                }
            }, start, end, MODE);
        }

        /// Add link highlight + listener
        Stack<Integer> stack = new Stack<>();
        Matcher lMatcher = LINK_PATTERN.matcher(sText.toString());
        while (lMatcher.find()) {
            stack.push(lMatcher.start());
            stack.push(lMatcher.end());
        }
        while (!stack.empty()) {
            int end = stack.pop();
            int start = stack.pop() + 1;
            final CharSequence link = sText.subSequence(start, end);
            if (start + MAX_LINK_LENGTH < end) {
                sText.replace(start + MAX_LINK_LENGTH, end, "...");
                end = start + MAX_LINK_LENGTH + 3;
            }
            sText.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    l.onLinkClick(link.toString());
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setColor(color);
                    ds.setUnderlineText(false);
                }
            }, start, end, MODE);
        }
        sText.subSequence(1, sText.length() - 1);
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
        SpannableStringBuilder sText = new SpannableStringBuilder(" " + text);

        /// Add '@' & '#' highlighting
        Matcher m = TW_PATTERN.matcher(sText.toString());
        while (m.find()) {
            int end = m.end();
            int start = m.start() + 1;
            ForegroundColorSpan sColor = new ForegroundColorSpan(color);
            sText.setSpan(sColor, start, end, MODE);
        }
        sText.subSequence(1, sText.length() - 1); // Remove first whitespace added at the beginning of this method
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
        SpannableStringBuilder sText = new SpannableStringBuilder(" " + text);

        /// Add '@' & '#' highlighting
        Matcher twMatcher = TW_PATTERN.matcher(sText.toString());
        while (twMatcher.find()) {
            int end = twMatcher.end();
            int start = twMatcher.start() + 1;
            ForegroundColorSpan sColor = new ForegroundColorSpan(color);
            sText.setSpan(sColor, start, end, MODE);
        }

        /// Add link highlighting
        Stack<Integer> stack = new Stack<>();
        Matcher lMatcher = LINK_PATTERN.matcher(sText.toString());
        while (lMatcher.find()) {
            stack.push(lMatcher.start());
            stack.push(lMatcher.end());
        }
        while (!stack.empty()) {
            int end = stack.pop();
            int start = stack.pop() + 1;
            if (start + MAX_LINK_LENGTH < end) {
                sText.replace(start + MAX_LINK_LENGTH, end, "...");
                end = start + MAX_LINK_LENGTH + 3;
            }
            ForegroundColorSpan sColor = new ForegroundColorSpan(color);
            sText.setSpan(sColor, start, end, MODE);
        }
        sText.subSequence(1, sText.length() - 1); // Remove first whitespace added at the beginning of this method
        return sText;
    }


    /**
     * Listener for clickable spans
     */
    public interface OnTagClickListener {
        /**
         * Called when user clicks on a tag
         *
         * @param tag Tag string (starting with '@', '#')
         */
        void onTagClick(String tag);

        /**
         * Called when user clicks on link
         *
         * @param link http(s) link
         */
        void onLinkClick(String link);
    }
}