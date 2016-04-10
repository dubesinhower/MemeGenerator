package csc550.memegenerator;

/**
 * Created by Christopher on 4/9/2016.
 */
public class Meme {
    public String displayName;
    public String generatorName;
    public String instanceUrl;
    public Meme() {
    }
    public Meme(String displayName, String generatorName, String instanceUrl) {
        this.displayName = displayName;
        this.generatorName = generatorName;
        this.instanceUrl = instanceUrl;
    }
}
