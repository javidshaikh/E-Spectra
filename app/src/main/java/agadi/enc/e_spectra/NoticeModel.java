package agadi.enc.e_spectra;

/**
 * Created by Mr.Robot on 9/3/2016.
 */
public class NoticeModel {


    private String PushKey;

    public String getIdKey() {
        return IdKey;
    }

    public void setIdKey(String idKey) {
        IdKey = idKey;
    }

    private String IdKey;

    public String getPushKey() {
        return PushKey;
    }

    public void setPushKey(String pushKey) {
        PushKey = pushKey;
    }

    public NoticeModel(){

    }



    public NoticeModel(String title, String description, String image, String key, String id) {
        this.title = title;
        Description = description;
        Image = image;
        PushKey = key;
        IdKey = id;

    }

    private String Description, Image ,title;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
