import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PackageManagerTest.java created by Zhengjia Mao on Macbook Pro in p4
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
 * PackageManagerTest - JUnit Test Class that tests operations of your PackageManager class
 * 
 * @author zmao27 (2020)
 *
 */
public class PackageManagerTest {

	protected PackageManager pm;

	/**
	 * Add code that runs before each test method
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		pm = new PackageManager();
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
	 * Tests whether constructGraph throws an exception when reads in a valid json
	 * file;
	 */
	@Test
	public void test000_constructGraph_read() {
		try {
			String path = new File("").getAbsolutePath() + "/";
			pm.constructGraph(path + "valid.json");
		} catch (Exception e) {
			fail("unexpected");
		}
	}

	/**
	 * Tests whether constructGraph creates a coorect graph when reads in a valid
	 * json file;
	 */
	@Test
	public void test001_constructGraph_graph() {
		try {
			String path = new File("").getAbsolutePath() + "/";
			pm.constructGraph(path + "valid.json");
		} catch (Exception e) {
			fail("unexpected");
		}
		Set<String> s = new HashSet<String>();
		s.add("A");
		s.add("B");
		s.add("C");
		s.add("D");
		s.add("E");
		Assert.assertEquals(pm.getAllPackages(), s);
	}

	/**
	 * Tests whether getPackageWithMaxDependencies() outputs the correct package;
	 */
	@Test
	public void test002_max_dep() {
		try {
			String path = new File("").getAbsolutePath() + "/";
			pm.constructGraph(path + "valid.json");
		} catch (Exception e) {
			fail("unexpected");
		}
		try {
			Assert.assertEquals(pm.getPackageWithMaxDependencies(), "B");
		} catch (CycleException e) {
			fail("unexpected");
		}
	}

	/**
	 * Test whether of a given getInstallationOrder() outputs a correct list;
	 */
	@Test
	public void test003_getInstallationOrder() {
		try {
			String path = new File("").getAbsolutePath() + "/";
			pm.constructGraph(path + "shared_dependencies.json");
		} catch (Exception e) {
			fail("unexpected");
		}
		List<String> s1 = new ArrayList<String>();
		List<String> s2 = new ArrayList<String>();
		s1.add("D");
		s1.add("C");
		s1.add("B");
		s1.add("A");
		s2.add("D");
		s2.add("B");
		s2.add("C");
		s2.add("A");
		try {
			if (!(pm.getInstallationOrder("A").equals(s1) || pm.getInstallationOrder("A").equals(s2))) {
				fail("Output mismatched");
			}
		} catch (CycleException e) {
			e.printStackTrace();
			fail("unexpected");
		} catch (PackageNotFoundException e) {
			e.printStackTrace();
			fail("unexpected");
		}
	}

	/**
	 * Tests whether getInstallationOrderForAllPackages() gives a correct output
	 * String list;
	 */
	@Test
	public void test004_getInstallationOrderForAllPackages() {
		try {
			String path = new File("").getAbsolutePath() + "/";
			pm.constructGraph(path + "shared_dependencies.json");
		} catch (Exception e) {
			fail("unexpected");
		}
		List<String> s1 = new ArrayList<String>();
		List<String> s2 = new ArrayList<String>();
		s1.add("D");
		s1.add("C");
		s1.add("B");
		s1.add("A");
		s2.add("D");
		s2.add("B");
		s2.add("C");
		s2.add("A");
		try {
			if (!(pm.getInstallationOrderForAllPackages().equals(s1)
					|| pm.getInstallationOrderForAllPackages().equals(s2))) {
				fail("Output mismatched");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexpected");
		}
	}

	/**
	 * Tests whether toInstall() gives a correct output list;
	 */
	@Test
	public void test005_toInstall() {
		try {
			String path = new File("").getAbsolutePath() + "/";
			pm.constructGraph(path + "valid.json");
		} catch (Exception e) {
			fail("unexpected");
		}
		List<String> s1 = new ArrayList<String>();
		s1.add("A");
		try {
			Assert.assertEquals(pm.toInstall("A", "B"), s1);
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexpected");
		}

	}

	/**
	 * Tests whether correctly detects cycles when read in a cyclic json file;
	 */
	@Test
	public void test006_Cyclic_Detection() {
		String path = new File("").getAbsolutePath() + "/";
		boolean result = false;
		try {
			pm.constructGraph(path + "cyclic.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexpected");
		}
		try {
			pm.getInstallationOrderForAllPackages();
		} catch (CycleException e) {
			result = true;
		} catch (Exception e) {
			fail("unexpected");
		}
		if (!result) {
			fail("didnt catch expected exception");
		}
	}

	/**
	 * Test whether getInstallationOrder() throws an exception when the argument
	 * does not exist;
	 */
	@Test
	public void test007_getOrder_Non_Existed() {
		String path = new File("").getAbsolutePath() + "/";
		boolean result = false;
		try {
			pm.constructGraph(path + "valid.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexpected");
		}
		try {
			pm.getInstallationOrder("W");
		} catch (PackageNotFoundException e) {
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexpected");
		}
		if (!result) {
			fail("didnt catch expected exception");
		}
	}
}
