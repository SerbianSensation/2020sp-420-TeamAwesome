// Package name
package tests;

// System imports
import org.junit.Test;

import controller.GUIController;
import main.ErrorHandler;
import model.UMLClass;
import model.UMLClassManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

// Local imports
import views.GUIView;

/**
 * Class to run JUnit tests on the GUI
 * 
 * @author Ryan
 *
 */
public class GUITests {
	/**
	 * Create the GUI environment and the environment is initialized
	 */
	@Test
	public void baseInitialization() {
		GUIView gui = new GUIView();
		assertTrue("Window not null", gui.getWindow() != null);
		assertTrue("Controller not null", gui.getController() != null);
		assertTrue("Diagram exists", gui.getDiagram() != null);
	}
	
	/**
	 * Ensure the initialization of the mouse menu and its menu items
	 */
	@Test
	public void mouseMenuInitializations() {
		GUIView gui = new GUIView();
		JComponent mouseMenu = gui.getComponent("Mouse Menu");
		assertTrue("Main mouse menu exists", mouseMenu != null);
		assertTrue("Main mouse menu is popup menu", mouseMenu instanceof JPopupMenu);
		
		// Test MenuItems' initialization
		Component[] mouseMenuChildren = mouseMenu.getComponents();
		assertTrue("Main mouse menu not empty", mouseMenuChildren.length != 0);
		assertEquals("Main mouse menu number of items", 4, mouseMenuChildren.length);
		assertEquals("Main mouse menu first child class add", "Add Class" , ((JMenuItem)mouseMenuChildren[0]).getName());
		assertTrue("Main mouse menu second child separator", mouseMenuChildren[1] instanceof JSeparator);
		assertEquals("Main mouse menu third child save", "Save to File" , ((JMenuItem)mouseMenuChildren[2]).getName());
		assertEquals("Main mouse menu fourth child load", "Load File" , ((JMenuItem)mouseMenuChildren[3]).getName());
	}
	
	/**
	 * Ensure the initialization of the class mouse menu and its items
	 */
	@Test
	public void classMenuInitialization() {
		GUIView gui = new GUIView();
		JComponent classMenu = gui.getComponent("Class Menu");
		assertTrue("Class menu exists", classMenu != null);
		assertTrue("Class menu is popup menu", classMenu instanceof JPopupMenu);

		// Test MenuItems' initialization
		Component[] classMenuChildren = classMenu.getComponents();
		assertTrue("Class menu not empty", classMenuChildren.length != 0);
		assertEquals("Class menu number of items", 13, classMenuChildren.length);
		assertEquals("Class menu first child", "Remove Class" , ((JMenuItem)classMenuChildren[0]).getName());
		assertEquals("Class menu second child", "Edit Class Name", ((JMenuItem)classMenuChildren[1]).getName());
		assertTrue("Class menu third child separator", classMenuChildren[2] instanceof JSeparator);
		assertEquals("Class menu fourth child", "Add Field" , ((JMenuItem)classMenuChildren[3]).getName());
		assertEquals("Class menu child 5", "Remove Field" , ((JMenuItem)classMenuChildren[4]).getName());
		assertEquals("Class menu child 6", "Edit Field Name" , ((JMenuItem)classMenuChildren[5]).getName());
		assertTrue("Class menu child 7 separator", classMenuChildren[6] instanceof JSeparator);
		assertEquals("Class menu child 8", "Add Method" , ((JMenuItem)classMenuChildren[7]).getName());
		assertEquals("Class menu child 9", "Remove Method" , ((JMenuItem)classMenuChildren[8]).getName());
		assertEquals("Class menu child 10", "Edit Method Name" , ((JMenuItem)classMenuChildren[9]).getName());
		assertTrue("Class menu child 11 separator", classMenuChildren[10] instanceof JSeparator);
		assertEquals("Class menu child 12", "Add Relationship" , ((JMenuItem)classMenuChildren[11]).getName());
		assertEquals("Class menu child 13", "Remove Relationship" , ((JMenuItem)classMenuChildren[12]).getName());
	}
	
	/**
	 * Test the addition of classes to the GUI
	 */
	@Test
	public void addClass() {
		UMLClassManager model = new UMLClassManager();
		GUIView gui = new GUIView(new GUIController(model), model);
		
		gui.loadData(new String[] {"myclass"});
		((JMenuItem)gui.getComponent("mouseAddClass")).doClick();
		assertEquals("Add class normal exit code", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Add class normal exist check", null, model.getClass("myclass"));
		assertEquals("Number of classes after single add", 1, model.getClassNames().length);
		
		gui.loadData(new String[] {"secondclass"});
		((JMenuItem)gui.getComponent("mouseAddClass")).doClick();
		assertEquals("Add class normal exit code 2", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Add class normal exist check 2", null, model.getClass("secondclass"));
		assertEquals("Number of classes after second add", 2, model.getClassNames().length);
		
		gui.loadData(new String[] {"3*29"});
		((JMenuItem)gui.getComponent("mouseAddClass")).doClick();
		assertNotEquals("Add class invalid name error code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Add class invalid not exists check", null, model.getClass("3*29"));
		assertEquals("Number of classes after invalid add", 2, model.getClassNames().length);
		
		gui.loadData(new String[] {"myclass"});
		((JMenuItem)gui.getComponent("mouseAddClass")).doClick();
		assertNotEquals("Add class duplicate exit code", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Add class duplicate exist check", null, model.getClass("myclass"));
		assertEquals("Number of classes after single add", 2, model.getClassNames().length);
	}
	
	/**
	 * Test the removal of classes from the GUI
	 */
	@Test
	public void removeClass() {
		UMLClassManager model = new UMLClassManager();
		// Assuming add class works
		model.addClass("myclass");
		model.addClass("secondclass");
		GUIView gui = new GUIView(new GUIController(model), model);
		
		assertEquals("Number of classes prior to remove", 2, model.getClassNames().length);
		
		gui.loadData(new String[] {"myclass"});
		((JMenuItem)gui.getComponent("mouseRemoveClass")).doClick();
		assertEquals("Remove class normal exit code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Remove class normal exist check", null, model.getClass("myclass"));
		assertEquals("Number of classes after normal remove", 1, model.getClassNames().length);
		
		gui.loadData(new String[] {"secondclass"});
		((JMenuItem)gui.getComponent("mouseRemoveClass")).doClick();
		assertEquals("Remove class normal exit code 2", 0, ErrorHandler.LAST_CODE);
		assertEquals("Remove class normal exist check 2", null, model.getClass("secondclass"));
		assertEquals("Number of classes after normal remove 2", 0, model.getClassNames().length);
		
		gui.loadData(new String[] {"notreal"}); // Like birds
		((JMenuItem)gui.getComponent("mouseRemoveClass")).doClick();
		assertNotEquals("Remove class not exist", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Number of classes after invalid remove", 0, model.getClassNames().length);
	}
	
	/**
	 * Test the editing of classes
	 */
	@Test
	public void editClass() {
		UMLClassManager model = new UMLClassManager();
		// Assuming add class works
		model.addClass("myclass");
		model.addClass("secondclass");
		GUIView gui = new GUIView(new GUIController(model), model);
		
		assertEquals("Number of classes prior to edit", 2, model.getClassNames().length);
		
		gui.loadData(new String[] {"myclass", "changedlol"});
		((JMenuItem)gui.getComponent("mouseEditClass")).doClick();
		assertEquals("Edit class normal exit code", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Edit class new name exist check", null, model.getClass("changedlol"));
		assertEquals("Number of class post name change", 2, model.getClassNames().length);
		
		gui.loadData(new String[] {"changedlol", "myclass"});
		((JMenuItem)gui.getComponent("mouseEditClass")).doClick();
		assertEquals("Edit class normal exit code 2", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Edit class new name exist check 2", null, model.getClass("myclass"));
		assertEquals("Number of class post name change", 2, model.getClassNames().length);

		gui.loadData(new String[] {"myclass", "n*tg*(dnamebird"});
		((JMenuItem)gui.getComponent("mouseEditClass")).doClick();
		assertNotEquals("Edit class invalid new name exit code", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Edit class invalid old name still exists", null, model.getClass("myclass"));
		assertEquals("Edit class invalid new name does not exist", null, model.getClass("n*tg*(dnamebird"));
		assertEquals("Number of class post invalid name change", 2, model.getClassNames().length);
		
		gui.loadData(new String[] {"myclass", "secondclass"});
		((JMenuItem)gui.getComponent("mouseEditClass")).doClick();
		assertNotEquals("Edit class invalid new name exit code 2", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Edit class invalid old name still exists 2", null, model.getClass("myclass"));
		assertNotEquals("Edit class invalid new name still exists", null, model.getClass("secondclass"));
		assertEquals("Number of class post invalid name change", 2, model.getClassNames().length);
		
		gui.loadData(new String[] {"notreal", "likebirds"});
		((JMenuItem)gui.getComponent("mouseEditClass")).doClick();
		assertNotEquals("Edit class invalid old name exit code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Edit class invalid old name still not exist", null, model.getClass("notreal"));
		assertEquals("Edit class invalid new name not exists", null, model.getClass("likebirds"));
		assertEquals("Number of class post invalid name change", 2, model.getClassNames().length);
	}
	
	/**
	 * Test the addition of fields to classes
	 */
	@Test
	public void addField() {
		UMLClassManager model = new UMLClassManager();
		// Assuming add class works
		model.addClass("myclass");
		UMLClass myclass = model.getClass("myclass");
		GUIView gui = new GUIView(new GUIController(model), model);
		
		assertEquals("Number of fields initial", 0, myclass.getFields().size());
		
		gui.loadData(new String[] {"myclass", "int", "myfield"});
		((JMenuItem)gui.getComponent("mouseAddField")).doClick();
		assertEquals("Add field valid exit code", 0, ErrorHandler.LAST_CODE);
		assertTrue("Add field valid has field", myclass.hasField("myfield"));
		assertEquals("Add field valid num fields", 1, myclass.getFields().size());
		
		gui.loadData(new String[] {"myclass", "UMLClass", "another"});
		((JMenuItem)gui.getComponent("mouseAddField")).doClick();
		assertEquals("Add field valid exit code 2", 0, ErrorHandler.LAST_CODE);
		assertTrue("Add field valid has field 2", myclass.hasField("another"));
		assertEquals("Add field valid num fields 2", 2, myclass.getFields().size());
		
		gui.loadData(new String[] {"myclass", "double", "myfield"});
		((JMenuItem)gui.getComponent("mouseAddField")).doClick();
		assertNotEquals("Add field duplicate name diff type", 0, ErrorHandler.LAST_CODE);
		assertTrue("Add field original still exists", myclass.hasField("myfield"));
		assertEquals("Add field duplicate num fields", 2, myclass.getFields().size());
		
		gui.loadData(new String[] {"myclass", "int", "m@lf0rm*dn@me"});
		((JMenuItem)gui.getComponent("mouseAddField")).doClick();
		assertNotEquals("Add field invalid exit code", 0, ErrorHandler.LAST_CODE);
		assertFalse("Add field invalid does not exist", myclass.hasField("m@lf0rm*dn@me"));
		assertEquals("Add filed invalid num fields", 2, myclass.getFields().size());
	}
}
