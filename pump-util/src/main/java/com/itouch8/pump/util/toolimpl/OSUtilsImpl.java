package com.itouch8.pump.util.toolimpl;

import static java.awt.Toolkit.getDefaultToolkit;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.util.Tool;


public abstract class OSUtilsImpl {

    private static final OSUtilsImpl instance = new OSUtilsImpl() {};

    private OSUtilsImpl() {}

    
    public static OSUtilsImpl getInstance() {
        return instance;
    }

    
    public String getSystemProperty(String property) {
        try {
            return System.getProperty(property);
        } catch (SecurityException ex) {
            System.err.println("Caught a SecurityException reading the system property '" + property + "'; the OSToolImpl property value will default to null.");
            return null;
        }
    }

    
    public int runOSCommond(String... cmd) {
        return CoreUtils.runOSCommand(cmd);
    }

    
    public void openFile(File file) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                Throw.throwRuntimeException(e);
            }
        }
    }

    
    public void editFile(File file) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().edit(file);
            } catch (IOException e) {
                Throw.throwRuntimeException(e);
            }
        }
    }

    
    public void browser(URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);;
            } catch (IOException e) {
                Throw.throwRuntimeException(e);
            }
        }
    }

    
    public void copyToClipboard(String str) {
        StringSelection copyItem = new StringSelection(str);
        Clipboard clipboard = getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(copyItem, null);
    }

    
    public String getStringFromClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable paste = clipboard.getContents(null);
        if (paste == null) {
            return null;
        }
        try {
            return (String) paste.getTransferData(DataFlavor.stringFlavor);
        } catch (Exception ex) {
            return null;
        }
    }

    
    public boolean checkClassInProcess(Class<?> cls) throws Exception {
        BufferedReader br = null;
        try {
            BufferedReader b = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("jps -l").getInputStream()));
            String line = null;
            while ((line = b.readLine()) != null) {
                if (line.indexOf(cls.getName()) >= 0) {
                    return true;
                }
            }
            return false;
        } finally {
            Tool.IO.closeQuietly(br);
        }
    }

    
    public int getClassInProcessNum(Class<?> cls) throws Exception {
        BufferedReader br = null;
        try {
            int i = 0;
            BufferedReader b = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("jps -l").getInputStream()));
            String line = null;
            while ((line = b.readLine()) != null) {
                if (line.indexOf(cls.getName()) >= 0) {
                    i++;
                }
            }
            return i;
        } finally {
            Tool.IO.closeQuietly(br);
        }
    }
}
