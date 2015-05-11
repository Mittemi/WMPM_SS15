package carrental.beans;

import org.apache.camel.Exchange;

/**
 * Created by Michael on 07.05.2015.
 */
public class DemoPrintTextBean {

    public void process(String s) {
        System.out.println("We got it! " + s);
    }
}
