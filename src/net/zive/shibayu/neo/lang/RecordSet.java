package net.zive.shibayu.neo.lang;

import java.util.LinkedList;

/**
 * レコードセット.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class RecordSet extends LinkedList<Row> {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public final String toString() {
        StringBuilder stb = new StringBuilder();
        String tmp = "";
        stb.append("{");
        for (Row row : this) {
            stb.append(tmp + "[" + row.toString() + "]");
            tmp = ",";
        }
        stb.append("}");
        return stb.toString();
    }
}
