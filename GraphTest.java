import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * GraphTest.java created by Zhengjia Mao on Macbook Pro in p4
 *
 * Author: 	 Zhengjia Mao(zmao27@wisc.edu)
 * Date: 	 @date 04/2/2020
 * 
 * Course: 	 CS400
 * Semester: Spring 2020
 * Lecture:	 002
 * 
 * IDE:		 Eclipse IDE for Java Developers
 * Version:  2019-12 (4.14.0)
 * Build id: 20191212-1212
 *
 * Device: 	 Zane's MacBook Pro
 * OS: 		 macOS Mojave
 * Version:  10.14.6
 * OS Build: 18G95
 *
 * List Collaborators: NONE
 *
 * Other Credits: NONE
 *
 * Known Bugs: NONE
 */

/**
 * GraphTest - JUnit Test Class that tests operations of GraphADT for your Graph
 * class
 * 
 * @author zmao27 (2020)
 *
 */
public class GraphTest {

	protected Graph graph;

	/**
	 * Add code that runs before each test method
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		graph = new Graph();
	}

	/**
	 * Add code that runs after each test method
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {

	}

	/**
	 * Tests whether add null vertex throws exceptions
	 */
	@Test
	public void test000_add_null_vertex() {
		try {
			graph.addVertex(null);
		} catch (Exception e) {
			fail("unexpected");
		}
	}

	/**
	 * Tests whether add existed vertex throws exceptions
	 */
	@Test
	public void test001_add_existed_vertex() {
		try {
			graph.addVertex("1");
			graph.addVertex("1");
		} catch (Exception e) {
			fail("unexpected");
		}
	}

	/**
	 * Tests whether remove null vertex throws exceptions
	 */
	@Test
	public void test003_remove_null_vertex() {
		try {
			graph.removeVertex(null);
		} catch (Exception e) {
			fail("unexpected");
		}
	}

	/**
	 * Tests whether remove non-existed vertex throws exceptions
	 */
	@Test
	public void test004_remove_non_existed_vertex() {
		try {
			graph.removeVertex("A");
		} catch (Exception e) {
			fail("unexpected");
		}
	}

	/**
	 * Tests whether add edge with duplicate vertexs throws exceptions
	 */
	@Test
	public void test005_add_edge_duplicate_vertex() {
		try {
			graph.addEdge("A", "A");
			;
		} catch (Exception e) {
			fail("unexpected");
		}
	}

	/**
	 * Tests whether add edge with one existed vertex and one non-existed vertex
	 * throws exceptions, and wether correctly creates the edge.
	 */
	@Test
	public void test006_add_edge_existed_non_existed() {
		try {
			graph.addVertex("A");
			graph.addEdge("A", "B");
		} catch (Exception e) {
			fail("unexpected");
		}
		if (graph.getAllEdges().size() != 1) {
			fail("edge not added");
		}
	}

	/**
	 * Tests whether add edge with both non-existed vertexs throws exceptions, and
	 * wether correctly creates the edge.
	 */
	@Test
	public void test007_add_edge_non_existed_non_existed() {
		try {
			graph.addEdge("A", "B");
		} catch (Exception e) {
			fail("unexpected");
		}
		if (graph.getAllEdges().size() != 1) {
			fail("edge not added");
		}
	}

	/**
	 * Tests whether remove edge with both non-existed vertexs/null
	 * vertexs/duplicate vertexs throws exceptions, and wether removes the existing
	 * edge.
	 */
	@Test
	public void test008_remove_edge_problematic() {
		try {
			graph.addEdge("C", "D");
			graph.removeEdge(null, null);
			graph.removeEdge("A", "B");
			graph.removeEdge("A", "A");
		} catch (Exception e) {
			fail("unexpected");
		}
		if (graph.getAllEdges().size() != 1) {
			fail("edge unpected removed");
		}
	}

	/**
	 * Tests whether call getAllVertices gives the correct output size
	 */
	@Test
	public void test009_all_vertex() {
		try {
			graph.addVertex("A");
			graph.addVertex("B");
			graph.addVertex("C");
		} catch (Exception e) {
			fail("unexpected");
		}
		if (graph.getAllVertices().size() != 3) {
			fail("incorrect number");
		}
	}

	/**
	 * Tests whether call getAllEdges gives the correct output size
	 */
	@Test
	public void test010_all_edge() {
		try {
			graph.addEdge("A", "B");
			graph.addEdge("C", "D");
			graph.addEdge("E", "F");
		} catch (Exception e) {
			fail("unexpected");
		}
		if (graph.getAllEdges().size() != 3) {
			fail("incorrect number");
		}
	}

	/**
	 * Tests whether call getAdjacentVerticesOf() gives the correct output size
	 */
	@Test
	public void test011_get_ad() {
		try {
			graph.addEdge("A", "B");
			graph.addEdge("A", "C");
		} catch (Exception e) {
			fail("unexpected");
		}
		if (graph.getAdjacentVerticesOf("A").size() != 2) {
			fail("incorrect number");
		}
	}
}
