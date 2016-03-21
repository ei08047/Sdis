package messages;

/**
 * Created by zabrn on 19/03/2016.
 */
public class ParseHeader {

    public String[] parse(String header)
    {
        String[] fields = null;
        fields = header.split(" ");
        return fields;
    }
}
