package carrental.model;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
/**
 * Created by Constantin on 12.05.2015.
 */
@CsvRecord(separator = ",", crlf = "UNIX")
public class Claim {
    @DataField(pos = 1)
    private String type;
    @DataField(pos = 2)
    private String description;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
