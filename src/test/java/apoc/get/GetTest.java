package apoc.get;

import apoc.util.TestUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.internal.helpers.collection.Iterators;
import org.neo4j.test.rule.DbmsRule;
import org.neo4j.test.rule.ImpermanentDbmsRule;

import java.util.Collection;

import static apoc.util.MapUtil.map;
import static org.junit.Assert.assertEquals;

/**
 * @author mh
 * @since 16.04.16
 */
public class GetTest {

    @Rule
    public static DbmsRule db = new ImpermanentDbmsRule();

    @Before
    public void setUp() throws Exception {
        TestUtil.registerProcedure(db,Get.class);
    }

    @Test
    public void testNodes() throws Exception {
        Collection<Object> ids = Iterators.asSet(db.execute("UNWIND range(0,2) as id CREATE (n:Node {id:id}) return id(n) as id").columnAs("id"));
        TestUtil.testResult(db, "CALL apoc.get.nodes({ids})", map("ids",ids), r -> {
            assertEquals(true, ids.contains(((Node) r.next().get("node")).getId()));
            assertEquals(true, ids.contains(((Node) r.next().get("node")).getId()));
            assertEquals(true, ids.contains(((Node) r.next().get("node")).getId()));
        });
    }

    @Test
    public void testRels() throws Exception {
        Collection<Object> ids = Iterators.asSet(db.execute("CREATE (n) WITH n UNWIND range(0,2) as id CREATE (n)-[r:KNOWS]->(n) return id(r) as id").columnAs("id"));
        TestUtil.testResult(db, "CALL apoc.get.rels({ids})", map("ids",ids), r -> {
            assertEquals(true, ids.contains(((Relationship) r.next().get("rel")).getId()));
            assertEquals(true, ids.contains(((Relationship) r.next().get("rel")).getId()));
            assertEquals(true, ids.contains(((Relationship) r.next().get("rel")).getId()));
        });
    }
}
