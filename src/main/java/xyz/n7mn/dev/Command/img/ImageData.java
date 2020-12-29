package xyz.n7mn.dev.Command.img;

public class ImageData {

    private final String ImageURL;
    private final String Description;

    public ImageData(String imageURL, String description){
        this.ImageURL = imageURL;
        this.Description = description;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public String getDescription() {
        return Description;
    }
}
