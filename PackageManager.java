import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * PackageManager.java created by Zhengjia Mao on Macbook Pro in p4
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
 * PackageManager is used to process json package dependency files and provide
 * function that make that information available to other users.
 * 
 * Each package that depends upon other packages has its own entry in the json
 * file.
 * 
 * Package dependencies are important when building software, as you must
 * install packages in an order such that each package is installed after all of
 * the packages that it depends on have been installed.
 * 
 * For example: package A depends upon package B, then package B must be
 * installed before package A.
 * 
 * This program will read package information and provide information about the
 * packages that must be installed before any given package can be installed.
 * all of the packages in
 * 
 * You may add a main method, but we will test all methods with our own Test
 * classes.
 */
public class PackageManager {

	private Graph graph;
	private ArrayList<String> install;

	/*
	 * Package Manager default no-argument constructor.
	 */
	public PackageManager() {
		graph = new Graph();
		install = new ArrayList<String>();
	}

	/**
	 * Takes in a file path for a json file and builds the package dependency graph
	 * from it.
	 * 
	 * @param jsonFilepath the name of json data file with package dependency
	 *                     information
	 * @throws FileNotFoundException if file path is incorrect
	 * @throws IOException           if the give file cannot be read
	 * @throws ParseException        if the given json cannot be parsed
	 */
	public void constructGraph(String jsonFilepath) throws FileNotFoundException, IOException, ParseException {
		Object obj = new JSONParser().parse(new FileReader(jsonFilepath));
		JSONObject jo = (JSONObject) obj;
		JSONArray packages = (JSONArray) jo.get("packages");

		for (int i = 0; i < packages.size(); i++) {
			JSONObject jsonPackage = (JSONObject) packages.get(i);
			String packageName = (String) jsonPackage.get("name");
			graph.addVertex(packageName);
			JSONArray packageDependencies = (JSONArray) jsonPackage.get("dependencies");
			System.out.print(packageName + " has " + packageDependencies.size() + " dependencies: ");
			for (int j = 0; j < packageDependencies.size(); j++) {
				System.out.print(packageDependencies.get(j) + ", ");
				graph.addEdge((String) packageDependencies.get(j), packageName);
			}
			System.out.println();
		}
		System.out.println("Found " + packages.size() + " packages");

	}

	/**
	 * Helper method to get all packages in the graph.
	 * 
	 * @return Set<String> of all the packages
	 */
	public Set<String> getAllPackages() {
		Set<String> s = graph.getAllVertices();
		return s;
	}

	/**
	 * Given a package name, returns a list of packages in a valid installation
	 * order.
	 * 
	 * Valid installation order means that each package is listed before any
	 * packages that depend upon that package.
	 * 
	 * @return List<String>, order in which the packages have to be installed
	 * 
	 * @throws CycleException           if you encounter a cycle in the graph while
	 *                                  finding the installation order for a
	 *                                  particular package. Tip: Cycles in some
	 *                                  other part of the graph that do not affect
	 *                                  the installation order for the specified
	 *                                  package, should not throw this exception.
	 * 
	 * @throws PackageNotFoundException if the package passed does not exist in the
	 *                                  dependency graph.
	 */
	public List<String> getInstallationOrder(String pkg) throws CycleException, PackageNotFoundException {
		if (getAllPackages() == null) {
			return null;
		}
		if (!getAllPackages().contains(pkg)) {
			throw new PackageNotFoundException();
		}
		if (graph.hasCycle(pkg)) {
			throw new CycleException();
		}
		return graph.OrderFrom(pkg);
	}

	/**
	 * Given two packages - one to be installed and the other installed, return a
	 * List of the packages that need to be newly installed.
	 * 
	 * For example, refer to shared_dependecies.json - toInstall("A","B") If package
	 * A needs to be installed and packageB is already installed, return the list
	 * ["A", "C"] since D will have been installed when B was previously installed.
	 * 
	 * @return List<String>, packages that need to be newly installed.
	 * 
	 * @throws CycleException           if you encounter a cycle in the graph while
	 *                                  finding the dependencies of the given
	 *                                  packages. If there is a cycle in some other
	 *                                  part of the graph that doesn't affect the
	 *                                  parsing of these dependencies, cycle
	 *                                  exception should not be thrown.
	 * 
	 * @throws PackageNotFoundException if any of the packages passed do not exist
	 *                                  in the dependency graph.
	 */
	public List<String> toInstall(String newPkg, String installedPkg) throws CycleException, PackageNotFoundException {
		if (getAllPackages() == null) {
			return null;
		}
		if (!getAllPackages().contains(installedPkg)) {
			throw new PackageNotFoundException();
		}
		if (graph.graphHasCycle()) {
			throw new CycleException();
		}
		install.clear();
		install.add(newPkg);
		installAddHelper(newPkg);
		installRemoveHelper(installedPkg);
		install.remove(installedPkg);
		System.out.println(install.toString());
		return install;
	}

	/**
	 * Private recursive helper method that add a pkg to the install order list
	 * 
	 * @param pkg
	 */
	private void installAddHelper(String pkg) {
		if (!graph.getPredecessorsOf(pkg).isEmpty()) {
			for (String e : graph.getPredecessorsOf(pkg)) {
				if (!install.contains(e)) {
					install.add(e);
				}
				installAddHelper(e);
			}
		}
	}

	/**
	 * Private recursive helper method that removes a pkg to the install order list
	 * 
	 * @param pkg
	 */
	private void installRemoveHelper(String pkg) {
		if (!graph.getPredecessorsOf(pkg).isEmpty()) {
			for (String e : graph.getPredecessorsOf(pkg)) {
				if (install.contains(e)) {
					install.remove(e);
				}
				installRemoveHelper(e);
			}
		}
	}

	/**
	 * Return a valid global installation order of all the packages in the
	 * dependency graph.
	 * 
	 * assumes: no package has been installed and you are required to install all
	 * the packages
	 * 
	 * returns a valid installation order that will not violate any dependencies
	 * 
	 * @return List<String>, order in which all the packages have to be installed
	 * @throws CycleException if you encounter a cycle in the graph
	 */
	public List<String> getInstallationOrderForAllPackages() throws CycleException {
		if (getAllPackages() == null) {
			return null;
		}
		if (graph.graphHasCycle()) {
			throw new CycleException();
		}
		return graph.allOder();
	}

	/**
	 * Find and return the name of the package with the maximum number of
	 * dependencies.
	 * 
	 * Tip: it's not just the number of dependencies given in the json file. The
	 * number of dependencies includes the dependencies of its dependencies. But, if
	 * a package is listed in multiple places, it is only counted once.
	 * 
	 * Example: if A depends on B and C, and B depends on C, and C depends on D.
	 * Then, A has 3 dependencies - B,C and D.
	 * 
	 * @return String, name of the package with most dependencies.
	 * @throws CycleException if you encounter a cycle in the graph
	 */
	public String getPackageWithMaxDependencies() throws CycleException {
		if (getAllPackages() == null) {
			return null;
		}
		if (graph.graphHasCycle()) {
			throw new CycleException();
		}
		Set<String> al = graph.getAllVertices();
		int maxDep = 0;
		String maxDepV = "";
		for (String e : al) {
			if (graph.getPredecessorsOf(e).size() > maxDep) {
				maxDep = graph.getPredecessorsOf(e).size();
				maxDepV = e;

			}
		}
		return maxDepV;
	}

	public static void main(String[] args)
			throws FileNotFoundException, IOException, ParseException, CycleException, PackageNotFoundException {
		System.out.println("PackageManager.main()");
		PackageManager pm = new PackageManager();
		String path = new File("").getAbsolutePath() + "/";
		//pm.constructGraph(path + "shared_dependencies.json");
		//pm.constructGraph(path + "test.json");
		pm.constructGraph(path + "valid.json");
		System.out.println(pm.getInstallationOrder("B").toString());
		System.out.println(pm.toInstall("A", "B"));
		//System.out.println(pm.getInstallationOrderForAllPackages().toString());
		System.out.println(pm.getPackageWithMaxDependencies());
	}

}
