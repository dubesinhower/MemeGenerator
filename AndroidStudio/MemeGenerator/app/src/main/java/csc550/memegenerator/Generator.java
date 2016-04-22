package csc550.memegenerator;

/**
 * Created by Christopher on 4/11/2016.
 */
public class Generator {
    public String displayName;
    public String generatorName;
    public String imageUrl;
    public int generatorId;
    public int imageId;
    public Generator(){

    };
    public Generator(String displayName, String generatorName, int generatorId, String imageUrl, int imageId) {
        this.displayName = displayName;
        this.generatorName = generatorName;
        this.generatorId = generatorId;
        this.imageUrl = imageUrl;
        this.imageId = imageId;
    }
}
