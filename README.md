# Hashtag-highlighting

- highlight #hashtag and @username and make them clickable
- customizable color
- easy to use


Gradle integration:

```java
dependencies {
  implementation 'com.github.NudeDude:Tagger:1.0'
  ...
}
```


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
