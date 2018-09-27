Hashtag-highlighting

- highlight #hashtags and @usernames and makes them clickable
- customizable color
- easy to use


Gradle integration:

implementation 'com.github.NudeDude:Tagger:1.0'



```java
String text = "#tag test @name test"
int redColor = 0xffff0000;

Spannable span = Tagger.makeText(text, redColor);

Spannable spanClick = Tagger.makeText(text, redColor, new Tagger.OnTagClickListener() {
                @Override
                public void onClick(String tag) {
                    // handle on click
                }
            });
            
 textView.setText(spanClick);
```
