package xyz.n7mn.dev.game;

public class GoldImg {
    private String URL;
    private String Content;

    public GoldImg(String url, String content){
        this.URL = url;
        this.Content = content;
    }


    public String getURL() {
        return URL;
    }

    public String getContent() {
        return Content;
    }
}
