package com.xplus.commons.swing.lookandfeel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.table.DefaultTableModel;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import com.jtattoo.plaf.AbstractLookAndFeel;

/**
 * @author huzexiong
 *
 */
public class DynamicChangeLookAndFeel {

  public static void changeLaf(JFrame frame, String className) {
    try {
      UIManager.setLookAndFeel(className);
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      JOptionPane.showMessageDialog(frame, e.getMessage());
      e.printStackTrace();
      try {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
          | UnsupportedLookAndFeelException e1) {
        e1.printStackTrace();
      }
    }
    SwingUtilities.updateComponentTreeUI(frame);
    frame.pack();
  }

  public static void main(String[] args) {
    JFrame.setDefaultLookAndFeelDecorated(true);
    JDialog.setDefaultLookAndFeelDecorated(true);
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        final JFrame frame = new JFrame();
        JPanel panel = new JPanel(new GridLayout(3, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, // top
            10, // left
            10, // bottom
            10) // right
        );
        JButton btnDemo = new JButton("Installed LAF");
        btnDemo.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent arg0) {
            List<Class<?>> list = getClasses("org/pushingpixels/substance",
                SubstanceLookAndFeel.class);
            for (Class cls : list) {
              System.out.println(cls.getName());
            }
          }
        });
        JSpinner spnDemo = new JSpinner();
        JComboBox<String> cmbDemo = new JComboBox<String>();
        cmbDemo.addItem("One");
        cmbDemo.addItem("Two");
        cmbDemo.addItem("Three");

        JMenuBar mBar = new JMenuBar();
        frame.setJMenuBar(mBar);

        JMenu menuLookAndFeel = new JMenu("LAF");
        menuLookAndFeel.setToolTipText("Application Look And Feel.");

        ButtonGroup btnGroup = lookAndFeelInstalled(frame, menuLookAndFeel);

        lookAndFeelMenu(frame, menuLookAndFeel, btnGroup, "Substance", "org/pushingpixels/substance",
            SubstanceLookAndFeel.class, new NameHandler() {
              @Override
              public String handle(String name) {
                return name.replaceFirst("org.pushingpixels.substance.api.skin.Substance", "");
              }
            });

        lookAndFeelMenu(frame, menuLookAndFeel, btnGroup, "SeaGlass", "com/seaglasslookandfeel",
            SynthLookAndFeel.class, new NameHandler() {
              @Override
              public String handle(String name) {
                return name.replaceFirst("com.seaglasslookandfeel.", "");
              }
            });
//        lookAndFeelMenu(frame, menuLookAndFeel, btnGroup, "Weblaf", "de/sciss",
//            SynthLookAndFeel.class, new NameHandler() {
//          @Override
//          public String handle(String name) {
//            return name.replaceFirst("de.sciss.", "");
//          }
//        });
        lookAndFeelMenu(frame, menuLookAndFeel, btnGroup, "Napkinlaf", "net/sourceforge/napkinlaf",
            BasicLookAndFeel.class, new NameHandler() {
          @Override
          public String handle(String name) {
            return name.replaceFirst("net.sourceforge.napkinlaf.", "");
          }
        });
        
        lookAndFeelMenu(frame, menuLookAndFeel, btnGroup, "JTattoo", "com/jtattoo/plaf",
            AbstractLookAndFeel.class, new NameHandler() {
          @Override
          public String handle(String name) {
            return name.replaceFirst("com.jtattoo.plaf.", "");
          }
        });

        mBar.add(menuLookAndFeel);

        DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new String[] {
            "First", "Second" });
        model.addRow(new Object[] {
            "Some text 1", "Another text 1" });
        model.addRow(new Object[] {
            "Some text 2", "Another text 2" });
        model.addRow(new Object[] {
            "Some text 3", "Another text 3" });
        model.addRow(new Object[] {
            "Some text 4", "Another text 4" });
        JTable table = new JTable(model);
        panel.add(btnDemo);
        panel.add(spnDemo);
        panel.add(cmbDemo);
        frame.add(panel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        // scrollPane.setBorder(BorderFactory.createEmptyBorder(10, // top
        // 10, // left
        // 10, // bottom
        // 10) // right
        // );
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);

      }

      /**
       * @param frame
       * @param mainMenu
       * @return
       */
      private ButtonGroup lookAndFeelInstalled(final JFrame frame, JMenu mainMenu) {
        JMenu menuSystem = new JMenu("System");
        ButtonGroup btnGroup = new ButtonGroup();
        LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
        for (final LookAndFeelInfo tmp : lafInfo) {
          JRadioButtonMenuItem radioButtonMenuItem = new JRadioButtonMenuItem(tmp.getName());
          btnGroup.add(radioButtonMenuItem);
          menuSystem.add(radioButtonMenuItem);
          radioButtonMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              changeLaf(frame, tmp.getClassName());
            }
          });
        }
        mainMenu.add(menuSystem);
        return btnGroup;
      }

      /**
       * @param frame
       * @param mainMenu
       * @param btnGroup
       */
      private void lookAndFeelMenu(final JFrame frame, JMenu mainMenu, ButtonGroup btnGroup,
          String menuName, String basePackage, Class<?> targetType, NameHandler nameHandler) {
        JMenu menuItemSeaGlass = new JMenu(menuName);
        List<Class<?>> seaGlasses = getClasses(basePackage, targetType);
        for (final Class tmp : seaGlasses) {
          String name = nameHandler.handle(tmp.getName());
          JRadioButtonMenuItem radioButtonMenuItem = new JRadioButtonMenuItem(name);
          btnGroup.add(radioButtonMenuItem);
          menuItemSeaGlass.add(radioButtonMenuItem);
          radioButtonMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              changeLaf(frame, tmp.getName());
            }
          });
        }
        mainMenu.add(menuItemSeaGlass);
      }
    });
  }

  interface NameHandler {
    String handle(String name);
  }

  private static List<Class<?>> getClasses(String packagePath, Class<?> targetType) {
    ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
        true);
    provider.addIncludeFilter(new AssignableTypeFilter(targetType));

    // scan in org.example.package
    Set<BeanDefinition> components = provider.findCandidateComponents(packagePath);
    List<Class<?>> list = new ArrayList<Class<?>>();
    for (BeanDefinition component : components) {
      try {
        list.add(Class.forName(component.getBeanClassName()));
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    return list;
  }

}
