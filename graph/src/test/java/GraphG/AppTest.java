package GraphG;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import edu.uci.ics.jung.graph.Graph;

/**
 * Unit test for simple App.
 */
public class AppTest extends SIS
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testGF  ()
    {
    	String NameFile = "C:\\\\eclipse\\\\usr\\\\AnsCalT.net" ;
        Graph<Node, Integer> g = loadGraphPajek(NameFile);
        frame(g);
    	 Assert.assertEquals(g.getVertexCount(),11);
    }
}
