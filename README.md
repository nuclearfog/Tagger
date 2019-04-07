<p align="center"><img src="/logo/logotype-horizontal.png"></p>


- highlight #hashtag and @username and make them clickable
- customizable color


Project integration:

[![](https://jitpack.io/v/NudeDude/Tagger.svg)](https://jitpack.io/#NudeDude/Tagger)


Code example:

```java
String text = "#tag test @name test"
int redColor = 0xffff0000;

// Highlight text

Spannable span = Tagger.makeText(text, redColor);

textView1.setText(span);


// Highlight text and add Listener

Spannable spanClick = Tagger.makeText(text, redColor, new Tagger.OnTagClickListener() {
                @Override
                public void onClick(String tag) {
                    // handle on click
                }
            });
            
 textView2.setText(spanClick);
```

Logo by <a href="https://github.com/Tobaloidee">@Tobaloidee</a>
